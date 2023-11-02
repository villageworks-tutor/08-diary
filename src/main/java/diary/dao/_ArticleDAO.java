package diary.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import diary.bean.ArticleBean;
import diary.bean.CriteriaBean;

/**
 * 記事に関するデータにアクセスするDAO
 */
public class _ArticleDAO extends BaseDAO {

	/**
	 * コンストラクタ
	 * @throws DAOException
	 */
	public _ArticleDAO() throws DAOException {
		super();
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
	 * ユーザIDが合致した記事を指定した検索条件で取得する
	 * @param criteria 検索条件
	 * @return 記事リストｓ
	 * @throws DAOException
	 */
	public List<ArticleBean> findAllByUserIdWithPagination(CriteriaBean criteria) throws DAOException {
		// 実行するSQLの設定
		String sql = "SELECT * FROM article WHERE user_id = ? ORDER BY article_id LIMIT ? OFFSET ?";
		try (// SQL実行オブジェクトを取得
			 PreparedStatement pstmt = this.conn.prepareStatement(sql);) {
			// オフセット位置を設定
			int  offset = 1;
			// プレースホルダにパラメータをバインド
			pstmt.setInt(1, criteria.getUserId());
			pstmt.setInt(2, criteria.getLimits());
			pstmt.setInt(3, offset);
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
	 * ログインユーザが投稿した記事の総数を取得する
	 * @param userId ログインユーザのユーザID
	 * @return 投稿記事の総数
	 * @throws DAOException
	 */
	public int countAllByUserId(int userId) throws DAOException {
		// 実行するSQLの設定
		String sql = "SELECT count(*) FROM article WHERE user_id = ?";
		try (// SQL実行オブジェクトを取得
			 PreparedStatement pstmt = this.conn.prepareStatement(sql);) {
			// プレースホルダにパラメータをバインド
			pstmt.setInt(1, userId);
			try (// SQLの実行と結果セットの取得
				 ResultSet rs = pstmt.executeQuery();) {
				// 結果セットから件数を取得
				int count = 0;
				if (rs.next()) {
					count = rs.getInt(1);
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
	 * 指定されたキーワードがタイトルまたは記事内容に含まれている記事を取得する
	 * @param condition 検索条件
	 * @return 記事リスト
	 * @throws DAOException
	 */
	public List<ArticleBean> findLikeKeywordAndUserId(CriteriaBean condition) throws DAOException {
		// 実行するSQLの設定
		String sql = "SELECT * FROM article WHERE (title LIKE ? OR content LIKE ?) AND user_id = ?";
		try (//
			 PreparedStatement pstmt = this.conn.prepareStatement(sql);) {
			// プレースホルダにパラメータをバインド
			pstmt.setString(1, "%" + condition.getKeyword() + "%");
			pstmt.setString(2, "%" + condition.getKeyword() + "%");
			pstmt.setInt(3, condition.getUserId());
			try (// SQLの実行と結果セットの取得
				 ResultSet rs = pstmt.executeQuery();) {
				// 結果セットを記事リストに変換
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
				// 記事リストを返却
				return list;
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DAOException("レコードの取得に失敗しました。");
		}
	}

	/**
	 * 指定されたキーワードがタイトルまたは記事内容に含まれている記事を取得する
	 * @param keyword 検索キーワード
	 * @param userId  ログインユーザのユーザID
	 * @return 記事リスト
	 * @throws DAOException
	 */
	public List<ArticleBean> findLikeKeywordAndUserId(String keyword, int userId) throws DAOException {
		try {
			CriteriaBean conditions = new CriteriaBean(keyword, userId);
			return this.findLikeKeywordAndUserId(conditions);
		} catch (DAOException e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	/**
	 * 指定されたページの１ページ分のキーワードに合致した記事を取得する
	 * @param keyword 検索キーワード
	 * @param limits  ページ当たりの表示件数
	 * @param page    表示ページ数
	 * @param userId  ログインユーザのユーザID
	 * @return 記事リスト
	 * @throws DAOException
	 */
	public List<ArticleBean> findLikeKeywordAndUserIdWithPagination(String keyword, int limits, int page, int userId) throws DAOException {
		// 実行するSQLを設定
		String sql = "SELECT * FROM article WHERE "; 
		String pagination = "LIMIT ? OFFSET ?";
		if (!(keyword == null || keyword.isEmpty())) {
			// キーワードが入力されている場合
			sql += "(title LIKE ? OR content LIKE ?) AND ";
		}
		sql += "user_id = ? " + pagination;
		
		try (// SQL実行オブジェクトを取得
			 PreparedStatement pstmt = this.conn.prepareStatement(sql);) {
			// オフセット位置（表示開始レコードの位置）を計算
			int offset = limits * (page - 1);
			// プレースホルダにパラメータをバインド
			if (!(keyword == null || keyword.isEmpty())) {
				// キーワードが入力されている場合
				pstmt.setString(1, "%" + keyword + "%");
				pstmt.setString(2, "%" + keyword + "%");
				pstmt.setInt(3, userId);
				pstmt.setInt(4, limits);
				pstmt.setInt(5, offset);
			} else {
				// キーワードが入力されていない場合
				pstmt.setInt(1, userId);
				pstmt.setInt(2, limits);
				pstmt.setInt(3, offset);
			}
			try (// SQLの実行と結果セットの取得
				 ResultSet rs = pstmt.executeQuery();) {
				// 結果セットから記事リストに変換
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
				// 記事リストを返却
				return list;
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DAOException("レコードの取得に失敗しました。");
		}
		
	}
	
	/**
	 * 検索条件に合致した記事を取得する
	 * @param criteria 検索条件
	 * @return 記事リスト
	 * @throws DAOException
	 */
	public List<ArticleBean> findLikeKeywordAndUserIdWithPagination(CriteriaBean criteria) throws DAOException {
		return this.findLikeKeywordAndUserIdWithPagination(criteria.getKeyword(), criteria.getLimits(), criteria.getPage(), criteria.getUserId());
	}
	
	public int countLikeKeywoordAndUserId(CriteriaBean condition) throws DAOException {
		// 実行するSQLの設定
		String sql = "SELECT count(*) FROM article WHERE (title LIKE ? OR content LIKE ?) AND user_id = ?";
		try (// SQL実行オブジェクトの取得
			 PreparedStatement pstmt = this.conn.prepareStatement(sql);) {
			// プレースホルダにパラメータをバインド
			pstmt.setString(1, "%" + condition.getKeyword() + "%");
			pstmt.setString(2, "%" + condition.getKeyword() + "%");
			pstmt.setInt(3, condition.getUserId());
			try (// SQLの実行と結果セットの取得
				 ResultSet rs = pstmt.executeQuery();) {
				// 結果セットから件数を取得
				int count = 0;
				if (rs.next()) {
					count = rs.getInt(1);
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
	 * 指定されたページの記事を取得する
	 * @param limits  ページあたりの表示件数
	 * @param page    表示するページ番号
	 * @param userId  ログインユーザのユーザID
	 * @return 記事リスト
	 * @throws DAOException 
	 */
	public List<ArticleBean> findByPaging(int limits, int page, int userId) throws DAOException {
		// 実行するSQLの設定
		String sql = "SELECT * FROM article WHERE user_id = ? ORDER BY article_id LIMIT ? OFFSET ?";
		try (// SQL実行オブジェクトを取得
			 PreparedStatement pstmt = this.conn.prepareStatement(sql);) {
			// 表示するページの先頭レコードの位置を計算
			int offset = limits * (page -1);
			// プレースホルダにパラメータをバインド
			pstmt.setInt(1, userId);
			pstmt.setInt(2, limits);
			pstmt.setInt(3, offset);
			try (// SQLの実行と結果セットの取得
				 ResultSet rs = pstmt.executeQuery();) {
				// 結果セットから記事リストに変換
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
				// 記事リストを返却
				return list;
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
		String sql = "";
		String mode = "";
		if (article.getId() == 0) {
			sql = "INSERT INTO article (title, content, created_at, user_id) VALUES (?, ?, CURRENT_TIMESTAMP, ?)";
			mode = "追加";
		} else {
			sql = "UPDATE article SET title = ?, content = ?, created_at = CURRENT_TIMESTAMP WHERE article_id = ?";
			mode = "更新";
		}
		try (// SQL実行オブジェクトを取得
			 PreparedStatement pstmt = this.conn.prepareStatement(sql);) {
			// プレースホルダにパラメータをバインド
			pstmt.setString(1, article.getTitle());
			pstmt.setString(2, article.getContent());
			if (article.getId() == 0) {
				pstmt.setInt(3, article.getUserId());
			} else {
				pstmt.setInt(3, article.getId());
			}
			// SQLの実行
			pstmt.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DAOException("レコードの" + mode + "に失敗しました。");
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

	public List<ArticleBean> findAllByUserId(CriteriaBean criteria) throws DAOException {
		return this.findAllByUserIdWithPagination(criteria);
	}

}
