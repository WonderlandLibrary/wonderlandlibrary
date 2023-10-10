/*
 * Decompiled with CFR 0.152.
 */
package com.viaversion.viaversion.libs.javassist;

import com.viaversion.viaversion.libs.javassist.ClassPath;
import com.viaversion.viaversion.libs.javassist.ClassPoolTail;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class URLClassPath
implements ClassPath {
    protected String hostname;
    protected int port;
    protected String directory;
    protected String packageName;

    public URLClassPath(String host, int port, String directory, String packageName) {
        this.hostname = host;
        this.port = port;
        this.directory = directory;
        this.packageName = packageName;
    }

    public String toString() {
        return this.hostname + ":" + this.port + this.directory;
    }

    @Override
    public InputStream openClassfile(String classname) {
        try {
            URLConnection con = this.openClassfile0(classname);
            if (con != null) {
                return con.getInputStream();
            }
        }
        catch (IOException iOException) {
            // empty catch block
        }
        return null;
    }

    private URLConnection openClassfile0(String classname) throws IOException {
        if (this.packageName == null || classname.startsWith(this.packageName)) {
            String jarname = this.directory + classname.replace('.', '/') + ".class";
            return URLClassPath.fetchClass0(this.hostname, this.port, jarname);
        }
        return null;
    }

    @Override
    public URL find(String classname) {
        try {
            URLConnection con = this.openClassfile0(classname);
            InputStream is = con.getInputStream();
            if (is != null) {
                is.close();
                return con.getURL();
            }
        }
        catch (IOException iOException) {
            // empty catch block
        }
        return null;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static byte[] fetchClass(String host, int port, String directory, String classname) throws IOException {
        byte[] b2;
        URLConnection con = URLClassPath.fetchClass0(host, port, directory + classname.replace('.', '/') + ".class");
        int size = con.getContentLength();
        try (InputStream s2 = con.getInputStream();){
            if (size <= 0) {
                b2 = ClassPoolTail.readStream(s2);
            } else {
                int n2;
                b2 = new byte[size];
                int len = 0;
                do {
                    if ((n2 = s2.read(b2, len, size - len)) >= 0) continue;
                    throw new IOException("the stream was closed: " + classname);
                } while ((len += n2) < size);
            }
        }
        return b2;
    }

    private static URLConnection fetchClass0(String host, int port, String filename) throws IOException {
        URL url;
        try {
            url = new URL("http", host, port, filename);
        }
        catch (MalformedURLException e2) {
            throw new IOException("invalid URL?");
        }
        URLConnection con = url.openConnection();
        con.connect();
        return con;
    }
}

