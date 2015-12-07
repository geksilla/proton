## proton repl dev-server

It's a hacky way to activate [fighweel](https://github.com/bhauman/lein-figwheel).

Before starting server make sure that all **atom** windows are closed. Also try
to figure out is there any atom processes are running and close them.

Ask *lein* to cleanup the build.

```sh
lein clean
```

Now start **figweel**:

```sh
$ lein figweel dev-server
```

Wait until repl loaded.
Now copy file **proton.js** from ```dev_server/proton.js``` into ```plugin/lib/```.

```sh
$ cp -f dev_server/proton.js plugin/lib/proton.js
```

Next open **atom** from ```plugin/lib``` folder. This step is required because
compiled .cljs code try to resolve path injected by **atom**.

```sh
$ cd plugin/lib; atom .
```

Path to ```/your/local/proton/plugin/lib``` will be set to **process.env.PWD**
and path to local compiled *.js* files will be resolved successfully.

When first **atom** window opened you should see **cljs.user=>** prompt on figweel
repl that's mean that you are connected to **atom** and hot code reload works!

Check that repl works fine and connected. Try to execute in REPL:

```
(js/alert "Hello Atom")
```

Now open **proton** folder and start to change files, hit save and enjoy.
