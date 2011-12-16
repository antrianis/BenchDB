set terminal postscript mono "Helvetica" 24 eps enhanced 
#oad.txt latency_mysql_2.txt latency_neo4j_2.txt izlaz.eps
		set output "output.eps" 
		plot "< paste load.txt latency_mysql_2.txt latency_neo4j_2.txt" u 1:2 w lines lc  rgb "black" title "MySQL", \
		     "< paste load.txt latency_mysql_2.txt latency_neo4j_2.txt" u 1:3 w lines lc  rgb "green" title "Neo4J"
		replot
