package ch.cern.cmms.wshub.rest.tools;

import ch.cern.eam.wshub.core.tools.ExceptionInfo;

public class RESTException {

	private String message;
	private ExceptionInfo[] exceptionInfoList;
	private int statusCode;

	public RESTException(String msg, ExceptionInfo[] details, int statusCode) {
		this.message = msg;
		this.exceptionInfoList = details;
		this.statusCode = statusCode;
	}

	public RESTException(String msg, ExceptionInfo[] details) {
		this.message = msg;
		this.exceptionInfoList = details;
		this.statusCode = 400;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public ExceptionInfo[] getExceptionInfoList() {
		return exceptionInfoList;
	}

	public void setExceptionInfoList(ExceptionInfo[] exceptionInfoList) {
		this.exceptionInfoList = exceptionInfoList;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

}
