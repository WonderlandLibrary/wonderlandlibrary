package org.apache.logging.log4j.core.config.plugins.processor;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.Map;
import java.util.TreeMap;

public class PluginCache {
  private final Map<String, Map<String, PluginEntry>> categories = new TreeMap<>();
  
  public Map<String, Map<String, PluginEntry>> getAllCategories() {
    return this.categories;
  }
  
  public Map<String, PluginEntry> getCategory(String category) {
    String key = category.toLowerCase();
    return this.categories.computeIfAbsent(key, ignored -> new TreeMap<>());
  }
  
  public void writeCache(OutputStream os) throws IOException {
    try (DataOutputStream out = new DataOutputStream(new BufferedOutputStream(os))) {
      out.writeInt(this.categories.size());
      for (Map.Entry<String, Map<String, PluginEntry>> category : this.categories.entrySet()) {
        out.writeUTF(category.getKey());
        Map<String, PluginEntry> m = category.getValue();
        out.writeInt(m.size());
        for (Map.Entry<String, PluginEntry> entry : m.entrySet()) {
          PluginEntry plugin = entry.getValue();
          out.writeUTF(plugin.getKey());
          out.writeUTF(plugin.getClassName());
          out.writeUTF(plugin.getName());
          out.writeBoolean(plugin.isPrintable());
          out.writeBoolean(plugin.isDefer());
        } 
      } 
    } 
  }
  
  public void loadCacheFiles(Enumeration<URL> resources) throws IOException {
    this.categories.clear();
    while (resources.hasMoreElements()) {
      URL url = resources.nextElement();
      try (DataInputStream in = new DataInputStream(new BufferedInputStream(url.openStream()))) {
        int count = in.readInt();
        for (int i = 0; i < count; i++) {
          String category = in.readUTF();
          Map<String, PluginEntry> m = getCategory(category);
          int entries = in.readInt();
          for (int j = 0; j < entries; j++) {
            String key = in.readUTF();
            String className = in.readUTF();
            String name = in.readUTF();
            boolean printable = in.readBoolean();
            boolean defer = in.readBoolean();
            m.computeIfAbsent(key, k -> {
                  PluginEntry entry = new PluginEntry();
                  entry.setKey(k);
                  entry.setClassName(className);
                  entry.setName(name);
                  entry.setPrintable(printable);
                  entry.setDefer(defer);
                  entry.setCategory(category);
                  return entry;
                });
          } 
        } 
      } 
    } 
  }
  
  public int size() {
    return this.categories.size();
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\config\plugins\processor\PluginCache.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */