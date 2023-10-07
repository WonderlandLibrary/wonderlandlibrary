// 
// Decompiled by Procyon v0.5.30
// 

package exhibition.gui.click.ui;

import java.util.HashMap;
import exhibition.util.StringConversions;
import exhibition.management.ColorObject;
import exhibition.util.MathUtils;
import exhibition.management.command.impl.Color;
import exhibition.util.misc.ChatUtil;
import exhibition.management.keybinding.Bindable;
import exhibition.management.keybinding.Keybind;
import org.lwjgl.input.Keyboard;
import exhibition.gui.click.components.DropdownButton;
import exhibition.gui.click.components.RGBSlider;
import exhibition.gui.click.components.ColorPreview;
import exhibition.management.ColorManager;
import exhibition.gui.click.components.GroupBox;
import exhibition.gui.click.components.DropdownBox;
import exhibition.module.data.Options;
import exhibition.gui.click.components.Slider;
import java.util.function.Function;
import java.util.Comparator;
import exhibition.gui.click.components.Checkbox;
import java.util.ArrayList;
import exhibition.module.data.Setting;
import java.util.List;
import exhibition.module.Module;
import exhibition.Client;
import exhibition.gui.click.components.CategoryPanel;
import exhibition.module.data.ModuleData;
import exhibition.gui.click.components.Button;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.Tessellator;
import java.util.Iterator;
import net.minecraft.client.gui.ScaledResolution;
import exhibition.gui.click.components.SLButton;
import org.lwjgl.opengl.GL11;
import exhibition.gui.click.components.CategoryButton;
import net.minecraft.client.renderer.GlStateManager;
import exhibition.util.RenderingUtil;
import exhibition.util.render.Colors;
import exhibition.gui.click.components.MainPanel;
import exhibition.gui.click.ClickGui;
import net.minecraft.util.ResourceLocation;
import net.minecraft.client.Minecraft;

public class Zeus extends UI
{
    private Minecraft mc;
    private ResourceLocation uparrow;
    private float hue;
    
    public Zeus() {
        this.mc = Minecraft.getMinecraft();
        this.uparrow = new ResourceLocation("textures/skeetchainmail.png");
    }
    
    @Override
    public void mainConstructor(final ClickGui p0) {
    }
    
    @Override
    public void mainPanelDraw(final MainPanel panel, final int p0, final int p1) {
        RenderingUtil.rectangleBordered(panel.x + panel.dragX - 0.3, panel.y + panel.dragY - 0.3, panel.x + 380.0f + panel.dragX + 0.5, panel.y + 310.0f + panel.dragY + 0.3, 0.5, Colors.getColor(60), Colors.getColor(10));
        RenderingUtil.rectangleBordered(panel.x + panel.dragX + 0.6, panel.y + panel.dragY + 0.6, panel.x + 380.0f + panel.dragX - 0.5, panel.y + 310.0f + panel.dragY - 0.6, 1.3, Colors.getColor(60), Colors.getColor(40));
        RenderingUtil.rectangleBordered(panel.x + panel.dragX + 2.5, panel.y + panel.dragY + 2.5, panel.x + 380.0f + panel.dragX - 2.5, panel.y + 310.0f + panel.dragY - 2.5, 0.5, Colors.getColor(22), Colors.getColor(12));
        RenderingUtil.drawGradientSideways(panel.x + panel.dragX + 3.0f, panel.y + panel.dragY + 3.0f, panel.x + 202.0f + panel.dragX - 3.0f, panel.dragY + panel.y + 4.0f, Colors.getColor(55, 177, 218), Colors.getColor(204, 77, 198));
        RenderingUtil.drawGradientSideways(panel.x + panel.dragX + 199.0f, panel.y + panel.dragY + 3.0f, panel.x + 380.0f + panel.dragX - 3.0f, panel.dragY + panel.y + 4.0f, Colors.getColor(204, 77, 198), Colors.getColor(204, 227, 53));
        RenderingUtil.rectangle(panel.x + panel.dragX + 3.0f, panel.y + panel.dragY + 3.3, panel.x + 380.0f + panel.dragX - 3.0f, panel.dragY + panel.y + 4.0f, Colors.getColor(0, 110));
        GlStateManager.pushMatrix();
        this.mc.getTextureManager().bindTexture(this.uparrow);
        GlStateManager.translate(panel.x + panel.dragX + 2.5, panel.dragY + panel.y + 3.0f, 0.0);
        this.drawIcon(1.0, 1.0, 0.0f, 0.0f, 373.5, 303.0, 325.0f, 275.0f);
        GlStateManager.popMatrix();
        float y = 15.0f;
        for (int i = 0; i <= panel.typeButton.size(); ++i) {
            if (i <= panel.typeButton.size() - 1 && panel.typeButton.get(i).categoryPanel.visible && i > 0) {
                y = 15 + i * 40;
            }
        }
        GlStateManager.pushMatrix();
        this.prepareScissorBox(panel.x + panel.dragX + 3.0f, panel.y + panel.dragY + 4.5f, panel.x + panel.dragX + 40.0f, panel.y + panel.dragY + y + 1.0f);
        GL11.glEnable(3089);
        RenderingUtil.rectangleBordered(panel.x + panel.dragX + 2.0f, panel.y + panel.dragY + 3.0f, panel.x + panel.dragX + 40.0f, panel.y + panel.dragY + y, 0.5, Colors.getColor(0), Colors.getColor(48));
        RenderingUtil.rectangle(panel.x + panel.dragX + 3.0f, panel.y + panel.dragY + 4.0f, panel.x + panel.dragX + 39.0f, panel.y + panel.dragY + y - 1.0f, Colors.getColor(12));
        GL11.glDisable(3089);
        GlStateManager.popMatrix();
        GlStateManager.pushMatrix();
        this.prepareScissorBox(panel.x + panel.dragX + 3.0f, panel.y + panel.dragY + y + 40.0f, panel.x + panel.dragX + 40.0f, panel.y + panel.dragY + 308.0f);
        GL11.glEnable(3089);
        RenderingUtil.rectangleBordered(panel.x + panel.dragX + 2.0f, panel.y + panel.dragY + y + 40.0f, panel.x + panel.dragX + 40.0f, panel.y + panel.dragY + 308.0f, 0.5, Colors.getColor(0), Colors.getColor(48));
        RenderingUtil.rectangle(panel.x + panel.dragX + 3.0f, panel.y + panel.dragY + y + 41.0f, panel.x + panel.dragX + 39.0f, panel.y + panel.dragY + 307.5, Colors.getColor(12));
        GL11.glDisable(3089);
        GlStateManager.popMatrix();
        for (final SLButton button : panel.slButtons) {
            button.draw(p0, p1);
        }
        for (final CategoryButton button2 : panel.typeButton) {
            button2.draw(p0, p1);
        }
        final ScaledResolution rs = new ScaledResolution(this.mc, this.mc.displayWidth, this.mc.displayHeight);
        if (panel.dragging) {
            panel.dragX = p0 - panel.lastDragX;
            panel.dragY = p1 - panel.lastDragY;
        }
        if (panel.dragX > rs.getScaledWidth() - 452) {
            panel.dragX = rs.getScaledWidth() - 452;
        }
        if (panel.dragX < -48.0f) {
            panel.dragX = -48.0f;
        }
        if (panel.dragY > rs.getScaledHeight() - 362) {
            panel.dragY = rs.getScaledHeight() - 362;
        }
        if (panel.dragY < -48.0f) {
            panel.dragY = -48.0f;
        }
    }
    
    private void drawIcon(final double x, final double y, final float u, final float v, final double width, final double height, final float textureWidth, final float textureHeight) {
        final float var8 = 1.0f / textureWidth;
        final float var9 = 1.0f / textureHeight;
        final Tessellator var10 = Tessellator.getInstance();
        final WorldRenderer var11 = var10.getWorldRenderer();
        var11.startDrawingQuads();
        var11.addVertexWithUV(x, y + height, 0.0, u * var8, (v + (float)height) * var9);
        var11.addVertexWithUV(x + width, y + height, 0.0, (u + (float)width) * var8, (v + (float)height) * var9);
        var11.addVertexWithUV(x + width, y, 0.0, (u + (float)width) * var8, v * var9);
        var11.addVertexWithUV(x, y, 0.0, u * var8, v * var9);
        var10.draw();
    }
    
    private void prepareScissorBox(final float x, final float y, final float x2, final float y2) {
        final ScaledResolution scale = new ScaledResolution(this.mc, this.mc.displayWidth, this.mc.displayHeight);
        final int factor = scale.getScaleFactor();
        GL11.glScissor((int)(x * factor), (int)((scale.getScaledHeight() - y2) * factor), (int)((x2 - x) * factor), (int)((y2 - y) * factor));
    }
    
    @Override
    public void mainPanelKeyPress(final MainPanel panel, final int key) {
        for (final CategoryButton cbutton : panel.typeButton) {
            for (final Button button : cbutton.categoryPanel.buttons) {
                button.keyPressed(key);
            }
        }
    }
    
    @Override
    public void panelConstructor(final MainPanel mainPanel, final float x, float y) {
        final int y2 = 15;
        for (final ModuleData.Type types : ModuleData.Type.values()) {
            mainPanel.typeButton.add(new CategoryButton(mainPanel, types.name(), x + 3.0f, y + y2));
            y += 40.0f;
        }
        mainPanel.typeButton.add(new CategoryButton(mainPanel, "Colors", x + 3.0f, y + y2));
        mainPanel.typeButton.get(0).enabled = true;
        mainPanel.typeButton.get(0).categoryPanel.visible = true;
    }
    
    @Override
    public void panelMouseClicked(final MainPanel mainPanel, final int x, final int y, final int z) {
        if (x >= mainPanel.x + mainPanel.dragX && y >= mainPanel.dragY + mainPanel.y && x <= mainPanel.dragX + mainPanel.x + 400.0f && y <= mainPanel.dragY + mainPanel.y + 12.0f && z == 0) {
            mainPanel.dragging = true;
            mainPanel.lastDragX = x - mainPanel.dragX;
            mainPanel.lastDragY = y - mainPanel.dragY;
        }
        for (final CategoryButton c : mainPanel.typeButton) {
            c.mouseClicked(x, y, z);
            c.categoryPanel.mouseClicked(x, y, z);
        }
        for (final SLButton button : mainPanel.slButtons) {
            button.mouseClicked(x, y, z);
        }
    }
    
    @Override
    public void panelMouseMovedOrUp(final MainPanel mainPanel, final int x, final int y, final int z) {
        if (z == 0) {
            mainPanel.dragging = false;
        }
        for (final CategoryButton button : mainPanel.typeButton) {
            button.mouseReleased(x, y, z);
        }
    }
    
    @Override
    public void categoryButtonConstructor(final CategoryButton p0, final MainPanel p1) {
        p0.categoryPanel = new CategoryPanel(p0.name, p0, 0.0f, 0.0f);
    }
    
    @Override
    public void categoryButtonMouseClicked(final CategoryButton p0, final MainPanel p1, final int p2, final int p3, final int p4) {
        if (p2 >= p0.x + p1.dragX && p3 >= p1.dragY + p0.y && p2 <= p1.dragX + p0.x + 40.0f && p3 <= p1.dragY + p0.y + 40.0f && p4 == 0) {
            for (final CategoryButton button : p1.typeButton) {
                if (button == p0) {
                    p0.enabled = true;
                    p0.categoryPanel.visible = true;
                }
                else {
                    button.enabled = false;
                    button.categoryPanel.visible = false;
                }
            }
        }
    }
    
    @Override
    public void categoryButtonDraw(final CategoryButton p0, final float p2, final float p3) {
        int color = p0.enabled ? Colors.getColor(210) : Colors.getColor(91);
        if (p2 >= p0.x + p0.panel.dragX && p3 >= p0.y + p0.panel.dragY && p2 <= p0.x + p0.panel.dragX + 40.0f && p3 <= p0.y + p0.panel.dragY + 40.0f && !p0.enabled) {
            color = Colors.getColor(165);
        }
        if (p0.name.equalsIgnoreCase("MSGO")) {
            Client.badCache.drawCenteredString("A", p0.x + 20.0f + p0.panel.dragX, p0.y + 20.0f + p0.panel.dragY, color);
        }
        else if (p0.name.equalsIgnoreCase("Combat")) {
            Client.badCache.drawCenteredString("E", p0.x + 19.0f + p0.panel.dragX, p0.y + 20.0f + p0.panel.dragY, color);
        }
        else if (p0.name.equalsIgnoreCase("Player")) {
            Client.badCache.drawCenteredString("F", p0.x + 18.0f + p0.panel.dragX, p0.y + 20.0f + p0.panel.dragY, color);
        }
        else if (p0.name.equalsIgnoreCase("Movement")) {
            Client.badCache.drawCenteredString("J", p0.x + 20.0f + p0.panel.dragX, p0.y + 20.0f + p0.panel.dragY, color);
        }
        else if (p0.name.equalsIgnoreCase("Visuals")) {
            Client.badCache.drawCenteredString("C", p0.x + 18.0f + p0.panel.dragX, p0.y + 20.0f + p0.panel.dragY, color);
        }
        else if (p0.name.equalsIgnoreCase("Colors")) {
            Client.badCache.drawCenteredString("H", p0.x + 18.5f + p0.panel.dragX, p0.y + 20.0f + p0.panel.dragY, color);
        }
        else if (p0.name.equalsIgnoreCase("Other")) {
            Client.badCache.drawCenteredString("I", p0.x + 19.0f + p0.panel.dragX, p0.y + 20.0f + p0.panel.dragY, color);
        }
        else {
            Client.f.drawStringWithShadow(Character.toString(p0.name.charAt(0)) + Character.toString(p0.name.charAt(1)), p0.x + 12.0f + p0.panel.dragX, p0.y + 13.0f + p0.panel.dragY, color);
        }
        if (p0.enabled) {
            p0.categoryPanel.draw(p2, p3);
        }
    }
    
    private List<Setting> getSettings(final Module mod) {
        final List<Setting> settings = new ArrayList<Setting>();
        for (final Setting set : ((HashMap<K, Setting>)mod.getSettings()).values()) {
            settings.add(set);
        }
        if (settings.isEmpty()) {
            return null;
        }
        return settings;
    }
    
