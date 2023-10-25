package org.apache.logging.log4j.core.appender.nosql;

public interface NoSqlObject<W> {
  void set(String paramString, Object paramObject);
  
  void set(String paramString, NoSqlObject<W> paramNoSqlObject);
  
  void set(String paramString, Object[] paramArrayOfObject);
  
  void set(String paramString, NoSqlObject<W>[] paramArrayOfNoSqlObject);
  
  W unwrap();
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\appender\nosql\NoSqlObject.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */