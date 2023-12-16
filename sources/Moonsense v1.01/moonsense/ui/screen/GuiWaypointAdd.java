// 
// Decompiled by Procyon v0.5.36
// 

package moonsense.ui.screen;

import moonsense.ui.elements.Element;
import moonsense.settings.Setting;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.util.ResourceLocation;
import java.io.IOException;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import moonsense.utils.WatermarkRenderer;
import net.minecraft.client.gui.Gui;
import moonsense.MoonsenseClient;
import moonsense.features.ThemeSettings;
import org.lwjgl.input.Keyboard;
import java.awt.Color;
import moonsense.ui.utils.GuiUtils;
import moonsense.ui.elements.settings.ElementCategory;
import moonsense.ui.elements.settings.SettingElement;
import moonsense.features.modules.type.world.waypoints.WaypointsModule;
import java.util.concurrent.atomic.AtomicBoolean;
import net.minecraft.client.gui.ScaledResolution;
import moonsense.features.modules.type.world.waypoints.WaypointManager;
import net.minecraft.util.Vec3;
import net.minecraft.client.Minecraft;
import moonsense.utils.ColorObject;
import moonsense.ui.elements.button.GuiButtonIcon;
import moonsense.ui.elements.button.GuiCustomButton;
import moonsense.ui.elements.settings.color.SettingElementColor;
import moonsense.ui.elements.text.GuiCustomTextField;
import moonsense.features.modules.type.world.waypoints.Waypoint;

public class GuiWaypointAdd extends AbstractGuiScreen
{
    public Waypoint waypoint;
    public GuiCustomTextField name;
    public GuiCustomTextField x;
    public GuiCustomTextField y;
    public GuiCustomTextField z;
    public SettingElementColor colorComponent;
    public GuiCustomButton save;
    public GuiCustomButton delete;
    public GuiButtonIcon back;
    public final AbstractGuiScreen parentScreen;
    private boolean editWaypoint;
    private ColorObject color;
    
    public GuiWaypointAdd(final AbstractGuiScreen parent, final boolean edit) {
        this(parent, new Waypoint("", new Vec3(Minecraft.getMinecraft().thePlayer.posX, Minecraft.getMinecraft().thePlayer.posY, Minecraft.getMinecraft().thePlayer.posZ), WaypointManager.getWaypointWorld(), Minecraft.getMinecraft().thePlayer.dimension, true, false), edit);
    }
    
    public GuiWaypointAdd(final AbstractGuiScreen parent, final Waypoint waypoint, final boolean edit) {
        this(parent, waypoint, true, edit);
    }
    
    public GuiWaypointAdd(final AbstractGuiScreen parent, final Waypoint waypoint, final boolean bl, final boolean edit) {
        this.editWaypoint = edit;
        this.parentScreen = parent;
        this.waypoint = waypoint;
        final ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
        this.color = waypoint.getColor();
        final AtomicBoolean found = new AtomicBoolean(false);
        final AtomicBoolean atomicBoolean;
        this.colorComponent = new SettingElementColor(sr.getScaledWidth() / 2 + 27, sr.getScaledHeight() / 2 - 35, 10, 10, this.width / 2 + 100 - 15, 0, WaypointsModule.INSTANCE.defaultColor, (mainElement, expanded) -> this.elements.forEach(settingElement -> {
            if ((settingElement instanceof SettingElement || settingElement instanceof ElementCategory) && (settingElement == mainElement || atomicBoolean.get())) {
                atomicBoolean.set(true);
                if (settingElement != mainElement && this.elements.indexOf(settingElement) > this.elements.indexOf(mainElement)) {
                    settingElement.addOffsetToY(((boolean)expanded) ? 85 : -85);
                }
            }
        }), (module, settingElement) -> {
            this.color = new ColorObject(GuiUtils.getRGB(settingElement.color, settingElement.alpha), settingElement.chroma.active, (int)settingElement.chromaSpeed.getDenormalized());
            module.setValue(new ColorObject(GuiUtils.getRGB(settingElement.color, settingElement.alpha), settingElement.chroma.active, (int)settingElement.chromaSpeed.getDenormalized()));
            return;
        }, this);
        final int[] hsv = GuiUtils.rgbToHsv(new Color(this.color.getColor()).getRGB());
        this.colorComponent.colorPane.saturation = hsv[1];
        this.colorComponent.colorPane.value = hsv[2];
        this.colorComponent.alphaPane.alpha = this.color.getAlpha();
        this.colorComponent.huePane.hue = hsv[0];
        this.colorComponent.chroma.active = this.color.isChroma();
        this.colorComponent.chromaSpeed.sliderValue = this.color.getChromaSpeed() / this.colorComponent.chromaSpeed.max;
    }
    
