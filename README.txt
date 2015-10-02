Cassandra, Node.js, and Angular.js Demo

Hi, this silly little web app was written by Mitchell Kyle as simplistic demo of Apache Cassandra, Node.js and Angular.js. 
It is under the Apache License 2.0 so it is free to explore.

To use:

1. start the database
launch cassadra as follows:
	cd opt/apache-cassandra-2.2.1
	bin/cassandra -f
	
2. seed the database
	cd opt/apache-cassandra-2.2.1
	bin/cqlsh -f ../../cql/seed.cql

3. launch the app
	lein ring server-headless 3000
	
4. browse to http://localhost:3000/
