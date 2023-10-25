package org.apache.http.concurrent;

public interface FutureCallback<T> {
  void completed(T paramT);
  
  void failed(Exception paramException);
  
  void cancelled();
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\http\concurrent\FutureCallback.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */