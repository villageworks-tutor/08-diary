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
class ArticleDAOTest_ extends BaseDaoTest {
	
	// データセットとして読み込むXMLファイルのパス
	protected static final String FIXTURES_XML_00  = DIR_FIXTURES + "記事_復元.xml";
	protected static final String FIXTURES_XML_01  = DIR_FIXTURES + "記事_登録数２０.xml";
	
	@Nested
	@DisplayName("ArticleDAO検索系メソッドのテストクラス")
	@TestInstance(Lifecycle.PER_CLASS)
	class SearchMethodsTests {
		
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
			dataset = new FlatXmlDataSetBuilder().build(new FileInputStream(FIXTURES_XML_01));
			// データの復元
			DatabaseOperation.CLEAN_INSERT.execute(dbUnitConnection, dataset);
		}
		
		@Nested
		@DisplayName("ArticleDAO#findLikeKeywordWithPagination(String, int, int, int)メソッドのテストケース")
		class FindLikeKeywordWithPaginationTest {
			// ページあたりの表示件数
			private static final int LIMIT_PER_PAGE = 3;
			
			@ParameterizedTest
			@MethodSource("test02Provider")
			@DisplayName("【Test-02】ログインユーザが投稿した記事のうちキーワード検索結果の記事を表示ページごとに取得できる（CriteriaBeanによる指定）")
			void test02(CriteriaBean criteria, List<ArticleBean> expectedList) throws Exception {
				// execute
				List<ArticleBean> actualList = sut.findLikeKeywordAndUserIdWithPagination(criteria);
				// verify
				for (int i = 0; i < expectedList.size(); i++) {
					ArticleBean actual = actualList.get(i);
					ArticleBean expected = expectedList.get(i);
					assertThat(actual, is(new EqualToArticle(expected)));
				}
			}
			
			@ParameterizedTest
			@MethodSource("test01Provider")
			@DisplayName("【Test-01】ログインユーザが投稿した記事のうちキーワード検索結果の指定したページの記事を取得できる")
			void test01(int userId, String keyword, int page, List<ArticleBean> expectedList) throws Exception {
				// execute
				List<ArticleBean> actualList = sut.findLikeKeywordAndUserIdWithPagination(keyword, LIMIT_PER_PAGE, page, userId);
				// verify
				ArticleBean actual = null;
				ArticleBean expected = null;
				for (int i = 0; i < expectedList.size(); i++) {
					actual = actualList.get(i);
					expected = expectedList.get(i);
					assertThat(actual, is(new EqualToArticle(expected)));
				}
				
			}
			
