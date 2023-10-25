package com.sun.jna;

public interface CallbackProxy extends Callback {
  Object callback(Object[] paramArrayOfObject);
  
  Class[] getParameterTypes();
  
  Class getReturnType();
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\sun\jna\CallbackProxy.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */