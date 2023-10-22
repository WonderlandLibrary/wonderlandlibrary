package com.sun.prism.d3d;

import com.sun.prism.impl.BaseGraphicsResource;
import com.sun.prism.impl.Disposer;

class D3DResource extends BaseGraphicsResource {
   protected final D3DRecord d3dResRecord;

   D3DResource(D3DRecord var1) {
      super((Disposer.Record)var1);
      this.d3dResRecord = var1;
   }

   public void dispose() {
      this.d3dResRecord.dispose();
   }

   static class D3DRecord implements Disposer.Record {
      private final D3DContext context;
      private long pResource;
      private boolean isDefaultPool;

      D3DRecord(D3DContext var1, long var2) {
         this.context = var1;
         this.pResource = var2;
         if (var2 != 0L) {
            var1.getResourceFactory().addRecord(this);
            this.isDefaultPool = D3DResourceFactory.nIsDefaultPool(var2);
         } else {
            this.isDefaultPool = false;
         }

      }

      long getResource() {
         return this.pResource;
      }

      D3DContext getContext() {
         return this.context;
      }

      boolean isDefaultPool() {
         return this.isDefaultPool;
      }

      protected void markDisposed() {
         this.pResource = 0L;
      }

      public void dispose() {
         if (this.pResource != 0L) {
            this.context.getResourceFactory().removeRecord(this);
            D3DResourceFactory.nReleaseResource(this.context.getContextHandle(), this.pResource);
            this.pResource = 0L;
         }

      }
   }
}