			static Stream<Arguments> test02Provider() {
				// setup
				String keyword = null;
				int limits     = LIMIT_PER_PAGE;
				int page       = -1;
				int userId     = -1;
				CriteriaBean criteria = null;
				List<CriteriaBean> criterias = new ArrayList<>();
				List<ArticleBean> expectedList = new ArrayList<ArticleBean>();
				List<List<ArticleBean>> expected = new ArrayList<List<ArticleBean>>();
				// [1] ユーザID「1」のユーザが「データの流れ」でのキーワード検索結果の先頭ページの記事を取得するテスト
				keyword = "データの流れ";
				userId = 1;
				page = 1;
				criteria = new CriteriaBean(keyword, userId, limits, page);
				criterias.add(criteria);
				// 期待値の設定
				expectedList = new ArrayList<ArticleBean>();
				expectedList.add(new ArticleBean(2, "IT 基礎1", "インターネットの仕組みとデータの流れを勉強しました", Timestamp.valueOf("2022-04-02 00:00:00"), 1));
				expectedList.add(new ArticleBean(7, "IT 基礎2", "インターネットの仕組みとデータの流れを勉強しました", Timestamp.valueOf("2022-05-02 00:00:00"), 1));
				expectedList.add(new ArticleBean(12, "IT 基礎3", "インターネットの仕組みとデータの流れを勉強しました", Timestamp.valueOf("2022-06-02 00:00:00"), 1));
				expected.add(expectedList);
				// [2] ユーザID「1」のユーザが「データの流れ」でのキーワード検索結果の最終ページの記事を取得するテスト
				keyword = "データの流れ";
				userId  = 1;
				page    = 2;
				criteria = new CriteriaBean(keyword, userId, limits, page);
				criterias.add(criteria);
				// 期待値の設定
				expectedList = new ArrayList<ArticleBean>();
				expectedList.add(new ArticleBean(17, "IT 基礎4", "インターネットの仕組みとデータの流れを勉強しました", Timestamp.valueOf("2022-07-02 00:00:00"), 1));
				expected.add(expectedList);
				// [3] ユーザID「2」のユーザが「！」でのキーワード検索すると記事を取得できないテスト
				keyword = "！";
				userId  = 2;
				page    = 1;
				criteria = new CriteriaBean(keyword, userId, limits, page);
				criterias.add(criteria);
				// 期待値の設定
				expectedList = new ArrayList<ArticleBean>();
				expected.add(expectedList);
				// [4] ユーザID「2」のユーザがキーワードを未入力で検索するとすべての記事を取得する
				keyword = "";
				userId  = 2;
				page    = 1;
				criteria = new CriteriaBean(keyword, userId, limits, page);
				criterias.add(criteria);
				// 期待値の設定
				expectedList = new ArrayList<ArticleBean>();
				expectedList.add(new ArticleBean(4, "ペットの紹介1", "猫を 2 匹飼っています", Timestamp.valueOf("2022-04-01 00:00:00"), 2));
				expectedList.add(new ArticleBean(5, "今日の天気1", "突然のにわか雨でした", Timestamp.valueOf("2022-04-02 00:00:00"), 2));
				expectedList.add(new ArticleBean(9, "ペットの紹介2", "猫を 2 匹飼っています", Timestamp.valueOf("2022-05-01 00:00:00"), 2));
//				expectedList.add(new ArticleBean(10, "今日の天気2", "突然のにわか雨でした", Timestamp.valueOf("2022-05-02 00:00:00"), 2));
//				expectedList.add(new ArticleBean(14, "ペットの紹介3", "猫を 2 匹飼っています", Timestamp.valueOf("2022-06-01 00:00:00"), 2));
//				expectedList.add(new ArticleBean(15, "今日の天気3", "突然のにわか雨でした", Timestamp.valueOf("2022-06-02 00:00:00"), 2));
//				expectedList.add(new ArticleBean(19, "ペットの紹介4", "猫を 2 匹飼っています", Timestamp.valueOf("2022-07-01 00:00:00"), 2));
//				expectedList.add(new ArticleBean(20, "今日の天気4", "突然のにわか雨でした", Timestamp.valueOf("2022-07-02 00:00:00"), 2));
				expected.add(expectedList);
				// テストパラメータを返却
				return
						Stream.of(
							Arguments.of(criterias.get(0), expected.get(0)),
							Arguments.of(criterias.get(1), expected.get(1)),
							Arguments.of(criterias.get(2), expected.get(2)),
							Arguments.of(criterias.get(3), expected.get(3))
						);
			}
			
			static Stream<Arguments> test01Provider() {
				// setup
				List<String> keywords = new ArrayList<String>();
				List<Integer> users = new ArrayList<Integer>();
				List<Integer> pages = new ArrayList<Integer>();
				List<ArticleBean> expectedList = new ArrayList<ArticleBean>();
				List<List<ArticleBean>> expected = new ArrayList<List<ArticleBean>>();
				// [1] ユーザID「1」のユーザが「データの流れ」でのキーワード検索結果の先頭ページの記事を取得するテスト
				keywords.add("データの流れ");
				users.add(1);
				pages.add(1);
				// 期待値の設定
				expectedList = new ArrayList<ArticleBean>();
				expectedList.add(new ArticleBean(2, "IT 基礎1", "インターネットの仕組みとデータの流れを勉強しました", Timestamp.valueOf("2022-04-02 00:00:00"), 1));
				expectedList.add(new ArticleBean(7, "IT 基礎2", "インターネットの仕組みとデータの流れを勉強しました", Timestamp.valueOf("2022-05-02 00:00:00"), 1));
				expectedList.add(new ArticleBean(12, "IT 基礎3", "インターネットの仕組みとデータの流れを勉強しました", Timestamp.valueOf("2022-06-02 00:00:00"), 1));
				expected.add(expectedList);
				// [2] ユーザID「1」のユーザが「データの流れ」でのキーワード検索結果の最終ページの記事を取得するテスト
				keywords.add("データの流れ");
				users.add(1);
				pages.add(2);
				// 期待値の設定
				expectedList = new ArrayList<ArticleBean>();
				expectedList.add(new ArticleBean(17, "IT 基礎4", "インターネットの仕組みとデータの流れを勉強しました", Timestamp.valueOf("2022-07-02 00:00:00"), 1));
				expected.add(expectedList);
				// [3] ユーザID「2」のユーザが「！」でのキーワード検索すると記事を取得できないテスト
				keywords.add("！");
				users.add(2);
				pages.add(1);
				// 期待値の設定
				expectedList = new ArrayList<ArticleBean>();
				expected.add(expectedList);
				// テストパラメータを返却
				return
						Stream.of(
							Arguments.of(users.get(0), keywords.get(0), pages.get(0), expected.get(0)),
							Arguments.of(users.get(1), keywords.get(1), pages.get(1), expected.get(1)),
							Arguments.of(users.get(2), keywords.get(2), pages.get(2), expected.get(2))
						);
			}
			
		}
		
		@Nested
		@DisplayName("ArticleDAO#findByPagination(int, int, int)メソッドのテストケース")
		class FindByPaginationTest {

			// ページあたりの表示件数
			private int limits = 5;
			
			@Test
			@DisplayName("【Test-04】ユーザID「1」のユーザが投稿した記事の99ページに表示される記事は0件である")
			void tset04() throws Exception {
				// setup
				int page = 99;
				int userId = 1;
				int expected = 0;
				// execute
				List<ArticleBean> list = sut.findByPaging(limits, page, userId);
				int actual = list.size();
				// verify
				assertThat(actual, is(expected));
			}
			
			@Test
			@DisplayName("【Test-03】ユーザID「2」のユーザが投稿した記事の最終ページに表示される記事は3件である")
			void tset03() throws Exception {
				// setup
				int page = 2;
				int userId = 2;
				int expected = 3;
				// execute
				List<ArticleBean> list = sut.findByPaging(limits, page, userId);
				int actual = list.size();
				// verify
				assertThat(actual, is(expected));
			}
			
			@Test
			@DisplayName("【Test-02】ユーザID「1」のユーザが投稿した記事の2ページ目の末尾の記事の記事番号は16である")
			void test02() throws Exception {
				// setup
				int page = 2;
				int userId = 1;
				int expected = 16;
				// execute
				List<ArticleBean> list = sut.findByPaging(limits, page, userId);
				int actual = list.get(list.size() - 1).getId();
				// verify
				assertThat(actual, is(expected));
			}
			
