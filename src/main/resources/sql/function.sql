-- Bunda funksiyalar turadi va programma ishga tushganda bularni ham yozadi. ortiqcha narsalar yozmaslikka harakat qiling!!!!
-- #getAdminUser
CREATE OR REPLACE FUNCTION getAdminUser(adminId bigint)
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
      where u.id = $1
  );
END;
$BODY$
LANGUAGE plpgsql VOLATILE
COST 100;
ALTER FUNCTION getAdminUser(bigint)
OWNER TO myadmin;