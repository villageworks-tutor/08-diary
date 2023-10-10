package diary.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import diary.bean.ProfileBean;

/**
 * ユーザ情報関連のレコードにアクセスするDAO
 */
public class ProfileDAO extends BaseDAO {

	/**
	 * コンストラクタ
	 * @throws DAOException
	 */
	public ProfileDAO() throws DAOException {
		super();
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
