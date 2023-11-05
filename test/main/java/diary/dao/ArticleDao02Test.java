package diary.dao;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;
import static org.junit.jupiter.api.Assertions.*;

import java.io.FileInputStream;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import diary.bean.ArticleBean;
import diary.dao.matcher.EqualToArticle;

@TestInstance(Lifecycle.PER_CLASS)
class ArticleDao02Test extends BaseDaoTest {

	// データセットとして読み込むXMLファイルのパス
	protected static final String FIXTURES_XML_00 = DIR_FIXTURES + "記事_復元.xml";
	protected static final String FIXTURES_XML_01 = DIR_FIXTURES + "記事_初期化５.xml";
	protected static final String FIXTURES_XML_11 = DIR_FIXTURES + "記事_登録_初期値５.xml"; // 追加後の初期値（１件追加）
	protected static final String FIXTURES_XML_12 = DIR_FIXTURES + "記事_登録_期待値６.xml"; // 追加後の期待値（１件追加）
	protected static final String FIXTURES_XML_21 = DIR_FIXTURES + "記事_更新_初期値６.xml"; // 更新前のテーブルの状態の初期値
	protected static final String FIXTURES_XML_22 = DIR_FIXTURES + "記事_更新_期待値６.xml"; // 更新後のテーブルの状態の期待値
	protected static final String FIXTURES_XML_31 = DIR_FIXTURES + "記事_削除_初期値６.xml"; // 削除前のテーブルの状態の初期値
	protected static final String FIXTURES_XML_32 = DIR_FIXTURES + "記事_削除_期待値６.xml"; // 削除後のテーブルの状態の期待値
	
	@Nested
	@DisplayName("更新系メソッドのテスtクラス")
	@TestInstance(Lifecycle.PER_CLASS)
	class UpdateMethodsTest {
		
		@BeforeAll
		void setUpBeforeClass() throws Exception {
			// テスト用データセットの設定
			dataset = new FlatXmlDataSetBuilder().build(new FileInputStream(FIXTURES_XML_01));
			// データセットの投入
			DatabaseOperation.CLEAN_INSERT.execute(dbUnitConnection, dataset);
		}

		@AfterAll
		void tearDownAfterClass() throws Exception {
			// 復元用データセットの設定
			dataset = new FlatXmlDataSetBuilder().build(new FileInputStream(FIXTURES_XML_00));
			// データセットの復元
			DatabaseOperation.CLEAN_INSERT.execute(dbUnitConnection, dataset);
		}
		
		@Nested
		@DisplayName("ArticleDAO#deleteAllByUserIdメソッドのテストクラス")
		class DeleteAllByUserIdTest {
			@BeforeEach
			void setup() throws Exception {
				setRecord(FIXTURES_XML_01);
			}
			@AfterEach
			void tearDown() throws Exception {
				setRecord(FIXTURES_XML_00);
			}
			
			@ParameterizedTest
			@MethodSource("test01Provider")
			@DisplayName("Test-01：【正常系】指定されたユーザIDのユーザが投稿した記事をすべて削除できる")
			void test01(List<Integer> targets, List<ArticleBean> expectedList) throws Exception {
				// execute & verify
				for (int target : targets) {
					sut.deleteAllByUserId(target);
					List<ArticleBean> actual = sut.findAllByUserId(target);
					assertThat(actual.size(), is(0));
				}
			}
			
			static Stream<Arguments> test01Provider() {
				// setup
				int target = 0;
				List<Integer> targetList = null;
				List<List<Integer>> targets = new ArrayList<List<Integer>>();
				List<ArticleBean> expectedList = new ArrayList<ArticleBean>();
				// [1] ユーザID「1」のユーザが投稿したすべての記事を削除できる
				target = 1;
				targetList = new ArrayList<Integer>();
				targetList.add(target);
				targets.add(targetList);
				
				// テストパラメータを返却
				return Stream.of(
						Arguments.of(targets.get(0), expectedList)
						);
			}
			
		}
		
		@Nested
		@DisplayName("ArticleDAO#deleteByArticleId(int)メソッドのテストクラス")
		class DeleteTest {
			@BeforeEach
			void setup() throws Exception {
				setRecord(FIXTURES_XML_31);
			}
			@AfterEach
			void tearDown() throws Exception {
				setRecord(FIXTURES_XML_00);
			}
			
