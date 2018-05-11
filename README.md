# COM2002-GROUP 34
Dental System


# Running the sql scripts
## From MySQL WorkBench
1. Start Server
2. File > Open SQL Script (or Ctrl+shit+O)
3. Find the sql script to run
4. Query > Execute (All or Selection) 

# Using The Database Class
- ConnectToDB
    - Takes a MySQL username and password and connects to the server on localhost
    - and uses the table dentistdb
- performSearch
    - can take just an SQL query and fetch results
    - or you can tell it what you want, with a table and condition
    - can do simple joins.
- performInsert
    - Used to insert an entry into the db
    - Takes an object or just pure sql.
- performUpdate
    - Takes an object or SQL an updates it's entry in the db.
    - can also give the SQL %s instead of values, and pass the actual values as an array of strings.
