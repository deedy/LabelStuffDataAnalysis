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

>"ObjectCount.txt"
images=`cat *.txt`;
total=`echo "$images" | wc -l`;
for ((k=1;k<=$total;k++))
do
curr_label=`echo "$images" | head -n $k | tail -n 1`;
curr_image=`echo $curr_label | grep -Eo '[[:alpha:]].{1,}[[:alpha:]]' | tr '[:upper:]' '[:lower:]' | tr -d ' '`;
# echo "!"$curr_image"!"
no_times=`echo $curr_label | grep -Eo '[[:digit:]]{1,}'`;
# echo "!"$no_times"!"x
posi=$(contains "${a[@]}" $curr_image);
# echo $posi
if [ $posi -gt -1 ]; then
    ((b[${posi}]+=no_times))
#    echo ${b[@]}
else
    a[${#a[@]}]=$curr_image;
    # c[${#c[@]}]=`echo $curr_obj | grep -Eo '<name>[^<]{1,}</name>' | sed -E s/'<\/{0,}name>'//g`;
    b[${#b[@]}]=$no_times;
fi
done
for ((k=1;k<${#b[@]};k++))
do
echo -e ${b[$k]}"\t"${a[$k]} >> "ObjectCount.txt"
done
