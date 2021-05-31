#!/bin/bash
  
CURDIR="$(dirname $BASH_SOURCE)"

counter=1
for i in $(ls TOPOLOGY_GENERATOR_BASH_NAME*)
do
  if [ $counter -eq 20 ]
  then
    read -p "Press enter to continue..."
    echo "Refresh Token"
    echo ""

    counter=1
  fi

  echo "api post $i..."
  sleep 5
  ((counter++))
  echo ""
  echo ""
done