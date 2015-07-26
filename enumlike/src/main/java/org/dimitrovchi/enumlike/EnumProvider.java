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

import java.util.List;
import java.util.ServiceLoader;
import javax.annotation.Nonnull;

/**
 * Enum provider.
 * 
 * @author Dmitry Ovchinnikov
 */
public abstract class EnumProvider {
        
    static {
        for (final EnumProvider enumProvider : ServiceLoader.load(EnumProvider.class)) {
            enumProvider.listEnums(); // initialize enum instances explicitly
        }
    }
        
    /**
     * Lists all enums for this provider.
     * @return All the enum instances for this provider.
     */
    @Nonnull
    protected abstract List<? extends Enum> listEnums();    
}
