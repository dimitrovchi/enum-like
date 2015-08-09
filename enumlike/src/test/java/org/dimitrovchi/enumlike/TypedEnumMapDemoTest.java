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

import com.google.common.collect.ImmutableSet;
import java.math.BigDecimal;
import java.util.stream.Collectors;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * TypedEnumMap demo test.
 *
 * @author Dmitry Ovchinnikov
 */
public class TypedEnumMapDemoTest {

    /**
     * Enum-like typed key.
     *
     * We need different enum-key classes for each TypedEnumMap to provide ordinal-based enum
     * uniqueness.
     *
     * @param <T> Key type.
     */
    public static class MyKey<T> extends EnumMapKey<T> {

        public MyKey(Class<T> type) {
            super(type);
        }
    }

    /**
     * Enum-like class.
     */
    public interface MyEnum {

        MyKey<Integer> ENUM_INT_KEY = new MyKey<>(Integer.class);
        MyKey<String> ENUM_STR_KEY = new MyKey<>(String.class);
        MyKey<BigDecimal> ENUM_BD_KEY = new MyKey<BigDecimal>(BigDecimal.class) {
            @Override
            public String name() {
                return "K";
            }
        };
    }

    /**
     * Enum container to construct TypedEnumMap instances.
     */
    public static final EnumMapKeyContainer<MyKey> CONTAINER =
            new DefaultEnumMapKeyContainer<>(MyKey.class, MyEnum.class);

    @Test
    public void testEnumContainer() {
        assertEquals(3, CONTAINER.getElements().size());
        assertEquals(ImmutableSet.of("ENUM_INT_KEY", "ENUM_STR_KEY", "K"),
                CONTAINER.getElements().stream().map(EnumMapKey::name).collect(Collectors.toSet()));
        assertEquals(MyKey.class, CONTAINER.getElementClass());
        assertEquals(CONTAINER.getElements().size() - 1, CONTAINER.getMaxOrdinal());
    }

    @Test
    public void testTypedEnumMap() {
        final TypedEnumMap map = new TypedEnumMap(CONTAINER);
        map.put(MyEnum.ENUM_INT_KEY, 1);
        //map.put(MyEnum.ENUM_INT_KEY, "x");  // compile error
        map.put(MyEnum.ENUM_BD_KEY, BigDecimal.TEN);
        assertEquals(1, map.get(MyEnum.ENUM_INT_KEY).intValue());
        assertEquals(10, map.get(MyEnum.ENUM_BD_KEY).intValue());
        assertNull(map.get(MyEnum.ENUM_STR_KEY));
    }
}
