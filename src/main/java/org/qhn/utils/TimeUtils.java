package org.qhn.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TimeUtils {

	private final static Logger log = LoggerFactory.getLogger(TimeUtils.class);

	public static long DAY_SECONDS = 86400L;

	public static final long MILLISECOND_DAY = 86400000L;

	public static String  yyyyMMddHHmmss="yyyy-MM-dd HH:mm:ss";

	public static final String yyyyMMddHHmmssInt = "yyyyMMddHHmmss";

	public static final String TIMESTAMP_FOMART_OTHER="yyyyMMddHHmmssSSS";

	/**
    * 获得指定时间的时间戳
    * @param data
    * @return
    */
   public static int getTimeStampByDate(String data,String pattern){
	   Date date = format(data, pattern);
	   Long tm = date.getTime()/1000;
	   return tm.intValue();
   }

	/**
	 * 取得系统时间
	 * @param pattern eg:yyyy-MM-dd HH:mm:ss,SSS
	 * @return
	 */
    public static String getSysTime(String pattern) {

        return formatSysTime(new SimpleDateFormat(pattern));
    }

    /**
     * 格式化系统时间
     * @param format
     * @return
     */
    private static String formatSysTime(SimpleDateFormat format) {

        String str = format.format(Calendar.getInstance().getTime());
        return str;
    }

    public static String format(Date date, String pattern) {

    	SimpleDateFormat format = new SimpleDateFormat(pattern, Locale.ENGLISH);
        String str = format.format(date);

		return str;
    }

    public static Date format(String str, String pattern) {

    	DateFormat formatter = new SimpleDateFormat(pattern, Locale.ENGLISH);
		Date date = null;
		try {
			date = (Date) formatter.parse(str);
		} catch (ParseException e) {
			log.error(TraceInfoUtils.getTraceInfo() +" str:"+str+" pattern:"+pattern+"\t"+ StringUtils.nullToStrTrim(e.getMessage()));
			return null;
		}

		return date;
	}


    public static String format(Long timeStamp, String pattern) {
    	Date date = new Date(timeStamp*1000);
    	SimpleDateFormat format = new SimpleDateFormat(pattern, Locale.ENGLISH);
        String str = format.format(date);

		return str;
    }

    public static String getSysYear() {

        return getSysTime("yyyy");
    }

    public static String getSysTime() {

        return getSysTime("yyyy-MM-dd HH:mm:ss");
    }

    public static String getSysTimeS() {

        return getSysTime("yyyy-MM-dd HH:mm:ss,SSS");
    }

    public static String getSysTimeLong() {

        return getSysTime("yyyyMMddHHmmss");
    }

    public static String getSysTimeSLong() {

        return getSysTime("yyyyMMddHHmmssSSS");
    }

    public static String getSysdate() {

        return getSysTime("yyyy-MM-dd");
    }

    public static String getSysyearmonthInt() {

        return getSysTime("yyyyMM");
    }

    public static String getSysdateInt() {

        return getSysTime("yyyyMMdd");
    }

    public static String getSysdateTimeStart() {

        return getSysdate() + " 00:00:00";
    }

    public static String getYesterdayTimeStart() {
    	String str = new SimpleDateFormat("yyyy-MM-dd").format( (getCurrentTimeStamp() - DAY_SECONDS)*1000l );
    	return str + " 00:00:00";
    }

    public static String getYesterdayTimeEnd() {
    	String str = new SimpleDateFormat("yyyy-MM-dd").format( (getCurrentTimeStamp() - DAY_SECONDS) *1000l);
    	return str + " 23:59:59";
    }

    public static String getYesterday(){
		   long timeStamp = getCurrentTimeStamp()-DAY_SECONDS;
		   return format(new Date(timeStamp*1000), "yyyy-MM-dd");
	   }

    public static String getSysdateTimeEnd() {

        return getSysdate() + " 23:59:59";
    }

    public static String getSysdateTimeEndLong() {

        return getSysdateInt() + "235959";
    }

    public static String getSysDateLocal() {

        return getSysTime("yyyy年MM月dd日");
    }

    public static String getDateEnd(int days,String pattern){
    	Date date = format(getSysdateTimeEnd(), yyyyMMddHHmmss);
    	Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DAY_OF_MONTH, days);
		Date date2 =  cal.getTime();
		return format(date2, pattern);
    }

    public static void sleep(long millis) {

		if(millis <= 0L) {
			return;
		}

		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			log.error(TraceInfoUtils.getTraceInfo() +" millis:"+millis+"\t"+ StringUtils.nullToStrTrim(e.getMessage()));
		}
	}


	public static Date addSecond(int second) {

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.add(Calendar.SECOND, second);
		Date time = calendar.getTime();
		return time;
	}

	 /**
	    * 获得指定时间的时间戳
	    * @param data
	    * @return
	    */
	   public static long getCurrentTimeStamp(){
		   Date date = new Date();
		   return date.getTime()/1000;
	   }

	   public static long timeLimitDay(Long startTime,Long endTime){
			Date date1 = new Date(startTime*1000l);
			Date date2 = new Date(endTime*1000l);
			return subtractDay(format(date2, "yyyyMMdd"), format(date1, "yyyyMMdd"), "yyyyMMdd");
		}

	   public static long subtractDay(String end, String start, String pattern) {

		    return subtract(end, start, pattern) / MILLISECOND_DAY;
		}

	   public static long subtract(String end, String start, String pattern) {
		    return format(end, pattern).getTime() - format(start, pattern).getTime();
		}

	   /**
		 * 两个时间之间的天数
		 *
		 * @param date1
		 * @param date2
		 * @return
		 */
		public static long getDays(String date1, String date2) {
		    if (date1 == null || date1.equals(""))
		        return 0;
		    if (date2 == null || date2.equals(""))
		        return 0;
		    // 转换为标准时间
		    SimpleDateFormat myFormatter = new SimpleDateFormat("yyyy-MM-dd");
		    Date date = null;
		    Date mydate = null;
		    try {
		        date = myFormatter.parse(date1);
		        mydate = myFormatter.parse(date2);
		    } catch (Exception e) {
		    	e.printStackTrace();
		    }
		    long day = (date.getTime() - mydate.getTime()) / (24 * 60 * 60 * 1000);
		    return day;
		}

	  /***
	    * 获取当天结束时间23：59
	    * @return
	    */
	   public static long getDayEndtamp(){
		   Calendar cal = Calendar.getInstance();
			cal.set(Calendar.HOUR_OF_DAY, 23);
			cal.set(Calendar.SECOND, 59);
			cal.set(Calendar.MINUTE, 59);
			cal.set(Calendar.MILLISECOND, 0);
			return cal.getTimeInMillis()/1000;
	   }

		/**
		 * 返回第n天后的结束时间
		 * @param dayNum
		 * @return 第n天后的结束时间
		 */
		public static Date getNdays(int dayNum) {
			long nowDate = getDayEndtamp();
			return new Date((Long.valueOf(String.valueOf(nowDate))*1000+dayNum*TimeUtils.DAY_SECONDS*1000));

		}

		/**
		 * @param args
		 * @throws ParseException
		 */
		public static void main(String[] args) throws ParseException {
//			SimpleDateFormat myFormatter = new SimpleDateFormat("HH:mm:ss");
//			Date date = new Date();
//		    System.out.println(isInTime("23:40:00-00:15:00", myFormatter.format(date)));
//		    System.out.println(isInTime("20:00:00-01:00:00", myFormatter.format(date)));
//		    System.out.println(isInTime("20:00:00-01:00:00", myFormatter.format(date)));
//		    System.out.println();
//		    System.out.println(isInTime("20:00:00-23:00:00", myFormatter.format(date)));
//		    System.out.println(isInTime("20:00:00-23:00:00", myFormatter.format(date)));
//		    System.out.println(isInTime("20:00:00-23:00:00", myFormatter.format(date)));
//		    System.out.println(isInTime("20:00:00-23:00:00", myFormatter.format(date)));
//		    System.out.println(isInTime("20:00:00-23:00:00", myFormatter.format(date)));
//			Date format = format("2017-12-16 12:49:43", "yyyy-MM-dd HH:mm");
//			System.out.println(format);
//			SimpleDateFormat sdf = new SimpleDateFormat("MM.dd HH:mm");
//			Date parse = sdf.parse("2017-12-16 12:49:43");
//			Date parse = sdf.parse("2017-12-16 12:49:43");
//			System.out.println(new Date());
//			String format = sdf.format(parse);
//			System.out.println(format);
//			String mdhm = getMDHM("2017-12-16 12:49:43");
//			System.out.println("时间：" + mdhm);
//			getChangeTodayTime(1522559375);
		}

		/**
		 * 判断某一时间是否在一个区间内
		 *
		 * @param sourceTime
		 *            时间区间,半闭合,如[10:00-20:00)
		 * @param curTime
		 *            需要判断的时间 如10:00
		 * @return
		 * @throws IllegalArgumentException
		 */
		public static boolean isInTime(String sourceTime, String curTime) {
		    if (sourceTime == null || !sourceTime.contains("-") || !sourceTime.contains(":")) {
		        throw new IllegalArgumentException("Illegal Argument arg:" + sourceTime);
		    }
		    if (curTime == null || !curTime.contains(":")) {
		        throw new IllegalArgumentException("Illegal Argument arg:" + curTime);
		    }
		    String[] args = sourceTime.split("-");
		    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		    try {
		        long now = sdf.parse(curTime).getTime();
		        long start = sdf.parse(args[0]).getTime();
		        long end = sdf.parse(args[1]).getTime();
		        if (args[1].equals("00:00:00")) {
		            args[1] = "24:00:00";
		        }
		        if (end < start) {
		            if (now >= end && now < start) {
		                return false;
		            } else {
		                return true;
		            }
		        }
		        else {
		            if (now >= start && now < end) {
		                return true;
		            } else {
		                return false;
		            }
		        }
		    } catch (ParseException e) {
		        e.printStackTrace();
		        throw new IllegalArgumentException("Illegal Argument arg:" + sourceTime);
		    }

		}


		public static Boolean judjeDateIsDay(String param){
			try {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				Date jDate = sdf.parse(param);
				Date date = new Date();
				return jDate.before(date);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			return false;
		}

		/**
		 *
		 * @param date
		 * @return
		 */
		public static String getMDHM(String date){
			String format2 = null;
			try {
				SimpleDateFormat sdf1 = new SimpleDateFormat(yyyyMMddHHmmss);
				Date date1 = sdf1.parse(date);

				SimpleDateFormat sdf2 = new SimpleDateFormat("MM.dd HH:mm");
				format2 = sdf2.format(date1);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return format2;
		}
		/**
         * 得到日期   yyyy-MM-dd
         * @param timeStamp  时间戳
         * @return
         */
        public static String formatDate(long timeStamp) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String date = sdf.format(timeStamp*1000);
                return date;
        }

        /**
         * 得到时间  HH:mm:ss
         * @param timeStamp   时间戳
         * @return
         */
        public static String getTime(long timeStamp) {
                String time = null;
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String date = sdf.format(timeStamp * 1000);
                String[] split = date.split("\\s");
                if ( split.length > 1 ){
                        time = split[1];
                }
                return time;
        }
        /**
         * 将非今天时间转化为今天时间
         * @param timeStamp   时间戳
         * @return
         */
        public static long getChangeTodayTime(long timeStamp) {
            String time = null;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String date = sdf.format(timeStamp);
            String[] split = date.split("\\s");
            /*if ( split.length > 1 ){
                    time = split[1];
            }*/
            String nowTime=sdf.format(new Date().getTime());
            String[] splitNowTime = nowTime.split("\\s");
            time=splitNowTime[0]+" "+split[1];
            System.out.println(time);
            long resultReturn = 0;
			try {
				resultReturn = sdf.parse(time).getTime();
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            return resultReturn;
    }
}
