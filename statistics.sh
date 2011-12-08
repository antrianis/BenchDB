
inc=1; 
while true; do
   top -b -n1 > statistics/output${inc}
   sleep 1
   inc=$(($inc+1))
   if inc ge 3; then	
   	exit
   	fi
done