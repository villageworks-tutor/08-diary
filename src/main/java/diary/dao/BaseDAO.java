package diary.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

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

}
