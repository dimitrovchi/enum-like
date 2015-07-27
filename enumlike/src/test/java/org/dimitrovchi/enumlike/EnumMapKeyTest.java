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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.dimitrovchi.enumlike.base.DefaultValue;
import org.dimitrovchi.enumlike.base.DefaultValueSupplier;
import org.junit.Assert;
import org.junit.Test;

/**
 * Enum map key test.
 *
 * @author Dmitry Ovchinnikov
 */
public class EnumMapKeyTest {

    @Test
    public void testOrdinals() {
        final TestKey[] enums = {TestEnumKeys2.KEY1, TestEnumKeys2.KEY2, TestEnumKeys2.KEY3};
        final List<String> keyNames = new ArrayList<>();
        for (final TestKey<?> key : enums) {
            keyNames.add(key.name());
        }
        Collections.sort(keyNames);
        Assert.assertEquals(Arrays.asList("KEY1", "KEY2", "KEY3"), keyNames);
    }

    interface TestEnumKeys {

        TestKey<Integer> KEY1 = new TestKey<>(Integer.class, new DefaultValue<>(0));
        TestKey<String> KEY2 = new TestKey<>(String.class);
    }

    interface TestEnumKeys2 extends TestEnumKeys {

        TestKey<String> KEY3 = new TestKey<>(String.class);
    }

    private static class TestKey<V> extends EnumMapKey<V> {

        public TestKey(Class<V> valueType, DefaultValueSupplier<V> defaultValueSupplier) {
            super(valueType, defaultValueSupplier);
        }

        public TestKey(Class<V> valueType) {
            super(valueType);
        }
    }
}