    @Override
    public void initGui() {
        final ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
        (this.name = new GuiCustomTextField(3, sr.getScaledWidth() / 2 - 200 + 30, sr.getScaledHeight() / 2 - 63, 70, 15, false, "Name")).setMaxStringLength(20);
        (this.x = new GuiCustomTextField(4, sr.getScaledWidth() / 2 - 85, sr.getScaledHeight() / 2 - 63, 75, 15, false, "X")).setMaxStringLength(9);
        this.x.setText(String.format("%.2f", this.waypoint.getLocation().xCoord));
        (this.y = new GuiCustomTextField(5, sr.getScaledWidth() / 2 + 5, sr.getScaledHeight() / 2 - 63, 75, 15, false, "Y")).setMaxStringLength(9);
        this.y.setText(String.format("%.2f", this.waypoint.getLocation().yCoord));
        (this.z = new GuiCustomTextField(6, sr.getScaledWidth() / 2 + 95, sr.getScaledHeight() / 2 - 63, 75, 15, false, "Z")).setMaxStringLength(9);
        this.z.setText(String.format("%.2f", this.waypoint.getLocation().zCoord));
        this.save = new GuiCustomButton(1, sr.getScaledWidth() / 2 + 200 - 40, sr.getScaledHeight() / 2 + 100 - 25, 35, 20, "Save", true);
        this.delete = new GuiCustomButton(2, sr.getScaledWidth() / 2 - 200 + 5, sr.getScaledHeight() / 2 + 100 - 25, 35, 20, "Delete", true);
        this.back = new GuiButtonIcon(0, sr.getScaledWidth() / 2 - 195, sr.getScaledHeight() / 2 - 95, 20, 20, "back.png", "", true);
        if (this.editWaypoint) {
            this.name.setText(this.waypoint.getName());
        }
        else {
            this.name.setText("New Waypoint");
        }
        Keyboard.enableRepeatEvents(true);
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        this.drawBackground(0);
        GuiUtils.drawRoundedRect((float)(this.width / 2 - 200), (float)(this.height / 2 - 100), (float)(this.width / 2 + 200), (float)(this.height / 2 + 100), 3.0f, ThemeSettings.INSTANCE.uiBackgroundMain.getColor());
        GuiUtils.drawRoundedOutline(this.width / 2 - 200, this.height / 2 - 100, this.width / 2 + 200, this.height / 2 + 100, 3.0f, 2.0f, ThemeSettings.INSTANCE.uiAccent.getColor());
        MoonsenseClient.titleRenderer.drawCenteredString(String.valueOf(this.editWaypoint ? "Edit" : "Add") + " Waypoint", (float)(this.width / 2), (float)(this.height / 2 - 100 + 10), -1);
        MoonsenseClient.titleRenderer.drawString("Name", this.width / 2 - 200 + 5, this.height / 2 - 60, -1);
        MoonsenseClient.titleRenderer.drawString("X", this.width / 2 - 95, this.height / 2 - 60, -1);
        MoonsenseClient.titleRenderer.drawString("Y", this.width / 2 - 5, this.height / 2 - 60, -1);
        MoonsenseClient.titleRenderer.drawString("Z", this.width / 2 + 85, this.height / 2 - 60, -1);
        this.name.drawTextBox();
        this.x.drawTextBox();
        this.y.drawTextBox();
        this.z.drawTextBox();
        this.back.drawButton(Minecraft.getMinecraft(), mouseX, mouseY);
        this.save.drawButton(Minecraft.getMinecraft(), mouseX, mouseY);
        if (this.editWaypoint) {
            this.delete.drawButton(Minecraft.getMinecraft(), mouseX, mouseY);
        }
        this.colorComponent.expanded = true;
        this.colorComponent.render(mouseX, mouseY, partialTicks);
    }
    
    @Override
    public void drawBackground(final int tint) {
        Gui.drawRect(0, 0, this.width, this.height, new Color(0, 0, 0, 50).getRGB());
        WatermarkRenderer.render(this.width, this.height);
    }
    
    @Override
    protected void keyTyped(final char typedChar, final int keyCode) throws IOException {
        if (keyCode == 1) {
            this.mc.displayGuiScreen(this.parentScreen);
        }
        if (keyCode == 28) {
            this.actionPerformed(this.save);
            return;
        }
        if (keyCode == 211 && this.editWaypoint) {
            this.actionPerformed(this.delete);
            return;
        }
        this.name.textboxKeyTyped(typedChar, keyCode);
        if (Character.isDigit(typedChar) || typedChar == '.' || typedChar == '-' || keyCode == 14) {
            if (typedChar != '.' || !this.x.getText().contains(".")) {
                this.x.textboxKeyTyped(typedChar, keyCode);
            }
            if (typedChar != '.' || !this.y.getText().contains(".")) {
                this.y.textboxKeyTyped(typedChar, keyCode);
            }
            if (typedChar != '.' || !this.z.getText().contains(".")) {
                this.z.textboxKeyTyped(typedChar, keyCode);
            }
        }
    }
    
