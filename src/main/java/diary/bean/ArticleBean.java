package diary.bean;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 投稿記事を管理するJavaBean：articleテーブルの1件分のレコードを管理
 */
public class ArticleBean implements Serializable {

	/**
	 * フィールド
	 */
	private int id;              // 投稿ID
	private String title;        // 投稿タイトル
	private String content;      // 投稿内容
	private Timestamp createdAt; // 投稿日時
	private int userId;          // 投稿者ID
	
	/**
	 * デフォルトコンストラクタ
	 */
	public ArticleBean() {}

	/**
	 * コンストラクタ
	 * @param title     投稿タイトル
	 * @param content   投稿内容 
	 * @param createdAt 投稿日時
	 * @param userId    投稿者ID
	 */
	public ArticleBean(String title, String content, Timestamp createdAt, int userId) {
		this.title = title;
		this.content = content;
		this.createdAt = createdAt;
		this.userId = userId;
	}

	/**
	 * コンストラクタ
	 * @param id        投稿ID
	 * @param title     投稿タイトル
	 * @param content   投稿内容 
	 * @param createdAt 投稿日時
	 * @param userId    投稿者ID
	 */
	public ArticleBean(int id, String title, String content, Timestamp createdAt, int userId) {
		this(title, content, createdAt, userId);
		this.id = id;
	}
	
	/**
	 * コンストラクタ
	 * @param id        投稿ID
	 * @param title     投稿タイトル
	 * @param content   投稿内容 
	 * @param userId    投稿者ID
	 */
	public ArticleBean(int id, String title, String content, int userId) {
		this(title, content, userId);
		this.id = id;
	}

	/**
	 * コンストラクタ
	 * @param title     投稿タイトル
	 * @param content   投稿内容 
	 * @param userId    投稿者ID
	 */
	public ArticleBean(String title, String content, int userId) {
		this.title = title;
		this.content = content;
		this.userId = userId;
	}
	
	/**
	 * コンストラクタ
	 * @param title     投稿タイトル
	 * @param content   投稿内容 
	 */
	public ArticleBean(String title, String content) {
		this.title = title;
		this.content = content;
	}


	/** アクセサメソッド群 */
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Timestamp getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
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
		builder.append("ArticeBean [");
		builder.append("id=" + id + ", ");
		builder.append("title=" + title + ", ");
		builder.append("content=" + content + ", ");
		builder.append("createdAt=" + createdAt	+ ", ");
		builder.append("userId=" + userId + "]");
		return builder.toString();
	}
	
}
