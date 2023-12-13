package com.sun.prism.impl;

import com.sun.prism.GraphicsResource;

public abstract class BaseGraphicsResource implements GraphicsResource {
   private final Object disposerReferent;
   protected final Disposer.Record disposerRecord;

   public BaseGraphicsResource(BaseGraphicsResource var1) {
      this.disposerReferent = var1.disposerReferent;
      this.disposerRecord = var1.disposerRecord;
   }

   protected BaseGraphicsResource(Disposer.Record var1) {
      this.disposerReferent = new Object();
      this.disposerRecord = var1;
      Disposer.addRecord(this.disposerReferent, var1);
   }

   public abstract void dispose();
}
