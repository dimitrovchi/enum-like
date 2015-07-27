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

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import javax.annotation.Nonnull;

/**
 * Reflection-based enum provider.
 * 
 * @param <E> Enum type.
 * 
 * @author Dmitry Ovchinnikov
 */
public class ReflectionEnumContainer<E extends Enum> implements EnumContainer<E> {
    
    private final List<E> enums;
    private final Class<E> enumClass;
    private final int maxOrdinal;
    
    public ReflectionEnumContainer(@Nonnull Class<E> enumClass, @Nonnull Class<?>... containerClasses) {
        this.enumClass = enumClass;
        final List<E> enumList = new ArrayList<>();
        for (final Class<?> c : containerClasses) {
            for (final Field f : c.getFields()) {
                if (Enum.isEnumField(f, enumClass)) {
                    try {
                        enumList.add(enumClass.cast(f.get(null)));
                    } catch (ReflectiveOperationException x) {
                        throw new IllegalStateException("Unable to enumerate enum fields: " + enumClass.getName(), x);
                    }
                }
            }
        }
        if (enumList.isEmpty()) {
            throw new IllegalArgumentException("Container classes " + Arrays.asList(containerClasses) + " don't contain enums");
        }
        enums = Collections.unmodifiableList(enumList);
        int max = 0;
        for (final E e : enumList) {
            if (e.ordinal() > max) {
                max = e.ordinal();
            }
        }
        maxOrdinal = max;
    }

    @Override
    public List<E> getEnums() {
        return enums;
    }

    @Override
    public Class<E> getEnumClass() {
        return enumClass;
    }

    @Override
    public int getMaxOrdinal() {
        return maxOrdinal;
    }

    @Override
    public String toString() {
        return String.format("%s(%s, %s)", getClass().getSimpleName(), enumClass.getName(), enums);
    }
}
