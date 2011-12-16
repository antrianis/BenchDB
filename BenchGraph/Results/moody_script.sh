#!/bin/sh 
                #type="echo $a | cut -d '_' -f1"

                gnuplot <<- EOC
                set xlabel "Load"
                set ylabel "$5"
                set terminal postscript mono "Helvetica" 24 eps enhanced 

                set output "$4" 
                plot "< paste $1 $2 $3" u 1:2 w lines lc  rgb "black" title "MySQL", \
                     "< paste $1 $2 $3" u 1:3 w lines lc  rgb "green" title "Neo4J"
                replot

                EOC
~                                         
