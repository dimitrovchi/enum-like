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
import java.util.concurrent.atomic.AtomicInteger;
import org.dimitrovchi.enumlike.collections.TypedMap;
import org.dimitrovchi.enumlike.data.HashEnumMap;
import org.dimitrovchi.enumlike.data.IdentityEnumMap;
import org.dimitrovchi.enumlike.data.TestCommonsEnumMapKey;
import org.dimitrovchi.enumlike.data.TreeEnumMap;
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

    private static final AtomicInteger COUNTER = new AtomicInteger();

    private final MemoryMeter memory = new MemoryMeter();
    private final TypedMap map;
    private final int capacity;
    private final int size;
    private final List<String> keys = new ArrayList<>(System.getProperties().stringPropertyNames());

    @Parameterized.Parameters
    public static Collection<Object[]> parameters() {
        final List<Object[]> parameters = new ArrayList<>();
        for (final int capacity : new int[] {16, 32, 64, 128, 256}) {
            for (final int size : new int[] {2, 4, 8, 16, 32, 64, 128, 256, 512, 3, 5, 10, 55, 300}) {
                parameters.add(new Object[] {new HashEnumMap(capacity), capacity, size});
                parameters.add(new Object[] {new IdentityEnumMap(capacity), capacity, size});
                parameters.add(new Object[] {new TreeEnumMap(), capacity, size});
            }
        }
        return parameters;
    }

    public TypedMapMemoryTest(TypedMap map, int capacity, int size) {
        this.map = map;
        this.capacity = capacity;
        this.size = size;
        for (int i = 0; i < keys.size(); i++) {
            keys.set(i, keys.get(i) + "_" + capacity + "_" + size + "_" + COUNTER.getAndIncrement());
        }
        final int delta = size - keys.size();
        for (int i = 0; i < delta; i++) {
            keys.add(map.getClass().getCanonicalName() + "_" + capacity + "_" + size + "_" + i);
        }
    }
    
    @BeforeClass
    public static void init() {
        System.out.println("Map_Name\tCap\tSize\tAmount");
    }

    @Test
    public void testMapMemoryConsumption() {
        for (int i = 0; i < size; i++) {
            map.put(new TestCommonsEnumMapKey<>(keys.get(i), Long.class, 100L), 300L);
        }
        System.out.printf("%s\t%d\t%d\t%d%n", 
                map.getClass().getSimpleName(), 
                capacity, 
                size, 
                memory.measureDeep(map));
    }
}
