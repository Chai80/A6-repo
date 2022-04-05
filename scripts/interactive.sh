#!/bin/bash

winpty docker run --mount type=bind,source="$(pwd)"/db,target=/usr/src/cs6310/db --mount type=bind,source="$(pwd)"/output,target=/usr/src/cs6310/output --name streamingwars -it gatech/streamingwars sh

docker rm streamingwars > /dev/null