    @Override
    public void categoryPanelConstructor(final CategoryPanel categoryPanel, final CategoryButton categoryButton, final float x, float y) {
        float xOff = 55.0f + categoryButton.panel.x;
        float yOff = 15.0f + categoryButton.panel.y;
        if (categoryButton.name.equalsIgnoreCase("Combat")) {
            float biggestY = 34.0f;
            float noSets = 0.0f;
            for (final Module module : Client.getModuleManager().getArray()) {
                if (module.getType() == ModuleData.Type.Combat) {
                    if (module.getName().equalsIgnoreCase("AutoPot")) {
                        yOff -= 52.0f;
                    }
                    y = 20.0f;
                    if (this.getSettings(module) != null) {
                        categoryPanel.buttons.add(new Button(categoryPanel, module.getName(), xOff, yOff + 10.0f, module));
                        float x2 = 0.0f;
                        for (final Setting setting : this.getSettings(module)) {
                            if (setting.getValue() instanceof Boolean) {
                                categoryPanel.checkboxes.add(new Checkbox(categoryPanel, setting.getName(), xOff + x2, yOff + y, setting));
                                x2 += 45.0f;
                                if (x2 != 90.0f) {
                                    continue;
                                }
                                x2 = 0.0f;
                                y += 10.0f;
                            }
                        }
                        if (x2 == 45.0f) {
                            y += 10.0f;
                        }
                        x2 = 0.0f;
                        int tY = 0;
                        final List<Setting> sliders = new ArrayList<Setting>();
                        for (final Setting setting2 : this.getSettings(module)) {
                            if (setting2.getValue() instanceof Number) {
                                sliders.add(setting2);
                            }
                        }
                        sliders.sort(Comparator.comparing((Function<? super Setting, ? extends Comparable>)Setting::getName));
                        for (final Setting setting2 : sliders) {
                            categoryPanel.sliders.add(new Slider(categoryPanel, xOff + x2 + 1.0f, yOff + y + 4.0f, setting2));
                            x2 += 45.0f;
                            tY = 12;
                            if (x2 == 90.0f) {
                                tY = 0;
                                x2 = 0.0f;
                                y += 12.0f;
                            }
                        }
                        for (final Setting setting2 : this.getSettings(module)) {
                            if (setting2.getValue() instanceof Options) {
                                if (x2 == 45.0f) {
                                    y += 14.0f;
                                }
                                x2 = 0.0f;
                            }
                        }
                        for (final Setting setting2 : this.getSettings(module)) {
                            if (setting2.getValue() instanceof Options) {
                                final Options option = setting2.getValue();
                                categoryPanel.dropdownBoxes.add(new DropdownBox(option, xOff + x2, yOff + y + 4.0f, categoryPanel));
                                tY = 17;
                                x2 += 45.0f;
                                if (x2 != 90.0f) {
                                    continue;
                                }
                                y += 17.0f;
                                tY = 0;
                                x2 = 0.0f;
                            }
                        }
                        y += tY;
                        categoryPanel.groupBoxes.add(new GroupBox(module, categoryPanel, xOff, yOff, (y == 34.0f) ? 40.0f : (y - 11.0f)));
                        xOff += 110.0f;
                        if (y >= biggestY) {
                            biggestY = y;
                        }
                    }
                    else {
                        categoryPanel.buttons.add(new Button(categoryPanel, module.getName(), 55.0f + categoryButton.panel.x + noSets, 330.0f, module));
                        noSets += 45.0f;
                    }
                    if (xOff > 20.0f + categoryButton.panel.y + 315.0f) {
                        xOff = 55.0f + categoryButton.panel.x;
                        yOff += ((y == 20.0f && biggestY == 20.0f) ? 26.0f : biggestY);
                    }
                }
            }
        }
        if (categoryButton.name == "Player") {
            float biggestY = 34.0f;
            float noSets = 0.0f;
            for (final Module module : Client.getModuleManager().getArray()) {
                if (module.getType() == ModuleData.Type.Player) {
                    y = 20.0f;
                    if (this.getSettings(module) != null) {
                        categoryPanel.buttons.add(new Button(categoryPanel, module.getName(), xOff, yOff + 10.0f, module));
                        float x2 = 0.0f;
                        for (final Setting setting : this.getSettings(module)) {
                            if (setting.getValue() instanceof Boolean) {
                                categoryPanel.checkboxes.add(new Checkbox(categoryPanel, setting.getName(), xOff + x2, yOff + y, setting));
                                x2 += 45.0f;
                                if (x2 != 90.0f) {
                                    continue;
                                }
                                x2 = 0.0f;
                                y += 10.0f;
                            }
                        }
                        if (x2 == 45.0f) {
                            y += 10.0f;
                        }
                        x2 = 0.0f;
                        int tY = 0;
                        final List<Setting> sliders = new ArrayList<Setting>();
                        for (final Setting setting2 : this.getSettings(module)) {
                            if (setting2.getValue() instanceof Number) {
                                sliders.add(setting2);
                            }
                        }
                        sliders.sort(Comparator.comparing((Function<? super Setting, ? extends Comparable>)Setting::getName));
                        for (final Setting setting2 : sliders) {
                            categoryPanel.sliders.add(new Slider(categoryPanel, xOff + x2 + 1.0f, yOff + y + 4.0f, setting2));
                            x2 += 45.0f;
                            tY = 12;
                            if (x2 == 90.0f) {
                                tY = 0;
                                x2 = 0.0f;
                                y += 12.0f;
                            }
                        }
                        for (final Setting setting2 : this.getSettings(module)) {
                            if (setting2.getValue() instanceof Options) {
                                if (x2 == 45.0f) {
                                    y += 14.0f;
                                }
                                x2 = 0.0f;
                            }
                        }
                        for (final Setting setting2 : this.getSettings(module)) {
                            if (setting2.getValue() instanceof Options) {
                                final Options option = setting2.getValue();
                                categoryPanel.dropdownBoxes.add(new DropdownBox(option, xOff + x2, yOff + y + 4.0f, categoryPanel));
                                tY = 17;
                                x2 += 45.0f;
                                if (x2 != 90.0f) {
                                    continue;
                                }
                                y += 17.0f;
                                tY = 0;
                                x2 = 0.0f;
                            }
                        }
                        y += tY;
                        categoryPanel.groupBoxes.add(new GroupBox(module, categoryPanel, xOff, yOff, (y == 34.0f) ? 40.0f : (y - 11.0f)));
                        xOff += 110.0f;
                        if (y >= biggestY) {
                            biggestY = y;
                        }
                    }
                    else {
                        if (noSets >= 315.0f) {
                            categoryPanel.buttons.add(new Button(categoryPanel, module.getName(), 55.0f + categoryButton.panel.x + noSets - 315.0f, 345.0f, module));
                        }
                        else {
                            categoryPanel.buttons.add(new Button(categoryPanel, module.getName(), 55.0f + categoryButton.panel.x + noSets, 330.0f, module));
                        }
                        noSets += 45.0f;
                    }
                    if (xOff > 20.0f + categoryButton.panel.y + 315.0f) {
                        xOff = 55.0f + categoryButton.panel.x;
                        yOff += ((y == 20.0f && biggestY == 20.0f) ? 26.0f : biggestY);
                    }
                }
            }
        }
        if (categoryButton.name == "Movement") {
            float biggestY = 34.0f;
            float noSets = 0.0f;
            for (final Module module : Client.getModuleManager().getArray()) {
                if (module.getType() == ModuleData.Type.Movement) {
                    y = 20.0f;
                    if (this.getSettings(module) != null) {
                        categoryPanel.buttons.add(new Button(categoryPanel, module.getName(), xOff, yOff + 10.0f, module));
                        float x2 = 0.0f;
                        for (final Setting setting : this.getSettings(module)) {
                            if (setting.getValue() instanceof Boolean) {
                                categoryPanel.checkboxes.add(new Checkbox(categoryPanel, setting.getName(), xOff + x2, yOff + y, setting));
                                x2 += 45.0f;
                                if (x2 != 90.0f) {
                                    continue;
                                }
                                x2 = 0.0f;
                                y += 10.0f;
                            }
                        }
                        if (x2 == 45.0f) {
                            y += 10.0f;
                        }
                        x2 = 0.0f;
                        int tY = 0;
                        final List<Setting> sliders = new ArrayList<Setting>();
                        for (final Setting setting2 : this.getSettings(module)) {
                            if (setting2.getValue() instanceof Number) {
                                sliders.add(setting2);
                            }
                        }
                        sliders.sort(Comparator.comparing((Function<? super Setting, ? extends Comparable>)Setting::getName));
                        for (final Setting setting2 : sliders) {
                            categoryPanel.sliders.add(new Slider(categoryPanel, xOff + x2 + 1.0f, yOff + y + 4.0f, setting2));
                            x2 += 45.0f;
                            tY = 12;
                            if (x2 == 90.0f) {
                                tY = 0;
                                x2 = 0.0f;
                                y += 12.0f;
                            }
                        }
                        for (final Setting setting2 : this.getSettings(module)) {
                            if (setting2.getValue() instanceof Options) {
                                if (x2 == 45.0f) {
                                    y += 14.0f;
                                }
                                x2 = 0.0f;
                            }
                        }
                        for (final Setting setting2 : this.getSettings(module)) {
                            if (setting2.getValue() instanceof Options) {
                                final Options option = setting2.getValue();
                                categoryPanel.dropdownBoxes.add(new DropdownBox(option, xOff + x2, yOff + y + 4.0f, categoryPanel));
                                tY = 17;
                                x2 += 45.0f;
                                if (x2 != 90.0f) {
                                    continue;
                                }
                                y += 17.0f;
                                tY = 0;
                                x2 = 0.0f;
                            }
                        }
                        y += tY;
                        categoryPanel.groupBoxes.add(new GroupBox(module, categoryPanel, xOff, yOff, (y == 34.0f) ? 40.0f : (y - 11.0f)));
                        xOff += 110.0f;
                        if (y >= biggestY) {
                            biggestY = y;
                        }
                    }
                    else {
                        categoryPanel.buttons.add(new Button(categoryPanel, module.getName(), 55.0f + categoryButton.panel.x + noSets, 330.0f, module));
                        noSets += 45.0f;
                    }
                    if (xOff > 20.0f + categoryButton.panel.y + 315.0f) {
                        xOff = 55.0f + categoryButton.panel.x;
                        yOff += ((y == 20.0f && biggestY == 20.0f) ? 26.0f : biggestY);
                    }
                }
            }
        }
        if (categoryButton.name == "Visuals") {
            float biggestY = 34.0f;
            float noSets = 0.0f;
            for (final Module module : Client.getModuleManager().getArray()) {
                if (module.getType() == ModuleData.Type.Visuals) {
                    y = 20.0f;
                    if (this.getSettings(module) != null) {
                        categoryPanel.buttons.add(new Button(categoryPanel, module.getName(), xOff, yOff + 10.0f, module));
                        float x2 = 0.0f;
                        for (final Setting setting : this.getSettings(module)) {
                            if (setting.getValue() instanceof Boolean) {
                                categoryPanel.checkboxes.add(new Checkbox(categoryPanel, setting.getName(), xOff + x2, yOff + y, setting));
                                x2 += 45.0f;
                                if (x2 != 90.0f) {
                                    continue;
                                }
                                x2 = 0.0f;
                                y += 10.0f;
                            }
                        }
                        if (x2 == 45.0f) {
                            y += 10.0f;
                        }
                        x2 = 0.0f;
                        int tY = 0;
                        final List<Setting> sliders = new ArrayList<Setting>();
                        for (final Setting setting2 : this.getSettings(module)) {
                            if (setting2.getValue() instanceof Number) {
                                sliders.add(setting2);
                            }
                        }
                        sliders.sort(Comparator.comparing((Function<? super Setting, ? extends Comparable>)Setting::getName));
                        for (final Setting setting2 : sliders) {
                            categoryPanel.sliders.add(new Slider(categoryPanel, xOff + x2 + 1.0f, yOff + y + 4.0f, setting2));
                            x2 += 45.0f;
                            tY = 12;
                            if (x2 == 90.0f) {
                                tY = 0;
                                x2 = 0.0f;
                                y += 12.0f;
                            }
                        }
                        for (final Setting setting2 : this.getSettings(module)) {
                            if (setting2.getValue() instanceof Options) {
                                if (x2 == 45.0f) {
                                    y += 14.0f;
                                }
                                x2 = 0.0f;
                            }
                        }
                        for (final Setting setting2 : this.getSettings(module)) {
                            if (setting2.getValue() instanceof Options) {
                                final Options option = setting2.getValue();
                                categoryPanel.dropdownBoxes.add(new DropdownBox(option, xOff + x2, yOff + y + 4.0f, categoryPanel));
                                tY = 17;
                                x2 += 45.0f;
                                if (x2 != 90.0f) {
                                    continue;
                                }
                                y += 17.0f;
                                tY = 0;
                                x2 = 0.0f;
                            }
                        }
                        y += tY;
                        categoryPanel.groupBoxes.add(new GroupBox(module, categoryPanel, xOff, yOff, (y == 34.0f) ? 40.0f : (y - 11.0f)));
                        xOff += 110.0f;
                        if (y >= biggestY) {
                            biggestY = y;
                        }
                    }
                    else {
                        categoryPanel.buttons.add(new Button(categoryPanel, module.getName(), 55.0f + categoryButton.panel.x + noSets, 330.0f, module));
                        noSets += 45.0f;
                    }
                    if (xOff > 20.0f + categoryButton.panel.y + 315.0f) {
                        xOff = 55.0f + categoryButton.panel.x;
                        yOff += ((y == 20.0f && biggestY == 20.0f) ? 26.0f : biggestY);
                    }
                }
            }
        }
        if (categoryButton.name == "Other") {
            float biggestY = 34.0f;
            float noSets = 0.0f;
            for (final Module module : Client.getModuleManager().getArray()) {
                if (module.getType() == ModuleData.Type.Other) {
                    y = 20.0f;
                    if (this.getSettings(module) != null) {
                        categoryPanel.buttons.add(new Button(categoryPanel, module.getName(), xOff, yOff + 10.0f, module));
                        float x2 = 0.0f;
                        for (final Setting setting : this.getSettings(module)) {
                            if (setting.getValue() instanceof Boolean) {
                                categoryPanel.checkboxes.add(new Checkbox(categoryPanel, setting.getName(), xOff + x2, yOff + y, setting));
                                x2 += 45.0f;
                                if (x2 != 90.0f) {
                                    continue;
                                }
                                x2 = 0.0f;
                                y += 10.0f;
                            }
                        }
                        if (x2 == 45.0f) {
                            y += 10.0f;
                        }
                        x2 = 0.0f;
                        int tY = 0;
                        final List<Setting> sliders = new ArrayList<Setting>();
                        for (final Setting setting2 : this.getSettings(module)) {
                            if (setting2.getValue() instanceof Number) {
                                sliders.add(setting2);
                            }
                        }
                        sliders.sort(Comparator.comparing((Function<? super Setting, ? extends Comparable>)Setting::getName));
                        for (final Setting setting2 : sliders) {
                            categoryPanel.sliders.add(new Slider(categoryPanel, xOff + x2 + 1.0f, yOff + y + 4.0f, setting2));
                            x2 += 45.0f;
                            tY = 12;
                            if (x2 == 90.0f) {
                                tY = 0;
                                x2 = 0.0f;
                                y += 12.0f;
                            }
                        }
                        for (final Setting setting2 : this.getSettings(module)) {
                            if (setting2.getValue() instanceof Options) {
                                if (x2 == 45.0f) {
                                    y += 14.0f;
                                }
                                x2 = 0.0f;
                            }
                        }
                        for (final Setting setting2 : this.getSettings(module)) {
                            if (setting2.getValue() instanceof Options) {
                                final Options option = setting2.getValue();
                                categoryPanel.dropdownBoxes.add(new DropdownBox(option, xOff + x2, yOff + y + 4.0f, categoryPanel));
                                tY = 17;
                                x2 += 45.0f;
                                if (x2 != 90.0f) {
                                    continue;
                                }
                                y += 17.0f;
                                tY = 0;
                                x2 = 0.0f;
                            }
                        }
                        y += tY;
                        categoryPanel.groupBoxes.add(new GroupBox(module, categoryPanel, xOff, yOff, (y == 34.0f) ? 40.0f : (y - 11.0f)));
                        xOff += 110.0f;
                        if (y >= biggestY) {
                            biggestY = y;
                        }
                    }
                    else {
                        categoryPanel.buttons.add(new Button(categoryPanel, module.getName(), 55.0f + categoryButton.panel.x + noSets, 330.0f, module));
                        noSets += 45.0f;
                    }
                    if (xOff > 20.0f + categoryButton.panel.y + 315.0f) {
                        xOff = 55.0f + categoryButton.panel.x;
                        yOff += ((y == 20.0f && biggestY == 20.0f) ? 26.0f : biggestY);
                    }
                }
            }
        }
        if (categoryButton.name == "MSGO") {
            float biggestY = 34.0f;
            for (final Module module2 : Client.getModuleManager().getArray()) {
                if (module2.getType() == ModuleData.Type.MSGO) {
                    categoryPanel.buttons.add(new Button(categoryPanel, module2.getName(), xOff, yOff + 10.0f, module2));
                    y = 34.0f;
                    if (this.getSettings(module2) != null) {
                        y = 20.0f;
                        float x3 = 0.0f;
                        for (final Setting setting3 : this.getSettings(module2)) {
                            if (setting3.getValue() instanceof Boolean) {
                                categoryPanel.checkboxes.add(new Checkbox(categoryPanel, setting3.getName(), xOff + x3, yOff + y, setting3));
                                x3 += 45.0f;
                                if (x3 != 90.0f) {
                                    continue;
                                }
                                x3 = 0.0f;
                                y += 10.0f;
                            }
                        }
                        if (x3 == 45.0f) {
                            y += 10.0f;
                        }
                        x3 = 0.0f;
                        final List<Setting> sliders2 = new ArrayList<Setting>();
                        for (final Setting setting : this.getSettings(module2)) {
                            if (setting.getValue() instanceof Number) {
                                sliders2.add(setting);
                            }
                        }
                        sliders2.sort(Comparator.comparing((Function<? super Setting, ? extends Comparable>)Setting::getName));
                        for (final Setting setting : sliders2) {
                            categoryPanel.sliders.add(new Slider(categoryPanel, xOff + x3 + 1.0f, yOff + y + 4.0f, setting));
                            x3 += 45.0f;
                            if (x3 == 90.0f) {
                                x3 = 0.0f;
                                y += 12.0f;
                            }
                        }
                        for (final Setting setting : this.getSettings(module2)) {
                            if (setting.getValue() instanceof Options) {
                                if (x3 == 45.0f) {
                                    y += 14.0f;
                                }
                                x3 = 0.0f;
                            }
                        }
                        for (final Setting setting : this.getSettings(module2)) {
                            if (setting.getValue() instanceof Options) {
                                final Options option2 = setting.getValue();
                                categoryPanel.dropdownBoxes.add(new DropdownBox(option2, xOff + x3, yOff + y + 4.0f, categoryPanel));
                                x3 += 45.0f;
                                if (x3 != 90.0f) {
                                    continue;
                                }
                                y += 19.0f;
                                x3 = 0.0f;
                            }
                        }
                    }
                    categoryPanel.groupBoxes.add(new GroupBox(module2, categoryPanel, xOff, yOff, (y == 34.0f) ? 40.0f : (y - 11.0f)));
                    xOff += 110.0f;
                    if (y >= biggestY) {
                        biggestY = y;
                    }
                    if (xOff > 20.0f + categoryButton.panel.y + 315.0f) {
                        xOff = 55.0f + categoryButton.panel.x;
                        yOff += biggestY;
                    }
                }
            }
        }
        if (categoryButton.name == "Colors") {
            categoryPanel.colorPreviews.add(new ColorPreview(ColorManager.fVis, "Friendly Visible", xOff + 80.0f, y + 5.0f, categoryButton));
            categoryPanel.colorPreviews.add(new ColorPreview(ColorManager.fInvis, "Friendly Invisible", xOff + 80.0f, y + 65.0f, categoryButton));
            categoryPanel.colorPreviews.add(new ColorPreview(ColorManager.eVis, "Enemy Visible", xOff + 190.0f, y + 5.0f, categoryButton));
            categoryPanel.colorPreviews.add(new ColorPreview(ColorManager.eInvis, "Enemy Invisible", xOff + 190.0f, y + 65.0f, categoryButton));
            categoryPanel.colorPreviews.add(new ColorPreview(ColorManager.fTeam, "Friendly Team", xOff + 80.0f, y + 125.0f, categoryButton));
            categoryPanel.colorPreviews.add(new ColorPreview(ColorManager.eTeam, "Enemy Team", xOff + 190.0f, y + 125.0f, categoryButton));
            categoryPanel.colorPreviews.add(new ColorPreview(Client.cm.getHudColor(), "Hud Color", xOff + 300.0f, y + 5.0f, categoryButton));
            categoryPanel.colorPreviews.add(new ColorPreview(ColorManager.xhair, "Crosshair Color", xOff + 300.0f, y + 65.0f, categoryButton));
        }
    }
    
