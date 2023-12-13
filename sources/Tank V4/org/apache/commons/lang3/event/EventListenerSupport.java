package org.apache.commons.lang3.event;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import org.apache.commons.lang3.Validate;

public class EventListenerSupport implements Serializable {
   private static final long serialVersionUID = 3593265990380473632L;
   private List listeners;
   private transient Object proxy;
   private transient Object[] prototypeArray;

   public static EventListenerSupport create(Class var0) {
      return new EventListenerSupport(var0);
   }

   public EventListenerSupport(Class var1) {
      this(var1, Thread.currentThread().getContextClassLoader());
   }

   public EventListenerSupport(Class var1, ClassLoader var2) {
      this();
      Validate.notNull(var1, "Listener interface cannot be null.");
      Validate.notNull(var2, "ClassLoader cannot be null.");
      Validate.isTrue(var1.isInterface(), "Class {0} is not an interface", var1.getName());
      this.initializeTransientFields(var1, var2);
   }

   private EventListenerSupport() {
      this.listeners = new CopyOnWriteArrayList();
   }

   public Object fire() {
      return this.proxy;
   }

   public void addListener(Object var1) {
      Validate.notNull(var1, "Listener object cannot be null.");
      this.listeners.add(var1);
   }

   int getListenerCount() {
      return this.listeners.size();
   }

   public void removeListener(Object var1) {
      Validate.notNull(var1, "Listener object cannot be null.");
      this.listeners.remove(var1);
   }

   public Object[] getListeners() {
      return this.listeners.toArray(this.prototypeArray);
   }

   private void writeObject(ObjectOutputStream var1) throws IOException {
      ArrayList var2 = new ArrayList();
      ObjectOutputStream var3 = new ObjectOutputStream(new ByteArrayOutputStream());
      Iterator var4 = this.listeners.iterator();

      while(var4.hasNext()) {
         Object var5 = var4.next();

         try {
            var3.writeObject(var5);
            var2.add(var5);
         } catch (IOException var7) {
            var3 = new ObjectOutputStream(new ByteArrayOutputStream());
         }
      }

      var1.writeObject(var2.toArray(this.prototypeArray));
   }

   private void readObject(ObjectInputStream var1) throws IOException, ClassNotFoundException {
      Object[] var2 = (Object[])((Object[])var1.readObject());
      this.listeners = new CopyOnWriteArrayList(var2);
      Class var3 = var2.getClass().getComponentType();
      this.initializeTransientFields(var3, Thread.currentThread().getContextClassLoader());
   }

   private void initializeTransientFields(Class var1, ClassLoader var2) {
      Object[] var3 = (Object[])((Object[])Array.newInstance(var1, 0));
      this.prototypeArray = var3;
      this.createProxy(var1, var2);
   }

   private void createProxy(Class var1, ClassLoader var2) {
      this.proxy = var1.cast(Proxy.newProxyInstance(var2, new Class[]{var1}, this.createInvocationHandler()));
   }

   protected InvocationHandler createInvocationHandler() {
      return new EventListenerSupport.ProxyInvocationHandler(this);
   }

   static List access$000(EventListenerSupport var0) {
      return var0.listeners;
   }

   protected class ProxyInvocationHandler implements InvocationHandler {
      final EventListenerSupport this$0;

      protected ProxyInvocationHandler(EventListenerSupport var1) {
         this.this$0 = var1;
      }

      public Object invoke(Object var1, Method var2, Object[] var3) throws Throwable {
         Iterator var4 = EventListenerSupport.access$000(this.this$0).iterator();

         while(var4.hasNext()) {
            Object var5 = var4.next();
            var2.invoke(var5, var3);
         }

         return null;
      }
   }
}
