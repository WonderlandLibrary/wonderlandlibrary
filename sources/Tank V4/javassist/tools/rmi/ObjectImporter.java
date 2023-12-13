package javassist.tools.rmi;

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

public class ObjectImporter implements Serializable {
   private final byte[] endofline = new byte[]{13, 10};
   private String servername;
   private String orgServername;
   private int port;
   private int orgPort;
   protected byte[] lookupCommand = "POST /lookup HTTP/1.0".getBytes();
   protected byte[] rmiCommand = "POST /rmi HTTP/1.0".getBytes();
   private static final Class[] proxyConstructorParamTypes;

   public ObjectImporter(Applet var1) {
      URL var2 = var1.getCodeBase();
      this.orgServername = this.servername = var2.getHost();
      this.orgPort = this.port = var2.getPort();
   }

   public ObjectImporter(String var1, int var2) {
      this.orgServername = this.servername = var1;
      this.orgPort = this.port = var2;
   }

   public Object getObject(String var1) {
      try {
         return this.lookupObject(var1);
      } catch (ObjectNotFoundException var3) {
         return null;
      }
   }

   public void setHttpProxy(String var1, int var2) {
      String var3 = "POST http://" + this.orgServername + ":" + this.orgPort;
      String var4 = var3 + "/lookup HTTP/1.0";
      this.lookupCommand = var4.getBytes();
      var4 = var3 + "/rmi HTTP/1.0";
      this.rmiCommand = var4.getBytes();
      this.servername = var1;
      this.port = var2;
   }

   public Object lookupObject(String var1) throws ObjectNotFoundException {
      try {
         Socket var2 = new Socket(this.servername, this.port);
         OutputStream var3 = var2.getOutputStream();
         var3.write(this.lookupCommand);
         var3.write(this.endofline);
         var3.write(this.endofline);
         ObjectOutputStream var4 = new ObjectOutputStream(var3);
         var4.writeUTF(var1);
         var4.flush();
         BufferedInputStream var5 = new BufferedInputStream(var2.getInputStream());
         this.skipHeader(var5);
         ObjectInputStream var6 = new ObjectInputStream(var5);
         int var7 = var6.readInt();
         String var8 = var6.readUTF();
         var6.close();
         var4.close();
         var2.close();
         if (var7 >= 0) {
            return this.createProxy(var7, var8);
         }
      } catch (Exception var9) {
         var9.printStackTrace();
         throw new ObjectNotFoundException(var1, var9);
      }

      throw new ObjectNotFoundException(var1);
   }

   private Object createProxy(int var1, String var2) throws Exception {
      Class var3 = Class.forName(var2);
      Constructor var4 = var3.getConstructor(proxyConstructorParamTypes);
      return var4.newInstance(this, new Integer(var1));
   }

   public Object call(int var1, int var2, Object[] var3) throws RemoteException {
      boolean var4;
      Object var5;
      String var6;
      try {
         Socket var7 = new Socket(this.servername, this.port);
         BufferedOutputStream var8 = new BufferedOutputStream(var7.getOutputStream());
         var8.write(this.rmiCommand);
         var8.write(this.endofline);
         var8.write(this.endofline);
         ObjectOutputStream var9 = new ObjectOutputStream(var8);
         var9.writeInt(var1);
         var9.writeInt(var2);
         this.writeParameters(var9, var3);
         var9.flush();
         BufferedInputStream var10 = new BufferedInputStream(var7.getInputStream());
         this.skipHeader(var10);
         ObjectInputStream var11 = new ObjectInputStream(var10);
         var4 = var11.readBoolean();
         var5 = null;
         var6 = null;
         if (var4) {
            var5 = var11.readObject();
         } else {
            var6 = var11.readUTF();
         }

         var11.close();
         var9.close();
         var7.close();
         if (var5 instanceof RemoteRef) {
            RemoteRef var12 = (RemoteRef)var5;
            var5 = this.createProxy(var12.oid, var12.classname);
         }
      } catch (ClassNotFoundException var13) {
         throw new RemoteException(var13);
      } catch (IOException var14) {
         throw new RemoteException(var14);
      } catch (Exception var15) {
         throw new RemoteException(var15);
      }

      if (var4) {
         return var5;
      } else {
         throw new RemoteException(var6);
      }
   }

   private void skipHeader(InputStream var1) throws IOException {
      int var2;
      do {
         int var3;
         for(var2 = 0; (var3 = var1.read()) >= 0 && var3 != 13; ++var2) {
         }

         var1.read();
      } while(var2 > 0);

   }

   private void writeParameters(ObjectOutputStream var1, Object[] var2) throws IOException {
      int var3 = var2.length;
      var1.writeInt(var3);

      for(int var4 = 0; var4 < var3; ++var4) {
         if (var2[var4] instanceof Proxy) {
            Proxy var5 = (Proxy)var2[var4];
            var1.writeObject(new RemoteRef(var5._getObjectId()));
         } else {
            var1.writeObject(var2[var4]);
         }
      }

   }

   static {
      proxyConstructorParamTypes = new Class[]{ObjectImporter.class, Integer.TYPE};
   }
}
