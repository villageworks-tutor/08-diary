/* �e�[�u���̍폜 */
DROP TABLE IF EXISTS profile;

/* �e�[�u����` */
CREATE TABLE profile (
	  user_id  SERIAL PRIMARY KEY -- ���[�U�ԍ�
	, email    TEXT   NOT NULL    -- �d�q���[���A�h���X
	, password TEXT   NOT NULL    -- �p�X���[�h
	, name     TEXT   NOT NULL    -- ����
);

/* �T���v�����R�[�h�̓o�^ */
INSERT INTO profile (email, password, name) VALUES ('tanaka@aaa.com', 'himitu', '�c�����Y');
INSERT INTO profile (email, password, name) VALUES ('suzuki@aaa.com', 'himitu', '��؈�N');
-- �ǉ����R�[�h�F�ۑ�d�l�ɋL�ڂ���Ă��Ȃ����R�[�h
INSERT INTO profile (email, password, name) VALUES ('info@aaa.com', 'himitu', '�⍇���S��');

/* ���R�[�h�m�F�pSELECT�� */
SELECT *  FROM profile;