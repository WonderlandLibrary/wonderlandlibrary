package best.azura.client.impl.ui.gui;

import best.azura.client.api.ui.buttons.Button;
import best.azura.client.impl.Client;
import best.azura.client.impl.rpc.DiscordRPCImpl;
import best.azura.client.impl.ui.gui.impl.Window;
import best.azura.client.impl.module.impl.render.BlurModule;
import best.azura.client.impl.ui.font.Fonts;
import best.azura.client.impl.ui.gui.impl.buttons.ButtonImpl;
import best.azura.client.util.render.RenderUtil;
import best.azura.client.util.render.StencilUtil;
import net.minecraft.client.gui.*;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.world.storage.ISaveFormat;
import net.minecraft.world.storage.SaveFormatComparator;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;

public class MainSingleplayer extends GuiScreen {

    private final ArrayList<ButtonImpl> buttons = new ArrayList<>();
    private final GuiScreen parent;
    private ButtonImpl selected;
    private double animation = 0;
    private long start = 0;
    private GuiScreen toShow;
    private List<SaveFormatComparator> list;
    private Window currentWindow = null;
    private ScrollRegion region;

    public MainSingleplayer(GuiScreen parent) {
        this.parent = parent;
    }

    @Override
    public void initGui() {
        this.region = new ScrollRegion(mc.displayWidth/2-500, mc.displayHeight/2-400, 1000, 720);
        start = System.currentTimeMillis();
        animation = 0;

        try {
            list = mc.getSaveLoader().getSaveList();
        } catch (Exception ignored) {}

        buttons.clear();
        String[] strings = new String[] {"Play", "Create", "Delete", "Back"};
        int calcWidth = 0;
        for(String s : strings) {
            int width = Fonts.INSTANCE.arial20.getStringWidth(s);
            buttons.add(new ButtonImpl(s, mc.displayWidth / 2 - 470 + calcWidth, mc.displayHeight / 2 + 350, width + 40, 40, 3));
            calcWidth += width + 40 + 5;
        }

        int count = 0;
        for(SaveFormatComparator comparator : list) {
            ButtonImpl button;
            buttons.add(button = new ButtonImpl("", mc.displayWidth/2-470, mc.displayHeight/2-380+count*78, 940, 70, 5));
            button.levelData = comparator;
            count++;
        }
        DiscordRPCImpl.updateNewPresence(
                "World Selection",
                "Selecting a world :thinking:"
        );
    }

