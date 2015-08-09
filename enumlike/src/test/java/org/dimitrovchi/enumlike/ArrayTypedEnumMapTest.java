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

import org.dimitrovchi.enumlike.data.TestEnumMapKey;
import org.junit.Assert;
import org.junit.Test;

/**
 * Array typed enum map test.
 *
 * @author Dmitry Ovchinnikov
 */
public class ArrayTypedEnumMapTest {

    @Test
    public void testPut() {
        final ArrayTypedEnumMap map = new ArrayTypedEnumMap();
        map.put(Enums.K1, 1);
        Assert.assertEquals(1, map.size());
        Assert.assertEquals(1, (int) map.get(Enums.K1));
        map.put(Enums.K6, 10);
        Assert.assertEquals(2, map.size());
        Assert.assertEquals(10, (int) map.get(Enums.K6));
        map.put(Enums.K3, 20);
        Assert.assertEquals(3, map.size());
        Assert.assertEquals(20, (int) map.get(Enums.K3));
        map.put(Enums.K4, 23);
        Assert.assertEquals(4, map.size());
        Assert.assertEquals(23, (int) map.get(Enums.K4));
        System.out.println(map.toMap());
    }

    public interface Enums {

        Key K1 = new Key();
        Key K2 = new Key();
        Key K3 = new Key();
        Key K4 = new Key();
        Key K5 = new Key();
        Key K6 = new Key();
    }

    public static class Key extends TestEnumMapKey<Integer> {

        public Key() {
            super(Integer.class);
        }
    }
}