			@ParameterizedTest
			@MethodSource("test01Provider")
			@DisplayName("Test-01：【正常系】投稿された記事番号の記事を削除できる")
			void test01(List<ArticleBean> targetList, List<ArticleBean> expectedList) throws Exception {
				// execute & verify
				for (ArticleBean target : targetList) {
					sut.deleteByArticleId(target.getId());
					List<ArticleBean> actualList = sut.findAllByUserId(target.getUserId());
					if (actualList.size() > 0) {
						for (int  i = 0; i < expectedList.size(); ++i) {
							ArticleBean actual = actualList.get(i);
							ArticleBean exxpected = expectedList.get(i);
							assertThat(actual, is(new EqualToArticle(exxpected)));
						}
					}
				}
				
			}
			
			static Stream<Arguments> test01Provider() {
				// setup
				ArticleBean target = null;
				List<ArticleBean> targetList = null;
				List<List<ArticleBean>> targets = new ArrayList<>();
				List<ArticleBean> expectedList = null;
				List<List<ArticleBean>> expected = new ArrayList<List<ArticleBean>>();
				// [1] 記事番号「6」の記事を削除できるテスト
				target = new ArticleBean();
				target.setId(6);
				target.setUserId(1);
				targetList = new ArrayList<>();
				targetList.add(target);
				targets.add(targetList);
				expectedList = new ArrayList<ArticleBean>();
				expectedList.add(new ArticleBean(1, "研修開始", "これから技術を習得してエンジニアを目指します！", Timestamp.valueOf("2022-04-01 00:00:00"), 1));
				expectedList.add(new ArticleBean(2, "IT 基礎", "インターネットの仕組みとデータの流れを勉強しました", Timestamp.valueOf("2022-04-02 00:00:00"), 1));
				expectedList.add(new ArticleBean(3, "プログラミング", "条件分岐や繰り返し処理によって幅が広がってきました", Timestamp.valueOf("2022-04-12 00:00:00"), 1));
				expected.add(expectedList);
				// [2] 記事番号「6」および「２」の記事を削除できるテスト
				target = new ArticleBean();
				target.setId(6);
				target.setUserId(1);
				targetList = new ArrayList<>();
				target.setId(2);
				target.setUserId(1);
				targetList.add(target);
				target = new ArticleBean();
				targets.add(targetList);
				expectedList = new ArrayList<ArticleBean>();
				expectedList.add(new ArticleBean(1, "研修開始", "これから技術を習得してエンジニアを目指します！", Timestamp.valueOf("2022-04-01 00:00:00"), 1));
				expectedList.add(new ArticleBean(3, "プログラミング", "条件分岐や繰り返し処理によって幅が広がってきました", Timestamp.valueOf("2022-04-12 00:00:00"), 1));
				expected.add(expectedList);
				// [3] 記事番号「-1」の記事を削除するテスト
				target = new ArticleBean();
				target.setId(-1);
				target.setUserId(1);
				targetList = new ArrayList<>();
				targetList.add(target);
				target = new ArticleBean();
				targets.add(targetList);
				expectedList = new ArrayList<ArticleBean>();
				expectedList.add(new ArticleBean(1, "研修開始", "これから技術を習得してエンジニアを目指します！", Timestamp.valueOf("2022-04-01 00:00:00"), 1));
				expectedList.add(new ArticleBean(2, "IT 基礎", "インターネットの仕組みとデータの流れを勉強しました", Timestamp.valueOf("2022-04-02 00:00:00"), 1));
				expectedList.add(new ArticleBean(3, "プログラミング", "条件分岐や繰り返し処理によって幅が広がってきました", Timestamp.valueOf("2022-04-12 00:00:00"), 1));
				expectedList.add(new ArticleBean(6, "今日の食事", "朝食にイングリッシュマフィンを食べました。", Timestamp.valueOf("2022-05-01 00:00:00"), 1));
				expected.add(expectedList);
				
				// テストパラメータを返却
				return Stream.of(
						  Arguments.of(targets.get(0), expected.get(0))
						, Arguments.of(targets.get(1), expected.get(1))
						, Arguments.of(targets.get(2), expected.get(2))
						);
			}
			
		}
		
		@Nested
		@DisplayName("ArticleDAO#update(ArticleBean)メソッドのテストクラス")
		class UpdateTest {
			@BeforeEach
			void setup() throws Exception {
				setRecord(FIXTURES_XML_21);
			}
			@AfterEach
			void tearDown() throws Exception {
				setRecord(FIXTURES_XML_00);
			}
			
			@ParameterizedTest
			@MethodSource("test01Provider")
			@DisplayName("Test-01：【正常系】記事を変更できる")
			void test01(List<ArticleBean> targetList, List<ArticleBean> expectedList) throws Exception {
				// execute & verify
				for (ArticleBean target : targetList) {
					sut.update(target);
					List<ArticleBean> actualList = sut.findAllByUserId(target.getUserId());
					if (actualList.size() > 0) {
						for (int i = 0; i < actualList.size(); ++i) {
							ArticleBean actual = actualList.get(i);
							ArticleBean expected = expectedList.get(i);
							assertThat(actual, is(new EqualToArticle(expected)));
						}
					} else {
						fail("テスト対象メソッドは未実装です");
					}
				}
			}
			
