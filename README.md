
# 📌 Building RCS [빌딩 원격관리 시스템]

## 목차

1. [**서비스 소개**](#서비스-소개)
2. [**프로젝트 기획서**](#프로젝트-기획서)
3. [**주요 기능**](#주요-기능)
4. [**팀원 소개**](#팀원-소개)
5. [**시연 방법**](#시연-방법)

# 서비스 소개
HDC랩스 NOVA FullStack 과정, 1팀의 First 프로젝트 Repository 입니다.</br>
스마트 빌딩을 주제 각 디바이스들에 대한 원격 제어, 자동 데이터 수집 기능을 구현한 서비스입니다.   
권한에 따라 지원되는 서비스를 구분하였으며,</br>
크게 전체통제가 가능한 관리자와, 각 오피스 별 부분 사용자 역할로 구분이 됩니다.

----------------------------

# 프로젝트 기획서

## 프로젝트 구성도
 | **시스템 아키텍처**                        |
 |-------------------------------------|
 | ![SA.png](img/SA.png)               |

 | **ERD**                             |
 |-------------------------------------|
 | ![ERD_Final.png](img/ERD_Final.png) |

 | **MQTT API**                        |
 |-------------------------------------|
 | |\=

 ## 프로젝트 파일 트리 (JAVA)
```
📦src
 ┣ 📂main
 ┃ ┣ 📂java
 ┃ ┃ ┣ 📜smartbuilding.sql // Database, Tables 생성 시 실행할 sql파일
 ┃ ┃ ┣ 📜Main.java // psvm 메서드로 java를 실행하는 첫 파일
 ┃ ┃ ┣ 📂dao // DB 작업 (CRUD) 을 수행하는 DAO 파일이 들어가는 폴더
 ┃ ┃ ┣ 📂dto // 데이터 전달 형식을 class, DTO 단위로 들어가는 폴더
 ┃ ┃ ┣ 📂mqtt //MqttClient 클래스를 통해 Connect, Sub/Pub 가 구현된 파일 폴더
 ┃ ┃ ┣ 📂controller // 프로젝트에서 주요 기능들을 처리하는 로직8
 ┃ ┃ ┣ 📂service // 각 기능 별로 로직 구현하는 폴더
 ┃ ┃ ┣ 📂util // DB Connection, ANSI Escape Codes 등 static으로 초기 생성하는 데이터
 ┃ ┃ ┣ 📂config // Broker 서버, DB 서버와 관련된 민감정보들이 들어가는 폴더 (깃에 안올림)
 ┃ ┃ ┗ 📂view // 콘솔 화면, UI를 넣는 폴더
```

## 프로젝트 일정 (25.10.21 - 25.10.29)    

| 작업 항목             | 시작 날짜      | 종료 날짜      | 기간(일) |
|-------------------|------------|------------|-------|
| 프로젝트 구조 설계        | 2025-10-21 | 2025-10-22 | 2     |
| 프로젝트 UI 설계        | 2025-10-22 | 2025-10-22 | 1     |
| 프로젝트 기능 구현        | 2025-10-22 | 2025-10-27 | 6     |
| ERD 구조 작성         | 2025-10-23 | 2025-10-25 | 3     |
| MQTT API 설계       | 2025-10-24 | 2025-10-25 | 2     |
| 전체 기능 테스트 및 오류 수정 | 2025-10-27 | 2025-10-28 | 2     |
| 최종 검토 및 발표        | 2025-10-29 | 2025-10-29 | 1     |

## 🛠️ 기술 스택
| 개발환경    | 언어     | 모듈                            | 데이터베이스 |
|---------|--------|-------------------------------|--------|
| Eclipse | Java   | paho-mqtt<br>mysql-connector-java | MySQL  |
| VSCode  | Python | paho-mqtt<br>RPi.GPIO         | MySQL  |


--------------------------

# 주요 기능

## 1. 로그인 / 회원가입
입력한 ID, PW 값으로 회원 조회를 하고, 권한에 맞게 관리자 / 사용자 페이지로 이동한다.<br>
회원가입 시 회원 정보가 추가되며, 사용자 권한으로 가입된다.

## 2. 출입문 제어 기능
로그인한 회원의 권한과 출입문을 비교하고, 제어할 수 있는 경우 해당 문을 원격으로 연다.<br>
Mqtt 토픽을 통해 해당 출입문을 열고, 해당 데이터 로그를 DB에 저장한다.<br>
제어할 수 없는 경우 콘솔에 제어 거부를 출력하고, 해당 데이터 로그를 DB에 저장한다.

## 3. 엘리베이터 제어 기능

| 콘솔 화면 | 디바이스 동작 결과    | 기능 설명                                                                                 |
|-------|---------------|---------------------------------------------------------------------------------------|
|<img src="https://github.com/user-attachments/assets/d1ed834b-9e08-4cf7-8e70-402ae5eb17c7" width="300" height=auto>|               | 엘리베이터 조작이 가능한 유저들의 목록을 콘솔에 출력한다.<br> 유저마다 어떤 권한을 가지고 있는지, 몇 층에 접근할 수 있는지 등의 정보를 출력한다. |
|<img src="https://github.com/user-attachments/assets/ae28bcd6-afec-4039-8ce9-5b9431d8247c" width="300" height=auto>|               | 엘리베이터 디바이스와 Mqtt 통신을 통해 엘리베이터의 현 위치와 상태를 출력한다.|
|<img src="" width="300" height=auto>|               | 엘리베이터 이용 여부를 변경시킨다. 정지시킬 경우 엘리베이터를 이용할 수 없다.|
|<img src="https://github.com/user-attachments/assets/2c35aef8-00c6-4d6b-88c0-51f293ce698e" width="300" height=auto>|<img src="https://github.com/user-attachments/assets/e9f64c6e-0017-42f3-b016-f721e79f5a94" width="300" height=auto>               | 엘리베이터의 현재 위치를 확인하고, 현재 위치해 있는 층과 원하는 층을 입력해 엘리베이터를 움직인다.|
|<img src="" width="300" height=auto>|               | 엘리베이터 호출, 이동과 관련된 로그 데이터를 DB에서 가져와 전체 출력한다.|



## 4. Office 별 디바이스 제어 기능
사무실마다 제어할 수 있는 디바이스 목록들이 다르다. 권한에 따라 제어할 수 있는 사무실 목록들을 출력하고, 어떤 사무실을 제어할 지 선택한다.<br>
사무실 선택 시, 디바이스 목록들이 출력되고, 제어할 수 있는 액추에이터를 선택해 제어 여부를 선택하게 한다.


## 5. 주차장 관리 제어 기능

| 콘솔 화면 | 디바이스 동작 결과 | 기능 설명 |
|------------|------------------|-------------|
| <img src="https://github.com/user-attachments/assets/3e0bb8f0-3d83-4f5f-b918-7a3e9b3d4b61" width="300" /> | <img src= https://github.com/user-attachments/assets/d4e83f8a-46dc-42f9-9fee-f7cfe2ee4c30 height="300" width="300" /> | 초음파 센서가 차량을 감지하면, MQTT 토픽(`parking/status`)으로 차량 감지 이벤트를 서버로 전송하고, 서보모터 게이트가 자동으로 열림.<br>차량이 지나간 뒤 일정 시간이 지나면 게이트가 자동으로 닫힌다. |
| <img src="" width="300" /> | <img src="https://github.com/user-attachments/assets/IMG_2905.gif" width="300" /> | 실제 시연 장면 — 차량이 진입하면 게이트가 자동으로 열리고, 차량이 지나간 뒤 자동으로 닫히는 과정.<br>MQTT 실시간 통신 기반으로 동작. |
| <img src="" width="300" /> | <img src="https://github.com/user-attachments/assets/df8136dc-74e8-4d5c-9f3b-d8ce3db70f14" width="300" /> | 서버(Java)에서 MQTT 메시지를 수신 후, `parking_space` 및 `parking_log` 테이블에 입·출차 기록 저장.<br>관리자는 콘솔 메뉴에서 전체 주차 현황 및 차량 로그를 확인할 수 있다. |

---

### 🚗 주차장 사용자 기능

| 콘솔 화면 | 디바이스 동작 결과 | 기능 설명 |
|------------|------------------|-------------|
| <img src="https://github.com/user-attachments/assets/81b14a5b-0a1b-4b5f-b917-33b07c27f601" width="300" /> |  | 로그인한 사용자는 자신의 차량 번호(`vehicle_no`)를 기준으로 최근 주차 내역을 조회할 수 있다. |
| <img src="" width="300" /> |  | 최근 입차 시간(`last_in`), 출차 시간(`last_out`), 총 이용 횟수(`total_logs`), 누적 주차 시간(`total_minutes`)을 표 형태로 출력한다. |
| <img src="" width="300" /> |  | 데이터는 `getUserParkingSummary()` 메서드를 통해 SQL JOIN으로 조회되며, 실시간 주차 현황 대시보드에도 반영된다. |

---

----------------------------
| 콘솔 화면 | 디바이스 동작 결과 | 기능 설명 |
|------------|------------------|-------------|
| <img src="https://github.com/user-attachments/assets/abc12345-7890-efgh-5678-ijkl9012mnop" width="300" /> |  | 로그인 후 `getUserInfo()` 메서드를 통해 `users` 테이블에서 자신의 정보를 조회한다.<br>이때 `user_id`, `id`, `name`, `vehicle_no`, `access_level` 등의 필드를 가져온다. |
| <img src="" width="300" /> |  | 사용자가 차량 등록 메뉴를 선택하면 `updateVehicle()` 메서드가 실행된다.<br>이미 등록된 차량이 있을 경우 콘솔에 “이미 차량이 등록되어 있습니다.” 메시지가 출력되고, 새로 등록은 불가하다. |
| <img src="" width="300" /> |  | 차량 번호가 비어 있으면 새로 입력받은 차량 번호(`vehicle_no`)로 DB를 업데이트한다.<br>등록 성공 시 “🚗 차량 등록 성공!” 메시지를 출력하고, 이후 주차장 로그 기록(`parking_log`)에도 해당 차량이 반영된다. |



# 👨‍👩‍👧‍팀원 소개

| 이름  |                  역할 및 담당 기능                  |
|:---:|:--------------------------------------------:|
| 양준길 | PM<br>MQTT 통신 구축<br>프로젝트 일정관리<br>엘리베이터 기능 구현 |     
| 조수민 | ERD 설계 및 DB 연동<br>출입문 기능 구현<br>화재감지센서 데이터 수집 |
| 이희원 |           주차 관리 기능 구현<br>센서 로그 수집            |
| 김광민 |       IoT 조명 제어 기능 구현<br>온습도 센서 로그 수집        |

----------------------------

# 시연 방법

## 자바 환경설정하기
프로그램 실행에 MySQL8.0, Mosquitto가 필요합니다.<br> 소스코드 실행 전에 미리 다운로드 받는걸 권장합니다.

### 1. 소스코드 다운로드 (Java)
위 프로그램은 자바 소스코드로, Eclipse 나 Intellij 환경에서 실행하는 걸 가정하에 설명드립니다.<br>
먼저 소스코드를 로컬 환경에 다운로드 받습니다.
```
git clone https://github.com/iot-building/iot_server.git
```
다운받은 소스코드 내엔 build.gradle 파일이 있습니다. <br>
build.gradle 내에 명시된 라이브러리들을 모두 받아야 정상 실행이 가능합니다.

### 2. config 패키지에 .properties 파일 추가
#### 2-1. broker.properties
파일 경로 "src/main/java/config/broker.properties" 에 파일을 만든다.<br>
위 파일에는 broker.ip, broker.port가 포함되어야 한다.<br>
로컬에서 Mqtt 브로커를 실행할 경우 아래와 같이 입력한다.
```
broker.ip = localhost
broker.port = 1883
```

#### 2-2. db.properties
파일 경로 "src/main/java/config/db.properties" 에 파일을 만든다.<br>
위 파일에는 db.ip, db.port, db.database, db.user, db.password 가 포함되어야 한다.<br>
로컬에서 MySQL 서버를 실행할 경우 아래와 같이 입력한다.
```
db.ip = localhost
db.port = 3306
db.database = smart_building
db.user = '사용할 db 유저'
db.password = '패스워드'
```

### 3. MySQL 테이블 세팅
smartbuilding.sql 파일을 실행합니다(src/main/java/smartbuilding.sql).<br>실행 후 사용되는 데이터베이스, 테이블, 기본 레코드들이 데이터베이스 상에 있어야 합니다. 어떤 테이블이 필요한 지는 [**ERD**](#프로젝트-구성도)를 참고하면 됩니다.
