# whitespace-maven-plugin

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
        <version>1.1.0</version>
        <executions>
            <execution>
                <phase>process-sources</phase>
                <goals>
                    <goal>trim</goal>
                </goals>
            </execution>
        </executions>
    </plugin>

## Options

- ```projectBasedir``` is defaulted to ${project.basedir}/src and can be overridden.  If 'src' is dropped and simply using basedir, do note that multi module build will effective run more than once against module files.
- ```entensions``` A comma delimited list of files to format with default of "css,groovy,html,java,js,json,kt,md,properties,scala,sh,wsdl,xhtml,xml,xsd,yaml,yml".

## Requirements ##

- Requires java 8 or better
- Requires maven 3.3.9 or better

## Forked usage ##

This plugin was originally released [here](https://github.com/dantwining/whitespace-maven-plugin/) and later forked and released [here](https://github.com/yusiwen/whitespace-maven-plugin).  In order to modernize it with newer maven usage, its now released against [here](https://github.com/hazendaz/whitespace-maven-plugin) and will be properly maintained going forwards.

All items noted on the original and fork will be looked at for addressing here.  This will be updated to note what has been applied from the upstreams once completed.

Completed patches requested from upstreams.

- https://github.com/dantwining/whitespace-maven-plugin/issues/14
- https://github.com/dantwining/whitespace-maven-plugin/issues/11
