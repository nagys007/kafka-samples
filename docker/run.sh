#!/usr/bin/env bash

docker-machine ls --filter name=confluent

if [ $(docker-machine status confluent) != "Running" ]
then
  docker-machine start confluent

  docker-machine ls --filter name=confluent
fi

docker-machine env confluent
eval $(docker-machine env confluent)

docker system prune

docker-compose stop

docker-compose up -d

sleep 3

echo "Checking zookeeper ..."
echo "stat" |nc confluent 2181
