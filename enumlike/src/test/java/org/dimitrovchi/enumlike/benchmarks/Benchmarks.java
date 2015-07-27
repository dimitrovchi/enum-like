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

import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.OptionsBuilder;

/**
 * Benchmarks runner.
 * 
 * @author Dmitry Ovchinnikov
 */
public class Benchmarks {
    
    public static void main(String... args) throws Exception {
        new Runner(new OptionsBuilder()
                .include(".+Benchmark")
                .build()).run();
    }
}