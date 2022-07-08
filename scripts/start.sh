#!/usr/bin/env bash
PROJECT_PATH="/home/ec2-user/egg-market"
PROJECT_NAME="eggmarket"
PROJECT_VERSION="0.0.1-SNAPSHOT"
JAR_FILE="$PROJECT_PATH/target/$PROJECT_NAME-$PROJECT_VERSION.jar"

DEPLOY_LOG="$PROJECT_PATH/deploy.log"

sh /etc/profile.d/codedeploy.sh

NOW=$(date +%c)

echo "[$NOW] 애플리케이션 실행" >>$DEPLOY_LOG
nohup java -jar -Dspring.profiles.active=prod $JAR_FILE &

RUNNING_PID=$(pgrep -f $PROJECT_NAME)
echo "[$NOW] 프로세스가 정상적으로 실행되었습니다. 실행 PID -> $RUNNING_PID" >>$DEPLOY_LOG
