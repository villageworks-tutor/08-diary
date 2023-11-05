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
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

import diary.bean.ArticleBean;
import diary.bean.CriteriaBean;
import diary.dao.matcher.EqualToArticle;

/**
 * ArticleDAOのテストクラス
 */
@TestInstance(Lifecycle.PER_CLASS)
class ArticleDaoTest extends BaseDaoTest {

	// データセットとして読み込むXMLファイルのパス
	protected static final String FIXTURES_XML_00 = DIR_FIXTURES + "記事_復元.xml";
	protected static final String FIXTURES_XML_01 = DIR_FIXTURES + "記事_登録数２０.xml";
	
	@Nested
	@DisplayName("検索系メソッドのテストクラス")
	@TestInstance(Lifecycle.PER_CLASS)
	class SearchMethodTest {
		
		@BeforeAll
		void setUp() throws Exception {
			// テスト用データセットの設定
			dataset = new FlatXmlDataSetBuilder().build(new FileInputStream(FIXTURES_XML_01));
			// データセットの投入
			DatabaseOperation.CLEAN_INSERT.execute(dbUnitConnection, dataset);
		}
		
		@AfterAll
		void tearDown() throws Exception {
			// 復元用データセットの設定
			dataset = new FlatXmlDataSetBuilder().build(new FileInputStream(FIXTURES_XML_00));
			// データの復元
			DatabaseOperation.CLEAN_INSERT.execute(dbUnitConnection, dataset);
		}
		
		@Nested
		@DisplayName("ArticleDAO#countAllByUserId(CriteriaBean)")
		class CountAllbyUserIdTest {
			void setup() throws Exception {
				// テスト用データセットの設定
				dataset = new FlatXmlDataSetBuilder().build(new FileInputStream(FIXTURES_XML_01));
				// データセットの投入
				DatabaseOperation.CLEAN_INSERT.execute(dbUnitConnection, dataset);
			}
			void tearDown() throws Exception {
				// テスト用データセットの設定
				dataset = new FlatXmlDataSetBuilder().build(new FileInputStream(FIXTURES_XML_00));
				// データセットの投入
				DatabaseOperation.CLEAN_INSERT.execute(dbUnitConnection, dataset);
			}
			
			@ParameterizedTest
			@CsvSource({
				  "1, 12" // ユーザID「１」のユーザが投稿したすべての記事の件数は12件である
				, "2, 8"  // ユーザID「２」のユーザが投稿したすべての記事の件数は8件である
				, "-1, 0" // ユーザID「-1」のユーザが投稿したすべての記事の件数は0件である
			})
			@DisplayName("Test-01：【正常系】")
			void test01(int userId, int expected) throws Exception {
				// setup
				CriteriaBean target = new CriteriaBean();
				target.setUserId(userId);
				// execute
				int actual = sut.countAllByUserId(target);
				// verify
				assertThat(actual, is(expected));
			}			
		}
		
		@Nested
		@DisplayName("ArticleDAO#findByUserIdWithPagination(CriteriaBean)メソッドのテスト")
		@TestInstance(Lifecycle.PER_CLASS)
		class FindWithPaginationTest {
			@BeforeAll
			void setUp() throws Exception {
				// テスト用データセットの設定
				dataset = new FlatXmlDataSetBuilder().build(new FileInputStream(FIXTURES_XML_01));
				// データセットの投入
				DatabaseOperation.CLEAN_INSERT.execute(dbUnitConnection, dataset);
			}
			@AfterAll
			void tearDown() throws Exception {
				// 復元用データセットの設定
				dataset = new FlatXmlDataSetBuilder().build(new FileInputStream(FIXTURES_XML_00));
				// データの復元
				DatabaseOperation.CLEAN_INSERT.execute(dbUnitConnection, dataset);
			}
						
			@Nested
			@DisplayName("【ユーザIDごとに記事を検索した結果をページあたりの件数分だけ取得するテスト】")
			class PaginationSearchTest {
				@ParameterizedTest
				@MethodSource("test02Provider")
				@DisplayName("【Test-02：正常系】指定したページの記事の件数を確認するテスト")
				void test02(CriteriaBean criteria, int expected) throws Exception {
					// expectedList.add(new ArticleBean(1, "研修開始1", "これから技術を習得してエンジニアを目指します！", Timestamp.valueOf("2022-04-01 00:00:00"), 1));
					// expectedList.add(new ArticleBean(2, "IT 基礎1", "インターネットの仕組みとデータの流れを勉強しました", Timestamp.valueOf("2022-04-02 00:00:00"), 1));
					// expectedList.add(new ArticleBean(3, "プログラミング1", "条件分岐や繰り返し処理によって幅が広がってきました", Timestamp.valueOf("2022-04-12 00:00:00"), 1));
					// expectedList.add(new ArticleBean(6, "研修開始2", "これから技術を習得してエンジニアを目指します！", Timestamp.valueOf("2022-05-01 00:00:00"), 1));
					// expectedList.add(new ArticleBean(7, "IT 基礎2", "インターネットの仕組みとデータの流れを勉強しました", Timestamp.valueOf("2022-05-02 00:00:00"), 1));
					//expectedList.add(new ArticleBean(8, "プログラミング2", "条件分岐や繰り返し処理によって幅が広がってきました", Timestamp.valueOf("2022-05-12 00:00:00"), 1));
					//expectedList.add(new ArticleBean(11, "研修開始3", "これから技術を習得してエンジニアを目指します！", Timestamp.valueOf("2022-06-01 00:00:00"), 1));
					//expectedList.add(new ArticleBean(12, "IT 基礎3", "インターネットの仕組みとデータの流れを勉強しました", Timestamp.valueOf("2022-06-02 00:00:00"), 1));
					//expectedList.add(new ArticleBean(13, "プログラミング3", "条件分岐や繰り返し処理によって幅が広がってきました", Timestamp.valueOf("2022-06-12 00:00:00"), 1));
					//expectedList.add(new ArticleBean(16, "研修開始4", "これから技術を習得してエンジニアを目指します！", Timestamp.valueOf("2022-07-01 00:00:00"), 1));
					// expectedList.add(new ArticleBean(17, "IT 基礎4", "インターネットの仕組みとデータの流れを勉強しました", Timestamp.valueOf("2022-07-02 00:00:00"), 1));
					// expectedList.add(new ArticleBean(18, "プログラミング4", "条件分岐や繰り返し処理によって幅が広がってきました", Timestamp.valueOf("2022-07-12 00:00:00"), 1));
					// execute
					List<ArticleBean> list = sut.findByUserIdWithPagination(criteria);
					// verify
					int  actual = list.size();
					assertThat(actual, is(expected));
				}
				
				static Stream<Arguments> test02Provider() {
					// setup
					int userId = -1;
					int limits = -1;
					int page   = -1;
					List<CriteriaBean>  criteria = new ArrayList<CriteriaBean>();
					List<Integer> expected = new ArrayList<Integer>();
					// [1] ユーザID「１」のユーザが投稿した記事をページあたり5件表示した場合２ページ目には5件表示されるテスト
					userId = 1;
					limits = 5;
					page = 2;
					criteria.add(new CriteriaBean(userId, limits, page));
					expected.add(5);
					// [2] ユーザID「１」のユーザが投稿した記事をページあたり5件表示した場合最終ページ以降に表示される記事は0件であるテスト
					userId = 1;
					limits = 5;
					page = 99;
					criteria.add(new CriteriaBean(userId, limits, page));
					expected.add(0);
					
					// テスト用パラメータを返却
					return Stream.of(
							  Arguments.of(criteria.get(0), expected.get(0))
							, Arguments.of(criteria.get(1), expected.get(1))
							);
				}
				
				@ParameterizedTest
				@MethodSource("test01Provider")
				@DisplayName("【Test-01：正常系】指定したページのレコードを取得できるテスト")
				void test01(CriteriaBean criteria, List<ArticleBean> expectedList) throws Exception {
					// execute & verify
					List<ArticleBean> actualList = sut.findByUserIdWithPagination(criteria);
					if (actualList.size() > 0) {
						for (int i = 0; i < expectedList.size(); i++) {
							ArticleBean actual = actualList.get(i);
							ArticleBean expected = expectedList.get(i);
							assertThat(actual, is(new EqualToArticle(expected)));
						}
					}
				}
				
				static Stream<Arguments> test01Provider() {
					// setup
					List<ArticleBean> expectedList = null;
					List<List<ArticleBean>> expected = new ArrayList<List<ArticleBean>>();
					List<CriteriaBean> criteria = new ArrayList<CriteriaBean>();
					// [1] ユーザID「２」のユーザが投稿した記事をページあたり３件表示の先頭ページに表示する分を取得するテスト
					int userId = 2;
					int limits = 3;
					int page = 1;
					criteria.add(new CriteriaBean(userId, limits,	page));
					expectedList = new ArrayList<ArticleBean>();
					expectedList.add(new ArticleBean(4, "ペットの紹介1", "猫を 2 匹飼っています", Timestamp.valueOf("2022-04-01 00:00:00"), 2));
					expectedList.add(new ArticleBean(5, "今日の天気1", "突然のにわか雨でした", Timestamp.valueOf("2022-04-02 00:00:00"), 2));
					expectedList.add(new ArticleBean(9, "ペットの紹介2", "猫を 2 匹飼っています", Timestamp.valueOf("2022-05-01 00:00:00"), 2));
					expected.add(expectedList);
					// [2] ユーザID「２」のユーザが投稿した記事をページあたり３件表示の２ページ目に表示する分を取得するテスト
					userId = 2;
					limits = 3;
					page = 2;
					criteria.add(new CriteriaBean(userId, limits,	page));
					expectedList = new ArrayList<ArticleBean>();
					expectedList.add(new ArticleBean(10, "今日の天気2", "突然のにわか雨でした", Timestamp.valueOf("2022-05-02 00:00:00"), 2));
					expectedList.add(new ArticleBean(14, "ペットの紹介3", "猫を 2 匹飼っています", Timestamp.valueOf("2022-06-01 00:00:00"), 2));
					expectedList.add(new ArticleBean(15, "今日の天気3", "突然のにわか雨でした", Timestamp.valueOf("2022-06-02 00:00:00"), 2));
					expected.add(expectedList);
					// [2] ユーザID「２」のユーザが投稿した記事をページあたり３件表示の２ページ目に表示する分を取得するテスト
					userId = 2;
					limits = 3;
					page = 3;
					criteria.add(new CriteriaBean(userId, limits,	page));
					expectedList = new ArrayList<ArticleBean>();
					expectedList.add(new ArticleBean(19, "ペットの紹介4", "猫を 2 匹飼っています", Timestamp.valueOf("2022-07-01 00:00:00"), 2));
					expectedList.add(new ArticleBean(20, "今日の天気4", "突然のにわか雨でした", Timestamp.valueOf("2022-07-02 00:00:00"), 2));
					expected.add(expectedList);
					
					// テスト用パラメータを返却
					return Stream.of(
							  Arguments.of(criteria.get(0), expected.get(0))
							, Arguments.of(criteria.get(1), expected.get(1))
							, Arguments.of(criteria.get(2), expected.get(2))
							);
				}
			}
		}
		
		@Nested
		@DisplayName("ArticleDAO#countByUserIdAndLikeKeywordメソッドのテスト")
		class CountByUserIdAndLikeKeywordTest {
			@Nested
			@DisplayName("【件数をカウントするテスト】")
			class count {
				@ParameterizedTest
				@MethodSource("countProvider")
				@DisplayName("【Test-01：件数をカウントするテスト】指定したユーザIDのユーザが投稿した記事のちキーワードが含まれている記事の件数を取得するテスト")
				void testCountByUserIdAndLikeKeyword(CriteriaBean criteria, int expected) throws Exception {
					// execute
					int actual = sut.countByUserIdAndLikeKeyword(criteria);
					// verify
					assertThat(actual, is(expected));
				}
				
				/**
				 * 引数が基本データ型であるfindByUserIdAndLikeKeywordテスト用のテストパラメータを生成する
				 * @return
				 */
				static Stream<Arguments> countProvider() {
					// setup
					List<CriteriaBean> criteria = new ArrayList<CriteriaBean>();
					List<Integer> expected = new ArrayList<Integer>();
					// [1] ユーザID「1」のユーザが投稿した記事のうちキーワード「繰り返し処理」を含む記事の件数をカウントするテスト
					criteria.add(new CriteriaBean("繰り返し処理", 1));
					expected.add(4);
					// [2] ユーザID「2」のユーザが投稿した記事のうちキーワード「今日」を含む記事の件数をカウントするテスト
					criteria.add(new CriteriaBean("今日", 2));
					expected.add(4);
					// [3] ユーザID「1」のユーザが投稿した記事のうちキーワード「にわか雨」を含む記事の件数をカウントするテスト
					criteria.add(new CriteriaBean("にわか雨", 1));
					expected.add(0);
					// [4] ユーザID「2」のユーザが投稿した記事のうちキーワードを未入力とする場合の記事の件数をカウントするテスト
					criteria.add(new CriteriaBean("", 2));
					expected.add(8);
					// テストパラメータを返却
					return Stream.of(
							  Arguments.of(criteria.get(0), expected.get(0))
							, Arguments.of(criteria.get(1), expected.get(1))
							);
				}
			}
		}
		
		@Nested
		@DisplayName("ArticleDAO#findByUserIdAndLikeKeywordWithPagingメソッドのテスト")
		class FindByUserIdAndLikeKeywordWithPagingTest {
			// クラス定数
			private static final int LIMIT_PAER_PAGE = 3;   // ページあたりの表示件数
			private static final int FIRST_PAGE_NOMBRE = 1; // 先頭ページノンブル
			@Nested
			@DisplayName("【キーワードを入力するテスト】")
			class paging01 {
				@ParameterizedTest
				@MethodSource("argumentPagingProvider")
				@DisplayName("【Test-01：キーワード検索の結果をページごとに表示するテスト】指定したユーザIDのユーザが投稿した記事のうち指定したキーワードが含まれている記事を取得するテスト")
				void testFindByUSerIdAndLikeKeywordWithPaging(CriteriaBean criteria, List<ArticleBean> expectedList) throws Exception {
					// execute
					List<ArticleBean> actualList = sut.findByUserIdAndLikeKeywordWithPaging(criteria);
					// verify
					if (actualList.size() > 0) {
						for (int i = 0; i < expectedList.size(); i++) {
							ArticleBean actual = actualList.get(i);
							ArticleBean expected = expectedList.get(i);
							assertThat(actual, is(new EqualToArticle(expected)));
						}
					} else {
						assertTrue(actualList.isEmpty());
					}
					
				}
				
