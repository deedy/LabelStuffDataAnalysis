#!bin/bash
echo > ObjectCount.csv
for i in *.xml
do
echo $i,`cat $i | grep  -Eo '<object>' | wc -l | sed 's/^ *//g'` >> ObjectCount.csv
done

