package ch.cern.cmms.wshub.entities;

import java.io.Serializable;

public class AsyncOperation implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1946002508455953779L;

	public AsyncOperation() {
		
	}
	
	public AsyncOperation(String operation, String data) {
		super();
		this.operation = operation;
		this.data = data;
		this.setDelay(0);
	}
	private String operation;
	private String data;
	private long delay;
	
	
	public String getOperation() {
		return operation;
	}
	public void setOperation(String operation) {
		this.operation = operation;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public long getDelay() {
		return delay;
	}
	public void setDelay(long delay) {
		this.delay = delay;
	}

	@Override
	public String toString() {
		return "AsyncOperation [operation=" + operation + ", data=" + data + ", delay=" + delay + "]";
	}

}
