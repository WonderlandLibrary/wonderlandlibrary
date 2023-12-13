/*
 * Decompiled with CFR 0.152.
 */
package com.viaversion.viaversion.libs.javassist.tools.rmi;

import com.viaversion.viaversion.libs.javassist.tools.rmi.ObjectNotFoundException;
import com.viaversion.viaversion.libs.javassist.tools.rmi.Proxy;
import com.viaversion.viaversion.libs.javassist.tools.rmi.RemoteException;
import com.viaversion.viaversion.libs.javassist.tools.rmi.RemoteRef;
import java.applet.Applet;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.net.Socket;
import java.net.URL;

public class ObjectImporter
implements Serializable {
    private static final long serialVersionUID = 1L;
    private final byte[] endofline = new byte[]{13, 10};
    private String servername;
    private String orgServername;
    private int port;
    private int orgPort;
    protected byte[] lookupCommand = "POST /lookup HTTP/1.0".getBytes();
    protected byte[] rmiCommand = "POST /rmi HTTP/1.0".getBytes();
    private static final Class<?>[] proxyConstructorParamTypes = new Class[]{ObjectImporter.class, Integer.TYPE};

    public ObjectImporter(Applet applet) {
        URL codebase = applet.getCodeBase();
        this.orgServername = this.servername = codebase.getHost();
        this.orgPort = this.port = codebase.getPort();
    }

    public ObjectImporter(String servername, int port) {
        this.orgServername = this.servername = servername;
        this.orgPort = this.port = port;
    }

    public Object getObject(String name) {
        try {
            return this.lookupObject(name);
        }
        catch (ObjectNotFoundException e2) {
            return null;
        }
    }

    public void setHttpProxy(String host, int port) {
        String proxyHeader = "POST http://" + this.orgServername + ":" + this.orgPort;
        String cmd = proxyHeader + "/lookup HTTP/1.0";
        this.lookupCommand = cmd.getBytes();
        cmd = proxyHeader + "/rmi HTTP/1.0";
        this.rmiCommand = cmd.getBytes();
        this.servername = host;
        this.port = port;
    }

    public Object lookupObject(String name) throws ObjectNotFoundException {
        try {
            Socket sock = new Socket(this.servername, this.port);
            OutputStream out = sock.getOutputStream();
            out.write(this.lookupCommand);
            out.write(this.endofline);
            out.write(this.endofline);
            ObjectOutputStream dout = new ObjectOutputStream(out);
            dout.writeUTF(name);
            dout.flush();
            BufferedInputStream in = new BufferedInputStream(sock.getInputStream());
            this.skipHeader(in);
            ObjectInputStream din = new ObjectInputStream(in);
            int n2 = din.readInt();
            String classname = din.readUTF();
            din.close();
            dout.close();
            sock.close();
            if (n2 >= 0) {
                return this.createProxy(n2, classname);
            }
        }
        catch (Exception e2) {
            e2.printStackTrace();
            throw new ObjectNotFoundException(name, e2);
        }
        throw new ObjectNotFoundException(name);
    }

    private Object createProxy(int oid, String classname) throws Exception {
        Class<?> c2 = Class.forName(classname);
        Constructor<?> cons = c2.getConstructor(proxyConstructorParamTypes);
        return cons.newInstance(this, oid);
    }

    public Object call(int objectid, int methodid, Object[] args) throws RemoteException {
        String errmsg;
        Object rvalue;
        boolean result;
        try {
            Socket sock = new Socket(this.servername, this.port);
            BufferedOutputStream out = new BufferedOutputStream(sock.getOutputStream());
            ((OutputStream)out).write(this.rmiCommand);
            ((OutputStream)out).write(this.endofline);
            ((OutputStream)out).write(this.endofline);
            ObjectOutputStream dout = new ObjectOutputStream(out);
            dout.writeInt(objectid);
            dout.writeInt(methodid);
            this.writeParameters(dout, args);
            dout.flush();
            BufferedInputStream ins = new BufferedInputStream(sock.getInputStream());
            this.skipHeader(ins);
            ObjectInputStream din = new ObjectInputStream(ins);
            result = din.readBoolean();
            rvalue = null;
            errmsg = null;
            if (result) {
                rvalue = din.readObject();
            } else {
                errmsg = din.readUTF();
            }
            din.close();
            dout.close();
            sock.close();
            if (rvalue instanceof RemoteRef) {
                RemoteRef ref = (RemoteRef)rvalue;
                rvalue = this.createProxy(ref.oid, ref.classname);
            }
        }
        catch (ClassNotFoundException e2) {
            throw new RemoteException(e2);
        }
        catch (IOException e3) {
            throw new RemoteException(e3);
        }
        catch (Exception e4) {
            throw new RemoteException(e4);
        }
        if (result) {
            return rvalue;
        }
        throw new RemoteException(errmsg);
    }

    private void skipHeader(InputStream in) throws IOException {
        int len;
        do {
            int c2;
            len = 0;
            while ((c2 = in.read()) >= 0 && c2 != 13) {
                ++len;
            }
            in.read();
        } while (len > 0);
    }

    private void writeParameters(ObjectOutputStream dout, Object[] params) throws IOException {
        int n2 = params.length;
        dout.writeInt(n2);
        for (int i2 = 0; i2 < n2; ++i2) {
            if (params[i2] instanceof Proxy) {
                Proxy p2 = (Proxy)params[i2];
                dout.writeObject(new RemoteRef(p2._getObjectId()));
                continue;
            }
            dout.writeObject(params[i2]);
        }
    }
}

