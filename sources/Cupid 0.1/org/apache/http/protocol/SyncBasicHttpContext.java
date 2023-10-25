package org.apache.http.protocol;

@Deprecated
public class SyncBasicHttpContext extends BasicHttpContext {
  public SyncBasicHttpContext(HttpContext parentContext) {
    super(parentContext);
  }
  
  public SyncBasicHttpContext() {}
  
  public synchronized Object getAttribute(String id) {
    return super.getAttribute(id);
  }
  
  public synchronized void setAttribute(String id, Object obj) {
    super.setAttribute(id, obj);
  }
  
  public synchronized Object removeAttribute(String id) {
    return super.removeAttribute(id);
  }
  
  public synchronized void clear() {
    super.clear();
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\http\protocol\SyncBasicHttpContext.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */