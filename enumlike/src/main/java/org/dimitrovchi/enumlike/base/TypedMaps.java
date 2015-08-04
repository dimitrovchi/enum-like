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
package org.dimitrovchi.enumlike.base;

import java.util.Map;

/**
 * Typed maps.
 * 
 * @author Dmitry Ovchinnikov
 */
public class TypedMaps {
    
    public static TypedMap synchronizedMap(TypedMap map) {
        return new SynchronizedTypedMap(map);
    }
    
    static class SynchronizedTypedMap implements TypedMap {
        
        protected final TypedMap delegate;
        
        public SynchronizedTypedMap(TypedMap delegate) {
            this.delegate = delegate;
        }

        @Override
        public synchronized boolean containsKey(TypedKey<?> key) {
            return delegate.containsKey(key);
        }

        @Override
        public synchronized boolean containsValue(Object value) {
            return delegate.containsValue(value);
        }

        @Override
        public synchronized <K extends TypedKey<V>, V> V get(K key) {
            return delegate.get(key);
        }

        @Override
        public synchronized <K extends TypedKey<V>, V> V put(K key, V value) {
            return delegate.put(key, value);
        }

        @Override
        public synchronized <K extends TypedKey<V>, V> V remove(K key) {
            return delegate.remove(key);
        }

        @Override
        public synchronized void clear() {
            delegate.clear();
        }

        @Override
        public synchronized int size() {
            return delegate.size();
        }

        @Override
        public synchronized Map<? extends TypedKey<?>, ?> toMap() {
            return delegate.toMap();
        }
    }
}
