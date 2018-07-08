#!/bin/bash

#
# Integration Test を行うための準備〜実行を行う
#
# <前提条件>
# - AWS CLI が準備されている
# - aws-sam-local がインストールされている
#     - 各々、バージョンは 0.4.0 である
# - プロジェクトルートから実行される
#

abort ()
{
   echo "$@" 1>&2
   exit 1
}

# $1: ファイル名
# $2: 検索文字列
wait4SetupApp ()
{
    SAFETY_BREAK=0
    INTERVAL=1
    echo -n "wait"
    while true;
    do
        sleep ${INTERVAL}
        # ラスト1行だと読み逃す可能性があるので2行読む
        if tail -n 5 $1 | grep "$2" ; then
            echo "ok"
            break
        fi
        echo -n "."

        SAFETY_BREAK=$(( SAFETY_BREAK + 1 ))
        # タイムアウト時間は 3 分に設定
        if [ ${SAFETY_BREAK} -gt 180 ] ; then
            echo "タイムアウトしました"
            cat $1
            exit 1
        fi
    done
}

echo "--- swagger spec 生成開始 ---"
./gradlew generateSwaggerDocumentation || abort "swagger spec の生成に失敗しました"
echo "--- swagger spec 生成終了 ---"

echo "--- テンプレート生成開始 ---"
ruby scripts/generateYamls.rb || abort "テンプレート生成に失敗しました"
echo "--- テンプレート生成終了 ---"

echo "--- テスト用 jar ファイル生成開始 ---"
./gradlew shadowJar || abort "テスト用 jar ファイル生成に失敗しました"
echo "--- テスト用 jar ファイル生成終了 ---"

sam local start-api -t generated/sam.yaml > samlocal_log 2>&1 &
wait4SetupApp samlocal_log "Running on http://127.0.0.1:3000/"

./gradlew integrationTest
