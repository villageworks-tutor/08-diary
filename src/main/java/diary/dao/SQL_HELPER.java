package diary.dao;

public class SQL_HELPER {

	/**
	 * クラス定数
	 */
	// 空文字列定数
	private static final String CHAR_EMPTY = " ";
	
	private static final String SQL_FIND_FROM_ARTICLE   = "SELECT * FROM article";
	private static final String SQL_COUNT_FROM_ARTICLE  = "SELECT count(*) FROM article";
	private static final String SQL_DELETE_FROM_ARTICLE = "DELETE FROM article";
	
	private static final String WHERE_ARTICLE_ID        = "WHERE article_id = ?";
	private static final String WHERE_USER_ID           = "WHERE user_id = ?";
	private static final String WHERE_LIKE_KEYWORD      = "AND (title LIKE ? OR content LIKE ?)";
	private static final String ORDER_BY_ARICLE         = "ORDER BY article_id";
	private static final String LIMIT_OFFSET            = "LIMIT ? OFFSET ?";
	
	private static final String SQL_INSERT_ARTICLE_WITH_PLACEHOLDER =                  "INSERT INTO article (title, content, created_at, user_id) VALUES (?, ?, current_timestamp, ?)";
	private static final String SQL_UPDATE_ARTICLE_WITH_PLACEHOLDER =                  "UPDATE article SET title = ?, content = ?, created_at = current_timestamp WHERE article_id = ?";

	/**
	 * フィールド
	 */
	private static StringBuilder builder;
	
	/**
	 * 記事番号を元に記事を取得するSQLを返す
	 * @return 記事番号を元に記事を取得するSQL
	 */
	static String FIND_BY_ARTICLE_ID() {
		builder = new StringBuilder();
		builder.append(SQL_FIND_FROM_ARTICLE).append(CHAR_EMPTY);
		builder.append(WHERE_ARTICLE_ID);
		return builder.toString();
	}
	
	/**
	 * ユーザIDを元に記事リストを取得するSQLを返す
	 * @return ユーザIDを元に記事リストを取得するSQL
	 */
	static String FIND_BY_USER_ID() {
		builder = new StringBuilder();
		builder.append(SQL_FIND_FROM_ARTICLE).append(CHAR_EMPTY);
		builder.append(WHERE_USER_ID).append(CHAR_EMPTY);
		builder.append(ORDER_BY_ARICLE);
		return builder.toString();
	}
	
	/**
	 * ユーザIDをのユーザが投稿した記事のうちキーワードを含む記事を取得するSQLを返す
	 * @return ユーザIDをのユーザが投稿した記事のうちキーワードを含む記事を取得するSQL
	 */
	static String FIND_BY_USER_ID_AND_LIKE_KEYWORD() {
		builder = new StringBuilder();
		builder.append(SQL_FIND_FROM_ARTICLE).append(CHAR_EMPTY);
		builder.append(WHERE_USER_ID).append(CHAR_EMPTY);
		builder.append(WHERE_LIKE_KEYWORD).append(CHAR_EMPTY);
		builder.append(ORDER_BY_ARICLE).append(CHAR_EMPTY);
		return builder.toString();
	}
	
	/**
	 * ユーザIDをのユーザが投稿した記事のうちキーワードを含む記事を１ページ表示分取得するSQLを返す
	 * @return ユーザIDをのユーザが投稿した記事のうちキーワードを含む記事を１ページ表示分取得するSQL
	 */
	static String FIND_BY_USER_ID_AND_LIKE_KEYWORD_WITH_PAGINATION() {
		builder = new StringBuilder();
		builder.append(FIND_BY_USER_ID_AND_LIKE_KEYWORD()).append(CHAR_EMPTY);
		builder.append(LIMIT_OFFSET);
		return builder.toString();
	}
	
	/**
	 * ユーザIDをのユーザが投稿した記事のうちキーワードを含む記事の総件数を取得するSQLを返す
	 * @return ユーザIDをのユーザが投稿した記事のうちキーワードを含む記事の総件数を取得するSQL
	 */
	static String COUNT_BY_USER_ID_AND_LIKE_KEYWORD() {
		builder = new StringBuilder();
		builder.append(SQL_COUNT_FROM_ARTICLE).append(CHAR_EMPTY);
		builder.append(WHERE_USER_ID).append(CHAR_EMPTY);
		builder.append(WHERE_LIKE_KEYWORD);
		return builder.toString();
	}

	/**
	 * 新規投稿記事を保存するSQLを返す
	 * @return 新規投稿記事を保存するSQL
	 */
	static String INSERT_ARTICLE() {
		return SQL_INSERT_ARTICLE_WITH_PLACEHOLDER;
	}

	/**
	 * 記事を更新するSQLを返す
	 * @return 記事を更新するSQL
	 */
	static String UPDATE_ARTICLE() {
		return SQL_UPDATE_ARTICLE_WITH_PLACEHOLDER;
	}
	
	/**
	 * 記事番号を指定して記事を削除するSQLを返す
	 * @return 記事番号を指定して記事を削除するSQL
	 */
	static String DELETE_BY_ARTICLE_ID() {
		builder = new StringBuilder();
		builder.append(SQL_DELETE_FROM_ARTICLE).append(CHAR_EMPTY);
		builder.append(WHERE_ARTICLE_ID);
		return builder.toString();
	}

	/**
	 * ユーザIDのユーザが投稿した記事をすべて削除するSQLを返す
	 * @return ユーザIDのユーザが投稿した記事をすべて削除するSQL
	 */
	static String DELETE_BY_USER_ID() {
		builder = new StringBuilder();
		builder.append(SQL_DELETE_FROM_ARTICLE).append(CHAR_EMPTY);
		builder.append(WHERE_USER_ID);
		return builder.toString();
	}
	
	/**
	 * ユーザIDごとにページあたりに表示する件数分だけの記事を取得するSQLを返す
	 * @return 	 * ユーザIDごとにページあたりに表示する件数分だけの記事を取得するSQL
	 */
	static String FIND_BY_USER_ID_WITH_PAGINATION() {
		builder = new StringBuilder();
		builder.append(SQL_FIND_FROM_ARTICLE).append(CHAR_EMPTY);
		builder.append(WHERE_USER_ID).append(CHAR_EMPTY);
		builder.append(LIMIT_OFFSET);
		return builder.toString();
	}

	/**
	 * ユーザIDごとに記事の総件数を取得するSQLを返す
	 * @return ユーザIDごとに記事の総件数を取得するSQL
	 */
	static String COUNT_FROM_ARTICLE_BY_USER_ID() {
		builder = new StringBuilder();
		builder.append(SQL_COUNT_FROM_ARTICLE).append(CHAR_EMPTY);
		builder.append(WHERE_USER_ID);
		return builder.toString();
	}
	
}
