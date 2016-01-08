--
-- Create the Database used for the Manatee2 Logger
--
-- Usage:
--   psql -U postgres -f create_database.sql
--
-- Note:
--   The following directories must exist before this script is executed:
--     C:\COTS\PostgreSQL\pgdata-app_tblspc\manatee_data
--     C:\COTS\PostgreSQL\pgdata-app_tblspc\manatee_indx
--

-- Create User
\echo 'Creating User'
CREATE USER manateeuser
WITH PASSWORD 'manateepassword';

-- Create Tablespaces
\echo 'Creating Tablespaces'
CREATE TABLESPACE manatee_data
LOCATION 'C:\COTS\PostgreSQL\pgdata-app_tblspc\manatee_data';
CREATE TABLESPACE manatee_indx
LOCATION 'C:\COTS\PostgreSQL\pgdata-app_tblspc\manatee_indx';

-- Create Database
\echo 'Creating Database.'
CREATE DATABASE manateedb
WITH ENCODING='UTF8'
OWNER=manateeuser
TABLESPACE=manatee_data;

-- Grant Privileges.
\echo 'Granting dPrivileges.'
GRANT ALL
ON DATABASE manateedb
TO manateeuser
WITH GRANT OPTION;
GRANT CREATE
ON TABLESPACE manatee_indx
TO manateeuser;