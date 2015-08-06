/*
 * Copyright 2015 Dmitry Ovchinnikov.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.dimitrovchi.enumlike;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import org.dimitrovchi.enumlike.base.TypedKey;
import org.dimitrovchi.enumlike.base.TypedMap;

/**
 * Identity typed enum map.
 * 
 * @author Dmitry Ovchinnikov
 */
public class ArrayTypedEnumMap implements TypedMap {
        
    private TypedKey<?>[] keys = new TypedKey<?>[0];
    private Object[] values = new Object[0];

    @Override
    public boolean containsKey(TypedKey<?> key) {
        return binarySearch(keys, key) >= 0;
    }

    @Override
    public boolean containsValue(Object value) {
        if (value == null) {
            return false;
        }
        for (final Object v : values) {
            if (value.equals(v)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public <K extends TypedKey<V>, V> V get(K key) {
        try {
            return key.getType().cast(values[binarySearch(keys, key)]);
        } catch (IndexOutOfBoundsException x) {
            return null;
        }
    }

    @Override
    public <K extends TypedKey<V>, V> V put(K key, V value) {
        if (value == null) {
            return remove(key);
        } else {
            final int index = binarySearch(keys, key);
            if (index >= 0) {
                final Object old = values[index];
                values[index] = value;
                return key.getType().cast(old);
            } else {
                final int ip = -(index + 1);
                final int n = size();
                if (n == 0) {
                    keys = new TypedKey<?>[] {key};
                    values = new Object[] {value};
                } else if (ip == n) {
                    keys = Arrays.copyOf(keys, n + 1);
                    values = Arrays.copyOf(values, n + 1);
                    keys[n] = key;
                    values[n] = value;
                } else {
                    keys = Arrays.copyOf(keys, n + 1);
                    values = Arrays.copyOf(values, n + 1);
                    System.arraycopy(keys, ip, keys, ip + 1, n - ip);
                    System.arraycopy(values, ip, values, ip + 1, n - ip);
                    keys[ip] = key;
                    values[ip] = value;
                }
                return null;
            }
        }
    }

    @Override
    public <K extends TypedKey<V>, V> V remove(K key) {
        final int index = binarySearch(keys, key);
        if (index >= 0) {
            final Object old = values[index];
            final int n = size() - 1;
            if (index == n) {
                keys = Arrays.copyOf(keys, n);
                values = Arrays.copyOf(values, n);
            } else {
                System.arraycopy(keys, index + 1, keys, index, n - index);
                System.arraycopy(values, index + 1, values, index, n - index);
                keys = Arrays.copyOf(keys, n);
                values = Arrays.copyOf(values, n);
            }
            return key.getType().cast(old);
        } else {
            return null;
        }
    }

    @Override
    public void clear() {
        keys = new TypedKey<?>[0];
        values = new Object[0];
    }

    @Override
    public int size() {
        return keys.length;
    }

    @Override
    public Map<? extends TypedKey<?>, ?> toMap() {
        final Map<TypedKey<?>, Object> map = new LinkedHashMap<>(keys.length);
        for (int i = 0; i < keys.length; i++) {
            map.put((TypedKey<?>) keys[i], values[i]);
        }
        return map;
    }
    
    private static int binarySearch(Object[] a, Object key) {
        int low = 0;
        int high = a.length - 1;
        while (low <= high) {
            final int mid = (low + high) >>> 1;
            final int cmp = a[mid].hashCode() - key.hashCode();
            if (cmp < 0) {
                low = mid + 1;
            } else if (cmp > 0) {
                high = mid - 1;
            } else {
                return mid;
            }
        }
        return -(low + 1);
    }
}
