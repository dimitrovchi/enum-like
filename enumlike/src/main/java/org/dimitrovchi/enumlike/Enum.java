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
import java.lang.reflect.Modifier;
import java.util.concurrent.atomic.AtomicInteger;
import javax.annotation.Nonnull;

/**
 * Enum-like class.
 *
 * @author Dmitry Ovchinnikov
 */
public abstract class Enum {

    private static final ClassResolver CLASS_RESOLVER = new ClassResolver();
    private static final ClassValue<AtomicInteger> ENUM_COUNTER = new ClassValue<AtomicInteger>() {
        @Override
        protected AtomicInteger computeValue(Class<?> type) {
            return new AtomicInteger();
        }
    };

    private final int ordinal;
    private final Class<?> caller;
    private String name;

    protected Enum() {
        Class<?> c = getClass();
        final Class<?>[] stack = CLASS_RESOLVER.getClassContext();
        for (int i = stack.length - 1; i >= 0; i--) {
            if (stack[i] == c && i < stack.length - 1) {
                c = stack[i + 1];
                break;
            }
        }
        caller = c;
        ordinal = ENUM_COUNTER.get(getEnumClass()).getAndIncrement();
    }

    /**
     * Get the associated enum class.
     *
     * @return Enum class.
     */
    @Nonnull
    public final Class<? extends Enum> getEnumClass() {
        final Class<? extends Enum> c = getClass();
        return c.isAnonymousClass() ? c.getSuperclass().asSubclass(Enum.class) : c;
    }

    /**
     * Get enum-like instance name.
     *
     * @return Enum-like instance name.
     */
    @Nonnull
    public String name() {
        if (name == null) {
            for (final Field field : caller.getFields()) {
                if (isEnumField(field, getEnumClass())) {
                    try {
                        if (field.get(null) == this) {
                            return name = field.getName();
                        }
                    } catch (ReflectiveOperationException x) {
                        throw new IllegalStateException("Unable to enumerate enum-like instances for " + caller, x);
                    }
                }
            }
            throw new IllegalStateException("Unable to find " + caller.getCanonicalName() + ":" + ordinal);
        }
        return name;
    }

    static boolean isEnumField(@Nonnull Field field, @Nonnull Class<? extends Enum> type) {
        try {
            if ((field.getModifiers() & (Modifier.PUBLIC | Modifier.STATIC | Modifier.FINAL)) != 0) {
                final Enum e = (Enum) field.get(null);
                return e != null && e.getEnumClass() == type;
            } else {
                return false;
            }
        } catch (ReflectiveOperationException x) {
            return false;
        }
    }

    /**
     * Get enum-like instance ordinal value.
     *
     * @return Enum-like instance ordinal value.
     */
    public final int ordinal() {
        return ordinal;
    }

    @Override
    public String toString() {
        return name();
    }

    private static class ClassResolver extends SecurityManager {

        @Override
        protected Class[] getClassContext() {
            return super.getClassContext();
        }
    }
}