				/**
				 * 引数が基本データ型であるfindByUserIdAndLikeKeywordテスト用のテストパラメータを生成する
				 * @return
				 */
				static Stream<Arguments> argumentPagingProvider() {
					// setup
					int userId = -1;
					String keyword = "";
					int limits = LIMIT_PAER_PAGE;
					int page = FIRST_PAGE_NOMBRE;
					List<CriteriaBean> criteria = new ArrayList<CriteriaBean>();
					List<ArticleBean> expectedList = null;
					List<List<ArticleBean>> expected = new ArrayList<List<ArticleBean>>();
					// [1] 【先頭ページのレコードを取得するテスト】ユーザID「1」のユーザが投稿した記事のうちキーワード「条件分岐」を含む記事をページあたり3件表示する場合の先頭ページに表示する記事を取得するテスト
					userId = 1;
					keyword = "条件分岐";
					criteria.add(new CriteriaBean(keyword, userId, limits, page));
					expectedList = new ArrayList<ArticleBean>();
					expectedList.add(new ArticleBean(3, "プログラミング1", "条件分岐や繰り返し処理によって幅が広がってきました", Timestamp.valueOf("2022-04-12 00:00:00"), 1));
					expectedList.add(new ArticleBean(8, "プログラミング2", "条件分岐や繰り返し処理によって幅が広がってきました", Timestamp.valueOf("2022-05-12 00:00:00"), 1));
					expectedList.add(new ArticleBean(13, "プログラミング3", "条件分岐や繰り返し処理によって幅が広がってきました", Timestamp.valueOf("2022-06-12 00:00:00"), 1));
					expected.add(expectedList);
					// [2] 【先頭および末尾以外のページのレコードを取得するテスト】ユーザID「1」のユーザが投稿した記事のうちキーワード「ました」を含む記事をページあたり3件表示する場合の2ページ目に表示する記事を取得するテスト
					page = 2;
					keyword= "ました";
					criteria.add(new CriteriaBean(keyword, userId, limits, page));
					expectedList = new ArrayList<ArticleBean>();
					expectedList.add(new ArticleBean(8, "プログラミング2", "条件分岐や繰り返し処理によって幅が広がってきました", Timestamp.valueOf("2022-05-12 00:00:00"), 1));
					expectedList.add(new ArticleBean(12, "IT 基礎3", "インターネットの仕組みとデータの流れを勉強しました", Timestamp.valueOf("2022-06-02 00:00:00"), 1));
					expectedList.add(new ArticleBean(13, "プログラミング3", "条件分岐や繰り返し処理によって幅が広がってきました", Timestamp.valueOf("2022-06-12 00:00:00"), 1));
					expected.add(expectedList);
					// [3] 【最終ページのレコードを取得するテスト】ユーザID「1」のユーザが投稿した記事のうちキーワード「ました」を含む記事をページあたり3件表示する場合の最終ページに表示する記事を取得するテスト
					page = 3;
					keyword= "ました";
					criteria.add(new CriteriaBean(keyword, userId, limits, page));
					expectedList = new ArrayList<ArticleBean>();
					expectedList.add(new ArticleBean(17, "IT 基礎4", "インターネットの仕組みとデータの流れを勉強しました", Timestamp.valueOf("2022-07-02 00:00:00"), 1));
					expectedList.add(new ArticleBean(18, "プログラミング4", "条件分岐や繰り返し処理によって幅が広がってきました", Timestamp.valueOf("2022-07-12 00:00:00"), 1));
					expected.add(expectedList);
					// [4] 【最終ページのレコードを取得するテスト】ユーザID「2」のユーザが投稿した記事のうちキーワードを未入力でページあたり5件表示する場合の先頭ページに表示する記事を取得するテスト
					userId = 2;
					limits = 5;
					criteria.add(new CriteriaBean(keyword, userId, limits, page));
					expectedList = new ArrayList<ArticleBean>();
					expectedList.add(new ArticleBean(4, "ペットの紹介1", "猫を 2 匹飼っています", Timestamp.valueOf("2022-04-01 00:00:00"), 2));
					expectedList.add(new ArticleBean(5, "今日の天気1", "突然のにわか雨でした", Timestamp.valueOf("2022-04-02 00:00:00"), 2));
					expectedList.add(new ArticleBean(9, "ペットの紹介2", "猫を 2 匹飼っています", Timestamp.valueOf("2022-05-01 00:00:00"), 2));
					expectedList.add(new ArticleBean(10, "今日の天気2", "突然のにわか雨でした", Timestamp.valueOf("2022-05-02 00:00:00"), 2));
					expectedList.add(new ArticleBean(14, "ペットの紹介3", "猫を 2 匹飼っています", Timestamp.valueOf("2022-06-01 00:00:00"), 2));
					expected.add(expectedList);
					// テストパラメータを返却
					return Stream.of(
							  Arguments.of(criteria.get(0), expected.get(0))
							, Arguments.of(criteria.get(1), expected.get(1))
							, Arguments.of(criteria.get(2), expected.get(2))
							, Arguments.of(criteria.get(3), expected.get(3))
							);
				}
			}
		}