			@Test
			@DisplayName("【Test-01】ユーザID「1」のユーザが投稿した記事の1ページ目を取得できる")
			void test01() throws Exception {
				// setup
				int page = 1;
				int userId = 1;
				List<ArticleBean> expectedList = new ArrayList<ArticleBean>();
				expectedList.add(new ArticleBean(1, "研修開始1", "これから技術を習得してエンジニアを目指します！", Timestamp.valueOf("2022-04-01 00:00:00"), 1));
				expectedList.add(new ArticleBean(2, "IT 基礎1", "インターネットの仕組みとデータの流れを勉強しました", Timestamp.valueOf("2022-04-02 00:00:00"), 1));
				expectedList.add(new ArticleBean(3, "プログラミング1", "条件分岐や繰り返し処理によって幅が広がってきました", Timestamp.valueOf("2022-04-12 00:00:00"), 1));
				expectedList.add(new ArticleBean(6, "研修開始2", "これから技術を習得してエンジニアを目指します！", Timestamp.valueOf("2022-05-01 00:00:00"), 1));
				expectedList.add(new ArticleBean(7, "IT 基礎2", "インターネットの仕組みとデータの流れを勉強しました", Timestamp.valueOf("2022-05-02 00:00:00"), 1));
				// execute
				List<ArticleBean> actualList = sut.findByPaging(limits, page, userId);
				// verify
				if (actualList.size() > 0) {
					ArticleBean actual = null;
					ArticleBean expected = null;
					for(int i = 0; i < expectedList.size(); i++) {
						actual = actualList.get(i);
						expected = expectedList.get(i);
						assertThat(actual, is(new EqualToArticle(expected)));
					}
				} else {
					fail("未実装です");
				}
			}
		}
		
		@Nested
		@DisplayName("ArticleDAO#findLikeKeyword(Strinng, int)メソッドのテストケース")
		class FindLikeKeywordTest {
			@Test
			@DisplayName("【Test-05】ユーザID「2」のユーザがキーワードを指定しない場合はユーザID「2」のユーザが投稿したすべての記事を取得する")
			void test05() throws Exception {
				// setup
				int userId = 2;
				String keyword  = "";
				CriteriaBean target = new CriteriaBean(keyword, userId);
				List<ArticleBean> expectedList = new ArrayList<ArticleBean>();
				expectedList.add(new ArticleBean(4, "ペットの紹介1", "猫を 2 匹飼っています", Timestamp.valueOf("2022-04-01 00:00:00"), 2));
				expectedList.add(new ArticleBean(5, "今日の天気1", "突然のにわか雨でした", Timestamp.valueOf("2022-04-02 00:00:00"), 2));
				expectedList.add(new ArticleBean(9, "ペットの紹介2", "猫を 2 匹飼っています", Timestamp.valueOf("2022-05-01 00:00:00"), 2));
				expectedList.add(new ArticleBean(10, "今日の天気2", "突然のにわか雨でした", Timestamp.valueOf("2022-05-02 00:00:00"), 2));
				expectedList.add(new ArticleBean(14, "ペットの紹介3", "猫を 2 匹飼っています", Timestamp.valueOf("2022-06-01 00:00:00"), 2));
				expectedList.add(new ArticleBean(15, "今日の天気3", "突然のにわか雨でした", Timestamp.valueOf("2022-06-02 00:00:00"), 2));
				expectedList.add(new ArticleBean(19, "ペットの紹介4", "猫を 2 匹飼っています", Timestamp.valueOf("2022-07-01 00:00:00"), 2));
				expectedList.add(new ArticleBean(20, "今日の天気4", "突然のにわか雨でした", Timestamp.valueOf("2022-07-02 00:00:00"), 2));
				// execute
				List<ArticleBean> actualList = sut.findLikeKeywordAndUserId(target);
				// verify
				if (actualList.size() > 0) {
					ArticleBean actual = null;
					ArticleBean expected = null;
					for (int i = 0; i < expectedList.size(); i++) {
						actual = actualList.get(i);
						expected = expectedList.get(i);
						assertThat(actual, is(new EqualToArticle(expected)));
					}
					
				} else {
					fail("未実装です");
				}
				
			}
			
