.PHONY: all clean run

JAR_NAME = app-1.0-jar-with-dependencies.jar

MAIN_CLASS = Main

all: fat-jar

fat-jar:
	mvn clean package

run: fat-jar
	java -jar target/$(JAR_NAME)

clean:
	mvn clean
