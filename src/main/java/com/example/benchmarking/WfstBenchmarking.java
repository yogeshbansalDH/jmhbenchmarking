package com.example.benchmarking;

import org.apache.lucene.search.suggest.Lookup;
import org.apache.lucene.search.suggest.fst.WFSTCompletionLookup;
import org.apache.lucene.store.MMapDirectory;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.SingleShotTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Measurement(batchSize = 100000, iterations = 10)
@Warmup(batchSize = 100000, iterations = 10)
@State(Scope.Thread)
public class WfstBenchmarking {

  private static final String KEY = "FP_BD:h9jp:4239584:spinach water:";
  private static final String SEPARATOR = ":";
  private static WFSTCompletionLookup singleLineLookup;
  private static WFSTCompletionLookup originalLookup;

    static {
    String temporaryDir = System.getProperty("java.io.tmpdir");
    MMapDirectory directory = null;
    try {
      directory = new MMapDirectory(Path.of(temporaryDir, "wfst"));

      singleLineLookup =
          new WFSTCompletionLookup(directory, UUID.randomUUID().toString().substring(0, 8));
      singleLineLookup.load(
          Files.newInputStream(Path.of("/Users/y.bansal/Desktop/wfst_one_line_per_product")));

      originalLookup =
          new WFSTCompletionLookup(directory, UUID.randomUUID().toString().substring(0, 8));
      originalLookup.load(
          Files.newInputStream(Path.of("/Users/y.bansal/Desktop/wfst_original")));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

    public static void main(String[] args) throws RunnerException,IOException {
        Options options =
                new OptionsBuilder()
                        .include(WfstBenchmarking.class.getSimpleName())
                        .threads(1)
                        .forks(1)
                        .shouldFailOnError(true)
                        .shouldDoGC(true)
                        .build();
        new Runner(options).run();
//        readCombinedWfstFiles();
//        readSeparateWfstFiles();
    }

  @Benchmark
  public Optional<QueryProductInteractionData> readSeparateWfstFiles() throws IOException {

    var queryProductInteractionDataBuilder = QueryProductInteractionData.builder();
    List<Lookup.LookupResult> results = originalLookup.lookup(KEY, false, 19);
    Optional<QueryProductInteractionData> queryProductInteractionDataFromMultipleRows =
        getQueryProductInteractionDataFromMultipleRows(queryProductInteractionDataBuilder, results);
    return queryProductInteractionDataFromMultipleRows;
  }

  private static Optional<QueryProductInteractionData> getQueryProductInteractionDataFromMultipleRows(
      QueryProductInteractionData.QueryProductInteractionDataBuilder
          queryProductInteractionDataBuilder,
      List<Lookup.LookupResult> results) {
    results.stream()
        .map(
            lookupResult -> {
              String[] split = lookupResult.key.toString().split(SEPARATOR, -1);
              return Optional.of(
                  QueryInteractionWFSTParameters.builder()
                      .globalEntityId(split[0])
                      .vendorId(split[1])
                      .productId(split[2])
                      .query(split[3])
                      .featureName(split[4])
                      .featureValues(Double.valueOf(split[5]))
                      .build());
            })
        .flatMap(Optional::stream)
        .forEach(
            queryInteractionWFSTParameters -> {
              var featureValues = queryInteractionWFSTParameters.getFeatureValues();
              switch (queryInteractionWFSTParameters.getFeatureName()) {
                case "atc_count_30d" -> queryProductInteractionDataBuilder.addToCartCount30Days(
                    featureValues.intValue());
                case "click_count_30d" -> queryProductInteractionDataBuilder.clickCount30Days(
                    featureValues.intValue());
                case "order_count_30d" -> queryProductInteractionDataBuilder.orderCount30Days(
                    featureValues.intValue());
                case "click_to_order_cvr_30d" -> queryProductInteractionDataBuilder
                    .clickToOrderCVR30Days(featureValues);
                case "click_to_atc_cvr_30d" -> queryProductInteractionDataBuilder
                    .clickToAddToCartCVR30Days(featureValues);

                case "atc_count_60d" -> queryProductInteractionDataBuilder.addToCartCount60Days(
                    featureValues.intValue());
                case "click_count_60d" -> queryProductInteractionDataBuilder.clickCount60Days(
                    featureValues.intValue());
                case "order_count_60d" -> queryProductInteractionDataBuilder.orderCount60Days(
                    featureValues.intValue());
                case "click_to_order_cvr_60d" -> queryProductInteractionDataBuilder
                    .clickToOrderCVR60Days(featureValues);
                case "click_to_atc_cvr_60d" -> queryProductInteractionDataBuilder
                    .clickToAddToCartCVR60Days(featureValues);

                case "atc_count_90d" -> queryProductInteractionDataBuilder.addToCartCount90Days(
                    featureValues.intValue());
                case "click_count_90d" -> queryProductInteractionDataBuilder.clickCount90Days(
                    featureValues.intValue());
                case "order_count_90d" -> queryProductInteractionDataBuilder.orderCount90Days(
                    featureValues.intValue());
                case "click_to_order_cvr_90d" -> queryProductInteractionDataBuilder
                    .clickToOrderCVR90Days(featureValues);
                case "click_to_atc_cvr_90d" -> queryProductInteractionDataBuilder
                    .clickToAddToCartCVR90Days(featureValues);

                case "master_click_count_90d" -> queryProductInteractionDataBuilder
                    .masterClickCount90Days(featureValues.intValue());
                case "product_search_count_90d" -> queryProductInteractionDataBuilder
                    .productSearchCount90Days(featureValues.intValue());
                case "master_click_to_atc_cvr_90d" -> queryProductInteractionDataBuilder
                    .masterClickToAtcCvr90days(featureValues.intValue());

                case "order_count_21d" -> queryProductInteractionDataBuilder.orderCount21Days(
                    featureValues.intValue());
                case "master_order_count_21d" -> queryProductInteractionDataBuilder
                    .masterOrderCount21Days(featureValues.intValue());
                case "atc_count_21d" -> queryProductInteractionDataBuilder.atcCount21Days(
                    featureValues.intValue());
                case "master_atc_count_21d" -> queryProductInteractionDataBuilder
                    .masterAtcCount21Days(featureValues.intValue());

                case "master_atc_count_15d" -> queryProductInteractionDataBuilder
                    .masterAtcCount15Days(featureValues.intValue());
                case "atc_count_15d" -> queryProductInteractionDataBuilder.atcCount15Days(
                    featureValues.intValue());
                case "order_count_15d" -> queryProductInteractionDataBuilder.orderCount15Days(
                    featureValues.intValue());
                case "master_order_count_15d" -> queryProductInteractionDataBuilder
                    .masterOrderCount15Days(featureValues.intValue());

                case "atc_count_7d" -> queryProductInteractionDataBuilder.atcCount7Days(
                    featureValues.intValue());
                case "master_atc_count_7d" -> queryProductInteractionDataBuilder
                    .masterAtcCount7Days(featureValues.intValue());
                case "order_count_7d" -> queryProductInteractionDataBuilder.orderCount7Days(
                    featureValues.intValue());
                case "master_order_count_7d" -> queryProductInteractionDataBuilder
                    .masterOrderCount7Days(featureValues.intValue());

                case "order_count_3d" -> queryProductInteractionDataBuilder.orderCount3Days(
                    featureValues.intValue());
                case "master_order_count_3d" -> queryProductInteractionDataBuilder
                    .masterOrderCount3Days(featureValues.intValue());
                case "atc_count_3d" -> queryProductInteractionDataBuilder.atcCount3Days(
                    featureValues.intValue());
                case "master_atc_count_3d" -> queryProductInteractionDataBuilder
                    .masterAtcCount3Days(featureValues.intValue());
              }
            });
    return Optional.of(queryProductInteractionDataBuilder.build());
  }

  @Benchmark
  public Optional<QueryProductInteractionData> readCombinedWfstFiles() throws IOException {
    List<Lookup.LookupResult> lookup = singleLineLookup.lookup(KEY, false, 1);
    return lookup.stream()
        .findFirst()
        .map(
            lookupResult -> {
              String[] split = lookupResult.key.toString().split(SEPARATOR, -1);
              var queryProductInteractionDataBuilder = QueryProductInteractionData.builder();
              queryProductInteractionDataBuilder.addToCartCount30Days(Integer.parseInt(split[16]));
              queryProductInteractionDataBuilder.clickCount30Days(Integer.parseInt(split[14]));
              queryProductInteractionDataBuilder.orderCount30Days(Integer.parseInt(split[15]));
              queryProductInteractionDataBuilder.clickToOrderCVR30Days(Double.parseDouble(split[10]));
              queryProductInteractionDataBuilder.clickToAddToCartCVR30Days(
                      Double.parseDouble(split[13]));

              queryProductInteractionDataBuilder.addToCartCount60Days(Integer.parseInt(split[19]));
              queryProductInteractionDataBuilder.clickCount60Days(Integer.parseInt(split[17]));
              queryProductInteractionDataBuilder.orderCount60Days(Integer.parseInt(split[18]));
              queryProductInteractionDataBuilder.clickToOrderCVR60Days(Double.parseDouble(split[9]));
              queryProductInteractionDataBuilder.clickToAddToCartCVR60Days(
                      Double.parseDouble(split[12]));

              queryProductInteractionDataBuilder.addToCartCount90Days(Integer.parseInt(split[22]));
              queryProductInteractionDataBuilder.clickCount90Days(Integer.parseInt(split[21]));
              queryProductInteractionDataBuilder.orderCount90Days(Integer.parseInt(split[20]));
              queryProductInteractionDataBuilder.clickToOrderCVR90Days(Double.parseDouble(split[8]));
              queryProductInteractionDataBuilder.clickToAddToCartCVR90Days(
                      Double.parseDouble(split[11]));

              queryProductInteractionDataBuilder.masterClickCount90Days(Integer.parseInt(split[6]));
              queryProductInteractionDataBuilder.masterOrderCount7Days(Integer.parseInt(split[7]));
              queryProductInteractionDataBuilder.productSearchCount90Days(
                  Integer.parseInt(split[5]));
              queryProductInteractionDataBuilder.masterClickToAtcCvr90days(
                  Double.parseDouble(split[4]));

              return queryProductInteractionDataBuilder.build();
            });
  }
}
