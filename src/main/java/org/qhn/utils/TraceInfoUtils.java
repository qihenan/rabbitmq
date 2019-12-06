package org.qhn.utils;

public class TraceInfoUtils {

	public static String getTraceInfo() {
		StringBuffer stringBuffer = new StringBuffer();
		StackTraceElement stackTraceElement = (new Throwable()).getStackTrace()[1];
		stringBuffer.append("(");
		stringBuffer.append("className:").append(
				stackTraceElement.getClassName());
		stringBuffer.append(";fieldName:").append(
				stackTraceElement.getFileName());
		stringBuffer.append(";methodName:").append(
				stackTraceElement.getMethodName());
		stringBuffer.append(";lineNumber:").append(
				stackTraceElement.getLineNumber());
		stringBuffer.append(")");
		return stringBuffer.toString();
	}

	public static String getTraceInfo(Throwable e) {
		StringBuffer stringBuffer = new StringBuffer();
		StackTraceElement stackTraceElement = e.getStackTrace()[0];
		stringBuffer.append("(");
		stringBuffer.append("className:").append(
				stackTraceElement.getClassName());
		stringBuffer.append(";fieldName:").append(
				stackTraceElement.getFileName());
		stringBuffer.append(";methodName:").append(
				stackTraceElement.getMethodName());
		stringBuffer.append(";lineNumber:").append(
				stackTraceElement.getLineNumber());
		stringBuffer.append(")");
		return stringBuffer.toString();
	}

}
