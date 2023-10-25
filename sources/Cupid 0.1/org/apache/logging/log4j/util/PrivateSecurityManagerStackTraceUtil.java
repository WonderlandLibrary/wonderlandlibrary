package org.apache.logging.log4j.util;

import java.util.Stack;

final class PrivateSecurityManagerStackTraceUtil {
  private static final PrivateSecurityManager SECURITY_MANAGER;
  
  static {
    PrivateSecurityManager psm;
    try {
      SecurityManager sm = System.getSecurityManager();
      if (sm != null)
        sm.checkPermission(new RuntimePermission("createSecurityManager")); 
      psm = new PrivateSecurityManager();
    } catch (SecurityException ignored) {
      psm = null;
    } 
    SECURITY_MANAGER = psm;
  }
  
  static boolean isEnabled() {
    return (SECURITY_MANAGER != null);
  }
  
  static Stack<Class<?>> getCurrentStackTrace() {
    Class<?>[] array = SECURITY_MANAGER.getClassContext();
    Stack<Class<?>> classes = new Stack<>();
    classes.ensureCapacity(array.length);
    for (Class<?> clazz : array)
      classes.push(clazz); 
    return classes;
  }
  
  private static final class PrivateSecurityManager extends SecurityManager {
    private PrivateSecurityManager() {}
    
    protected Class<?>[] getClassContext() {
      return super.getClassContext();
    }
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4\\util\PrivateSecurityManagerStackTraceUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */