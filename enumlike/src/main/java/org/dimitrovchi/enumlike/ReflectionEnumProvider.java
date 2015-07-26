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
import java.util.List;
import javax.annotation.Nonnull;

/**
 * Reflection-based enum provider.
 * 
 * @author Dmitry Ovchinnikov
 */
public abstract class ReflectionEnumProvider extends EnumProvider {
    
    private final Class<?>[] classes;
    
    public ReflectionEnumProvider(@Nonnull Class<?>... classes) {
        this.classes = classes;
    }

    @Override
    protected List<? extends Enum> listEnums() {
        final List<Enum> enumList = new ArrayList<>();
        for (final Class<?> klass : classes) {
            for (final Field field : klass.getFields()) {
                if (Enum.isEnumField(field, Enum.class)) {
                    try {
                        enumList.add((Enum) field.get(null));
                    } catch (ReflectiveOperationException x) {
                        throw new IllegalStateException("Unable to enumerate enums for " + klass, x);
                    }
                }
            }
        }
        return enumList;
    }
}
