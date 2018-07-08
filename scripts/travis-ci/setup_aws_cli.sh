#!/bin/bash

#
# AWS CLI を準備する
# - AWS CLI に対応する IAM User の credential 情報 (アクセスキー、シークレットキー) を設定する
# - AWS CLI をダウンロードし、パスを通す
#

# 環境変数に credential 情報を設定する
./scripts/travis-ci/ecr_credentials.sh
# AWS CLI のインストール
curl "https://s3.amazonaws.com/aws-cli/awscli-bundle.zip" -o "awscli-bundle.zip"
unzip awscli-bundle.zip
./awscli-bundle/install -b ~/bin/aws
# AWS CLI へパスを通す
export PATH=~/bin:$PATH
