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
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Typed map.
 *  
 * @author Dmitry Ovchinnikov
 */
public interface TypedMap {
    
    boolean containsKey(@Nonnull TypedKey<?> key);
    
    boolean containsValue(@Nullable Object value);
    
    <K extends TypedKey<V>, V> V get(@Nonnull K key);
    
    <K extends TypedKey<V>, V> V put(@Nonnull K key, V value);
        
    <K extends TypedKey<V>, V> V remove(@Nonnull K key);
    
    void clear();
    
    int size();
    
    Map<? extends TypedKey<?>, ?> toMap();
}
