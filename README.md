## Soklet Tomcat

#### What Is It?

[Tomcat](http://tomcat.apache.org) integration for [Soklet](http://soklet.com), a minimalist infrastructure for Java webapps and microservices.

**Note:** Soklet Tomcat is under active development and will be ready for production use soon.

#### License

[Apache 2.0](https://www.apache.org/licenses/LICENSE-2.0)

#### Maven Installation

```xml
<dependency>
  <groupId>com.soklet</groupId>
  <artifactId>soklet-tomcat</artifactId>
  <version>0.1.0-SNAPSHOT</version>
</dependency>
```

#### Direct Download

Coming soon
<!--
If you don't use Maven, you can drop [soklet-tomcat-1.0.0.jar](http://central.maven.org/maven2/com/soklet/soklet-tomcat/1.0.0/soklet-tomcat-1.0.0.jar) directly into your project.  You'll also need [Tomcat](http://tomcat.apache.org) as a dependency. -->

## Example Code

```java
// Assumes you're using Guice as your DI framework via soklet-guice
public static void main(String[] args) throws Exception {
  Injector injector = createInjector(Modules.override(new SokletModule()).with(new AppModule()));
  Server server = injector.getInstance(Server.class);
  server.start();
  System.in.read(); // Wait for keypress
  server.stop();
}

class AppModule extends AbstractModule {
  @Inject
  @Provides
  @Singleton
  public Server provideServer(InstanceProvider instanceProvider) {
    // We'll have Tomcat be our Soklet server
    return TomcatServer.forInstanceProvider(instanceProvider).port(8080).build();
  }
}
```