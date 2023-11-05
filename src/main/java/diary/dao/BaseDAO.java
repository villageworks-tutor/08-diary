package diary.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import diary.bean.ArticleBean;

/**
 * DAOクラスが継承する基底DAOクラス
 */
public class BaseDAO {
	
	/**
	 * クラス定数
	 */
	// データベース接続情報文字列定数群
	private static final String JDBC_DRIVER = "org.postgresql.Driver";
	private static final String DB_URL      = "jdbc:postgresql://localhost:5432/sample";
	private static final String DB_USER     = "student";
	private static final String DB_PASSWORD = "himitu";
	// ワイルドカード文字列定数
	protected static final String WILDCARD = "%";


	/**
	 * フィールド
	 */
	protected Connection conn;

	/**
	 * コンストラクタ
	 * @throws DAOException 
	 */
	public BaseDAO() throws DAOException {
		this.getConnection();
	}

	/**
	 * データベースに接続する
	 * @throws DAOException
	 */
	private void getConnection() throws DAOException {
		try {
			// JDBCドライバの読み込み
			Class.forName(JDBC_DRIVER);
			// データベースに接続：データベース接続オブジェクトを取得
			this.conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			throw new DAOException("JDBCドライバの読み込みに失敗しました。");
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DAOException("データベースへの接続に失敗しました。");
		}
	}

	/**
	 * 結果セットを記事リストに変換する
	 * @param  rs                結果セット
	 * @return List<ArticleBean> 記事リスト
	 * @throws SQLException 呼び出し元でDAO例外に変換する
	 */
	protected List<ArticleBean> convertToBeans(ResultSet rs) throws SQLException {
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
		return list;
	}
	
	/**
	 * 結果セットを記事インスタンスに変換する
	 * @param  rs           結果セット
	 * @return ArticleBean  記事インスタンス
	 * @throws SQLException 呼び出し元でDAO例外に変換する
	 */
	protected ArticleBean convertToBean(ResultSet rs) throws SQLException {
		ArticleBean bean = null;
		if  (rs.next()) {
			bean = new ArticleBean();
			bean.setId(rs.getInt("article_id"));
			bean.setTitle(rs.getString("title"));
			bean.setContent(rs.getString("content"));
			bean.setCreatedAt(rs.getTimestamp("created_at"));
			bean.setUserId(rs.getInt("user_id"));
		}
		return bean;
	}
	
	/**
	 * ワイルドカードを文字列の両端に追加する
	 * @param  target 対象文字列
	 * @return String ワイルドカードが両端に追加された文字列
	 */
	protected String appendWildcardTo(String target) {
		return WILDCARD + target + WILDCARD;
	}

}
