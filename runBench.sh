#!/bin/bash

#test case / total ops / total nodes /  neo4j or mysql / mysqlSchema


#b.testCase = Integer.parseInt(args[0]);
#b.totalOps = Integer.parseInt(args[1]);
#b.totalNodes = Integer.parseInt(args[2]);
#b.db = args[3];
#b.mySqlSchema = Integer.parseInt(args[4]);
#b.clientNumber = Integer.parseInt(args[5]);
#b.maxExecutionTime = Integer.parseInt(args[6]);


parameters=("1" "30" "10000" "neo4j" "0" "1" "1")  


COUNTER=0
while [  $COUNTER -lt 20 ]; do
            let COUNTER=COUNTER+1 

#echo "Executing BenchMark:"

#echo "Test Case:" ${parameters[0]} 

#echo "Total Operations:" ${parameters[1]} 

#echo "REMEMBER TO SYNCHRONISE THIS VALUE Total Nodes:"  ${parameters[2]}

parameters[3]="neo4j";


#echo "DB:" ${parameters[3]}

##./statistics.sh

cd BenchGraph/
#parameters[1]=240;
java -cp lib/mysql-connector-java-5.1.17-bin.jar:lib/neo4jDriver.jar:bin/:lib/gson-1.7.1.jar benchmark.BenchMarkSuite ${parameters[0]} ${parameters[1]} ${parameters[2]} ${parameters[3]} ${parameters[4]} ${parameters[5]} ${parameters[6]}


#cd /data
#done

#sudo mount -t tmpfs -o size=1024M tmpfs /tmp/ramdisk

#cd ..


##parameters=("1" "1000" "1000" "mysql" "1" "1" "3")  

#parameters[3]="mysql";

#echo "DB:" ${parameters[3]}

##./statistics.sh

#cd BenchGraph/

#java -cp lib/mysql-connector-java-5.1.17-bin.jar:lib/neo4jDriver.jar:bin/:lib/gson-1.7.1.jar benchmark.BenchMarkSuite ${parameters[0]} ${parameters[1]} ${parameters[2]} ${parameters[3]} ${parameters[4]} ${parameters[5]} ${parameters[6]}


let parameters[1]=parameters[1]*2 

cd ..

done
