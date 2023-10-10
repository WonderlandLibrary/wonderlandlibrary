// 
// Decompiled by Procyon v0.5.36
// 

package com.sun.jna.platform.linux;

import java.util.Set;
import java.util.LinkedHashSet;
import java.util.Collection;
import com.sun.jna.Pointer;
import com.sun.jna.Memory;
import java.nio.charset.Charset;
import java.io.IOException;
import com.sun.jna.Native;

public abstract class XAttrUtil
{
    private XAttrUtil() {
    }
    
    public static void setXAttr(final String path, final String name, final String value) throws IOException {
        setXAttr(path, name, value, Native.getDefaultStringEncoding());
    }
    
    public static void setXAttr(final String path, final String name, final String value, final String encoding) throws IOException {
        setXAttr(path, name, value.getBytes(encoding));
    }
    
    public static void setXAttr(final String path, final String name, final byte[] value) throws IOException {
        final int retval = XAttr.INSTANCE.setxattr(path, name, value, new XAttr.size_t((long)value.length), 0);
        if (retval != 0) {
            final int eno = Native.getLastError();
            throw new IOException("errno: " + eno);
        }
    }
    
    public static void lSetXAttr(final String path, final String name, final String value) throws IOException {
        lSetXAttr(path, name, value, Native.getDefaultStringEncoding());
    }
    
    public static void lSetXAttr(final String path, final String name, final String value, final String encoding) throws IOException {
        lSetXAttr(path, name, value.getBytes(encoding));
    }
    
    public static void lSetXAttr(final String path, final String name, final byte[] value) throws IOException {
        final int retval = XAttr.INSTANCE.lsetxattr(path, name, value, new XAttr.size_t((long)value.length), 0);
        if (retval != 0) {
            final int eno = Native.getLastError();
            throw new IOException("errno: " + eno);
        }
    }
    
    public static void fSetXAttr(final int fd, final String name, final String value) throws IOException {
        fSetXAttr(fd, name, value, Native.getDefaultStringEncoding());
    }
    
    public static void fSetXAttr(final int fd, final String name, final String value, final String encoding) throws IOException {
        fSetXAttr(fd, name, value.getBytes(encoding));
    }
    
    public static void fSetXAttr(final int fd, final String name, final byte[] value) throws IOException {
        final int retval = XAttr.INSTANCE.fsetxattr(fd, name, value, new XAttr.size_t((long)value.length), 0);
        if (retval != 0) {
            final int eno = Native.getLastError();
            throw new IOException("errno: " + eno);
        }
    }
    
    public static String getXAttr(final String path, final String name) throws IOException {
        return getXAttr(path, name, Native.getDefaultStringEncoding());
    }
    
    public static String getXAttr(final String path, final String name, final String encoding) throws IOException {
        final byte[] valueMem = getXAttrBytes(path, name);
        return new String(valueMem, Charset.forName(encoding));
    }
    
    public static byte[] getXAttrBytes(final String path, final String name) throws IOException {
        int eno = 0;
        XAttr.ssize_t retval;
        byte[] valueMem;
        do {
            retval = XAttr.INSTANCE.getxattr(path, name, (byte[])null, XAttr.size_t.ZERO);
            if (retval.longValue() < 0L) {
                eno = Native.getLastError();
                throw new IOException("errno: " + eno);
            }
            valueMem = new byte[retval.intValue()];
            retval = XAttr.INSTANCE.getxattr(path, name, valueMem, new XAttr.size_t((long)valueMem.length));
            if (retval.longValue() >= 0L) {
                continue;
            }
            eno = Native.getLastError();
            if (eno != 34) {
                throw new IOException("errno: " + eno);
            }
        } while (retval.longValue() < 0L && eno == 34);
        return valueMem;
    }
    
    public static Memory getXAttrAsMemory(final String path, final String name) throws IOException {
        int eno = 0;
        XAttr.ssize_t retval;
        Memory valueMem;
        do {
            retval = XAttr.INSTANCE.getxattr(path, name, (Pointer)null, XAttr.size_t.ZERO);
            if (retval.longValue() < 0L) {
                eno = Native.getLastError();
                throw new IOException("errno: " + eno);
            }
            if (retval.longValue() == 0L) {
                return null;
            }
            valueMem = new Memory(retval.longValue());
            retval = XAttr.INSTANCE.getxattr(path, name, valueMem, new XAttr.size_t(valueMem.size()));
            if (retval.longValue() >= 0L) {
                continue;
            }
            eno = Native.getLastError();
            if (eno != 34) {
                throw new IOException("errno: " + eno);
            }
        } while (retval.longValue() < 0L && eno == 34);
        return valueMem;
    }
    
