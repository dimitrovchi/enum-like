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
package org.dimitrovchi.enumlike.base;

import java.util.List;
import javax.annotation.Nonnull;

/**
 * Typed key container.
 * 
 * @param <E> Key type.
 * 
 * @author Dmitry Ovchinnikov
 */
public interface TypedKeyContainer<E extends TypedKey<?>> {
    
    /**
     * Get elements.
     * @return Typed key elements.
     */
    @Nonnull
    List<E> getElements();
    
    /**
     * Get element class.
     * @return Element class.
     */
    @Nonnull
    Class<E> getElementClass();
}
