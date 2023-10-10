package org.alphacentauri.core;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.HashMap;
import org.alphacentauri.AC;
import org.alphacentauri.core.CoreListener;
import org.alphacentauri.launcher.api.API;
import org.alphacentauri.launcher.api.PluginMsgListener;
import org.alphacentauri.management.events.Event;
import org.alphacentauri.management.events.EventListener;
import org.alphacentauri.management.events.EventServerLeave;
import org.alphacentauri.management.events.EventSuccessfulServerJoin;
import org.alphacentauri.management.events.EventTick;
import org.alphacentauri.management.notifications.Notification;
import org.alphacentauri.management.util.CloudProtocol;
import org.alphacentauri.management.util.StringUtils;
import org.alphacentauri.modules.ModuleNoIRC;

public class CloudAPI implements PluginMsgListener, EventListener {
   private static final String PLUGIN_ID = "AC18";
   private int updateCounter = 0;
   public HashMap users = new HashMap();
   public static CloudAPI instance;

   public CloudAPI() {
      API.registerPluginMsgListener(this);
      instance = this;
      this.send(0, ByteBuffer.allocate(4 + "AC18".length()).putInt("AC18".length()).put("AC18".getBytes(Charset.forName("UTF-8"))).array());
   }

   private void send(int id, byte[] data) {
      API.sendPluginMessage(id, data);
   }

   public void onMsg(int id, byte[] data) {
      ByteBuffer wrap = ByteBuffer.wrap(data);
      if(id == 1) {
         if(AC.getMC().getWorld() != null) {
            AC.getNotificationManager().addNotification(new Notification(CloudProtocol.readString(wrap), CloudProtocol.readString(wrap), wrap.getInt()));
         }
      } else if(id == 2) {
         String s = CloudProtocol.readString(wrap);
         String[] split = s.split("\n");
         AC.addChat("AlphaServer", "Users with Alpha Centauri on your server:");

         for(String altuser : split) {
            String[] split1 = altuser.split("=");
            AC.addChat("AlphaServer", StringUtils.nonAlias(split1[0]) + " aka " + split1[1]);
         }
      } else if(id == 3) {
         String s = CloudProtocol.readString(wrap);
         String[] split = s.split("\n");
         this.users.clear();

         for(String altuser : split) {
            String[] split1 = altuser.split("=");
            this.users.put(split1[0], split1[1]);
         }
      } else if(id == 4) {
         if(AC.getModuleManager().get(ModuleNoIRC.class).isEnabled()) {
            return;
         }

         String s = CloudProtocol.readString(wrap);
         AC.addChat("IRC", s.replace("&", "§"));
      }

   }

   private void requestUserListSilent() {
      String serverIP = CoreListener.server;
      if(serverIP != null) {
         instance.send(4, ByteBuffer.allocate(4 + serverIP.length()).putInt(serverIP.length()).put(serverIP.getBytes(Charset.forName("UTF-8"))).array());
      }
   }

   public void onEvent(Event event) {
      if(event instanceof EventSuccessfulServerJoin) {
         String addr = ((EventSuccessfulServerJoin)event).getAddr();
         int port = ((EventSuccessfulServerJoin)event).getPort();
         String user = AC.getMC().session.getUsername();

         InetAddress ip;
         try {
            ip = Inet4Address.getByName(addr);
         } catch (Exception var7) {
            return;
         }

         String host = ip.getHostAddress();
         this.send(1, ByteBuffer.allocate(16 + host.length() + user.length() + addr.length()).putInt(host.length()).put(host.getBytes(Charset.forName("UTF-8"))).putInt(port).putInt(user.length()).put(user.getBytes(Charset.forName("UTF-8"))).putInt(addr.length()).put(addr.getBytes(Charset.forName("UTF-8"))).array());
         this.requestUserListSilent();
      } else if(event instanceof EventServerLeave) {
         this.send(2, new byte[0]);
      } else if(event instanceof EventTick && this.updateCounter++ > 300) {
         this.requestUserListSilent();
         this.updateCounter = 0;
      }

   }

   public static void sendIRC(String msg) {
      instance.send(5, ByteBuffer.allocate(4 + msg.length()).putInt(msg.length()).put(msg.getBytes(Charset.forName("UTF-8"))).array());
   }

   public static void requestUserList(String serverIP) {
      instance.send(3, ByteBuffer.allocate(4 + serverIP.length()).putInt(serverIP.length()).put(serverIP.getBytes(Charset.forName("UTF-8"))).array());
   }
}
