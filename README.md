# JavaScript Templates for Java (jstemplates4j)
This project is an umbrella project which aims to run various popular JavaScript templates for the JVM.

At present the script engine from Java 8 - Nashorn is leveraged to power various JavaScript templates to run on the JVM. As the version numbers would suggest, this is still a work in progress.

The following JavaScript templates are supported:

1. [Handlebars](http://handlebarsjs.com)
2. [Dust](http://www.dustjs.com)

The following JavaScript templates being considered to be supported in the near future:

* [Underscore](http://underscorejs.org)
* [Backbone](http://backbonejs.org)
* [React](https://facebook.github.io/react)

## Features
* Lightweight - each library is just few KBs.
* Least dependencies - except for dependency on SLF4J api, every sub-project is mostly self-contained and requires no additional dependencies.
* Fast - runs on the Nashorn script engine which is fast.
* OSGi Ready - all the sub-projects are valid OSGi bundles.

## Getting Started
Building this project requires Java 8 JDK (or higher) and Maven (http://maven.apache.org/) 3.0.0 or later. We recommend to use the latest Maven version.

### Building
This project is not available as a binary in any repository, so you would have to build it. To build the project, it is required to first build the `parent` project independently, since the `parent` POM is not released to any public repository.

	mvn install

Thereafter, the `builder` project (top level directory) can be built which is a reactor project to build all the sub-projects in this umbrella project.

	mvn clean install

### Using
This project requires JRE 8 (or higher) to run. All the compilers follow similar API for basic functioning.

*Handlebars Example:*
```java
JSTemplateCompiler compiler = new Handlebars();
JSTemplate template = compiler.compile("Hello {{this}}"); // follow the templating language syntax
System.out.println(template.renderWithData("Data Object for JSTemplate"));

template = compiler.compile("Hello {{language}}"); // follow the templating language syntax
System.out.println(template.renderWithJSON(template, "{ \"language\" : \"Handlebars for Java\"}"));
```

*Dust Example:*
```java
JSTemplateCompiler compiler = new Dust();
JSTemplate template = compiler.compile("Hello {.}"); // follow the templating language syntax
System.out.println(template.renderWithData("Data Object for JSTemplate"));

JSTemplate template = compiler.compile("Hello {language}"); // follow the templating language syntax
System.out.println(template.renderWithJSON(template, "{ \"language\" : \"Dust for Java\"}"));
```

Follow the README and documentation for the respective sub-projects for specific usage.

## Contributing
This project is in very early stages and may not be fully compatible to its JavaScript counterpart, although a genuine effort has been made to enable the major functionalities available for the given templating framework.

I would encourage you to report bugs by creating issues in Github against this project, and submit bug-fixes & improvements by submitting pull requests.

## License & Philosophy
This project is licensed under [Apache 2.0 license](http://www.apache.org/licenses/LICENSE-2.0). Although not required by the license, I would appreciate if you give me some credit when you fork it or copy major portions of it. Use this as a library or copy portions of code to yours, tweak it and open source what you create! Feedback for this work is most welcome. I am doing this for some credit and to learn.