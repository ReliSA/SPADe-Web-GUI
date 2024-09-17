if not exists (select * from sysobjects where name='users' and xtype='U')
create table users(
    id int identity(1,1),
    email nvarchar(255) not null,
    name nvarchar(255) not null,
    password varchar(255) not null,
    PRIMARY KEY(id)
);