-- Bunda funksiyalar turadi va programma ishga tushganda bularni ham yozadi. ortiqcha narsalar yozmaslikka harakat qiling!!!!
-- #getProductOstatok
CREATE OR REPLACE FUNCTION getAdminUser(adminId bigint)
  RETURNS TABLE(id bigint, firstname varchar, lastname varchar, printableName varchar) AS
$BODY$
BEGIN
  RETURN QUERY(
      select
          u.id,
          u.first_name,
          u.last_name,
          u.printable_name
      from user u
      where u.id = $1
  );
END;
$BODY$
LANGUAGE plpgsql VOLATILE
COST 100;
ALTER FUNCTION getAdminUser(bigint)
OWNER TO myadmin;