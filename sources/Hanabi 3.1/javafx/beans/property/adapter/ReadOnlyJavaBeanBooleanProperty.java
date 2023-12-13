package javafx.beans.property.adapter;

import com.sun.javafx.property.adapter.Disposer;
import com.sun.javafx.property.adapter.ReadOnlyPropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.UndeclaredThrowableException;
import java.security.AccessControlContext;
import java.security.AccessController;
import javafx.beans.property.ReadOnlyBooleanPropertyBase;
import sun.reflect.misc.MethodUtil;

public final class ReadOnlyJavaBeanBooleanProperty extends ReadOnlyBooleanPropertyBase implements ReadOnlyJavaBeanProperty {
   private final ReadOnlyPropertyDescriptor descriptor;
   private final ReadOnlyPropertyDescriptor.ReadOnlyListener listener;
   private final AccessControlContext acc = AccessController.getContext();

   ReadOnlyJavaBeanBooleanProperty(ReadOnlyPropertyDescriptor var1, Object var2) {
      this.descriptor = var1;
      this.listener = var1.new ReadOnlyListener(var2, this);
      var1.addListener(this.listener);
      Disposer.addRecord(this, new DescriptorListenerCleaner(var1, this.listener));
   }

   public boolean get() {
      return (Boolean)AccessController.doPrivileged(() -> {
         try {
            return (Boolean)MethodUtil.invoke(this.descriptor.getGetter(), this.getBean(), (Object[])null);
         } catch (IllegalAccessException var2) {
            throw new UndeclaredThrowableException(var2);
         } catch (InvocationTargetException var3) {
            throw new UndeclaredThrowableException(var3);
         }
      }, this.acc);
   }

   public Object getBean() {
      return this.listener.getBean();
   }

   public String getName() {
      return this.descriptor.getName();
   }

   public void fireValueChangedEvent() {
      super.fireValueChangedEvent();
   }

   public void dispose() {
      this.descriptor.removeListener(this.listener);
   }
}