    public static String lGetXAttr(final String path, final String name) throws IOException {
        return lGetXAttr(path, name, Native.getDefaultStringEncoding());
    }
    
    public static String lGetXAttr(final String path, final String name, final String encoding) throws IOException {
        final byte[] valueMem = lGetXAttrBytes(path, name);
        return new String(valueMem, Charset.forName(encoding));
    }
    
    public static byte[] lGetXAttrBytes(final String path, final String name) throws IOException {
        int eno = 0;
        XAttr.ssize_t retval;
        byte[] valueMem;
        do {
            retval = XAttr.INSTANCE.lgetxattr(path, name, (byte[])null, XAttr.size_t.ZERO);
            if (retval.longValue() < 0L) {
                eno = Native.getLastError();
                throw new IOException("errno: " + eno);
            }
            valueMem = new byte[retval.intValue()];
            retval = XAttr.INSTANCE.lgetxattr(path, name, valueMem, new XAttr.size_t((long)valueMem.length));
            if (retval.longValue() >= 0L) {
                continue;
            }
            eno = Native.getLastError();
            if (eno != 34) {
                throw new IOException("errno: " + eno);
            }
        } while (retval.longValue() < 0L && eno == 34);
        return valueMem;
    }
    
    public static Memory lGetXAttrAsMemory(final String path, final String name) throws IOException {
        int eno = 0;
        XAttr.ssize_t retval;
        Memory valueMem;
        do {
            retval = XAttr.INSTANCE.lgetxattr(path, name, (Pointer)null, XAttr.size_t.ZERO);
            if (retval.longValue() < 0L) {
                eno = Native.getLastError();
                throw new IOException("errno: " + eno);
            }
            if (retval.longValue() == 0L) {
                return null;
            }
            valueMem = new Memory(retval.longValue());
            retval = XAttr.INSTANCE.lgetxattr(path, name, valueMem, new XAttr.size_t(valueMem.size()));
            if (retval.longValue() >= 0L) {
                continue;
            }
            eno = Native.getLastError();
            if (eno != 34) {
                throw new IOException("errno: " + eno);
            }
        } while (retval.longValue() < 0L && eno == 34);
        return valueMem;
    }
    
    public static String fGetXAttr(final int fd, final String name) throws IOException {
        return fGetXAttr(fd, name, Native.getDefaultStringEncoding());
    }
    
    public static String fGetXAttr(final int fd, final String name, final String encoding) throws IOException {
        final byte[] valueMem = fGetXAttrBytes(fd, name);
        return new String(valueMem, Charset.forName(encoding));
    }
    
    public static byte[] fGetXAttrBytes(final int fd, final String name) throws IOException {
        int eno = 0;
        XAttr.ssize_t retval;
        byte[] valueMem;
        do {
            retval = XAttr.INSTANCE.fgetxattr(fd, name, (byte[])null, XAttr.size_t.ZERO);
            if (retval.longValue() < 0L) {
                eno = Native.getLastError();
                throw new IOException("errno: " + eno);
            }
            valueMem = new byte[retval.intValue()];
            retval = XAttr.INSTANCE.fgetxattr(fd, name, valueMem, new XAttr.size_t((long)valueMem.length));
            if (retval.longValue() >= 0L) {
                continue;
            }
            eno = Native.getLastError();
            if (eno != 34) {
                throw new IOException("errno: " + eno);
            }
        } while (retval.longValue() < 0L && eno == 34);
        return valueMem;
    }
    
    public static Memory fGetXAttrAsMemory(final int fd, final String name) throws IOException {
        int eno = 0;
        XAttr.ssize_t retval;
        Memory valueMem;
        do {
            retval = XAttr.INSTANCE.fgetxattr(fd, name, (Pointer)null, XAttr.size_t.ZERO);
            if (retval.longValue() < 0L) {
                eno = Native.getLastError();
                throw new IOException("errno: " + eno);
            }
            if (retval.longValue() == 0L) {
                return null;
            }
            valueMem = new Memory(retval.longValue());
            retval = XAttr.INSTANCE.fgetxattr(fd, name, valueMem, new XAttr.size_t(valueMem.size()));
            if (retval.longValue() >= 0L) {
                continue;
            }
            eno = Native.getLastError();
            if (eno != 34) {
                throw new IOException("errno: " + eno);
            }
        } while (retval.longValue() < 0L && eno == 34);
        return valueMem;
    }
    
