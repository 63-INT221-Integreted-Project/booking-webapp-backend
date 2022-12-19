insert into event_category values(1,"Project Management Clinic", "ตารางนัดหมายนี้ใช้สำหรับนัดหมาย project management clinic ในวิชา INT221 integrated project I ให้นักศึกษาเตรียมเอกสารที่เกี่ยวข้องเพื่อแสดงระหว่างขอคำปรึกษา", 30);
insert into event_category values(2,"DevOps/Infra Clinic", "Use this event category for DevOps/Infra clinic.", 30);
insert into event_category values(3,"Database Clinic", "ตารางนัดหมายนี้ใช้สำหรับนัดหมาย database clinic ในวิชา INT221 integrated project I", 30);
insert into event_category values(4,"Client-side Clinic", "ตารางนัดหมายนี้ใช้สำหรับนัดหมาย client-side clinic ในวิชา INT221 integrated project I", 30);
insert into event_category values(5,"Server-side Clinic", null, 30);

insert into event values(1, "Jirachoti Aekopas", "somchai.jai@mail.kmutt.ac.th", "2022-12-24 13:00:00", 30, "", 1, null);
insert into event values(2, "Jirachoti Aekopas", "komkrid.rak@mail.kmutt.ac.th", "2022-12-25 13:00:00", 30, "", 2, null);
insert into event values(3, "Jirachoti Aekopas", "teststudent2@mail.kmutt.ac.th", "2022-12-23 17:00:00", 30, "เลท 30 นาที ติดธุระ", 3 , null);
insert into event values(4, "Jirachoti Aekopas", "teststudent2@mail.kmutt.ac.th", "2022-11-24 13:00:00", 30, "", 4, null);
insert into event values(5, "Daranpob Posamran", "teststudent@mail.kmutt.ac.th", "2022-11-25 13:00:00", 30, "", 5, null);
insert into event values(6, "Daranpob Posamran", "teststudent@mail.kmutt.ac.th", "2022-12-23 17:00:00", 30, "เลท 30 นาที ติดธุระ", 1 , null);
insert into event values(7, "Daranpob Posamran", "teststudent@mail.kmutt.ac.th", "2022-12-24 13:00:00", 30, "", 2, null);
insert into event values(8, "Daranpob Posamran", "teststudent@mail.kmutt.ac.th", "2022-11-25 09:00:00", 30, "", 1, null);
insert into event values(9, "Daranpob Posamran", "teststudent@mail.kmutt.ac.th", "2022-12-23 16:00:00", 30, "เลท 30 นาที ติดธุระ", 2 , null);
insert into event values(10, "Daranpob Posamran", "somchai.jai@mail.kmutt.ac.th", "2022-12-24 09:00:00", 30, "", 5, null);
insert into event values(11, "Somsri Rakdee (SJ-3)", "komkrid.rak@mail.kmutt.ac.th", "2022-12-01 08:00:00", 30, "", 1, null);
insert into event values(12, "Jirachoti Aekopas", "teststudent2@mail.kmutt.ac.th", "2022-12-02 10:00:00", 30, "เลท 30 นาที ติดธุระ", 3 , null);
insert into event values(13, "Jirachoti Aekopas", "teststudent2@mail.kmutt.ac.th", "2022-11-03 11:00:00", 30, "", 4, null);
insert into event values(14, "Daranpob Posamran", "teststudent@mail.kmutt.ac.th", "2022-11-04 15:00:00", 30, "", 5, null);
insert into event values(15, "Daranpob Posamran", "teststudent@mail.kmutt.ac.th", "2022-12-05 17:00:00", 30, "เลท 30 นาที ติดธุระ", 3 , null);
insert into event values(16, "Daranpob Posamran", "teststudent@mail.kmutt.ac.th", "2022-12-06 07:00:00", 30, "", 2, null);
insert into event values(17, "Daranpob Posamran", "teststudent@mail.kmutt.ac.th", "2022-11-07 07:30:00", 30, "", 1, null);

insert into event values(18, "INT491 User003", "int491.user003@mail.kmutt.ac.th", "2022-12-01 17:00:00", 30, "เลท 30 นาที ติดธุระ", 5 , null);
insert into event values(19, "INT491 User003", "int491.user003@mail.kmutt.ac.th", "2022-12-02 00:00:00", 30, "", 5, null);
insert into event values(20, "INT491 User003", "int491.user003@mail.kmutt.ac.th", "2022-11-03 00:00:00", 30, "", 2, null);
insert into event values(21, "INT491 User003", "int491.user003@mail.kmutt.ac.th", "2022-12-04 17:00:00", 30, "เลท 30 นาที ติดธุระ", 4 , null);



