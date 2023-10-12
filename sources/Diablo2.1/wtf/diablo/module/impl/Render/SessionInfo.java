package wtf.diablo.module.impl.Render;

import com.google.common.eventbus.Subscribe;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import wtf.diablo.Diablo;
import wtf.diablo.events.impl.OverlayEvent;
import wtf.diablo.events.impl.Render2DEvent;
import wtf.diablo.gui.guiElement.GuiElement;
import wtf.diablo.module.Module;
import wtf.diablo.module.data.Category;
import wtf.diablo.module.data.ServerType;
import wtf.diablo.utils.font.Fonts;
import wtf.diablo.utils.render.ColorUtil;
import wtf.diablo.utils.render.RenderUtil;

public class SessionInfo extends Module {
    public SessionInfo() {
        super("SessionInfo", "View session information", Category.RENDER, ServerType.Hypixel);
    }

    public GuiElement guiElement = new GuiElement("SessionInfo", this,5,15,150,60);

    @Subscribe
    public void onOverlay(OverlayEvent e) {
        ScaledResolution sr = new ScaledResolution(mc);
        int kills = Diablo.hypixelStatus.getSessionKills();
        guiElement.renderStart();
        RenderUtil.drawRoundedRect(0,0,150,60,15, 0x9f232323);
        RenderUtil.drawOutlinedRoundedRect(0,0,150,59,15,3, Hud.hudType.getMode() == "Smooth" ? 0x9FFFFFFF : ColorUtil.getColor(0));
        Fonts.axi18.drawCenteredString("Session Information",150 / 2f,7,0xf9ffffff);
        Fonts.SFReg18.drawStringWithShadow("Time Played: " + Diablo.hypixelStatus.getSessionLengthString(), 10,28,-1);
        Fonts.SFReg18.drawStringWithShadow("Session Kills: " + kills, 10,30 + (Fonts.SFReg18.getHeight() + 1),-1);
        Fonts.SFReg18.drawStringWithShadow("Server: " + (mc.isSingleplayer() ? "Singleplayer" : Minecraft.getMinecraft().getCurrentServerData().serverIP), 10,40 + (Fonts.SFReg18.getHeight() + 1),-1);

        RenderUtil.drawRoundedRect(10,20,130,2,2, 0x9fffffff);
        guiElement.renderEnd();
    }
}
