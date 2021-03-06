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
import org.dimitrovchi.enumlike.ArrayTypedEnumMap;
import org.dimitrovchi.enumlike.ConcurrentTypedEnumMap;
import org.dimitrovchi.enumlike.DefaultEnumMapKeyContainer;
import org.dimitrovchi.enumlike.EnumMapKeyContainer;
import org.dimitrovchi.enumlike.TypedEnumMap;
import org.dimitrovchi.enumlike.base.TypedMap;
import org.dimitrovchi.enumlike.collections.HashTypedMap;
import org.dimitrovchi.enumlike.collections.IdentityHashTypedMap;
import org.dimitrovchi.enumlike.collections.SkipListTypedMap;
import org.dimitrovchi.enumlike.collections.TreeTypedMap;
import org.dimitrovchi.enumlike.data.TestEnumMapKey;
import org.dimitrovchi.enumlike.data.TestEnums;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OperationsPerInvocation;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.OptionsBuilder;

/**
 * Replace benchmark.
 *
 * @author Dmitry Ovchinnikov
 */
@State(Scope.Benchmark)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Warmup(iterations = 3, batchSize = ReplaceBenchmark.BATCH)
@Measurement(iterations = 5, batchSize = ReplaceBenchmark.BATCH)
@Fork(3)
@OperationsPerInvocation(ReplaceBenchmark.CAPACITY * ReplaceBenchmark.BATCH)
@SuppressWarnings("unchecked")
public class ReplaceBenchmark {

    public static final int BATCH = 10_000;
    public static final int CAPACITY = 70;
    private static final EnumMapKeyContainer<TestEnumMapKey> CONTAINER
            = new DefaultEnumMapKeyContainer<>(TestEnumMapKey.class, TestEnums.class);

    private static final TestEnumMapKey[] KEYS
            = CONTAINER.getElements().toArray(new TestEnumMapKey[CAPACITY]);

    private static final TypedMap ENUM_MAP = new TypedEnumMap(CONTAINER);
    private static final TypedMap C_ENUM_MAP = new ConcurrentTypedEnumMap(CONTAINER);
    private static final TypedMap HASH_TYPED_MAP = new HashTypedMap(CAPACITY);
    private static final TypedMap I_HASH_TYPED_MAP = new IdentityHashTypedMap(CAPACITY);
    private static final TypedMap TREE_TYPED_MAP = new TreeTypedMap();
    private static final TypedMap SKIPLIST_TYPED_MAP = new SkipListTypedMap();
    private static final TypedMap A_MAP = new ArrayTypedEnumMap();

    private final Blackhole bh = new Blackhole();

    static {
        final Random random = new Random(0L);
        for (final TestEnumMapKey k : CONTAINER.getElements()) {
            ENUM_MAP.put(k, random.nextInt());
            C_ENUM_MAP.put(k, random.nextInt());
            HASH_TYPED_MAP.put(k, random.nextInt());
            I_HASH_TYPED_MAP.put(k, random.nextInt());
            TREE_TYPED_MAP.put(k, random.nextInt());
            SKIPLIST_TYPED_MAP.put(k, random.nextInt());
            A_MAP.put(k, random.nextInt());
        }
    }

    @Benchmark
    public void putToAMap() {
        for (int i = 0; i < CAPACITY; i++) {
            bh.consume(A_MAP.put(KEYS[i], i));
        }
    }

    @Benchmark
    public void putToEnumMap() {
        for (int i = 0; i < CAPACITY; i++) {
            bh.consume(ENUM_MAP.put(KEYS[i], i));
        }
    }

    @Benchmark
    public void putToCEnumMap() {
        for (int i = 0; i < CAPACITY; i++) {
            bh.consume(C_ENUM_MAP.put(KEYS[i], i));
        }
    }

    @Benchmark
    public void putToHashMap() {
        for (int i = 0; i < CAPACITY; i++) {
            bh.consume(HASH_TYPED_MAP.put(KEYS[i], i));
        }
    }

    @Benchmark
    public void putToIHashMap() {
        for (int i = 0; i < CAPACITY; i++) {
            bh.consume(I_HASH_TYPED_MAP.put(KEYS[i], i));
        }
    }

    @Benchmark
    public void putToTreeMap() {
        for (int i = 0; i < CAPACITY; i++) {
            bh.consume(TREE_TYPED_MAP.put(KEYS[i], i));
        }
    }

    @Benchmark
    public void putToSkipListMap() {
        for (int i = 0; i < CAPACITY; i++) {
            bh.consume(SKIPLIST_TYPED_MAP.put(KEYS[i], i));
        }
    }

    public static void main(String... args) throws Exception {
        new Runner(new OptionsBuilder()
                .jvmArgsAppend("-D" + AbstractTypedEnumMap.FAST_KEY)
                .include(ReplaceBenchmark.class.getSimpleName())
                .build()).run();
    }
}
