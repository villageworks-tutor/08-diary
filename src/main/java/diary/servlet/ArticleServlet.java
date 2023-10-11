package diary.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import diary.bean.ArticleBean;
import diary.common.ConvertUtils;
import diary.dao.ArticleDAO;
import diary.dao.DAOException;

/**
 * Servlet implementation class ArticleServlet
 * 投稿記事関連の処理を実行するServlet
 */
@WebServlet("/ArticleServlet")
public class ArticleServlet extends BaseServlet {
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
		if (action.equals("post") || action.equals("update")) { // 可読性向上のために、actionキーの値を「create」→「post」に変更
			// 新規投稿処理
			String title = request.getParameter("title");
			String content = request.getParameter("content");
			// 整数に変換して取得
			int articleId = ConvertUtils.toInt(request.getParameter("id"));
			int userId = ConvertUtils.toInt(request.getParameter("userId"));
																																																																											
			// 記事のインスタンス化
			ArticleBean bean = new ArticleBean(articleId, title, content, userId);

			try {
				// 記事に関する操作を担当するDAOをインスタンス化
				ArticleDAO dao = new ArticleDAO();
				// レコード操作を実行
				dao.update(bean);
				
				// 記事リストを取得
				List<ArticleBean> list = dao.findAllByUserId(userId);
				// リクエストに記事リストを登録
				request.setAttribute("articleList", list);
				// 画面遷移
				this.gotoPage(request, response, "success.jsp");
				
			} catch (DAOException e) {
				e.printStackTrace();
				this.gotoErrPage(request, response);
			}
			
		} else if (action.equals("detail") || action.equals("edit") ) {  // 可読性向上のために、actionキーの値を「showOne」→「detail」に変更
			// 記事詳細表示または更新画面表示
			int articleId = Integer.parseInt(request.getParameter("id"));
			
			try {
				// 指定された記事番号の記事を取得
				ArticleDAO dao = new ArticleDAO();
				ArticleBean bean = dao.findByArticleId(articleId);
				// リクエストに取得した記事を登録
				request.setAttribute("article", bean);
				// actionキーによって遷移先を変えて画面遷移
				if (action.equals("detail")) {
					// 単一記事表示の場合
					this.gotoPage(request, response, "showArticle.jsp");
				} else {
					// 更新画面表示の場合
					this.gotoPage(request, response, "editArticle.jsp");
				}
				
			} catch (DAOException e) {
				e.printStackTrace();
				this.gotoErrPage(request, response);
			}
			
		} else if (action.equals("delete")) {
			// 削除処理
			int articleId = Integer.parseInt(request.getParameter("id"));
			int userId = Integer.parseInt(request.getParameter("userId"));
			
			try {
				// 削除の実行
				ArticleDAO dao = new ArticleDAO();
				dao.deleteByArticleId(articleId);
				
				// 記事リストを取得
				List<ArticleBean> list = dao.findAllByUserId(userId);
				// リクエストに記事リストを登録
				request.setAttribute("articleList", list);
				// 画面遷移
				this.gotoPage(request, response, "success.jsp");
				
			} catch (DAOException e) {
				e.printStackTrace();
				this.gotoErrPage(request, response);
			}
			
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
