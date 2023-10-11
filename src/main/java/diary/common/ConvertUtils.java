package diary.common;

/**
 * データ変換に関するユーティリティクラス
 */
public class ConvertUtils {

	/**
	 * 数字列を整数に変換する
	 * @param target 変換対象数字列
	 * @return 整数に変換できる場合は変換後の整数、それ以外は0
	 */
	public static int toInt(String target) {
		if (target == null || target.isEmpty() || !target.matches("^[0-9]+$")) {
			return 0;
		}
		return Integer.parseInt(target);
	}

}
