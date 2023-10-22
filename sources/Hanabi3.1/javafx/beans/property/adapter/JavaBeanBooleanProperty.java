package javafx.beans.property.adapter;

import com.sun.javafx.binding.ExpressionHelper;
import com.sun.javafx.property.adapter.Disposer;
import com.sun.javafx.property.adapter.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.UndeclaredThrowableException;
import java.security.AccessControlContext;
import java.security.AccessController;
import javafx.beans.InvalidationListener;
import javafx.beans.property.BooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import sun.reflect.misc.MethodUtil;

public final class JavaBeanBooleanProperty extends BooleanProperty implements JavaBeanProperty {
   private final PropertyDescriptor descriptor;
   private final PropertyDescriptor.Listener listener;
   private ObservableValue observable = null;
   private ExpressionHelper helper = null;
   private final AccessControlContext acc = AccessController.getContext();

   JavaBeanBooleanProperty(PropertyDescriptor var1, Object var2) {
      this.descriptor = var1;
      this.listener = var1.new Listener(var2, this);
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

   public void set(boolean var1) {
      if (this.isBound()) {
         throw new RuntimeException("A bound value cannot be set.");
      } else {
         AccessController.doPrivileged(() -> {
            try {
               MethodUtil.invoke(this.descriptor.getSetter(), this.getBean(), new Object[]{var1});
               ExpressionHelper.fireValueChangedEvent(this.helper);
               return null;
            } catch (IllegalAccessException var3) {
               throw new UndeclaredThrowableException(var3);
            } catch (InvocationTargetException var4) {
               throw new UndeclaredThrowableException(var4);
            }
         }, this.acc);
      }
   }

   public void bind(ObservableValue var1) {
      if (var1 == null) {
         throw new NullPointerException("Cannot bind to null");
      } else {
         if (!var1.equals(this.observable)) {
            this.unbind();
            this.set((Boolean)var1.getValue());
            this.observable = var1;
            this.observable.addListener(this.listener);
         }

      }
   }

   public void unbind() {
      if (this.observable != null) {
         this.observable.removeListener(this.listener);
         this.observable = null;
      }

   }

   public boolean isBound() {
      return this.observable != null;
   }

   public Object getBean() {
      return this.listener.getBean();
   }

   public String getName() {
      return this.descriptor.getName();
   }

   public void addListener(ChangeListener var1) {
      this.helper = ExpressionHelper.addListener(this.helper, this, (ChangeListener)var1);
   }

   public void removeListener(ChangeListener var1) {
      this.helper = ExpressionHelper.removeListener(this.helper, var1);
   }

   public void addListener(InvalidationListener var1) {
      this.helper = ExpressionHelper.addListener(this.helper, this, (InvalidationListener)var1);
   }

   public void removeListener(InvalidationListener var1) {
      this.helper = ExpressionHelper.removeListener(this.helper, var1);
   }

   public void fireValueChangedEvent() {
      ExpressionHelper.fireValueChangedEvent(this.helper);
   }

   public void dispose() {
      this.descriptor.removeListener(this.listener);
   }

   public String toString() {
      Object var1 = this.getBean();
      String var2 = this.getName();
      StringBuilder var3 = new StringBuilder("BooleanProperty [");
      if (var1 != null) {
         var3.append("bean: ").append(var1).append(", ");
      }

      if (var2 != null && !var2.equals("")) {
         var3.append("name: ").append(var2).append(", ");
      }

      if (this.isBound()) {
         var3.append("bound, ");
      }

      var3.append("value: ").append(this.get());
      var3.append("]");
      return var3.toString();
   }
}
