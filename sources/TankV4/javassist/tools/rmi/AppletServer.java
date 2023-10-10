package javassist.tools.rmi;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InvalidClassException;
import java.io.NotSerializableException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.Hashtable;
import java.util.Vector;
import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.NotFoundException;
import javassist.tools.web.BadHttpRequest;
import javassist.tools.web.Webserver;

public class AppletServer extends Webserver {
   private StubGenerator stubGen;
   private Hashtable exportedNames;
   private Vector exportedObjects;
   private static final byte[] okHeader = "HTTP/1.0 200 OK\r\n\r\n".getBytes();

   public AppletServer(String var1) throws IOException, NotFoundException, CannotCompileException {
      this(Integer.parseInt(var1));
   }

   public AppletServer(int var1) throws IOException, NotFoundException, CannotCompileException {
      this(ClassPool.getDefault(), new StubGenerator(), var1);
   }

   public AppletServer(int var1, ClassPool var2) throws IOException, NotFoundException, CannotCompileException {
      this(new ClassPool(var2), new StubGenerator(), var1);
   }

   private AppletServer(ClassPool var1, StubGenerator var2, int var3) throws IOException, NotFoundException, CannotCompileException {
      super(var3);
      this.exportedNames = new Hashtable();
      this.exportedObjects = new Vector();
      this.stubGen = var2;
      this.addTranslator(var1, var2);
   }

   public void run() {
      super.run();
   }

   public synchronized int exportObject(String var1, Object var2) throws CannotCompileException {
      Class var3 = var2.getClass();
      ExportedObject var4 = new ExportedObject();
      var4.object = var2;
      var4.methods = var3.getMethods();
      this.exportedObjects.addElement(var4);
      var4.identifier = this.exportedObjects.size() - 1;
      if (var1 != null) {
         this.exportedNames.put(var1, var4);
      }

      try {
         this.stubGen.makeProxyClass(var3);
      } catch (NotFoundException var6) {
         throw new CannotCompileException(var6);
      }

      return var4.identifier;
   }

   public void doReply(InputStream var1, OutputStream var2, String var3) throws IOException, BadHttpRequest {
      if (var3.startsWith("POST /rmi ")) {
         this.processRMI(var1, var2);
      } else if (var3.startsWith("POST /lookup ")) {
         this.lookupName(var3, var1, var2);
      } else {
         super.doReply(var1, var2, var3);
      }

   }

   private void processRMI(InputStream var1, OutputStream var2) throws IOException {
      ObjectInputStream var3 = new ObjectInputStream(var1);
      int var4 = var3.readInt();
      int var5 = var3.readInt();
      Exception var6 = null;
      Object var7 = null;

      try {
         ExportedObject var8 = (ExportedObject)this.exportedObjects.elementAt(var4);
         Object[] var9 = this.readParameters(var3);
         var7 = this.convertRvalue(var8.methods[var5].invoke(var8.object, var9));
      } catch (Exception var12) {
         var6 = var12;
         this.logging2(var12.toString());
      }

      var2.write(okHeader);
      ObjectOutputStream var13 = new ObjectOutputStream(var2);
      if (var6 != null) {
         var13.writeBoolean(false);
         var13.writeUTF(var6.toString());
      } else {
         try {
            var13.writeBoolean(true);
            var13.writeObject(var7);
         } catch (NotSerializableException var10) {
            this.logging2(var10.toString());
         } catch (InvalidClassException var11) {
            this.logging2(var11.toString());
         }
      }

      var13.flush();
      var13.close();
      var3.close();
   }

   private Object[] readParameters(ObjectInputStream var1) throws IOException, ClassNotFoundException {
      int var2 = var1.readInt();
      Object[] var3 = new Object[var2];

      for(int var4 = 0; var4 < var2; ++var4) {
         Object var5 = var1.readObject();
         if (var5 instanceof RemoteRef) {
            RemoteRef var6 = (RemoteRef)var5;
            ExportedObject var7 = (ExportedObject)this.exportedObjects.elementAt(var6.oid);
            var5 = var7.object;
         }

         var3[var4] = var5;
      }

      return var3;
   }

   private Object convertRvalue(Object var1) throws CannotCompileException {
      if (var1 == null) {
         return null;
      } else {
         String var2 = var1.getClass().getName();
         return this.stubGen.isProxyClass(var2) ? new RemoteRef(this.exportObject((String)null, var1), var2) : var1;
      }
   }

   private void lookupName(String var1, InputStream var2, OutputStream var3) throws IOException {
      ObjectInputStream var4 = new ObjectInputStream(var2);
      String var5 = DataInputStream.readUTF(var4);
      ExportedObject var6 = (ExportedObject)this.exportedNames.get(var5);
      var3.write(okHeader);
      ObjectOutputStream var7 = new ObjectOutputStream(var3);
      if (var6 == null) {
         this.logging2(var5 + "not found.");
         var7.writeInt(-1);
         var7.writeUTF("error");
      } else {
         this.logging2(var5);
         var7.writeInt(var6.identifier);
         var7.writeUTF(var6.object.getClass().getName());
      }

      var7.flush();
      var7.close();
      var4.close();
   }
}
