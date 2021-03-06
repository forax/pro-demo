import static com.github.forax.pro.Pro.*;
import static com.github.forax.pro.builder.Builders.*;
import static java.nio.file.Files.*;
import static com.github.forax.pro.helper.FileHelper.*;

var distribute = command(() -> {
  deleteAllFiles(location("pro-example-springboot"), true);
  delete(location("target/image/bin/com.github.forax.pro.example.springboot"));
  copy(location("deploy-script/pro-example-springboot"), location("target/image/bin/pro-example-springboot"));
  move(location("target/image"), location("pro-example-springboot"));
});

resolver.
    checkForUpdate(true).
    dependencies(   
        // Spring Boot + autoconfigure + Spring Web MVC
        "spring.boot=org.springframework.boot:spring-boot:2.0.2.RELEASE",
        "spring.boot.autoconfigure=org.springframework.boot:spring-boot-autoconfigure:2.0.2.RELEASE",
        "spring.aop:5.0.7.RELEASE",
        "spring.beans:5.0.7.RELEASE", 
        "spring.context:5.0.7.RELEASE",
        "spring.core:5.0.7.RELEASE",
        "spring.expression:5.0.7.RELEASE",
        "spring.jcl:5.0.7.RELEASE",
        "spring.web=org.springframework:spring-web:5.0.7.RELEASE",
        "spring.webmvc=org.springframework:spring-webmvc:5.0.7.RELEASE",
        
        // Tomcat Embeded
        "org.apache.tomcat.embed.core=org.apache.tomcat.embed:tomcat-embed-core:9.0.8",
        "org.apache.tomcat.annotations.api=org.apache.tomcat:tomcat-annotations-api:9.0.8",
        
        // Jackson core + datatype.jdk8
        "com.fasterxml.jackson.core:2.9.6",
        "com.fasterxml.jackson.databind:2.9.6",
        "com.fasterxml.jackson.datatype.jdk8=com.fasterxml.jackson.datatype:jackson-datatype-jdk8:2.9.6",
        "com.fasterxml.jackson.annotations=com.fasterxml.jackson.core:jackson-annotations:2.9.6" /*,*/
    )

packager.
    modules(
        "com.github.forax.pro.example.springboot@1.0/com.github.forax.pro.example.springboot.DemoApplication"
    )   
    
runner.
    rawArguments(
        "--add-opens", "java.base/java.lang=spring.core"
    )    
    
run(resolver, modulefixer, compiler, packager, linker, distribute)

pro.arguments().forEach(plugin -> run(plugin))   // run plugins (runner ?)

/exit
