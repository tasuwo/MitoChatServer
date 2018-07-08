#!/bin/bash

#
# fake services を終了しログを出力する
#

# fake services の終了
kill $(pgrep -f sam)

# fake services のログ出力
echo "--- aws-sam-local のログ出力開始 ---"
cat samlocal_log
echo "--- aws-sam-local のログ出力終了 ---"
