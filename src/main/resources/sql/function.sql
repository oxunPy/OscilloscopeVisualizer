-- Bunda funksiyalar turadi va programma ishga tushganda bularni ham yozadi. ortiqcha narsalar yozmaslikka harakat qiling!!!!
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