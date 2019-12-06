package org.qhn.utils.sms;

import com.alibaba.fastjson.JSON;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 *
 * @author tianyh
 * @Description:HTTP 请求
 */
public class ChuangLanSmsUtil {

	public static final String charset = "utf-8";
	// 请登录zz.253.com 获取创蓝API账号(非登录账号,示例:N1234567)
	public static String account = "N7622620";
	// 请登录zz.253.com 获取创蓝API密码(非登录密码)
	public static String password = "u07rfRVp3mf576";

	/**
	 *
	 * @author tianyh
	 * @Description
	 * @param path
	 * @param postContent
	 * @return String
	 * @throws
	 */
	public static String sendSmsByPost(String path, String postContent) {
		URL url = null;
		try {
			url = new URL(path);
			HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
			httpURLConnection.setRequestMethod("POST");// 提交模式
			httpURLConnection.setConnectTimeout(10000);//连接超时 单位毫秒
			httpURLConnection.setReadTimeout(2000);//读取超时 单位毫秒
			// 发送POST请求必须设置如下两行
			httpURLConnection.setDoOutput(true);
			httpURLConnection.setDoInput(true);
			httpURLConnection.setRequestProperty("Charset", "UTF-8");
			httpURLConnection.setRequestProperty("Content-Type", "application/json");

//			PrintWriter printWriter = new PrintWriter(httpURLConnection.getOutputStream());
//			printWriter.write(postContent);
//			printWriter.flush();

			httpURLConnection.connect();
			OutputStream os=httpURLConnection.getOutputStream();
			os.write(postContent.getBytes("UTF-8"));
			os.flush();

			StringBuilder sb = new StringBuilder();
			int httpRspCode = httpURLConnection.getResponseCode();
			if (httpRspCode == HttpURLConnection.HTTP_OK) {
				// 开始获取数据
				BufferedReader br = new BufferedReader(
						new InputStreamReader(httpURLConnection.getInputStream(), "utf-8"));
				String line = null;
				while ((line = br.readLine()) != null) {
					sb.append(line);
				}
				br.close();
				return sb.toString();

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 发送单条短信
	 * @param msg
	 * @param mobile
	 */
	public static SmsSendResponse sendSingleMsg(String msg,String mobile){
		SmsSendRequest smsSingleRequest = new SmsSendRequest(account, password, msg, mobile,"true");
		String requestJson = JSON.toJSONString(smsSingleRequest);
		System.out.println("before request string is: " + requestJson);
		String response = ChuangLanSmsUtil.sendSmsByPost("http://smssh1.253.com/msg/send/json", requestJson);
		System.out.println("response after request result is :" + response);
		SmsSendResponse smsSendResponse = JSON.parseObject(response, SmsSendResponse.class);
		//todo 模拟发送短信
		smsSendResponse.setCode("0");
		System.out.println("response  toString is :" + smsSendResponse);
		return smsSendResponse;
	}

	/**
	 * 群发短信
	 * @param msg
	 * @param mobiles
	 */
	public static void sendMutilMsg(String msg,List<String> mobiles){
		String mobile = "";
		if(0 < mobiles.size() && mobiles.size() < 1000){
			for(int i = 0;i<mobiles.size();i++){
				if(i==mobiles.size()-1){
					mobile += mobiles.get(i);
				}else {
					mobile += mobiles.get(i) + ",";
				}
			}
			SmsSendRequest smsSingleRequest = new SmsSendRequest(account, password, msg, mobile,"true");
			String requestJson = JSON.toJSONString(smsSingleRequest);
			System.out.println("before request string is: " + requestJson);
			String response = ChuangLanSmsUtil.sendSmsByPost("http://smssh1.253.com/msg/send/json", requestJson);
			System.out.println("response after request result is :" + response);
			SmsSendResponse smsSingleResponse = JSON.parseObject(response, SmsSendResponse.class);
			System.out.println("response  toString is :" + smsSingleResponse);
		}
	}

	public static void main(String[] args) {
		ChuangLanSmsUtil.sendSingleMsg("【奇灵科技】尊敬的用户您好，您的账单2000元即将到期，请及时通过有米APP进行还款，如已还款请忽略","18657173257");
	}

}
