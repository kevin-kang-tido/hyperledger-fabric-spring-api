#!/bin/bash

message="$1"
# shellcheck disable=SC1019
if [ -z "$message" ]; then
  echo " Usage: $0 < Forget to commit !>  "
  exit 1
fi

git add . 
git commit -m "$message "
git push -u origin $(git branch --show-current)