    @Override
    public void categoryPanelMouseClicked(final CategoryPanel categoryPanel, final int p1, final int p2, final int p3) {
        boolean active = false;
        for (final DropdownBox db : categoryPanel.dropdownBoxes) {
            if (db.active) {
                db.mouseClicked(p1, p2, p3);
                active = true;
                break;
            }
        }
        if (!active) {
            for (final DropdownBox db : categoryPanel.dropdownBoxes) {
                db.mouseClicked(p1, p2, p3);
            }
            for (final Button button : categoryPanel.buttons) {
                button.mouseClicked(p1, p2, p3);
            }
            for (final Checkbox checkbox : categoryPanel.checkboxes) {
                checkbox.mouseClicked(p1, p2, p3);
            }
            for (final Slider slider : categoryPanel.sliders) {
                slider.mouseClicked(p1, p2, p3);
            }
            for (final ColorPreview cp : categoryPanel.colorPreviews) {
                for (final RGBSlider slider2 : cp.sliders) {
                    slider2.mouseClicked(p1, p2, p3);
                }
            }
        }
    }
    
    @Override
    public void categoryPanelDraw(final CategoryPanel categoryPanel, final float x, final float y) {
        for (final ColorPreview cp : categoryPanel.colorPreviews) {
            cp.draw(x, y);
        }
        for (final GroupBox groupBox : categoryPanel.groupBoxes) {
            groupBox.draw(x, y);
        }
        for (final Button button : categoryPanel.buttons) {
            button.draw(x, y);
        }
        for (final Checkbox checkbox : categoryPanel.checkboxes) {
            checkbox.draw(x, y);
        }
        for (final Slider slider : categoryPanel.sliders) {
            slider.draw(x, y);
        }
        for (final DropdownBox db : categoryPanel.dropdownBoxes) {
            db.draw(x, y);
        }
        for (final DropdownBox db : categoryPanel.dropdownBoxes) {
            if (db.active) {
                for (final DropdownButton b : db.buttons) {
                    b.draw(x, y);
                }
            }
        }
    }
    