		@Nested
		@DisplayName("ArticleDAO#findByUserIdAndLikeKeywordメソッドのテスト")
		class FindByUserIdAndLikeKeywordTest {
			// クラス定数
			private static final int LIMIT_PAER_PAGE = 3;   // ページあたりの表示件数
			private static final int FIRST_PAGE_NOMBRE = 1; // 先頭ページノンブル
			@Nested
			@DisplayName("【引数がCriteriaBeanのインスタンスである場合】")
			class argument02 {
				@ParameterizedTest
				@MethodSource("argumentCriteriaProvider")
				@DisplayName("【Test-02：登録済ユーザの投稿記事のテスト】")
				void testFindByUserIdAndLikeKeyword(CriteriaBean criteria, List<ArticleBean> expectedList) throws Exception {
					// execute
					List<ArticleBean> actualList = sut.findByUserIdAndLikeKeyword(criteria);
					// verify
					if (actualList.size() > 0) {
						for (int i = 0; i < expectedList.size(); i++) {
							ArticleBean actual = actualList.get(i);
							ArticleBean expected = expectedList.get(i);
							assertThat(actual, is(new EqualToArticle(expected)));
						}
					} else {
						assertTrue(actualList.isEmpty());
					}
					
				}
				
				/**
				 * 引数が基本データ型であるfindByUserIdAndLikeKeywordテスト用のテストパラメータを生成する
				 * @return
				 */
				static Stream<Arguments> argumentCriteriaProvider() {
					// setup
					int userId = -1;
					String keyword = "";
					int limits = LIMIT_PAER_PAGE;
					int page = FIRST_PAGE_NOMBRE;
					List<CriteriaBean> criteria = new ArrayList<CriteriaBean>();
					List<ArticleBean> expectedList = null;
					List<List<ArticleBean>> expected = new ArrayList<List<ArticleBean>>();
					// [1] 【中間一致】ユーザID「2」のユーザが投稿した記事のうち「にわか雨」で始まる記事を取得するテスト
					userId = 2;
					keyword = "にわか雨";
					criteria.add(new CriteriaBean(keyword, userId, 20, 1));
					expectedList = new ArrayList<ArticleBean>();
					expectedList.add(new ArticleBean(5, "今日の天気1", "突然のにわか雨でした", Timestamp.valueOf("2022-04-02 00:00:00"), 2));
					expectedList.add(new ArticleBean(10, "今日の天気2", "突然のにわか雨でした", Timestamp.valueOf("2022-05-02 00:00:00"), 2));
					expectedList.add(new ArticleBean(15, "今日の天気3", "突然のにわか雨でした", Timestamp.valueOf("2022-06-02 00:00:00"), 2));
					expectedList.add(new ArticleBean(20, "今日の天気4", "突然のにわか雨でした", Timestamp.valueOf("2022-07-02 00:00:00"), 2));
					expected.add(expectedList);
					// [2] 【後方一致】ユーザID「2」のユーザが投稿した記事のうち「います」で終わる記事を取得できるテスト
					userId = 2;
					keyword = "います";
					criteria.add(new CriteriaBean(keyword, userId, 20, 1));
					expectedList = new ArrayList<ArticleBean>();
					expectedList.add(new ArticleBean(4, "ペットの紹介1", "猫を 2 匹飼っています", Timestamp.valueOf("2022-04-01 00:00:00"), 2));
					expectedList.add(new ArticleBean(9, "ペットの紹介2", "猫を 2 匹飼っています", Timestamp.valueOf("2022-05-01 00:00:00"), 2));
					expectedList.add(new ArticleBean(14, "ペットの紹介3", "猫を 2 匹飼っています", Timestamp.valueOf("2022-06-01 00:00:00"), 2));
					expectedList.add(new ArticleBean(19, "ペットの紹介4", "猫を 2 匹飼っています", Timestamp.valueOf("2022-07-01 00:00:00"), 2));
					expected.add(expectedList);
					// [3] 【ユーザIDごとの全件検索】ユーザID「1」のユーザが投稿した記事のうちキーワードに空文字列「」を指定して全ての記事を取得するテスト
					userId = 1;
					keyword = ""; // 全件検索のためキーワードは空文字列
					criteria.add(new CriteriaBean(keyword, userId, 20, 1));
					expectedList = new ArrayList<ArticleBean>();
					expectedList.add(new ArticleBean(1, "研修開始1", "これから技術を習得してエンジニアを目指します！", Timestamp.valueOf("2022-04-01 00:00:00"), 1));
					expectedList.add(new ArticleBean(2, "IT 基礎1", "インターネットの仕組みとデータの流れを勉強しました", Timestamp.valueOf("2022-04-02 00:00:00"), 1));
					expectedList.add(new ArticleBean(3, "プログラミング1", "条件分岐や繰り返し処理によって幅が広がってきました", Timestamp.valueOf("2022-04-12 00:00:00"), 1));
					expectedList.add(new ArticleBean(6, "研修開始2", "これから技術を習得してエンジニアを目指します！", Timestamp.valueOf("2022-05-01 00:00:00"), 1));
					expectedList.add(new ArticleBean(7, "IT 基礎2", "インターネットの仕組みとデータの流れを勉強しました", Timestamp.valueOf("2022-05-02 00:00:00"), 1));
					expectedList.add(new ArticleBean(8, "プログラミング2", "条件分岐や繰り返し処理によって幅が広がってきました", Timestamp.valueOf("2022-05-12 00:00:00"), 1));
					expectedList.add(new ArticleBean(11, "研修開始3", "これから技術を習得してエンジニアを目指します！", Timestamp.valueOf("2022-06-01 00:00:00"), 1));
					expectedList.add(new ArticleBean(12, "IT 基礎3", "インターネットの仕組みとデータの流れを勉強しました", Timestamp.valueOf("2022-06-02 00:00:00"), 1));
					expectedList.add(new ArticleBean(13, "プログラミング3", "条件分岐や繰り返し処理によって幅が広がってきました", Timestamp.valueOf("2022-06-12 00:00:00"), 1));
					expectedList.add(new ArticleBean(16, "研修開始4", "これから技術を習得してエンジニアを目指します！", Timestamp.valueOf("2022-07-01 00:00:00"), 1));
					expectedList.add(new ArticleBean(17, "IT 基礎4", "インターネットの仕組みとデータの流れを勉強しました", Timestamp.valueOf("2022-07-02 00:00:00"), 1));
					expectedList.add(new ArticleBean(18, "プログラミング4", "条件分岐や繰り返し処理によって幅が広がってきました", Timestamp.valueOf("2022-07-12 00:00:00"), 1));
					expected.add(expectedList);
					// テストパラメータを返却
					return Stream.of(
							Arguments.of(criteria.get(0), expected.get(0)),
							Arguments.of(criteria.get(1), expected.get(1)),
							Arguments.of(criteria.get(2), expected.get(2))
							);
				}
				
			}
			
