package javafx.scene.media;

import java.util.Arrays;
import java.util.Collection;
import javafx.util.Builder;

/** @deprecated */
@Deprecated
public final class MediaBuilder implements Builder {
   private int __set;
   private Runnable onError;
   private String source;
   private Collection tracks;

   protected MediaBuilder() {
   }

   public static MediaBuilder create() {
      return new MediaBuilder();
   }

   public void applyTo(Media var1) {
      int var2 = this.__set;
      if ((var2 & 1) != 0) {
         var1.setOnError(this.onError);
      }

      if ((var2 & 2) != 0) {
         var1.getTracks().addAll(this.tracks);
      }

   }

   public MediaBuilder onError(Runnable var1) {
      this.onError = var1;
      this.__set |= 1;
      return this;
   }

   public MediaBuilder source(String var1) {
      this.source = var1;
      return this;
   }

   public MediaBuilder tracks(Collection var1) {
      this.tracks = var1;
      this.__set |= 2;
      return this;
   }

   public MediaBuilder tracks(Track... var1) {
      return this.tracks((Collection)Arrays.asList(var1));
   }

   public Media build() {
      Media var1 = new Media(this.source);
      this.applyTo(var1);
      return var1;
   }
}
