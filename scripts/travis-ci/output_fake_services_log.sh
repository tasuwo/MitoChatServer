#!/bin/bash

#
# fake services を終了しログを出力する
#

# fake services の終了
kill $(pgrep -f dynamodb)
kill $(pgrep -f fakes3)
kill $(pgrep -f sam)

# fake services のログ出力
echo "--- dynamodb-local のログ出力開始 ---"
cat dynamodb_local_log
echo "--- dynamodb-localのログ出力終了 ---"

echo "--- fakes3 のログ出力開始 ---"
cat fakes3_log
echo "--- fakes3 のログ出力終了 ---"

echo "--- aws-sam-local のログ出力開始 ---"
cat samlocal_log
echo "--- aws-sam-local のログ出力終了 ---"