    public static Collection<String> listXAttr(final String path) throws IOException {
        return listXAttr(path, Native.getDefaultStringEncoding());
    }
    
    public static Collection<String> listXAttr(final String path, final String encoding) throws IOException {
        int eno = 0;
        XAttr.ssize_t retval;
        byte[] listMem;
        do {
            retval = XAttr.INSTANCE.listxattr(path, (byte[])null, XAttr.size_t.ZERO);
            if (retval.longValue() < 0L) {
                eno = Native.getLastError();
                throw new IOException("errno: " + eno);
            }
            listMem = new byte[retval.intValue()];
            retval = XAttr.INSTANCE.listxattr(path, listMem, new XAttr.size_t((long)listMem.length));
            if (retval.longValue() >= 0L) {
                continue;
            }
            eno = Native.getLastError();
            if (eno != 34) {
                throw new IOException("errno: " + eno);
            }
        } while (retval.longValue() < 0L && eno == 34);
        return splitBufferToStrings(listMem, encoding);
    }
    
    public static Collection<String> lListXAttr(final String path) throws IOException {
        return lListXAttr(path, Native.getDefaultStringEncoding());
    }
    
    public static Collection<String> lListXAttr(final String path, final String encoding) throws IOException {
        int eno = 0;
        XAttr.ssize_t retval;
        byte[] listMem;
        do {
            retval = XAttr.INSTANCE.llistxattr(path, (byte[])null, XAttr.size_t.ZERO);
            if (retval.longValue() < 0L) {
                eno = Native.getLastError();
                throw new IOException("errno: " + eno);
            }
            listMem = new byte[retval.intValue()];
            retval = XAttr.INSTANCE.llistxattr(path, listMem, new XAttr.size_t((long)listMem.length));
            if (retval.longValue() >= 0L) {
                continue;
            }
            eno = Native.getLastError();
            if (eno != 34) {
                throw new IOException("errno: " + eno);
            }
        } while (retval.longValue() < 0L && eno == 34);
        return splitBufferToStrings(listMem, encoding);
    }
    
    public static Collection<String> fListXAttr(final int fd) throws IOException {
        return fListXAttr(fd, Native.getDefaultStringEncoding());
    }
    
    public static Collection<String> fListXAttr(final int fd, final String encoding) throws IOException {
        int eno = 0;
        XAttr.ssize_t retval;
        byte[] listMem;
        do {
            retval = XAttr.INSTANCE.flistxattr(fd, (byte[])null, XAttr.size_t.ZERO);
            if (retval.longValue() < 0L) {
                eno = Native.getLastError();
                throw new IOException("errno: " + eno);
            }
            listMem = new byte[retval.intValue()];
            retval = XAttr.INSTANCE.flistxattr(fd, listMem, new XAttr.size_t((long)listMem.length));
            if (retval.longValue() >= 0L) {
                continue;
            }
            eno = Native.getLastError();
            if (eno != 34) {
                throw new IOException("errno: " + eno);
            }
        } while (retval.longValue() < 0L && eno == 34);
        return splitBufferToStrings(listMem, encoding);
    }
    
    public static void removeXAttr(final String path, final String name) throws IOException {
        final int retval = XAttr.INSTANCE.removexattr(path, name);
        if (retval != 0) {
            final int eno = Native.getLastError();
            throw new IOException("errno: " + eno);
        }
    }
    
    public static void lRemoveXAttr(final String path, final String name) throws IOException {
        final int retval = XAttr.INSTANCE.lremovexattr(path, name);
        if (retval != 0) {
            final int eno = Native.getLastError();
            throw new IOException("errno: " + eno);
        }
    }
    
    public static void fRemoveXAttr(final int fd, final String name) throws IOException {
        final int retval = XAttr.INSTANCE.fremovexattr(fd, name);
        if (retval != 0) {
            final int eno = Native.getLastError();
            throw new IOException("errno: " + eno);
        }
    }
    
    private static Collection<String> splitBufferToStrings(final byte[] valueMem, final String encoding) throws IOException {
        final Charset charset = Charset.forName(encoding);
        final Set<String> attributesList = new LinkedHashSet<String>(1);
        int offset = 0;
        for (int i = 0; i < valueMem.length; ++i) {
            if (valueMem[i] == 0) {
                final String name = new String(valueMem, offset, i - offset, charset);
                attributesList.add(name);
                offset = i + 1;
            }
        }
        return attributesList;
    }
}
