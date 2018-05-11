#DROP DATABASE DentistDB; #If you don't already have a database called DentistDB, comment out this line
#CREATE DATABASE DentistDB;
#USE DentistDB;

SET foreign_key_checks=0; #Allows the creation of the tables in any order.

CREATE TABLE patients (
	patientID INTEGER AUTO_INCREMENT,
    title VARCHAR(4) NOT NULL,
    forename VARCHAR(30) NOT NULL,
    surname VARCHAR(30) NOT NULL,
    dateOfBirth DATE NOT NULL,
    houseNumber INTEGER NOT NULL,
    postCode VARCHAR(8) NOT NULL,
    healthCarePlan VARCHAR(30),
    totalOwed FLOAT,
    checkUpsThisYear INTEGER,
    hygieneVisitsThisYear INTEGER,
    repairsThisYear INTEGER,
    
    PRIMARY KEY (patientID),
    
    FOREIGN KEY (houseNumber, postCode) REFERENCES addresses(houseNumber, postCode),
	FOREIGN KEY (healthCarePlan) REFERENCES healthcareplans (planName)
);	

CREATE TABLE partners (
	partnerID INTEGER NOT NULL AUTO_INCREMENT,
    role VARCHAR(30) NOT NULL,
    fullName VARCHAR(60) NOT NULL,
    
    PRIMARY KEY (partnerID)
);

CREATE TABLE addresses (
	houseNumber INTEGER NOT NULL,
    streetName VARCHAR(30) NOT NULL,
    districtName VARCHAR(30),
    city VARCHAR(30) NOT NULL,
    postCode VARCHAR(8) NOT NULL,
    
    PRIMARY KEY (houseNumber, postCode)
);


CREATE TABLE healthcareplans (
	planName VARCHAR(30) NOT NULL,
    checkups INTEGER DEFAULT 0,
    hygieneVisits INTEGER DEFAULT 0,
    repairs INTEGER DEFAULT 0,
    monthlyCost FLOAT NOT NULL,
    
    PRIMARY KEY (planName)
);

CREATE TABLE appointments (
	startTime TIME NOT NULL,
    endTime TIME NOT NULL,
    isComplete BOOLEAN NOT NULL,
    date DATE NOT NULL,
    partnerID INTEGER NOT NULL,
    patientID INTEGER,
    treatment VARCHAR(30),
    
    PRIMARY KEY (startTime, date, partnerID),
    
    FOREIGN KEY (partnerID) REFERENCES partners(partnerID),
    FOREIGN KEY (patientID) REFERENCES patients(patientID),
    FOREIGN KEY (treatment) REFERENCES treatments(treatmentName)
);

CREATE TABLE treatments (
	treatmentName VARCHAR(30) NOT NULL,
    cost FLOAT NOT NULL,
    
    PRIMARY KEY (treatmentName)
);