package org.apache.logging.log4j.core.lookup;

public abstract class AbstractLookup implements StrLookup {
  public String lookup(String key) {
    return lookup(null, key);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\lookup\AbstractLookup.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */