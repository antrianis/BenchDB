# nodes relations

parameters=( "1000" "1000000" "many") 

echo "Importing Graph Data:"
echo "No of nodes:"
echo ${parameters[0]}
echo "No of relations:" 
echo ${parameters[1]} 
echo "Mysql relation schema:"
echo ${parameters[2]}

	
rm Neo4jDriver/db/*

cd BenchGraph/

#create followers file

java -cp bin/ GraphFromFile.graphFileCreator ${parameters[0]} ${parameters[1]} 


java -cp lib/jta.jar:lib/neo4j-kernel-1.3.jar:lib/mysql-connector-java-5.1.17-bin.jar:lib/neo4jDriver.jar:/data/BenchGraph/bin/:lib/gson-1.7.1.jar GraphFromFile.Neo4jGraphCreator ${parameters[0]} 

java -Xmx2g -XX:-UseGCOverheadLimit -cp lib/jta.jar:lib/neo4j-kernel-1.3.jar:lib/mysql-connector-java-5.1.17-bin.jar:lib/neo4jDriver.jar:/data/BenchGraph/bin/:lib/gson-1.7.1.jar GraphFromFile.SQLConnect ${parameters[0]}  ${parameters[2]}
