// 
// Decompiled by Procyon v0.5.36
// 

package moonsense.features.modules.type.keystrokes;

import moonsense.config.utils.AnchorPoint;
import moonsense.enums.ModuleCategory;
import net.minecraft.client.settings.KeyBinding;
import moonsense.features.modules.type.keystrokes.keys.KeySpacebar;
import moonsense.features.modules.type.keystrokes.keys.KeyMouse;
import moonsense.features.modules.type.keystrokes.keys.FillerKey;
import moonsense.features.modules.type.keystrokes.keys.Key;
import java.util.Iterator;
import org.lwjgl.opengl.GL11;
import java.awt.Color;
import moonsense.features.SCModule;
import moonsense.settings.Setting;
import moonsense.features.SCAbstractRenderModule;

public class KeystrokesModule extends SCAbstractRenderModule
{
    public final int gap = 2;
    public KeyLayoutBuilder builder;
    public final Setting boxColor;
    public final Setting boxPressedColor;
    public final Setting boxSize;
    public final Setting boxFade;
    public final Setting doFade;
    public final Setting showCPS;
    public final Setting textColor;
    public final Setting pressedTextColor;
    public final Setting outline;
    public final Setting outlineColor;
    public final Setting keyMode;
    public final Setting showMouseButtons;
    public final Setting showSpacebar;
    public final Setting spacebarColor;
    public float offset;
    public static KeystrokesModule INSTANCE;
    
    public KeystrokesModule() {
        super("Keystrokes", "Displays when your movement keys, mouse buttons, or space bar is pressed.");
        KeystrokesModule.INSTANCE = this;
        new Setting(this, "Box Options");
        this.boxColor = new Setting(this, "Box Color").setDefault(new Color(0, 0, 0, 50).getRGB(), 0);
        this.boxPressedColor = new Setting(this, "Box Pressed Color").setDefault(new Color(255, 255, 255, 100).getRGB(), 0);
        this.boxSize = new Setting(this, "Box Size").setDefault(20.0f).setRange(10.0f, 30.0f, 0.1f).onValueChanged(setting -> this.builder = this.createLayout());
        this.boxFade = new Setting(this, "Fade", "keybox.fadeSpeed").setDefault(250).setRange(1, 500, 1);
        this.doFade = new Setting(this, "Fade", "keybox.doFade").setDefault(false);
        this.showCPS = new Setting(this, "Show CPS").setDefault(true);
        this.textColor = new Setting(this, "Text Color").setDefault(new Color(255, 255, 255, 255).getRGB(), 0);
        this.pressedTextColor = new Setting(this, "Pressed Text Color").setDefault(new Color(0, 0, 0, 255).getRGB(), 0);
        this.outline = new Setting(this, "Outline").setDefault(false);
        this.outlineColor = new Setting(this, "Outline Color").setDefault(new Color(0, 0, 0, 100).getRGB(), 0);
        new Setting(this, "Key Options");
        this.keyMode = new Setting(this, "Key Mode").setDefault(0).setRange("Letter", "Arrow");
        this.showMouseButtons = new Setting(this, "Show Mouse Buttons").setDefault(true).onValueChanged(setting -> this.builder = this.createLayout());
        this.showSpacebar = new Setting(this, "Show Spacebar").setDefault(true).onValueChanged(setting -> this.builder = this.createLayout());
        this.spacebarColor = new Setting(this, "Spacebar Color").setDefault(Color.white.getRGB(), 0);
    }
    
    @Override
    public int getWidth() {
        if (this.builder == null) {
            this.builder = this.createLayout();
        }
        return (int)this.builder.getWidth();
    }
    
    @Override
    public int getHeight() {
        return (int)this.builder.getHeight();
    }
    
    @Override
    public void render(final float x, final float y) {
        if (this.builder == null) {
            this.builder = this.createLayout();
        }
        GL11.glPushMatrix();
        GL11.glTranslatef(x, y, 0.0f);
        for (final Rows row : this.builder.rows) {
            GL11.glPushMatrix();
            Key[] keys;
            for (int length = (keys = row.getKeys()).length, i = 0; i < length; ++i) {
                final Key key = keys[i];
                key.render(false);
                final double offset = row.getWidth() + this.builder.getGapSize();
                this.offset += key.getHeight();
                GL11.glTranslated(offset, 0.0, 0.0);
            }
            GL11.glPopMatrix();
            GL11.glTranslated(0.0, row.getHeight() + this.builder.getGapSize(), 0.0);
            this.offset = 0.0f;
        }
        GL11.glPopMatrix();
    }
    
    @Override
    public void renderDummy(final float x, final float y) {
        if (this.builder == null) {
            this.builder = this.createLayout();
        }
        GL11.glPushMatrix();
        GL11.glTranslatef(x, y, 0.0f);
        for (final Rows row : this.builder.rows) {
            GL11.glPushMatrix();
            Key[] keys;
            for (int length = (keys = row.getKeys()).length, i = 0; i < length; ++i) {
                final Key key = keys[i];
                key.render(false);
                final double offset = row.getWidth() + this.builder.getGapSize();
                this.offset += key.getHeight();
                GL11.glTranslated(offset, 0.0, 0.0);
            }
            GL11.glPopMatrix();
            GL11.glTranslated(0.0, row.getHeight() + this.builder.getGapSize(), 0.0);
            this.offset = 0.0f;
        }
        GL11.glPopMatrix();
    }
    
    private KeyLayoutBuilder createLayout() {
        final FillerKey filler = new FillerKey(2, this);
        final Key keyW = this.create(this.mc.gameSettings.keyBindForward, this);
        final Key keyA = this.create(this.mc.gameSettings.keyBindLeft, this);
        final Key keyS = this.create(this.mc.gameSettings.keyBindBack, this);
        final Key keyD = this.create(this.mc.gameSettings.keyBindRight, this);
        final Key keyLMB = new KeyMouse(2, this.mc.gameSettings.keyBindAttack, this).setLeft();
        final Key keyRMB = new KeyMouse(2, this.mc.gameSettings.keyBindUseItem, this).setRight();
        final Key keySpace = new KeySpacebar(2, this.mc.gameSettings.keyBindJump, this);
        final KeyLayoutBuilder klb = new KeyLayoutBuilder().setGapSize(2).setWidth(this.boxSize.getFloat() * 3.0f + 4.0f).addRow(filler, keyW, filler).addRow(keyA, keyS, keyD);
        if (this.showMouseButtons.getBoolean()) {
            klb.addRow(keyLMB, keyRMB);
        }
        if (this.showSpacebar.getBoolean()) {
            klb.addRow(keySpace);
        }
        return klb.build();
    }
    
    private Key create(final KeyBinding keyBinding, final KeystrokesModule keystrokesModule) {
        return new Key(2, keyBinding, keystrokesModule);
    }
    
    @Override
    public ModuleCategory getCategory() {
        return ModuleCategory.HUD;
    }
    
    @Override
    public AnchorPoint getDefaultPosition() {
        return AnchorPoint.BOTTOM_RIGHT;
    }
}
