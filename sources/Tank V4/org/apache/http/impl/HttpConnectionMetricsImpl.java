package org.apache.http.impl;

import java.util.HashMap;
import java.util.Map;
import org.apache.http.HttpConnectionMetrics;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.io.HttpTransportMetrics;

@NotThreadSafe
public class HttpConnectionMetricsImpl implements HttpConnectionMetrics {
   public static final String REQUEST_COUNT = "http.request-count";
   public static final String RESPONSE_COUNT = "http.response-count";
   public static final String SENT_BYTES_COUNT = "http.sent-bytes-count";
   public static final String RECEIVED_BYTES_COUNT = "http.received-bytes-count";
   private final HttpTransportMetrics inTransportMetric;
   private final HttpTransportMetrics outTransportMetric;
   private long requestCount = 0L;
   private long responseCount = 0L;
   private Map metricsCache;

   public HttpConnectionMetricsImpl(HttpTransportMetrics var1, HttpTransportMetrics var2) {
      this.inTransportMetric = var1;
      this.outTransportMetric = var2;
   }

   public long getReceivedBytesCount() {
      return this.inTransportMetric != null ? this.inTransportMetric.getBytesTransferred() : -1L;
   }

   public long getSentBytesCount() {
      return this.outTransportMetric != null ? this.outTransportMetric.getBytesTransferred() : -1L;
   }

   public long getRequestCount() {
      return this.requestCount;
   }

   public void incrementRequestCount() {
      ++this.requestCount;
   }

   public long getResponseCount() {
      return this.responseCount;
   }

   public void incrementResponseCount() {
      ++this.responseCount;
   }

   public Object getMetric(String var1) {
      Object var2 = null;
      if (this.metricsCache != null) {
         var2 = this.metricsCache.get(var1);
      }

      if (var2 == null) {
         if ("http.request-count".equals(var1)) {
            var2 = this.requestCount;
         } else if ("http.response-count".equals(var1)) {
            var2 = this.responseCount;
         } else {
            if ("http.received-bytes-count".equals(var1)) {
               if (this.inTransportMetric != null) {
                  return this.inTransportMetric.getBytesTransferred();
               }

               return null;
            }

            if ("http.sent-bytes-count".equals(var1)) {
               if (this.outTransportMetric != null) {
                  return this.outTransportMetric.getBytesTransferred();
               }

               return null;
            }
         }
      }

      return var2;
   }

   public void setMetric(String var1, Object var2) {
      if (this.metricsCache == null) {
         this.metricsCache = new HashMap();
      }

      this.metricsCache.put(var1, var2);
   }

   public void reset() {
      if (this.outTransportMetric != null) {
         this.outTransportMetric.reset();
      }

      if (this.inTransportMetric != null) {
         this.inTransportMetric.reset();
      }

      this.requestCount = 0L;
      this.responseCount = 0L;
      this.metricsCache = null;
   }
}