			static Stream<Arguments> test01Provider() {
				// setup
				ArticleBean target = null;
				List<ArticleBean> targetList = null;
				List<List<ArticleBean>> targets = new ArrayList<List<ArticleBean>>();
				List<ArticleBean> expectedList = null;
				List<List<ArticleBean>>  expected = new ArrayList<>();
				// [１] 記事番号「６」の記事のタイトルを更新するテスト
				target = new ArticleBean(6, "今日の朝食", "朝食にイングリッシュマフィンを食べました。", Timestamp.valueOf("2022-05-01 00:00:00"), 1);
				targetList = new ArrayList<ArticleBean>();
				targetList.add(target);
				targets.add(targetList);
				expectedList = new ArrayList<ArticleBean>();
				expectedList.add(new ArticleBean(1, "研修開始", "これから技術を習得してエンジニアを目指します！", Timestamp.valueOf("2022-04-01 00:00:00"), 1));
				expectedList.add(new ArticleBean(2, "IT 基礎", "インターネットの仕組みとデータの流れを勉強しました", Timestamp.valueOf("2022-04-02 00:00:00"), 1));
				expectedList.add(new ArticleBean(3, "プログラミング", "条件分岐や繰り返し処理によって幅が広がってきました", Timestamp.valueOf("2022-04-12 00:00:00"), 1));
				expectedList.add(new ArticleBean(6, "今日の朝食", "朝食にイングリッシュマフィンを食べました。", Timestamp.valueOf("2022-05-01 00:00:00"), 1));
				expected.add(expectedList);

				// [2] 記事番号「６」の記事の内容を更新するテスト
				target = new ArticleBean(6, "今日の食事", "朝食にエッグベネディクトを食べました。", Timestamp.valueOf("2022-05-01 00:00:00"), 1);
				targetList = new ArrayList<ArticleBean>();
				targetList.add(target);
				targets.add(targetList);
				expectedList = new ArrayList<ArticleBean>();
				expectedList.add(new ArticleBean(1, "研修開始", "これから技術を習得してエンジニアを目指します！", Timestamp.valueOf("2022-04-01 00:00:00"), 1));
				expectedList.add(new ArticleBean(2, "IT 基礎", "インターネットの仕組みとデータの流れを勉強しました", Timestamp.valueOf("2022-04-02 00:00:00"), 1));
				expectedList.add(new ArticleBean(3, "プログラミング", "条件分岐や繰り返し処理によって幅が広がってきました", Timestamp.valueOf("2022-04-12 00:00:00"), 1));
				expectedList.add(new ArticleBean(6, "今日の食事", "朝食にエッグベネディクトを食べました。", Timestamp.valueOf("2022-05-01 00:00:00"), 1));
				expected.add(expectedList);
				
				
				//  テスト用パラメータを返却
				return Stream.of(
						  Arguments.of(targets.get(0), expected.get(0))
						, Arguments.of(targets.get(1), expected.get(1))
						);
			}
			
		}
		
		@Nested
		@DisplayName("ArticleDAO#insert(ArticleBean)メソッドのテストクラス")
		class InsertTest {
			@BeforeEach
			void setUp() throws Exception {
				setRecord(FIXTURES_XML_01);
			}
			@AfterEach
			void tearDown() throws Exception {
				setRecord(FIXTURES_XML_00);
			}
			
			@Test
			@DisplayName("Test-12：【異常系：null参照例外のテスト】nullである記事を保存するとDAO例外が発出される")
			void test12() {
				// setup
				ArticleBean target = null;
				// execute & verify
				DAOException exception = assertThrows(DAOException.class, () -> sut.insert(target));
				assertThat(exception.getMessage(), is("レコードの登録に失敗しました。"));
			}
			
			@Test
			@DisplayName("Test-11：【異常系：外部キー制約違反のテスト】登録されていないユーザIDで記事を保存するとDAO例外が発出される")
			void test11() {
				// setup
				int targetId = -1; // 負数のユーザIDは未登録：必ずDAO例外が発出される
				ArticleBean target = new ArticleBean("今日のやらかし", "この記事は登録することはできません。", targetId);
				// execute & verify
				DAOException exception = assertThrows(DAOException.class, () -> sut.insert(target));
				assertThat(exception.getMessage(), is("レコードの登録に失敗しました。"));
			}
			
