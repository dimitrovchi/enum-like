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
package org.dimitrovchi.enumlike.mm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.dimitrovchi.enumlike.base.TypedMap;
import org.dimitrovchi.enumlike.collections.HashTypedMap;
import org.dimitrovchi.enumlike.collections.IdentityHashTypedMap;
import org.dimitrovchi.enumlike.collections.SkipListTypedMap;
import org.dimitrovchi.enumlike.data.TestCommonsEnumMapKey;
import org.dimitrovchi.enumlike.collections.TreeTypedMap;
import org.github.jamm.MemoryMeter;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

/**
 * IdentityHashMap memory test.
 *
 * @author Dmitry Ovchinnikov
 */
@RunWith(Parameterized.class)
public class TypedMapMemoryTest {

    private static final MemoryMeter MEMORY = new MemoryMeter()
            .ignoreKnownSingletons()
            .ignoreNonStrongReferences()
            .withGuessing(MemoryMeter.Guess.FALLBACK_UNSAFE);
    private static final List<String> KEYS = new ArrayList<>(System.getProperties().stringPropertyNames());
    private static final List<TestCommonsEnumMapKey<Long>> TYPED_KEYS = new ArrayList<>();

    static {
        final int delta = 1024 - KEYS.size();
        for (int i = 0; i < delta; i++) {
            KEYS.add(KEYS.get(i % KEYS.size()) + "_" + "key_" + Math.pow(i, 3));
        }
        KEYS.forEach(key -> TYPED_KEYS.add(new TestCommonsEnumMapKey<>(key, Long.class, 100L)));
    }

    private final TypedMap map;
    private final int capacity;
    private final int size;

    @Parameterized.Parameters
    public static Collection<Object[]> parameters() {
        final List<Object[]> parameters = new ArrayList<>();
        for (final int size : new int[] {2, 3, 4, 9, 16, 35, 100, 270}) {
            for (final int capacity : new int[] {16, 32, 64, 128}) {
                parameters.add(new Object[] {new HashTypedMap(capacity), capacity, size});
                parameters.add(new Object[] {new IdentityHashTypedMap(capacity), capacity, size});
            }
            parameters.add(new Object[] {new TreeTypedMap(), 0, size});
            parameters.add(new Object[] {new SkipListTypedMap(), 0, size});
        }
        return parameters;
    }

    public TypedMapMemoryTest(TypedMap map, int capacity, int size) {
        this.map = map;
        this.capacity = capacity;
        this.size = size;
    }

    @BeforeClass
    public static void init() {
        System.out.println("Map_Name\tCap\tSize\tAmount");
    }

    @Test
    public void testMapMemoryConsumption() {
        for (int i = 0; i < size; i++) {
            map.put(TYPED_KEYS.get(i), 300L);
        }
        final int[] cps = map.getClass().getSimpleName().codePoints()
                .filter(Character::isUpperCase).toArray();
        System.out.printf("%s\t%d\t%d\t%d%n",
                new String(cps, 0, cps.length),
                capacity,
                size,
                MEMORY.measureDeep(map));
    }
}
