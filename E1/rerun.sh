#!/bin/bash

re='^[0-9]+$' 

if [[ !( $1 =~ $re ) || ( $1 -le 0 ) ]] 
then 
	echo "Error: Invalid K"
	exit 1
fi
if [[ !($2 =~ $re) || ( $2 -le 0 ) ]] 
then
	echo "Error: invalid max iteration "
	exit 1
fi

if [[ ($3 < 0) && ($3 > 1) ]] 
then 
	echo "Error: Invalid delta"
	exit 1
fi

if [[ "$4" != "cosine" && "$4" != "euclidean" ]]
then 
	echo "Error: Distance is neither cosine or euclidean"
	exit 1
fi

if [[ "$5" != "random" && "$5" != "partition" ]]
then 
	echo "Error: Distance is neither cosine or euclidean"
	exit 1
fi

if [[ ! -f $6 ]]
then 
	echo "Error: input file does not exist"
	exit 1
fi

if [[ -f $7 ]]
then 
	echo "Error: output file exist"
	exit 1
fi

java -jar target/kmeans.jar $1 $2 $3 $4 $5 $6 > $7
