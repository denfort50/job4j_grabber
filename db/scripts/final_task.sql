create table company
(
    id integer not null,
    name character varying,
    constraint company_pkey primary key (id)
);

create table person
(
    id integer not null,
    name character varying,
    company_id integer references company(id),
    constraint person_pkey primary key (id)
);

insert into company (id, name) values (1, 'Apple');
insert into company (id, name) values (2, 'Microsoft');
insert into company (id, name) values (3, 'IBM');
insert into company (id, name) values (4, 'Oracle');
insert into company (id, name) values (5, 'Google');

insert into person (id, name, company_id) values (1, 'Jack', 3);
insert into person (id, name, company_id) values (2, 'John', 4);
insert into person (id, name, company_id) values (3, 'Alex', 1);
insert into person (id, name, company_id) values (4, 'Denis', 4);
insert into person (id, name, company_id) values (5, 'Peter', 5);
insert into person (id, name, company_id) values (6, 'Jason', 3);
insert into person (id, name, company_id) values (7, 'Nick', 2);
insert into person (id, name, company_id) values (8, 'Ann', 4);
insert into person (id, name, company_id) values (9, 'Olga', 1);
insert into person (id, name, company_id) values (10, 'Jacob', 3);
insert into person (id, name, company_id) values (11, 'Leonardo', 5);
insert into person (id, name, company_id) values (12, 'Max', 3);
insert into person (id, name, company_id) values (13, 'Simon', 5);
insert into person (id, name, company_id) values (14, 'Harry', 5);

-- Запрос №1
select p.name, c.name from person p
join company c on p.company_id = c.id
where company_id <> 5;

-- Запрос №2
select name, people_amount
from (
    select c.name, count(*) people_amount, max(count(*)) over() max_people_amount
    from company c
    join person p on p.company_id = c.id
    group by c.name
) tmp
where people_amount = max_people_amount;
