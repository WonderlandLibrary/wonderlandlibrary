package cc.novoline.modules.visual;

import cc.novoline.events.EventTarget;
import cc.novoline.events.events.Render2DEvent;
import cc.novoline.gui.screen.dropdown.DropdownGUI;
import cc.novoline.gui.screen.setting.Setting;
import cc.novoline.modules.AbstractModule;
import cc.novoline.modules.EnumModuleType;
import cc.novoline.modules.ModuleManager;
import cc.novoline.modules.configurations.annotation.Property;
import cc.novoline.modules.configurations.property.object.BooleanProperty;
import cc.novoline.modules.configurations.property.object.DoubleProperty;
import cc.novoline.utils.ScaleUtils;
import net.minecraft.client.gui.ScaledResolution;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.lwjgl.opengl.GL11;

import static cc.novoline.gui.screen.setting.Manager.put;
import static cc.novoline.gui.screen.setting.SettingType.CHECKBOX;
import static cc.novoline.gui.screen.setting.SettingType.SLIDER;
import static cc.novoline.modules.configurations.property.object.PropertyFactory.booleanTrue;
import static cc.novoline.modules.configurations.property.object.PropertyFactory.createDouble;
import static cc.novoline.utils.ColorUtils.getColor;
import static cc.novoline.utils.RenderUtils.drawBorderedRect;
import static org.lwjgl.opengl.GL11.glColor4f;

public final class Crosshair extends AbstractModule {

    /* properties @off */
    @Property("dynamic")
    private final BooleanProperty dynamic = booleanTrue();
    @Property("gap")
    private final DoubleProperty gap = createDouble(0.95D).minimum(0.25D).maximum(15.0D);
    @Property("width")
    private final DoubleProperty width = createDouble(0.25D).minimum(0.0D).maximum(10.0D);
    @Property("size")
    private final DoubleProperty size = createDouble(4.0D).minimum(0.25D).maximum(15.0D);

    /* constructors @on */
    public Crosshair(@NonNull ModuleManager moduleManager) {
        super(moduleManager, "Crosshair", EnumModuleType.VISUALS, "Crosshair like in cs:go!");
        put(new Setting("Dynamic", "Dynamic", CHECKBOX, this, this.dynamic));
        put(new Setting("Gap", "Gap", SLIDER, this, this.gap, 0.05));
        put(new Setting("Width", "Width", SLIDER, this, this.width, 0.01));
        put(new Setting("CH_size", "Size", SLIDER, this, this.size, 0.05));
    }

    /* events */
    @EventTarget
    public void onEvent(Render2DEvent event) {
        if(mc.currentScreen instanceof DropdownGUI){
            return;
        }
        GL11.glPushMatrix();
        ScaleUtils.scale(mc);
        final double gap = this.gap.get(), // @off
                width = this.width.get(),
                size = this.size.get();
        final boolean dynamic = this.dynamic.get();
        final int oColor = getColor(0, 0, 0, 255),
                iColor = getModule(HUD.class).getHUDColor();

        final ScaledResolution resolution = event.getResolution();
        final int scaledWidth = resolution.getScaledWidthStatic(mc),
                scaledHeight = resolution.getScaledHeightStatic(mc); // @on

        final float i = scaledWidth / 2.0F;
        final float j = scaledHeight / 2.0F;

        if (dynamic) {
            if (this.mc.player.isMoving()) {
                drawBorderedRect(i - width, j - gap - size - 2, i + 1.0F + width, j - gap - 2, 0.5F, oColor, iColor);
                drawBorderedRect(i - width, j + gap + 1 + (this.mc.player.isMoving() ? 2 : 0) - 0.15F,
                        i + 1.0f + width, j + 1 + gap + size + (this.mc.player.isMoving() ? 2 : 0) - 0.15F, 0.5F,
                        oColor, iColor);
                drawBorderedRect(i - gap - size - (this.mc.player.isMoving() ? 2 : 0) + 0.5F, j - width,
                        i - gap - (this.mc.player.isMoving() ? 2 : 0) + 0.5, j + 1.0F + width, 0.5F, oColor, iColor);
                drawBorderedRect(i + 0.5f + gap + (this.mc.player.isMoving() ? 2 : 0), j - width,
                        i + size + gap + 0.5F + (this.mc.player.isMoving() ? 2 : 0), j + 1.0F + width, 0.5F, oColor,
                        iColor);
            } else {
                drawBorderedRect(i - width, j - gap - size - 0, i + 1.0F + width, j - gap - 0, 0.5F, oColor, iColor);
                drawBorderedRect(i - width, j + gap + 1 + (this.mc.player.isMoving() ? 2 : 0) - 0.15F,
                        i + 1.0f + width, j + 1 + gap + size + (this.mc.player.isMoving() ? 2 : 0) - 0.15F, 0.5F,
                        oColor, iColor);
                drawBorderedRect(i - gap - size - (this.mc.player.isMoving() ? 2 : 0) + 0.5F, j - width,
                        i - gap - (this.mc.player.isMoving() ? 2 : 0) + 0.5, j + 1.0F + width, 0.5F, oColor, iColor);
                drawBorderedRect(i + 0.5f + gap + (this.mc.player.isMoving() ? 2 : 0), j - width,
                        i + size + gap + 0.5F + (this.mc.player.isMoving() ? 2 : 0), j + 1.0F + width, 0.5F, oColor,
                        iColor);
            }
        } else {
            if (this.mc.player.isMoving()) {
                drawBorderedRect(i - width, j - gap - size - 0, i + 1.0F + width, j - gap - 0, 0.5F, oColor, iColor);
                drawBorderedRect(i - width, j + gap + 1 + 0 - 0.15F, i + 1.0f + width, j + 1 + gap + size + 0 - 0.15F,
                        0.5F, oColor, iColor);
                drawBorderedRect(i - gap - size - 0 + 0.5F, j - width, i - gap - 0 + 0.5, j + 1.0F + width, 0.5F,
                        oColor, iColor);
                drawBorderedRect(i + 0.5f + gap + 0, j - width, i + size + gap + 0.5F + 0, j + 1.0F + width, 0.5F,
                        oColor, iColor);
            } else {
                drawBorderedRect(i - width, j - gap - size - 0, i + 1.0F + width, j - gap - 0, 0.5F, oColor, iColor);
                drawBorderedRect(i - width, j + gap + 1 + 0 - 0.15F, i + 1.0f + width, j + 1 + gap + size + 0 - 0.15F,
                        0.5F, oColor, iColor);
                drawBorderedRect(i - gap - size - 0 + 0.5F, j - width, i - gap - 0 + 0.5, j + 1.0F + width, 0.5F,
                        oColor, iColor);
                drawBorderedRect(i + 0.5f + gap + 0, j - width, i + size + gap + 0.5F + 0, j + 1.0F + width, 0.5F,
                        oColor, iColor);
            }
        }

        glColor4f(1, 1, 1, 1);
        GL11.glPopMatrix();
    }

}
