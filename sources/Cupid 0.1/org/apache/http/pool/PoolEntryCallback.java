package org.apache.http.pool;

public interface PoolEntryCallback<T, C> {
  void process(PoolEntry<T, C> paramPoolEntry);
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\http\pool\PoolEntryCallback.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */