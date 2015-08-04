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
import java.util.Comparator;
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
    
    private static final Comparator<Object> COMPARATOR = new Comparator<Object>() {
        @Override
        public int compare(Object o1, Object o2) {
            final Object k1 = o1 instanceof Object[] ? ((Object[]) o1)[0] : o1;
            final Object k2 = o2 instanceof Object[] ? ((Object[]) o2)[0] : o2;
            return Integer.compare(k1.hashCode(), k2.hashCode());
        }
    };
    
    private Object[][] table = new Object[0][2];

    @Override
    public boolean containsKey(TypedKey<?> key) {
        return Arrays.binarySearch(table, key, COMPARATOR) >= 0;
    }

    @Override
    public boolean containsValue(Object value) {
        if (value == null) {
            return false;
        }
        for (final Object[] pair : table) {
            if (value.equals(pair[1])) {
                return true;
            }
        }
        return false;
    }

    @Override
    public <K extends TypedKey<V>, V> V get(K key) {
        try {
            return key.getType().cast(table[Arrays.binarySearch(table, key, COMPARATOR)][1]);
        } catch (IndexOutOfBoundsException x) {
            return null;
        }
    }

    @Override
    public <K extends TypedKey<V>, V> V put(K key, V value) {
        if (value == null) {
            return remove(key);
        } else {
            final int index = Arrays.binarySearch(table, key, COMPARATOR);
            if (index >= 0) {
                final Object old = table[index][1];
                table[index][1] = value;
                return key.getType().cast(old);
            } else {
                final int ip = -(index + 1);
                final int n = table.length;
                if (n == 0) {
                    table = new Object[1][2];
                    table[0][0] = key;
                    table[0][1] = value;
                } else if (ip == n) {
                    final Object[][] t = new Object[n + 1][2];
                    System.arraycopy(table, 0, t, 0, n);
                    t[n][0] = key;
                    t[n][1] = value;
                    table = t;
                } else {
                    final Object[][] t = new Object[n + 1][2];
                    System.arraycopy(table, 0, t, 0, ip);
                    t[ip][0] = key;
                    t[ip][1] = value;
                    System.arraycopy(table, ip, t, ip + 1, n - ip);
                    table = t;
                }
                return null;
            }
        }
    }

    @Override
    public <K extends TypedKey<V>, V> V remove(K key) {
        final int index = Arrays.binarySearch(table, key, COMPARATOR);
        if (index >= 0) {
            final Object old = table[index][1];
            final int n = table.length - 1;
            if (index == n) {
                table = Arrays.copyOf(table, n);
            } else {
                System.arraycopy(table, index + 1, table, index, n - index);
                table = Arrays.copyOf(table, n);
            }
            return key.getType().cast(old);
        } else {
            return null;
        }
    }

    @Override
    public void clear() {
        table = new Object[0][2];
    }

    @Override
    public int size() {
        return table.length;
    }

    @Override
    public Map<? extends TypedKey<?>, ?> toMap() {
        final Map<TypedKey<?>, Object> map = new LinkedHashMap<>(table.length);
        for (final Object[] pair : table) {
            map.put((TypedKey<?>) pair[0], pair[1]);
        }
        return map;
    }
}