    public void refresh(SaveFormatComparator comparator) {
        buttons.clear();
        String[] strings = new String[] {"Play", "Create", "Delete", "Back"};
        int calcWidth = 0;
        for(String s : strings) {
            int width = Fonts.INSTANCE.arial20.getStringWidth(s);
            buttons.add(new ButtonImpl(s, mc.displayWidth / 2 - 470 + calcWidth, mc.displayHeight / 2 + 350, width + 40, 40, 3));
            calcWidth += width + 40 + 5;
        }

        int count = 0;
        for(SaveFormatComparator comp : list) {
            if(comparator.getFileName().equals(comp.getFileName())) continue;
            ButtonImpl button;
            buttons.add(button = new ButtonImpl("", mc.displayWidth/2-470, mc.displayHeight/2-380+count*78, 940, 70, 5));
            button.levelData = comp;
            count++;
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {

        if(toShow != null) {
            float anim = Math.min(1, (System.currentTimeMillis()-start)/500f);
            animation = -1 * Math.pow(anim-1, 6) + 1;
            animation = 1 - animation;
        } else {
            float anim = Math.min(1, (System.currentTimeMillis()-start)/500f);
            animation = -1 * Math.pow(anim-1, 6) + 1;
        }

        if(toShow != null && animation == 0) {
            mc.displayGuiScreen(toShow);
            return;
        }

        GlStateManager.pushMatrix();
        glEnable(GL_BLEND);
        drawDefaultBackground();

        RenderUtil.INSTANCE.scaleFix(1.0);
        final ScaledResolution sr = new ScaledResolution(mc);
        final boolean blur = Client.INSTANCE.getModuleManager().getModule(BlurModule.class).isEnabled() && BlurModule.blurMenu.getObject();

        if (blur) {
            GL11.glPushMatrix();
            RenderUtil.INSTANCE.invertScaleFix(1.0);
            StencilUtil.initStencilToWrite();
            GlStateManager.scale(1.0 / sr.getScaleFactor(), 1.0 / sr.getScaleFactor(), 1);
        }

        RenderUtil.INSTANCE.drawRoundedRect(mc.displayWidth/2-500, mc.displayHeight/2-400, 1000, 800, 10, new Color(0, 0, 0, 170));

        if (blur) {
            GlStateManager.scale(sr.getScaleFactor(), sr.getScaleFactor(), 1);
            StencilUtil.readStencilBuffer(1);
            BlurModule.blurShader.blur();
            StencilUtil.uninitStencilBuffer();
            RenderUtil.INSTANCE.scaleFix(1.0);
            GL11.glPopMatrix();
        }

        int count = 0;
        int calcHeight = 0;

        region.prepare();
        region.render(mouseX, mouseY);

        for(ButtonImpl button : buttons) {
            if (button.levelData == null) continue;
            button.y = mc.displayHeight/2-380+count*78+ region.mouse;
            button.width = 940;
            button.x = mc.displayWidth/2-470;
            button.height = 70;
            button.roundness = 5;
            button.animation = animation;
            button.draw(currentWindow != null ? 0 : mouseX, currentWindow != null ? 0 : mouseY);
            button.selected = selected == button;
            count++;
            calcHeight += 78;
        }

        if(calcHeight > region.height) {
            region.offset = region.height - calcHeight - 20;
        }

        region.end();

        for(ButtonImpl button : buttons) {
            if(button.levelData != null) continue;
            button.animation = animation;
            button.selected = selected == button;
            button.draw(mouseX, mouseY);
            count++;
        }

        if(currentWindow != null && currentWindow.hidden && currentWindow.animation == 0) {
            currentWindow = null;
        }

        if(currentWindow != null) {
            RenderUtil.INSTANCE.drawRect(0, 0, mc.displayWidth, mc.displayHeight, new Color(0, 0, 0, (int) (50*currentWindow.animation)));
            currentWindow.draw(mouseX, mouseY);
        }

        glDisable(GL_BLEND);
        GlStateManager.popMatrix();
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {

        if(currentWindow != null) {
            Button button = currentWindow.clickedButton(mouseX, mouseY);
            if(button == null) return;
            if (button instanceof ButtonImpl) {
                ButtonImpl button1 = (ButtonImpl) button;
                switch (button1.text) {
                    case "Delete":
                        currentWindow.hide();
                        ISaveFormat isaveformat = this.mc.getSaveLoader();
                        isaveformat.flushCache();
                        isaveformat.deleteWorldDirectory(selected.levelData.getFileName());
                        refresh(selected.levelData);
                        break;
                    case "Cancel":
                        currentWindow.hide();
                        break;
                }
                return;
            }
        }

        boolean multiplayer = false;
        String clickedButton = "";

        for(ButtonImpl button : buttons) {
            if(button.levelData != null && button.hovered && region.isHovered()) {
                if(button == selected) {
                    multiplayer = true;
                    break;
                }
                selected = button;
                return;
            } else if(button.hovered && button.levelData == null) {
                clickedButton = button.text;
                break;
            }
        }

        if(multiplayer) {
            launchWorld();
            return;
        }

        if(clickedButton.equals("")) {
            return;
        }

        switch (clickedButton) {
            case "Back":
                animation = 0;
                start = System.currentTimeMillis();
                toShow = parent;
                break;
            case "Delete":
                if(selected != null) {
                    currentWindow = new Window("Confirm deletion", 300, 200);
                    ButtonImpl button;
                    currentWindow.buttons.add(button = new ButtonImpl("Delete", mc.displayWidth/2-140, mc.displayHeight/2+50, 100, 40, 5));
                    button.normalColor = new Color(100, 50, 50, 50);
                    button.hoverColor = new Color(100, 50, 50, 100);
                    currentWindow.buttons.add(new ButtonImpl("Cancel", mc.displayWidth/2+40, mc.displayHeight/2+50, 100, 40, 5));
                }
                break;
            case "Create":
                this.mc.displayGuiScreen(new GuiCreateWorld(this));
                break;
            case "Play":
                if(selected != null) {
                    launchWorld();
                }
                break;
        }

    }

    public void launchWorld() {
        this.mc.displayGuiScreen(null);

        SaveFormatComparator data = this.selected.levelData;
        String s = data.getFileName();
        String s1 = data.getDisplayName();

        if (this.mc.getSaveLoader().canLoadWorld(s))
        {
            this.mc.launchIntegratedServer(s, s1, null);
        }
    }

    @Override
    public void confirmClicked(boolean result, int id) {
        initGui();
        mc.displayGuiScreen(this);
    }

    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        region.handleMouseInput();
    }

    @Override
    public void onTick() {
        super.onTick();
        region.onTick();
    }
}
