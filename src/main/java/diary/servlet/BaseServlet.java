package diary.servlet;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class BaseServlet
 */
@WebServlet("/BaseServlet")
public class BaseServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	/**
	 * クラス定数
	 */
	protected static final int LIMIT_PER_PAGE = 5; // ページあたりに表示する件数

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// リクエストパラメータの文字コードを設定
		request.setCharacterEncoding("utf-8");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
	
	/**
	 * 固定メーッセージをエラーページに表示する
	 * @param request  HttpServletRequest
	 * @param response HttpServletResponse
	 * @throws ServletException
	 * @throws IOException
	 */
	protected void gotoErrPage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
	protected void gotoErrPage(HttpServletRequest request, HttpServletResponse response, String message) throws ServletException, IOException {
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
	protected void gotoPage(HttpServletRequest request, HttpServletResponse response, String nextPath) throws ServletException, IOException {
		RequestDispatcher dispatcher = request.getRequestDispatcher(nextPath);
		dispatcher.forward(request, response);
	}

}
