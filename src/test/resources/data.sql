INSERT INTO users VALUES ('user1', '$2a$10$wUtdYp0GXHF5xXdICpmgDuP5kdxCILDTE9X1MJoUAFjZWsco5LeEm', 'test1@test.com', true);
INSERT INTO users VALUES ('user2', '$2a$10$wUtdYp0GXHF5xXdICpmgDuP5kdxCILDTE9X1MJoUAFjZWsco5LeEm', 'test2@test.com', true);
INSERT INTO users VALUES ('quickBrownFox', '$2a$12$CrcWGknL42aodfsrDgxuXeEBdvJCjGbWzCn1ZobCaq3KDuT1UpcCW', 'quick.fox@example.org', true);
INSERT INTO authorities VALUES ('user1', 'ROLE_ADMIN');
INSERT INTO authorities VALUES ('user2', 'ROLE_USER');
INSERT INTO authorities VALUES ('quickBrownFox', 'ROLE_ADMIN');
