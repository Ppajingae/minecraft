#!/bin/bash

set -e  # 에러 발생 시 즉시 중단

# 1. 로컬에서 빌드
cd ..
./gradlew build

# 2. 서버 설정
SERVER_USER=kimdoguyn
SERVER_HOST=192.168.45.98
SERVER_PATH=/home/kimdoguyn/server
JAR_NAME=minecraft-0.0.1-SNAPSHOT.jar

# 3. 서버에 접속해서 기존 컨테이너/이미지 삭제
ssh $SERVER_USER@$SERVER_HOST << EOF
  cd $SERVER_PATH
  sudo docker-compose down
  sudo docker image rm minecraft:latest || true
EOF

# 4. 빌드된 JAR 전송 (build/libs에서)
scp build/libs/$JAR_NAME $SERVER_USER@$SERVER_HOST:$SERVER_PATH
scp deploy/docker-compose.yml $SERVER_USER@$SERVER_HOST:$SERVER_PATH
scp deploy/Dockerfile $SERVER_USER@$SERVER_HOST:$SERVER_PATH


# 5. 서버에서 다시 실행
ssh $SERVER_USER@$SERVER_HOST << EOF
  cd $SERVER_PATH
  sudo docker-compose up -d --build
EOF