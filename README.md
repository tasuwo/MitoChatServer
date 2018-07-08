# みとちゃっと [![Build Status](https://travis-ci.com/tasuwo/MitoChatServer.svg?branch=master)](https://travis-ci.com/tasuwo/MitoChatServer)

```shell
## ビルド & ユニットテスト
$ ./gradlew build
## ドキュメント生成
$ ./gradlew generateSwaggerDocumentation
$ ruby scripts/generateYamls.rb
## Fat Jar 作成
$ ./gradlew shadowJar

## AWS SAM CLI
$ pip install --user --upgrade awscli
## local 立ち上げ
$ sam local start-api -t generated/sam.yaml
## インテグレーションテスト
$ ./gradlew integrationTest
```
