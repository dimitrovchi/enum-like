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
package org.dimitrovchi.enumlike.benchmarks;

import java.util.Random;
import java.util.concurrent.TimeUnit;
import org.dimitrovchi.enumlike.AbstractTypedEnumMap;
import org.dimitrovchi.enumlike.ConcurrentTypedEnumMap;
import org.dimitrovchi.enumlike.DefaultEnumMapKeyContainer;
import org.dimitrovchi.enumlike.EnumMapKeyContainer;
import org.dimitrovchi.enumlike.TypedEnumMap;
import org.dimitrovchi.enumlike.base.TypedMap;
import org.dimitrovchi.enumlike.collections.HashTypedMap;
import org.dimitrovchi.enumlike.collections.IdentityHashTypedMap;
import org.dimitrovchi.enumlike.collections.SkipListTypedMap;
import org.dimitrovchi.enumlike.collections.TreeTypedMap;
import org.dimitrovchi.enumlike.data.TestCommonsEnumMapKey;
import org.dimitrovchi.enumlike.data.TestEnumMapKey;
import org.dimitrovchi.enumlike.data.TestEnums;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.CompilerControl;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OperationsPerInvocation;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.OptionsBuilder;

/**
 * Benchmark 1.
 * 
 * @author Dmitry Ovchinnikov
 */
@State(Scope.Benchmark)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Warmup(iterations = 3, batchSize = GetSemiFilledBenchmark.BATCH)
@Measurement(iterations = 5, batchSize = GetSemiFilledBenchmark.BATCH)
@Fork(3)
@OperationsPerInvocation(70 * GetSemiFilledBenchmark.BATCH)
@SuppressWarnings("unchecked")
public class GetSemiFilledBenchmark {

    public static final int BATCH = 10_000;
    private static final int CAPACITY = 70;
    private static final EnumMapKeyContainer<TestEnumMapKey> CONTAINER = 
            new DefaultEnumMapKeyContainer<>(TestEnumMapKey.class, TestEnums.class);
    
    private static final TestEnumMapKey[] KEYS = 
            CONTAINER.getElements().toArray(new TestEnumMapKey[CAPACITY]);
    
    private static final TypedMap ENUM_MAP = new TypedEnumMap(CONTAINER);
    private static final TypedMap C_ENUM_MAP = new ConcurrentTypedEnumMap(CONTAINER);
    private static final TypedMap HASH_TYPED_MAP = new HashTypedMap(CAPACITY);
    private static final TypedMap I_HASH_TYPED_MAP = new IdentityHashTypedMap(CAPACITY);
    private static final TypedMap TREE_TYPED_MAP = new TreeTypedMap();
    private static final TypedMap SKIPLIST_TYPED_MAP = new SkipListTypedMap();
    
    private static final TestCommonsEnumMapKey[] STR_KEYS = new TestCommonsEnumMapKey[CAPACITY];
    
    private static final TypedMap STR_HASH_MAP = new HashTypedMap(CAPACITY);
    private static final TypedMap I_STR_HASH_MAP = new IdentityHashTypedMap(CAPACITY);
    
    static {
        final Random random = new Random(1000L);
        for (int i = 0; i < CAPACITY; i++) {
            final String k = Integer.toString(random.nextInt(1_000_000), Character.MAX_RADIX) + i;
            final TestCommonsEnumMapKey<Integer> key = new TestCommonsEnumMapKey(k, Integer.class, 0);
            STR_KEYS[i] = key;
            if (i % 2 == 0) {
                STR_HASH_MAP.put(key, random.nextInt());
                I_STR_HASH_MAP.put(key, random.nextInt());
            }
        }
        for (final TestEnumMapKey k : CONTAINER.getElements()) {
            if (k.ordinal() % 2 == 0) {
                ENUM_MAP.put(k, random.nextInt());
                C_ENUM_MAP.put(k, random.nextInt());
                HASH_TYPED_MAP.put(k, random.nextInt());
                I_HASH_TYPED_MAP.put(k, random.nextInt());
                TREE_TYPED_MAP.put(k, random.nextInt());
                SKIPLIST_TYPED_MAP.put(k, random.nextInt());
            }
        }
    }
        
    @Benchmark
    public void getFromEnumMap() throws Exception {
        get(ENUM_MAP);
    }
    
    @Benchmark
    public void getFromCEnumMap() throws Exception {
        get(C_ENUM_MAP);
    }
    
    @Benchmark
    public void getFromHashMap() throws Exception {
        get(HASH_TYPED_MAP);
    }
    
    @Benchmark
    public void getFromIHashMap() throws Exception {
        get(I_HASH_TYPED_MAP);
    }
    
    @Benchmark
    public void getFromTreeMap() throws Exception {
        get(TREE_TYPED_MAP);
    }
    
    @Benchmark
    public void getFromSkipListMap() throws Exception {
        get(SKIPLIST_TYPED_MAP);
    }
    
    @Benchmark
    public void getFromStrHashMap() throws Exception {
        getStr(STR_HASH_MAP);
    }
    
    @Benchmark
    public void getFromStrIHashMap() throws Exception {
        getStr(I_STR_HASH_MAP);
    }
    
    private void get(TypedMap map) {
        for (int i = 0; i < CAPACITY; i++) {
            sink((Integer) map.get(KEYS[i]));
        }
    }
    
    private void getStr(TypedMap map) {
        for (int i = 0; i < CAPACITY; i++) {
            sink((Integer) map.get(STR_KEYS[i]));
        }
    }
    
    @CompilerControl(CompilerControl.Mode.DONT_INLINE)
    public static void sink(Integer value) {
    }
    
    public static void main(String... args) throws Exception {
        new Runner(new OptionsBuilder()
                .jvmArgsAppend("-XX:+UseG1GC", "-D" + AbstractTypedEnumMap.FAST_KEY)
                .include(GetSemiFilledBenchmark.class.getSimpleName())
                .build()).run();
    }
}
