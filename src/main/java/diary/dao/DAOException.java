package diary.dao;

/**
 * すべてのDAOで発生する例外を統一して管理するための独自例外
 */
public class DAOException extends Exception {

	public DAOException(String message) {
		super(message);
	}

}