			@Test
			@DisplayName("【Test-04】ユーザID「2」のユーザが投稿した記事にはキーワード「JDBC」を含む記事は存在しない")
			void test04() throws Exception {
				// setup
				CriteriaBean target = new CriteriaBean("JDBC", 1);
				int expected = 0;
				// execute
				int actual = sut.findLikeKeywordAndUserId(target).size();
				// verify
				assertThat(actual, is(expected));
			}
			
			@Test
			@DisplayName("【Test-03】ユーザID「1」のユーザが投稿した記事のうちキーワード「！」で終わる記事を取得できる")
			void tes03() throws Exception {
				// setup
				int id = 1;
				String keyword = "！";
				List<ArticleBean> expectedList = new ArrayList<ArticleBean>();
				expectedList.add(new ArticleBean(1, "研修開始1", "これから技術を習得してエンジニアを目指します！", Timestamp.valueOf("2022-04-01 00:00:00"), 1));
				expectedList.add(new ArticleBean(6, "研修開始2", "これから技術を習得してエンジニアを目指します！", Timestamp.valueOf("2022-05-01 00:00:00"), 1));
				expectedList.add(new ArticleBean(11, "研修開始3", "これから技術を習得してエンジニアを目指します！", Timestamp.valueOf("2022-06-01 00:00:00"), 1));
				expectedList.add(new ArticleBean(16, "研修開始4", "これから技術を習得してエンジニアを目指します！", Timestamp.valueOf("2022-07-01 00:00:00"), 1));
				// execute
				List<ArticleBean> actualList = sut.findLikeKeywordAndUserId(keyword, id);
				// verify
				if (actualList.size() > 0) {
					ArticleBean actual = null;
					ArticleBean expected = null;
					for (int i = 0; i < expectedList.size(); i++) {
						actual = actualList.get(i);
						expected = expectedList.get(i);
						assertThat(actual, is(new EqualToArticle(expected)));
					}
					
				} else {
					fail("未実装です");
				}
				
			}
			
			@Test
			@DisplayName("【Test-02】ユーザID「2」のユーザが投稿した記事のうちキーワード「にわか雨」を含む記事を取得できる（SearchBeanを利用するテストケース）")
			void test02() throws Exception {
				// setup
				CriteriaBean target = new CriteriaBean("にわか雨", 2);
				List<ArticleBean> expectedList = new ArrayList<ArticleBean>();
				expectedList.add(new ArticleBean(5, "今日の天気1", "突然のにわか雨でした", Timestamp.valueOf("2022-04-02 00:00:00"), 2));
				expectedList.add(new ArticleBean(10, "今日の天気2", "突然のにわか雨でした", Timestamp.valueOf("2022-05-02 00:00:00"), 2));
				expectedList.add(new ArticleBean(15, "今日の天気3", "突然のにわか雨でした", Timestamp.valueOf("2022-06-02 00:00:00"), 2));
				expectedList.add(new ArticleBean(20, "今日の天気4", "突然のにわか雨でした", Timestamp.valueOf("2022-07-02 00:00:00"), 2));
				// excute
				List<ArticleBean> actualList = sut.findLikeKeywordAndUserId(target);
				// verify
				if (actualList.size() > 0) {
					ArticleBean actual = null;
					ArticleBean expected = null;
					for (int i = 0; i < expectedList.size(); i++) {
						actual = actualList.get(i);
						expected = expectedList.get(i);
						assertThat(actual, is(new EqualToArticle(expected)));
					}
					
				} else {
					fail("未実装です");
				}
				
			}
			