			@Nested
			@DisplayName("【引数が基本データ型と文字列型のリストである場合】")
			class argument01 {

				@ParameterizedTest
				@MethodSource("argumentIntAndStringProvider")
				@DisplayName("【Test-01：登録済ユーザの投稿記事のテスト】ユーザID「1」のユーザが投稿した記事を取得するテーストケース")
				void testFindByUserIdAndLikeKeyword(int userId, String keyword, List<ArticleBean> expectedList) throws Exception {
					// execute
					List<ArticleBean> actualList = sut.findByUserIdAndLikeKeyword(userId, keyword);
					// verify
					if (actualList.size() > 0) {
						for (int i = 0; i < expectedList.size(); i++) {
							ArticleBean actual = actualList.get(i);
							ArticleBean expected = expectedList.get(i);
							assertThat(actual, is(new EqualToArticle(expected)));
						}
					} else {
						assertTrue(actualList.isEmpty());
					}
					
				}
				
				/**
				 * 引数が基本データ型であるfindByUserIdAndLikeKeywordテスト用のテストパラメータを生成する
				 * @return
				 */
				static Stream<Arguments> argumentIntAndStringProvider() {
					// setup
					int user = -1;
					List<Integer> users = new ArrayList<Integer>();
					List<String> keywords = new ArrayList<String>();
					List<ArticleBean> expectedList = null;
					List<List<ArticleBean>> expected = new ArrayList<List<ArticleBean>>();
					// [1] 【前方一致】ユーザID「1」のユーザが投稿した記事のうち「研修」で始まる記事を取得するテスト
					user = 1;
					users.add(user);
					keywords.add("研修");
					expectedList = new ArrayList<ArticleBean>();
					expectedList.add(new ArticleBean(1, "研修開始1", "これから技術を習得してエンジニアを目指します！", Timestamp.valueOf("2022-04-01 00:00:00"), 1));
					expectedList.add(new ArticleBean(6, "研修開始2", "これから技術を習得してエンジニアを目指します！", Timestamp.valueOf("2022-05-01 00:00:00"), 1));
					expectedList.add(new ArticleBean(11, "研修開始3", "これから技術を習得してエンジニアを目指します！", Timestamp.valueOf("2022-06-01 00:00:00"), 1));
					expectedList.add(new ArticleBean(16, "研修開始4", "これから技術を習得してエンジニアを目指します！", Timestamp.valueOf("2022-07-01 00:00:00"), 1));
					expected.add(expectedList);
					// [2] 未登録ユーザ（ユーザID「-1」）が投稿した記事のうち「研修」は空リストが返される
					user = -1;
					users.add(user);
					keywords.add("研修");
					expectedList = new ArrayList<ArticleBean>();
					expected.add(expectedList);
					// テストパラメータを返却
					return Stream.of(
							Arguments.of(users.get(0), keywords.get(0), expected.get(0)),
							Arguments.of(users.get(1), keywords.get(1), expected.get(1))
							);
				}
				
			}
		}
		
