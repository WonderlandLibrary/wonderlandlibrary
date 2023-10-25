package org.apache.logging.log4j.core.config.plugins.processor;

import java.io.Serializable;

public class PluginEntry implements Serializable {
  private static final long serialVersionUID = 1L;
  
  private String key;
  
  private String className;
  
  private String name;
  
  private boolean printable;
  
  private boolean defer;
  
  private transient String category;
  
  public String getKey() {
    return this.key;
  }
  
  public void setKey(String key) {
    this.key = key;
  }
  
  public String getClassName() {
    return this.className;
  }
  
  public void setClassName(String className) {
    this.className = className;
  }
  
  public String getName() {
    return this.name;
  }
  
  public void setName(String name) {
    this.name = name;
  }
  
  public boolean isPrintable() {
    return this.printable;
  }
  
  public void setPrintable(boolean printable) {
    this.printable = printable;
  }
  
  public boolean isDefer() {
    return this.defer;
  }
  
  public void setDefer(boolean defer) {
    this.defer = defer;
  }
  
  public String getCategory() {
    return this.category;
  }
  
  public void setCategory(String category) {
    this.category = category;
  }
  
  public String toString() {
    return "PluginEntry [key=" + this.key + ", className=" + this.className + ", name=" + this.name + ", printable=" + this.printable + ", defer=" + this.defer + ", category=" + this.category + "]";
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\config\plugins\processor\PluginEntry.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */