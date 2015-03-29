CREATE TABLE IF NOT EXISTS books
(
  isbn varchar(32) PRIMARY KEY,
  b_name varchar(32) NOT NULL
)

CREATE TABLE IF NOT EXISTS users
(
  u_id integer(8) AUTO_INCREMENT PRIMARY KEY,
  u_un varchar(16) UNIQUE NOT NULL,
  u_pw varchar(32) NOT NULL
)

Create table IF NOT EXISTS cartitems
(
  u_id integer(8),
  isbn varchar(32),
  b_num integer(8),
  primary key (u_id, isbn),
  foreign key (u_id) references users(u_id),
  foreign key(isbn) references books(isbn)
)
Create table IF NOT EXISTS orders
(
  o_id integer(8) AUTO_INCREMENT PRIMARY KEY,
  u_id integer(8),
  foreign key(u_id) references users(u_id) 
)
Create table IF NOT EXISTS orderitems
(
  o_id integer(8),
  isbn varchar(32),
  primary key(o_id, isbn),
  foreign key (o_id) references orders(o_id),
  foreign key(isbn) references books(isbn)
)