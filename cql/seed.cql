DROP KEYSPACE hellocompojure;
CREATE KEYSPACE IF NOT EXISTS hellocompojure WITH REPLICATION = { 'class' : 'SimpleStrategy', 'replication_factor': 1 };

USE hellocompojure;
CREATE TABLE IF NOT EXISTS comments (author text, body text, posted timestamp, id uuid PRIMARY KEY);