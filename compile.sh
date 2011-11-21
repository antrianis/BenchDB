#!/bin/sh

cd Neo4jDriver

#javac -cp lib/gson-1.7.1.jar:lib/jta.jar:lib/neo4j-kernel-1.3.jar src/driver/* -d bin/

javac -cp lib/gson-1.7.1.jar:lib/jta.jar:lib/neo4j-kernel-1.3.jar:lib/neo4j-graph-algo-1.3.jar src/driver/* -d bin/ 


jar cf neo4jDriver.jar -C src .

mv neo4jDriver.jar ../BenchGraph/lib/

cd ..
cd BenchGraph 

#javac -cp bin:lib/mysql-connector-java-5.1.17-bin.jar:lib/neo4jDriver.jar:lib/gson-1.7.1.jar:lib/jta.jar:lib/neo4j-kernel-1.3.jar:lib/neo4j-graph-algo-1.3.jar src/GraphFromFile/* -d bin/

javac -cp bin:lib/mysql-connector-java-5.1.17-bin.jar:lib/neo4jDriver.jar:lib/gson-1.7.1.jar:lib/jta.jar:lib/neo4j-kernel-1.3.jar:lib/neo4j-graph-algo-1.3.jar src/*/* -d bin/

#javac -cp bin:lib/mysql-connector-java-5.1.17-bin.jar:lib/neo4jDriver.jar:lib/gson-1.7.1.jar:lib/jta.jar:lib/neo4j-kernel-1.3.jar:lib/neo4j-graph-algo-1.3.jar src/benchmark/* -d bin/
#javac -cp bin:lib/mysql-connector-java-5.1.17-bin.jar:lib/neo4jDriver.jar:lib/gson-1.7.1.jar:lib/jta.jar:lib/neo4j-kernel-1.3.jar:lib/neo4j-graph-algo-1.3.jar src/databases/* -d bin/
#javac -cp bin:lib/mysql-connector-java-5.1.17-bin.jar:lib/neo4jDriver.jar:lib/gson-1.7.1.jar:lib/jta.jar:lib/neo4j-kernel-1.3.jar:lib/neo4j-graph-algo-1.3.jar src/generators/* -d bin/
#javac -cp bin:lib/mysql-connector-java-5.1.17-bin.jar:lib/neo4jDriver.jar:lib/gson-1.7.1.jar:lib/jta.jar:lib/neo4j-kernel-1.3.jar:lib/neo4j-graph-algo-1.3.jar src/graph/* -d bin/