			@Test
			@DisplayName("【Test-01】ユーザID「1」のユーザが投稿した記事のうちキーワード「プログラミング」で始まる記事を取得できる")
			void test01() throws Exception {
				// setup
				int userId = 1;
				String keyword = "プログラミング";
				List<ArticleBean> expectedList = new ArrayList<ArticleBean>();
				expectedList.add(new ArticleBean(3, "プログラミング1", "条件分岐や繰り返し処理によって幅が広がってきました", Timestamp.valueOf("2022-04-12 00:00:00"), 1));
				expectedList.add(new ArticleBean(8, "プログラミング2", "条件分岐や繰り返し処理によって幅が広がってきました", Timestamp.valueOf("2022-05-12 00:00:00"), 1));
				expectedList.add(new ArticleBean(13, "プログラミング3", "条件分岐や繰り返し処理によって幅が広がってきました", Timestamp.valueOf("2022-06-12 00:00:00"), 1));
				expectedList.add(new ArticleBean(18, "プログラミング4", "条件分岐や繰り返し処理によって幅が広がってきました", Timestamp.valueOf("2022-07-12 00:00:00"), 1));
				// execute
				List<ArticleBean> actualList = sut.findLikeKeywordAndUserId(keyword, userId);
				// verify
				if (actualList.size() > 0) {
					ArticleBean actual = null;
					ArticleBean expected = null;
					for (int i = 0; i < expectedList.size(); i++) {
						actual = actualList.get(i);
						expected = expectedList.get(i);
						assertThat(actual, is(new EqualToArticle(expected)));
					}
					
				} else {
					fail("未実装です");
				}
				
			}
			
		}
		
		@Nested
		@DisplayName("ArticleDAO#findAllByUserIdWithPagination(CriteriaBean)メソッドのテストクラス")
		class FindByUserIdWithPaginatinTest {
			
			private static final int LIMIT_PER_PAGE = 3;
			
			@ParameterizedTest
			@MethodSource("findAllByUserIdWithPaginationProvider")
			@DisplayName("【Test-01：ユーザID別の全件検索】ログインユーザが投稿したすべての記事を表示ページごとに取得するテスト（CriteriaBeanによる指定）")
			void tetst01(CriteriaBean criteria, List<ArticleBean> expectedList) throws Exception {
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
			
			static Stream<Arguments> finndAllByUserIdWithPaginationProvider() {
				// setup
				int userId = -1;
				int limits = -1;
				int page   = -1;
				List<CriteriaBean> criteria = new ArrayList<CriteriaBean>();
				List<ArticleBean> expectedList = null;
				List<List<ArticleBean>> expected = new ArrayList<List<ArticleBean>>();
				// [1] ユーザID「1」のユーザが投稿したすべての記事ページのうち先頭ページの記事を取得するテスト
				userId = 1;
				limits = LIMIT_PER_PAGE;
				page   = 1;
				criteria.add(new CriteriaBean(userId, limits, page));
				expectedList = new ArrayList<ArticleBean>();
				expectedList.add(new ArticleBean(1, "研修開始1", "これから技術を習得してエンジニアを目指します！", Timestamp.valueOf("2022-04-01 00:00:00"), 1));
				expectedList.add(new ArticleBean(2, "IT 基礎1", "インターネットの仕組みとデータの流れを勉強しました", Timestamp.valueOf("2022-04-02 00:00:00"), 1));
				expectedList.add(new ArticleBean(3, "プログラミング1", "条件分岐や繰り返し処理によって幅が広がってきました", Timestamp.valueOf("2022-04-12 00:00:00"), 1));
				expectedList.add(new ArticleBean(6, "研修開始2", "これから技術を習得してエンジニアを目指します！", Timestamp.valueOf("2022-05-01 00:00:00"), 1));
				expectedList.add(new ArticleBean(7, "IT 基礎2", "インターネットの仕組みとデータの流れを勉強しました", Timestamp.valueOf("2022-05-02 00:00:00"), 1));
				expected.add(expectedList);
				// [2] ユーザID「1」のユーザが投稿したすべての記事ページのうち2ページ目の記事を取得するテスト
				userId = 1;
				limits = LIMIT_PER_PAGE;
				page   = ２;
				criteria.add(new CriteriaBean(userId, limits, page));
				expectedList = new ArrayList<ArticleBean>();
				expectedList.add(new ArticleBean(8, "プログラミング2", "条件分岐や繰り返し処理によって幅が広がってきました", Timestamp.valueOf("2022-05-12 00:00:00"), 1));
				expectedList.add(new ArticleBean(11, "研修開始3", "これから技術を習得してエンジニアを目指します！", Timestamp.valueOf("2022-06-01 00:00:00"), 1));
				expectedList.add(new ArticleBean(12, "IT 基礎3", "インターネットの仕組みとデータの流れを勉強しました", Timestamp.valueOf("2022-06-02 00:00:00"), 1));
				expectedList.add(new ArticleBean(13, "プログラミング3", "条件分岐や繰り返し処理によって幅が広がってきました", Timestamp.valueOf("2022-06-12 00:00:00"), 1));
				expectedList.add(new ArticleBean(16, "研修開始4", "これから技術を習得してエンジニアを目指します！", Timestamp.valueOf("2022-07-01 00:00:00"), 1));
				expected.add(expectedList);
				// [3] ユーザID「2」のユーザが投稿したすべての記事ページの最終ページの記事を取得するテスト
				userId = 2;
				limits = LIMIT_PER_PAGE;
				page   = 2;
				criteria.add(new CriteriaBean(userId, limits, page));
				expectedList = new ArrayList<ArticleBean>();
				expectedList.add(new ArticleBean(15, "今日の天気3", "突然のにわか雨でした", Timestamp.valueOf("2022-06-02 00:00:00"), 2));
				expectedList.add(new ArticleBean(19, "ペットの紹介4", "猫を 2 匹飼っています", Timestamp.valueOf("2022-07-01 00:00:00"), 2));
				expectedList.add(new ArticleBean(20, "今日の天気4", "突然のにわか雨でした", Timestamp.valueOf("2022-07-02 00:00:00"), 2));
				expected.add(expectedList);
				// [4] ユーザID「2」のユーザが投稿したすべての記事ページの-1ページ目の記事を取得できないテスト
				userId = 2;
				limits = LIMIT_PER_PAGE;
				page   = -1;
				expectedList = new ArrayList<ArticleBean>();
				expected.add(expectedList);
				// テストパラメータを返却
				return Stream.of(
						Arguments.of(criteria.get(0), expected.get(0)),
						Arguments.of(criteria.get(1), expected.get(1)),
						Arguments.of(criteria.get(2), expected.get(2)),
						Arguments.of(criteria.get(3), expected.get(3))
						);
			}
		}
		
