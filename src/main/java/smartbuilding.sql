create taBLE parking_log (
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

create table users (
 
  user_id INT auto_increment primary key,
	id varchar(100) not null unique,
	name varchar(100) not null,
	pass varchar(255) not null,
	access_level int default 1,    
    office_id int,
	card_id varchar(50) unique not null,
    vehicle_no varchar(20),
    is_active tinyint,
	created_at timestamp default current_timestamp 

);

	create TABLE parking_space (
		space_id INT AUTO_INCREMENT PRIMARY KEY,    
		location VARCHAR(50) NOT NULL,              
		is_occupied BOOLEAN DEFAULT FALSE,         
		last_update DATETIME DEFAULT CURRENT_TIMESTAMP  
	);

