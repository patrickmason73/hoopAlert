USE hoop_alert;

-- Check the timezone setting
SELECT @@global.time_zone, @@session.time_zone;

-- Check the current time according to the MySQL server
SELECT NOW();
