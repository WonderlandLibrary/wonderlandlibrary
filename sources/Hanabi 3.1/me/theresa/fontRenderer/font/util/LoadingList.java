package me.theresa.fontRenderer.font.util;

import java.util.ArrayList;
import me.theresa.fontRenderer.font.log.Log;
import me.theresa.fontRenderer.font.opengl.InternalTextureLoader;

public class LoadingList {
   private static LoadingList single = new LoadingList();
   private final ArrayList deferred = new ArrayList();
   private int total;

   public static LoadingList get() {
      return single;
   }

   public static void setDeferredLoading(boolean loading) {
      single = new LoadingList();
      InternalTextureLoader.get().setDeferredLoading(loading);
   }

   public static boolean isDeferredLoading() {
      return InternalTextureLoader.get().isDeferredLoading();
   }

   private LoadingList() {
   }

   public void add(DeferredResource resource) {
      ++this.total;
      this.deferred.add(resource);
   }

   public void remove(DeferredResource resource) {
      Log.info("Early loading of deferred resource due to req: " + resource.getDescription());
      --this.total;
      this.deferred.remove(resource);
   }

   public int getTotalResources() {
      return this.total;
   }

   public int getRemainingResources() {
      return this.deferred.size();
   }

   public DeferredResource getNext() {
      return this.deferred.size() == 0 ? null : (DeferredResource)this.deferred.remove(0);
   }
}
