package com.sun.prism.impl;

public abstract class DisposerManagedResource extends ManagedResource {
   Object referent = new Object();

   public DisposerManagedResource(Object var1, ResourcePool var2, Disposer.Record var3) {
      super(var1, var2);
      Disposer.addRecord(this.referent, var3);
   }
}
