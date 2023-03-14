CREATE OR REPLACE FUNCTION insert_osci_data(data numeric)
    RETURNS INTEGER AS $$
DECLARE
    result_id integer default 0;
BEGIN
    INSERT INTO osci_data(created, status, data)
    VALUES(NOW(), 'CREATED', $1) RETURNING ID INTO result_id;

    return result_id;
END;
$$ LANGUAGE plpgsql;