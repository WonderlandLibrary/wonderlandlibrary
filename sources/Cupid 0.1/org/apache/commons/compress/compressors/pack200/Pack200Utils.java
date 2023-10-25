package org.apache.commons.compress.compressors.pack200;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;
import java.util.jar.Pack200;

public class Pack200Utils {
  public static void normalize(File jar) throws IOException {
    normalize(jar, jar, null);
  }
  
  public static void normalize(File jar, Map<String, String> props) throws IOException {
    normalize(jar, jar, props);
  }
  
  public static void normalize(File from, File to) throws IOException {
    normalize(from, to, null);
  }
  
  public static void normalize(File from, File to, Map<String, String> props) throws IOException {
    if (props == null)
      props = new HashMap<String, String>(); 
    props.put("pack.segment.limit", "-1");
    File f = File.createTempFile("commons-compress", "pack200normalize");
    f.deleteOnExit();
    try {
      OutputStream os = new FileOutputStream(f);
      JarFile j = null;
      try {
        Pack200.Packer p = Pack200.newPacker();
        p.properties().putAll(props);
        p.pack(j = new JarFile(from), os);
        j = null;
        os.close();
        os = null;
        Pack200.Unpacker u = Pack200.newUnpacker();
        os = new JarOutputStream(new FileOutputStream(to));
        u.unpack(f, (JarOutputStream)os);
      } finally {
        if (j != null)
          j.close(); 
        if (os != null)
          os.close(); 
      } 
    } finally {
      f.delete();
    } 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\commons\compress\compressors\pack200\Pack200Utils.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */