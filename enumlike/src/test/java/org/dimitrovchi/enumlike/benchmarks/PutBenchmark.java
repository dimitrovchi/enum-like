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

import java.util.concurrent.TimeUnit;
import org.dimitrovchi.enumlike.AbstractTypedEnumMap;
import org.dimitrovchi.enumlike.ArrayTypedEnumMap;
import org.dimitrovchi.enumlike.ConcurrentTypedEnumMap;
import org.dimitrovchi.enumlike.DefaultEnumMapKeyContainer;
import org.dimitrovchi.enumlike.EnumMapKeyContainer;
import org.dimitrovchi.enumlike.TypedEnumMap;
import org.dimitrovchi.enumlike.collections.HashTypedMap;
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
 * Put benchmark.
 *
 * @author Dmitry Ovchinnikov
 */
@State(Scope.Benchmark)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Warmup(iterations = 3, batchSize = PutBenchmark.BATCH)
@Measurement(iterations = 5, batchSize = PutBenchmark.BATCH)
@Fork(3)
@OperationsPerInvocation(PutBenchmark.CAPACITY * PutBenchmark.BATCH)
@SuppressWarnings("unchecked")
public class PutBenchmark {

    public static final int BATCH = 10_000;
    public static final int CAPACITY = 70;
    private static final EnumMapKeyContainer<TestEnumMapKey> CONTAINER
            = new DefaultEnumMapKeyContainer<>(TestEnumMapKey.class, TestEnums.class);
    private static final TestEnumMapKey[] KEYS
            = CONTAINER.getElements().toArray(new TestEnumMapKey[CAPACITY]);

    private final Blackhole bh = new Blackhole();

    @Benchmark
    public void putToAMap() {
        final ArrayTypedEnumMap map = new ArrayTypedEnumMap();
        for (int i = 0; i < CAPACITY; i++) {
            bh.consume(map.put(KEYS[i], i));
        }
    }

    @Benchmark
    public void putToHashMap() {
        final HashTypedMap map = new HashTypedMap(32);
        for (int i = 0; i < CAPACITY; i++) {
            bh.consume(map.put(KEYS[i], i));
        }
    }

    @Benchmark
    public void putToTreeMap() {
        final TreeTypedMap map = new TreeTypedMap();
        for (int i = 0; i < CAPACITY; i++) {
            bh.consume(map.put(KEYS[i], i));
        }
    }

    @Benchmark
    public void putToSkipListMap() {
        final SkipListTypedMap map = new SkipListTypedMap();
        for (int i = 0; i < CAPACITY; i++) {
            bh.consume(map.put(KEYS[i], i));
        }
    }

    @Benchmark
    public void putToEnumMap() {
        final TypedEnumMap map = new TypedEnumMap(CONTAINER);
        for (int i = 0; i < CAPACITY; i++) {
            bh.consume(map.put(KEYS[i], i));
        }
    }

    @Benchmark
    public void putToCEnumMap() {
        final ConcurrentTypedEnumMap map = new ConcurrentTypedEnumMap(CONTAINER);
        for (int i = 0; i < CAPACITY; i++) {
            bh.consume(map.put(KEYS[i], i));
        }
    }

    public static void main(String... args) throws Exception {
        new Runner(new OptionsBuilder()
                .jvmArgsAppend("-D" + AbstractTypedEnumMap.FAST_KEY)
                .include(PutBenchmark.class.getSimpleName())
                .build()).run();
    }
}
