package diary.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import diary.bean.ArticleBean;
import diary.bean.CriteriaBean;
import diary.bean.ProfileBean;
import diary.dao.ArticleDAO;
import diary.dao.DAOException;
import diary.dao.ProfileDAO;

/**
 * Servlet implementation class AuthServlet
 * ユーザ認証関連の処理を実行するServlet
 */
@WebServlet("/AuthServlet")
public class AuthServlet extends BaseServlet {
	private static final long serialVersionUID = 1L;
	
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 基底クラスのdoGetメソッドの呼び出し：リクエストパラメータの文字コードの設定
		super.doGet(request, response);
		// リクエストパラメータから処理分岐用actionキーを取得
		String action = request.getParameter("action");
		// actionキーによって処理を分岐
		if (action.equals("top")) {
			// 初期表示処理：ログイン画面表示
			this.gotoPage(request, response, "login.jsp");
		} else if (action.equals("login")) {
			// ログイン処理
			String email = request.getParameter("email");
			String password = request.getParameter("password");
			
			try {
				// リクエストパラメータから取得したメールアドレスとパスワードに一致するユーザ情報を取得
				ProfileDAO profileDao = new ProfileDAO();
				ProfileBean profile = profileDao.findByEmailAndPassword(email, password);
				
				if (profile == null) {
					// ユーザ情報が取得できなかった場合：ログイン失敗
					this.gotoErrPage(request, response, "メールアドレスまたはパスワードが間違っています。");
					return;
				}
				
				// ログインユーザが投稿したすべての記事を取得
				ArticleDAO articleDao = new ArticleDAO();
				// List<ArticleBean> list = articleDao.findAllByUserId(profile.getId());
				int firstPage = 1;
				// List<ArticleBean> list = articleDao.findLikeKeywordWithPagination(criteria);
				CriteriaBean criteria = new CriteriaBean(profile.getId(), LIMIT_PER_PAGE, firstPage);
				List<ArticleBean> list = articleDao.findByUserIdWithPagination(criteria);
				
				// ログインユーザの投稿した記事の総数を取得
				int count = articleDao.countByUserIdAndLikeKeyword(criteria);
				// 分割されたページ数を計算
				int totalPage = count / LIMIT_PER_PAGE + 1;
				// セッションにユーザ情報を登録
				HttpSession session = request.getSession();
				
				session.setAttribute("profile", profile);
				// リクエストに記事リストを登録
				request.setAttribute("articleList", list);
				request.setAttribute("totalPage", totalPage);
				
				// 画面遷移
				this.gotoPage(request, response, "success.jsp");
				
			} catch (DAOException e) {
				e.printStackTrace();
				this.gotoErrPage(request, response);
			}
			
		} else if (action.equals("logout")) {
			// ログアウト処理
			HttpSession session = request.getSession(false);
			if (session != null) {
				session.invalidate();
			}
			// 画面遷移
			this.gotoPage(request, response, "login.jsp");
		} else {
			// actionキーの値が未定義の場合
			this.gotoErrPage(request, response);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
