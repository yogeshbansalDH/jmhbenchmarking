package com.example.benchmarking;

import lombok.Builder;
import lombok.Value;
import lombok.With;

@Builder
@Value
public class QueryInteractionWFSTParameters {
  String globalEntityId;
  String vendorId;
  String query;
  @With String productId;
  @Builder.Default String featureName = "";
  @Builder.Default Double featureValues = 0.0;
}