		@Nested
		@DisplayName("ArticleDAO#findByUserId(int)メソッドのテストクラス")
		class FindByUserIdTest {
			@ParameterizedTest
			@CsvSource({
				"2, 8", // ユーザID「2」のユーザの登録記事数は8件である
				"-1, 0" // 未登録ユーザの投稿記事数は0件である
				})
			@DisplayName("【Test-02】ユーザID「target」のユーザが投稿した記事の総数は expected 件である")
			void test02(int target, int expected) throws Exception {
				// execute
				List<ArticleBean> list = sut.findAllByUserId(target);
				int actual = list.size();
				// verify
				assertThat(actual, is(expected));
			}
			
			@Test
			@DisplayName("【Test-01】ユーザID「1」のユーザが投稿したすべての記事を取得できる")
			void test01() throws Exception {
				// setup
				int target = 1;
				List<ArticleBean> expectedList = new ArrayList<ArticleBean>();
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
				// execute
				List<ArticleBean> actualList = sut.findAllByUserId(target);
				// verify
				if (actualList.size() > 0) {
					ArticleBean actual = null;
					ArticleBean expected = null;
					for (int i = 0; i < expectedList.size(); i++) {
						actual = actualList.get(i);
						expected = expectedList.get(i);
						assertThat(actual, is(new EqualToArticle(expected)));
					}
					
				} else {
					fail("未実装です");
				}
				
			}
			
		}
	
	}
	
}