    @Override
    public void categoryPanelMouseMovedOrUp(final CategoryPanel categoryPanel, final int x, final int y, final int button) {
        for (final Slider slider : categoryPanel.sliders) {
            slider.mouseReleased(x, y, button);
        }
        for (final ColorPreview cp : categoryPanel.colorPreviews) {
            for (final RGBSlider slider2 : cp.sliders) {
                slider2.mouseReleased(x, y, button);
            }
        }
    }
    
    @Override
    public void groupBoxConstructor(final GroupBox groupBox, final float x, final float y) {
    }
    
    @Override
    public void groupBoxMouseClicked(final GroupBox groupBox, final int p1, final int p2, final int p3) {
    }
    
    @Override
    public void groupBoxDraw(final GroupBox groupBox, final float x, final float y) {
        final float xOff = groupBox.x + groupBox.categoryPanel.categoryButton.panel.dragX - 2.5f;
        final float yOff = groupBox.y + groupBox.categoryPanel.categoryButton.panel.dragY + 10.0f;
        RenderingUtil.rectangleBordered(xOff, yOff - 6.0f, xOff + 90.0f, yOff + groupBox.ySize, 0.3, Colors.getColor(48), Colors.getColor(10));
        RenderingUtil.rectangle(xOff + 1.0f, yOff - 5.0f, xOff + 89.0f, yOff + groupBox.ySize - 1.0f, Colors.getColor(17));
        RenderingUtil.rectangle(xOff + 5.0f, yOff - 6.0f, xOff + Client.fs.getWidth(groupBox.module.getName()) + 5.0f, yOff - 4.0f, Colors.getColor(17));
    }
    
    @Override
    public void groupBoxMouseMovedOrUp(final GroupBox groupBox, final int x, final int y, final int button) {
    }
    
    @Override
    public void buttonContructor(final Button p0, final CategoryPanel panel) {
    }
    
    @Override
    public void buttonMouseClicked(final Button p0, final int p2, final int p3, final int p4, final CategoryPanel panel) {
        if (panel.categoryButton.enabled) {
            final float xOff = panel.categoryButton.panel.dragX;
            final float yOff = panel.categoryButton.panel.dragY;
            final boolean hovering = p2 >= p0.x + xOff && p3 >= p0.y + yOff && p2 <= p0.x + 35.0f + xOff && p3 <= p0.y + 6.0f + yOff;
            if (hovering) {
                if (p4 == 0) {
                    if (!p0.isBinding) {
                        p0.module.toggle();
                        p0.enabled = p0.module.isEnabled();
                    }
                    else {
                        p0.isBinding = false;
                    }
                }
                else if (p4 == 1) {
                    if (p0.isBinding) {
                        p0.module.setKeybind(new Keybind(p0.module, Keyboard.getKeyIndex("NONE")));
                        p0.isBinding = false;
                    }
                    else {
                        p0.isBinding = true;
                    }
                }
            }
            else if (p0.isBinding) {
                p0.isBinding = false;
            }
        }
    }
    
