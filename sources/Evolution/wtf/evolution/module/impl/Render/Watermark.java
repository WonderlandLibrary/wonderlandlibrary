package wtf.evolution.module.impl.Render;

import com.mojang.realmsclient.gui.ChatFormatting;

import net.minecraft.pathfinding.PathFinder;
import wtf.evolution.Main;
import wtf.evolution.editor.Drag;
import wtf.evolution.event.EventTarget;
import wtf.evolution.event.events.impl.EventDisplay;
import wtf.evolution.helpers.font.Fonts;
import wtf.evolution.helpers.render.RenderUtil;
import wtf.evolution.helpers.render.RoundedUtil;
import wtf.evolution.module.Category;
import wtf.evolution.module.Module;
import wtf.evolution.module.ModuleInfo;

import java.awt.*;

@ModuleInfo(name = "Watermark", type = Category.Render)
public class Watermark extends Module {

    private int tick;

    public Drag d = Main.createDrag(this, "watermark", 4, 4);

    @EventTarget
    public void onRender(EventDisplay e) {
        String ping = String.valueOf(mc.getConnection().getPlayerInfo(mc.player.getName()).getResponseTime());
        String text = "Evolution" + ChatFormatting.GRAY + " | " + ChatFormatting.RESET + Main.username + ChatFormatting.GRAY + " | " + ChatFormatting.RESET + (mc.isSingleplayer() ? "localhost" : mc.getCurrentServerData().serverIP) + ChatFormatting.GRAY + " | " + ChatFormatting.RESET + ping + "ms";

        float width = Fonts.REG16.getStringWidth(text) + 6;

        int xx = (int) d.getX();
        int yy = (int) d.getY();
        d.setWidth(width);
        d.setHeight(12);
        RenderUtil.drawBlurredShadow(xx, yy, width, 12, 15, new Color(20, 20, 20));
        RenderUtil.drawRectWH(xx, yy, width, 12, new Color(20, 20, 20).getRGB());
        RenderUtil.drawBlurredShadow(xx, yy, width, 1, 15, new Color(105, 157, 253));
        RenderUtil.drawRectWH(xx, yy, width, 0.9f, new Color(105, 157, 253).getRGB());
        Fonts.REG16.drawStringWithShadow(text, xx + 3, yy + 3f, -1);
    }

}
