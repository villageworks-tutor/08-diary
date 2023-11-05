package diary.dao;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class SqlCreatorTest {

	private static final String SQL_FIND_FROM_ARTICLE   = "SELECT * FROM article";
	private static final String SQL_COUNT_FROM_ARTICLE  = "SELECT count(*) FROM article";
	private static final String SQL_DELETE_FROM_ARTICLE = "DELETE FROM article";
	
	private static final String WHERE_ARTICLE_ID        = "WHERE article_id = ?";
	private static final String WHERE_USER_ID           = "WHERE user_id = ?";
	private static final String WHERE_LIKE_KEYWORD      = "AND (title LIKE ? OR content LIKE ?)";
	private static final String ORDER_BY_ARICLE         = "ORDER BY article_id";
	private static final String LIMIT_OFFSET            = "LIMIT ? OFFSET ?";
	
	private static final String SQL_FIND_BY_ARTICLE_ID  = "SELECT * FROM article WHERE article_id = ?";
	private static final String SQL_FIND_BY_USER_ID     = "SELECT * FROM article WHERE user_id = ? ORDER BY article_id";
	private static final String SQL_FIND_BY_USER_ID_AND_LIKE_KEYWORD_WITH_PAGINATION 
	        = "SELECT * FROM article WHERE user_id = ? AND (title LIKE ? OR content LIKE ?) ORDER BY article_id LIMIT ? OFFSET ?";
	private static final String SQL_COUNT_BY_USER_ID_AND_LIKE_KEYWORD 
	        = "SELECT count(*) FROM article WHERE user_id = ? AND (title LIKE ? OR content LIKE ?)";
	private static final String SQL_FIND_BY_USER_ID_WITH_PAGINATION = "SELECT * FROM article WHERE user_id = ? LIMIT ? OFFSET ?";
	private static final String SQL_COUNT_ALL_FROM__ARTICLE_BY_USER_ID = "SELECT count(*) FROM article WHERE user_id = ?";
	
	@SuppressWarnings("unused")
	private static final String SQL_INSERT_ARTICLE_WITH_PLACEHOLDER 
	        = "INSERT INTO article (title, content, created_at, user_id) VALUES (?, ?, current_timestamp, ?)";
	@SuppressWarnings("unused")
	private static final String SQL_UPDATE_ARTICLE_WITH_PLACEHOLDER 
	        = "UPDATE article SET title = ?, content = ?, created_at = current_timestamp WHERE article_id = ?";
	private static final String SQL_DELETE_BY_ARTICLE_ID_WITH_PLACEHOLDER 
	        = "DELETE FROM article WHERE article_id = ?";
	private static final String SQL_DELETE_BY_USER_ID_WITH_PLACEHOLDER 
	        = "DELETE FROM article WHERE user_id = ?";
	
	private static final String CHAR_EMPTY = " ";
	
	@Nested
	@DisplayName("検索系SQLの生成")
	class FindMethodTest {
		@ParameterizedTest
		@MethodSource("paramsProvider")
		@DisplayName("記事番号を元に記事を検索する")
		void test01(String actual, String expected) {
			// verify
			assertThat(actual, is(expected));
		}
		
		static Stream<Arguments> paramsProvider() {
			// setup
			StringBuilder builder = null;
			List<String> actual = new ArrayList<String>();
			List<String> expected = new ArrayList<String>();
			// [1] 記事番号を元に記事を検索する
			expected.add(SQL_FIND_BY_ARTICLE_ID);
			builder = new StringBuilder();
			builder.append(SQL_FIND_FROM_ARTICLE).append(CHAR_EMPTY);
			builder.append(WHERE_ARTICLE_ID);
			actual.add(builder.toString());
			// [2] ユーザIDをもとに記事を検索する
			expected.add(SQL_FIND_BY_USER_ID);
			builder = new StringBuilder();
			builder.append(SQL_FIND_FROM_ARTICLE).append(CHAR_EMPTY);
			builder.append(WHERE_USER_ID).append(CHAR_EMPTY);
			builder.append(ORDER_BY_ARICLE);
			actual.add(builder.toString());
			// [3] ユーザIDをもとに記事をキーワードで検索する
			expected.add(SQL_FIND_BY_USER_ID_AND_LIKE_KEYWORD_WITH_PAGINATION);
			builder = new StringBuilder();
			builder.append(SQL_FIND_FROM_ARTICLE).append(CHAR_EMPTY);
			builder.append(WHERE_USER_ID).append(CHAR_EMPTY);
			builder.append(WHERE_LIKE_KEYWORD).append(CHAR_EMPTY);
			builder.append(ORDER_BY_ARICLE).append(CHAR_EMPTY);
			builder.append(LIMIT_OFFSET);
			actual.add(builder.toString());
			// [4] ユーザIDをもとに記事をキーワード検索した結果記事の件数をカウントする
			expected.add(SQL_COUNT_BY_USER_ID_AND_LIKE_KEYWORD);
			builder = new StringBuilder();
			builder.append(SQL_COUNT_FROM_ARTICLE).append(CHAR_EMPTY);
			builder.append(WHERE_USER_ID).append(CHAR_EMPTY);
			builder.append(WHERE_LIKE_KEYWORD);
			actual.add(builder.toString());
			// [5] 記事番号をもとに記事を削除する
			expected.add(SQL_DELETE_BY_ARTICLE_ID_WITH_PLACEHOLDER);
			builder = new StringBuilder();
			builder.append(SQL_DELETE_FROM_ARTICLE).append(CHAR_EMPTY);
			builder.append(WHERE_ARTICLE_ID);
			actual.add(builder.toString());
			// [6] 記事番号をもとに記事を削除する
			expected.add(SQL_DELETE_BY_USER_ID_WITH_PLACEHOLDER);
			builder = new StringBuilder();
			builder.append(SQL_DELETE_FROM_ARTICLE).append(CHAR_EMPTY);
			builder.append(WHERE_USER_ID);
			actual.add(builder.toString());
			// [7] ユーザIDごとにページあたりに表示する件数分だけの記事を取得する
			expected.add(SQL_FIND_BY_USER_ID_WITH_PAGINATION);
			builder = new StringBuilder();
			builder.append(SQL_FIND_FROM_ARTICLE).append(CHAR_EMPTY);
			builder.append(WHERE_USER_ID).append(CHAR_EMPTY);
			builder.append(LIMIT_OFFSET);
			actual.add(builder.toString());
			// [8] ユーザIDごとに記事の総件数を取得する
			expected.add(SQL_COUNT_ALL_FROM__ARTICLE_BY_USER_ID);
			builder = new StringBuilder();
			builder.append(SQL_COUNT_FROM_ARTICLE).append(CHAR_EMPTY);
			builder.append(WHERE_USER_ID);
			actual.add(builder.toString());
			
			// パラメータの返却
			return Stream.of(
					  Arguments.of(actual.get(0), expected.get(0))
					, Arguments.of(actual.get(1), expected.get(1))
					, Arguments.of(actual.get(2), expected.get(2))
					, Arguments.of(actual.get(3), expected.get(3))
					, Arguments.of(actual.get(4), expected.get(4))
					, Arguments.of(actual.get(5), expected.get(5))
					, Arguments.of(actual.get(6), expected.get(6))
					, Arguments.of(actual.get(7), expected.get(7))
					);
		}
	}
	

}
