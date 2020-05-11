# leon.computer.repl.reloaded

Reloaded workflow development tool for systems built with [component](https://github.com/stuartsierra/component).  Uses `clojure.tools.namespace.repl` for code reloading.

## Usage

1. Invoke `set-system-to-load!` with a system map representing unloaded state.
2. Invoke `(reset)` whenever you want a full stop/code reload/start
3. Goto 1 when you need to spin up a different system

## Accessing runtime instances
`leon.computer.repl.system/system` has a running or stopped system.  This is where you access runtime instances of loaded components.  Access runtime facilities for dev use via `(get-in leon.computer.repl.reloaded/system [:system <keyword-of-component-you-want>])`.  We recommend to set up little getter fn0s in your dev namespace for quicker access.

## What if `reset` fails?

If `reset` fails due to bad code you should fix your code and hit `reset` again.  If `reset` fails because a start method of a component has thrown, you likely have spawned headless IO facilities and should reboot the JVM.

## `clojure.tools.namespace.repl` gotcha

Sometimes `clojure.tools.namespace.repl` finds too much code like generated ClojureScript source on the classpath. Use `clojure.tools.namespace.repl/set-refresh-dirs` to narrow down the source pathes you want reloaded.

## Other features
- You may explicitly use `start` and `stop` instead of `reset` but usually won't have to.
- `leon.computer.repl.system/system` has a `::status` map with `:started?` and `:stopped?` keys.  Usually you don't need it.

## License

Copyright © 2020 leon.computer UG (haftungsbeschränkt)

Distributed under the Eclipse Public License either version 1.0 or (at your option) any later version.