		@Nested
		@DisplayName("ArticleDAO#findByArticleIdメソッドのテストクラス")
		class FindByArticleIdTest {
			@Nested
			@DisplayName("【引数が基本データ型である場合】")
			class TestFindByArticleId {
				@Test
				@DisplayName("【Test-02：未投稿記事番号のテスト】記事番号「-1」の記事を取得できないテストケース")
				void testFindByArticleId02() throws Exception {
					// setup
					int target = -1;
					// execute
					ArticleBean actual = sut.findByArticleId(target);
					// verify
					assertThat(actual, is(nullValue()));
				}
				
				@Test
				@DisplayName("【Test-01：投稿済記事番号のテスト】記事番号「1」の記事を取得するテーストケース")
				void testFindByArticleId01() throws Exception {
					// setup
					int target = 1;
					ArticleBean expected = new ArticleBean(1, "研修開始1", "これから技術を習得してエンジニアを目指します！", Timestamp.valueOf("2022-04-01 00:00:00"), 1);
					// execute
					ArticleBean actual = sut.findByArticleId(target);
					// verify
					assertThat(actual, is(new EqualToArticle(expected)));
				}
			}
		}
		
		@Nested
		@DisplayName("ArticleDAO#findAllByUserIdメソッドのテストクラス")
		class FindAllByUserIdTest {
			@Nested
			@DisplayName("【引数がCriteriaBeanのインスタンスである場合】")
			class TestFindAllByUserIdWithCriteria {
				@ParameterizedTest
				@MethodSource("argumentCriteriaProvider")
				@DisplayName("【Test-01：登録済ユーザの投稿記事のテスト】ユーザID「2」のユーザが投稿した記事を取得するテーストケース")
				void testFindAllByUserId01(CriteriaBean criteria, List<ArticleBean> expectedList) throws Exception {
					// execute
					List<ArticleBean> actualList = sut.findAllByUserId(criteria);
					// verify
					if (actualList.size() > 0) {
						for (int i = 0; i < expectedList.size(); i++) {
							ArticleBean actual = actualList.get(i);
							ArticleBean expected = expectedList.get(i);
							assertThat(actual, is(new EqualToArticle(expected)));
						}
					} else {
						assertTrue(actualList.isEmpty());
					}
				}
				
				/**
				 * 引数が基本データ型であるfindAllByUserIdテスト用のテストパラメータを生成する
				 * @return
				 */
				static Stream<Arguments> argumentCriteriaProvider() {
					// setup
					int userId = -1;
					List<CriteriaBean>criteria = new ArrayList<>();
					List<ArticleBean> expectedList = null;
					List<List<ArticleBean>> expected = new ArrayList<List<ArticleBean>>();
					// [1] ユーザID「1」のユーザが投稿したすべての記事を取得するテスト
					userId = 2;
					criteria.add(new CriteriaBean(userId, 20, 1));
					expectedList = new ArrayList<ArticleBean>();
					expectedList.add(new ArticleBean(4, "ペットの紹介1", "猫を 2 匹飼っています", Timestamp.valueOf("2022-04-01 00:00:00"), 2));
					expectedList.add(new ArticleBean(5, "今日の天気1", "突然のにわか雨でした", Timestamp.valueOf("2022-04-02 00:00:00"), 2));
					expectedList.add(new ArticleBean(9, "ペットの紹介2", "猫を 2 匹飼っています", Timestamp.valueOf("2022-05-01 00:00:00"), 2));
					expectedList.add(new ArticleBean(10, "今日の天気2", "突然のにわか雨でした", Timestamp.valueOf("2022-05-02 00:00:00"), 2));
					expectedList.add(new ArticleBean(14, "ペットの紹介3", "猫を 2 匹飼っています", Timestamp.valueOf("2022-06-01 00:00:00"), 2));
					expectedList.add(new ArticleBean(15, "今日の天気3", "突然のにわか雨でした", Timestamp.valueOf("2022-06-02 00:00:00"), 2));
					expectedList.add(new ArticleBean(19, "ペットの紹介4", "猫を 2 匹飼っています", Timestamp.valueOf("2022-07-01 00:00:00"), 2));
					expectedList.add(new ArticleBean(20, "今日の天気4", "突然のにわか雨でした", Timestamp.valueOf("2022-07-02 00:00:00"), 2));
					expected.add(expectedList);
					// [2] 未登録ユーザ（ユーザID「-1」）が投稿した記事は空リストが返される
					criteria.add(new CriteriaBean(userId, 20, 1));
					expectedList = new ArrayList<ArticleBean>();
					expected.add(expectedList);
					// テストパラメータを返却
					return Stream.of(
							Arguments.of(criteria.get(0), expected.get(0)),
							Arguments.of(criteria.get(1), expected.get(1))
							);
				}
				
			}
			
