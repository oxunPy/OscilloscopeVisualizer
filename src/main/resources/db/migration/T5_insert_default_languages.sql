-- #insertDefaultLanguages
INSERT INTO osci_language(created, status, name, code, flag_file_id)
VALUES(now(), 'ACTIVE', 'English', 'en', (select id from osci_file where flag_code = 'en')),
      (now(), 'ACTIVE', 'Uzbek', 'uz', (select id from osci_file where flag_code = 'uz')),
      (now(), 'ACTIVE', 'Русский', 'ru', (select id from osci_file where flag_code = 'ru'));