package javassist.util;

import com.sun.jdi.Bootstrap;
import com.sun.jdi.ReferenceType;
import com.sun.jdi.VirtualMachine;
import com.sun.jdi.connect.AttachingConnector;
import com.sun.jdi.connect.Connector;
import com.sun.jdi.connect.IllegalConnectorArgumentsException;
import com.sun.jdi.connect.Connector.Argument;
import com.sun.jdi.event.Event;
import com.sun.jdi.event.EventIterator;
import com.sun.jdi.event.EventQueue;
import com.sun.jdi.event.EventSet;
import com.sun.jdi.event.MethodEntryEvent;
import com.sun.jdi.request.EventRequestManager;
import com.sun.jdi.request.MethodEntryRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

public class HotSwapper {
   private VirtualMachine jvm;
   private MethodEntryRequest request;
   private Map newClassFiles;
   private Trigger trigger;
   private static final String HOST_NAME = "localhost";
   private static final String TRIGGER_NAME = Trigger.class.getName();

   public HotSwapper(int var1) throws IOException, IllegalConnectorArgumentsException {
      this(Integer.toString(var1));
   }

   public HotSwapper(String var1) throws IOException, IllegalConnectorArgumentsException {
      this.jvm = null;
      this.request = null;
      this.newClassFiles = null;
      this.trigger = new Trigger();
      AttachingConnector var2 = (AttachingConnector)this.findConnector("com.sun.jdi.SocketAttach");
      Map var3 = var2.defaultArguments();
      ((Argument)var3.get("hostname")).setValue("localhost");
      ((Argument)var3.get("port")).setValue(var1);
      this.jvm = var2.attach(var3);
      EventRequestManager var4 = this.jvm.eventRequestManager();
      this.request = methodEntryRequests(var4, TRIGGER_NAME);
   }

   private Connector findConnector(String var1) throws IOException {
      List var2 = Bootstrap.virtualMachineManager().allConnectors();
      Iterator var3 = var2.iterator();

      Connector var4;
      do {
         if (!var3.hasNext()) {
            throw new IOException("Not found: " + var1);
         }

         var4 = (Connector)var3.next();
      } while(!var4.name().equals(var1));

      return var4;
   }

   private static MethodEntryRequest methodEntryRequests(EventRequestManager var0, String var1) {
      MethodEntryRequest var2 = var0.createMethodEntryRequest();
      var2.addClassFilter(var1);
      var2.setSuspendPolicy(1);
      return var2;
   }

   private void deleteEventRequest(EventRequestManager var1, MethodEntryRequest var2) {
      var1.deleteEventRequest(var2);
   }

   public void reload(String var1, byte[] var2) {
      ReferenceType var3 = this.toRefType(var1);
      HashMap var4 = new HashMap();
      var4.put(var3, var2);
      this.reload2(var4, var1);
   }

   public void reload(Map var1) {
      Set var2 = var1.entrySet();
      Iterator var3 = var2.iterator();
      HashMap var4 = new HashMap();
      String var5 = null;

      while(var3.hasNext()) {
         Entry var6 = (Entry)var3.next();
         var5 = (String)var6.getKey();
         var4.put(this.toRefType(var5), var6.getValue());
      }

      if (var5 != null) {
         this.reload2(var4, var5 + " etc.");
      }

   }

   private ReferenceType toRefType(String var1) {
      List var2 = this.jvm.classesByName(var1);
      if (var2 != null && !var2.isEmpty()) {
         return (ReferenceType)var2.get(0);
      } else {
         throw new RuntimeException("no such class: " + var1);
      }
   }

   private void reload2(Map var1, String var2) {
      Trigger var3;
      synchronized(var3 = this.trigger){}
      this.startDaemon();
      this.newClassFiles = var1;
      this.request.enable();
      this.trigger.doSwap();
      this.request.disable();
      Map var4 = this.newClassFiles;
      if (var4 != null) {
         this.newClassFiles = null;
         throw new RuntimeException("failed to reload: " + var2);
      }
   }

   private void startDaemon() {
      (new Thread(this) {
         final HotSwapper this$0;

         {
            this.this$0 = var1;
         }

         private void errorMsg(Throwable var1) {
            System.err.print("Exception in thread \"HotSwap\" ");
            var1.printStackTrace(System.err);
         }

         public void run() {
            EventSet var1 = null;

            try {
               var1 = this.this$0.waitEvent();
               EventIterator var2 = var1.eventIterator();

               while(var2.hasNext()) {
                  Event var3 = var2.nextEvent();
                  if (var3 instanceof MethodEntryEvent) {
                     this.this$0.hotswap();
                     break;
                  }
               }
            } catch (Throwable var5) {
               this.errorMsg(var5);
            }

            try {
               if (var1 != null) {
                  var1.resume();
               }
            } catch (Throwable var4) {
               this.errorMsg(var4);
            }

         }
      }).start();
   }

   EventSet waitEvent() throws InterruptedException {
      EventQueue var1 = this.jvm.eventQueue();
      return var1.remove();
   }

   void hotswap() {
      Map var1 = this.newClassFiles;
      this.jvm.redefineClasses(var1);
      this.newClassFiles = null;
   }
}
