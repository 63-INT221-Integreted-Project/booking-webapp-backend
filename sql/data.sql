insert into event_category values(1,"Project Management Clinic", "ตารางนัดหมายนี้ใช้สำหรับนัดหมาย project management clinic ในวิชา INT221 integrated project I ให้นักศึกษาเตรียมเอกสารที่เกี่ยวข้องเพื่อแสดงระหว่างขอคำปรึกษา", 30);
insert into event_category values(2,"DevOps/Infra Clinic", "Use this event category for DevOps/Infra clinic.", 30);
insert into event_category values(3,"Database Clinic", "ตารางนัดหมายนี้ใช้สำหรับนัดหมาย database clinic ในวิชา INT221 integrated project I", 30);
insert into event_category values(4,"Client-side Clinic", "ตารางนัดหมายนี้ใช้สำหรับนัดหมาย client-side clinic ในวิชา INT221 integrated project I", 30);
insert into event_category values(5,"Server-side Clinic", null, 30);

insert into event values(1, "Somchai Jaidee (OR-7)", "somchai.jai@mail.kmutt.ac.th", "2022-04-24 00:00:00", 30, "", 1, null);
insert into event values(6, "Somsri Rakdee (SJ-3) 123", "komkrid.rak@mail.kmutt.ac.th", "2022-08-25 00:00:00", 30, "", 1, null);
insert into event values(3, "Somkiat TT-4", "somkiat.kay@kmutt.ac.th", "2022-04-23 17:00:00", 30, "เลท 30 นาที ติดธุระ", 3 , null);

-- password: testadmin 
insert into user(name, email, role, password) values("Test Admin", "testadmin@gmail.com", "admin", "$argon2id$v=19$m=65536,t=22,p=1$RXEdKF0LEOGdw4NHvAHFDQ$pxopU4S+2nplQ0EAffCNFg");
-- password: teststudent
insert into user(name, email, role, password) values("Test Student", "teststudent@gmail.com", "student", "$argon2id$v=19$m=65536,t=22,p=1$8UwRcOQL4cDywElx+MYzVw$rPSwObuPmgD/j2+YFYiINg");
-- password: testlecturer
insert into user(name, email, role, password) values("Test Lecturer", "testlecturer@gmail.com", "lecturer", "$argon2id$v=19$m=65536,t=22,p=1$vcedMZfvcXobNEJ4Ke9DTQ$1mIf9E504GMfxXrt1Tptxg");


insert into file values (1, "testpic.jpg", "/downloadFile/taurCHoF", "612507");
