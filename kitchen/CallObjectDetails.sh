#!/bin/bash

if [ -d $1 ]
then
files=`ls $1 | grep -E '_1\.xml'`;
nofiles=`echo "$files" | wc -l`;
if [ $nofiles -gt 0 ]
then
if [ -f "./ObjectDetails2.sh" ]
then
echo "BEGINNING PROCESSING OF "$nofiles" IMAGE ANNOTATION FILES"

for i in `echo "$files"`
do
./ObjectDetails2.sh $i
done

else

echo "ObjectDetails2.sh does not exist in current directory";

fi

else 
echo $1" does not contain any proper xml files";

fi

else

echo $1" is not a directory";

fi
