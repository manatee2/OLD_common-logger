--
-- Create the Tables used for the Manatee2 Logger
--
-- Usage:
--   psql -U manateeuser -d manateedb -f create_tables.sql
--

-- Create Tables
\echo 'Creating Tables'
CREATE TABLE log_messages
(
	ID					BIGSERIAL		CONSTRAINT LOG_MESSAGE_PK PRIMARY KEY USING INDEX TABLESPACE manatee_indx,
	TIMESTAMP			TIMESTAMP		NOT NULL,
	REPORTER			VARCHAR(50)		NOT NULL,
	SEVERITY			VARCHAR(10)		NOT NULL,
	TEXT				VARCHAR(1024)	NOT NULL
) TABLESPACE manatee_data;
