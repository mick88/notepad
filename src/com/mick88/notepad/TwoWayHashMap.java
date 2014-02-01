package com.mick88.notepad;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class TwoWayHashMap<K,V> extends HashMap<K, V> implements TwoWayMap<K, V>
{
	private static final long serialVersionUID = 1L;
	
	/**
	 * Maps values to keys
	 */
	private final Map<V, K> keys;
	
	public static <K, V> Map<V, K> inverseMap(Map<K, V> map)
	{
		Map<V, K> result = new HashMap<V, K>(map.size());
		for (Map.Entry<K, V> entry : map.entrySet())
			result.put(entry.getValue(), entry.getKey());
		return result;
	}
	
	public TwoWayHashMap()
	{
		super();
		keys =  new HashMap<V, K>();
	}

	public TwoWayHashMap(int capacity, float loadFactor)
	{
		super(capacity, loadFactor);
		keys =  new HashMap<V, K>(capacity, loadFactor);
	}



	public TwoWayHashMap(int capacity)
	{
		super(capacity);
		keys =  new HashMap<V, K>(capacity);
	}

	public TwoWayHashMap(Map<? extends K, ? extends V> map)
	{
		super(map);
		keys =  new HashMap<V, K>(inverseMap(map));
	}


	public K getKeyOf(V value)
	{
		return keys.get(value);
	}

	@Override
	public boolean containsValue(Object value)
	{
		return keys.containsKey(value);
	}

	@Override
	public V put(K key, V value)
	{
		if (keys.containsKey(value))
			remove(keys.get(value));
		keys.put(value, key);
		return super.put(key, value);
	}

	@Override
	public void putAll(Map<? extends K, ? extends V> map)
	{
		keys.putAll(inverseMap(map));
		super.putAll(map);
	}

	@Override
	public V remove(Object key)
	{
		keys.remove(get(key));
		return super.remove(key);
	}

	@Override
	public Collection<V> values()
	{
		return keys.keySet();
	}

	
}
