package org.ftn.msgbase.tools;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class CollectionUtils {

	public static <K, V> void putInMultimap(Map<K, List<V>> map, K key, V value) {
		List<V> list = map.get(key);
		if (list == null) {
			map.put(key, list = new ArrayList<>());
		}
		list.add(value);
	}

	public static <K, V> void putAllInMultimap(Map<K, List<V>> map, K key, List<V> value) {
		List<V> list = map.get(key);
		if (list == null) {
			map.put(key, list = new ArrayList<>());
		}
		list.addAll(value);
	}

}
