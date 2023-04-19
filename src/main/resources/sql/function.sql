-- #getSecretUser
CREATE OR REPLACE FUNCTION getSecretUser()
  RETURNS TABLE(id integer, firstname varchar, lastname varchar, middlename varchar, printableName varchar) AS
$BODY$
BEGIN
  RETURN QUERY(
      select
          u.id,
          u.firstname,
          u.lastname,
          u.middlename,
          u.middlename
      from osci_user u
      where u.login = 'secret'
  );
END;
$BODY$
LANGUAGE plpgsql VOLATILE
COST 100;
-- #insertFile
CREATE OR REPLACE FUNCTION insertFile(data_size bigint, file_path varchar, file_name varchar, original_name varchar, file_type varchar)
    RETURNS BIGINT AS
$BODY$
DECLARE
    result bigint default 0;
BEGIN
    INSERT INTO osci_file(created, status, data_size, file_path, file_name, original_name)
    VALUES(now(), 'CREATED', $1, $2, $3, $4) RETURNING id INTO result;
    RETURN result;
END;
$BODY$
LANGUAGE plpgsql VOLATILE
COST 100;
END;