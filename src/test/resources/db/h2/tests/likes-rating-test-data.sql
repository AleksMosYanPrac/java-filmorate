insert into FILMS(ID,NAME,DESCRIPTION,RELEASE_DATE,DURATION,MPA_RATING)
values (1,'test','-','2020-12-12',100, 1),
       (2,'test','-','2020-10-10',100, 1);

insert into FILM_GENRE (FILM_ID,GENRE_ID)
values (1,1),(1,2),(2,1);

insert into USERS(ID,LOGIN,EMAIL,NAME,BIRTHDAY)
values (1,'user_1','a@a.com','name','2000-12-12'),
       (2,'user_2','b@b.com', null, '2000-10-10'),
       (3,'user_3','c@c.com', null, '2000-10-10');

insert into LIKES_RATING(FILM_ID,USER_ID)
values (1,1),(1,2),(1,3),
       (2,1),(2,2);