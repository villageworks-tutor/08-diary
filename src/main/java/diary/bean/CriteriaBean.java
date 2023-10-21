package diary.bean;

import java.io.Serializable;

/**
 * 検索条件のパラメータを管理するJavaBean
 */
public class CriteriaBean implements Serializable {

	/**
	 * フィールド
	 */
	private String keyword; // 検索キーワード
	private int    userId;  // ログインユーザのユーザ番号
	private int    limits;  // ページあたりの表示件数
	private int    page;    // 表示ページ数
	
	/**
	 * デフォルトコンストラクタ
	 */
	public CriteriaBean() {}

	/**
	 * コンストラクタ
	 * @param keyword 検索キーワード
	 * @param userId  ログインしているユーザのユーザ番号
	 */
	public CriteriaBean(String keyword, int userId) {
		this.keyword = keyword;
		this.userId  = userId;
	}
	
	/**
	 * コンストラクタ
	 * @param limits ページあたりの表示件数
	 * @param page   表示ページ数
	 * @param userId ログインしているユーザのユーザ番号
	 */
	public CriteriaBean(int userId, int limits, int page) {
		this.userId  = userId;
		this.limits  = limits;
		this.page    = page;
	}

	/**
	 * コンストラクタ
	 * @param keyword 検索キーワード
	 * @param userId ログインしているユーザのユーザ番号
	 * @param limits ページあたりの表示件数
	 * @param page   表示ページ数
	 */
	public CriteriaBean(String keyword, int userId, int limits, int page) {
		this.keyword = keyword;
		this.userId  = userId;
		this.limits  = limits;
		this.page    = page;
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

	public int getLimits() {
		return limits;
	}

	public void setLimits(int limits) {
		this.limits = limits;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("CriteriaBean [");
		builder.append("keyword=" + keyword + ", ");
		builder.append("userId=" + userId + ", ");
		builder.append("limits=" + limits + ", ");
		builder.append("page=" + page + "]");
		return builder.toString();
	}
	
}
