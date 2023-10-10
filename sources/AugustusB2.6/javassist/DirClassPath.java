// 
// Decompiled by Procyon v0.5.36
// 

package javassist;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.io.FileNotFoundException;
import java.io.FileInputStream;
import java.io.File;
import java.io.InputStream;

final class DirClassPath implements ClassPath
{
    String directory;
    
    DirClassPath(final String dirName) {
        this.directory = dirName;
    }
    
    public InputStream openClassfile(final String classname) {
        try {
            final char sep = File.separatorChar;
            final String filename = this.directory + sep + classname.replace('.', sep) + ".class";
            return new FileInputStream(filename.toString());
        }
        catch (FileNotFoundException e) {}
        catch (SecurityException ex) {}
        return null;
    }
    
    public URL find(final String classname) {
        final char sep = File.separatorChar;
        final String filename = this.directory + sep + classname.replace('.', sep) + ".class";
        final File f = new File(filename);
        if (f.exists()) {
            try {
                return f.getCanonicalFile().toURI().toURL();
            }
            catch (MalformedURLException e) {}
            catch (IOException ex) {}
        }
        return null;
    }
    
    public void close() {
    }
    
    public String toString() {
        return this.directory;
    }
}