			@ParameterizedTest
			@MethodSource("test01Provider")
			@DisplayName("Test-01：【正常系】投稿記事を保存できる")
			void test01(List<ArticleBean> targetList, List<ArticleBean> expectedList) throws Exception {
				// execute
				for (ArticleBean target : targetList) {
					sut.insert(target);
				}
				// verify
				for  (ArticleBean target : targetList) {
					// 検証用の改めて取得した記事リストを実行値として取得
					List<ArticleBean> actualList = sut.findAllByUserId(target.getUserId());
					// 実行値として取得した記事リストの件数によって検証処理を分岐
					if (actualList.size()> 0) {
						// 記事リストが1件以上ある場合：検証実施（期待値と比較）
						for (int i = 0; i < expectedList.size(); ++i) {
							ArticleBean actual = actualList.get(i);
							ArticleBean expected = expectedList.get(i);
							assertThat(actual, is(new EqualToArticle(expected)));
						}
					} else {
						// 記事リストが0建以下である場合：テスト対象メソッドの未実装扱い（問答無用でテストは失敗）
						fail("未実装です");
					}
				}
			}
			
			static Stream<Arguments> test01Provider() {
				// setup
				ArticleBean target = null;
				List<ArticleBean> targets = null;
				List<List<ArticleBean>> targetList = new ArrayList<List<ArticleBean>>();
				List<ArticleBean> expectedList = null;
				List<List<ArticleBean>>  expected = new ArrayList<>();
				// [1] ユーザID「1」のユーザがタイトル「今日の食事」投稿内容「朝食にイングリッシュマフィンを食べました。」という記事を投稿できる
				targets = new ArrayList<ArticleBean>();
				target = new ArticleBean("今日の食事", "朝食にイングリッシュマフィンを食べました。", 1);
				targets.add(target);
				targetList.add(targets);
				expectedList = new ArrayList<ArticleBean>();
				expectedList.add(new ArticleBean(1, "研修開始", "これから技術を習得してエンジニアを目指します！", Timestamp.valueOf("2022-04-01 00:00:00"), 1));
				expectedList.add(new ArticleBean(2, "IT 基礎", "インターネットの仕組みとデータの流れを勉強しました", Timestamp.valueOf("2022-04-02 00:00:00"), 1));
				expectedList.add(new ArticleBean(3, "プログラミング", "条件分岐や繰り返し処理によって幅が広がってきました", Timestamp.valueOf("2022-04-12 00:00:00"), 1));
				expectedList.add(new ArticleBean(6, "今日の食事", "朝食にイングリッシュマフィンを食べました。", Timestamp.valueOf("2022-05-01 00:00:00"), 1));
				expected.add(expectedList);
				// [2] ユーザID「1」のユーザがタイトル「今日の食事」投稿内容「朝食にイングリッシュマフィンを食べました。」という記事を投稿できる
				targets = new ArrayList<ArticleBean>();
				target = new ArticleBean("今日の食事", "朝食にイングリッシュマフィンを食べました。", 1);
				targets.add(target);
				target = new ArticleBean("DB実習", "データベースの接続情報はプロダクトに関係なくすべてのデータベースについて必要な情報である。", 1);
				targets.add(target);
				targetList.add(targets);
				expectedList = new ArrayList<ArticleBean>();
				expectedList.add(new ArticleBean(1, "研修開始", "これから技術を習得してエンジニアを目指します！", Timestamp.valueOf("2022-04-01 00:00:00"), 1));
				expectedList.add(new ArticleBean(2, "IT 基礎", "インターネットの仕組みとデータの流れを勉強しました", Timestamp.valueOf("2022-04-02 00:00:00"), 1));
				expectedList.add(new ArticleBean(3, "プログラミング", "条件分岐や繰り返し処理によって幅が広がってきました", Timestamp.valueOf("2022-04-12 00:00:00"), 1));
				expectedList.add(new ArticleBean(6, "今日の食事", "朝食にイングリッシュマフィンを食べました。", Timestamp.valueOf("2022-05-01 00:00:00"), 1));
				expectedList.add(new ArticleBean(22, "DB実習", "データベースの接続情報はプロダクトに関係なくすべてのデータベースについて必要な情報である。", Timestamp.valueOf("2022-07-04 00:00:00"), 1));				expected.add(expectedList);
				expected.add(expectedList);
				// テスト用パラメータを返却
				return Stream.of(
						  Arguments.of(targetList.get(0), expected.get(0))
						, Arguments.of(targetList.get(1), expected.get(1))
						);
				
			}
		}
		
		
	}

}
