#!/bin/bash

#
# integration test のためのパッケージを用意する
#

sudo apt-get update
sudo apt-get install -y npm
sudo apt-get install -y ruby
sudo apt-get install -y python

USER_BASE_PATH=$(python -m site --user-base)
export PATH=$PATH:${USER_BASE_PATH}/bin
