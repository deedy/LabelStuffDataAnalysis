#!/bin/bash
echo "FileName,NumberLabelled,NumberTotal" > ObjectDetails.csv
for i in *.xml
do
#Putting all Objects annotations in different lines in a string
objects=`cat $i | sed -E s/'<\/{0,}object>'/'!'/g | grep -Eo '![^!]{1,}!' | tr -d '!'`;
#Calculating number of unlabelled objects
num_unlabelled=`echo "$objects" | grep -Eo '<material>undefined</material>' | wc -l`;
#Calculating number of total objects
total_objects=`echo "$objects" | wc -l | tr -d ' '`;
#Calculating number of labelled objects
num_labelled=$(($total_objects-$num_unlabelled));
echo $i,$num_labelled,$total_objects >> ObjectDetails.csv; 
done

