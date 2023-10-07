package cc.novoline.modules.visual;

import cc.novoline.events.EventTarget;
import cc.novoline.events.events.Render2DEvent;
import cc.novoline.gui.screen.setting.Setting;
import cc.novoline.gui.screen.setting.SettingType;
import cc.novoline.modules.AbstractModule;
import cc.novoline.modules.EnumModuleType;
import cc.novoline.modules.ModuleManager;
import cc.novoline.modules.configurations.annotation.Property;
import cc.novoline.modules.configurations.property.object.DoubleProperty;
import cc.novoline.modules.configurations.property.object.IntProperty;
import cc.novoline.utils.PlayerUtils;
import cc.novoline.utils.RenderUtils;
import cc.novoline.utils.RotationUtil;
import cc.novoline.utils.ScaleUtils;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.lwjgl.opengl.GL11;

import java.awt.*;

import static cc.novoline.gui.screen.setting.Manager.put;
import static cc.novoline.modules.PlayerManager.EnumPlayerType.TARGET;
import static cc.novoline.modules.configurations.property.object.PropertyFactory.createDouble;
import static cc.novoline.modules.configurations.property.object.PropertyFactory.createInt;
import static java.lang.Math.toRadians;

public final class Radar extends AbstractModule {

    private final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

    /* properties @off */
    @Property("alpha")
    private final IntProperty alpha = createInt(180).minimum(50).maximum(255);
    @Property("scale")
    private final DoubleProperty scale = createDouble(0.85D).minimum(0.25D).maximum(5.0D);
    @Property("x")
    private final IntProperty x = createInt(1).minimum(1).maximum((int) (screenSize.getWidth() / 2));
    @Property("y")
    private final IntProperty y = createInt(152).minimum(1).maximum((int) (screenSize.getHeight() / 2));
    @Property("size")
    private final IntProperty size = createInt(120).minimum(50).maximum(200);

    /* constructors @on */
    public Radar(@NonNull ModuleManager moduleManager) {
        super(moduleManager, "Radar", EnumModuleType.VISUALS, "Useful for games like uhc/sg");
        put(new Setting("RADAR_ALPHA", "Alpha", SettingType.SLIDER, this, this.alpha, 1));
        put(new Setting("RADAR_SCALE", "Scale", SettingType.SLIDER, this, this.scale, 0.05));
        put(new Setting("RADAR_X_POS", "X", SettingType.SLIDER, this, this.x, 5));
        put(new Setting("RADAR_Y_POS", "Y", SettingType.SLIDER, this, this.y, 5));
        put(new Setting("RADAR_SIZE", "Size", SettingType.SLIDER, this, this.size, 5));
    }

    /* events */
    @EventTarget
    public void radar(Render2DEvent e) {
        if (!(mc.currentScreen instanceof GuiChat)) {
            renderRadar();
        }
    }

    public void renderRadar() {
        GL11.glPushMatrix();
        ScaleUtils.scale(mc);

        Gui.drawRect(x.get() - 1, y.get() - 1, x.get() + size.get() + 1, y.get() + size.get() + 1, new Color(29, 29, 29, 255).getRGB());
        Gui.drawRect(x.get(), y.get(), x.get() + size.get(), y.get() + size.get(), new Color(40, 40, 40, 255).getRGB());

        Gui.drawRect(x.get() + size.get() / 2 - 0.3 + 1, y.get(), x.get() + size.get() / 2 + 0.3 + 1, y.get() + size.get(), new Color(255, 255, 255, 50).getRGB());
        Gui.drawRect(x.get(), y.get() + size.get() / 2 - 0.3, x.get() + size.get(), y.get() + size.get() / 2 + 0.3, new Color(255, 255, 255, 50).getRGB());


        for (Entity entity : mc.world.getLoadedEntityList()) {
            if (entity == mc.player) continue;
            if (entity instanceof EntityPlayer && !entity.isInvisible()) {

                float yawToEntity = RotationUtil.getYawToPoint(entity.posX, entity.posZ);
                float yawDiff = -(yawToEntity - mc.player.rotationYaw + 180);
                double yawDiffRad = toRadians(yawDiff);

                double x = Math.abs(mc.player.posX - entity.posX);
                double z = Math.abs(mc.player.posZ - entity.posZ);
                double distance = Math.sqrt(x * x + z * z) / scale.get();

                int color = 0xffffffff;
                if (novoline.playerManager.hasType(entity.getName(), TARGET)) {
                    color = new Color(255, 59, 59, 255).getRGB();
                } else if (novoline.playerManager.hasType(entity.getName(), TARGET)) {
                    color = new Color(169, 255, 43, 255).getRGB();
                } else if (PlayerUtils.inTeam(entity, mc.player)) {
                    color = new Color(0, 231, 255, 255).getRGB();
                }

                if (Math.sin(yawDiffRad) > 0 && Math.cos(yawDiffRad) < 0 &&
                        Math.sin(yawDiffRad) * distance < size.get() / 2 && -Math.cos(yawDiffRad) * distance < size.get() / 2 ||
                        Math.sin(yawDiffRad) < 0 && Math.cos(yawDiffRad) < 0 &&
                                -Math.sin(yawDiffRad) * distance < size.get() / 2 && -Math.cos(yawDiffRad) * distance < size.get() / 2 ||
                        Math.sin(yawDiffRad) > 0 && Math.cos(yawDiffRad) > 0 &&
                                Math.sin(yawDiffRad) * distance < size.get() / 2 && Math.cos(yawDiffRad) * distance < size.get() / 2 ||
                        Math.sin(yawDiffRad) < 0 && Math.cos(yawDiffRad) > 0 &&
                                -Math.sin(yawDiffRad) * distance < size.get() / 2 && Math.cos(yawDiffRad) * distance < size.get() / 2) {

                    RenderUtils.drawBorderedRect(
                            this.x.get() + size.get() / 2 +
                                    Math.sin(yawDiffRad) * distance - 1f,
                            this.y.get() + size.get() / 2 +
                                    Math.cos(yawDiffRad) * distance - 1f,

                            this.x.get() + size.get() / 2 +
                                    Math.sin(yawDiffRad) * distance + 1f,

                            this.y.get() + size.get() / 2 +
                                    Math.cos(yawDiffRad) * distance + 1f,
                            0.1f, color, color);
                }


            }
        }
        GL11.glPopMatrix();
    }

    public IntProperty getSize() {
        return size;
    }

    public IntProperty getX() {
        return x;
    }

    public IntProperty getY() {
        return y;
    }
}
