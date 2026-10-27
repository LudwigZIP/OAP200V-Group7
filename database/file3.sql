CREATE USER IF NOT EXISTS 'student'@'localhost' IDENTIFIED BY 'student';
CREATE USER IF NOT EXISTS 'student'@'%' IDENTIFIED BY 'student';
ALTER USER 'student'@'localhost' IDENTIFIED BY 'student';
ALTER USER 'student'@'%' IDENTIFIED BY 'student';
GRANT ALL PRIVILEGES ON private_library.* TO 'student'@'localhost';
GRANT ALL PRIVILEGES ON private_library.* TO 'student'@'%';
FLUSH PRIVILEGES;
SELECT COUNT(*) FROM private_library.book;