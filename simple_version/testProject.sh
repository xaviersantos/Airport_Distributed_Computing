#!/bin/bash

for i in $(seq 1 10000)
do
  echo -e "\nRun n.º " $i
  java -jar out/artifacts/project_SD_jar/project_SD.jar
done
