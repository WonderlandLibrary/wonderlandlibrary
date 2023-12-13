package org.newdawn.slick.loading;

import java.util.ArrayList;
import org.newdawn.slick.openal.SoundStore;
import org.newdawn.slick.opengl.InternalTextureLoader;
import org.newdawn.slick.util.Log;

public class LoadingList {
   private static LoadingList single = new LoadingList();
   private ArrayList deferred = new ArrayList();
   private int total;

   public static LoadingList get() {
      return single;
   }

   public static void setDeferredLoading(boolean var0) {
      single = new LoadingList();
      InternalTextureLoader.get().setDeferredLoading(var0);
      SoundStore.get().setDeferredLoading(var0);
   }

   public static boolean isDeferredLoading() {
      return InternalTextureLoader.get().isDeferredLoading();
   }

   private LoadingList() {
   }

   public void add(DeferredResource var1) {
      ++this.total;
      this.deferred.add(var1);
   }

   public void remove(DeferredResource var1) {
      Log.info("Early loading of deferred resource due to req: " + var1.getDescription());
      --this.total;
      this.deferred.remove(var1);
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
