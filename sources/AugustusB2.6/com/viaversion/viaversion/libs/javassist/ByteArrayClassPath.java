// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.javassist;

import java.io.IOException;
import java.net.URLConnection;
import java.net.MalformedURLException;
import java.net.URLStreamHandler;
import java.net.URL;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class ByteArrayClassPath implements ClassPath
{
    protected String classname;
    protected byte[] classfile;
    
    public ByteArrayClassPath(final String name, final byte[] classfile) {
        this.classname = name;
        this.classfile = classfile;
    }
    
    @Override
    public String toString() {
        return "byte[]:" + this.classname;
    }
    
    @Override
    public InputStream openClassfile(final String classname) {
        if (this.classname.equals(classname)) {
            return new ByteArrayInputStream(this.classfile);
        }
        return null;
    }
    
    @Override
    public URL find(final String classname) {
        if (this.classname.equals(classname)) {
            final String cname = classname.replace('.', '/') + ".class";
            try {
                return new URL(null, "file:/ByteArrayClassPath/" + cname, new BytecodeURLStreamHandler());
            }
            catch (MalformedURLException ex) {}
        }
        return null;
    }
    
    private class BytecodeURLStreamHandler extends URLStreamHandler
    {
        @Override
        protected URLConnection openConnection(final URL u) {
            return new BytecodeURLConnection(u);
        }
    }
    
    private class BytecodeURLConnection extends URLConnection
    {
        protected BytecodeURLConnection(final URL url) {
            super(url);
        }
        
        @Override
        public void connect() throws IOException {
        }
        
        @Override
        public InputStream getInputStream() throws IOException {
            return new ByteArrayInputStream(ByteArrayClassPath.this.classfile);
        }
        
        @Override
        public int getContentLength() {
            return ByteArrayClassPath.this.classfile.length;
        }
    }
}
