package org.jibx.binding.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

public class Lists {

	public static <E> ArrayList<E> newArrayList(E... elements) {
		JibxUtils.checkNotNull(elements); // for GWT
		// Avoid integer overflow when a large array is passed in
		ArrayList<E> list = new ArrayList<E>(elements.length);
		Collections.addAll(list, elements);
		return list;
	}

	public static <E> ArrayList<E> newArrayList(Iterable<? extends E> elements) {
		JibxUtils.checkNotNull(elements);
		return (elements instanceof Collection) ? new ArrayList<E>((Collection) (elements)) : newArrayList(elements.iterator());
	}

	public static <E> ArrayList<E> newArrayList(Iterator<? extends E> elements) {
		JibxUtils.checkNotNull(elements); // for GWT
		ArrayList<E> list = newArrayList();
		while (elements.hasNext()) {
			list.add(elements.next());
		}
		return list;
	}

}
