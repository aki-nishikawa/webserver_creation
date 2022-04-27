JC = javac

all:	Modoki.class ServerThread.class SendResponse.class MyURLDecoder.class Util.class

Modoki.class:	Modoki.java
	$(JC) $<

ServerThread.class:	ServerThread.java
	$(JC) $<

SendResponse.class:	SendResponse.java
	$(JC) $<

MyURLDecoder.class:	MyURLDecoder.java
	$(JC) $<

Util.class:	Util.java
	$(JC) $<

clean:
	rm *.class

PHONY: all