/* �e�[�u���̍폜 */
DROP TABLE IF EXISTS article;

/* �e�[�u����` */
CREATE TABLE article (
  article_id   SERIAL    PRIMARY KEY
  , title      TEXT      NOT NULL
  , content    TEXT      NOT NULL
  , created_at TIMESTAMP NOT NULL
  , user_id    INTEGER   NOT NULL
);
-- ALTER TABLE�ɂ��O���L�[����̐ݒ�
ALTER TABLE article ADD FOREIGN KEY (user_id) REFERENCES profile(user_id);

/* �T���v�����R�[�h�̓o�^ */
INSERT INTO article (title, content, created_at, user_id) VALUES ('���C�J�n', '���ꂩ��Z�p���K�����ăG���W�j�A��ڎw���܂��I', '2022-04-01', 1);
INSERT INTO article (title, content, created_at, user_id) VALUES ('IT��b', '�C���^�[�l�b�g�̎d�g�݂ƃf�[�^�̗����׋����܂���', '2022-04-02', 1);
INSERT INTO article (title, content, created_at, user_id) VALUES ('�v���O���~���O', '���������J��Ԃ������ɂ���ĕ����L�����Ă��܂���', '2022-04-12', 1);
INSERT INTO article (title, content, created_at, user_id) VALUES ('�y�b�g�̏Љ�', '�L�� 2 �C�����Ă��܂�', '2022-04-01', 2);
INSERT INTO article (title, content, created_at, user_id) VALUES ('�����̓V�C', '�ˑR�̂ɂ킩�J�ł���', '2022-04-02', 2);
-- �ǉ����R�[�h�F�ۑ�d�l�ɋL�ڂ���Ă��Ȃ����R�[�h

/* ���R�[�h�m�F�pSELECT�� */
SELECT *  FROM article;
