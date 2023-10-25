package org.apache.http.impl.conn.tsccm;

import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.impl.conn.AbstractPoolEntry;
import org.apache.http.impl.conn.AbstractPooledConnAdapter;

@Deprecated
public class BasicPooledConnAdapter extends AbstractPooledConnAdapter {
  protected BasicPooledConnAdapter(ThreadSafeClientConnManager tsccm, AbstractPoolEntry entry) {
    super(tsccm, entry);
    markReusable();
  }
  
  protected ClientConnectionManager getManager() {
    return super.getManager();
  }
  
  protected AbstractPoolEntry getPoolEntry() {
    return super.getPoolEntry();
  }
  
  protected void detach() {
    super.detach();
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\http\impl\conn\tsccm\BasicPooledConnAdapter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */