#!/bin/bash

#
# 統合テスト実行に必要な fake services のインストール
#

mkdir dynamodb_local
curl https://s3-ap-northeast-1.amazonaws.com/dynamodb-local-tokyo/dynamodb_local_latest.zip > dynamodb_local/dynamodb_local.zip
unzip dynamodb_local/dynamodb_local.zip -d dynamodb_local
gem install fakes3 -v 1.2.1
pip install --user aws-sam-cli==0.4.0