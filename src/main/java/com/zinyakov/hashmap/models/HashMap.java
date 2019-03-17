package com.zinyakov.hashmap.models;

import java.util.Arrays;
import static java.util.Arrays.stream;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

public class HashMap<K,V> implements Map<K, V> {
    private Bucket<K, V>[] buckets;

    public HashMap(int capacity) {
        this.buckets = new Bucket[capacity];

        for(int i = 0; i < capacity; i++){
            this.buckets[i] = new Bucket<>(new LinkedList<>());
        }
    }

    @Override
    public int size() {
        return stream(buckets)
                .map(bucket -> bucket.getEntryList().size())
                .reduce((x,y) -> x + y)
                .orElse(0);
    }

    @Override
    public boolean isEmpty() {
        return this.size() == 0;
    }

    @Override
    public boolean containsKey(Object key) {
        return getEntry(key) != null;
    }

    @Override
    public boolean containsValue(Object value) {
        return false;
    }

    @Override
    public V get(Object key) {
        Entry<K, V> matchingEntry = getEntry(key);
        return matchingEntry == null ? null : matchingEntry.getValue();
    }

    @Override
    public V put(K key, V value) {
        Entry<K, V> matchingEntry = getEntry(key);

        if (matchingEntry == null) {
            Bucket<K, V> bucket = findBucketForKey(key);
            bucket.getEntryList().add(new EntryModel<>(key, value));
            return null;
        }

        V oldValue = matchingEntry.getValue();
        matchingEntry.setValue(value);
        return oldValue;
    }

    @Override
    public boolean remove(Object key, Object value) {
        if (value != getEntry(key).getValue()) {
            return false;
        }
        return removeEntry(key);
    }

    @Override
    public V remove(Object key) {
        V oldValue = getEntry(key).getValue();
        removeEntry(key);
        return oldValue;
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        m.entrySet().forEach(entry -> put(entry.getKey(), entry.getValue()));
    }

    @Override
    public void clear() {
        Arrays.stream(buckets)
              .map(Bucket::getEntryList)
              .forEach(List::clear);
    }

    @Override
    public Set<K> keySet() {
        return entrySet().stream()
                         .map(Entry::getKey)
                         .collect(toSet());
    }

    @Override
    public Collection<V> values() {
        return entrySet().stream()
                         .map(Entry::getValue)
                         .collect(toList());
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        return Arrays.stream(buckets)
                     .map(Bucket::getEntryList)
                     .flatMap(List::stream)
                     .collect(toSet());
    }

    private Entry<K, V> getEntry(Object key) {
        Bucket<K, V> bucket = findBucketForKey(key);

        return bucket.getEntryList()
                     .stream()
                     .filter(entry -> key.equals(entry.getKey()))
                     .findFirst()
                     .orElse(null);
    }

    private boolean removeEntry(Object key) {
        Entry<K, V> matchingEntry = getEntry(key);
        Bucket<K, V> bucket = findBucketForKey(key);
        return bucket.getEntryList().remove(matchingEntry);
    }

    private Bucket<K, V> findBucketForKey(Object key) {
        int bucketNumber = key.hashCode() % buckets.length;
        return buckets[bucketNumber];
    }

}
