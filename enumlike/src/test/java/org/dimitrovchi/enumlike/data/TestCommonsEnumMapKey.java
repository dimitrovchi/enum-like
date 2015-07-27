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

import org.apache.commons.lang.enums.Enum;
import org.dimitrovchi.enumlike.base.DefaultValue;
import org.dimitrovchi.enumlike.base.DefaultValueSupplier;
import org.dimitrovchi.enumlike.base.TypedKey;

/**
 * Commons enum map test key.
 * 
 * @author Dmitry Ovchinnikov
 */
public class TestCommonsEnumMapKey<V> extends Enum implements TypedKey<V> {
    
    private final Class<V> type;
    private final DefaultValueSupplier<V> defaultValueSupplier;

    public TestCommonsEnumMapKey(String name, Class<V> type, V defaultValue) {
        super(name);
        this.type = type;
        this.defaultValueSupplier = new DefaultValue<>(defaultValue);
    }

    @Override
    public Class<V> getType() {
        return type;
    }

    @Override
    public String name() {
        return getName();
    }

    @Override
    public DefaultValueSupplier<V> getDefaultValueSupplier() {
        return defaultValueSupplier;
    }
}
