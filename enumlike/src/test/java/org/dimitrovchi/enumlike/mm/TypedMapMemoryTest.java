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

import org.dimitrovchi.enumlike.testutil.ObjectSizeOf;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import net.sf.ehcache.pool.sizeof.SizeOf;
import net.sf.ehcache.pool.sizeof.filter.AnnotationSizeOfFilter;
import org.apache.karaf.shell.table.HAlign;
import org.apache.karaf.shell.table.ShellTable;
import org.dimitrovchi.enumlike.ArrayTypedEnumMap;
import org.dimitrovchi.enumlike.ConcurrentTypedEnumMap;
import org.dimitrovchi.enumlike.DefaultEnumMapKeyContainer;
import org.dimitrovchi.enumlike.TypedEnumMap;
import org.dimitrovchi.enumlike.base.TypedMap;
import org.dimitrovchi.enumlike.collections.HashTypedMap;
import org.dimitrovchi.enumlike.collections.IdentityHashTypedMap;
import org.dimitrovchi.enumlike.collections.SkipListTypedMap;
import org.dimitrovchi.enumlike.collections.TreeTypedMap;
import org.dimitrovchi.enumlike.data.TestEnumMapKey;
import org.dimitrovchi.enumlike.data.TestEnumMapKeyContainer;
import org.dimitrovchi.enumlike.data.TestEnums;
import org.junit.AfterClass;
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

    private static final SizeOf SIZE_OF = new ObjectSizeOf(new AnnotationSizeOfFilter(), true);
    private static final ShellTable RESULTS = new ShellTable();
    private static final DefaultEnumMapKeyContainer<TestEnumMapKey> CONTAINER = new DefaultEnumMapKeyContainer<>(TestEnumMapKey.class, TestEnums.class);

    @Parameterized.Parameters
    public static Collection<Object[]> parameters() {
        final List<Object[]> parameters = new ArrayList<>();
        for (final int size : new int[] {7, 9, 16, 35, 50, 70}) {
            for (final int capacity : new int[] {16, 32, 64, 70}) {
                if (size <= capacity) {
                    parameters.add(new Object[] {new HashTypedMap(capacity), capacity, size});
                    parameters.add(new Object[] {new IdentityHashTypedMap(capacity), capacity, size});
                    final TestEnumMapKeyContainer container = new TestEnumMapKeyContainer(capacity);
                    parameters.add(new Object[] {new TypedEnumMap(container), capacity, size});
                    parameters.add(new Object[] {new ConcurrentTypedEnumMap(container), capacity, size});
                }
            }
            parameters.add(new Object[] {new TreeTypedMap(), 0, size});
            parameters.add(new Object[] {new SkipListTypedMap(), 0, size});
            parameters.add(new Object[] {new ArrayTypedEnumMap(), 0, size});
        }
        return parameters;
    }
    
    private final TypedMap map;
    private final int capacity;
    private final int size;

    public TypedMapMemoryTest(TypedMap map, int capacity, int size) {
        this.map = map;
        this.capacity = capacity;
        this.size = size;
    }

    @BeforeClass
    public static void init() {
        RESULTS.column("Map").align(HAlign.left);
        RESULTS.column("Capacity").align(HAlign.right);
        RESULTS.column("Size").align(HAlign.right);
        RESULTS.column("Amount").align(HAlign.right);
    }

    @AfterClass
    public static void results() {
        synchronized (System.out) {
            System.out.println();
            RESULTS.print(System.out);
            System.out.println();
        }
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testMapMemoryConsumption() {
        for (int i = 0; i < size; i++) {
            map.put(CONTAINER.getElements().get(i), 300);
        }
        RESULTS.addRow().addContent(
                map.getClass().getSimpleName(),
                capacity,
                size,
                SIZE_OF.deepSizeOf(Integer.MAX_VALUE, true, map).getCalculated()
        );
    }
}
