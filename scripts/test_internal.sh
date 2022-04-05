#!/bin/bash

rm db/users.db

for i in {0..40}
do
  echo " ------ Test Case: $i ----"
  tc="00$i"
  SCENARIO=${tc:(-2):2}
  java -cp ./lib/*:streaming_wars.jar Main < commands_${SCENARIO}.txt > stream_test_${SCENARIO}_results.txt 2>&1
done

rm db/users.db

for i in {41..100}
do
  echo " ------ Test Case: $i ----"
  SCENARIO=$i
  java -cp ./lib/*:streaming_wars.jar Main < commands_${SCENARIO}.txt > stream_test_${SCENARIO}_results.txt 2>&1
done

mv stream_test_*_results.txt ./test_results
