# syslog_client

This is used to demonstrate a tiny bit of the syslog4j functionality.
This client is different that the other one in that the message format created is consistent with
a standard defined in RFC 5424...and yes, it matters to the server getting the log messages.

To build this, use the command:
```bash
mvn package
```

## Running
Since you will need the syslog4j package and your program's jar, the <b>run</b> script was included to help
in getting the jar files and executing the test. Any argument you would give the Logger program you can pass in
on the command line like so:
I those cases do:
```bash
./run here is the message I want to send
```
