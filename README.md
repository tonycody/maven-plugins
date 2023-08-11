# Maven plug-in list

## sort-maven-plugin

This plug-in sorts the dependencies in the pom.xml file.

Supported goals:

- sort
- check
- clean
- help

JDK 1.8 or later is required.

E.g.:

```xml

<plugin>
    <groupId>io.github.tonycody.maven.plugins</groupId>
    <artifactId>sorter-maven-plugin</artifactId>
    <version>1.0.1</version>
    <executions>
        <execution>
            <goals>
                <goal>sort</goal>
                <goal>clean</goal>
            </goals>
        </execution>
    </executions>
</plugin>
```

# Development Guide

## Deploy

```shell
mvn clean deploy -P distribution
```