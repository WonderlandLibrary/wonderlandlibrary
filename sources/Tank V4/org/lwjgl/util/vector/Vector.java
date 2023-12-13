package org.lwjgl.util.vector;

import java.io.Serializable;
import java.nio.FloatBuffer;

public abstract class Vector implements Serializable, ReadableVector {
   protected Vector() {
   }

   public final float length() {
      return (float)Math.sqrt((double)this.lengthSquared());
   }

   public abstract float lengthSquared();

   public abstract Vector load(FloatBuffer var1);

   public abstract Vector negate();

   public final Vector normalise() {
      float var1 = this.length();
      if (var1 != 0.0F) {
         float var2 = 1.0F / var1;
         return this.scale(var2);
      } else {
         throw new IllegalStateException("Zero length vector");
      }
   }

   public abstract Vector store(FloatBuffer var1);

   public abstract Vector scale(float var1);
}
