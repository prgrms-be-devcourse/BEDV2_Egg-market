#!/usr/bin/env bash
PROJECT_PATH="/home/ec2-user/egg-market"
PROJECT_NAME="demo"
PROJECT_VERSION="0.0.1-SNAPSHOT"
DEPLOY_LOG="$PROJECT_PATH/deploy.log"

RUNNING_PID=$(pgrep -f $PROJECT_NAME-$PROJECT_VERSION)

NOW=$(date +%c)

if [ -z "$CUREENT_PID" ]; then
  echo "[$NOW] 현재 실행중인 애플리케이션이 없습니다." >>$DEPLOY_LOG
else
  echo "[$NOW] 실행중인 $RUNNING_PID 애플리케이션을 종료합니다." >> $DEPLOY_LOG
  kill -15 "$RUNNING_PID"
fi