			@Nested
			@DisplayName("【引数が基本データ型である場合】")
			class TestFindAllByUserWithInt {
				@ParameterizedTest
				@CsvSource({
					  " 2, 8" // ユーザID「2」のユーザの投稿記事数は8件である
					, "-1, 0" // 未登録ユーザの投稿記事数は0件である
				})
				@DisplayName("【Test-02：投稿記事数のテスト】ユーザ投稿した記事数のテストケース")
				void testFindAllByUserId02(int userId, int expected) throws Exception {
					// execute
					List<ArticleBean> actualList = sut.findAllByUserId(userId);
					int actual = actualList.size();
					// verify
					assertThat(actual, is(expected));
				}
				
				@ParameterizedTest
				@MethodSource("argumentIntProvider")
				@DisplayName("【Test-01：登録済ユーザの投稿記事のテスト】ユーザID「1」のユーザが投稿した記事を取得するテーストケース")
				void testFindAllByUserId01(int userId, List<ArticleBean> expectedList) throws Exception {
					// execute
					List<ArticleBean> actualList = sut.findAllByUserId(userId);
					// verify
					if (actualList.size() > 0) {
						for (int i = 0; i < expectedList.size(); i++) {
							ArticleBean actual = actualList.get(i);
							ArticleBean expected = expectedList.get(i);
							assertThat(actual, is(new EqualToArticle(expected)));
						}
					} else {
						assertTrue(actualList.isEmpty());
					}
				}
				
				/**
				 * 引数が基本データ型であるfindAllByUserIdテスト用のテストパラメータを生成する
				 * @return
				 */
				static Stream<Arguments> argumentIntProvider() {
					// setup
					int user = 1;
					List<Integer> users = new ArrayList<Integer>();
					List<ArticleBean> expectedList = null;
					List<List<ArticleBean>> expected = new ArrayList<List<ArticleBean>>();
					// [1] ユーザID「1」のユーザが投稿したすべての記事を取得するテスト
					user = 1;
					users.add(user);
					expectedList = new ArrayList<ArticleBean>();
					expectedList.add(new ArticleBean(1, "研修開始1", "これから技術を習得してエンジニアを目指します！", Timestamp.valueOf("2022-04-01 00:00:00"), 1));
					expectedList.add(new ArticleBean(2, "IT 基礎1", "インターネットの仕組みとデータの流れを勉強しました", Timestamp.valueOf("2022-04-02 00:00:00"), 1));
					expectedList.add(new ArticleBean(3, "プログラミング1", "条件分岐や繰り返し処理によって幅が広がってきました", Timestamp.valueOf("2022-04-12 00:00:00"), 1));
					expectedList.add(new ArticleBean(6, "研修開始2", "これから技術を習得してエンジニアを目指します！", Timestamp.valueOf("2022-05-01 00:00:00"), 1));
					expectedList.add(new ArticleBean(7, "IT 基礎2", "インターネットの仕組みとデータの流れを勉強しました", Timestamp.valueOf("2022-05-02 00:00:00"), 1));
					expectedList.add(new ArticleBean(8, "プログラミング2", "条件分岐や繰り返し処理によって幅が広がってきました", Timestamp.valueOf("2022-05-12 00:00:00"), 1));
					expectedList.add(new ArticleBean(11, "研修開始3", "これから技術を習得してエンジニアを目指します！", Timestamp.valueOf("2022-06-01 00:00:00"), 1));
					expectedList.add(new ArticleBean(12, "IT 基礎3", "インターネットの仕組みとデータの流れを勉強しました", Timestamp.valueOf("2022-06-02 00:00:00"), 1));
					expectedList.add(new ArticleBean(13, "プログラミング3", "条件分岐や繰り返し処理によって幅が広がってきました", Timestamp.valueOf("2022-06-12 00:00:00"), 1));
					expectedList.add(new ArticleBean(16, "研修開始4", "これから技術を習得してエンジニアを目指します！", Timestamp.valueOf("2022-07-01 00:00:00"), 1));
					expectedList.add(new ArticleBean(17, "IT 基礎4", "インターネットの仕組みとデータの流れを勉強しました", Timestamp.valueOf("2022-07-02 00:00:00"), 1));
					expectedList.add(new ArticleBean(18, "プログラミング4", "条件分岐や繰り返し処理によって幅が広がってきました", Timestamp.valueOf("2022-07-12 00:00:00"), 1));
					expected.add(expectedList);
					// [2] 未登録ユーザ（ユーザID「-1」）が投稿した記事は空リストが返される
					user = -1;
					users.add(user);
					expectedList = new ArrayList<ArticleBean>();
					expected.add(expectedList);
					// テストパラメータを返却
					return Stream.of(
							Arguments.of(users.get(0), expected.get(0)),
							Arguments.of(users.get(1), expected.get(1))
							);
				}
				
			}
			
		}
		
	}
	
}
