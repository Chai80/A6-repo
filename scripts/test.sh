#!/bin/bash

SCENARIO=$1

mkdir -p ./test_results
docker run --mount type=bind,source="$(pwd)"/db,target=/usr/src/cs6310/db --mount type=bind,source="$(pwd)"/output,target=/usr/src/cs6310/output --name streamingwars gatech/streamingwars sh -c "\
    java -cp ./lib/*:streaming_wars.jar Main < commands_${SCENARIO}.txt > stream_test_${SCENARIO}_results.txt 2>&1"
docker cp streamingwars:/usr/src/cs6310/stream_test_${SCENARIO}_results.txt ./test_results/
docker rm streamingwars > /dev/null
