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
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import net.sf.ehcache.pool.sizeof.SizeOf;
import net.sf.ehcache.pool.sizeof.filter.AnnotationSizeOfFilter;
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
    private static final DefaultEnumMapKeyContainer<TestEnumMapKey> CONTAINER
            = new DefaultEnumMapKeyContainer<>(TestEnumMapKey.class, TestEnums.class);
    private static final List<Record> RECORDS = new ArrayList<>();

    @Parameterized.Parameters
    public static Collection<Object[]> parameters() {
        final List<Object[]> parameters = new ArrayList<>();
        for (final int size : new int[] {9, 50, 70}) {
            for (final int capacity : new int[] {16, 64, 70}) {
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

    @AfterClass
    public static void printStatistics() {
        final List<Class<?>> classes = RECORDS.stream()
                .map(r -> r.mapClass).distinct().collect(Collectors.toList());
        final int[] widths = new int[classes.size() + 2];
        Arrays.fill(widths, 10);
        final String format = Arrays.stream(widths)
                .mapToObj(w -> "%" + w + "s")
                .collect(Collectors.joining("|", "|", "|%n"));
        final Object[] header = new Object[widths.length];
        header[0] = "Capacity";
        header[1] = "Size";
        System.arraycopy(classes.stream()
                .map(c -> c.getSimpleName().substring(0, 2)).toArray(), 0, header, 2, classes.size());
        System.out.printf(format, header);
        System.out.printf(format, Arrays.stream(widths)
                .mapToObj(w -> {
                    final char[] chars = new char[w];
                    Arrays.fill(chars, '-');
                    return String.valueOf(chars);
                }).toArray());
        RECORDS.stream().mapToInt(r -> r.capacity).filter(capacity -> capacity != 0).sorted().distinct()
                .forEach(capacity -> RECORDS.stream().mapToInt(r -> r.size).filter(size -> size <= capacity).sorted().distinct()
                        .forEach(size -> {
                            final Object[] amounts = classes.stream().map(c -> RECORDS.stream()
                                    .filter(r -> r.mapClass == c && r.size == size && (r.capacity == 0 || r.capacity == capacity))
                                    .map(r -> r.amount)
                                    .findFirst().get()).toArray();
                            final Object[] row = new Object[widths.length];
                            row[0] = capacity;
                            row[1] = size;
                            System.arraycopy(amounts, 0, row, 2, amounts.length);
                            System.out.printf(format, row);
                        })
                );
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testMapMemoryConsumption() {
        for (int i = 0; i < size; i++) {
            map.put(CONTAINER.getElements().get(i), 300);
        }
        RECORDS.add(new Record(map.getClass(), capacity, size, SIZE_OF.deepSizeOf(Integer.MAX_VALUE, true, map).getCalculated()));
    }

    public static class Record {

        public final Class<?> mapClass;
        public final int capacity;
        public final int size;
        public final long amount;

        public Record(Class<?> mapClass, int capacity, int size, long amount) {
            this.mapClass = mapClass;
            this.capacity = capacity;
            this.size = size;
            this.amount = amount;
        }
    }
}
