package org.qhn.utils.sms;

import com.alibaba.fastjson.JSON;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;

//导入可选配置类
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;

// 导入对应SMS模块的client
import org.qhn.utils.sms.SmsClient;

// 导入要请求接口对应的request response类
import org.qhn.utils.sms.models.SendSmsRequest;
import org.qhn.utils.sms.models.SendSmsResponse;

/**
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
     * @return String
     * @author tianyh
     * @Description
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
            OutputStream os = httpURLConnection.getOutputStream();
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
     * 腾讯云发送单条短信
     */
    public static SendSmsResponse sendSingleMsg(String msg, String mobile) {
//		SmsSendRequest smsSingleRequest = new SmsSendRequest(account, password, msg, mobile,"true");
//		String requestJson = JSON.toJSONString(smsSingleRequest);
//		System.out.println("before request string is: " + requestJson);
//		String response = ChuangLanSmsUtil.sendSmsByPost("http://smssh1.253.com/msg/send/json", requestJson);
//		System.out.println("response after request result is :" + response);
//		SmsSendResponse smsSendResponse = JSON.parseObject(response, SmsSendResponse.class);
//		//todo 模拟发送短信
//		smsSendResponse.setCode("0");
//		System.out.println("response  toString is :" + smsSendResponse);
//		return smsSendResponse;

        try {
            /* 必要步骤：
             * 实例化一个认证对象，入参需要传入腾讯云账户密钥对secretId，secretKey。
             * 这里采用的是从环境变量读取的方式，需要在环境变量中先设置这两个值。
             * 你也可以直接在代码中写死密钥对，但是小心不要将代码复制、上传或者分享给他人，
             * 以免泄露密钥对危及你的财产安全。
             * CAM密匙查询: https://console.cloud.tencent.com/cam/capi*/
            Credential cred = new Credential("AKID5xbzDpYWgesUQ8ApMIxZ06zmR3YnfuJX",
                "PacVBbzEG2WpwOZS7fx1qPhsjRv83i6D");

            // 实例化一个http选项，可选，没有特殊需求可以跳过
            HttpProfile httpProfile = new HttpProfile();
            // 设置代理
//			httpProfile.setProxyHost("host");
//			httpProfile.setProxyPort(port);
            /* SDK默认使用POST方法。
             * 如果你一定要使用GET方法，可以在这里设置。GET方法无法处理一些较大的请求 */
            httpProfile.setReqMethod("POST");
            /* SDK有默认的超时时间，非必要请不要进行调整
             * 如有需要请在代码中查阅以获取最新的默认值 */
            httpProfile.setConnTimeout(60);
            /* SDK会自动指定域名。通常是不需要特地指定域名的，但是如果你访问的是金融区的服务
             * 则必须手动指定域名，例如sms的上海金融区域名： sms.ap-shanghai-fsi.tencentcloudapi.com */
            httpProfile.setEndpoint("sms.tencentcloudapi.com");

            /* 非必要步骤:
             * 实例化一个客户端配置对象，可以指定超时时间等配置 */
            ClientProfile clientProfile = new ClientProfile();
            /* SDK默认用TC3-HMAC-SHA256进行签名
             * 非必要请不要修改这个字段 */
            clientProfile.setSignMethod("HmacSHA256");
            clientProfile.setHttpProfile(httpProfile);
            /* 实例化要请求产品(以sms为例)的client对象
             * 第二个参数是地域信息，可以直接填写字符串ap-guangzhou，或者引用预设的常量 */
            SmsClient client = new SmsClient(cred, "ap-guangzhou", clientProfile);
            /* 实例化一个请求对象，根据调用的接口和实际情况，可以进一步设置请求参数
             * 你可以直接查询SDK源码确定接口有哪些属性可以设置
             * 属性可能是基本类型，也可能引用了另一个数据结构
             * 推荐使用IDE进行开发，可以方便的跳转查阅各个接口和数据结构的文档说明 */
            SendSmsRequest req = new SendSmsRequest();

            /* 填充请求参数,这里request对象的成员变量即对应接口的入参
             * 你可以通过官网接口文档或跳转到request对象的定义处查看请求参数的定义
             * 基本类型的设置:
             * 帮助链接：
             * 短信控制台: https://console.cloud.tencent.com/sms/smslist
             * sms helper: https://cloud.tencent.com/document/product/382/3773 */

            /* 短信应用ID: 短信SdkAppid在 [短信控制台] 添加应用后生成的实际SdkAppid，示例如1400006666 */
			String appid = "1400366726";
            req.setSmsSdkAppid(appid);

            /* 短信签名内容: 使用 UTF-8 编码，必须填写已审核通过的签名，签名信息可登录 [短信控制台] 查看 */
            String sign = "travelingtog";
            req.setSign(sign);

            /* 国际/港澳台短信 senderid: 国内短信填空，默认未开通，如需开通请联系 [sms helper] */
            String senderid = "";
            req.setSenderId(senderid);

            /* 用户的 session 内容: 可以携带用户侧 ID 等上下文信息，server 会原样返回 */
//			String session = "xxx";
//			req.setSessionContext(session);

            /* 短信码号扩展号: 默认未开通，如需开通请联系 [sms helper] */
//			String extendcode = "xxx";
//			req.setExtendCode(extendcode);

            /* 模板 ID: 必须填写已审核通过的模板 ID。模板ID可登录 [短信控制台] 查看 */
            String templateID = "605192";
            req.setTemplateID(templateID);

            /* 下发手机号码，采用 e.164 标准，+[国家或地区码][手机号]
             * 示例如：+8613711112222， 其中前面有一个+号 ，86为国家码，13711112222为手机号，最多不要超过200个手机号*/
            String[] phoneNumbers = {"+86" + mobile};
            req.setPhoneNumberSet(phoneNumbers);

            /* 模板参数: 若无模板参数，则设置为空*/
            String[] templateParams = {"5678"};
            req.setTemplateParamSet(templateParams);

            /* 通过 client 对象调用 SendSms 方法发起请求。注意请求方法名与请求对象是对应的
             * 返回的 res 是一个 SendSmsResponse 类的实例，与请求对象对应 */
            System.out.println("Request:"+SendSmsRequest.toJsonString(req));
            SendSmsResponse res = client.SendSms(req);

            // 输出json格式的字符串回包
            System.out.println("Response:"+SendSmsResponse.toJsonString(res));

            // 也可以取出单个值，你可以通过官网接口文档或跳转到response对象的定义处查看返回字段的定义
            System.out.println(res.getRequestId());

            return res;
        } catch (TencentCloudSDKException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 群发短信
     */
    public static void sendMutilMsg(String msg, List<String> mobiles) {
        String mobile = "";
        if (0 < mobiles.size() && mobiles.size() < 1000) {
            for (int i = 0; i < mobiles.size(); i++) {
                if (i == mobiles.size() - 1) {
                    mobile += mobiles.get(i);
                } else {
                    mobile += mobiles.get(i) + ",";
                }
            }
            SmsSendRequest smsSingleRequest = new SmsSendRequest(account, password, msg, mobile,
                "true");
            String requestJson = JSON.toJSONString(smsSingleRequest);
            System.out.println("before request string is: " + requestJson);
            String response = ChuangLanSmsUtil
                .sendSmsByPost("http://smssh1.253.com/msg/send/json", requestJson);
            System.out.println("response after request result is :" + response);
            SmsSendResponse smsSingleResponse = JSON.parseObject(response, SmsSendResponse.class);
            System.out.println("response  toString is :" + smsSingleResponse);
        }
    }

    public static void main(String[] args) {
        ChuangLanSmsUtil
            .sendSingleMsg("【奇灵科技】尊敬的用户您好，您的账单2000元即将到期，请及时通过有米APP进行还款，如已还款请忽略", "18657173257");
    }

}
