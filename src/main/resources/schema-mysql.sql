CREATE TABLE IF NOT EXISTS `easy_travel`.`user_info` (
  `account` VARCHAR(20) NOT NULL,
  `password` VARCHAR(20) NOT NULL,
  `name` VARCHAR(20) NOT NULL,
  `birthday` DATE NULL,
  `active` TINYINT NOT NULL DEFAULT 0,
  `reg_time` DATETIME NOT NULL,
  `motorcycle_license` TINYINT NULL,
  `driving_license` TINYINT NULL,
  `vip` TINYINT NULL,
  `vip_start_date` DATE NULL,
  PRIMARY KEY (`account`));
  
  CREATE TABLE IF NOT EXISTS `easy_travel`.`vehicle` (
  `license_plate` VARCHAR(20) NOT NULL,
  `category` VARCHAR(20) NOT NULL,
  `cc` INT NOT NULL DEFAULT 0,
  `start_serving_date` DATE NOT NULL,
  `latest_check_date` DATE NULL,
  `status` VARCHAR(20) NOT NULL,
  `city` VARCHAR(20) NULL,
  `location` VARCHAR(20) NULL,
  `odo` DOUBLE NOT NULL DEFAULT 0,
  `price` INT NOT NULL DEFAULT 0,
  PRIMARY KEY (`license_plate`));
  
  CREATE TABLE IF NOT EXISTS `easy_travel`.`fee` (
  `serial_number` INT NOT NULL AUTO_INCREMENT,
  `project` VARCHAR(20) NOT NULL,
  `cc` INT NOT NULL DEFAULT 0,
  `rate` DOUBLE NULL DEFAULT 0,
  `threshold` INT NULL DEFAULT 0,
  PRIMARY KEY (`serial_number`));
  
  CREATE TABLE IF NOT EXISTS `easy_travel`.`maintenance` (
  `serial_number` INT NOT NULL AUTO_INCREMENT,
  `license_plate` VARCHAR(20) NOT NULL,
  `price` INT NULL DEFAULT 0,
  `start_time` DATETIME NULL,
  `end_time` DATETIME NULL,
  `note` VARCHAR(45) NULL,
  PRIMARY KEY (`serial_number`));
  
  CREATE TABLE IF NOT EXISTS `easy_travel`.`finance` (
  `serial_number` INT NOT NULL AUTO_INCREMENT,
  `title` VARCHAR(20) NOT NULL,
  `detail` VARCHAR(20) NOT NULL,
  `price` INT NULL,
  `build_time` DATETIME NULL,
  PRIMARY KEY (`serial_number`));
  
  CREATE TABLE IF NOT EXISTS `easy_travel`.`rent` (
  `serial_number` INT NOT NULL AUTO_INCREMENT,
  `account` VARCHAR(20) NOT NULL,
  `license_plate` VARCHAR(20) NOT NULL,
  `city` VARCHAR(20) NULL,
  `location` VARCHAR(20) NULL,
  `now_time` DATETIME NULL,
  `rent` TINYINT NULL DEFAULT 0,
  `price` INT NULL,
  PRIMARY KEY (`serial_number`));
  
  CREATE TABLE IF NOT EXISTS `easy_travel`.`stop` (
  `city` VARCHAR(20) NOT NULL,
  `location` VARCHAR(20) NOT NULL,
  `bike_amount` INT NULL,
  `motorcycle_amount` INT NULL,
  `car_amount` INT NULL,
  PRIMARY KEY (`city`, `location`));

