
insert into event_category values(1,"Project Management Clinic", "ตารางนัดหมายนี้ใช้สำหรับนัดหมาย project management clinic ในวิชา INT221 integrated project I ให้นักศึกษาเตรียมเอกสารที่เกี่ยวข้องเพื่อแสดงระหว่างขอคำปรึกษา", 30);
insert into event_category values(2,"DevOps/Infra Clinic", "Use this event category for DevOps/Infra clinic.", 30);
insert into event_category values(3,"Database Clinic", "ตารางนัดหมายนี้ใช้สำหรับนัดหมาย database clinic ในวิชา INT221 integrated project I", 30);
insert into event_category values(4,"Client-side Clinic", "ตารางนัดหมายนี้ใช้สำหรับนัดหมาย client-side clinic ในวิชา INT221 integrated project I", 30);
insert into event_category values(5,"Server-side Clinic", null, 30);

insert into event values(1, "Somchai Jaidee (OR-7)", "somchai.jai@mail.kmutt.ac.th", "2022-12-24 00:00:00", 30, "", 1, null);
insert into event values(2, "Somsri Rakdee (SJ-3)", "komkrid.rak@mail.kmutt.ac.th", "2022-12-25 00:00:00", 30, "", 2, null);
insert into event values(3, "Somsri Rakdee (SJ-3)", "teststudent2@mail.kmutt.ac.th", "2022-12-23 17:00:00", 30, "เลท 30 นาที ติดธุระ", 3 , null);
insert into event values(4, "Somsri Rakdee (SJ-3)", "teststudent2@mail.kmutt.ac.th", "2022-11-24 00:00:00", 30, "", 4, null);
insert into event values(5, "Somchai Jaidee (OR-7)", "teststudent@mail.kmutt.ac.th", "2022-11-25 00:00:00", 30, "", 5, null);
insert into event values(6, "Somchai Jaidee (OR-7)", "teststudent@mail.kmutt.ac.th", "2022-12-23 17:00:00", 30, "เลท 30 นาที ติดธุระ", 1 , null);
insert into event values(7, "Somchai Jaidee (OR-7)", "teststudent@mail.kmutt.ac.th", "2022-12-24 00:00:00", 30, "", 2, null);
insert into event values(8, "Somchai Jaidee (OR-7)", "teststudent@mail.kmutt.ac.th", "2022-11-25 00:00:00", 30, "", 1, null);
insert into event values(9, "Somchai Jaidee (OR-7)", "teststudent@mail.kmutt.ac.th", "2022-12-23 17:00:00", 30, "เลท 30 นาที ติดธุระ", 2 , null);
insert into event values(10, "Somchai Jaidee (OR-7)", "somchai.jai@mail.kmutt.ac.th", "2022-12-24 00:00:00", 30, "", 5, null);
insert into event values(11, "Somsri Rakdee (SJ-3)", "komkrid.rak@mail.kmutt.ac.th", "2022-12-01 00:00:00", 30, "", 1, null);
insert into event values(12, "Somsri Rakdee (SJ-3)", "teststudent2@mail.kmutt.ac.th", "2022-12-02 17:00:00", 30, "เลท 30 นาที ติดธุระ", 3 , null);
insert into event values(13, "Somsri Rakdee (SJ-3)", "teststudent2@mail.kmutt.ac.th", "2022-11-03 00:00:00", 30, "", 4, null);
insert into event values(14, "Somchai Jaidee (OR-7)", "teststudent@mail.kmutt.ac.th", "2022-11-04 00:00:00", 30, "", 5, null);
insert into event values(15, "Somchai Jaidee (OR-7)", "teststudent@mail.kmutt.ac.th", "2022-12-05 17:00:00", 30, "เลท 30 นาที ติดธุระ", 3 , null);
insert into event values(16, "Somchai Jaidee (OR-7)", "teststudent@mail.kmutt.ac.th", "2022-12-06 00:00:00", 30, "", 2, null);
insert into event values(17, "Somchai Jaidee (OR-7)", "teststudent@mail.kmutt.ac.th", "2022-11-07 00:00:00", 30, "", 1, null);
insert into event values(18, "Somchai Jaidee (OR-7)", "teststudent@mail.kmutt.ac.th", "2022-12-10 17:00:00", 30, "เลท 30 นาที ติดธุระ", 3 , null);

