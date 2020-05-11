(ns leon.computer.repl.reloaded
  (:require
   [com.stuartsierra.component :as component]
   [clojure.tools.namespace.repl :refer [disable-reload! refresh]]))

(defrecord Status []
  component/Lifecycle
  (start [this]
    (assoc this :started? true))
  (stop [this]
    (assoc this :stopped? true)))

(def new-unloaded-system
  (component/system-map
   ::status (->Status)))

(def unloaded-system new-unloaded-system)

(def system unloaded-system)

(defn set-system-to-load!
  [system]
  (alter-var-root #'unloaded-system
                  (constantly
                   (into new-unloaded-system system))))

(defn stop []
  (let [{{:keys [started? stopped?]} ::status} system
        system
        (if (and started?
                 (not stopped?))
          (do (println "[SYSTEM] Stopping...")
              (component/stop-system system))
          (do
            (println "[SYSTEM] Skipping stop: "
                     "Not started or already stopped.")
            system))]
    (alter-var-root #'system (constantly system))))

(defn start []
  (let [{{:keys [started? stopped?]} ::status} system
        system
        (if (and started?
                 (not stopped?))
          (do (println (str "[SYSTEM] Skipping start: "
                            "Still running (Use stop or reset "
                            "for a full reload)"))
              system)
          (do (println "[SYSTEM] Starting...")
              (when (empty? (dissoc unloaded-system ::status))
                (println
                 (str "[SYSTEM] Warning: No system set. "
                      "(Use set-system-to-load!)")))
              (component/start-system unloaded-system)))]
    (alter-var-root #'system (constantly system))))

(defn reset []
  (stop)
  (refresh :after `start))
