package diary.bean;

import java.io.Serializable;

/**
 * ユーザ認証情報を管理するJavaBean：profileテーブルの1件分のレコードを管理
 */
public class ProfileBean implements Serializable {

	/**
	 * フィールド
	 */
	private int id;          // ユーザID
	private String email;    // 電子メールアドレス
	private String password; // パスワード
	private String name;     // ユーザ名
	
	/**
	 * デフォルトコンストラクタ
	 */
	public ProfileBean() {}

	/**
	 * コンストラクタ
	 * @param id       ユーザID
	 * @param email    電子メールアドレス
	 * @param password パスワード
	 * @param name     ユーザ名
	 */
	public ProfileBean(int id, String email, String password, String name) {
		this.id = id;
		this.email = email;
		this.password = password;
		this.name = name;
	}

	/**
	 * コンストラクタ
	 * @param email    電子メールアドレス
	 * @param password パスワード
	 */
	public ProfileBean(String email, String password) {
		this.email = email;
		this.password = password;
	}

	/** アクセサメソッド群 */
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ProfileBean [");
		builder.append("id=" + id + ", ");
		builder.append("email=" + email + ", ");
		builder.append("password=" + password + ", ");
		builder.append("name=" + name + "]");
		return builder.toString();
	}
	
}
