# expressions-resolver [![Build Status](https://travis-ci.org/mzweigert/expressions-resolver.svg?branch=master)](https://travis-ci.org/mzweigert/expressions-resolver)

## Software needed to run:

* Java: `1.8` [How to install](https://java.com/en/download/help/download_options.xml)
* Maven: `3.3.x` [How to install](https://maven.apache.org/install.html)
* Make sure, that you have set JAVA_HOME and MAVEN_HOME environment variables mentioned in above links.

## How to build project
Make sure, that you have installed maven and java, then in project root folder type:
`mvn clean install -DskipTests`

## How to run tests
Make sure, that you have installed maven and java, then in project root folder type:
`mvn verify -B`

## How to run expressions resolver

usage: (Windows: `expressions_resolver` | Linux/Unix: `./expressions_resolver.sh` ) `<path to input folder>` `<path to output folder>`
<pre>
 &lt;path to input folder&gt; – the folder where input files are located
 &lt;path to output folder&gt; – the folder where results should be stored 
</pre>

## Result files
When resolver finishes work, results are saved as xml files to &lt;path to output folder&gt; given as param.
