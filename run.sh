#!/bin/bash

./package_all.sh &&
  docker compose up --force-recreate --build --remove-orphans
