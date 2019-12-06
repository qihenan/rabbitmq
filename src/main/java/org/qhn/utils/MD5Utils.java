package org.qhn.utils;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.SignatureException;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class MD5Utils {
	private final static Logger log = LoggerFactory.getLogger(MD5Utils.class);

	private static final int BUFFERSIZE = 8196;
	private static final String ALGORITHM = "MD5";
	private static final String CHARSET = "UTF-8";

	private static final String SALT = "1qazxsw2";

	private static final String ALGORITH_NAME = "md5";

	private static final int HASH_ITERATIONS = 2;

	public MD5Utils() {
	}

	public static String md5(String input) {
		return md5(input, CHARSET);
	}

	private static String md5(String input, String charsetName) {
		try {
			MessageDigest md5 = MessageDigest.getInstance(ALGORITHM);
			byte md5Bytes[] = md5.digest(input.getBytes(charsetName));
			return StringUtils.byte2hex(md5Bytes);
		} catch (Exception e) {
			log.error((new StringBuilder(String.valueOf(TraceInfoUtils.getTraceInfo()))).append(e.getMessage()).toString());
		}
		return input;
	}

	public static String md5file(String filename) {
		BufferedInputStream bufferedInputStream = null;
		MessageDigest md;
		try {
			bufferedInputStream = new BufferedInputStream(new FileInputStream(filename), BUFFERSIZE);
			md = MessageDigest.getInstance(ALGORITHM);
			byte[] buffer = new byte[BUFFERSIZE];
			int i = 0;
			while ((i = bufferedInputStream.read(buffer)) != -1) {
				md.update(buffer, 0, i);
			}
			return StringUtils.byte2hex(md.digest());
		} catch (Exception e) {
			log.error(TraceInfoUtils.getTraceInfo() + " filename:" + filename + "\t" + StringUtils.nullToStrTrim(e.getMessage()));
			return null;
		} finally {
			WebUtils.getInstance().closeBufferedInputStream(bufferedInputStream);
		}
	}

	public static String md5reverse(String md5) {
		return "";
	}

	/**
	 * 签名字符串
	 *
	 * @param text
	 *            需要签名的字符串
	 * @param key
	 *            密钥
	 * @param input_charset
	 *            编码格式
	 * @return 签名结果
	 */
	public static String sign(String text, String key, String input_charset) {
		text = text + key;
		return DigestUtils.md5Hex(getContentBytes(text, input_charset));
	}

	/**
	 * 签名字符串
	 *
	 * @param text
	 *            需要签名的字符串
	 * @param sign
	 *            签名结果
	 * @param key
	 *            密钥
	 * @param input_charset
	 *            编码格式
	 * @return 签名结果
	 */
	public static boolean verify(String text, String sign, String key, String input_charset) {
		text = text + key;
		String mysign = DigestUtils.md5Hex(getContentBytes(text, input_charset));
		if (mysign.equals(sign)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * @param content
	 * @param charset
	 * @return
	 * @throws SignatureException
	 * @throws UnsupportedEncodingException
	 */
	private static byte[] getContentBytes(String content, String charset) {
		if (charset == null || "".equals(charset)) {
			return content.getBytes();
		}
		try {
			return content.getBytes(charset);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("MD5签名过程中出现错误,指定的编码集不对,您目前指定的编码集是:" + charset);
		}
	}

	private static final char[] hexDigits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

	public static String encryptGenerate(String data) {
		if (StringUtils.isEmpty(data)) {
			return null;
		} else {
			data = data.trim();
		}
		return encrypt(encrypt(encrypt(encrypt(data) + "msc50.com")));
	}

	public static String encrypt(String data) {
		try {
			byte[] bdata = data.getBytes("utf-8");
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(bdata);
			byte[] digests = md.digest();
			int len = digests.length;
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < len; i++) {
				byte byte0 = digests[i];
				sb.append(hexDigits[byte0 >>> 4 & 0xf]);
				sb.append(hexDigits[byte0 & 0xf]);
			}
			return sb.toString();
		} catch (Exception e) {
		}
		return null;
	}

/*	public static String encrypt(String username, String pswd) {
		String newPassword = new SimpleHash(ALGORITH_NAME, pswd, ByteSource.Util.bytes(username + SALT),
				HASH_ITERATIONS).toHex();
		return newPassword;
	}*/

	public static final String MD5Encrpytion(String source) {
		try {
			byte[] strTemp = source.getBytes(Charset.forName("UTF-8"));
			MessageDigest mdTemp = MessageDigest.getInstance("MD5");
			mdTemp.update(strTemp);
			byte[] md = mdTemp.digest();
			int j = md.length;
			char[] str = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; ++i) {
				byte byte0 = md[i];
				str[(k++)] = hexDigits[(byte0 >>> 4 & 0xF)];
				str[(k++)] = hexDigits[(byte0 & 0xF)];
			}
			for (int m = 0; m < str.length; ++m) {
				if ((str[m] >= 'a') && (str[m] <= 'z')) {
					str[m] = (char) (str[m] - ' ');
				}
			}
			// System.out.println("[MD5Utils] [source String]" + source);
			// System.out.println("[MD5Utils] [MD5 String]" + new String(str));
			return new String(str);
		} catch (Exception e) {
		}
		return null;
	}

	public static final String MD5Encrpytion(byte[] source) {
		try {
			MessageDigest mdTemp = MessageDigest.getInstance("MD5");
			mdTemp.update(source);
			byte[] md = mdTemp.digest();
			int j = md.length;
			char[] str = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; ++i) {
				byte byte0 = md[i];
				str[(k++)] = hexDigits[(byte0 >>> 4 & 0xF)];
				str[(k++)] = hexDigits[(byte0 & 0xF)];
			}
			for (int m = 0; m < str.length; ++m) {
				if ((str[m] >= 'a') && (str[m] <= 'z')) {
					str[m] = (char) (str[m] - ' ');
				}
			}
			// System.out.println("[MD5Utils] [source String]" + source);
			// System.out.println("[MD5Utils] [MD5 String]" + new String(str));
			return new String(str);
		} catch (Exception e) {
		}
		return null;
	}

}
