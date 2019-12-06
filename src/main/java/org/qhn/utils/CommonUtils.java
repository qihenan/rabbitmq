package org.qhn.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import org.htmlparser.Node;
import org.htmlparser.Parser;
import org.htmlparser.util.NodeList;


public class CommonUtils {
	public static String CHARSET = "UTF-8";

	public static int getWordLength(String str) {
		str = StringUtils.nullToStrTrim(str);
		return str.replaceAll("[^\\x00-\\xff]", "**").length();
	}

	public static String getDesKey(){
		String md5 = MD5Utils.md5((new Date()).getTime()+"");
		return md5.substring(8,16).toUpperCase();
	}

	public static String getRandom() {

		String str = MD5Utils.md5(getUUID() + System.currentTimeMillis() + getRandom(999999999));
		str = str.toLowerCase();
		return str;
	}

	//获得随机数
	public static int getRandom(int accuracy) {
		return (int) (Math.random() * accuracy);
	}

	public static String getUUID() {
		return UUID.randomUUID().toString();
	}

	public static int getRealLength(String str) {

		return getRealLength(str, CHARSET);
	}

	public static int getRealLength(String str, String charsetName) {

		str = StringUtils.nullToStrTrim(str);

		if (ValidatorUtils.isNull(str)) {
			return 0;
		}

		try {
			return str.getBytes(charsetName).length;
		} catch (UnsupportedEncodingException e) {
			return 0;
		}
	}

	public static Map<String, String> pageInfo(int currentPage,int pageSize,int totalRows){
		int totalPage = 1;
		if(totalRows>0){
			totalPage = (totalRows%pageSize==0)?(totalRows/pageSize):((totalRows/pageSize)+1);
		}
		if(currentPage>totalPage){
			currentPage = totalPage;
		}
		Map<String, String> result = new LinkedHashMap<String, String>();
		result.put("total_count", totalRows+"");
		result.put("total_page", totalPage+"");
		result.put("page_size", pageSize+"");
		result.put("current_page", currentPage+"");
		return result;
	}

	public static List<Map<String, Object>> convertDataMapToApiMap(List<Map<String, Object>> dbResult,String key){
		if(dbResult==null){
			return null;
		}
		List<Map<String, Object>> result = new ArrayList<Map<String,Object>>();
		Map<String, Object> obj = null;
		for (Map<String, Object> map : dbResult) {
			obj = new HashMap<String, Object>();
			obj.put(key, map);
			result.add(obj);
		}
		return result;
	}

	/**
	 * 验证码创建
	 * @return
	 */
	public static String createVerification(){
		String code = "";
		for(int i=0;i<6;i++){
			code = code + (int)(Math.random()*10);
		}
		return code;
	}

	   /**
	    * 校验银行卡卡号
	    * @param cardId
	    * @return
	    */
	   public static boolean checkBankCard(String cardId) {
	         char bit = getBankCardCheckCode(cardId.substring(0, cardId.length() - 1));
	         if(bit == 'N'){
	             return false;
	         }
	         return cardId.charAt(cardId.length() - 1) == bit;
	   }

	   /**
	    * 从不含校验位的银行卡卡号采用 Luhm 校验算法获得校验位
	    * @param nonCheckCodeCardId
	    * @return
	    */
	   private static char getBankCardCheckCode(String nonCheckCodeCardId){
	       if(nonCheckCodeCardId == null || nonCheckCodeCardId.trim().length() == 0
	               || !nonCheckCodeCardId.matches("\\d+")) {
	        //如果传的不是数据返回N
	           return 'N';
	       }
	       char[] chs = nonCheckCodeCardId.trim().toCharArray();
	       int luhmSum = 0;
	       for(int i = chs.length - 1, j = 0; i >= 0; i--, j++) {
	           int k = chs[i] - '0';
	           if(j % 2 == 0) {
	               k *= 2;
	               k = k / 10 + k % 10;
	           }
	           luhmSum += k;
	       }
	       return (luhmSum % 10 == 0) ? '0' : (char)((10 - luhmSum % 10) + '0');
	   }

	/**
	 * 通过身份证判断年纪
	 * @param idCard
	 * @return
	 */
	public static int getAgeByIdcard(String idCard){
		int curYear = Integer.valueOf(TimeUtils.getSysTime("yyyy"));
		int birthYear = Integer.valueOf(idCard.substring(6, 10));
		int curDate = Integer.valueOf(TimeUtils.getSysTime("MMdd"));
		int birthDate = Integer.valueOf(idCard.substring(10, 14));
		if(curDate>=birthDate+1){//天数往后延一天
			return (curYear-birthYear);
		}else{
			return (curYear-birthYear-1);
		}
	}


