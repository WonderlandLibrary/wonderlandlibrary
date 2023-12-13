package org.alphacentauri.launcher.api;

import j.j.j.k;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Iterator;
import org.alphacentauri.launcher.api.PluginMsgListener;
import tk.alphacentauri.launcher.Main;
import tk.alphacentauri.launcher.j;

public class API {
   private static ArrayList ALLATORIxDEMO = new ArrayList();

   public static void onPluginMsgRecv(int a, byte[] a) {
      Iterator var2;
      for(Iterator var10000 = var2 = ALLATORIxDEMO.iterator(); var10000.hasNext(); var10000 = var2) {
         ((PluginMsgListener)var2.next()).onMsg(a, a);
      }

   }

   public static char[] getPassword() {
      return Main.d.a.getPassword();
   }

   public static String getUsername() {
      return Main.d.I.getText();
   }

   public static void saveFile(String a, byte[] a) {
      Main.b.ALLATORIxDEMO(a, a);
   }

   public static byte[] loadFile(String a) {
      return Main.b.ALLATORIxDEMO(a);
   }

   public static void registerPluginMsgListener(PluginMsgListener a) {
      ALLATORIxDEMO.add(a);
   }

   public static void sendPluginMessage(int a, byte[] a) {
      Main.b.send(-1337, ByteBuffer.allocate(4 + a.length).putInt(a).put(a).array());
   }

   public static String pleaseReadBeforeCracking() {
      return k.ALLATORIxDEMO(j.ALLATORIxDEMO("B\u0002d\taO,\u0005m\u0001\'\u0019&\u001ce\u001an\u00128\fq\u0006nQf\u0016}\u0018:[`\u0011K)CeW,Yo\u00021N\"R*R [5T0\u001e{_#S<[8DtG<8Q-WaF:D0V4E/E#\t(R3^;Au"));
   }

   public static boolean isServerBlacklisted(String a) {
      return Main.b.ALLATORIxDEMO(a);
   }
}
