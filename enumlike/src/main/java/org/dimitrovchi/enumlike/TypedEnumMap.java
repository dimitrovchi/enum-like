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

import org.dimitrovchi.enumlike.base.TypedMap;
import org.dimitrovchi.enumlike.base.TypedKey;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Typed enum map.
 *
 * @author Dmitry Ovchinnikov
 */
public class TypedEnumMap implements TypedMap {

    private final Object[] values;
    private final EnumMapKeyContainer<? extends EnumMapKey<?>> enumContainer;

    public TypedEnumMap(EnumMapKeyContainer<? extends EnumMapKey<?>> enumContainer) {
        this.enumContainer = enumContainer;
        this.values = new Object[enumContainer.getMaxOrdinal() + 1];
    }

    @Override
    public boolean containsKey(TypedKey<?> key) {
        final EnumMapKey k = key(key);
        return values[k.ordinal()] != null;
    }

    @Override
    public boolean containsValue(Object value) {
        for (final Object v : values) {
            if (v != null && v.equals(value)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public <K extends TypedKey<V>, V> V get(K key) {
        final EnumMapKey k = key(key);
        return key.getType().cast(getByKey(k));
    }

    @Override
    public <K extends TypedKey<V>, V> V put(K key, V value) {
        final EnumMapKey k = key(key);
        final Object old = setByKey(k, value);
        return key.getType().cast(old);
    }

    @Override
    public <K extends TypedKey<V>, V> V remove(K key) {
        return put(key, null);
    }

    @Override
    public void clear() {
        Arrays.fill(values, null);
    }

    @Override
    public int size() {
        int size = 0;
        for (final Object v : values) {
            if (v != null) {
                size++;
            }
        }
        return size;
    }

    @Override
    public Map<? extends TypedKey<?>, ?> toMap() {
        final Map<TypedKey<?>, Object> map = new LinkedHashMap<>(size());
        for (int i = 0; i < values.length; i++) {
            final Object v = values[i];
            if (v != null) {
                final EnumMapKey<?> k = enumContainer.getElements().get(i);
                map.put(k, v);
            }
        }
        return map;
    }

    private EnumMapKey key(TypedKey<?> key) {
        try {
            return enumContainer.getElementClass().cast(key);
        } catch (ClassCastException x) {
            throw new IllegalArgumentException("Unknown key: " + key, x);
        }
    }

    private Object getByKey(EnumMapKey key) {
        try {
            return values[key.ordinal()];
        } catch (ArrayIndexOutOfBoundsException x) {
            throw new IllegalArgumentException("Unknown key: " + key, x);
        }
    }

    private Object setByKey(EnumMapKey key, Object value) {
        try {
            final Object old = values[key.ordinal()];
            values[key.ordinal()] = value;
            return old;
        } catch (ArrayIndexOutOfBoundsException x) {
            throw new IllegalArgumentException("Unknown key: " + key, x);
        }
    }
}
