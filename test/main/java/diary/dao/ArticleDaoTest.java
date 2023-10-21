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
	protected static final String FIXTURES_XML_00  = DIR_FIXTURES + "記事_復元.xml";
	protected static final String FIXTURES_XML_01  = DIR_FIXTURES + "記事_登録数２０.xml";
	
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
			dataset = new FlatXmlDataSetBuilder().build(new FileInputStream(FIXTURES_XML_01));
			// データの復元
			DatabaseOperation.CLEAN_INSERT.execute(dbUnitConnection, dataset);
		}
		
		@Nested
		@DisplayName("ArticleDAO#findLikeKeywordAndUserIdWithPagination(CriteriaBean)メソッドのテストクラス")
		class findLikeKeywordAndUserIdWithPaginationTest {
			// ページあたりの表示件数
			private static final int LIMIT_PER_PAGE = 3;
			
			@ParameterizedTest
			@MethodSource("findLikeKeywordAndUserIdWithPagination01Provider")
			@DisplayName("【Test-02：キーワード検索】CriteriaBeanによるタイトル検索と投稿内容検索")
			void testFindLikeKeywordAndUserIdWithPagination(CriteriaBean criteria, List<ArticleBean> expectedList) throws Exception {
				// execute
				List<ArticleBean> actualList = sut.findLikeKeywordAndUserIdWithPagination(criteria);
				// verify
				if (actualList.size() > 0) {
					for (int i = 0; i < expectedList.size(); i++) {
						ArticleBean actual = actualList.get(i);
						ArticleBean expected = expectedList.get(i);
						assertThat(actual, is(new EqualToArticle(expected)));
					}
				}
			}
			
			/**
			 * findLikeKeywordAndUserIdWithPagination(CriteriaBean)メソッドのテストパラメータを生成する
			 * @return
			 */
			static Stream<Arguments> findLikeKeywordAndUserIdWithPagination01Provider() {
				// setup
				String keyword = null;
				int userId = -1;
				int limits = -1;
				int page   = -1;
				List<CriteriaBean> criteria = new ArrayList<CriteriaBean>();
				List<ArticleBean> expectedList = null;
				List<List<ArticleBean>> expected = new ArrayList<List<ArticleBean>>();
				// [1] ユーザID「1」のユーザが投稿した記事のうちキーワード「プログラミング」で始まる記事のうち先頭ページ分を取得するテスト
				keyword = "プログラミング";
				userId = 1;
				limits = LIMIT_PER_PAGE;
				page = 1;
				criteria.add(new CriteriaBean(keyword, userId, limits, page));
				expectedList = new ArrayList<ArticleBean>();
				expectedList.add(new ArticleBean(3, "プログラミング1", "条件分岐や繰り返し処理によって幅が広がってきました", Timestamp.valueOf("2022-04-12 00:00:00"), 1));
				expectedList.add(new ArticleBean(8, "プログラミング2", "条件分岐や繰り返し処理によって幅が広がってきました", Timestamp.valueOf("2022-05-12 00:00:00"), 1));
				expectedList.add(new ArticleBean(13, "プログラミング3", "条件分岐や繰り返し処理によって幅が広がってきました", Timestamp.valueOf("2022-06-12 00:00:00"), 1));
				expected.add(expectedList);
				// [2] ユーザID「1」のユーザが投稿した記事のうちキーワード「ました」を含む記事のうち2ページ目に表示する記事を取得するテスト
				keyword = "ました";
				userId = １;
				limits = LIMIT_PER_PAGE;
				page = 2;
				criteria.add(new CriteriaBean(keyword, userId, limits, page));
				expectedList = new ArrayList<ArticleBean>();
				expectedList.add(new ArticleBean(8, "プログラミング2", "条件分岐や繰り返し処理によって幅が広がってきました", Timestamp.valueOf("2022-05-12 00:00:00"), 1));
				expectedList.add(new ArticleBean(12, "IT 基礎3", "インターネットの仕組みとデータの流れを勉強しました", Timestamp.valueOf("2022-06-02 00:00:00"), 1));
				expectedList.add(new ArticleBean(13, "プログラミング3", "条件分岐や繰り返し処理によって幅が広がってきました", Timestamp.valueOf("2022-06-12 00:00:00"), 1));
				expected.add(expectedList);
				// [3] ユーザID「2」のユーザが投稿した記事のうちキーワード「にわか雨」を含む記事のうち最終ページ分を取得するテスト
				keyword = "にわか雨";
				userId = 2;
				limits = LIMIT_PER_PAGE;
				page = 2;
				criteria.add(new CriteriaBean(keyword, userId, limits, page));
				expectedList = new ArrayList<ArticleBean>();
				expectedList.add(new ArticleBean(20, "今日の天気4", "突然のにわか雨でした", Timestamp.valueOf("2022-07-02 00:00:00"), 2));
				expected.add(expectedList);
				// テストパラメータを返却
				return Stream.of(
						Arguments.of(criteria.get(0), expected.get(0)),
						Arguments.of(criteria.get(1), expected.get(1))
						);
			}
		}
		
		@Nested
		@DisplayName("ArticleDAO#findLikeKeywordAndserId(CriteriaBean)メソッドのテストクラス")
		class findLikeKeywordAndUserIdWithCriteriaTest {
			@ParameterizedTest
			@MethodSource("findLikeKeywordAndUserId02Provider")
			@DisplayName("【Test-02：キーワード検索】CriteriaBeanによるタイトル検索と投稿内容検索")
			void testFindLikeKeywordAndUserId02(CriteriaBean criteria, List<ArticleBean> expectedList) throws Exception {
				// execute
				List<ArticleBean> actualList = sut.findLikeKeywordAndUserId(criteria);
				// verify
				if (actualList.size() > 0) {
					for (int i = 0; i < expectedList.size(); i++) {
						ArticleBean actual = actualList.get(i);
						ArticleBean expected = expectedList.get(i);
						assertThat(actual, is(new EqualToArticle(expected)));
					}
				}
			}
			
			/**
			 * findLikeKeywordAndUserId(CriteriaBean)メソッドのテストパラメータを生成する
			 * @return
			 */
			static Stream<Arguments> findLikeKeywordAndUserId02Provider() {
				// setup
				String keyword = null;
				int userId = -1;
				List<CriteriaBean> criteria = new ArrayList<>();
				List<ArticleBean> expectedList = null;
				List<List<ArticleBean>> expected = new ArrayList<List<ArticleBean>>();
				// [1] ユーザID「1」のユーザが投稿した記事のうちキーワード「プログラミング」で始まる記事を取得するテスト
				keyword = "プログラミング";
				userId = 1;
				criteria.add(new CriteriaBean(keyword, userId));
				expectedList = new ArrayList<ArticleBean>();
				expectedList.add(new ArticleBean(3, "プログラミング1", "条件分岐や繰り返し処理によって幅が広がってきました", Timestamp.valueOf("2022-04-12 00:00:00"), 1));
				expectedList.add(new ArticleBean(8, "プログラミング2", "条件分岐や繰り返し処理によって幅が広がってきました", Timestamp.valueOf("2022-05-12 00:00:00"), 1));
				expectedList.add(new ArticleBean(13, "プログラミング3", "条件分岐や繰り返し処理によって幅が広がってきました", Timestamp.valueOf("2022-06-12 00:00:00"), 1));
				expectedList.add(new ArticleBean(18, "プログラミング4", "条件分岐や繰り返し処理によって幅が広がってきました", Timestamp.valueOf("2022-07-12 00:00:00"), 1));
				expected.add(expectedList);
				// [2] ユーザID「1」のユーザが投稿した記事のうちキーワード「データの流れ」を含む記事を取得するテスト
				keyword = "データの流れ";
				userId = 1;
				criteria.add(new CriteriaBean(keyword, userId));
				expectedList = new ArrayList<ArticleBean>();
				expectedList.add(new ArticleBean(2, "IT 基礎1", "インターネットの仕組みとデータの流れを勉強しました", Timestamp.valueOf("2022-04-02 00:00:00"), 1));
				expectedList.add(new ArticleBean(7, "IT 基礎2", "インターネットの仕組みとデータの流れを勉強しました", Timestamp.valueOf("2022-05-02 00:00:00"), 1));
				expectedList.add(new ArticleBean(12, "IT 基礎3", "インターネットの仕組みとデータの流れを勉強しました", Timestamp.valueOf("2022-06-02 00:00:00"), 1));
				expectedList.add(new ArticleBean(17, "IT 基礎4", "インターネットの仕組みとデータの流れを勉強しました", Timestamp.valueOf("2022-07-02 00:00:00"), 1));
				expected.add(expectedList);
				// [3] ユーザID「1」のユーザが投稿した記事のうち文字列「！」で終わる記事を取得するテスト
				keyword = "！";
				userId = 1;
				criteria.add(new CriteriaBean(keyword, userId));
				expectedList = new ArrayList<ArticleBean>();
				expectedList.add(new ArticleBean(1, "研修開始1", "これから技術を習得してエンジニアを目指します！", Timestamp.valueOf("2022-04-01 00:00:00"), 1));
				expectedList.add(new ArticleBean(6, "研修開始2", "これから技術を習得してエンジニアを目指します！", Timestamp.valueOf("2022-05-01 00:00:00"), 1));
				expectedList.add(new ArticleBean(11, "研修開始3", "これから技術を習得してエンジニアを目指します！", Timestamp.valueOf("2022-06-01 00:00:00"), 1));
				expectedList.add(new ArticleBean(16, "研修開始4", "これから技術を習得してエンジニアを目指します！", Timestamp.valueOf("2022-07-01 00:00:00"), 1));
				expected.add(expectedList);
				// [4] ユーザID「2」のユーザが投稿した記事のうち空文字列「」で検索するとすべての記事を取得するテスト
				keyword = "";
				userId = 2;
				criteria.add(new CriteriaBean(keyword, userId));
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
				// [5] ユーザID「2」のユーザが投稿した記事のうちnullで検索するとすべての記事を取得するテスト
				keyword = null;
				userId = 2;
				criteria.add(new CriteriaBean(keyword, userId));
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
				// テストパラメータを返却
				return Stream.of(
						Arguments.of(criteria.get(0), expected.get(0)),
						Arguments.of(criteria.get(1), expected.get(1)),
						Arguments.of(criteria.get(2), expected.get(2)),
						Arguments.of(criteria.get(3), expected.get(3)),
						Arguments.of(criteria.get(4), expected.get(4))
						);
			}
			
		}
		
		@Nested
		@DisplayName("ArticleDAO#findLikeKeywordAndserId(String, int)メソッドのテストクラス")
		class findLikeKeywordAndUserIdTest {
				
			@ParameterizedTest
			@MethodSource("findLikeKeywordAndUserId01Provider")
			@DisplayName("【Test-01：キーワード検索】キーワードによるタイトル検索と投稿内容検索")
			void testFindLikeKeywordAndUserId01(String keyword, int userId, List<ArticleBean> expectedList) throws Exception {
				// execute
				List<ArticleBean> actualList = sut.findLikeKeywordAndUserId(keyword, userId);
				// verify
				if (actualList.size() > 0) {
					for (int i = 0; i < expectedList.size(); i++) {
						ArticleBean actual = actualList.get(i);
						ArticleBean expected = expectedList.get(i);
						assertThat(actual, is(new EqualToArticle(expected)));
					}
				}
			}
			
			/**
			 * findLikeKeywordAndUserIdメソッドのテストパラメータを生成する
			 * @return
			 */
			static Stream<Arguments> findLikeKeywordAndUserId01Provider() {
				// setup
				List<String> keywords = new ArrayList<String>();
				List<Integer> users = new ArrayList<Integer>();
				List<ArticleBean> expectedList = null;
				List<List<ArticleBean>> expected = new ArrayList<List<ArticleBean>>();
				// [1] ユーザID「1」のユーザが投稿した記事のうちキーワード「プログラミング」で始まる記事を取得するテスト
				keywords.add("プログラミング");
				users.add(1);
				expectedList = new ArrayList<ArticleBean>();
				expectedList.add(new ArticleBean(3, "プログラミング1", "条件分岐や繰り返し処理によって幅が広がってきました", Timestamp.valueOf("2022-04-12 00:00:00"), 1));
				expectedList.add(new ArticleBean(8, "プログラミング2", "条件分岐や繰り返し処理によって幅が広がってきました", Timestamp.valueOf("2022-05-12 00:00:00"), 1));
				expectedList.add(new ArticleBean(13, "プログラミング3", "条件分岐や繰り返し処理によって幅が広がってきました", Timestamp.valueOf("2022-06-12 00:00:00"), 1));
				expectedList.add(new ArticleBean(18, "プログラミング4", "条件分岐や繰り返し処理によって幅が広がってきました", Timestamp.valueOf("2022-07-12 00:00:00"), 1));
				expected.add(expectedList);
				// [2] ユーザID「1」のユーザが投稿した記事のうちキーワード「データの流れ」を含む記事を取得するテスト
				keywords.add("データの流れ");
				users.add(1);
				expectedList = new ArrayList<ArticleBean>();
				expectedList.add(new ArticleBean(2, "IT 基礎1", "インターネットの仕組みとデータの流れを勉強しました", Timestamp.valueOf("2022-04-02 00:00:00"), 1));
				expectedList.add(new ArticleBean(7, "IT 基礎2", "インターネットの仕組みとデータの流れを勉強しました", Timestamp.valueOf("2022-05-02 00:00:00"), 1));
				expectedList.add(new ArticleBean(12, "IT 基礎3", "インターネットの仕組みとデータの流れを勉強しました", Timestamp.valueOf("2022-06-02 00:00:00"), 1));
				expectedList.add(new ArticleBean(17, "IT 基礎4", "インターネットの仕組みとデータの流れを勉強しました", Timestamp.valueOf("2022-07-02 00:00:00"), 1));
				expected.add(expectedList);
				// [3] ユーザID「1」のユーザが投稿した記事のうち文字列「！」で終わる記事を取得するテスト
				keywords.add("！");
				users.add(1);
				expectedList = new ArrayList<ArticleBean>();
				expectedList.add(new ArticleBean(1, "研修開始1", "これから技術を習得してエンジニアを目指します！", Timestamp.valueOf("2022-04-01 00:00:00"), 1));
				expectedList.add(new ArticleBean(6, "研修開始2", "これから技術を習得してエンジニアを目指します！", Timestamp.valueOf("2022-05-01 00:00:00"), 1));
				expectedList.add(new ArticleBean(11, "研修開始3", "これから技術を習得してエンジニアを目指します！", Timestamp.valueOf("2022-06-01 00:00:00"), 1));
				expectedList.add(new ArticleBean(16, "研修開始4", "これから技術を習得してエンジニアを目指します！", Timestamp.valueOf("2022-07-01 00:00:00"), 1));
				expected.add(expectedList);
				// [4] ユーザID「2」のユーザが投稿した記事のうち空文字列「」で検索するとすべての記事を取得するテスト
				keywords.add("");
				users.add(2);
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
				// [5] ユーザID「2」のユーザが投稿した記事のうちnullで検索するとすべての記事を取得するテスト
				keywords.add(null);
				users.add(2);
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
				// テストパラメータを返却
				return Stream.of(
						Arguments.of(keywords.get(0), users.get(0), expected.get(0)),
						Arguments.of(keywords.get(1), users.get(1), expected.get(1)),
						Arguments.of(keywords.get(2), users.get(2), expected.get(2)),
						Arguments.of(keywords.get(3), users.get(3), expected.get(3)),
						Arguments.of(keywords.get(4), users.get(4), expected.get(4))
						);
			}
		}
				
		@Nested
		@DisplayName("ArticleDAO#findAllByUserId(int)メソッドのテストクラス")
		class findAllByUserIdTest {
			@ParameterizedTest
			@CsvSource({
				  " 2, 8" // ユーザID「2」のユーザの登録記事数は8件である
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
			@MethodSource("findAllByUserId01Provider")
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
			 * findAllByUserId01テスト用のテストパラメータを生成する
			 * @return
			 */
			static Stream<Arguments> findAllByUserId01Provider() {
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
				// [2] 未登録ユーザ（ユーザID「-1」のユーザ）が投稿した記事は取得できない
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
