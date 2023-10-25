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
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import org.apache.commons.lang3.Validate;

public class EventListenerSupport<L> implements Serializable {
  private static final long serialVersionUID = 3593265990380473632L;
  
  private List<L> listeners = new CopyOnWriteArrayList<L>();
  
  private transient L proxy;
  
  private transient L[] prototypeArray;
  
  public static <T> EventListenerSupport<T> create(Class<T> listenerInterface) {
    return new EventListenerSupport<T>(listenerInterface);
  }
  
  public EventListenerSupport(Class<L> listenerInterface) {
    this(listenerInterface, Thread.currentThread().getContextClassLoader());
  }
  
  public EventListenerSupport(Class<L> listenerInterface, ClassLoader classLoader) {
    this();
    Validate.notNull(listenerInterface, "Listener interface cannot be null.", new Object[0]);
    Validate.notNull(classLoader, "ClassLoader cannot be null.", new Object[0]);
    Validate.isTrue(listenerInterface.isInterface(), "Class {0} is not an interface", new Object[] { listenerInterface.getName() });
    initializeTransientFields(listenerInterface, classLoader);
  }
  
  public L fire() {
    return this.proxy;
  }
  
  public void addListener(L listener) {
    Validate.notNull(listener, "Listener object cannot be null.", new Object[0]);
    this.listeners.add(listener);
  }
  
  int getListenerCount() {
    return this.listeners.size();
  }
  
  public void removeListener(L listener) {
    Validate.notNull(listener, "Listener object cannot be null.", new Object[0]);
    this.listeners.remove(listener);
  }
  
  public L[] getListeners() {
    return this.listeners.toArray(this.prototypeArray);
  }
  
  private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
    ArrayList<L> serializableListeners = new ArrayList<L>();
    ObjectOutputStream testObjectOutputStream = new ObjectOutputStream(new ByteArrayOutputStream());
    for (L listener : this.listeners) {
      try {
        testObjectOutputStream.writeObject(listener);
        serializableListeners.add(listener);
      } catch (IOException exception) {
        testObjectOutputStream = new ObjectOutputStream(new ByteArrayOutputStream());
      } 
    } 
    objectOutputStream.writeObject(serializableListeners.toArray(this.prototypeArray));
  }
  
  private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
    L[] srcListeners = (L[])objectInputStream.readObject();
    this.listeners = new CopyOnWriteArrayList<L>(srcListeners);
    Class<L> listenerInterface = (Class)srcListeners.getClass().getComponentType();
    initializeTransientFields(listenerInterface, Thread.currentThread().getContextClassLoader());
  }
  
  private void initializeTransientFields(Class<L> listenerInterface, ClassLoader classLoader) {
    L[] array = (L[])Array.newInstance(listenerInterface, 0);
    this.prototypeArray = array;
    createProxy(listenerInterface, classLoader);
  }
  
  private void createProxy(Class<L> listenerInterface, ClassLoader classLoader) {
    this.proxy = listenerInterface.cast(Proxy.newProxyInstance(classLoader, new Class[] { listenerInterface }, createInvocationHandler()));
  }
  
  protected InvocationHandler createInvocationHandler() {
    return new ProxyInvocationHandler();
  }
  
  private EventListenerSupport() {}
  
  protected class ProxyInvocationHandler implements InvocationHandler {
    public Object invoke(Object unusedProxy, Method method, Object[] args) throws Throwable {
      for (L listener : EventListenerSupport.this.listeners)
        method.invoke(listener, args); 
      return null;
    }
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\commons\lang3\event\EventListenerSupport.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */