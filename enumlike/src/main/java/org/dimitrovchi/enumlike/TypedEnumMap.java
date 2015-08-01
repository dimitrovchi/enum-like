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

import java.util.Arrays;

/**
 * Typed enum map.
 *
 * @author Dmitry Ovchinnikov
 */
public class TypedEnumMap extends AbstractArrayBasedTypedEnumMap {

    private final Object[] values;

    public TypedEnumMap(EnumMapKeyContainer<? extends EnumMapKey> enumContainer) {
        super(enumContainer);
        this.values = new Object[enumContainer.getMaxOrdinal() + 1];
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
    public void clear() {
        Arrays.fill(values, null);
    }

    @Override
    protected int fullSize() {
        return values.length;
    }

    @Override
    protected Object get(int ordinal) {
        return values[ordinal];
    }

    @Override
    protected Object set(int ordinal, Object value) {
        final Object old = values[ordinal];
        values[ordinal] = value;
        return old;
    }
}
