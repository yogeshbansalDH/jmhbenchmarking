package com.example.benchmarking;

import com.google.common.base.Splitter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

@BenchmarkMode(Mode.SingleShotTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Measurement(batchSize = 100000, iterations = 10)
@Warmup(batchSize = 100000, iterations = 10)
@State(Scope.Thread)
public class SplitUtil {

  public static final CharSequence KEY_SEQUENCE = "DJ_CZ:a0ep:24731808:brandName:haribo";
  public static final String KEY = "DJ_CZ:a0ep:24731808:brandName:haribo";
  public static final Character SEPARATOR = ':';
  public static final String STRING_SEPARATOR = ":";
  public static final Pattern PATTERN = Pattern.compile(":+");

  @Benchmark
  public String[] split() {
    return KEY.split(STRING_SEPARATOR, -1);
  }

  @Benchmark
  public String[] splitter() {
    return Splitter.on(SEPARATOR).splitToList(KEY_SEQUENCE).toArray(new String[0]);
  }

  @Benchmark
  public String[] splitUsingIndexOf() {
    int pos = 0;
    int end;
    List<String> list = new ArrayList<>();
    while ((end = KEY.indexOf(SEPARATOR, pos)) >= 0) {
      list.add(KEY.substring(pos, end));
      pos = end + 1;
    }
    list.add(KEY.substring(pos));
    return list.toArray(new String[0]);
  }

  @Benchmark
  public String[] splitUsingPreCompiledRegexPattern(){
    return PATTERN.split(KEY);
  }

  @Benchmark
  public String[] splitUsingIndexOfWithoutList() {
    String[] output = new String[5];
    int pos = 0;
    int end;
    int index = 0;
    while ((end = KEY.indexOf(SEPARATOR, pos)) >= 0) {
      output[index++] = KEY.substring(pos, end);
      pos = end + 1;
    }
    output[index]  = KEY.substring(pos);
    return output;
  }

  public static void main(String[] args) throws Exception {
    Options options =
        new OptionsBuilder()
            .include(SplitUtil.class.getSimpleName())
            .threads(1)
            .forks(1)
            .shouldFailOnError(true)
            .shouldDoGC(true)
            .build();
    new Runner(options).run();
  }
}
