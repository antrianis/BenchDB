#test case / total ops / total nodes /  neo4j or mysql / mysqlSchema


#COUNTER=0
#while [  $COUNTER -lt 10 ]; do
#            let COUNTER=COUNTER+1 


parameters=("5" "1" "10000" "neo4j" "0")  

echo "Executing BenchMark:"

echo "Test Case:" ${parameters[0]} 

echo "Total Operations:" ${parameters[1]} 

echo "REMEMBER TO SYNCHRONISE THIS VALUE Total Nodes:"  ${parameters[2]}

echo "DB:" ${parameters[3]}

#./statistics.sh

cd BenchGraph/

java -cp lib/mysql-connector-java-5.1.17-bin.jar:lib/neo4jDriver.jar:bin/:lib/gson-1.7.1.jar benchmark.BenchMarkSuite ${parameters[0]} ${parameters[1]} ${parameters[2]} ${parameters[3]} ${parameters[4]}


#cd /data
#done

#sudo mount -t tmpfs -o size=1024M tmpfs /tmp/ramdisk

cd ..


parameters=("5" "1" "10000" "mysql" "1")  

echo "DB:" ${parameters[3]}

#./statistics.sh

cd BenchGraph/

java -cp lib/mysql-connector-java-5.1.17-bin.jar:lib/neo4jDriver.jar:bin/:lib/gson-1.7.1.jar benchmark.BenchMarkSuite ${parameters[0]} ${parameters[1]} ${parameters[2]} ${parameters[3]} ${parameters[4]}

