# 데이터베이스 구성

![image](https://user-images.githubusercontent.com/38197077/100518157-3f03b480-31d3-11eb-9fa4-e07d135a15c9.png)

`source` → `destination`

# 프로그램 요약

**사용된 언어**: `Java`

**사용된 빌드 툴**: `Gradle`

**데이터스토어**: 관계형 데이터베이스 (`MySQL 5.7`)

프로그램은 TCP 프로토콜로 통신하여 시스템간 인터페이스를 형성하고, Datagram이라는 객체 단위로 메세지를 전달합니다. (1초 마다의 요청 응답, 5초 마다의 요청,응답으로 나뉘어져 있습니다.)

프로젝트는 Gradle 멀티 모듈로 이루어져 있으며, 요구사항 명세에 맞는 Data, Master, Slave 세개의 실행 가능한 애플리케이션으로 구분 되어져 있고, 모듈 구조로는 `common`, `data`, `master`, `slave`로 구성되어 있습니다.

---

## 각 프로그램의 역할

- Data\
0.1초 마다 source 테이블에 데이터를 생성하여 insert
- Master\
TCP Scoket Server ( 두개의 서버로 구성되어 있습니다.)
    1. (기본 포트: 10055) 1초마다 접근 해오는 클라이언트에게 현재부터 1초 과거 까지의 데이터를 TCP 프로토콜로 전달
    2. (기본 포트: 10054) 5초마다 접근 해오는 클라이언트에게 과거 데이터 중 처리되지 못한 최대 100개까지의 데이터를 전달 ( 여러개의 쓰레드가 동시에 데이터베이스에 접근하여 레코드를 가져오므로, `ConcurrentHashMap`, `LinkedBlockingQueue`를 사용하여, 데이터가 중복 되지 않게 큐잉)
- Slave\
TCP Socket Client
    - 1초마다 실행되는 스케줄러가 1번 소켓 서버에 접속하여 데이터를 전달받고 destination에 저장, 저장이 모두 완전하게 되었을 때 source에서 해당 데이터 삭제
    - 5초마다 실행되는 스케줄러가 2번 소켓 서버에 접속하여 과거 데이터를 전달받고 destination에 저장, 저장이 모두 완전하게 되었을 때 source에서 해당 데이터 삭제

common 모듈의 `config/MySQLConfig.java`에서 Database 접속 정보 수정

## 빌드, 실행

1. 다음과 같은 sql대로 DB Table 스키마 생성

    ```sql
    CREATE TABLE IF NOT EXISTS `destination` (
      `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
      `r_num` int(11) NOT NULL,
      `created_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
      `ms` int(3) NOT NULL DEFAULT '0',
      PRIMARY KEY (`id`),
      KEY `dest_created_time_IDX` (`created_time`) USING BTREE
    ) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

    CREATE TABLE IF NOT EXISTS `source` (
      `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
      `r_num` int(11) NOT NULL,
      `created_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
      `ms` int(3) NOT NULL DEFAULT '0',
      PRIMARY KEY (`id`),
      KEY `source_created_time_IDX` (`created_time`) USING BTREE
    ) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;
    ```

2. 빌드

    ```bash
    $ ./gradlew build
    ```

3. 실행
    data, master, slave 실행
    ```bash
    $ cd ./build/libs
    $ java -jar {data|master|slave}-{SNAPSHOT_VERSION}.jar
    ```
   
