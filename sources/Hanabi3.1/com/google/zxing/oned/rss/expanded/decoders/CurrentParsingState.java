package com.google.zxing.oned.rss.expanded.decoders;

final class CurrentParsingState {
   private int position = 0;
   private State encoding;

   CurrentParsingState() {
      this.encoding = CurrentParsingState.State.NUMERIC;
   }

   int getPosition() {
      return this.position;
   }

   void setPosition(int position) {
      this.position = position;
   }

   void incrementPosition(int delta) {
      this.position += delta;
   }

   boolean isAlpha() {
      return this.encoding == CurrentParsingState.State.ALPHA;
   }

   boolean isNumeric() {
      return this.encoding == CurrentParsingState.State.NUMERIC;
   }

   boolean isIsoIec646() {
      return this.encoding == CurrentParsingState.State.ISO_IEC_646;
   }

   void setNumeric() {
      this.encoding = CurrentParsingState.State.NUMERIC;
   }

   void setAlpha() {
      this.encoding = CurrentParsingState.State.ALPHA;
   }

   void setIsoIec646() {
      this.encoding = CurrentParsingState.State.ISO_IEC_646;
   }

   private static enum State {
      NUMERIC,
      ALPHA,
      ISO_IEC_646;
   }
}
