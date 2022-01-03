CREATE FUNCTION evaluation_saved()
    RETURNS TRIGGER
AS
$$
BEGIN
    PERFORM pg_notify('EVALUATION_SAVED', row_to_json(NEW)::text);
RETURN NULL;
END;
$$
LANGUAGE plpgsql;

CREATE TRIGGER valoration_saved_trigger
    AFTER INSERT OR UPDATE
    ON evaluation
    FOR EACH ROW
EXECUTE PROCEDURE evaluation_saved();
