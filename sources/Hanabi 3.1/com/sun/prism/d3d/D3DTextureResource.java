package com.sun.prism.d3d;

import com.sun.prism.impl.DisposerManagedResource;

class D3DTextureResource extends DisposerManagedResource {
   public D3DTextureResource(D3DTextureData var1) {
      super(var1, D3DVramPool.instance, var1);
   }

   public void free() {
      if (this.resource != null) {
         ((D3DTextureData)this.resource).dispose();
      }

   }

   public boolean isValid() {
      return this.resource != null && ((D3DTextureData)this.resource).getResource() != 0L;
   }
}
