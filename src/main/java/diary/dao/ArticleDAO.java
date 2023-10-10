package diary.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import diary.bean.ArticleBean;

/**
 * 記事に関するデータにアクセスするDAO
 */
public class ArticleDAO {

	/**
	 * クラス定数
	 */
	// データベース接続情報文字列定数群
	private static final String JDBC_DRIVER = "org.postgresql.Driver";
	private static final String DB_URL      = "jdbc:postgresql://localhost:5432/sample";
	private static final String DB_USER     = "student";
	private static final String DB_PASSWORD = "himitu";
	
	/**
	 * フィールド
	 */
	private Connection conn; // データベース接続オブジェクト

	/**
	 * コンストラクタ
	 * @throws DAOException 
	 */
	public ArticleDAO() throws DAOException {
		this.getConnection();
	}

	/**
	 * データベースに接続する：データベース接続オブジェクトを取得する
	 * @throws DAOException
	 */
	private void getConnection() throws DAOException {
		try {
			// JDBCドライバのロード
			Class.forName(JDBC_DRIVER);
			// データベースに接続：データベース接続オブジェクトを取得
			this.conn =DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			throw new DAOException("JDBCの読み込みに失敗しました。");
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DAOException("データベースへの接続に失敗しました。");
		}
	}

	/**
	 * ユーザIDに合致したすべての投稿記事を取得する
	 * @param userId ユーザID
	 * @return ユーザIDに合致した投稿記事のリスト
	 * @throws DAOException 
	 */
	public List<ArticleBean> findAllByUserId(int userId) throws DAOException {
		// 実行するSQLの設定
		String sql = "SELECT * FROM article WHERE user_id = ? ORDER BY article_id";
		try (// SQL実行オブジェクトを取得
			 PreparedStatement pstmt = this.conn.prepareStatement(sql);) {
			// プレースホルダにパラメータをバインド
			pstmt.setInt(1, userId);
			try (// SQLの実行と結果セットの取得
				 ResultSet rs = pstmt.executeQuery();) {
				// 結果セットから投稿記事リストを生成
				List<ArticleBean> list = new ArrayList<ArticleBean>();
				ArticleBean bean = null;
				while (rs.next()) {
					bean = new ArticleBean();
					bean.setId(rs.getInt("article_id"));
					bean.setTitle(rs.getString("title"));
					bean.setContent(rs.getString("content"));
					bean.setCreatedAt(rs.getTimestamp("created_at"));
					bean.setUserId(rs.getInt("user_id"));
					list.add(bean);
				}
				// 投稿記事リストを返却
				return list;
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DAOException("レコードの取得に失敗しました。");
		}
		
	}
	
	/**
	 * 記事番号から記事を取得する
	 * @param id 取得する記事の記事番号
	 * @return 記事がある場合は記事インスタンス、それ以外はnull
	 * @throws DAOException
	 */
	public ArticleBean findByArticleId(int id) throws DAOException {
		// 実行するSQLを設定
		String sql = "SELECT * FROM article WHERE article_id = ?";
		try (// SQL実行オブジェクトを取得
			 PreparedStatement pstmt = this.conn.prepareStatement(sql);) {
			// プレースホルダにパラメータをバインド
			pstmt.setInt(1, id);
			try (// SQLの実行と結果セットの取得
				 ResultSet rs = pstmt.executeQuery();) {
				// 結果セットから記事インスタンスに変換
				ArticleBean bean = null;
				if (rs.next()) {
					bean = new ArticleBean();
					bean.setId(rs.getInt("article_id"));
					bean.setTitle(rs.getString("title"));
					bean.setContent(rs.getString("content"));
					bean.setCreatedAt(rs.getTimestamp("created_at"));
					bean.setUserId(rs.getInt("user_id"));
				}
				// 記事インスタンスを返却
				return bean;
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DAOException("レコードの取得に失敗しました。");
		}
	}

	/**
	 * 新規記事を投稿する
	 * @param article 新規記事
	 * @throws DAOException
	 */
	public void insert(ArticleBean article) throws DAOException {
		// 実行するSQLの設定
		String sql = "INSERT INTO article (title, content, created_at, user_id) VALUES (?, ?, CURRENT_TIMESTAMP, ?)";
		try (// SQL実行オブジェクトを取得
			 PreparedStatement pstmt = this.conn.prepareStatement(sql);) {
			// プレースホルダにパラメータをバインド
			pstmt.setString(1, article.getTitle());
			pstmt.setString(2, article.getContent());
			pstmt.setInt(3, article.getUserId());
			// SQLの実行
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DAOException("レコードの追加に失敗しました。");
		}
	}

	/**
	 * 指定された記事番号の記事を更新する
	 * @param article 更新対象の記事のインスタンス
	 * @throws DAOException
	 */
	public void update(ArticleBean article) throws DAOException {
		// 実行するSQLを設定
		String sql = "UPDATE article SET title = ?, content = ?, created_at = CURRENT_TIMESTAMP WHERE article_id = ?";
		try (// SQL実行オブジェクトを取得
			 PreparedStatement pstmt = this.conn.prepareStatement(sql);) {
			// プレースホルダにパラメータをバインド
			pstmt.setString(1, article.getTitle());
			pstmt.setString(2, article.getContent());
			pstmt.setInt(3, article.getId());
			// SQLの実行
			pstmt.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DAOException("レコードの更新に失敗しました。");
		}
	}

	/**
	 * 指定された記事番号の記事を削除する
	 * @param articleId 削除隊sh法の記事の記事番号
	 * @throws DAOException
	 */
	public void deleteByArticleId(int articleId) throws DAOException {
		// 実行するSQLを設定
		String sql = "DELETE FROM article WHERE article_id = ?";
		try (// SQL実行オブジェクトを取得
			 PreparedStatement pstmt = this.conn.prepareStatement(sql);) {
			// プレースホルダにパラメータをバインド
			pstmt.setInt(1, articleId);
			// SQLの実行
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DAOException("レコードの削除に失敗しました。");
		}
	}

}