    @Override
    protected void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException {
        this.name.mouseClicked(mouseX, mouseY, mouseButton);
        this.x.mouseClicked(mouseX, mouseY, mouseButton);
        this.y.mouseClicked(mouseX, mouseY, mouseButton);
        this.z.mouseClicked(mouseX, mouseY, mouseButton);
        this.colorComponent.mouseClicked(mouseX, mouseY, mouseButton);
        if (this.back.isMouseOver()) {
            this.actionPerformed(this.back);
        }
        if (this.save.isMouseOver()) {
            this.actionPerformed(this.save);
        }
        if (this.delete.isMouseOver() && this.editWaypoint) {
            this.actionPerformed(this.delete);
        }
    }
    
    @Override
    protected void mouseClickMove(final int mouseX, final int mouseY, final int clickedMouseButton, final long timeSinceLastClick) {
        this.colorComponent.mouseDragged(mouseX, mouseY);
    }
    
    @Override
    protected void mouseReleased(final int mouseX, final int mouseY, final int state) {
        this.colorComponent.mouseReleased(mouseX, mouseY, state);
    }
    
    @Override
    protected void actionPerformed(final GuiButton button) throws IOException {
        Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.createPositionedSoundRecord(new ResourceLocation("gui.button.press"), 1.0f));
        switch (button.id) {
            case 0: {
                Minecraft.getMinecraft().displayGuiScreen(this.parentScreen);
                break;
            }
            case 1: {
                try {
                    if (!this.name.getText().isEmpty() && !this.x.getText().isEmpty() && !this.y.getText().isEmpty()) {
                        if (!this.z.getText().isEmpty()) {
                            final int oldWaypointIndex = MoonsenseClient.INSTANCE.getWaypointManager().indexOf(this.waypoint);
                            final String string = this.name.getText();
                            final float x = Float.parseFloat(this.x.getText());
                            final float y = Float.parseFloat(this.y.getText());
                            final float z = Float.parseFloat(this.z.getText());
                            this.waypoint.setName(string);
                            this.waypoint.setLocation(new Vec3(x, y, z));
                            final int wpColor = GuiUtils.hsvToRgb(this.colorComponent.huePane.hue, this.colorComponent.colorPane.saturation, this.colorComponent.colorPane.value);
                            final int wpAlpha = this.colorComponent.alphaPane.alpha;
                            final boolean wpChroma = this.colorComponent.chroma.active;
                            final int wpChromaSpeed = (int)(this.colorComponent.chromaSpeed.sliderValue * this.colorComponent.chromaSpeed.max);
                            final float[] wpColorComponents = new Color(wpColor).getRGBColorComponents(null);
                            this.waypoint.setColor(new ColorObject(new Color(wpColorComponents[0], wpColorComponents[1], wpColorComponents[2], (float)(wpAlpha / 255)).getRGB(), wpChroma, wpChromaSpeed));
                            if (this.editWaypoint) {
                                MoonsenseClient.INSTANCE.getWaypointManager().set(oldWaypointIndex, this.waypoint);
                            }
                            else {
                                MoonsenseClient.INSTANCE.getWaypointManager().add(this.waypoint);
                            }
                            Minecraft.getMinecraft().displayGuiScreen(this.parentScreen);
                            new Thread("Save Waypoints") {
                                @Override
                                public void run() {
                                    MoonsenseClient.INSTANCE.getWaypointManager().save();
                                }
                            }.start();
                        }
                    }
                }
                catch (Exception exception) {
                    exception.printStackTrace();
                }
                break;
            }
            case 2: {
                MoonsenseClient.INSTANCE.getWaypointManager().remove(this.waypoint);
                Minecraft.getMinecraft().displayGuiScreen(this.parentScreen);
                new Thread("Save Waypoints") {
                    @Override
                    public void run() {
                        MoonsenseClient.INSTANCE.getWaypointManager().save();
                    }
                }.start();
                break;
            }
        }
    }
    
    private boolean lIlIlIlIlIIlIIlIIllIIIIIl(final Waypoint waypoint, final boolean bl) {
        MoonsenseClient.INSTANCE.getWaypointManager().add(waypoint);
        return true;
    }
}
