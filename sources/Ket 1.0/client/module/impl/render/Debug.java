package client.module.impl.render;

import client.Client;
import client.event.EventLink;
import client.event.Listener;
import client.event.impl.other.Render2DEvent;
import client.module.Category;
import client.module.Module;
import client.module.ModuleInfo;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.EnumChatFormatting;

import java.awt.*;

@ModuleInfo(name = "Debug", description = "Debugs some values", category = Category.RENDER)
public class Debug extends Module {
    @EventLink
    public final Listener<Render2DEvent> onRender2D = event -> {
        final String s = EnumChatFormatting.GRAY + ": " + EnumChatFormatting.RESET;
        final String[] strings = {
                "prevPosX" + s + mc.thePlayer.prevPosX,
                "prevPosY" + s + mc.thePlayer.prevPosY,
                "prevPosZ" + s + mc.thePlayer.prevPosZ,
                "posX" + s + mc.thePlayer.posX,
                "posY" + s + mc.thePlayer.posY,
                "posZ" + s + mc.thePlayer.posZ,
                "motionX" + s + mc.thePlayer.motionX,
                "motionY" + s + mc.thePlayer.motionY,
                "motionZ" + s + mc.thePlayer.motionZ,
                "rotationYaw" + s + mc.thePlayer.rotationYaw,
                "rotationPitch" + s + mc.thePlayer.rotationPitch,
                "onGround" + s + mc.thePlayer.onGround,
                "fallDistance" + s + mc.thePlayer.fallDistance,
                "lastTickPosX" + s + mc.thePlayer.lastTickPosX,
                "lastTickPosY" + s + mc.thePlayer.lastTickPosY,
                "lastTickPosZ" + s + mc.thePlayer.lastTickPosZ,
                "ticksExisted" + s + mc.thePlayer.ticksExisted,
                "hurtTime" + s + mc.thePlayer.hurtTime,
                "moveStrafing" + s + mc.thePlayer.moveStrafing,
                "moveForward" + s + mc.thePlayer.moveForward,
                "lastReportedPosX" + s + mc.thePlayer.lastReportedPosX,
                "lastReportedPosY" + s + mc.thePlayer.lastReportedPosY,
                "lastReportedPosZ" + s + mc.thePlayer.lastReportedPosZ,
                "lastReportedYaw" + s + mc.thePlayer.lastReportedYaw,
                "lastReportedPitch" + s + mc.thePlayer.lastReportedPitch,
                "serverSneakState" + s + mc.thePlayer.serverSneakState,
                "serverSprintState" + s + mc.thePlayer.serverSprintState,
                "onGroundTicks" + s + mc.thePlayer.onGroundTicks,
                "offGroundTicks" + s + mc.thePlayer.offGroundTicks
        };
        for (int i = 0; i < strings.length; i++) {
            final boolean b = Client.INSTANCE.getModuleManager().get(Statistics.class).isEnabled();
            Gui.drawRect(0, (b ? 55 : 11) + 11 * i, mc.fontRendererObj.getStringWidth(strings[i]) + 3, (b ? 55 : 11) + 11 * i + 11, Integer.MIN_VALUE);
            mc.fontRendererObj.drawString(strings[i], 2, (b ? 57 : 13) + 11 * i, getRainbow(i));
        }
    };

    private int getRainbow(int i) {
        return Color.getHSBColor((System.currentTimeMillis() - 83 * i) % 1000 / 1000f, 0.5f, 1).getRGB();
    }
}
