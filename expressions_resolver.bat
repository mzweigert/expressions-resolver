@echo off
IF EXIST ./target/expressions-resolver*.jar (
   mvn -DskipTests exec:java -Dexec.args="%*"
) ELSE (
    mvn clean install -DskipTests exec:java -Dexec.args="%*"
)