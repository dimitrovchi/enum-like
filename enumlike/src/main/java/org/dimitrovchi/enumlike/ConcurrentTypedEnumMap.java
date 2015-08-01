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

import java.util.concurrent.atomic.AtomicReferenceArray;

/**
 * Concurrent typed enum map.
 * 
 * @author Dmitry Ovchinnikov
 */
public class ConcurrentTypedEnumMap extends AbstractArrayBasedTypedEnumMap {
    
    private final AtomicReferenceArray<Object> values;

    public ConcurrentTypedEnumMap(EnumMapKeyContainer<? extends EnumMapKey> enumContainer) {
        super(enumContainer);
        this.values = new AtomicReferenceArray<>(enumContainer.getMaxOrdinal() + 1);
    }

    @Override
    public boolean containsValue(Object value) {
        for (int i = 0; i < values.length(); i++) {
            final Object v = values.get(i);
            if (v != null && v.equals(value)) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected int fullSize() {
        return values.length();
    }

    @Override
    protected Object get(int ordinal) {
        return values.get(ordinal);
    }

    @Override
    protected Object set(int ordinal, Object value) {
        return values.getAndSet(ordinal, value);
    }
}
