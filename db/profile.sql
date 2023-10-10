/* テーブルの削除 */
DROP TABLE IF EXISTS profile;

/* テーブル定義 */
CREATE TABLE profile (
	  user_id  SERIAL PRIMARY KEY -- ユーザ番号
	, email    TEXT   NOT NULL    -- 電子メールアドレス
	, password TEXT   NOT NULL    -- パスワード
	, name     TEXT   NOT NULL    -- 氏名
);

/* サンプルレコードの登録 */
INSERT INTO profile (email, password, name) VALUES ('tanaka@aaa.com', 'himitu', '田中太郎');
INSERT INTO profile (email, password, name) VALUES ('suzuki@aaa.com', 'himitu', '鈴木一朗');
-- 追加レコード：課題仕様に記載されていないレコード
INSERT INTO profile (email, password, name) VALUES ('info@aaa.com', 'himitu', '問合せ担当');

/* レコード確認用SELECT文 */
SELECT *  FROM profile;