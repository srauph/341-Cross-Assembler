package utils;

import java.util.HashMap;

public interface ISymbolTable<K, V> {
    void put(K key, V value);

    V get(K key);

    boolean containsKey(K key);

    boolean containsValue(V value);

    HashMap<K, V> getMap();
}
