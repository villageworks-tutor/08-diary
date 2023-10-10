package diary.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import diary.bean.ArticleBean;
import diary.bean.ProfileBean;
import diary.dao.ArticleDAO;
import diary.dao.DAOException;
import diary.dao.ProfileDAO;

/**
 * Servlet implementation class AuthServlet
 */
@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// リクエストパラメータの文字コードを設定
		request.setCharacterEncoding("utf-8");
		// 処理分岐のactionキーを取得
		String action = request.getParameter("action");
		// 取得したactionキーによる処理の分岐
		if (action.equals("top")) {
			// ログインページに遷移
			this.gotoPage(request, response, "login.jsp");
		} else if (action.equals("login")) {
			// ユーザ認証の実行
			// リクエストパラメータを取得
			String email = request.getParameter("email");
			String password = request.getParameter("password");
			// 入力値からユーザ情報を取得する
			try {
				ProfileDAO profileDao = new ProfileDAO();
				ProfileBean profile = profileDao.findByEmailAndPassword(email, password);
				
				if (profile == null) {
					// ユーザ情報が取得できなかった場合：ログイン失敗
					this.gotoErrPage(request, response, "メールアドレスまたはパスワードが間違っています。");
					return;
				}
				
				// ログインユーザのすべての投稿記事を取得
				ArticleDAO articleDao = new ArticleDAO();
				List<ArticleBean> articleList = articleDao.findAllByUserId(profile.getId());
				
				// セッションにユーザ情報と投稿記事リストを登録
				HttpSession session = request.getSession();
				session.setAttribute("profile", profile);
				// リクエストスコープに投稿記事リストを登録
				request.setAttribute("articleList", articleList);
				
				// 画面遷移
				this.gotoPage(request, response, "success.jsp");
				
			} catch (DAOException e) {
				e.printStackTrace();
				this.gotoErrPage(request, response);
			}
		} else if (action.equals("create")) {
			// 新規投稿の場合
			// リクエストパラメータを取得
			String title = request.getParameter("title");
			String content = request.getParameter("content");
			int userId = Integer.parseInt(request.getParameter("userId"));
			// ArticleBeanをインスタンス化
			ArticleBean bean = new ArticleBean();
			bean.setTitle(title);
			bean.setContent(content);
			bean.setUserId(userId);
			try {
				// 記事を登録
				ArticleDAO dao = new ArticleDAO();
				dao.insert(bean);
				// すべての記事を取得
				List<ArticleBean> list = dao.findAllByUserId(userId);
				// リクエストに記事リストを登録
				request.setAttribute("articleList", list);
				// 画面遷移
				this.gotoPage(request, response, "success.jsp");
			} catch (DAOException e) {
				e.printStackTrace();
				this.gotoErrPage(request, response);
			}
		} else if (action.equals("edit")) {
			// 編集の場合
			// リクエストパラメータを取得
			int id = Integer.parseInt(request.getParameter("id"));
			// 記事番号に該当する記事を取得
			try {
				// 編集対象の記事を取得
				ArticleDAO dao = new ArticleDAO();
				ArticleBean bean = dao.findByArticleId(id);
				// リクエストに取得した記事を登録
				request.setAttribute("article", bean);
				// 画面遷移
				this.gotoPage(request, response, "editArticle.jsp");
				
			} catch (DAOException e) {
				e.printStackTrace();
				this.gotoErrPage(request, response);
			}
		} else if (action.equals("update")) {
			// 更新処理の場合
			// リクエストパラメータを取得
			int id = Integer.parseInt(request.getParameter("id"));
			String title = request.getParameter("title");
			String content = request.getParameter("content");
			int userId = Integer.parseInt(request.getParameter("userId"));
			// 記事のインスタンス化
			ArticleBean bean = new ArticleBean(id, title, content, userId);
			// 記事の更新
			try {
				// 記事の更新
				ArticleDAO dao = new ArticleDAO();
				dao.update(bean);
				
				// すべての記事を取得
				List<ArticleBean> list = dao.findAllByUserId(userId);
				// リクエストに記事リストを登録
				request.setAttribute("articleList", list);
				// 画面遷移
				this.gotoPage(request, response, "success.jsp");
			} catch (DAOException e) {
				e.printStackTrace();
				this.gotoErrPage(request, response);
			}
		} else if (action.equals("showOne")) {
			// 記事単一表示の場合
			// リクエストパラメータを取得
			int id = Integer.parseInt(request.getParameter("id"));
			// 記事番号に該当する記事を取得
			try {
				// 編集対象の記事を取得
				ArticleDAO dao = new ArticleDAO();
				ArticleBean bean = dao.findByArticleId(id);
				// リクエストに取得した記事を登録
				request.setAttribute("article", bean);
				// 画面遷移
				this.gotoPage(request, response, "showArticle.jsp");
				
			} catch (DAOException e) {
				e.printStackTrace();
				this.gotoErrPage(request, response);
			}
		} else if (action.equals("delete")) {
			// 削除の場合
			// リクエストパラメータを取得
			int id = Integer.parseInt(request.getParameter("id"));
			int userId = Integer.parseInt(request.getParameter("userId"));
			// 記事番号に該当する記事を取得
			try {
				// 指定された記事番号の記事を削除
				ArticleDAO dao = new ArticleDAO();
				dao.deleteByArticleId(id);
				// すべての記事を取得
				List<ArticleBean> list = dao.findAllByUserId(userId);
				// リクエストスコープに記事リストを登録
				request.setAttribute("articleList", list);
				// 画面遷移
				this.gotoPage(request, response, "success.jsp");
				
			} catch (DAOException e) {
				e.printStackTrace();
				this.gotoErrPage(request, response);
			}
			
		} else if (action.equals("logout")) {
			// セッションを取得
			HttpSession session = request.getSession(false);
			if (session != null) {
				// セッションがあれば破棄
				session.invalidate();
			}
			// ログイン画面に遷移
			this.gotoPage(request, response, "login.jsp");
		} else {
			// 定義外の値がactinキーに設定された場合
			this.gotoErrPage(request, response);
		}
	}
	
	/**
	 * 固定メーッセージをエラーページに表示する
	 * @param request  HttpServletRequest
	 * @param response HttpServletResponse
	 * @throws ServletException
	 * @throws IOException
	 */
	private void gotoErrPage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.gotoErrPage(request, response, "内部エラーが発生しました。");
	}

	/**
	 * メッセージをエラーページに表示する
	 * @param request  HttpServletRequest
	 * @param response HttpServletResponse
	 * @param message  表示するメッセージ
	 * @throws ServletException
	 * @throws IOException
	 */
	private void gotoErrPage(HttpServletRequest request, HttpServletResponse response, String message) throws ServletException, IOException {
		request.setAttribute("message", message);
		this.gotoPage(request, response, "error.jsp");
	}

	/**
	 * 指定したパスに遷移する
	 * @param request  HttpServletRequest
	 * @param response HttpServletResponse
	 * @param nextPath 遷移先パス
	 * @throws ServletException
	 * @throws IOException
	 */
	private void gotoPage(HttpServletRequest request, HttpServletResponse response, String nextPath) throws ServletException, IOException {
		RequestDispatcher dispatcher = request.getRequestDispatcher(nextPath);
		dispatcher.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}