CREATE OR REPLACE FUNCTION get_user(login_str character varying, pass_str character varying)
    RETURNS TABLE (
                      id integer,
                      printablename character varying,
                      login character varying,
                      pass character varying) AS $$
BEGIN
    RETURN QUERY (
        select
            u.id,
            u.printablename,
            u.login,
            u.pass
        from osci_user u
        where u.login=$1 and u.pass=$2 and u.status != 'DELETED');
END;
$$ LANGUAGE plpgsql;