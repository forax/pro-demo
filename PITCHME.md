@title[pro]
# pro
## A Java 9 compatible build tool

Note: No need to be a maven to be able to use a build tool

+++

- Rémi
- Christian


---

# principles

@ul

- programmatic API first
- use convention over configuration
- stateless plugins
- separate configuration time where configuration is mutable and build time where configuration is immutable
- external dependencies are in plain sight 

@ulend


---

# a simple application

- that print json value using jackson
- here we talk about how pro works
- how to scaffold
- how a `module-info.java` ☕ works
- how to configure for Java 8, etc

+++

###### `module-info.java`

```java
import annotation.A;
import annotation.B;
import com.example.foo.Impl;

@A @B
module com.example.foo {
    requires com.example.foo.http;
    requires java.logging;
    requires transitive com.example.foo.network;

    exports com.example.foo.bar;
    exports com.example.foo.internal to com.example.foo.probe;

    opens com.example.foo.quux;
    opens com.example.foo.internal to com.example.foo.network,
                                      com.example.foo.probe;

    uses com.example.foo.spi.Intf;
    provides com.example.foo.spi.Intf with Impl;
}
```
@[6](Module name)
@[7-9](Name of a **module** on which this module has a dependence)
@[11-12](Name of a **package** to be exported by the this module)
@[14-16](Name of a **package** to be opened by the current module)
@[18-19](Service consumption and provision via `java.util.ServiceLoader`)
@[1-20]

Note:
Java Language Specification says "A module declaration specifies a new named module"

---

# enhanced application
 
- have a client code and a library code,  both are modules,
- we talk about how multi-modules works
- and how the tests works ✅
- how to auto-generate the POM 


---

# spring-ify the application

- use Spring and acts as a server
- which involve to deal with non-modular dependencies
- using the module-fixer, and the linker