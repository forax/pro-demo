# pro-demo
a simple demo of how to use [pro](https://github.com/forax/pro)

## Foreword 

before starting, be sure to have pro in your PATH,
```
  export PATH=$PATH:/path/to/pro/bin/pro
```

# A simple modular application

Let say we want to create a simple application that is able to print some messages.
These messages can be printed on the console, using a logger, etc, so we want to be able at deployment time to choose
which kind of of Printer should be used.

For the demo, let's pretend that it's a good idea to define 3 modules
 - the main module *printer.main*, which contains the main
 - the API module *printer.api*, which defines the interface Printer
 - the factory module *printer.logger* that provide one implementation of the Printer interface using the java.logging API.
 
On disk, the layout is the following
```
 src
   main
     java
       printer.main        <- module name
         module-info.java  <- module descriptor
         com
           acme
             printer
               main        <- package name
                 Main.java
       printer.api         
         module-info.java
         com    
           acme
             printer
               api
                 Printer.java
       printer.logger
         module-info.java
         com
           acme
             printer
               logger
                 PrinterFactory.java      
```
the folder [src/main/java](src/main/java) contains the 3 modules *printer.main*, *printer.api* and *printer.logger*

## Module printer.api

The module-info.java defines the *modules required* (using the directive __requires__) and the *package exported*
(using the directive __exports__).

Here, there is no module required (*java.base* is required by default) so the
[module-info.java](src/main/java/printer.main/module-info.java) of *printer.api*
only contains one directive __exports__
```
module printer.api {
  exports com.acme.printer.api;
}
```

## Module printer.main

Again, the module-info defines the module __requires__ and the packages __exported__.
Here, the module *printer.main* uses the interface *com.acme.printer.api.Printer* so it requires the module *printer.api*, i.e. the module that contains the package *com.acme.printer.api*.
We do not declare the package *com.acme.printer.main* as exported, so it will be non visible for the other modules.

Furthermore, we use the __service__ mechanism provided by the module descriptor. If a module declare a directive __uses__ with an interface (a __service__ interface), it can use the class *java.util.ServiceLoader* to discover at runtime all the implementations of the interface (the implementation of the __service__).

So here, the Main class will use the ServiceLoader with the interface *com.acme.printer.api.Printer*.
```
module printer.main {
  requires printer.api;
  
  uses com.acme.printer.api.Printer;
}
```

## Module printer.logger

Here, we want to implements the interface *com.acme.printer.api.Printer* which is defined in the module *printer.api* and we want to implement it using the *java.util.logging* API (defined in the module *java.logging*).
So the [module-info](rc/main/java/printer.logger/module-info.java)
__requires__ *java.logging* and *printer.api*.
We also want the class *com.acme.printer.logger.PrinterFactory* to be visible by the other modules, so we declare the package *com.acme.printer.logger* as exported.

Because,the class *com.acme.printer.logger.PrinterFactory* is public and exported and
this class has a public method (named 'provider') that uses a type from the module *printer.api*,
we declare *printer.api* with the directive __requires transitive__ so if a module __requires__ *printer.logger*, it will not have to also __requires__ *printer.api*.  

This module also __provides__ an implementation of the __service__ *com.acme.printer.api.Printer*, we use the directive __provides__ to indicate the class *com.acme.printer.logger.PrinterFactory* that will act as a factory that provides an implementation of the interface *com.acme.printer.api.Printer*.
```
module printer.logger {
  requires java.logging;
  requires transitive printer.api;
  
  exports com.acme.printer.logger;
  
  provides com.acme.printer.api.Printer
    with com.acme.printer.logger.PrinterFactory;
}
```

# Using the REPL

Perhaps the simplest way to use pro is to use is to use its REPL,
```
$ pro shell
```

by default you need to import all the commands of __pro__ that are defined as functions inside the class *com.github.forax.pro.Pro*
```
jshell> import static com.github.forax.pro.Pro.*
```

now, you can use the function 'run' to run the different plugins of __pro__, by example
```
jshell> run("compiler")
```
will run the java compiler on all the sources inside the folder *src/main/java*.

The result on your console should be something like that
```
[pro] registered plugins compiler, convention, linker, modulefixer, packager, resolver, uberpackager
[pro] DONE !          elapsed time 1,064 ms
  
```
The classes of the modules generated by the compiler are stored in *target/src/exploded*.
From the REPL, you can get all the class files using the function 'files', 'location' and 'perlRegex'.
```
jshell> files(location("target/src/exploded"), perlRegex(".*\\.class"))  
``` 

The result is a Java list of all paths inside the folder *printer.main*. 
```
$3 ==> [target/src/exploded/printer.logger/module-info.class, ...]
```

Now, instead of just compiling, we can compile and package all the classes in modular jars
```
jshell> run("compiler", "packager")
```

The result is stored in folder *target/src/artifact*
```
jshell> files(location("target/src/artifact"))
$6 ==> [target/src/artifact, target/src/artifact/printer.logger-1.0.jar, ...]
```

If we want to run the application, we first need to declare the version of the module and which module declares the main class by setting the value of the attribute *packager.moduleMetadata* 
```
set("packager.moduleMetadata", list("printer.main@1.0/com.acme.printer.main.Main"))
```

And then re-package the application and run it
```
jshell> run("compiler", "packager", "runner")

```

Congratulation, you have run your first modular application with __pro__ !

To exit the shell
```
\exit
```

# Using the command line

You can record all the build information info one JSON file named __build.json__
```
{
  packager: {
    moduleMetadata: [
      "printer.main@1.0/com.acme.printer.main.Main",
      "printer.api@1.0",
      "printer.factory@1.0",
    ]
  },
  run: ["compiler", "packager", "runner"]
}
```

And just run it
```
$ pro 
Dec 24, 2016 3:23:04 PM com.acme.printer.main.Main main
INFO: hello world !
[pro] DONE !          elapsed time 1,590 ms
```


# Using the daemon mode

In daemon mode, __pro__ asks the file system to call __pro__ when one file among the inputs of the first plugin
specified by 'run' (here the inputs of *compiler*) has changed, in that case, __pro__ will run all the specified plugin.

```
$ pro daemon
```

By example if a file change in src/main/java
```
$ touch src/main/java/printer.main/module-info.java
```

pro will run all the plugins specified in __build.json__.
```
Dec 24, 2016 3:15:09 PM com.acme.printer.main.Main main
INFO: hello world !
[daemon] DONE !
```

You can stop pro using Control C !

