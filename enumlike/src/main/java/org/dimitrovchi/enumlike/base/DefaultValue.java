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

/**
 * Basic DefaultValueSupplier implementation.
 * 
 * @param <V> Value type.
 * 
 * @author Dmitry Ovchinnikov
 */
public class DefaultValue<V> implements DefaultValueSupplier<V> {
    
    private static final DefaultValue<?> NULL_VALUE_SUPPLIER = new DefaultValue<>(null);
        
    private final V defaultValue;
    
    /**
     * Constructs a constant-base default value.
     * @param defaultValue Default value constant.
     */
    public DefaultValue(V defaultValue) {
        this.defaultValue = defaultValue;
    }

    @Override
    public V getDefaultValue() {
        return defaultValue;
    }
    
    /**
     * Get a singleton-based null value supplier.
     * @param <V> Value type.
     * @return Null value supplier.
     */
    @SuppressWarnings("unchecked")
    public static <V> DefaultValueSupplier<V> nullValueSupplier() {
        return (DefaultValue<V>) NULL_VALUE_SUPPLIER;
    }
}
