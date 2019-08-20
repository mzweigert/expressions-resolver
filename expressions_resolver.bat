@echo off
IF EXIST ./target/expressions-resolver*.jar (
   mvn -DskipTests exec:java -Dexec.args="%*" -q
) ELSE (
    mvn clean install -DskipTests exec:java -Dexec.args="%*" -q
)