package org.graalvm.datastructure;

import java.util.AbstractList;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * One way would be:
 * ArrayList<Integer> = new @specialize(Integer.class) ArrayList<>(); -> This would fail because we cannot specialize when the left side is a final class
 * AbstractList<Integer> = new @specialize(Integer.class) ArrayList<>(); -> this would work
 *
 * A second way would be
 * AbstractList<Integer> = graalvm.collections.SpecializedCollectionFactory.newArrayList(Integer.class)
 */

public class DataStructureFactory {

	static {
		instance = new SpecializedDataStructureFactory();
	}

	static public DataStructureFactory instance;

	protected DataStructureFactory() { }

	public <T> AbstractList<T> ArrayList(Class<T> parameterT) {
		return new ArrayList<T>();
	}

	public <T> AbstractList<T> ArrayList(Class<T> parameterT, int initialCapacity) {
		return new ArrayList<T>(initialCapacity);
	}

	public <T> AbstractList<T> ArrayList(Class<T> parameterT, Collection<? extends T> c) {
		return new ArrayList<T>(c);
	}

	public <K,V> AbstractMap<K,V> HashMap(Class<K> parameterK, Class<V> parameterV) {
		return new HashMap<K,V>();
	}

	public <K,V> AbstractMap<K,V> HashMap(Class<K> parameterK, Class<V> parameterV, int initialCapacity) {
		return new HashMap<K,V>(initialCapacity);
	}

	public <K,V> AbstractMap<K,V> HashMap(Class<K> parameterK, Class<V> parameterV, int initialCapacity, float loadFactor) {
		return new HashMap<K,V>(initialCapacity, loadFactor);
	}

	public <K,V> AbstractMap<K,V> HashMap(Map<? extends K, ? extends V> m) {
		return new HashMap<K,V>(m);
	}

	public <K,V> AbstractMap<K,V> ConcurrentHashMap(Class<K> parameterK, Class<V> parameterV) {
		return new ConcurrentHashMap<K,V>();
	}

	public <K,V> AbstractMap<K,V> ConcurrentHashMap(Class<K> parameterK, Class<V> parameterV, int initialCapacity) {
		return new ConcurrentHashMap<K,V>(initialCapacity);
	}

	public <K,V> AbstractMap<K,V> ConcurrentHashMap(Class<K> parameterK, Class<V> parameterV, Map<? extends K, ? extends V> m) {
		return new ConcurrentHashMap<K,V>(m);
	}

	public <K,V> AbstractMap<K,V> ConcurrentHashMap(Class<K> parameterK, Class<V> parameterV, int initialCapacity, float loadFactor) {
		return new ConcurrentHashMap<K,V>(initialCapacity, loadFactor);
	}

	public <K,V> AbstractMap<K,V> ConcurrentHashMap(Class<K> parameterK, Class<V> parameterV, int initialCapacity, float loadFactor, int concurrencyLevel) {
		return new ConcurrentHashMap<K,V>(initialCapacity, loadFactor, concurrencyLevel);
	}
}