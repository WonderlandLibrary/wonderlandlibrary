package javafx.beans.property.adapter;

import com.sun.javafx.property.adapter.ReadOnlyPropertyDescriptor;
import java.lang.ref.WeakReference;

class DescriptorListenerCleaner implements Runnable {
   private final ReadOnlyPropertyDescriptor pd;
   private final WeakReference lRef;

   DescriptorListenerCleaner(ReadOnlyPropertyDescriptor var1, ReadOnlyPropertyDescriptor.ReadOnlyListener var2) {
      this.pd = var1;
      this.lRef = new WeakReference(var2);
   }

   public void run() {
      ReadOnlyPropertyDescriptor.ReadOnlyListener var1 = (ReadOnlyPropertyDescriptor.ReadOnlyListener)this.lRef.get();
      if (var1 != null) {
         this.pd.removeListener(var1);
      }

   }
}
