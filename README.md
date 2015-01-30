##GwtChosen
[Chosen](https://github.com/harvesthq/chosen) is a javascript plugin (for jQuery and Prototype) _that makes long, unwieldy select boxes much more user-friendly._ GwtChosen is a port of the jquery version of Chosen for GWT Web Toolkit. It is not a wrapper but a complete rewrite using the GWT standards. It is available as a GwtQuery plugin or as a widget.

##Documentation
* [For documentation and examples](http://arcbees.github.com/gwtchosen/)

##Demo
* [Widget Sample](http://arcbees.github.com/gwtchosen/widgetsample/index.html)

##Stable version
* [Maven Central](http://search.maven.org/#search%7Cga%7C1%7Ccom.github.jdramaix)

##Maven Configuration
Find the the available jars in [Maven Central](http://search.maven.org/#search%7Cga%7C1%7Ccom.github.jdramaix).

### Release:
```xml
<dependency>
    <groupId>com.github.jdramaix</groupId>
    <artifactId>gwtchosen</artifactId>
    <version>1.2.0</version>
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
    <version>2.0.0-SNAPSHOT</version>
    <scope>compile</scope>
</dependency>
```

##Previous Versions
* [GwtChosen 1.1.0](http://code.google.com/p/gwtquery-plugins/downloads/detail?name=gwtchosen-1.1.0.jar)

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

#FAQ

##Credits
* The initial chosen javascript plugin was built by [Harvest](http://www.getharvest.com/).
* Concept and development by [Patrick Filler](http://patrickfiller.com/).
* Design and CSS by [Matthew Lettini](http://matthewlettini.com/)
* The GWT port of Chosen was built by [Julien Dramaix](https://plus.google.com/u/0/103916508880440628637)
