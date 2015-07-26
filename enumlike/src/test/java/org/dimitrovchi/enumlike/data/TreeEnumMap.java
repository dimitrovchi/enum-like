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
package org.dimitrovchi.enumlike.data;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import org.dimitrovchi.enumlike.collections.TypedKey;
import org.dimitrovchi.enumlike.collections.TypedMap;

/**
 * Tree enum map.
 * 
 * @author Dmitry Ovchinnikov
 */
public class TreeEnumMap implements TypedMap {
    
    private final Map<TypedKey<?>, Object> map = new TreeMap<>();

    @Override
    public boolean containsKey(TypedKey<?> key) {
        return map.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return map.containsValue(value);
    }

    @Override
    public <K extends TypedKey<V>, V> V get(K key) {
        return key.getType().cast(map.get(key));
    }

    @Override
    public <K extends TypedKey<V>, V> V put(K key, V value) {
        return key.getType().cast(map.put(key, value));
    }

    @Override
    public <K extends TypedKey<V>, V> V remove(K key) {
        return key.getType().cast(map.remove(key));
    }

    @Override
    public void clear() {
        map.clear();
    }

    @Override
    public int size() {
        return map.size();
    }

    @Override
    public Set<TypedKey<?>> keySet() {
        return map.keySet();
    }

    @Override
    public Collection<?> values() {
        return map.values();
    }

    @Override
    public Set<? extends Map.Entry<? extends TypedKey<?>, ?>> entrySet() {
        return map.entrySet();
    }
}
