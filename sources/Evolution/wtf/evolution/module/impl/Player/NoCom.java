package wtf.evolution.module.impl.Player;

import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.server.SPacketBlockChange;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import wtf.evolution.Main;
import wtf.evolution.event.EventTarget;
import wtf.evolution.event.events.impl.EventMotion;
import wtf.evolution.event.events.impl.EventPacket;
import wtf.evolution.event.events.impl.EventRender;
import wtf.evolution.helpers.render.RenderUtil;
import wtf.evolution.module.Category;
import wtf.evolution.module.Module;
import wtf.evolution.module.ModuleInfo;
import wtf.evolution.notifications.NotificationType;
import wtf.evolution.settings.options.BooleanSetting;
import wtf.evolution.settings.options.SliderSetting;

import java.awt.*;
import java.util.ArrayList;

@ModuleInfo(name = "NoCom", type = Category.Player)
public class NoCom extends Module {

    public int distance = 100;
    public int xyiec = 0;

    public SliderSetting boost = new SliderSetting("Boost", 7, 1, 10, 1).call(this);
    public SliderSetting distance1 = new SliderSetting("Distance", 16, 1, 16 * 2, 1).call(this);
    public BooleanSetting render = new BooleanSetting("Render checked block", true).call(this);

    public ArrayList<BlockPos> p = new ArrayList<>();

    @EventTarget
    public void onUpdate(EventMotion e) {
        for (int i = 0; i < boost.get(); i++) {
            xyiec++;
            setSuffix(String.valueOf(xyiec));
            distance += distance1.get();
            double x = mc.player.posX + Math.cos(Math.toRadians(distance)) * distance / distance1.get();
            double z = mc.player.posZ + Math.sin(Math.toRadians(distance)) * distance / distance1.get();
            p.add(new BlockPos(x, mc.player.posY - 1, z));
            mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, new BlockPos(x, 255, z), mc.player.getHorizontalFacing()));
        }
    }

    @EventTarget
    public void onPacket(EventPacket e) {
        if (e.getPacket() instanceof SPacketBlockChange) {
            SPacketBlockChange packet = (SPacketBlockChange) e.getPacket();
            mc.ingameGUI.getChatGUI().printChatMessage(new TextComponentString("Founded: " + packet.getBlockPosition().getX() + " " + packet.getBlockPosition().getZ()));
            Main.notify.call("NoCom", "Founded: " + packet.getBlockPosition().getX() + " " + packet.getBlockPosition().getZ(), NotificationType.INFO);
        }
    }

@EventTarget
public void onRender(EventRender e) {
        if (!render.get()) return;
    for (BlockPos pos : p) {
        RenderUtil.blockEsp(pos, new Color(255, 255, 255, 100));
    }
}
    @Override
    public void onDisable() {
        super.onDisable();
        xyiec = 0;
        distance = 100;
        p.clear();
    }
}
