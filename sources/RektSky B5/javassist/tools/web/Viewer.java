/*
 * Decompiled with CFR 0.152.
 */
package javassist.tools.web;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.net.URLConnection;

public class Viewer
extends ClassLoader {
    private String server;
    private int port;

    public static void main(String[] args) throws Throwable {
        if (args.length >= 3) {
            Viewer cl = new Viewer(args[0], Integer.parseInt(args[1]));
            String[] args2 = new String[args.length - 3];
            System.arraycopy(args, 3, args2, 0, args.length - 3);
            cl.run(args[2], args2);
        } else {
            System.err.println("Usage: java javassist.tools.web.Viewer <host> <port> class [args ...]");
        }
    }

    public Viewer(String host, int p2) {
        this.server = host;
        this.port = p2;
    }

    public String getServer() {
        return this.server;
    }

    public int getPort() {
        return this.port;
    }

    public void run(String classname, String[] args) throws Throwable {
        Class<?> c2 = this.loadClass(classname);
        try {
            c2.getDeclaredMethod("main", String[].class).invoke(null, new Object[]{args});
        }
        catch (InvocationTargetException e2) {
            throw e2.getTargetException();
        }
    }

    @Override
    protected synchronized Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        Class<?> c2 = this.findLoadedClass(name);
        if (c2 == null) {
            c2 = this.findClass(name);
        }
        if (c2 == null) {
            throw new ClassNotFoundException(name);
        }
        if (resolve) {
            this.resolveClass(c2);
        }
        return c2;
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        Class<?> c2 = null;
        if (name.startsWith("java.") || name.startsWith("javax.") || name.equals("javassist.tools.web.Viewer")) {
            c2 = this.findSystemClass(name);
        }
        if (c2 == null) {
            try {
                byte[] b2 = this.fetchClass(name);
                if (b2 != null) {
                    c2 = this.defineClass(name, b2, 0, b2.length);
                }
            }
            catch (Exception exception) {
                // empty catch block
            }
        }
        return c2;
    }

    protected byte[] fetchClass(String classname) throws Exception {
        byte[] b2;
        URL url = new URL("http", this.server, this.port, "/" + classname.replace('.', '/') + ".class");
        URLConnection con = url.openConnection();
        con.connect();
        int size = con.getContentLength();
        InputStream s2 = con.getInputStream();
        if (size <= 0) {
            b2 = this.readStream(s2);
        } else {
            int n2;
            b2 = new byte[size];
            int len = 0;
            do {
                if ((n2 = s2.read(b2, len, size - len)) >= 0) continue;
                s2.close();
                throw new IOException("the stream was closed: " + classname);
            } while ((len += n2) < size);
        }
        s2.close();
        return b2;
    }

    private byte[] readStream(InputStream fin) throws IOException {
        byte[] buf = new byte[4096];
        int size = 0;
        int len = 0;
        do {
            if (buf.length - (size += len) > 0) continue;
            byte[] newbuf = new byte[buf.length * 2];
            System.arraycopy(buf, 0, newbuf, 0, size);
            buf = newbuf;
        } while ((len = fin.read(buf, size, buf.length - size)) >= 0);
        byte[] result = new byte[size];
        System.arraycopy(buf, 0, result, 0, size);
        return result;
    }
}

