package io.netty.handler.codec.spdy;

import io.netty.util.internal.StringUtil;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class DefaultSpdySettingsFrame implements SpdySettingsFrame {
  private boolean clear;
  
  private final Map<Integer, Setting> settingsMap = new TreeMap<Integer, Setting>();
  
  public Set<Integer> ids() {
    return this.settingsMap.keySet();
  }
  
  public boolean isSet(int id) {
    Integer key = Integer.valueOf(id);
    return this.settingsMap.containsKey(key);
  }
  
  public int getValue(int id) {
    Integer key = Integer.valueOf(id);
    if (this.settingsMap.containsKey(key))
      return ((Setting)this.settingsMap.get(key)).getValue(); 
    return -1;
  }
  
  public SpdySettingsFrame setValue(int id, int value) {
    return setValue(id, value, false, false);
  }
  
  public SpdySettingsFrame setValue(int id, int value, boolean persistValue, boolean persisted) {
    if (id < 0 || id > 16777215)
      throw new IllegalArgumentException("Setting ID is not valid: " + id); 
    Integer key = Integer.valueOf(id);
    if (this.settingsMap.containsKey(key)) {
      Setting setting = this.settingsMap.get(key);
      setting.setValue(value);
      setting.setPersist(persistValue);
      setting.setPersisted(persisted);
    } else {
      this.settingsMap.put(key, new Setting(value, persistValue, persisted));
    } 
    return this;
  }
  
  public SpdySettingsFrame removeValue(int id) {
    Integer key = Integer.valueOf(id);
    if (this.settingsMap.containsKey(key))
      this.settingsMap.remove(key); 
    return this;
  }
  
  public boolean isPersistValue(int id) {
    Integer key = Integer.valueOf(id);
    if (this.settingsMap.containsKey(key))
      return ((Setting)this.settingsMap.get(key)).isPersist(); 
    return false;
  }
  
  public SpdySettingsFrame setPersistValue(int id, boolean persistValue) {
    Integer key = Integer.valueOf(id);
    if (this.settingsMap.containsKey(key))
      ((Setting)this.settingsMap.get(key)).setPersist(persistValue); 
    return this;
  }
  
  public boolean isPersisted(int id) {
    Integer key = Integer.valueOf(id);
    if (this.settingsMap.containsKey(key))
      return ((Setting)this.settingsMap.get(key)).isPersisted(); 
    return false;
  }
  
  public SpdySettingsFrame setPersisted(int id, boolean persisted) {
    Integer key = Integer.valueOf(id);
    if (this.settingsMap.containsKey(key))
      ((Setting)this.settingsMap.get(key)).setPersisted(persisted); 
    return this;
  }
  
  public boolean clearPreviouslyPersistedSettings() {
    return this.clear;
  }
  
  public SpdySettingsFrame setClearPreviouslyPersistedSettings(boolean clear) {
    this.clear = clear;
    return this;
  }
  
  private Set<Map.Entry<Integer, Setting>> getSettings() {
    return this.settingsMap.entrySet();
  }
  
  private void appendSettings(StringBuilder buf) {
    for (Map.Entry<Integer, Setting> e : getSettings()) {
      Setting setting = e.getValue();
      buf.append("--> ");
      buf.append(e.getKey());
      buf.append(':');
      buf.append(setting.getValue());
      buf.append(" (persist value: ");
      buf.append(setting.isPersist());
      buf.append("; persisted: ");
      buf.append(setting.isPersisted());
      buf.append(')');
      buf.append(StringUtil.NEWLINE);
    } 
  }
  
  public String toString() {
    StringBuilder buf = new StringBuilder();
    buf.append(StringUtil.simpleClassName(this));
    buf.append(StringUtil.NEWLINE);
    appendSettings(buf);
    buf.setLength(buf.length() - StringUtil.NEWLINE.length());
    return buf.toString();
  }
  
  private static final class Setting {
    private int value;
    
    private boolean persist;
    
    private boolean persisted;
    
    Setting(int value, boolean persist, boolean persisted) {
      this.value = value;
      this.persist = persist;
      this.persisted = persisted;
    }
    
    int getValue() {
      return this.value;
    }
    
    void setValue(int value) {
      this.value = value;
    }
    
    boolean isPersist() {
      return this.persist;
    }
    
    void setPersist(boolean persist) {
      this.persist = persist;
    }
    
    boolean isPersisted() {
      return this.persisted;
    }
    
    void setPersisted(boolean persisted) {
      this.persisted = persisted;
    }
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\netty\handler\codec\spdy\DefaultSpdySettingsFrame.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */