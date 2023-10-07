// 
// Decompiled by Procyon v0.6.0
// 

package org.newdawn.slick.muffin;

import org.newdawn.slick.util.Log;
import java.io.EOFException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.ObjectOutputStream;
import java.io.FileOutputStream;
import java.io.File;
import java.util.HashMap;

public class FileMuffin implements Muffin
{
    public void saveFile(final HashMap scoreMap, final String fileName) throws IOException {
        final String userHome = System.getProperty("user.home");
        File file = new File(userHome);
        file = new File(file, ".java");
        if (!file.exists()) {
            file.mkdir();
        }
        file = new File(file, fileName);
        final FileOutputStream fos = new FileOutputStream(file);
        final ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(scoreMap);
        oos.close();
    }
    
    public HashMap loadFile(final String fileName) throws IOException {
        HashMap hashMap = new HashMap();
        final String userHome = System.getProperty("user.home");
        File file = new File(userHome);
        file = new File(file, ".java");
        file = new File(file, fileName);
        if (file.exists()) {
            try {
                final FileInputStream fis = new FileInputStream(file);
                final ObjectInputStream ois = new ObjectInputStream(fis);
                hashMap = (HashMap)ois.readObject();
                ois.close();
            }
            catch (final EOFException e) {}
            catch (final ClassNotFoundException e2) {
                Log.error(e2);
                throw new IOException("Failed to pull state from store - class not found");
            }
        }
        return hashMap;
    }
}
