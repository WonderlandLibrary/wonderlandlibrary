// 
// Decompiled by Procyon v0.5.36
// 

package javazoom.jl.decoder;

import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.InputStream;

public class JavaLayerUtils
{
    private static JavaLayerHook hook;
    
    public static Object deserialize(final InputStream in, final Class cls) throws IOException {
        if (cls == null) {
            throw new NullPointerException("cls");
        }
        final Object obj = deserialize(in, cls);
        if (!cls.isInstance(obj)) {
            throw new InvalidObjectException("type of deserialized instance not of required class.");
        }
        return obj;
    }
    
    public static Object deserialize(final InputStream in) throws IOException {
        if (in == null) {
            throw new NullPointerException("in");
        }
        final ObjectInputStream objIn = new ObjectInputStream(in);
        Object obj;
        try {
            obj = objIn.readObject();
        }
        catch (ClassNotFoundException ex) {
            throw new InvalidClassException(ex.toString());
        }
        return obj;
    }
    
    public static Object deserializeArray(final InputStream in, final Class elemType, final int length) throws IOException {
        if (elemType == null) {
            throw new NullPointerException("elemType");
        }
        if (length < -1) {
            throw new IllegalArgumentException("length");
        }
        final Object obj = deserialize(in);
        final Class cls = obj.getClass();
        if (!cls.isArray()) {
            throw new InvalidObjectException("object is not an array");
        }
        final Class arrayElemType = cls.getComponentType();
        if (arrayElemType != elemType) {
            throw new InvalidObjectException("unexpected array component type");
        }
        if (length != -1) {
            final int arrayLength = Array.getLength(obj);
            if (arrayLength != length) {
                throw new InvalidObjectException("array length mismatch");
            }
        }
        return obj;
    }
    
    public static Object deserializeArrayResource(final String name, final Class elemType, final int length) throws IOException {
        final InputStream str = getResourceAsStream(name);
        if (str == null) {
            throw new IOException("unable to load resource '" + name + "'");
        }
        final Object obj = deserializeArray(str, elemType, length);
        return obj;
    }
    
    public static void serialize(final OutputStream out, final Object obj) throws IOException {
        if (out == null) {
            throw new NullPointerException("out");
        }
        if (obj == null) {
            throw new NullPointerException("obj");
        }
        final ObjectOutputStream objOut = new ObjectOutputStream(out);
        objOut.writeObject(obj);
    }
    
    public static synchronized void setHook(final JavaLayerHook hook0) {
        JavaLayerUtils.hook = hook0;
    }
    
    public static synchronized JavaLayerHook getHook() {
        return JavaLayerUtils.hook;
    }
    
    public static synchronized InputStream getResourceAsStream(final String name) {
        InputStream is = null;
        if (JavaLayerUtils.hook != null) {
            is = JavaLayerUtils.hook.getResourceAsStream(name);
        }
        else {
            final Class cls = JavaLayerUtils.class;
            is = cls.getResourceAsStream(name);
        }
        return is;
    }
    
    static {
        JavaLayerUtils.hook = null;
    }
}
