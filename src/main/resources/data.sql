insert into task_group (id, name, status, position)
values  (1, 'TODO', 'NEW', 1),
        (2, 'In Progress', 'IN_PROGRESS', 2),
        (3, 'Done', 'COMPLETE', 3);

INSERT into task (id, title, description)
values (1, 'Pat the cat', 'patting the cat'),
       (2, 'Buy some shorts', 'Find shorts shop');

INSERT into comment (id, task_id, text, created_on)
values (1, 1, 'My first comment', '2019-11-20 12:00:00'),
       (2, 1, 'My second comment', '2019-11-20 13:00:00'),
       (3, 1, 'My third comment', '2019-11-20 14:00:00'),
       (4, 2, 'Another task', '2019-11-20 12:00:00');