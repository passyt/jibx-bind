package org.jibx.binding.util;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 
 * @author Passyt
 *
 */
public class Counter {

	private static Counter INSTANCE = new Counter();

	private Map<String, AtomicInteger> bindingCounter = new HashMap<String, AtomicInteger>();
	private Map<String, Integer> bindingClassCache = new HashMap<String, Integer>();
	private Map<String, AtomicInteger> bindingClassMethodCounter = new HashMap<String, AtomicInteger>();

	private Counter() {
	}

	public int classId(String bindingName, String className) {
		String key = bindingName + "-" + className;
		if (bindingClassCache.containsKey(key)) {
			return bindingClassCache.get(key);
		}

		synchronized (bindingClassCache) {
			if (bindingClassCache.containsKey(key)) {
				return bindingClassCache.get(key);
			}

			int id = currentId(bindingCounter, bindingName).getAndIncrement();
			bindingClassCache.put(key, id);
			return id;
		}
	}

	public int classMethodId(String bindingName, String className) {
		return currentId(bindingClassMethodCounter, bindingName + "-" + className).incrementAndGet();
	}

	private AtomicInteger currentId(Map<String, AtomicInteger> counter, String name) {
		AtomicInteger ai = counter.get(name);
		if (ai == null) {
			synchronized (counter) {
				ai = counter.get(name);
				if (ai == null) {
					ai = new AtomicInteger(0);
					counter.put(name, ai);
				}
			}
		}

		return ai;
	}

	public static Counter get() {
		return INSTANCE;
	}

}
