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

import net.sf.ehcache.pool.sizeof.annotations.IgnoreSizeOf;
import org.dimitrovchi.enumlike.EnumMapKey;
import org.dimitrovchi.enumlike.base.DefaultValueSupplier;

/**
 * Test enum map key.
 * 
 * @param <T> Value type.
 * 
 * @author Dmitry Ovchinnikov
 */
@IgnoreSizeOf
public class TestEnumMapKey<T> extends EnumMapKey<T> implements Comparable<TestEnumMapKey<T>> {

    public TestEnumMapKey(Class<T> type, DefaultValueSupplier<T> defaultValueSupplier) {
        super(type, defaultValueSupplier);
    }

    public TestEnumMapKey(Class<T> valueType) {
        super(valueType);
    }

    @Override
    public int compareTo(TestEnumMapKey<T> o) {
        return Integer.compare(ordinal(), o.ordinal());
    }
}
