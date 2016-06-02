--------------------------------------------------
-- CREATE FUNCTION
--------------------------------------------------

-- トリガ関数
CREATE OR REPLACE FUNCTION triggrer_function_date_updater()
  RETURNS trigger AS
$BODY$BEGIN  
  IF (TG_OP = 'INSERT') THEN  
    NEW.updated_at := CURRENT_TIMESTAMP;  
    NEW.created_at := CURRENT_TIMESTAMP;  
  ELSIF (TG_OP = 'UPDATE') THEN  
    NEW.updated_at := CURRENT_TIMESTAMP;  
  END IF;  
  RETURN NEW;  
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;



--------------------------------------------------
-- CREATE TABLE AUTH
--------------------------------------------------

-- ログイン履歴
DO $$
BEGIN
    CREATE TABLE login_history (
      id bigserial NOT NULL, -- ID
      user_info_id bigint NOT NULL, -- ユーザID
      ip_address character varying(64), -- IPアドレス
      user_agent character varying(128), -- UA
      logined_at timestamp without time zone NOT NULL, -- ログイン日時
      CONSTRAINT login_history_pkc PRIMARY KEY (id)
    );
    
    COMMENT ON TABLE login_history IS 'ログイン履歴';
    COMMENT ON COLUMN login_history.id IS 'ID';
    COMMENT ON COLUMN login_history.user_info_id IS 'ユーザID';
    COMMENT ON COLUMN login_history.ip_address IS 'IPアドレス';
    COMMENT ON COLUMN login_history.user_agent IS 'UA';
    COMMENT ON COLUMN login_history.logined_at IS 'ログイン日時';
EXCEPTION
    WHEN duplicate_table THEN RAISE NOTICE 'table login_history already exists.';
END $$;

-- ユーザ情報
DO $$
BEGIN
    CREATE TABLE user_info (
      id bigint NOT NULL, -- ID
      username bytea NOT NULL, -- ユーザ名
      password bytea NOT NULL, -- パスワード
      last_logined_at timestamp without time zone, -- 前回ログイン日時
      password_valid_term date, -- パスワード有効期限
      miss_count smallint NOT NULL DEFAULT 0, -- 連続認証失敗回数
      delete_flg boolean NOT NULL DEFAULT false, -- 削除フラグ
      created_at timestamp without time zone NOT NULL, -- 作成日時
      created_by character varying, -- 作成者
      updated_at timestamp without time zone NOT NULL, -- 更新日時
      updated_by character varying, -- 更新者
      CONSTRAINT user_info_pkc PRIMARY KEY (id)
    );
    
    COMMENT ON TABLE user_info IS 'ユーザ情報';
    COMMENT ON COLUMN user_info.id IS 'ID';
    COMMENT ON COLUMN user_info.username IS 'ユーザ名';
    COMMENT ON COLUMN user_info.password IS 'パスワード';
    COMMENT ON COLUMN user_info.last_logined_at IS '前回ログイン日時';
    COMMENT ON COLUMN user_info.password_valid_term IS 'パスワード有効期限';
    COMMENT ON COLUMN user_info.miss_count IS '連続認証失敗回数';
    COMMENT ON COLUMN user_info.delete_flg IS '削除フラグ';
    COMMENT ON COLUMN user_info.created_at IS '作成日時';
    COMMENT ON COLUMN user_info.created_by IS '作成者';
    COMMENT ON COLUMN user_info.updated_at IS '更新日時';
    COMMENT ON COLUMN user_info.updated_by IS '更新者';
    
    CREATE TRIGGER triggrer_date_updater 
    BEFORE INSERT OR UPDATE ON user_info 
    FOR EACH ROW EXECUTE PROCEDURE triggrer_function_date_updater();
EXCEPTION
    WHEN duplicate_table THEN RAISE NOTICE 'table user_info already exists.';
END $$;


-- ユーザ情報ID採番
DO $$
BEGIN
    CREATE TABLE user_info_id_generator (
      pk character varying(100) NOT NULL, -- IDキー
      value bigint NOT NULL, -- ID現在値
      CONSTRAINT user_info_id_generator_pkc PRIMARY KEY (pk)
    );
    
    COMMENT ON TABLE user_info_id_generator IS 'ユーザ情報ID採番';
    COMMENT ON COLUMN user_info_id_generator.pk IS 'IDキー';
    COMMENT ON COLUMN user_info_id_generator.value IS 'ID現在値';
EXCEPTION
    WHEN duplicate_table THEN RAISE NOTICE 'table user_info_id_generator already exists.';
END $$;

-- ユーザ権限情報
DO $$
BEGIN
    CREATE TABLE user_privilege_info (
      id bigserial NOT NULL,
      user_info_id bigint NOT NULL, -- ユーザID
      user_privilege character varying(64) NOT NULL, -- 権限区分
      version bigint, -- バージョン
      created_at timestamp without time zone NOT NULL, -- 作成日時
      created_by character varying, -- 作成者
      updated_at timestamp without time zone NOT NULL, -- 更新日時
      updated_by character varying, -- 更新者
      CONSTRAINT user_privilege_info_pkc PRIMARY KEY (id)
    );
    
    COMMENT ON TABLE user_privilege_info IS 'ユーザ権限情報';
    COMMENT ON COLUMN user_privilege_info.user_info_id IS 'ユーザID';
    COMMENT ON COLUMN user_privilege_info.user_privilege IS '権限区分';
    COMMENT ON COLUMN user_privilege_info.version IS 'バージョン';
    COMMENT ON COLUMN user_privilege_info.created_at IS '作成日時';
    COMMENT ON COLUMN user_privilege_info.created_by IS '作成者';
    COMMENT ON COLUMN user_privilege_info.updated_at IS '更新日時';
    COMMENT ON COLUMN user_privilege_info.updated_by IS '更新者';
    
    CREATE TRIGGER triggrer_date_updater 
    BEFORE INSERT OR UPDATE ON user_privilege_info 
    FOR EACH ROW EXECUTE PROCEDURE triggrer_function_date_updater();
EXCEPTION
    WHEN duplicate_table THEN RAISE NOTICE 'table user_privilege_info already exists.';
END $$;


-- pgcrypto の設定
CREATE EXTENSION IF NOT EXISTS pgcrypto;


-- ユーザ情報ID採番に初期値セット
DO $$
BEGIN
    INSERT INTO user_info_id_generator (pk, value) VALUES ('user_info_id', 0);
EXCEPTION
    WHEN unique_violation THEN RAISE NOTICE 'EXTENSION pgcrypto already exists';
END $$;

