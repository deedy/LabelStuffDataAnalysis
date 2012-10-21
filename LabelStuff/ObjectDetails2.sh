#!/bin/bash

function contains() {
    local n=$#
    local value=${!n}
    for ((i=0;i < $#;i++)) {
        if [ "${!i}" == "${value}" ]; then
	    echo $(($i-1));
            return 0
        fi
    }
    echo -1;
    return 1
}

# i=`ls | grep -E '_1\.xml' | head -n 1`;
# for i in `ls | grep -E '_1\.xml'`
# do
if [ -f $1 ]
then
filename=`echo $1 | grep -Eo '[^\/]{1,}_[[:digit:]]\.xml' | tail -n 1`
if [ `echo $filename | wc -l` == 1 ]
then

i=$filename;
base_name=${i:0:${#i}-6}
echo "Processing file with base name: "$base_name;
>$base_name".txt"
for j in `ls | grep $base_name`
do
objects=`cat $j | sed -E s/'<\/{0,}object>'/'!'/g | grep -Eo '![^!]{1,}!' | tr -d '!'`;
polygons=`echo "$objects" | grep -Eo '<polygon>.{1,}</polygon>' | grep -Eo '<pt>.{1,}</pt>'`;
total=`echo "$objects" | wc -l`;
for ((k=1;k<=$total;k++))
do
curr_poly=`echo "$polygons" | head -n $k | tail -n 1`;
curr_obj=`echo "$objects" | head -n $k | tail -n 1`;
no_materials=`echo $curr_obj | grep -Eo '<matname>.{1,}</matname>' | wc -l`;
posi=$(contains "${a[@]}" $curr_poly);
if [ $posi -gt -1 ]; then
    ((b[${posi}]+=no_materials))
else
    a[${#a[@]}]=$curr_poly;
    c[${#c[@]}]=`echo $curr_obj | grep -Eo '<name>[^<]{1,}</name>' | sed -E s/'<\/{0,}name>'//g`;
    b[${#b[@]}]=$no_materials;
fi
done
done
for ((k=1;k<${#b[@]};k++))
do
echo -e ${b[$k]}"\t"${c[$k]} >>$base_name".txt"
done
unset a;
unset b;
unset c;
echo -e "Saved processed file as: "$base_name".txt";
fi
fi
