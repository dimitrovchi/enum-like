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
import org.dimitrovchi.enumlike.data.TestEnums;
import org.junit.Assert;
import org.junit.Test;

/**
 * DefaultEnumMapKeyContainer test.
 * 
 * @author Dmitry Ovchinnikov
 */
public class DefaultEnumMapKeyContainerTest {
    
    @Test
    public void testContainer() {
        final DefaultEnumMapKeyContainer<?> container = new DefaultEnumMapKeyContainer<>(TestEnumMapKey.class, TestEnums.class);
        Assert.assertEquals(70, container.getElements().size());
        Assert.assertEquals(69, container.getElements().size() - 1);
    }
}
