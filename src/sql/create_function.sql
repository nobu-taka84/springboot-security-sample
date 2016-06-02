--------------------------------------------------
-- CREATE FUNCTION
--------------------------------------------------

CREATE OR REPLACE FUNCTION decrypt_null_on_err(data bytea, key bytea, iv bytea, type text, encode text) RETURNS text AS $$
BEGIN
  RETURN convert_from(decrypt_iv(data, key, iv, type), encode);
EXCEPTION
  WHEN OTHERS THEN
    RAISE DEBUG USING
       MESSAGE = format('Decryption failed: SQLSTATE %s, Msg: %s', SQLSTATE,SQLERRM),
       HINT = 'encrypt(...) failed; check your key',
       ERRCODE = SQLSTATE;
    RETURN NULL;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION decrypt_bytea_null_on_err(data bytea, key bytea, iv bytea, type text) RETURNS bytea AS $$
BEGIN
  RETURN decrypt_iv(data, key, iv, type);
EXCEPTION
  WHEN OTHERS THEN
    RAISE DEBUG USING
       MESSAGE = format('Decryption failed: SQLSTATE %s, Msg: %s', SQLSTATE,SQLERRM),
       HINT = 'encrypt(...) failed; check your key',
       ERRCODE = SQLSTATE;
    RETURN NULL;
END;
$$ LANGUAGE plpgsql;

