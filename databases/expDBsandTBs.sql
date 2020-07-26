CREATE DATABASE Small100_ORIG;
CREATE DATABASE Small100_target;

CREATE DATABASE Med1000_ORIG;
CREATE DATABASE Med1000_TARGET;

CREATE DATABASE Large65535_ORIG;
CREATE DATABASE Large65535_TARGET;

CREATE TABLE Small100_ORIG.data_table (
	id int,
    data_item VARCHAR(100)
);

CREATE TABLE Small100_target.data_table (
	id int,
    data_item VARCHAR(100)
);

CREATE TABLE Med1000_ORIG.data_table (
	id int,
    data_item VARCHAR(1000)
);

CREATE TABLE Med1000_TARGET.data_table (
	id int,
    data_item VARCHAR(1000)
);

CREATE TABLE Large65535_ORIG.data_table (
	id int,
    data_item VARCHAR(21840)
);

CREATE TABLE Large65535_TARGET.data_table (
	id int,
    data_item VARCHAR(21840)
);
