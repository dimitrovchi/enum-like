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

import org.dimitrovchi.enumlike.base.TypedKey;

/**
 * Abstract array-based typed enum map.
 * 
 * @author Dmitry Ovchinnikov
 */
public abstract class AbstractArrayBasedTypedEnumMap extends AbstractTypedEnumMap {

    public AbstractArrayBasedTypedEnumMap(EnumMapKeyContainer<? extends EnumMapKey> enumContainer) {
        super(enumContainer);
    }

    @Override
    public boolean containsKey(TypedKey<?> key) {
        final EnumMapKey<?> k = key(key);
        try {
            return get(k.ordinal()) != null;
        } catch (IndexOutOfBoundsException x) {
            throw new IllegalArgumentException("Unknown enum key: " + key, x);
        }
    }

    @Override
    public <K extends TypedKey<V>, V> V get(K key) {
        final EnumMapKey<?> k = key(key);
        try {
            return key.getType().cast(get(k.ordinal()));
        } catch (IndexOutOfBoundsException x) {
            throw new IllegalArgumentException("Unknown enum key: " + key, x);
        }
    }

    @Override
    public <K extends TypedKey<V>, V> V put(K key, V value) {
        final EnumMapKey<?> k = key(key);
        try {
            return key.getType().cast(set(k.ordinal(), value));
        } catch (IndexOutOfBoundsException x) {
            throw new IllegalArgumentException("Unknown enum key: " + key, x);
        }
    }

    @Override
    public <K extends TypedKey<V>, V> V remove(K key) {
        return put(key, null);
    }
}
