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
package org.dimitrovchi.enumlike.collections;

import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import org.dimitrovchi.enumlike.EnumContainer;

/**
 * Typed enum map.
 *
 * @author Dmitry Ovchinnikov
 */
public class TypedEnumMap implements TypedMap {

    private final Object[] values;
    private final EnumContainer<? extends AbstractEnumMapKey<?>> enumContainer;

    public TypedEnumMap(EnumContainer<? extends AbstractEnumMapKey<?>> enumContainer) {
        this.enumContainer = enumContainer;
        this.values = new Object[enumContainer.getMaxOrdinal() + 1];
    }

    @Override
    public boolean containsKey(TypedKey<?> key) {
        final AbstractEnumMapKey k = enumContainer.getEnumClass().cast(key);
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
        final AbstractEnumMapKey k = key(key);
        return key.getType().cast(getByKey(k));
    }

    @Override
    public <K extends TypedKey<V>, V> V put(K key, V value) {
        final AbstractEnumMapKey k = key(key);
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
    public Set<? extends AbstractEnumMapKey<?>> keySet() {
        return new AbstractSet<AbstractEnumMapKey<?>>() {
            @Override
            public Iterator<AbstractEnumMapKey<?>> iterator() {
                final AtomicInteger c = new AtomicInteger();
                return new Iterator<AbstractEnumMapKey<?>>() {
                    @Override
                    public boolean hasNext() {
                        for (int i = c.get(); i < values.length; i++) {
                            if (values[i] != null) {
                                return true;
                            }
                        }
                        return false;
                    }

                    @Override
                    public AbstractEnumMapKey<?> next() {
                        for (int i = c.get(); i < values.length; i++) {
                            if (values[i] != null) {
                                c.set(i);
                                return enumContainer.getEnums().get(i);
                            }
                        }
                        throw new NoSuchElementException();
                    }

                    @Override
                    public void remove() {
                        values[c.get()] = null;
                    }
                };
            }

            @Override
            public int size() {
                return TypedEnumMap.this.size();
            }

            @Override
            public void clear() {
                TypedEnumMap.this.clear();
            }
        };
    }

    @Override
    public Collection<?> values() {
        return Arrays.asList(values);
    }

    @Override
    public Set<? extends Map.Entry<? extends AbstractEnumMapKey<?>, ?>> entrySet() {
        return new AbstractSet<Map.Entry<? extends AbstractEnumMapKey<?>, ?>>() {
            @Override
            public Iterator<Map.Entry<? extends AbstractEnumMapKey<?>, ?>> iterator() {
                final AtomicInteger c = new AtomicInteger();
                return new Iterator<Map.Entry<? extends AbstractEnumMapKey<?>, ?>>() {
                    @Override
                    public boolean hasNext() {
                        for (int i = c.get(); i < values.length; i++) {
                            if (values[i] != null) {
                                return true;
                            }
                        }
                        return false;
                    }

                    @Override
                    public Map.Entry<? extends AbstractEnumMapKey<?>, ?> next() {
                        for (int i = c.get(); i < values.length; i++) {
                            final Object v = values[i];
                            if (v != null) {
                                c.set(i);
                                final AbstractEnumMapKey<?> k = enumContainer.getEnums().get(i);
                                return new AbstractMap.SimpleImmutableEntry<>(k, v);
                            }
                        }
                        throw new NoSuchElementException();
                    }

                    @Override
                    public void remove() {
                        values[c.get()] = null;
                    }
                };
            }

            @Override
            public int size() {
                return TypedEnumMap.this.size();
            }
            
            @Override
            public void clear() {
                TypedEnumMap.this.clear();
            }
        };
    }

    private AbstractEnumMapKey key(TypedKey<?> key) {
        try {
            return enumContainer.getEnumClass().cast(key);
        } catch (ClassCastException x) {
            throw new IllegalArgumentException("Unknown key: " + key, x);
        }
    }

    private Object getByKey(AbstractEnumMapKey key) {
        try {
            return values[key.ordinal()];
        } catch (ArrayIndexOutOfBoundsException x) {
            throw new IllegalArgumentException("Unknown key: " + key, x);
        }
    }

    private Object setByKey(AbstractEnumMapKey key, Object value) {
        try {
            final Object old = values[key.ordinal()];
            values[key.ordinal()] = value;
            return old;
        } catch (ArrayIndexOutOfBoundsException x) {
            throw new IllegalArgumentException("Unknown key: " + key, x);
        }
    }
}
