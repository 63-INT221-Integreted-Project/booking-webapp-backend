insert into event_category values(1,"Project Management Clinic", "ตารางนัดหมายนี้ใช้สำหรับนัดหมาย project management clinic ในวิชา INT221 integrated project I ให้นักศึกษาเตรียมเอกสารที่เกี่ยวข้องเพื่อแสดงระหว่างขอคำปรึกษา", 30);
insert into event_category values(2,"DevOps/Infra Clinic", "Use this event category for DevOps/Infra clinic.", 30);
insert into event_category values(3,"Database Clinic", "ตารางนัดหมายนี้ใช้สำหรับนัดหมาย database clinic ในวิชา INT221 integrated project I", 30);
insert into event_category values(4,"Client-side Clinic", "ตารางนัดหมายนี้ใช้สำหรับนัดหมาย client-side clinic ในวิชา INT221 integrated project I", 30);
insert into event_category values(5,"Server-side Clinic", null, 30);

insert into event values(1, "Somchai Jaidee (OR-7)", "somchai.jai@mail.kmutt.ac.th", "2022-04-24 00:00:00", 30, "", 1);
insert into event values(2, "Somsri Rakdee (SJ-3)", "somsri.rak@mail.kmutt.ac.th", "2022-04-25 00:00:00", 30, "", 2);
insert into event values(3, "Somkiat TT-4", "somkiat.kay@kmutt.ac.th", "2022-04-23 17:00:00", 30, "ปรึกษาเพื่อนไม่ช่วยงาน", 3);


insert into user(name, email, role, password) values("John Wick", "johnwick2@gmail.com", "admin", "$argon2id$v=19$m=65536,t=22,p=1$OTMD9u2lz666cIa5AmC8lw$Y/RkvRVM/6dRkib8ZqfoDg");
-- รหัสคือ somkiat.kay007
insert into user(name, email, role, password) values("สมเกียรติ ขยันเรียน", "somkiat007y@kmutt.ac.th", "admin", "$argon2id$v=19$m=65536,t=22,p=1$OTMD9u2lz666cIa5AmC8lw$Y/RkvRVM/6dRkib8ZqfoDg");
insert into user(name, email, role, password) values("สมเกียรติ ขยันเรียน", "somkiat007y@kmutt.ac.th", "admin", "$argon2id$v=19$m=65536,t=22,p=1$OTMD9u2lz666cIa5AmC8lw$Y/RkvRVM/6dRkib8ZqfoDg");
