INSERT INTO `dentistdb`.`partners`
(`role`,
`fullName`)
VALUES
('Dentist',
'Steve Johnson');

INSERT INTO `dentistdb`.`partners`
(`role`,
`fullName`)
VALUES
('Hygienist',
'John Stevenson');

INSERT INTO `dentistdb`.`addresses`
(`houseNumber`,
`streetName`,
`city`,
`postCode`)
VALUES
(6,
'a street',
'sheffield',
'a1aa1');

INSERT INTO `dentistdb`.`patients`
(`patientID`,
`title`,
`forename`,
`surname`,
`dateOfBirth`,
`houseNumber`,
`postCode`,
`totalOwed`,
`checkUpsThisYear`,
`hygieneVisitsThisYear`,
`repairsThisYear`)
VALUES
(0,
'mr',
'steve',
'johnson',
'19920209',
6,
'a1aa1',
0.0,
0,
0,
0);
