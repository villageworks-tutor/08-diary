package diary.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import diary.bean.ArticleBean;
import diary.bean.CriteriaBean;

/**
 * 記事に関するデータにアクセスするDAO
 */
public class ArticleDAO extends BaseDAO {
	
	/**
	 * コンストラクタ
	 * @throws DAOException
	 */
	public ArticleDAO() throws DAOException {
		super();
	}
	
	/**
	 * 指定された記事番号に合致する記事を取得する
	 * @param articleId     記事番号
	 * @return ArticleBean  記事番号に合致した記事インスタンス
	 * @throws DAOException DAO例外
	 */
	public ArticleBean findByArticleId(int articleId) throws DAOException {
		try (// SQL実行オブジェクトを取得
			 PreparedStatement pstmt = this.conn.prepareStatement(SQL_HELPER.FIND_BY_ARTICLE_ID());) {
			// プレースホルダにパラメータをバインド
			pstmt.setInt(1, articleId);
			try (// SQLの実行と結果セットの取得
				 ResultSet rs = pstmt.executeQuery();) {
				// 結果セットから記事インスタンスに変換
				ArticleBean bean = this.convertToBean(rs);
				// 記事インスタンスを返却
				return bean;
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DAOException("レコードの取得に失敗しました。");
		}
		
	}

	/**
	 * ユーザIDのユーザが投稿したすべての記事を取得する
	 * @param  userId            ユーザID
	 * @return List<ArticleBean> 記事リスト
	 * @throws DAOException      DAO例外
	 */
	public List<ArticleBean> findAllByUserId(int userId) throws DAOException {
		try (// SQL実行オブジェクトを取得
			 PreparedStatement pstmt = this.conn.prepareStatement(SQL_HELPER.FIND_BY_USER_ID());) {
			// プレースホルダにパラメータをバインド
			pstmt.setInt(1, userId);
			try (// SQLの実行と結果セットの取得
				 ResultSet rs = pstmt.executeQuery();) {
				// 結果セットから投稿記事リストを生成
				List<ArticleBean> list = this.convertToBeans(rs);
				// 投稿記事リストを返却
				return list;
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DAOException("レコードの取得に失敗しました。");
		}
		
	}

	/**
	 * ユーザIDのユーザが投稿したすべての記事を取得する
	 * @param  criteria          検索条件インスタンス：ここではユーザIDフィールド以外は未設定
	 * @param  userId            ユーザID
	 * @return List<ArticleBean> 記事リスト
	 * @throws DAOException      DAO例外
	 */
	public List<ArticleBean> findAllByUserId(CriteriaBean criteria) throws DAOException {
		return this.findAllByUserId(criteria.getUserId());
	}

	/**
	 * ユーザIDのユーザが投稿した記事のうち指定されたキーワードがタイトルまたは投稿内容に含まれている記事を取得する
	 * @param  userId            ユーザID
	 * @param  keyword           検索キーワード
	 * @return List<ArticleBean> 記事リスト
	 * @throws DAOException
	 */
	public List<ArticleBean> findByUserIdAndLikeKeyword(int userId, String keyword) throws DAOException {
		try (// SQL実行オブジェクトを取得
			 PreparedStatement pstmt = this.conn.prepareStatement(SQL_HELPER.FIND_BY_USER_ID_AND_LIKE_KEYWORD());) {
			// プレースホルダにパラメータをバインド
			pstmt.setInt(1, userId);
			pstmt.setString(2, this.appendWildcardTo(keyword));
			pstmt.setString(3, this.appendWildcardTo(keyword));
			try (// SQLの実行と結果セットの取得
				 ResultSet rs = pstmt.executeQuery();) {
				// 結果セットから記事リストに変換
				List<ArticleBean> list = this.convertToBeans(rs);
				// 記事リストを返却
				return list;
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DAOException("レコードの取得に失敗しました。");
		}
		
	}

	/**
	 * ユーザIDのユーザが投稿した記事のうち指定されたキーワードがタイトルまたは投稿内容に含まれている記事を取得する
	 * @param  criteria          検索条件インスタンス：ここではユーザIDフィールドとキーワードフィールド以外は未設定
	 * @return List<ArticleBean> 記事リスト
	 * @throws DAOException      DAO例外
	 */
	public List<ArticleBean> findByUserIdAndLikeKeyword(CriteriaBean criteria) throws DAOException {
		return this.findByUserIdAndLikeKeyword(criteria.getUserId(), criteria.getKeyword());
	}

	/**
	 * ユーザIDのユーザが投稿した記事のうちしてされたキーワードを含む記事をページごとに表示する記事を取得する
	 * @param  criteria          検索条件インスタンス：ここではユーザIDフィールドとキーワードフィールド以外は未設定
	 * @return List<ArticleBean> 記事リスト
	 * @throws DAOException      DAO例外
	 */
	public List<ArticleBean> findByUserIdAndLikeKeywordWithPaging(CriteriaBean criteria) throws DAOException {
		try (// SQL実行オブジェクトを取得
			 PreparedStatement pstmt = this.conn.prepareStatement(SQL_HELPER.FIND_BY_USER_ID_AND_LIKE_KEYWORD_WITH_PAGINATION());) {
			// 表示ページの先頭記事の記事番号を計算
			int offset = criteria.getLimits()  * (criteria.getPage() - 1);
			// プレースホルダにパラメータをバインド
			pstmt.setInt(1, criteria.getUserId());
			pstmt.setString(2, this.appendWildcardTo(criteria.getKeyword()));
			pstmt.setString(3, this.appendWildcardTo(criteria.getKeyword()));
			pstmt.setInt(4, criteria.getLimits());
			pstmt.setInt(5, offset);
			try (// SQLの実行と結果セットの取得
				 ResultSet rs = pstmt.executeQuery();) {
				// 結果セットから記事リストに変換
				List<ArticleBean> list = this.convertToBeans(rs);
				// 記事リストを返却
				return list;
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DAOException("レコードの取得に失敗しました。");
		}
		
	}

	/**
	 * ユーザIDのユーザが投稿した記事のうちしてされたキーワードを含む記事の総件数を取得する
	 * @param  criteria     検索条件インスタンス：ここではユーザIDフィールドとキーワードフィールド以外は未設定
	 * @return int          記事リスト
	 * @throws DAOException DAO例外
	 */
	public int countByUserIdAndLikeKeyword(CriteriaBean criteria) throws DAOException {
		try (// SQL実行オブジェクトを取得
			 PreparedStatement pstmt = this.conn.prepareStatement(SQL_HELPER.COUNT_BY_USER_ID_AND_LIKE_KEYWORD());) {
			// プレースホルダにパラメータをバインド
			pstmt.setInt(1, criteria.getUserId());
			pstmt.setString(2, this.appendWildcardTo(criteria.getKeyword()));
			pstmt.setString(3, this.appendWildcardTo(criteria.getKeyword()));
			try (// SQLの実行と結果セットの取得
				 ResultSet rs = pstmt.executeQuery();) {
				// 結果セットから件数を取得
				int count = 0;
				if (rs.next()) {
					count = rs.getInt(1); // フィールドが決まっているんのでインデックスで取得：AS句宣言した別名で取得しても構わない
				}
				// 件数を返却
				return count;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DAOException("レコードの取得に失敗しました。");
		}
		
	}

	/**
	 * 投稿した記事を保存する
	 * @param  article      処理対象記事
	 * @throws DAOException DAO例外
	 */
	public void insert(ArticleBean article) throws DAOException {
		try (// SQL実行オブジェクトを取得
			 PreparedStatement pstmt = this.conn.prepareStatement(SQL_HELPER.INSERT_ARTICLE());) {
			// プレースホルダにパラメータをバインド
			pstmt.setString(1, article.getTitle());
			pstmt.setString(2, article.getContent());
			pstmt.setInt(3, article.getUserId());
			// SQLの実行
			pstmt.executeUpdate();
		} catch (SQLException | NullPointerException e) {
			e.printStackTrace();
			throw new DAOException("レコードの登録に失敗しました。");
		}
	}

	/**
	 * 指定された記事番号の記事を更新する
	 * @param  article      処理対象記事
	 * @throws DAOException DAO例外
	 */
	public void update(ArticleBean article) throws DAOException {
		try (// 実行するSQLを設定
			 PreparedStatement pstmt = this.conn.prepareStatement(SQL_HELPER.UPDATE_ARTICLE());) {
			// プレースホルダにパラメータをバインド
			pstmt.setString(1, article.getTitle());
			pstmt.setString(2, article.getContent());
			pstmt.setInt(3, article.getId());
			// SQLの実行
			pstmt.executeLargeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DAOException("レコードの更新に失敗しました。");
		}
	}

	/**
	 * 指定された記事番号の記事を削除する
	 * @param  articleId    処理対象記事の記事番号
	 * @throws DAOException DAO例外
	 */
	public void deleteByArticleId(int articleId) throws DAOException {
		try (// SQL実行オブジェクトを取得
			 PreparedStatement pstmt = this.conn.prepareStatement(SQL_HELPER.DELETE_BY_ARTICLE_ID());) {
			// プレースホルダにパラメータをバインド
			pstmt.setInt(1, articleId);
			// SQLを実行
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DAOException("レコードの削除に失敗しました。");
		}
	}

	/**
	 * 指定されたユーザIDのユーザが投稿したすべての記事を削除する
	 * @param  articleId    処理対象記事の投稿者ID
	 * @throws DAOException DAO例外
	 */
	public void deleteAllByUserId(int userId) throws DAOException {
		try (// SQL実行オブジェクトを取得
			 PreparedStatement pstmt = this.conn.prepareStatement(SQL_HELPER.DELETE_BY_USER_ID());) {
			// プレースホルダにパラメータをバインド
			pstmt.setInt(1, userId);
			// SQLの実行
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DAOException("レコードの削除に失敗しました。");
		}
	}
	
}
