package diary.bean;

import java.io.Serializable;

/**
 * 検索条件のパラメータを管理するJavaBean
 */
public class SearchBean implements Serializable {

	/**
	 * フィールド
	 */
	private String keyword; // 検索キーワード
	private int    userId;  // ログインユーザのユーザ番号
	
	/**
	 * デフォルトコンストラクタ
	 */
	public SearchBean() {}

	/**
	 * コンストラクタ
	 * @param keyword  検索キーワード
	 * @param userId   ログインしているユーザのユーザ番号
	 */
	public SearchBean(String keyword, int userId) {
		this.keyword = keyword;
		this.userId  = userId;
	}

	/** アクセッサメソッド群 */
	
	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("SearchBean [");
		builder.append("keyword=" + keyword + ", ");
		builder.append("userId=" + userId + "]");
		return builder.toString();
	}
	
}