-- password: testadmin 
insert into user(userId, name, email, role, password) values(1,"Nimman Sorapinij", "testadmin@gmail.com", "admin", "$argon2id$v=19$m=65536,t=22,p=1$RXEdKF0LEOGdw4NHvAHFDQ$pxopU4S+2nplQ0EAffCNFg");
-- password: teststudent
insert into user(userId,name, email, role, password) values(2,"Thanaree Tosila", "teststudent@gmail.com", "student", "$argon2id$v=19$m=65536,t=22,p=1$8UwRcOQL4cDywElx+MYzVw$rPSwObuPmgD/j2+YFYiINg");
-- password: testlecturer
insert into user(userId,name, email, role, password) values(3 ,"Rosanan Sritong", "testlecturer@gmail.com", "lecturer", "$argon2id$v=19$m=65536,t=22,p=1$vcedMZfvcXobNEJ4Ke9DTQ$1mIf9E504GMfxXrt1Tptxg");
-- password: testlecturer2
insert into user(userId,name, email, role, password) values(4, "Thitikorn Udomdechsakul", "testlecturer2@mail.kmutt.ac.th", "lecturer", "$argon2id$v=19$m=65536,t=22,p=1$vcedMZfvcXobNEJ4Ke9DTQ$1mIf9E504GMfxXrt1Tptxg");
-- password: testlecturer3
insert into user(userId,name, email, role, password) values(5, "Nattatham Pongwiroj", "testlecturer3@mail.kmutt.ac.th", "lecturer", "$argon2id$v=19$m=65536,t=22,p=1$vcedMZfvcXobNEJ4Ke9DTQ$1mIf9E504GMfxXrt1Tptxg");
-- password: testlecturer4
insert into user(userId,name, email, role, password) values(6, "Phuwadech Rattanaprasert", "testlecturer4@mail.kmutt.ac.th", "lecturer", "$argon2id$v=19$m=65536,t=22,p=1$vcedMZfvcXobNEJ4Ke9DTQ$1mIf9E504GMfxXrt1Tptxg");
-- password: testlecturer
insert into user(userId,name, email, role, password) values(7, "Narit Jaroenkan", "testlecturer@mail.kmutt.ac.th", "lecturer", "$argon2id$v=19$m=65536,t=22,p=1$vcedMZfvcXobNEJ4Ke9DTQ$1mIf9E504GMfxXrt1Tptxg");
-- password: testadmin 
insert into user(userId,name, email, role, password) values(8, "Tampat Krajangjit", "testadmin@mail.kmutt.ac.th", "admin", "$argon2id$v=19$m=65536,t=22,p=1$RXEdKF0LEOGdw4NHvAHFDQ$pxopU4S+2nplQ0EAffCNFg");
-- password: teststudent
insert into user(userId,name, email, role, password) values(9, "Daranpob Posamran", "teststudent@mail.kmiutt.ac.th", "student", "$argon2id$v=19$m=65536,t=22,p=1$8UwRcOQL4cDywElx+MYzVw$rPSwObuPmgD/j2+YFYiINg");
-- password: teststudent
insert into user(userId,name, email, role, password) values(10, "Jirachoti Aekopas", "teststudent2@mail.kmutt.ac.th", "student", "$argon2id$v=19$m=65536,t=22,p=1$8UwRcOQL4cDywElx+MYzVw$rPSwObuPmgD/j2+YFYiINg");

-- password: IP21student
insert into user(userId,name, email, role, password) values(11, "INT491 User003", "int491.user003@mail.kmutt.ac.th", "student", "$argon2id$v=19$m=65536,t=22,p=1$xuAYyYVpZkEFQ9CNIYEI/w$p6dDBRvNOzRCdTEAO3O6Ug");
-- password: IP21admin
insert into user(userId,name, email, role, password) values(12, "INT491 User001", "int491.user001@mail.kmutt.ac.th", "admin", "$argon2id$v=19$m=65536,t=22,p=1$zAf+ZpDKCrm5FoknUaQHoQ$KC1zhvuPpJDXV5KcHLt43A");
-- password: IP21lecturer
insert into user(userId,name, email, role, password) values(13, "INT491 User002", "int491.user002@mail.kmutt.ac.th", "lecturer", "$argon2id$v=19$m=65536,t=22,p=1$/upVHoMbeNdheVDyBJPMYA$K1DP7e+wwtvRbPQmaRA6Vg");
-- password: IP21guest
insert into user(userId,name, email, role, password) values(14, "INT491 User004", "int491.user004@mail.kmutt.ac.th", "guest", "$argon2id$v=19$m=65536,t=22,p=1$Pq0izh3oj4irxtx4r/YccQ$4YH+aHcCLa3rTJeujjWRKw");


-- insert into file values (1, "testpic.jpg", "/downloadFile/taurCHoF", "612507");

select * from event_category_owner;
insert into event_category_owner values(1,3);
insert into event_category_owner values(2,3);
insert into event_category_owner values(4,4);
insert into event_category_owner values(1,7);
insert into event_category_owner values(3,5);
insert into event_category_owner values(4,6);
insert into event_category_owner values(2,7);
insert into event_category_owner values(3,6);
insert into event_category_owner values(5,6);
