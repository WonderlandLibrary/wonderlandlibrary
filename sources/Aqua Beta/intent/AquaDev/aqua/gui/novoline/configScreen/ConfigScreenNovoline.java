package intent.AquaDev.aqua.gui.novoline.configScreen;

import intent.AquaDev.aqua.Aqua;
import intent.AquaDev.aqua.config.Config;
import intent.AquaDev.aqua.modules.visual.Blur;
import intent.AquaDev.aqua.modules.visual.Shadow;
import intent.AquaDev.aqua.utils.FileUtil;
import intent.AquaDev.aqua.utils.RenderUtil;
import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.MathHelper;
import org.lwjgl.opengl.GL11;

public class ConfigScreenNovoline
extends GuiScreen {
    private int scrollAdd = 0;

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        int hudColor = Aqua.setmgr.getSetting("HUDColor").getColor();
        ScaledResolution sr = new ScaledResolution(this.mc);
        RenderUtil.drawRoundedRect2Alpha((double)((float)sr.getScaledWidth() / 2.0f - 100.0f), (double)130.0, (double)190.0, (double)20.0, (double)1.0, (Color)new Color(0, 0, 0, 110));
        Shadow.drawGlow(() -> RenderUtil.drawRoundedRect((double)((float)sr.getScaledWidth() / 2.0f - 100.0f), (double)130.0, (double)190.0, (double)20.0, (double)1.0, (int)new Color(0, 0, 0, 255).getRGB()), (boolean)false);
        RenderUtil.drawRoundedRect2Alpha((double)((float)sr.getScaledWidth() / 2.0f - 100.0f), (double)130.0, (double)190.0, (double)2.0, (double)1.0, (Color)new Color(hudColor));
        RenderUtil.drawRoundedRect2Alpha((double)((float)sr.getScaledWidth() / 2.0f - 100.0f), (double)150.0, (double)190.0, (double)200.0, (double)1.0, (Color)new Color(0, 0, 0, 100));
        Blur.drawBlurred(() -> RenderUtil.drawRoundedRect((double)((float)sr.getScaledWidth() / 2.0f - 100.0f), (double)150.0, (double)190.0, (double)200.0, (double)1.0, (int)new Color(0, 0, 0, 100).getRGB()), (boolean)false);
        Shadow.drawGlow(() -> RenderUtil.drawRoundedRect((double)((float)sr.getScaledWidth() / 2.0f - 100.0f), (double)150.0, (double)190.0, (double)200.0, (double)1.0, (int)Color.black.getRGB()), (boolean)false);
        Blur.drawBlurred(() -> RenderUtil.drawRoundedRect((double)((float)sr.getScaledWidth() / 2.0f - 100.0f), (double)150.0, (double)40.0, (double)200.0, (double)1.0, (int)new Color(0, 0, 0, 100).getRGB()), (boolean)false);
        Shadow.drawGlow(() -> RenderUtil.drawRoundedRect((double)((float)sr.getScaledWidth() / 2.0f - 100.0f), (double)150.0, (double)40.0, (double)200.0, (double)1.0, (int)new Color(0, 0, 0, 255).getRGB()), (boolean)false);
        Aqua.INSTANCE.comfortaa3.drawString("Local", (float)sr.getScaledWidth() / 2.0f - 70.0f, 133.0f, hudColor);
        Aqua.INSTANCE.comfortaa3.drawString("Online", (float)sr.getScaledWidth() / 2.0f + 20.0f, 133.0f, hudColor);
        int currentY = this.scrollAdd;
        int calcHeight = 0;
        ArrayList<Config> configList = this.findConfigs();
        for (Config config : configList) {
            calcHeight += 25;
        }
        GL11.glEnable((int)3089);
        RenderUtil.scissor((double)((float)sr.getScaledWidth() / 2.0f - 100.0f), (double)150.0, (double)190.0, (double)200.0);
        for (Config config : configList) {
            RenderUtil.drawRoundedRect2Alpha((double)((float)sr.getScaledWidth() / 2.0f - 97.0f), (double)((currentY += 25) + 125), (double)183.0, (double)22.0, (double)3.0, (Color)new Color(0, 0, 0, 60));
            Aqua.INSTANCE.comfortaa4.drawString("" + config.getConfigName(), (float)sr.getScaledWidth() / 2.0f - 90.0f, (float)(132 + currentY), -1);
        }
        if (this.mouseOver(mouseX, mouseY, (int)((float)sr.getScaledWidth() / 2.0f - 100.0f), 150, 190, 200)) {
            int mouseDelta = Aqua.INSTANCE.mouseWheelUtil.mouseDelta;
            this.scrollAdd += mouseDelta / 5;
            this.scrollAdd = MathHelper.clamp_int((int)this.scrollAdd, (int)(-calcHeight + 200), (int)0);
        }
        GL11.glDisable((int)3089);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    public void initGui() {
        ScaledResolution sr = new ScaledResolution(this.mc);
    }

    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        ScaledResolution sr = new ScaledResolution(this.mc);
        int currentY = this.scrollAdd;
        for (Config config : this.findConfigs()) {
            if (!this.mouseOver(mouseX, mouseY, (int)((float)sr.getScaledWidth() / 2.0f - 97.0f), (currentY += 25) + 125, 183, 22)) continue;
            config.load();
        }
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    protected void actionPerformed(GuiButton button) throws IOException {
    }

    private ArrayList<Config> findConfigs() {
        ArrayList configs = new ArrayList();
        File confDir = new File(FileUtil.DIRECTORY, "configs/");
        for (File file : (File[])Objects.requireNonNull((Object)confDir.listFiles())) {
            Config config = new Config(file.getName().replaceAll(".json", ""));
            configs.add((Object)config);
        }
        return configs;
    }

    private boolean mouseOver(int x, int y, int modX, int modY, int modWidth, int modHeight) {
        return x >= modX && x <= modX + modWidth && y >= modY && y <= modY + modHeight;
    }
}
