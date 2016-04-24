CREATE SCHEMA `magnus`;

CREATE TABLE `magnus`.`airline` (
  `airlineId` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`airlineId`));

CREATE TABLE `magnus`.`flight` (
  `flightId` INT NOT NULL AUTO_INCREMENT,
  `flightNumber` INT NOT NULL,
  `seats` INT NOT NULL,
  `flightTime` DATETIME NOT NULL,
  `airlineId` INT NOT NULL,
  PRIMARY KEY (`flightId`),
  UNIQUE INDEX `flightId_UNIQUE` (`flightId` ASC),
  INDEX `airlineId_idx` (`airlineId` ASC),
  CONSTRAINT `airlineId`
    FOREIGN KEY (`airlineId`)
    REFERENCES `magnus`.`airline` (`airlineId`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);

CREATE TABLE `magnus`.`airport` (
  `airportId` INT NOT NULL AUTO_INCREMENT,
  `IATACode` VARCHAR(45) NOT NULL,
  `timeZone` VARCHAR(45) NOT NULL COMMENT 'Offset in hours * 100',
  `name` VARCHAR(200) NOT NULL,
  `country` VARCHAR(45) NOT NULL,
  `city` VARCHAR(45) NOT NULL,
  `flightId` INT NOT NULL,
  PRIMARY KEY (`airportId`),
  INDEX `flightId_idx` (`flightId` ASC),
  CONSTRAINT `flightId`
    FOREIGN KEY (`flightId`)
    REFERENCES `magnus`.`flight` (`flightId`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);

CREATE TABLE `magnus`.`flight_instance`(
	`flightInstanceId` INT NOT NULL AUTO_INCREMENT,
	`flightId` INT NOT NULL,
	`date` DATETIME NOT NULL,
	`time` TIME NOT NULL,
	`availableSeats` INT NOT NULL,
	`price` DECIMAL(20,2) NOT NULL DEFAULT 0,
	PRIMARY KEY (`flightInstanceId`), 
	INDEX `flightId_idx` (`flightId` ASC),
	CONSTRAINT `flight_instance_flightId`
		FOREIGN KEY (`flightId`)
		REFERENCES `magnus`.`flight`(`flightId`)
		ON DELETE NO ACTION
		ON UPDATE NO ACTION);

CREATE TABLE `magnus`.`reservation`(
	`reservationId` INT NOT NULL AUTO_INCREMENT,
	`totalPrice` DECIMAL(20,2) NOT NULL DEFAULT 0,
	`flightInstanceId` INT NOT NULL,
	PRIMARY KEY (`reservationId`),
	INDEX `flightInstanceId_idx` (`flightInstanceId` ASC),
	CONSTRAINT `reservation_flightInstance_flightInstanceId`
		FOREIGN KEY (`flightInstanceId`)
		REFERENCES `magnus`.`flight_instance`(`flightInstanceId`)
		ON DELETE NO ACTION
		ON UPDATE NO ACTION);

CREATE TABLE `magnus`.`passenger`(
	`passengerId` INT NOT NULL AUTO_INCREMENT,
	`firstName` VARCHAR(45) NOT NULL, 
	`LastName` VARCHAR(45) NOT NULL,
	`reservationId` INT NOT NULL,
	PRIMARY KEY (`passengerId`), 
	INDEX `reservationId_idx` (`reservationId` ASC),
	CONSTRAINT `passenger_reservation_reservationId`
		FOREIGN KEY (`reservationId`)
		REFERENCES `magnus`.`reservation`(`reservationId`)
		ON DELETE NO ACTION
		ON UPDATE NO ACTION);

