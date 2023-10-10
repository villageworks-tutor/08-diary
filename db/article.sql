/* テーブルの削除 */
DROP TABLE IF EXISTS article;

/* テーブル定義 */
CREATE TABLE article (
  article_id   SERIAL    PRIMARY KEY
  , title      TEXT      NOT NULL
  , content    TEXT      NOT NULL
  , created_at TIMESTAMP NOT NULL
  , user_id    INTEGER   NOT NULL
);
-- ALTER TABLEによる外部キー制約の設定
ALTER TABLE article ADD FOREIGN KEY (user_id) REFERENCES profile(user_id);

/* サンプルレコードの登録 */
INSERT INTO article (title, content, created_at, user_id) VALUES ('研修開始', 'これから技術を習得してエンジニアを目指します！', '2022-04-01', 1);
INSERT INTO article (title, content, created_at, user_id) VALUES ('IT基礎', 'インターネットの仕組みとデータの流れを勉強しました', '2022-04-02', 1);
INSERT INTO article (title, content, created_at, user_id) VALUES ('プログラミング', '条件分岐や繰り返し処理によって幅が広がってきました', '2022-04-12', 1);
INSERT INTO article (title, content, created_at, user_id) VALUES ('ペットの紹介', '猫を 2 匹飼っています', '2022-04-01', 2);
INSERT INTO article (title, content, created_at, user_id) VALUES ('今日の天気', '突然のにわか雨でした', '2022-04-02', 2);
-- 追加レコード：課題仕様に記載されていないレコード

/* レコード確認用SELECT文 */
SELECT *  FROM article;
