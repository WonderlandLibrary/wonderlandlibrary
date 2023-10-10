package org.alphacentauri.management.io;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Set;
import java.util.UUID;
import java.util.Map.Entry;
import org.alphacentauri.launcher.api.API;

public class ConfigFile {
   private final String name;
   private HashMap cache;

   public ConfigFile(String name) {
      this.name = name;
      this.cache = new HashMap();
      this.load();
   }

   public String get(String key) {
      return (String)this.cache.get(key);
   }

   public boolean getBoolean(String key) {
      String v = this.get(key);
      if(v == null) {
         return false;
      } else {
         try {
            return Boolean.parseBoolean(v);
         } catch (Exception var4) {
            return false;
         }
      }
   }

   public short getShort(String key) {
      String v = this.get(key);
      if(v == null) {
         return (short)0;
      } else {
         try {
            return Short.parseShort(v);
         } catch (Exception var4) {
            return (short)0;
         }
      }
   }

   public int getInt(String key) {
      String v = this.get(key);
      if(v == null) {
         return 0;
      } else {
         try {
            return Integer.parseInt(v);
         } catch (Exception var4) {
            return 0;
         }
      }
   }

   public long getLong(String key) {
      String v = this.get(key);
      if(v == null) {
         return 0L;
      } else {
         try {
            return Long.parseLong(v);
         } catch (Exception var4) {
            return 0L;
         }
      }
   }

   public float getFloat(String key) {
      String v = this.get(key);
      if(v == null) {
         return 0.0F;
      } else {
         try {
            return Float.parseFloat(v);
         } catch (Exception var4) {
            return 0.0F;
         }
      }
   }

   public double getDouble(String key) {
      String v = this.get(key);
      if(v == null) {
         return 0.0D;
      } else {
         try {
            return Double.parseDouble(v);
         } catch (Exception var4) {
            return 0.0D;
         }
      }
   }

   public void clear() {
      this.cache.clear();
   }

   public boolean contains(String key) {
      return this.cache.containsKey(key);
   }

   private void load() {
      byte[] file = API.loadFile(this.name);
      String username = API.getUsername();
      StringBuilder pwb = new StringBuilder();

      for(char c : API.getPassword()) {
         pwb.append(c);
      }

      byte[] key = (username + pwb.toString()).getBytes();

      for(int i = 0; i < file.length; ++i) {
         file[i] ^= key[i % key.length];
      }

      String content = new String(file, Charset.forName("UTF-8"));
      String[] lines = content.split("\n");

      for(String line : lines) {
         String[] split = line.split("=");
         if(split.length == 2) {
            this.cache.put(split[0], split[1]);
         }
      }

   }

   public void save() {
      StringBuilder fileBuilder = new StringBuilder();

      for(Entry<String, String> entry : this.cache.entrySet()) {
         fileBuilder.append((String)entry.getKey()).append('=').append((String)entry.getValue()).append('\n');
      }

      byte[] data = fileBuilder.toString().getBytes(Charset.forName("UTF-8"));
      String username = API.getUsername();
      StringBuilder pwb = new StringBuilder();

      for(char c : API.getPassword()) {
         pwb.append(c);
      }

      byte[] key = (username + pwb.toString()).getBytes();

      for(int i = 0; i < data.length; ++i) {
         data[i] ^= key[i % key.length];
      }

      API.saveFile(this.name, data);
   }

   public void set(String key, Object value) {
      this.set(key, String.valueOf(value));
   }

   public void set(String key, String value) {
      if(value == null) {
         this.cache.remove(key);
      } else {
         this.cache.put(key, value);
      }

   }

   public Set all() {
      return this.cache.entrySet();
   }

   public UUID getUUID(String key) {
      String v = this.get(key);
      if(v == null) {
         return null;
      } else {
         try {
            return UUID.fromString(v);
         } catch (Exception var4) {
            return null;
         }
      }
   }
}
