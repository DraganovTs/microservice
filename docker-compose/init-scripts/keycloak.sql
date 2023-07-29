-- keycloak.sql

-- Create the 'keycloak' schema if it doesn't exist
CREATE SCHEMA IF NOT EXISTS keycloak;

-- Create other tables and database objects for Keycloak, using 'keycloak' schema
CREATE TABLE keycloak.databasechangeloglock (
                                                ID INTEGER NOT NULL,
                                                LOCKED BOOLEAN NOT NULL,
                                                LOCKGRANTED TIMESTAMP WITHOUT TIME ZONE,
                                                LOCKEDBY VARCHAR(255),
                                                CONSTRAINT databasechangeloglock_pkey PRIMARY KEY (ID)
);

-- Add more CREATE TABLE and other DDL statements for other Keycloak tables
-- ...