	public static String get(String apiUrl,String decode)
			throws Exception {
		String str = null;
		URL url = new URL(apiUrl);
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.setRequestMethod("GET");
		con.setReadTimeout(5000);
		con.setRequestProperty("Content-Type",
				"application/x-www-form-urlencoded");
		if (con.getResponseCode() == 200) {
			str = formatIsToString(con.getInputStream(),decode);
		}
		return str;
	}

	public static String formatIsToString(InputStream is,String decode) throws Exception {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] buf = new byte[1024];
		int len = -1;
		try {
			while ((len = is.read(buf)) != -1) {
				baos.write(buf, 0, len);
			}
			baos.flush();
			baos.close();
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new String(baos.toByteArray(), decode);
	}

	/**
	 * 手机归属地查询
	 * @param phone
	 * @return
	 */
	public static String phoneWonership(String phone) {
		try {
			String url = "http://www.ip138.com:8080/search.asp?mobile="+phone+"&action=mobile";
			Parser parser = new Parser (url);
			NodeList list = parser.parse(null);
			Node node = list.elementAt(3);
			String info = node.toPlainTextString();
			int start = info.indexOf("卡号归属地",0);
			info = info.substring(start);
			int end = info.indexOf("卡",1);
			return info.substring(5,end).trim().replaceAll("&nbsp;", "");
		} catch (Exception e) {
			return "";
		}
	}


	/**
	 * 将{1,2,3}形式的字符串转化成数组
	 * @param array
	 * @return
	 */
	public static String[] getArrayByStr(String array){
		String arrayStr = array.substring(1, array.length()-1);
		return arrayStr.split(",");
	}

/**
	 * google坐标(微信中获取的)转化为百度坐标
	 * @param longitude 经度
	 * @param latitude 维度
	 * @return
	 */
/*	public static String  transCoords(String latitude,String longitude){
		String coords = "";
		try {
			String key = "HaNc9so2mqtyOs6DzGDzQjiPnDQO9hrP";
			String transUrl = "http://api.map.baidu.com/geoconv/v1/?" +
					"output=json&coords=" +latitude+","+longitude+
					"&from=3&to=5&ak=";
			transUrl = transUrl+key;
			JSONObject transInfo = new JSONObject(get(transUrl, "utf-8").toString())
			      .getJSONArray("result").getJSONObject(0);
			coords = transInfo.getDouble("x")+","+transInfo.getDouble("y");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return coords;
	}*/

/*	*//**
	 * 根据坐标获取具体位置,坐标系必须为百度的
	 * @param coords 经度,维度
	 * @return
	 *//*
	public static String gformattedAddress(String coords){
		String address = "";
		try {
			String key = "HaNc9so2mqtyOs6DzGDzQjiPnDQO9hrP";
			 String url = " http://api.map.baidu.com/geocoder/v2/?output=json&ak=" +
			       		key+"&location="+coords;
			  JSONObject info = new JSONObject(get(url, "utf-8").toString());
			   address = info.getJSONObject("result").getString("formatted_address");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return address;
	}

	*//**
	 * 根据坐标获取具体位置,坐标系必须为百度的
	 * @param coords 经度,维度
	 * @return
	 *//*
	public static String getIpArea(String ip){
		String url = "http://int.dpool.sina.com.cn/iplookup/iplookup.php?format=json&ip=";
		String result = null;
		try {
			result = get(url+ip, "utf-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		 return result;
	}*/

	/**
	 * 获得客户端真实IP地址
	 * @param request
	 * @return
	 */
	public static String getIpAddr(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
        System.out.println("x-forwarded-for ip: " + ip);
        if (ip != null && ip.length() != 0 && !"unknown".equalsIgnoreCase(ip)) {
            // 多次反向代理后会有多个ip值，第一个ip才是真实ip
            if( ip.indexOf(",")!=-1 ){
                ip = ip.split(",")[0];
            }
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
            System.out.println("Proxy-Client-IP ip: " + ip);
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
            System.out.println("WL-Proxy-Client-IP ip: " + ip);
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
            System.out.println("HTTP_CLIENT_IP ip: " + ip);
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
            System.out.println("HTTP_X_FORWARDED_FOR ip: " + ip);
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
            System.out.println("X-Real-IP ip: " + ip);
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
            System.out.println("getRemoteAddr ip: " + ip);
        }
        System.out.println("获取客户端ip: " + ip);
        return ip;
	   }

}
