package me.crimp.claudius.mod.modules.render;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.crimp.claudius.claudius;
import me.crimp.claudius.event.events.Render2DEvent;
import me.crimp.claudius.mod.modules.Module;
import me.crimp.claudius.mod.modules.client.ClickGuiModule;
import me.crimp.claudius.mod.setting.Setting;
import me.crimp.claudius.utils.ColorUtil;
import me.crimp.claudius.utils.RenderUtil;
import net.minecraft.client.Minecraft;

import java.awt.*;


public class BetterWatermark extends Module {

    private final Setting<Snap> SnapMode = register(new Setting<>("Snap", Snap.Off));
    public Setting<Integer> X = register(new Setting<>("X", 10, 0, 1200));
    public Setting<Integer> Y = register(new Setting<>("Y", 10, 0, 1200));
    public Setting<Boolean> Name = register(new Setting<>("PlayerName", true));
    public Setting<Boolean> Ping = register(new Setting<>("Ping", true));
    public Setting<Boolean> Config = register(new Setting<>("Config", false));
    public Setting<Boolean> Fps = register(new Setting<>("Fps", false));
    public Setting<Boolean> Ip = register(new Setting<>("Ip", false));
    public Setting<Boolean> BackGround = register(new Setting<>("BackGround", false));
    public Setting<Boolean> Colour = register(new Setting<>("GuiColour", false,v -> BackGround.getValue()));
    public Setting<Boolean> BasicBitchMode = register(new Setting<>("BasicBitchMode", false));


    public BetterWatermark() {
        super("BetterWatermark", "Watermark But Better", Category.Render, false, false);
    }

    private enum Snap {
        TopLeft, BottemLeft, Off
    }

    @Override
    public void onRender2D(Render2DEvent event) {
        drawWatermark();
    }

    int BoxColour;

    public void drawWatermark() {
        if (BasicBitchMode.getValue()) {
            int textcolor = ColorUtil.toRGBA(ClickGuiModule.INSTANCE.red.getValue(), ClickGuiModule.INSTANCE.green.getValue(), ClickGuiModule.INSTANCE.blue.getValue());
            String string = claudius.MODNAME + " v" + claudius.MODVER;
            claudius.textManager.drawString(string, 2.0F, 2, textcolor, true);
        } else {
            String message = ChatFormatting.BOLD + "" + ChatFormatting.DARK_AQUA + "Cldus v" + claudius.MODVER + ChatFormatting.RESET;

            if (Name.getValue()) message = message + " | Name:" + mc.player.getName();
            if (Ping.getValue()) message = message + " | Ping:" + claudius.serverManager.getPing() + "ms";
            if (Config.getValue()) message = message + " | Cfg:" + claudius.configManager.CurrentConfig();
            if (Fps.getValue()) message = message + " | Fps:" + Minecraft.getDebugFPS();

            if (Ip.getValue()) {
                if (!mc.isSingleplayer()) {
                    String serverIp = mc.currentServerData.serverIP;
                    if (mc.serverPort != 0) message = message + " | IP:" + serverIp + ":" + mc.serverPort;
                    else
                        message = message + " | IP:" + serverIp;
                } else message = message + " | IP:Singleplayer";
            }
            if (BackGround.getValue()) {
                if (Colour.getValue())
                    BoxColour = ColorUtil.toARGB(ClickGuiModule.INSTANCE.topRed.getValue(), ClickGuiModule.INSTANCE.topGreen.getValue(), ClickGuiModule.INSTANCE.topBlue.getValue(), ClickGuiModule.INSTANCE.topAlpha.getValue());
                else BoxColour = ColorUtil.toRGBA(0, 0, 0, 255);
            }


            int w = mc.fontRenderer.getStringWidth(message);
            int h = mc.fontRenderer.FONT_HEIGHT;
            if (SnapMode.getValue().equals(Snap.Off)) {
                if (BackGround.getValue())
                    RenderUtil.drawRectangleCorrectly(X.getValue(), Y.getValue(), w + 8, h + 4, BoxColour);
                claudius.textManager.drawString(message, X.getValue() + 3, Y.getValue() + 3, ColorUtil.toRGBA(255, 255, 255, 255), true);
            } else if (SnapMode.getValue().equals(Snap.TopLeft)) {
                if (BackGround.getValue()) RenderUtil.drawRectangleCorrectly(10, 1, w + 8, h + 4, BoxColour);
                claudius.textManager.drawString(message, 10 + 3, 1 + 3, ColorUtil.toRGBA(255, 255, 255, 255), true);
            } else if (SnapMode.getValue().equals(Snap.BottemLeft)) {
                if (BackGround.getValue()) RenderUtil.drawRectangleCorrectly(1, 530, w + 8, h + 4, BoxColour);
                claudius.textManager.drawString(message, 1 + 3, 530 + 3, ColorUtil.toRGBA(255, 255, 255, 255), true);
            }
        }
    }
}