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

import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;
import org.dimitrovchi.enumlike.base.TypedKey;
import org.dimitrovchi.enumlike.base.TypedMap;

/**
 * Tree enum map.
 * 
 * @author Dmitry Ovchinnikov
 */
public final class TreeTypedMap implements TypedMap {
    
    private final Map<TypedKey<?>, Object> map;
    
    public TreeTypedMap() {
        map = new TreeMap<>();
    }
    
    public TreeTypedMap(Comparator<? super TypedKey<?>> comparator) {
        map = new TreeMap<>(comparator);
    }

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
    public Map<? extends TypedKey<?>, ?> toMap() {
        return map;
    }
}
