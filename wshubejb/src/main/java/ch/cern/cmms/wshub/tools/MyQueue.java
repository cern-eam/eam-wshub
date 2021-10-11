package ch.cern.cmms.wshub.tools;

import java.util.LinkedHashMap;
import java.util.Map;


public class MyQueue<T> {

	private final static int SIZE_LIMIT = 200;
	  private Map<String, T> cache =
	    new LinkedHashMap<String, T>(16, 0.75f, true) {
			private static final long serialVersionUID = 1L;
		protected boolean removeEldestEntry(Map.Entry<String, T> eldest)
	      {
	        return size() > SIZE_LIMIT;
	      }
	  };

	public void add(String code, T entity) {
		cache.put(code, entity);
	}
	
	public T get(String code) {
		return cache.get(code);
	}
	
	public T remove(String code) {
		return cache.remove(code);
	}
	
}
