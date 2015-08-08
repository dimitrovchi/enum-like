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
import javax.annotation.Nonnull;
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
        final int index = binarySearch(keys, key);
        return index >= 0 ? key.getType().cast(values[index]) : null;
    }

    @Override
    public <K extends TypedKey<V>, V> V put(K key, V value) {
        if (value == null) {
            return remove(key);
        } else {
            int index = binarySearch(keys, key);
            if (index >= 0) {
                final Object old = values[index];
                values[index] = value;
                return key.getType().cast(old);
            } else {
                index = -(index + 1);
                final int n = keys.length;
                keys = Arrays.copyOf(keys, n + 1);
                values = Arrays.copyOf(values, n + 1);
                keys[index] = key;
                values[index] = value;
                if (index != n) {
                    System.arraycopy(keys, index, keys, index + 1, n - index);
                    System.arraycopy(values, index, values, index + 1, n - index);
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
            if (index != n) {
                System.arraycopy(keys, index + 1, keys, index, n - index);
                System.arraycopy(values, index + 1, values, index, n - index);
            }
            keys = Arrays.copyOf(keys, n);
            values = Arrays.copyOf(values, n);
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
    
    private static int binarySearch(@Nonnull Object[] a, @Nonnull Object key) {
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
