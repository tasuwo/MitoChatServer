#!/bin/sh

# <目的>
# アプリケーションのデプロイ
#
# <前提条件>
# - aws cli がインストールされている
# - aws cli が設定済みである (ユーザ情報(アクセスキー/シークレット)を aws cli に登録済みである)
# - Integration Test が実行済みである
#     - swagger.yml が生成済みである

STACK_NAME=$1
TEMPLATE_UPLOAD_BUCKET_NAME=$2

SCRIPT_DIR=$(cd $(dirname $0); pwd)
ARTIFACTS_DIR="${SCRIPT_DIR}/../generated"

abort ()
{
   echo "$@" 1>&2
   exit 1
}

echo "--- Api テンプレートアップロード開始 ---"
aws s3 cp ${ARTIFACTS_DIR}/swagger.yaml s3://${TEMPLATE_UPLOAD_BUCKET_NAME}/ \
     || abort "Api テンプレートアップロードに失敗しました"
echo "--- Api テンプレートアップロード開始 ---"

echo "--- jar ファイル生成開始 ---"
./gradlew shadowJar \
     || abort "jar ファイル生成に失敗しました"
echo "--- jar ファイル生成終了 ---"

echo "--- jar ファイルアップロード開始 ---"
aws cloudformation package \
     --template-file ${ARTIFACTS_DIR}/sam.yaml \
     --output-template-file ${ARTIFACTS_DIR}/output-sam.yaml \
     --s3-bucket ${TEMPLATE_UPLOAD_BUCKET_NAME} \
     --s3-prefix jars \
     || abort "jar ファイルアップロードに失敗しました"
echo "--- jar ファイルアップロード終了 ---"

echo "--- デプロイ開始 ---"
aws cloudformation deploy \
     --template-file ${ARTIFACTS_DIR}/output-sam.yaml \
     --stack-name ${STACK_NAME} \
     --capabilities CAPABILITY_IAM \
     --parameter-overrides \
        TemplateUploadBucketName=${TEMPLATE_UPLOAD_BUCKET_NAME} \
     || abort "デプロイに失敗しました"
aws s3 rm s3://${TEMPLATE_UPLOAD_BUCKET_NAME}/jars/ --recursive
echo "--- デプロイ終了 ---"
