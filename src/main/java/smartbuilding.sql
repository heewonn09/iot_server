create database smart_building;
use smart_building;

-- [1] 사무실 테이블 생성
CREATE TABLE offices (
    office_id int NOT NULL AUTO_INCREMENT,
    name varchar(100) NOT NULL,
    floor_no int NOT NULL,
    created_at timestamp NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (office_id)
);
-- 레코드 데이터 삽입
insert into offices values(null,'101A',1,default);
insert into offices values(null,'202B',2,default);
insert into offices values(null,'305A',3,default);
insert into offices values(null,'주차장',1,default);

-- [2] 유저 테이블 생성
CREATE TABLE users (
  `user_id` int NOT NULL AUTO_INCREMENT,
  `id` varchar(100) NOT NULL,
  `name` varchar(100) NOT NULL,
  `card_id` varchar(50) DEFAULT NULL,
  `pass` varchar(255) NOT NULL,
  `access_level` int DEFAULT '1',
  `office_id` int DEFAULT NULL,
  `vehicle_no` varchar(20) DEFAULT NULL,
  `is_active` BOOLEAN DEFAULT TRUE,
  `created_at` timestamp DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `card_id` (`card_id`),
  UNIQUE KEY `vehicle_no` (`vehicle_no`),
  FOREIGN KEY (`office_id`) REFERENCES `offices` (`office_id`)
);
-- 레코드 데이터 삽입
insert into users values(null,'admin','admin',null,1234,3,1,null,1,default);
insert into users values(null,'floor','floor',null,1234,2,2,null,1,default);
insert into users values(null,'member','member',null,1234,1,3,'777로7777',1,default);

-- [3] 디바이스 테이블 생성
CREATE TABLE `devices` (
  `device_id` int NOT NULL AUTO_INCREMENT,
  `office_id` int NOT NULL,
  `name` varchar(100) NOT NULL,
  `type` enum('RFID','MQ2','DHT','LED','HVAC','SERVO','HCSR','BUZZER','ELEVATOR') NOT NULL,
  `status` enum('ON','OFF','IDLE','ALERT') DEFAULT 'IDLE',
  `last_updated` timestamp DEFAULT CURRENT_TIMESTAMP,
  `is_active` BOOLEAN DEFAULT TRUE,
  PRIMARY KEY (`device_id`),
  FOREIGN KEY (`office_id`) REFERENCES `offices` (`office_id`)
);
-- 레코드 데이터 삽입
insert into devices values(null,1,'LED-101A','LED','OFF',default,1); --1층 디바이스
insert into devices values(null,1,'SERVO-101A','SERVO','OFF',default,1);
insert into devices values(null,1,'DHT-101A','DHT','IDLE',default,1);
insert into devices values(null,1,'RFID-101A','RFID','IDLE',default,1);
insert into devices values(null,4,'HCSR-주차장','HCSR','IDLE',default,1); --주차장 디바이스
insert into devices values(null,4,'SERVO-주차장','SERVO','OFF',default,1);
insert into devices values(null,2,'LED-202B','LED','OFF',default,1); -- 2층 디바이스
insert into devices values(null,2,'BUZZER-202B','BUZZER','IDLE',default,1);
insert into devices values(null,2,'MQ2-202B','MQ2','IDLE',default,1); -- 3층 디바이스
insert into devices values(null,3,'LED-305A','LED','OFF',default,1);
insert into devices values(null,3,'DHT-305A','DHT','IDLE',default,1);
insert into devices values(null,1,'ELV-101A','ELEVATOR','ON',default,1); --엘리베이터

-- [4] 주차 공간 테이블 생성
create table parking_space (
    space_id INT AUTO_INCREMENT PRIMARY KEY,
    location VARCHAR(50) NOT NULL,
    is_occupied BOOLEAN DEFAULT FALSE,
    last_update DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- [5] 주차장 이용 로그 테이블 생성
create table parking_log (
    parking_id INT AUTO_INCREMENT PRIMARY KEY,  
    user_id INT NOT NULL,                      
    space_id INT,                               
    in_time DATETIME DEFAULT CURRENT_TIMESTAMP, 
    out_time DATETIME,                          
    duration_min INT,                         
    action ENUM('IN','OUT') NOT NULL,           
    note VARCHAR(100),                          
    FOREIGN KEY (user_id) REFERENCES users(user_id),
    FOREIGN KEY (space_id) REFERENCES parking_space(space_id)
);

-- [6] 엘리베이터 이용 로그 테이블 생성
CREATE TABLE `elevator_log` (
  `elevator_id` int NOT NULL AUTO_INCREMENT,
  `user_id` int DEFAULT NULL,
  `from_floor` int DEFAULT NULL,
  `to_floor` int DEFAULT NULL,
  `status` enum('CALLED','ARRIVED','DENIED') DEFAULT 'CALLED',
  `requested_at` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (elevator_id),
  FOREIGN KEY (user_id) REFERENCES users(user_id)
);

-- [7] 센서 데이터 로그 테이블 생성 (온습도, 가스 센서)
CREATE TABLE `environment_data` (
  `record_id` int NOT NULL AUTO_INCREMENT,
  `device_id` int NOT NULL,
  `temperature` float,
  `humidity` float,
  `gas_level` float,
  `measured_at` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`record_id`),
  FOREIGN KEY (`device_id`) REFERENCES `devices` (`device_id`)
)

-- [8] 액추에이터 로그 테이블 생성 (SERVO, 조명, 화재감지 등)
CREATE TABLE `event_log` (
  `event_id` int NOT NULL AUTO_INCREMENT,
  `device_id` int DEFAULT NULL,
  `user_id` int DEFAULT NULL,
  `office_id` int DEFAULT NULL,
  `event_type` enum('ACCESS','FIRE','HVAC','LIGHT','PARKING') NOT NULL,
  `event_action` varchar(50) NOT NULL,
  `value` varchar(50) DEFAULT NULL,
  `timestamp` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `note` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`event_id`),
  FOREIGN KEY (`device_id`) REFERENCES `devices` (`device_id`),
  FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`),
  FOREIGN KEY (`office_id`) REFERENCES `offices` (`office_id`)
)