-- password: testadmin 
insert into user(name, email, role, password) values("Test Admin", "testadmin@gmail.com", "admin", "$argon2id$v=19$m=65536,t=22,p=1$RXEdKF0LEOGdw4NHvAHFDQ$pxopU4S+2nplQ0EAffCNFg");
-- password: teststudent
insert into user(name, email, role, password) values("Test Student", "teststudent@gmail.com", "student", "$argon2id$v=19$m=65536,t=22,p=1$8UwRcOQL4cDywElx+MYzVw$rPSwObuPmgD/j2+YFYiINg");
-- password: testlecturer
insert into user(name, email, role, password) values("Test Lecturer1", "testlecturer@gmail.com", "lecturer", "$argon2id$v=19$m=65536,t=22,p=1$vcedMZfvcXobNEJ4Ke9DTQ$1mIf9E504GMfxXrt1Tptxg");
-- password: testlecturer2
insert into user(name, email, role, password) values("Test Lecture2", "testlecturer2@mail.kmutt.ac.th", "lecturer", "$argon2id$v=19$m=65536,t=22,p=1$vcedMZfvcXobNEJ4Ke9DTQ$1mIf9E504GMfxXrt1Tptxg");
-- password: testlecturer3
insert into user(name, email, role, password) values("Test Lecture3", "testlecturer3@mail.kmutt.ac.th", "lecturer", "$argon2id$v=19$m=65536,t=22,p=1$vcedMZfvcXobNEJ4Ke9DTQ$1mIf9E504GMfxXrt1Tptxg");
-- password: testlecturer4
insert into user(name, email, role, password) values("Test Lecturer4", "testlecturer4@mail.kmutt.ac.th", "lecturer", "$argon2id$v=19$m=65536,t=22,p=1$vcedMZfvcXobNEJ4Ke9DTQ$1mIf9E504GMfxXrt1Tptxg");
-- password: testadmin 
insert into user(name, email, role, password) values("Test Admin2", "testadmin@mail.kmutt.ac.th", "admin", "$argon2id$v=19$m=65536,t=22,p=1$RXEdKF0LEOGdw4NHvAHFDQ$pxopU4S+2nplQ0EAffCNFg");
-- password: teststudent
insert into user(name, email, role, password) values("Test Student1", "teststudent@mail.kmiutt.ac.th", "student", "$argon2id$v=19$m=65536,t=22,p=1$8UwRcOQL4cDywElx+MYzVw$rPSwObuPmgD/j2+YFYiINg");
-- password: teststudent
insert into user(name, email, role, password) values("Test Student2", "teststudent2@mail.kmutt.ac.th", "student", "$argon2id$v=19$m=65536,t=22,p=1$8UwRcOQL4cDywElx+MYzVw$rPSwObuPmgD/j2+YFYiINg");
-- password: testlecturer
insert into user(name, email, role, password) values("Test Lecturer", "testlecturer@mail.kmutt.ac.th", "lecturer", "$argon2id$v=19$m=65536,t=22,p=1$vcedMZfvcXobNEJ4Ke9DTQ$1mIf9E504GMfxXrt1Tptxg");


-- insert into file values (1, "testpic.jpg", "/downloadFile/taurCHoF", "612507");

select * from event_category_owner;
insert into event_category_owner values(1,3);
insert into event_category_owner values(2,3);
insert into event_category_owner values(4,4);
insert into event_category_owner values(1,5);
insert into event_category_owner values(3,5);
insert into event_category_owner values(4,1);
insert into event_category_owner values(1,4);
insert into event_category_owner values(3,2);
