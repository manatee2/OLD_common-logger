--
-- Delete the Tables used for the Manatee2 Logger
--
-- Usage:
--   psql -U manateeuser -d manateedb -f drop_tables.sql
--

-- Drop Tables
\echo 'Dropping Tables.'
DROP TABLE log_messages;
