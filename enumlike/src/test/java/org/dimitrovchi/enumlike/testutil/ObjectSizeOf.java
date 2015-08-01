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
package org.dimitrovchi.enumlike.testutil;

import java.util.Objects;
import net.sf.ehcache.pool.Size;
import net.sf.ehcache.pool.sizeof.AgentSizeOf;
import net.sf.ehcache.pool.sizeof.ReflectionSizeOf;
import net.sf.ehcache.pool.sizeof.SizeOf;
import net.sf.ehcache.pool.sizeof.UnsafeSizeOf;
import net.sf.ehcache.pool.sizeof.filter.SizeOfFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Object sizeOf.
 * 
 * @author Dmitry Ovchinnikov
 */
public class ObjectSizeOf extends SizeOf {
    
    private static final Logger LOG = LoggerFactory.getLogger(ObjectSizeOf.class);
    
    private final SizeOf sizeOf;
    
    public ObjectSizeOf(SizeOfFilter sizeOfFilter, boolean caching) {
        super(sizeOfFilter, caching);
        SizeOf so;
        try {
            so = new AgentSizeOf(sizeOfFilter, caching);
        } catch (Exception x) {
            LOG.warn("Size-of agent is not available", x);
            try {
                so = new UnsafeSizeOf(sizeOfFilter, caching);
            } catch (Exception y) {
                LOG.warn("Unsafe size-of agent is not available", y);
                so = new ReflectionSizeOf(sizeOfFilter, caching);
            }
        }
        sizeOf = Objects.requireNonNull(so);
    }

    @Override
    public Size deepSizeOf(int maxDepth, boolean abortWhenMaxDepthExceeded, Object... obj) {
        return sizeOf.deepSizeOf(maxDepth, abortWhenMaxDepthExceeded, obj);
    }

    @Override
    public long sizeOf(Object obj) {
        return sizeOf.sizeOf(obj);
    }

    @Override
    public String toString() {
        return sizeOf.toString();
    }
}
