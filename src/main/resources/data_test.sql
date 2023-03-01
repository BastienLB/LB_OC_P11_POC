DROP TABLE IF EXISTS `mtm_speciality_hospital`;
DROP TABLE IF EXISTS `speciality`;
DROP TABLE IF EXISTS `hospital`;

CREATE TABLE `speciality` (
  `id` int PRIMARY KEY AUTO_INCREMENT,
  `name` varchar(255)
);

CREATE TABLE `mtm_speciality_hospital` (
  `id` int PRIMARY KEY AUTO_INCREMENT,
  `speciality_id` INTEGER,
  `hospital_id` INTEGER
);

CREATE TABLE `hospital` (
  `id` int PRIMARY KEY AUTO_INCREMENT,
  `name` varchar(255),
  `latitude` Decimal(8,6) DEFAULT 0,
  `longitude` Decimal(9,6) DEFAULT 0,
  `available_beds` INTEGER,
  `address1` varchar(255),
  `address2` varchar(255),
  `address3` varchar(255),
  `city` varchar(255),
  `county` varchar(255),
  `postcode` varchar(255),
  `phone` varchar(255),
  `email` varchar(255)
);

ALTER TABLE `mtm_speciality_hospital` ADD CONSTRAINT fk_mtm_speciality FOREIGN KEY (`speciality_id`) REFERENCES `speciality` (`id`) ON DELETE CASCADE;
ALTER TABLE `mtm_speciality_hospital` ADD CONSTRAINT fk_mtm_hospital FOREIGN KEY (`hospital_id`) REFERENCES `hospital` (`id`) ON DELETE CASCADE;

INSERT INTO speciality(name) VALUES 
	('Anaesthetics'), 
	('Intensive care medicine'), 
	('Clinical oncology'), 
	('Additional dental specialties'),
	('Dental and maxillofacial radiology');


INSERT INTO hospital(name, latitude, longitude, available_beds, address1, address2, address3, city, county, postcode, phone, email) VALUES 
	('Walton Community Hospital - Virgin Care Services Ltd', 51.379997, -0.406042, 9, '', 'Rodney Road', '', 'Walton-on-Thames', 'Surrey', 'KT12 3LD', '01932 414205', ''),
	('Woking Community Hospital (Virgin Care)', 51.315132, -0.556289, 1, '', 'Heathside Road', '', 'Woking', 'Surrey', 'GU22 7HS', '01483 715911', ''),
	('North Somerset Community Partnership Cic HQ', 51.439693, -2.840069, 8, 'Castlewood', 'Tickenham Road', '', 'Clevedon', 'Avon', 'BS21 6AB', '', ''),
	('North Somerset Community Hospital', 51.437195, -2.847193, 0, 'North Somerset Community Hospital', 'Old Street', '', 'Clevedon', 'Avon', 'BS21 6BS', '01275 872212', ''),
	('Bridgewater Hospital', 53.459743, -2.245469, 10, '120 Princess Road', '', '', 'Manchester', 'Greater Manchester', 'M15 5AT', '0161 2270000', '');


INSERT INTO mtm_speciality_hospital(speciality_id, hospital_id) VALUES 
    (1,1),
    (2,2),
    (3,3),
    (4,4),
    (5,5);


