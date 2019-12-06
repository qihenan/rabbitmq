package org.qhn.utils;

import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ValidatorUtils {

	private final static Logger log = LoggerFactory.getLogger(ValidatorUtils.class);

	public static boolean isNotEmpty(@SuppressWarnings("rawtypes") Map result){
		if(result!=null&&!result.isEmpty()&&result.size()>0){
			return true;
		}
		return false;
	}

	public static boolean isEmpty(@SuppressWarnings("rawtypes") Map result){
		return !isNotEmpty(result);
	}

	public static boolean isNotEmpty(@SuppressWarnings("rawtypes") List result){
		if(result!=null&&!result.isEmpty()&&result.size()>0){
			return true;
		}
		return false;
	}

	public static boolean isEmpty(@SuppressWarnings("rawtypes") List result){
		return !isNotEmpty(result);
	}

	public static boolean isNotNull(String str) {
		if (str == null) {
			return false;
		}
		return ((str != null) && (str.trim().length() > 0) && !"null"
				.equals(str.trim()));
	}

	public static boolean isNull(String str) {
		return !isNotNull(str);
	}

	public static boolean isNotNull(Integer str) {
		if (str == null) {
			return false;
		}
		return true;
	}

	public static boolean isNull(Integer str) {
		return !isNotNull(str);
	}

	public static boolean isNotNull(Long str) {
		if (str == null) {
			return false;
		}
		return true;
	}

	public static boolean isNull(Long str) {
		return !isNotNull(str);
	}

	public static boolean isNotNull(Date str) {
		if (str == null) {
			return false;
		}
		return true;
	}

	public static boolean isNull(Date str) {
		return !isNotNull(str);
	}


	public static boolean isBlank(String str) {

		int length = 0;

		if ((str == null) || ((length = str.length()) == 0)) {
			return true;
		}

		for (int i = 0; i < length; i++) {
			if (Character.isWhitespace(str.charAt(i))) {
				return true;
			}
		}

		return false;
	}

	public static boolean isHalfAngle(String str) {

		str = StringUtils.nullToStrTrim(str);
		return str.length() == CommonUtils.getWordLength(str);
	}

	public static boolean checkString(String str, String regex) {

		return str.matches(regex);
	}

	public static boolean isMd5(String md5) {

		if (md5.length() != 32) {
			return false;
		}

		return checkString(md5, "[0-9A-Fa-f]+");
	}

	public static boolean validTime(String time, String pattern) {

		if (time.length() != 14) {
			return false;
		}

		if (!checkString(time, "[2][0-9]+")) {
			return false;
		}

    	DateFormat formatter = new SimpleDateFormat(pattern, Locale.ENGLISH);

		Date date = null;
		try {
			date = (Date) formatter.parse(time);
		} catch (ParseException e) {
			log.error(TraceInfoUtils.getTraceInfo() +" time:"+time+" pattern:"+pattern+"\t"+ StringUtils.nullToStrTrim(e.getMessage()));
			return false;
		}
		return time.equals(formatter.format(date));
	}

	public static boolean isInfoCode(String code) {

		code = StringUtils.nullToStrTrim(code);
    	if(code.length() != 6) {
    		return false;
    	}

    	String regex = "^([1-9])\\d{5}$";

    	return code.matches(regex);
	}

	public static boolean isMobile(String code){
		code = StringUtils.nullToStrTrim(code);
    	if(code.length() != 11) {
    		return false;
    	}
    	String regex = "^13[0-9]{9}$|14[0-9]{9}|15[0-9]{9}|17[0-9]{9}$|18[0-9]{9}$";

    	return code.matches(regex);
	}

	public static boolean isEmail(String code){
		code = StringUtils.nullToStrTrim(code);
    	String regex = "^([a-zA-Z0-9_\\.\\-])+\\@(([a-zA-Z0-9\\-])+\\.)+([a-zA-Z0-9]{2,4})+$";
    	return code.matches(regex);
	}

	public static boolean isURL(String value){
    	String regex = "^(http|https)\\://([a-zA-Z0-9\\.\\-]+(\\:[a-zA-Z0-9\\.&amp;%\\$\\-]+)*@)*((25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9])\\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[0-9])|localhost|([a-zA-Z0-9\\-]+\\.)*[a-zA-Z0-9\\-]+\\.(com|edu|gov|int|mil|net|org|biz|arpa|info|name|pro|aero|coop|museum|[a-zA-Z]{2}))(\\:[0-9]+)*(/($|[a-zA-Z0-9\\.\\,\\?\\'\\\\\\+&amp;%\\$#\\=~_\\-]+))*$";
    	return value.matches(regex);
	}

	public static boolean isDigital(String v){
		v = StringUtils.nullToStrTrim(v);
		if(v.length()==0){
			return false;
		}
    	String regex = "^[0-9]*$";
    	return v.matches(regex);
	}

	public static boolean isInteger(String v){
		if(!isDigital(v)){
			return false;
		}
		if(Integer.valueOf(v)<=0){
			return false;
		}
		return true;
	}

	public static boolean isSms(String code){
		code = StringUtils.nullToStrTrim(code);
    	String regex = "^\\d{6}$";
    	return code.matches(regex);
	}
	public static boolean isVersion(String code){
		code = StringUtils.nullToStrTrim(code);
    	String regex = "^\\d{3}$";
    	return code.matches(regex);
	}

	public static boolean isMoney(String value){
		value = StringUtils.nullToStrTrim(value);
    	String regex = "^([1-9][\\d]{0,7}|0)(\\.[\\d]{1,2})?$";
    	return value.matches(regex);
	}

	public static boolean isNickName(String value){
		value = StringUtils.nullToStrTrim(value);
		int len = 0;
		try {
			len = value.getBytes("utf-8").length;
		} catch (UnsupportedEncodingException e) {
			return false;
		}
		if(len<4||len>18){
			return false;
		}
    	String regex = "^[0-9a-zA-Z\u4e00-\u9fa5]*$";
    	return value.matches(regex);
	}


	/**
	 * 身份证格式验证
	 * @param number
	 * @return
	 */
	public static boolean isCardId(String number){
		number = StringUtils.nullToStrTrim(number);
	    String regex = "^\\d{15}|^\\d{17}([0-9]|X|x)$";
	    return number.matches(regex);
	}

	/**
	 * 真实姓名验证
	 * @param name
	 * @return
	 */
	 public static boolean isRealName(String name){
		String regex = "(([\u4E00-\u9FA5]{2,5}))";
		return name.matches(regex);
	}

	 /**
	  * 验证日期是否有效
	  * @param dateStr
	  * @param pattern
	  * @return
	  */
	 public static boolean validDate(String dateStr, String pattern) {

	    	DateFormat formatter = new SimpleDateFormat(pattern, Locale.ENGLISH);
			Date date = null;
			try {
				date = (Date) formatter.parse(dateStr);
			} catch (ParseException e) {
				log.error(TraceInfoUtils.getTraceInfo() +" date:"+dateStr+" pattern:"+pattern+"\t"+ StringUtils.nullToStrTrim(e.getMessage()));
				return false;
			}
			return dateStr.equals(formatter.format(date));
		}


	public static boolean isNum(String num){
		num = StringUtils.nullToStrTrim(num);
    	String regex = "^\\d+(\\.\\d+)?$";
    	return num.matches(regex);
	}

}