    @Override
    public void buttonDraw(final Button p0, final float p2, final float p3, final CategoryPanel panel) {
        if (panel.categoryButton.enabled) {
            GlStateManager.pushMatrix();
            final float xOff = panel.categoryButton.panel.dragX;
            final float yOff = panel.categoryButton.panel.dragY;
            RenderingUtil.rectangle(p0.x + xOff + 0.6, p0.y + yOff + 0.6, p0.x + 6.0f + xOff - 0.6, p0.y + 6.0f + yOff - 0.6, Colors.getColor(10));
            RenderingUtil.drawGradient(p0.x + xOff + 1.0f, p0.y + yOff + 1.0f, p0.x + 6.0f + xOff - 1.0f, p0.y + 6.0f + yOff - 1.0f, Colors.getColor(76), Colors.getColor(51));
            p0.enabled = p0.module.isEnabled();
            final boolean hovering = p2 >= p0.x + xOff && p3 >= p0.y + yOff && p2 <= p0.x + 35.0f + xOff && p3 <= p0.y + 6.0f + yOff;
            GlStateManager.pushMatrix();
            Client.fs.drawStringWithShadow(p0.module.getName(), p0.x + xOff + 3.0f, p0.y + 0.5f + yOff - 7.0f, Colors.getColor(220));
            Client.fss.drawStringWithShadow("Enable", p0.x + 7.6f + xOff, p0.y + 1.0f + yOff, Colors.getColor(220));
            final String meme = p0.module.getKeybind().getKeyStr().equalsIgnoreCase("None") ? "[-]" : ("[" + p0.module.getKeybind().getKeyStr() + "]");
            GlStateManager.pushMatrix();
            GlStateManager.translate(p0.x + xOff + 29.0f, p0.y + 1.0f + yOff, 0.0f);
            GlStateManager.scale(0.5, 0.5, 0.5);
            this.mc.fontRendererObj.drawStringWithShadow(meme, 0.0f, 0.0f, p0.isBinding ? Colors.getColor(216, 56, 56) : Colors.getColor(75));
            GlStateManager.popMatrix();
            GlStateManager.popMatrix();
            if (p0.enabled) {
                RenderingUtil.drawGradient(p0.x + xOff + 1.0f, p0.y + yOff + 1.0f, p0.x + xOff + 5.0f, p0.y + yOff + 5.0f, Colors.getColor(ColorManager.hudColor.red, ColorManager.hudColor.green, ColorManager.hudColor.blue, 255), Colors.getColor(ColorManager.hudColor.red, ColorManager.hudColor.green, ColorManager.hudColor.blue, 120));
            }
            if (hovering && !p0.enabled) {
                RenderingUtil.rectangle(p0.x + xOff + 1.0f, p0.y + yOff + 1.0f, p0.x + xOff + 5.0f, p0.y + yOff + 5.0f, Colors.getColor(255, 40));
            }
            if (hovering) {
                Client.fss.drawStringWithShadow((p0.module.getDescription() != null && !p0.module.getDescription().equalsIgnoreCase("")) ? p0.module.getDescription() : "ERROR: No Description Found.", panel.categoryButton.panel.x + 2.0f + panel.categoryButton.panel.dragX + 55.0f, panel.categoryButton.panel.y + 9.0f + panel.categoryButton.panel.dragY, Colors.getColor(220));
            }
            GlStateManager.popMatrix();
        }
    }
    
    @Override
    public void buttonKeyPressed(final Button button, final int key) {
        if (button.isBinding && key != 0) {
            int keyToBind = key;
            if (key == 1 || key == 14) {
                keyToBind = Keyboard.getKeyIndex("NONE");
            }
            final Keybind keybind = new Keybind(button.module, keyToBind);
            button.module.setKeybind(keybind);
            Module.saveStatus();
            button.isBinding = false;
        }
    }
    
    @Override
    public void checkBoxMouseClicked(final Checkbox p0, final int p2, final int p3, final int p4, final CategoryPanel panel) {
        if (panel.categoryButton.enabled) {
            final float xOff = panel.categoryButton.panel.dragX;
            final float yOff = panel.categoryButton.panel.dragY;
            final boolean hovering = p2 >= p0.x + xOff && p3 >= p0.y + yOff && p2 <= p0.x + 35.0f + xOff && p3 <= p0.y + 6.0f + yOff;
            if (hovering && p4 == 0) {
                final boolean xd = p0.setting.getValue();
                p0.setting.setValue(!xd);
                Module.saveSettings();
            }
        }
    }
    
    @Override
    public void checkBoxDraw(final Checkbox p0, final float p2, final float p3, final CategoryPanel panel) {
        if (panel.categoryButton.enabled) {
            GlStateManager.pushMatrix();
            final float xOff = panel.categoryButton.panel.dragX;
            final float yOff = panel.categoryButton.panel.dragY;
            RenderingUtil.rectangle(p0.x + xOff + 0.6, p0.y + yOff + 0.6, p0.x + 6.0f + xOff - 0.6, p0.y + 6.0f + yOff - 0.6, Colors.getColor(10));
            RenderingUtil.drawGradient(p0.x + xOff + 1.0f, p0.y + yOff + 1.0f, p0.x + 6.0f + xOff - 1.0f, p0.y + 6.0f + yOff - 1.0f, Colors.getColor(76), Colors.getColor(51));
            p0.enabled = p0.setting.getValue();
            final boolean hovering = p2 >= p0.x + xOff && p3 >= p0.y + yOff && p2 <= p0.x + 35.0f + xOff && p3 <= p0.y + 6.0f + yOff;
            GlStateManager.pushMatrix();
            final String xd = p0.setting.getName().charAt(0) + p0.setting.getName().toLowerCase().substring(1);
            Client.fss.drawStringWithShadow(xd, p0.x + 7.5f + xOff, p0.y + 1.0f + yOff, Colors.getColor(220));
            GlStateManager.popMatrix();
            if (p0.enabled) {
                RenderingUtil.drawGradient(p0.x + xOff + 1.0f, p0.y + yOff + 1.0f, p0.x + xOff + 5.0f, p0.y + yOff + 5.0f, Colors.getColor(ColorManager.hudColor.red, ColorManager.hudColor.green, ColorManager.hudColor.blue, 255), Colors.getColor(ColorManager.hudColor.red, ColorManager.hudColor.green, ColorManager.hudColor.blue, 120));
            }
            if (hovering && !p0.enabled) {
                RenderingUtil.rectangle(p0.x + xOff + 1.0f, p0.y + yOff + 1.0f, p0.x + xOff + 5.0f, p0.y + yOff + 5.0f, Colors.getColor(255, 40));
            }
            if (hovering) {
                Client.fss.drawStringWithShadow(this.getDescription(p0.setting), panel.categoryButton.panel.x + 2.0f + panel.categoryButton.panel.dragX + 55.0f, panel.categoryButton.panel.y + 9.0f + panel.categoryButton.panel.dragY, -1);
            }
            GlStateManager.popMatrix();
        }
    }
    
    @Override
    public void dropDownContructor(final DropdownBox p0, final float p2, final float p3, final CategoryPanel panel) {
        int y = 10;
        for (final String value : p0.option.getOptions()) {
            p0.buttons.add(new DropdownButton(value, p2, p3 + y, p0));
            y += 9;
        }
    }
    
    @Override
    public void dropDownMouseClicked(final DropdownBox dropDown, final int mouseX, final int mouseY, final int mouse, final CategoryPanel panel) {
        for (final DropdownButton db : dropDown.buttons) {
            if (dropDown.active && dropDown.panel.visible) {
                db.mouseClicked(mouseX, mouseY, mouse);
            }
        }
        if (mouseX >= panel.categoryButton.panel.dragX + dropDown.x && mouseY >= panel.categoryButton.panel.dragY + dropDown.y && mouseX <= panel.categoryButton.panel.dragX + dropDown.x + 40.0f && mouseY <= panel.categoryButton.panel.dragY + dropDown.y + 8.0f && mouse == 0 && dropDown.panel.visible) {
            dropDown.active = !dropDown.active;
        }
        else {
            dropDown.active = false;
        }
    }
    
    @Override
    public void dropDownDraw(final DropdownBox p0, final float p2, final float p3, final CategoryPanel panel) {
        final float xOff = panel.categoryButton.panel.dragX;
        final float yOff = panel.categoryButton.panel.dragY;
        final boolean hovering = p2 >= panel.categoryButton.panel.dragX + p0.x && p3 >= panel.categoryButton.panel.dragY + p0.y && p2 <= panel.categoryButton.panel.dragX + p0.x + 40.0f && p3 <= panel.categoryButton.panel.dragY + p0.y + 9.0f;
        RenderingUtil.rectangle(p0.x + xOff - 0.3, p0.y + yOff - 0.3, p0.x + xOff + 40.0f + 0.3, p0.y + yOff + 9.0f + 0.3, Colors.getColor(10));
        RenderingUtil.drawGradient(p0.x + xOff, p0.y + yOff, p0.x + xOff + 40.0f, p0.y + yOff + 9.0f, Colors.getColor(31), Colors.getColor(36));
        if (hovering) {
            RenderingUtil.rectangleBordered(p0.x + xOff, p0.y + yOff, p0.x + xOff + 40.0f, p0.y + yOff + 9.0f, 0.3, Colors.getColor(0, 0), Colors.getColor(90));
        }
        Client.fss.drawStringWithShadow(p0.option.getName(), p0.x + xOff + 1.0f, p0.y - 6.0f + yOff, Colors.getColor(220));
        GlStateManager.pushMatrix();
        GlStateManager.translate(p0.x + xOff + 38.0f - (p0.active ? 2.5 : 0.0), p0.y + 4.0f + yOff, 0.0);
        GlStateManager.rotate(p0.active ? 270.0f : 90.0f, 0.0f, 0.0f, 90.0f);
        RenderingUtil.rectangle(-1.0, 0.0, -0.5, 2.5, Colors.getColor(0));
        RenderingUtil.rectangle(-0.5, 0.0, 0.0, 2.5, Colors.getColor(151));
        RenderingUtil.rectangle(0.0, 0.5, 0.5, 2.0, Colors.getColor(151));
        RenderingUtil.rectangle(0.5, 1.0, 1.0, 1.5, Colors.getColor(151));
        GlStateManager.popMatrix();
        Client.fss.drawString(p0.option.getSelected(), p0.x + 4.0f + xOff - 1.0f, p0.y + 3.0f + yOff, Colors.getColor(151));
        if (p0.active) {
            final int i = p0.buttons.size();
            RenderingUtil.rectangle(p0.x + xOff - 0.3, p0.y + 10.0f + yOff - 0.3, p0.x + xOff + 40.0f + 0.3, p0.y + yOff + 9.0f + 9 * i + 0.3, Colors.getColor(10));
            RenderingUtil.drawGradient(p0.x + xOff, p0.y + yOff + 10.0f, p0.x + xOff + 40.0f, p0.y + yOff + 9.0f + 9 * i, Colors.getColor(31), Colors.getColor(36));
        }
        if (hovering) {
            Client.fss.drawStringWithShadow("ERROR: No Description Found.", panel.categoryButton.panel.x + 2.0f + panel.categoryButton.panel.dragX + 55.0f, panel.categoryButton.panel.y + 9.0f + panel.categoryButton.panel.dragY, -1);
        }
    }
    
