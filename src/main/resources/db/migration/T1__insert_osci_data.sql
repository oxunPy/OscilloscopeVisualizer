CREATE OR REPLACE FUNCTION insert_osci_data(date timestamp without time zone, dataname varchar, file_id bigint, osci_tool_id bigint)
    RETURNS INTEGER AS $$
DECLARE
    result_id integer default 0;
BEGIN
    INSERT INTO osci_data(created, status, date, dataname, file_id, osci_tool_id)
    VALUES(NOW(), 'CREATED', $1, $2, $3, $4) RETURNING ID INTO result_id;

    return result_id;
END;
$$ LANGUAGE plpgsql;