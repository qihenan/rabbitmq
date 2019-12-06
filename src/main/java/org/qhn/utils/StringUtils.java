package org.qhn.utils;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Collection;
import java.util.Map;

public class StringUtils {

	public static String nullToStrTrim(String str) {
		if (str == null) {
			str = "";
		}
		return str.trim();
	}

	public static int nullToIntZero(String str) {
		if (ValidatorUtils.isNull(str)) {
			str = "0";
		}
		return Integer.valueOf(str.trim(), 10);
	}

	public static String byte2hex(byte[] b) {

		String str = "";
		String stmp = "";

		int length = b.length;

		for (int n = 0; n < length; n++) {
			stmp = (Integer.toHexString(b[n] & 0XFF));
			if (stmp.length() == 1) {
				str += "0";
			}
			str += stmp;
		}

		return str.toLowerCase();
	}

	public static String encode(String str, String enc) throws Exception {

		String strEncode = "";

		if (str != null)
			strEncode = URLEncoder.encode(str, enc);

		return strEncode;
	}

	public static String decode(String str, String enc) throws Exception {

		String strDecode = "";

		if (str != null)
			strDecode = URLDecoder.decode(str, enc);

		return strDecode;
	}

	public static String encode(String str) throws Exception {

		return encode(str, CoreConstants.CHARSETNAME_DEFAULT);
	}

	public static String decode(String str) throws Exception {

		return decode(str, CoreConstants.CHARSETNAME_DEFAULT);
	}

	// 获取一个32位的字符,传入内容长度不够32则以0补齐
	public static String get32Str(String str) {
		StringBuilder sb = new StringBuilder();
		if (ValidatorUtils.isNotNull(str)) {
			while (str.length() + sb.length() < 32) {
				sb.append("0");
			}
		}
		return sb.append(str).toString();
	}

	/**
	 * 检测是否有emoji字符
	 *
	 * @param source
	 * @return 一旦含有就抛出
	 */
	public static boolean containsEmoji(String source) {
		if (ValidatorUtils.isNull(source)) {
			return false;
		}

		int len = source.length();

		for (int i = 0; i < len; i++) {
			char codePoint = source.charAt(i);

			if (isEmojiCharacter(codePoint)) {
				// do nothing，判断到了这里表明，确认有表情字符
				return true;
			}
		}

		return false;
	}

	private static boolean isEmojiCharacter(char codePoint) {
		return (codePoint == 0x0) || (codePoint == 0x9) || (codePoint == 0xA)
				|| (codePoint == 0xD)
				|| ((codePoint >= 0x20) && (codePoint <= 0xD7FF))
				|| ((codePoint >= 0xE000) && (codePoint <= 0xFFFD))
				|| ((codePoint >= 0x10000) && (codePoint <= 0x10FFFF));
	}

	/**
	 * 过滤emoji 或者 其他非文字类型的字符
	 *
	 * @param source
	 * @return
	 */
	public static String filterEmoji(String source) {

		if (!containsEmoji(source)) {
			return source;// 如果不包含，直接返回
		}
		// 到这里铁定包含
		StringBuilder buf = null;

		int len = source.length();

		for (int i = 0; i < len; i++) {
			char codePoint = source.charAt(i);

			if (isEmojiCharacter(codePoint)) {
				if (buf == null) {
					buf = new StringBuilder(source.length());
				}

				buf.append(codePoint);
			}
		}

		if (buf == null) {
			return source;// 如果没有找到 emoji表情，则返回源字符串
		} else {
			if (buf.length() == len) {// 这里的意义在于尽可能少的toString，因为会重新生成字符串
				buf = null;
				return source;
			} else {
				return buf.toString();
			}
		}
	}

	/**
	 * 判断对象是否Empty(null或元素为0)<br>
	 * 实用于对如下对象做判断:String Collection及其子类 Map及其子类
	 *
	 * @param pObj
	 *            待检查对象
	 * @return boolean 返回的布尔值
	 */
	@SuppressWarnings("rawtypes")
	public static boolean isEmpty(Object pObj) {
		if (pObj == null)
			return true;

		if (pObj instanceof String) {
			if (((String) pObj).length() == 0) {
				return true;
			}
		} else if (pObj instanceof Collection) {
			if (((Collection) pObj).size() == 0) {
				return true;
			}
		} else if (pObj instanceof Map) {
			if (((Map) pObj).size() == 0) {
				return true;
			}
		}
		return false;
	}

}
