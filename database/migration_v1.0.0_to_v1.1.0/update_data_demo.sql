-- Create user
insert into public.registered_user (email, first_name, last_name, "password",roles)
VALUES
  (
  	'agetten.dev@gmail.com',
    'user',
    'userLastName',
    '$2a$10$1nAVWVhiESnNhYSTMr03N.w2tR0zeqS5YYq9rK7Atb503qa7ksc8K',
    'USER'
  ),
  (
  	'dvalat.dev@gmail.com',
    'user',
    'userLastName',
    '$2a$10$1nAVWVhiESnNhYSTMr03N.w2tR0zeqS5YYq9rK7Atb503qa7ksc8K',
    'USER'
  ),
  (
  	'slassy.dev@gmail.com',
    'user',
    'userLastName',
    '$2a$10$1nAVWVhiESnNhYSTMr03N.w2tR0zeqS5YYq9rK7Atb503qa7ksc8K',
    'USER'
  );
 
 -- Create borrow
 
 INSERT INTO public.borrow 
(book_returned, borrow_date, extended_duration, return_date, book_id, library_id, registered_user_id) 
values
(
    false,
    '2020-01-01',
    false,
    '2020-02-01',
    1,
    1,
    2
  ),
  (
    false,
    '2020-02-17',
    false,
    '2020-03-14',
    3,
    1,
    3
  );


-- Create Reservation

 insert into public.reservation
(avalaibility_date, notification_is_sent, book_id, library_id, registered_user_id, position)
VALUES
(null, false, 1, 1, 3, 1),
(null, false, 1, 1, 2, 2);


-- update AvailableCopie

update public.available_copie 
set nearest_return_date = '2020-02-01', reservation_count = 2
where book_id = 1 and library_id = 1;

update public.available_copie 
set nearest_return_date = '2020-02-14'
where book_id = 2 and library_id = 1;

update public.available_copie 
set nearest_return_date = '2020-05-14'
where book_id = 3 and library_id = 1;