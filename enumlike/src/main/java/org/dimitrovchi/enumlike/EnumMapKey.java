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

import org.dimitrovchi.enumlike.base.DefaultValueSupplier;
import org.dimitrovchi.enumlike.base.DefaultValue;
import org.dimitrovchi.enumlike.base.TypedKey;
import javax.annotation.Nonnull;

/**
 * Enum-like map key.
 * 
 * @param <V> Value type.
 * 
 * @author Dmitry Ovchinnikov
 */
public abstract class EnumMapKey<V> extends Enum implements TypedKey<V> {
    
    private final Class<V> type;
    private final DefaultValueSupplier<V> defaultValueSupplier;
    
    /**
     * Constructs an enum-map key.
     * 
     * @param type Value type.
     * @param defaultValueSupplier Default value supplier.
     */
    public EnumMapKey(@Nonnull Class<V> type, @Nonnull DefaultValueSupplier<V> defaultValueSupplier) {
        this.type = type;
        this.defaultValueSupplier = defaultValueSupplier;
    }
        
    /**
     * Constructs an enum-map key.
     * 
     * @param valueType Value type.
     */
    public EnumMapKey(@Nonnull Class<V> valueType) {
        this(valueType, DefaultValue.<V>nullValueSupplier());
    }

    /**
     * Get value type.
     * @return Value type.
     */
    @Override
    @Nonnull
    public Class<V> getType() {
        return type;
    }
    
    /**
     * Get default value supplier.
     * 
     * @return Default value supplier.
     */
    @Override
    @Nonnull
    public DefaultValueSupplier<V> getDefaultValueSupplier() {
        return defaultValueSupplier;
    }
}
