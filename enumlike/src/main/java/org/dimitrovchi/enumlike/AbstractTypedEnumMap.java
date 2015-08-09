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

import java.util.LinkedHashMap;
import java.util.Map;
import javax.annotation.Nonnull;
import org.dimitrovchi.enumlike.base.TypedKey;
import org.dimitrovchi.enumlike.base.TypedMap;

/**
 * Abstract typed enum map.
 *
 * @author Dmitry Ovchinnikov
 */
public abstract class AbstractTypedEnumMap implements TypedMap {

    public static final String FAST_KEY = "TypedEnumMap.fast";
    private static final boolean FAST = System.getProperties().containsKey(FAST_KEY);

    protected final EnumMapKeyContainer<? extends EnumMapKey> enumContainer;

    public AbstractTypedEnumMap(@Nonnull EnumMapKeyContainer<? extends EnumMapKey> enumContainer) {
        this.enumContainer = enumContainer;
    }

    protected abstract int fullSize();

    protected final EnumMapKey<?> key(TypedKey<?> key) {
        try {
            if (FAST) {
                return enumContainer.getElementClass().cast(key);
            } else {
                final EnumMapKey<?> k = (EnumMapKey<?>) key;
                if (k.getEnumClass() != enumContainer.getElementClass()) {
                    throw new ClassCastException(k.getEnumClass().getName());
                }
                return k;
            }
        } catch (ClassCastException x) {
            throw new IllegalArgumentException("Unknown key: " + key, x);
        }
    }

    protected abstract Object get(int ordinal);

    protected abstract Object set(int ordinal, Object value);

    @Override
    public int size() {
        final int n = fullSize();
        int size = 0;
        for (int i = 0; i < n; i++) {
            if (get(i) != null) {
                size++;
            }
        }
        return size;
    }

    @Override
    public void clear() {
        for (int i = 0; i < fullSize(); i++) {
            set(i, null);
        }
    }

    @Override
    public Map<? extends TypedKey<?>, ?> toMap() {
        final Map<TypedKey<?>, Object> map = new LinkedHashMap<>(size());
        final int n = fullSize();
        for (int i = 0; i < n; i++) {
            final Object v = get(i);
            if (v != null) {
                final EnumMapKey<?> k = enumContainer.getElements().get(i);
                map.put(k, v);
            }
        }
        return map;
    }
}
