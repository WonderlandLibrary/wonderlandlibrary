package org.apache.logging.log4j.core.appender.nosql;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DefaultNoSqlObject implements NoSqlObject<Map<String, Object>> {
  private final Map<String, Object> map = new HashMap<>();
  
  public void set(String field, Object value) {
    this.map.put(field, value);
  }
  
  public void set(String field, NoSqlObject<Map<String, Object>> value) {
    this.map.put(field, value.unwrap());
  }
  
  public void set(String field, Object[] values) {
    this.map.put(field, Arrays.asList(values));
  }
  
  public void set(String field, NoSqlObject<Map<String, Object>>[] values) {
    List<Map<String, Object>> list = new ArrayList<>(values.length);
    for (NoSqlObject<Map<String, Object>> value : values)
      list.add(value.unwrap()); 
    this.map.put(field, list);
  }
  
  public Map<String, Object> unwrap() {
    return this.map;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\appender\nosql\DefaultNoSqlObject.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */