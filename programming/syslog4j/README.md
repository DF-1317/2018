# syslog4j

This package provides a whole lot of functionality with regards to logging and the syslog standard.
For lots of info, [you want to visit their site](http://www.syslog4j.org)

This is a copy taken from github that someone had on their account.
To build this, use the command:
```bash
mvn install
```

This will put the artifact jar into your local repository.

## Testing
The build does lots of testing. If they pass for the most part, but a few do not, your install may not work.
I those cases do:
```bash
mvn -DskipTests install
```
