merge into genres as t
using (
values (1,'Комедия'),
       (2,'Драма'),
       (3,'Мультфильм'),
       (4,'Триллер'),
       (5,'Документальный'),
       (6,'Боевик')) s(id, name)
on t.id = s.id
when matched then update set name = s.name
when not matched then insert values (s.id, s.name);

merge into mpa_rating as m
using (
values (1,'G'),
       (2,'PG'),
       (3,'PG-13'),
       (4,'R'),
       (5,'NC-17')) f(id, name)
on m.id = f.id
when matched then update set name = f.name
when not matched then insert values (f.id, f.name);
