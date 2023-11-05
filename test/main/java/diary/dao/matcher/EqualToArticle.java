package diary.dao.matcher;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

import diary.bean.ArticleBean;

public class EqualToArticle extends TypeSafeMatcher<ArticleBean> {

	/** 期待値 */
	private ArticleBean expected;
	
	/** 異なる項目 */
	private String difference;
	
	public EqualToArticle(ArticleBean expected) {
		this.expected = expected;
	}

	@Override
	public void describeTo(Description description) {
		description.appendValue(difference);
		description.appendText("の値が異なっています。");
	}

	@Override
	protected boolean matchesSafely(ArticleBean actual) {
//		// 記事番号一致検査
//		if (actual.getId() != expected.getId()) {
//			this.difference = "記事番号";
//			return false;
//		}
		// 記事タイトル一致検査
		if (!actual.getTitle().equals(expected.getTitle())) {
			this.difference = "記事タイトル";
			return false;
		}
		// 記事内容一致検査
		if (!actual.getContent().equals(expected.getContent())) {
			this.difference = "記事内容";
			return false;
		}
		// 投稿者一致検査
		if (actual.getUserId() != expected.getUserId()) {
			this.difference = "投稿者";
			return false;
		}
		return true;
	}

}
