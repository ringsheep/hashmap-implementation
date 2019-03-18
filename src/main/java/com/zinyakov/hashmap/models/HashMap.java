package com.zinyakov.hashmap.models;

import static java.util.Arrays.stream;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

public class HashMap<K,V> implements Map<K, V> {
    private static final int DEFAULT_CAPACITY = 16;

    private Bucket<K, V>[] buckets;

    public HashMap() {
        this(DEFAULT_CAPACITY);
    }

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
        return keySet().contains(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return values().contains(value);
    }

    @Override
    public V get(Object key) {
        Optional<Entry<K, V>> matchingEntry = getEntry(key);
        return matchingEntry.isPresent() ? matchingEntry.get().getValue() : null;
    }

    @Override
    public V put(K key, V value) {
        Optional<Entry<K, V>> matchingEntry = getEntry(key);

        if (!matchingEntry.isPresent()) {
            Bucket<K, V> bucket = findBucketForKey(key);
            bucket.getEntryList().add(new EntryModel<>(key, value));
            return null;
        }

        V oldValue = matchingEntry.get().getValue();
        matchingEntry.get().setValue(value);
        return oldValue;
    }

    @Override
    public boolean remove(Object key, Object value) {
        Optional<Map.Entry<K, V>> entry = getEntry(key);
        if (!entry.isPresent()) {
            return false;
        }
        if (value != entry.get().getValue()) {
            return false;
        }

        return removeEntry(key);
    }

    @Override
    public V remove(Object key) {
        Optional<Map.Entry<K, V>> entry = getEntry(key);
        if (!entry.isPresent()) {
            return null;
        }

        V oldValue = entry.get().getValue();
        removeEntry(key);
        return oldValue;
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        m.entrySet().forEach(entry -> put(entry.getKey(), entry.getValue()));
    }

    @Override
    public void clear() {
        stream(buckets).map(Bucket::getEntryList)
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
                         .filter(Objects::nonNull)
                         .collect(toList());
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        return stream(buckets)
                .map(Bucket::getEntryList)
                .flatMap(List::stream)
                .collect(toSet());
    }

    private Optional<Entry<K, V>> getEntry(Object key) {
        Bucket<K, V> bucket = findBucketForKey(key);

        return bucket.getEntryList()
                     .stream()
                     .filter(entry -> key.equals(entry.getKey()))
                     .findFirst();
    }

    private boolean removeEntry(Object key) {
        Optional<Map.Entry<K, V>> entry = getEntry(key);
        if (!entry.isPresent()) {
            return false;
        }
        Bucket<K, V> bucket = findBucketForKey(key);
        return bucket.getEntryList().remove(entry.get());
    }

    private Bucket<K, V> findBucketForKey(Object key) {
        int bucketNumber = key.hashCode() % buckets.length;
        return buckets[bucketNumber];
    }

}
