package com.mick88.notepad;

import java.util.Map;

public interface TwoWayMap<K,V> extends Map<K, V>
{
	K getKeyOf(V value);
}
