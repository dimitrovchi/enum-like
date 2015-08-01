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
import java.util.Comparator;
import java.util.TreeSet;
import java.util.stream.IntStream;
import org.dimitrovchi.enumlike.data.TestEnumMapKey;
import org.dimitrovchi.enumlike.data.TestEnums;
import org.junit.Assert;
import org.junit.Test;

/**
 * Enum map key test.
 *
 * @author Dmitry Ovchinnikov
 */
public class EnumMapKeyTest {

    @Test
    public void testOrdinals() throws Exception {
        final TreeSet<TestEnumMapKey<?>> set = new TreeSet<>(Comparator.comparing(k -> k.ordinal()));
        for (final Field field : TestEnums.class.getFields()) {
            set.add((TestEnumMapKey<?>) field.get(null));
        }
        final int[] ordinals = set.stream().mapToInt(TestEnumMapKey::ordinal).toArray();
        final int[] expectedOrdinals = IntStream.range(0, set.size()).toArray();
        Assert.assertArrayEquals(expectedOrdinals, ordinals);
    }
}
