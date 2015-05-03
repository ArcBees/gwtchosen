[![Dependency Status](https://www.versioneye.com/user/projects/54de6d40271c93aa12000006/badge.svg?style=flat)](https://www.versioneye.com/user/projects/54de6d40271c93aa12000006)

##GwtChosen
[Chosen](https://github.com/harvesthq/chosen) is a javascript plugin (for jQuery and Prototype) _that makes long, unwieldy select boxes much more user-friendly._ GwtChosen is a port of the jquery version of Chosen for GWT Web Toolkit. It is not a wrapper but a complete rewrite using the GWT standards. It is available as a GwtQuery plugin or as a widget.

##Documentation
* [For documentation and examples](http://arcbees.github.com/gwtchosen/)

##Demo
* [Widget Sample](http://arcbees.github.com/gwtchosen/widgetsample/index.html)

##Stable version
* [Maven Central](http://search.maven.org/#search%7Cga%7C1%7Ccom.arcbees.gwtchosen)

##Maven Configuration

### Release:
```xml
<dependency>
    <groupId>com.arcbees</groupId>
    <artifactId>gwtchosen</artifactId>
    <version>2.1</version>
    <scope>compile</scope>
</dependency>
```

### Snapshot:
```xml
<repositories>
    <repository>
        <id>sonatype.snapshots</id>
        <name>Sonatype snapshot repository</name>
        <url>https://oss.sonatype.org/content/repositories/snapshots/</url>
        <layout>default</layout>
    </repository>
</repositories>
<dependency>
    <groupId>com.arcbees</groupId>
    <artifactId>gwtchosen</artifactId>
    <version>2.2.0-SNAPSHOT</version>
    <scope>compile</scope>
</dependency>
```

#Contributor

##IDE Setup
* Create a git clone.
* Import the project using Maven.
* General IDEA and Eclipse project import instructions can be found [here](http://c.gwt-examples.com/home/maven/ide-import).
* If using Eclipse double check the GwtChosen GPE plugin and see if GWT is enabled.

##Debugging integration tests locally
1. `cd integration-test`
2. `mvn gwt:run -Pintegration-test`
3. Open your browser to `http://127.0.0.1:8080/#{test case token}`

##Uploading docs and demos
0. Add your Github credentials to `~/.m2/settings.xml`
1. `cd sample`
2. `mvn clean install site`

This will upload the docs site to branch `gh-pages` to be served at `http://arcbees.github.io/gwtchosen`.

##Credits
* The initial chosen javascript plugin was built by [Harvest](http://www.getharvest.com/).
* Concept and development by [Patrick Filler](http://patrickfiller.com/).
* Design and CSS by [Matthew Lettini](http://matthewlettini.com/)
* The GWT port of Chosen was built by [Julien Dramaix](https://plus.google.com/u/0/103916508880440628637)

##Thanks to
[![Arcbees.com](http://i.imgur.com/HDf1qfq.png)](http://arcbees.com)

[![Atlassian](http://i.imgur.com/BKkj8Rg.png)](https://www.atlassian.com/)

[![IntelliJ](https://lh6.googleusercontent.com/--QIIJfKrjSk/UJJ6X-UohII/AAAAAAAAAVM/cOW7EjnH778/s800/banner_IDEA.png)](http://www.jetbrains.com/idea/index.html)
