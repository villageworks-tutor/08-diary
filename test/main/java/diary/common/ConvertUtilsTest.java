package diary.common;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

/**
 * ConvertUtiilsクラスのテストクラス
 */
class ConvertUtilsTest {

	@Nested
	@DisplayName("ConvertUtils#toInt(String)メソッドのテストクラス")
	class ToIntTest {
		
		@Test
		@DisplayName("【Test-03】nullの変換")
		void test03() {
			// setup
			String target = null;
			int expected = 0;
			// execute
			int actual = ConvertUtils.toInt(target);
			// vevrify
			assertThat(actual, is(expected));
		}
		
		@Test
		@DisplayName("【Test-02】空白文字列の変換")
		void test02() {
			// setup
			String[] targets = new String[] {" ", "　"};
			int expected = 0;
			
			for (String target : targets) {
				// execute
				int actual = ConvertUtils.toInt(target);
				// vevrify
				assertThat(actual, is(expected));
			}
		}
		
		@ParameterizedTest
		@CsvSource({
			  "7, 7"    // 数字列→整数のテスト 
			, "03, 3"   // ゼロパディングの数字列→整数のテスト
			, "a, 0"    // 文字→0 のテスト
			, "hoge, 0" // 文字列→0 のテスト
			, "3.14, 0" // 小数列→0 のテスト
			
		})
		@DisplayName("【Test-01】一般的な文字列の変換")
		void test01(String target, int expected) {
			// execute & 
			int actual = ConvertUtils.toInt(target);
			// vevrify
			assertThat(actual, is(expected));
		}
	}

}
