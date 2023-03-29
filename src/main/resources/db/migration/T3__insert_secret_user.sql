-- #insertSecretUser
INSERT INTO osci_user(created, status, authset, usertype, info, login, pass)
VALUES(now(), 'ACTIVE', true, 'SECRET', '---', 'secret', '202CB962AC59075B964B07152D234B70')