    @Override
    public void dropDownButtonMouseClicked(final DropdownButton p0, final DropdownBox p1, final int x, final int y, final int mouse) {
        if (x >= p1.panel.categoryButton.panel.dragX + p0.x && y >= p1.panel.categoryButton.panel.dragY + p0.y && x <= p1.panel.categoryButton.panel.dragX + p0.x + 40.0f && y <= p1.panel.categoryButton.panel.dragY + p0.y + 8.5 && mouse == 0) {
            p1.option.setSelected(p0.name);
            p1.active = false;
        }
    }
    
    @Override
    public void dropDownButtonDraw(final DropdownButton p0, final DropdownBox p1, final float x, final float y) {
        final float xOff = p1.panel.categoryButton.panel.dragX;
        final float yOff = p1.panel.categoryButton.panel.dragY;
        final boolean hovering = x >= xOff + p0.x && y >= yOff + p0.y && x <= xOff + p0.x + 40.0f && y <= yOff + p0.y + 8.5;
        GlStateManager.pushMatrix();
        Client.fss.drawStringWithShadow(p0.name, p0.x + 3.0f + xOff, p0.y + 2.0f + yOff, hovering ? Colors.getColor(ColorManager.hudColor.red, ColorManager.hudColor.green, ColorManager.hudColor.blue, 255) : -1);
        GlStateManager.scale(1.0f, 1.0f, 1.0f);
        GlStateManager.popMatrix();
    }
    
    @Override
    public void SliderContructor(final Slider p0, final CategoryPanel panel) {
        p0.dragX = p0.setting.getValue().doubleValue() * 40.0 / p0.setting.getMax();
    }
    
    @Override
    public void categoryButtonMouseReleased(final CategoryButton categoryButton, final int x, final int y, final int button) {
        categoryButton.categoryPanel.mouseReleased(x, y, button);
    }
    
    @Override
    public void slButtonDraw(final SLButton slButton, final float x, final float y, final MainPanel panel) {
        final float xOff = panel.dragX;
        final float yOff = panel.dragY + 75.0f;
        final boolean hovering = x >= 55.0f + slButton.x + xOff && y >= slButton.y + yOff - 2.0f && x <= 55.0f + slButton.x + xOff + 40.0f && y <= slButton.y + 8.0f + yOff + 2.0f;
        RenderingUtil.rectangleBordered(slButton.x + xOff + 55.0f - 0.3, slButton.y + yOff - 0.3 - 2.0, slButton.x + xOff + 40.0f + 55.0f + 0.3, slButton.y + 8.0f + yOff + 0.3 + 2.0, 0.3, Colors.getColor(10), Colors.getColor(10));
        RenderingUtil.drawGradient(slButton.x + xOff + 55.0f, slButton.y + yOff - 2.0f, slButton.x + xOff + 40.0f + 55.0f, slButton.y + 8.0f + yOff + 2.0f, Colors.getColor(46), Colors.getColor(27));
        if (hovering) {
            RenderingUtil.rectangleBordered(slButton.x + xOff + 55.0f, slButton.y + yOff - 2.0f, slButton.x + xOff + 40.0f + 55.0f, slButton.y + 8.0f + yOff + 2.0f, 0.6, Colors.getColor(0, 0), Colors.getColor(90));
        }
        final float xOffset = Client.fs.getWidth(slButton.name) / 2.0f;
        Client.fs.drawStringWithShadow(slButton.name, xOff + 25.0f + 55.0f - xOffset, slButton.y + yOff + 1.5f, -1);
    }
    
    @Override
    public void slButtonMouseClicked(final SLButton slButton, final float x, final float y, final int button, final MainPanel panel) {
        final float xOff = panel.dragX;
        final float yOff = panel.dragY + 75.0f;
        if (button == 0 && x >= 55.0f + slButton.x + xOff && y >= slButton.y + yOff - 2.0f && x <= 55.0f + slButton.x + xOff + 40.0f && y <= slButton.y + 8.0f + yOff + 2.0f) {
            if (slButton.load) {
                ChatUtil.printChat("Settings have been loaded.");
                Module.loadSettings();
                Color.loadStatus();
                for (final CategoryPanel xd : panel.typePanel) {
                    for (final Slider slider3 : xd.sliders) {
                        final Slider slider2;
                        final Slider slider = slider2 = slider3;
                        final double n = slider.setting.getValue().doubleValue() * 40.0 / slider.setting.getMax();
                        slider2.lastDragX = n;
                        slider3.dragX = n;
                    }
                }
            }
            else {
                ChatUtil.printChat("Settings have been saved.");
                Color.saveStatus();
                Module.saveSettings();
                for (final CategoryPanel xd : panel.typePanel) {
                    for (final Slider slider5 : xd.sliders) {
                        final Slider slider4;
                        final Slider slider = slider4 = slider5;
                        final double n2 = slider.setting.getValue().doubleValue() * 40.0 / slider.setting.getMax();
                        slider4.lastDragX = n2;
                        slider5.dragX = n2;
                    }
                }
            }
        }
    }
    
    @Override
    public void colorConstructor(final ColorPreview colorPreview, final float x, final float y) {
        int i = 0;
        for (final RGBSlider.Colors xd : RGBSlider.Colors.values()) {
            colorPreview.sliders.add(new RGBSlider(x + 10.0f, y + i, colorPreview, xd));
            i += 12;
        }
    }
    
    @Override
    public void colorPrewviewDraw(final ColorPreview colorPreview, final float x, final float y) {
        final float xOff = colorPreview.x + colorPreview.categoryPanel.panel.dragX;
        final float yOff = colorPreview.y + colorPreview.categoryPanel.panel.dragY + 75.0f;
        RenderingUtil.rectangleBordered(xOff - 80.0f, yOff - 6.0f, xOff + 1.0f, yOff + 46.0f, 0.3, Colors.getColor(48), Colors.getColor(10));
        RenderingUtil.rectangle(xOff - 79.0f, yOff - 5.0f, xOff, yOff + 45.0f, Colors.getColor(17));
        RenderingUtil.rectangle(xOff - 74.0f, yOff - 6.0f, xOff - 73.0f + Client.fs.getWidth(colorPreview.colorName) + 1.0f, yOff - 4.0f, Colors.getColor(17));
        Client.fs.drawStringWithShadow(colorPreview.colorName, xOff - 73.0f, yOff - 8.0f, -1);
        for (final RGBSlider slider : colorPreview.sliders) {
            slider.draw(x, y);
        }
    }
    
