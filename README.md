apljax
======

Application Programmatic Listing for JAX-RS - pronounced Apple Jacks (R)

You can think of apljax as a navigation system for your REST API.  It will scan the classpath
for your JAX-RS annotations and build a nested tree structure fully documenting your API that
can be easily serialized to JSON, XML or a custom output format.  The builder output can be
restricted based on the security permissions of the requesting principal so that only the
REST calls and methods that the user has access to are reported.

Dependencies:

  apljax needs the following jars to operate:

  annovention-1.7.jar or better

  javassist-3.18.0-GA.jar or better

  slf4j-api-1.7.6.jar is used for logging

  You can copy the dependencies out of the pom.xml

Getting started:

- mvn clean install to build the apljax jar and source jar
- Add the apljax jar to your JAX-RS application's classpath
- Add depencies to your pom.xml
- Review src/test/java/apljax/BuilderDump.java for info on using the DefaultBuilder

more to come...