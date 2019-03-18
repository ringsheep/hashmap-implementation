package com.zinyakov.hashmap;

import com.zinyakov.hashmap.models.HashMap;
import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import org.junit.Test;

public class HashMapTest {

    @Test
    public void should_init_with_custom_capacity() {
        HashMap map = new HashMap<>(99999);

        assertNotNull(map);
    }

    @Test
    public void should_change_size() {
        HashMap<String, Integer> map = new HashMap<>();

        map.put("123", 123);
        map.put("345", 345);
        map.put("567", 567);

        assertEquals(3, map.size());
    }

    @Test
    public void should_contain_key_and_value() {
        HashMap<String, Integer> map = new HashMap<>();

        map.put("123", 123);

        assertTrue(map.containsKey("123"));
        assertTrue(map.containsValue(123));
    }

    @Test
    public void should_get_value_by_key() {
        Integer expectedValue = 123;
        HashMap<String, Integer> map = new HashMap<>();

        map.put("123", expectedValue);
        Integer value = map.get("123");

        assertEquals(expectedValue, value);
    }

    @Test
    public void should_not_get_value_by_non_existing_key() {
        HashMap<String, Integer> map = new HashMap<>();

        assertNull(map.get("123"));
    }

    @Test
    public void should_be_empty_after_init() {
        assertTrue(new HashMap<>().isEmpty());
    }

    @Test
    public void should_update_on_put() {
        Integer expectedValue = 345;
        String key = "123";
        HashMap<String, Integer> map = new HashMap<>();

        map.put(key, 123);
        map.put(key, expectedValue);

        assertEquals(expectedValue, map.get(key));
    }

    @Test
    public void should_put_all_from_map() {
        HashMap<String, Integer> inputMap = new HashMap<>();
        inputMap.put("123", 123);
        inputMap.put("345", 345);
        inputMap.put("567", 567);
        HashMap<String, Integer> map = new HashMap<>();

        map.putAll(inputMap);

        assertEquals(3, map.size());
    }

    @Test
    public void should_convert_to_keysset_and_values() {
        HashMap<String, Integer> map = new HashMap<>();

        map.put("123", 123);
        map.put("345", 345);
        map.put("567", null);

        assertEquals(3, map.keySet().size());
        assertEquals(2, map.values().size());
    }

    @Test
    public void should_clear_all_values() {
        HashMap<String, Integer> map = new HashMap<>();

        map.put("123", 123);
        map.put("345", 345);
        map.put("567", 567);
        map.clear();

        assertEquals(0, map.size());
    }

    @Test
    public void should_remove_values_by_key() {
        Integer expectedValue = 123;
        HashMap<String, Integer> map = new HashMap<>();

        map.put("123", expectedValue);
        Integer oldValue = map.remove("123");

        assertEquals(0, map.size());
        assertEquals(expectedValue, oldValue);
    }

    @Test
    public void should_remove_values_by_key_and_value() {
        Integer value = 123;
        HashMap<String, Integer> map = new HashMap<>();

        map.put("123", value);
        Boolean result = map.remove("123", value);

        assertEquals(0, map.size());
        assertTrue(result);
    }

    @Test
    public void should_not_remove_values_by_non_existing_key() {
        HashMap<String, Integer> map = new HashMap<>();

        Integer oldValue = map.remove("123");

        assertEquals(0, map.size());
        assertNull(oldValue);
    }

    @Test
    public void should_not_remove_values_by_key_and_non_existing_value() {
        HashMap<String, Integer> map = new HashMap<>();

        map.put("123", 123);
        Boolean result = map.remove("123", 345);

        assertEquals(1, map.size());
        assertFalse(result);
    }
}