    @Override
    public void rgbSliderDraw(final RGBSlider slider, final float x, final float y) {
        final float xOff = slider.x + slider.colorPreview.categoryPanel.panel.dragX - 75.0f;
        final float yOff = slider.y + slider.colorPreview.categoryPanel.panel.dragY + 74.0f;
        final double fraction = slider.dragX / 60.0;
        final double value = MathUtils.getIncremental(fraction * 255.0, 1.0);
        final ColorObject cO = slider.colorPreview.colorObject;
        int faggotNiggerColor = Colors.getColor(cO.red, cO.green, cO.blue, 255);
        int faggotNiggerColor2 = Colors.getColor(cO.red, cO.green, cO.blue, 120);
        RenderingUtil.rectangle(xOff, yOff, xOff + 60.0f, yOff + 6.0f, Colors.getColor(32));
        switch (slider.rgba) {
            case ALPHA: {
                faggotNiggerColor = Colors.getColor(ColorManager.hudColor.red, ColorManager.hudColor.green, ColorManager.hudColor.blue, 255);
                faggotNiggerColor2 = Colors.getColor(ColorManager.hudColor.red, ColorManager.hudColor.green, ColorManager.hudColor.blue, 120);
                break;
            }
        }
        RenderingUtil.rectangle(xOff, yOff, xOff + 60.0 * fraction, yOff + 6.0f, Colors.getColor(0));
        RenderingUtil.drawGradient(xOff, yOff, xOff + 60.0 * fraction, yOff + 6.0f, faggotNiggerColor, faggotNiggerColor2);
        String current = "R";
        switch (slider.rgba) {
            case BLUE: {
                current = "B";
                break;
            }
            case GREEN: {
                current = "G";
                break;
            }
            case ALPHA: {
                current = "A";
                break;
            }
        }
        Client.fs.drawStringWithShadow(current, xOff - 7.0f, yOff + 0.5f, Colors.getColor(220));
        final float textX = xOff + 30.0f - Client.fs.getWidth(Integer.toString((int)value)) / 2.0f;
        Client.fsmallbold.drawBorderedString(Integer.toString((int)value), textX, yOff + 5.0f, Colors.getColor(220));
        double newValue = 0.0;
        if (slider.dragging) {
            slider.dragX = x - slider.lastDragX;
            if (value <= 255.0 && value >= 0.0) {
                newValue = value;
            }
            switch (slider.rgba) {
                case RED: {
                    slider.colorPreview.colorObject.setRed((int)newValue);
                    break;
                }
                case GREEN: {
                    slider.colorPreview.colorObject.setGreen((int)newValue);
                    break;
                }
                case BLUE: {
                    slider.colorPreview.colorObject.setBlue((int)newValue);
                    break;
                }
                case ALPHA: {
                    slider.colorPreview.colorObject.setAlpha((int)newValue);
                    break;
                }
            }
        }
        if (slider.dragX <= 0.0) {
            slider.dragX = 0.0;
        }
        if (slider.dragX >= 60.0) {
            slider.dragX = 60.0;
        }
    }
    
    @Override
    public void rgbSliderClick(final RGBSlider slider, final float x, final float y, final int mouse) {
        final float xOff = slider.x + slider.colorPreview.categoryPanel.panel.dragX - 75.0f;
        final float yOff = slider.y + slider.colorPreview.categoryPanel.panel.dragY + 74.0f;
        if (slider.colorPreview.categoryPanel.enabled && x >= xOff && y >= yOff && x <= xOff + 60.0f && y <= yOff + 6.0f && mouse == 0) {
            slider.dragging = true;
            slider.lastDragX = x - slider.dragX;
        }
    }
    
    @Override
    public void rgbSliderMovedOrUp(final RGBSlider slider, final float x, final float y, final int mouse) {
        if (mouse == 0) {
            Color.saveStatus();
            slider.dragging = false;
        }
    }
    
    @Override
    public void SliderMouseClicked(final Slider slider, final int mouseX, final int mouseY, final int mouse, final CategoryPanel panel) {
        final float xOff = panel.categoryButton.panel.dragX;
        final float yOff = panel.categoryButton.panel.dragY;
        if (panel.visible && mouseX >= panel.x + xOff + slider.x && mouseY >= yOff + panel.y + slider.y - 6.0f && mouseX <= xOff + panel.x + slider.x + 40.0f && mouseY <= yOff + panel.y + slider.y + 4.0f && mouse == 0) {
            slider.dragging = true;
            slider.lastDragX = mouseX - slider.dragX;
        }
    }
    
    @Override
    public void SliderMouseMovedOrUp(final Slider slider, final int mouseX, final int mouseY, final int mouse, final CategoryPanel panel) {
        if (mouse == 0) {
            slider.dragging = false;
        }
    }
    
    @Override
    public void SliderDraw(final Slider slider, final float x, final float y, final CategoryPanel panel) {
        if (panel.visible) {
            GlStateManager.pushMatrix();
            final float xOff = panel.categoryButton.panel.dragX;
            final float yOff = panel.categoryButton.panel.dragY;
            final double percent = slider.dragX / 40.0;
            final double value = MathUtils.getIncremental(percent * 100.0 * (slider.setting.getMax() - slider.setting.getMin()) / 100.0 + slider.setting.getMin(), slider.setting.getInc());
            final float sliderX = (float)(percent * 38.0);
            RenderingUtil.rectangle(slider.x + xOff - 0.3, slider.y + yOff - 0.3, slider.x + xOff + 38.0f + 0.3, slider.y + yOff + 3.0f + 0.3, Colors.getColor(10));
            RenderingUtil.drawGradient(slider.x + xOff, slider.y + yOff, slider.x + xOff + 38.0f, slider.y + yOff + 3.0f, Colors.getColor(46), Colors.getColor(27));
            RenderingUtil.drawGradient(slider.x + xOff, slider.y + yOff, slider.x + xOff + sliderX, slider.y + yOff + 3.0f, Colors.getColor(ColorManager.hudColor.red, ColorManager.hudColor.green, ColorManager.hudColor.blue, 255), Colors.getColor(ColorManager.hudColor.red, ColorManager.hudColor.green, ColorManager.hudColor.blue, 120));
            final String xd = slider.setting.getName().charAt(0) + slider.setting.getName().toLowerCase().substring(1);
            final double setting = slider.setting.getValue().doubleValue();
            GlStateManager.pushMatrix();
            String valu2e = MathUtils.isInteger(setting) ? ((int)setting + "") : (setting + "");
            final String a = slider.setting.getName().toLowerCase();
            if (a.contains("fov")) {
                valu2e += "°";
            }
            else if (a.contains("delay")) {
                valu2e += "ms";
            }
            final float strWidth = Client.fs.getWidth(valu2e);
            final float textX = (sliderX + strWidth > 42.0f) ? (sliderX - strWidth) : (sliderX - strWidth / 2.0f);
            Client.fsmallbold.drawBorderedString(valu2e, slider.x + xOff + textX, slider.y + yOff + 1.5f, Colors.getColor(220));
            GlStateManager.scale(1.0f, 1.0f, 1.0f);
            GlStateManager.popMatrix();
            Client.fss.drawStringWithShadow(xd, slider.x + xOff, slider.y - 6.0f + yOff, Colors.getColor(220));
            if (slider.dragging) {
                slider.dragX = x - slider.lastDragX;
                final Object newValue = StringConversions.castNumber(Double.toString(value), slider.setting.getInc());
                slider.setting.setValue(newValue);
            }
            if (slider.setting.getValue().doubleValue() <= slider.setting.getMin()) {
                final Object newValue = StringConversions.castNumber(Double.toString(slider.setting.getMin()), slider.setting.getInc());
                slider.setting.setValue(newValue);
            }
            if (slider.setting.getValue().doubleValue() >= slider.setting.getMax()) {
                final Object newValue = StringConversions.castNumber(Double.toString(slider.setting.getMax()), slider.setting.getInc());
                slider.setting.setValue(newValue);
            }
            if (slider.dragX <= 0.0) {
                slider.dragX = 0.0;
            }
            if (slider.dragX >= 40.0) {
                slider.dragX = 40.0;
            }
            if ((x >= xOff + slider.x && y >= yOff + slider.y - 6.0f && x <= xOff + slider.x + 38.0f && y <= yOff + slider.y + 3.0f) || slider.dragging) {
                Client.fss.drawStringWithShadow(this.getDescription(slider.setting) + " Min: " + slider.setting.getMin() + " Max: " + slider.setting.getMax(), panel.categoryButton.panel.x + 2.0f + panel.categoryButton.panel.dragX + 55.0f, panel.categoryButton.panel.y + 9.0f + panel.categoryButton.panel.dragY, -1);
            }
            GlStateManager.popMatrix();
        }
    }
    
    private String getDescription(final Setting setting) {
        if (setting.getDesc() != null && !setting.getDesc().equalsIgnoreCase("")) {
            return setting.getDesc();
        }
        return "ERROR: No Description Found.";
    }
}
