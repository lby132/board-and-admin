-- 테스트 계정
-- TODO: 테스트용이지만 비밀번호가 노출된 데이터 세팅. 개선하는 것이 좋을 지 고민해 보자.
insert into user_account (user_id, user_password, role_types, nickname, email, memo, created_at, created_by, modified_at, modified_by)
values ('uno', '{noop}asdf1234', 'ADMIN', 'Uno', 'uno@mail.com', 'I am Uno.', now(), 'uno', now(), 'uno'),
       ('mark', '{noop}asdf1234', 'MANAGER', 'Uno', 'uno1@mail.com', 'I am Uno.', now(), 'uno', now(), 'uno'),
       ('susan', '{noop}asdf1234', 'MANAGER,DEVELOPER', 'Uno', 'uno2@mail.com', 'I am Uno.', now(), 'uno', now(), 'uno'),
       ('jim', '{noop}asdf1234', 'USER', 'Uno', 'uno3@mail.com', 'I am Uno.', now(), 'uno', now(), 'uno')
;