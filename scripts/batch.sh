#!/bin/bash

mkdir -p ./test_results
exec &> "./test_results/batch_results.txt"

DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null 2>&1 && pwd )"

rm test_results/*

mkdir -p ./test_results

docker run --mount type=bind,source="$(pwd)"/db,target=/usr/src/cs6310/db --mount type=bind,source="$(pwd)"/output,target=/usr/src/cs6310/output --mount type=bind,source="$(pwd)"/test_results,target=/usr/src/cs6310/test_results --name streamingwars gatech/streamingwars sh -c "\
    ./test_internal.sh"
docker rm streamingwars > /dev/null

wait
