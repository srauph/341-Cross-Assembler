package utils;

import java.util.HashMap;

/**
 * A symbol table utilizing a HashMap as its backend
 * Accepts any object type for Key and Value
 */
public class SymbolTable<K, V> {

    private final HashMap<K, V> map;

    public SymbolTable() {
        this.map = new HashMap<>();
    }

    public void put(K key, V value) {
        this.map.put(key, value);
    }

    public V get(K key) {
        return map.get(key);
    }

    public boolean containsKey(K key) {
        return map.containsKey(key);
    }

    public boolean containsValue(V value) {
        return map.containsValue(value);
    }

}