package com.example.benchmarking;

import lombok.Builder;
import lombok.Builder.Default;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;

@Value
@Builder
@Slf4j
public class QueryProductInteractionData {

  @Default int addToCartCount30Days = 0;
  @Default int clickCount30Days = 0;
  @Default int orderCount30Days = 0;
  @Default double clickToOrderCVR30Days = 0.0;
  @Default double clickToAddToCartCVR30Days = 0.0;

  @Default int addToCartCount60Days = 0;
  @Default int clickCount60Days = 0;
  @Default int orderCount60Days = 0;
  @Default double clickToOrderCVR60Days = 0.0;
  @Default double clickToAddToCartCVR60Days = 0.0;

  @Default int addToCartCount90Days = 0;
  @Default int clickCount90Days = 0;
  @Default int orderCount90Days = 0;
  @Default double clickToOrderCVR90Days = 0.0;
  @Default double clickToAddToCartCVR90Days = 0.0;
  @Default int masterClickCount90Days = 0;
  @Default int productSearchCount90Days = 0;
  @Default double masterClickToAtcCvr90days = 0;

  @Default int orderCount21Days = 0;
  @Default int masterOrderCount21Days = 0;
  @Default int atcCount21Days = 0;
  @Default int masterAtcCount21Days = 0;
  @Default int masterAtcCount15Days = 0;
  @Default int orderCount15Days = 0;
  @Default int masterOrderCount15Days = 0;
  @Default int atcCount15Days = 0;
  @Default int orderCount7Days = 0;
  @Default int masterOrderCount7Days = 0;
  @Default int atcCount7Days = 0;
  @Default int masterAtcCount7Days = 0;
  @Default int orderCount3Days = 0;
  @Default int masterOrderCount3Days = 0;
  @Default int atcCount3Days = 0;
  @Default int masterAtcCount3Days = 0;

}
