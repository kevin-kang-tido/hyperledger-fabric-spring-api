#!/bin/bash
git add . 
git commit -m "default message " 
git push -u origin $(git branch --show-current)