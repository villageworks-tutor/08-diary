package diary.dao;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;

import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.junit.jupiter.api.BeforeAll;

/**
 * DAOのすべてのテストクラスが継承する基底クラス
 * テスト対象クラスとテスト補助クラスはこのクラス内でそれぞれ1つあればよいのでstaticオブジェクトとして宣言する。
 * TODO: データベース接続情報文字列に関してはシステムリソースとしてまとめてリソースファイルから読み込むのが基本。
 */
class BaseDaoTest {

	/**
	 * クラス定数
	 */
	// データベース接続情報文字列定数群
	protected static final String JDBC_DRIVER = "org.postgresql.Driver";
	protected static final String DB_URL      = "jdbc:postgresql://localhost:5432/sample";
	protected static final String DB_USER     = "student";
	protected static final String DB_PASSWORD = "himitu";
	// フィクスチャ（データセットとして読み込むXMLを格納するディレクトリ）パス：読み込むXMLファイルは継承クラスで指定
	protected static final String DIR_FIXTURES = "test/main/java/diary/dao/_fixtures/";
	
	/** テスト対象クラス：systen under test */
	protected static ArticleDAO sut;
	
	/** テスト補助クラス */
	protected static Connection jdbcConnection;            // JDBCによるデータベース接続オブジェクト
	protected static IDatabaseConnection dbUnitConnection; // DbUnitによるデータベース接続オブジェクト
	protected static IDataSet dataset;                     // テスト用データセット
	
	@BeforeAll
	void setUpAll() throws Exception {
		// テスト対象クラスのインスタンス化
		sut = new ArticleDAO();
		// JDBCドライバの読込み
		Class.forName(JDBC_DRIVER);
		// JDBCによるデータベース接続オブジェクトの取得
		jdbcConnection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
		// DbUnnitによるデータベース接続オブジェクトの取得
		dbUnitConnection = new DatabaseConnection(jdbcConnection);
	}
	
	/**
	 * テスト用データを対象テーブルに設定する
	 * @param targetXmlPath テスト用XMLデータファイルパス
	 * @throws Exception
	 */
	void setRecord(String targetXmlPath) throws Exception {
		// 復元用データセットの設定
		dataset = new FlatXmlDataSetBuilder().build(new FileInputStream(targetXmlPath));
		// データセットの復元
		DatabaseOperation.CLEAN_INSERT.execute(dbUnitConnection, dataset);
	}
	
}
