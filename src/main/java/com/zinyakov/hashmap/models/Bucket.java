package com.zinyakov.hashmap.models;

import java.util.List;
import java.util.Map;

class Bucket<K, V> {
    private List<Map.Entry<K, V>> entryList;

    Bucket(List<Map.Entry<K, V>> entryList) {
        this.entryList = entryList;
    }

    List<Map.Entry<K, V>> getEntryList() {
        return entryList;
    }
}
