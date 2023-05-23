whitespace-maven-plugin
=======================

[![Java CI](https://github.com/hazendaz/whitespace-maven-plugin/workflows/Java%20CI/badge.svg)](https://github.com/hazendaz/whitespace-maven-plugin/actions?query=workflow%3A%22Java+CI%22)
[![Maven central](https://maven-badges.herokuapp.com/maven-central/com.github.hazendaz.maven/whitespace-maven-plugin/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.hazendaz.maven/whitespace-maven-plugin)
[![Apache 2](http://img.shields.io/badge/license-Apache%202-blue.svg)](http://www.apache.org/licenses/LICENSE-2.0)

![hazendaz](src/site/resources/images/hazendaz-banner.jpg)

The whitespace plugin will remove any trailing whitespace from files in your project.

Default file extensions:

- css
- groovy
- html
- java
- js
- json
- kt
- md
- properties
- scala
- sh
- wsdl
- xhtml
- xml
- xsd
- yaml
- yml

## Usage

The plugin works best if it is run every time during Maven compilation. However, if you wish to perform a simple test of what the plugin does, then run the following within your Maven project:

`mvn com.github.dantwining.whitespace-maven-plugin:whitespace-maven-plugin:trim`

To enable the plugin for every Maven compilation, add the following to your pom and run any maven phase that would include the `process-sources` phase:

    <plugin>
        <artifactId>whitespace-maven-plugin</artifactId>
        <groupId>com.github.dantwining.whitespace-maven-plugin</groupId>
        <version>1.2.0</version>
        <executions>
            <execution>
                <phase>process-sources</phase>
                <goals>
                    <goal>trim</goal>
                </goals>
            </execution>
        </executions>
    </plugin>

To run a verification only, the setup is the same with 'verify' mojo instead.

## Options

- ```projectBasedir``` is defaulted to ${project.basedir}/src and can be overridden.  If 'src' is dropped and simply using basedir, do note that multi module build will effective run more than once against module files.
- ```entensions``` A comma delimited list of files to format with default of "css,groovy,html,java,js,json,jsp,jspx,kt,md,properties,scala,sh,wsdl,xhtml,xml,xsd,yaml,yml".
- ```encoding``` The encoding to use for the project with default set as ```project.build.sourceEncoding```

## Requirements ##

- Requires java 8 or better to run
- Requires maven 3.3.9 or better to run
- Requires java 11 to build
- Requires maven 3.9.2 to build

## Forked usage ##

This plugin was originally released [dantwining](https://github.com/dantwining/whitespace-maven-plugin/) and later forked and released [yusiwen](https://github.com/yusiwen/whitespace-maven-plugin).  In order to modernize it with newer maven usage, its now released against [hazendaz](https://github.com/hazendaz/whitespace-maven-plugin) and will be properly maintained going forwards.

All items from [dantwining](https://github.com/dantwining/whitespace-maven-plugin/) have been linked.  Many have been addressed with a few outstanding as of 5/23/2023.

No outstanding items were on [yusiwen](https://github.com/yusiwen/whitespace-maven-plugin) as issues never opened.  However, this was ahead and everything outside of the following was applied.

- Fork locked down ability to override the basedir breaking from original and was not accepted into my fork.
 
See https://github.com/hazendaz/whitespace-maven-plugin/pull/2 for more details of what was accepted and why this was not between both forks.
