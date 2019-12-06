package org.qhn.utils;

public class CoreConstants
{

	public static String ENVIRONMENT = "";

	public static String UNKNOWN = "";

	public static final long BYTE_HEX = 1024L;

	public static final long MBYTE = 1048576L;

	public static final long GBYTE = 1073741824L;

	public static final String FILE_SEPARATOR = System.getProperty("file.separator");

	public static final String ENVIRONMENT_PRODUCT = "production";

	public static final String ENVIRONMENT_TESTING = "testing";

	public static final String ENVIRONMENT_DEVELOPMENT = "";

	public static final String UP_FLAG_TRUE = "1";// 有上传文件

	public static final String UP_FLAG_FALSE = "2";// 无上传文件

	// 是否运行上传空数据
	public static final String UP_FILE_NULL_FLAG_TRUE = "1";// 允许上传空数据

	public static final String UP_FILE_NULL_FLAG_FALSE = "2";// 不允许上传空数据

	public static final String SERVICETYPE_HTTP = "1";

	public static final String CHARSETNAME_DEFAULT = "UTF-8";

	public static final String ENCODING_UTF8 = "UTF-8";

	// 请求格式
	public static final String XML = "xml";

	public static final String JSON = "json";

	public static final int CONTENTLENGTH = 102400; // 100KB

	public static final int CONTENTLENGTH_MAX = 1073741824; // 1G

	public static final int CONTENTLENGTH_IMG800 = 819200; // 图片800KB

	public static final int CONTENTLENGTH_IMG600 = 614400; // 图片600KB

	// 请求方式
	public static final String METHOD_GET = "GET";

	public static final String METHOD_POST = "POST";

	// API允许状态
	public static final String SERVICE_STATUS_RUNNING = "1";// 运行中

	public static final String SERVICE_STATUS_STOP = "2";// 服务已暂停

	public static final String RESOURCEPATH = new Object()
	{
		public String getResourcePath()
		{
			return this.getClass().getClassLoader().getResource("").getPath();
		}
	}.getResourcePath();

	private static final String DATA = "data";

	private static final String DATAPATH = RESOURCEPATH + DATA + FILE_SEPARATOR;

	public static final String DEFAULTRESPATH = DATAPATH + FILE_SEPARATOR + "res" + FILE_SEPARATOR;

}
