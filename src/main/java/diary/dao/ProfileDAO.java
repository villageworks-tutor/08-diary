package diary.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import diary.bean.ProfileBean;

public class ProfileDAO {

	/**
	 * クラス定数
	 */
	// データベース接続情報文字列定数群
	static final String JDBC_DRIVER = "org.postgresql.Driver";
	static final String DB_URL      = "jdbc:postgresql://localhost:5432/sample";
	static final String DB_USER     = "student";
	static final String DB_PASSWORD = "himitu";

	/**
	 * フィールド
	 */
	private Connection conn;

	/**
	 * コンストラクタ
	 * @throws DAOException 
	 */
	public ProfileDAO() throws DAOException {
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
	 * 電子メールアドレスとパスワードに合致した認証情報を取得する
	 * @param email    電子メールアドレス
	 * @param password パスワード
	 * @return 一致したレコードがある場合はパスワード以外のフィールドを設定した認証情報クラスのインスタンス、それ以外はnull
	 * @throws DAOException
	 */
	public ProfileBean findByEmailAndPassword(String email, String password) throws DAOException {
		// 実行するSQLを設定
		String sql = "SELECT * FROM profile WHERE email = ? AND password = ?";
		try (// SQL実行オブジェクトを取得
			 PreparedStatement pstmt = this.conn.prepareStatement(sql)) {
			// プレースホルダにパラメータをバインド
			pstmt.setString(1, email);
			pstmt.setString(2, password);
			try (// SQLの実行と結果セットの取得
				 ResultSet rs = pstmt.executeQuery()) {
				// 結果セットからログイン情報のインスタンスを取得
				ProfileBean bean = null;
				if (rs.next()) {
					bean = new ProfileBean();
					bean.setId(rs.getInt("user_id"));
					bean.setEmail(rs.getString("email"));
					bean.setName(rs.getString("name"));
				}
				// インスタンスを返却
				return bean;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DAOException("レコードの取得に失敗しました。");
		}
	}
	
}
