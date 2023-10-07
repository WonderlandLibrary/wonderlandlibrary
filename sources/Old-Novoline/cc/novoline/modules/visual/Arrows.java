package cc.novoline.modules.visual;

import cc.novoline.events.EventTarget;
import cc.novoline.events.events.Render2DEvent;
import cc.novoline.gui.screen.setting.Manager;
import cc.novoline.gui.screen.setting.Setting;
import cc.novoline.gui.screen.setting.SettingType;
import cc.novoline.modules.AbstractModule;
import cc.novoline.modules.EnumModuleType;
import cc.novoline.modules.ModuleManager;
import cc.novoline.modules.PlayerManager;
import cc.novoline.modules.configurations.annotation.Property;
import cc.novoline.modules.configurations.property.object.*;
import cc.novoline.utils.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.Comparator;
import java.util.stream.Collectors;

import static java.lang.Math.toRadians;
import static net.minecraft.util.MathHelper.*;

/**
 * @author Artyom Popov
 * @since October 10, 2020 @ 4:50 PM
 */
public final class Arrows extends AbstractModule {

    @Property("only_invisible")
    private final BooleanProperty onlyInvisibles = PropertyFactory.booleanTrue();
    @Property("radius")
    private final IntProperty radius = PropertyFactory.createInt(150).minimum(55).maximum(165);
    @Property("color")
    private final ColorProperty color = PropertyFactory.createColor(0xFF_00_00_FF);
    @Property("targets")
    private final ListProperty<String> targets = PropertyFactory.createList("Players").acceptableValues("Players", "Animals", "Mobs");
    @Property("filters")
    private final ListProperty<String> filters = PropertyFactory.createList("Teams").acceptableValues("Teams");
    @Property("targets-only")
    private final BooleanProperty targetsOnly = PropertyFactory.booleanFalse();
    @Property("color-type")
    private final StringProperty colorType = PropertyFactory.createString("Static").acceptableValues("Static", "Team", "Rainbow");
    @Property("rainbow-type")
    private final StringProperty rainbowType = PropertyFactory.createString("Static").acceptableValues("Static", "Cycle");
    @Property("alpha-type")
    private final StringProperty alphaType = PropertyFactory.createString("Distance").acceptableValues("Distance", "Pulse", "Static");
    @Property("alpha")
    private final IntProperty alpha = PropertyFactory.createInt(255).minimum(0).maximum(255);

    public Arrows(ModuleManager moduleManager) {
        super(moduleManager, "Arrows", EnumModuleType.VISUALS, "Shows arrows to players");
        Manager.put(new Setting("ARROWS_ONLY_VISIBLES", "Not in FOV only", SettingType.CHECKBOX, this, onlyInvisibles));
        Manager.put(new Setting("ARROWS_TARGETS_ONLY", "Targets Only", SettingType.CHECKBOX, this, targetsOnly));
        Manager.put(new Setting("ARROWS_RADIUS", "Radius", SettingType.SLIDER, this, radius, 1));
        Manager.put(new Setting("ARROWS_TARGETS", "Targets", SettingType.SELECTBOX, this, targets));
        Manager.put(new Setting("ARROWS_FILTER", "Filters", SettingType.SELECTBOX, this, filters, () -> targets.contains("Players")));
        Manager.put(new Setting("ARROWS_COLORTYPE", "Color Type", SettingType.COMBOBOX, this, colorType));
        Manager.put(new Setting("ARROWS_RAINBOWTYPE", "Rainbow Type", SettingType.COMBOBOX, this, rainbowType,
                () -> colorType.get().equalsIgnoreCase("Rainbow")));
        Manager.put(new Setting("ARROWS_COLOR", "Color", SettingType.COLOR_PICKER, this, color, null,
                () -> colorType.get().equalsIgnoreCase("Static")));
        Manager.put(new Setting("ARROWS_ALPHA", "Alpha Type", SettingType.COMBOBOX, this, this.alphaType));
        Manager.put(new Setting("ARROWS_ALPHA_SLIDER", "Alpha", SettingType.SLIDER, this, alpha, 10,
                () -> alphaType.get().equalsIgnoreCase("Static")));

    }

    public static AbstractModule create(ModuleManager moduleManager) {
        return new Arrows(moduleManager);
    }
    //endregion

    private static final int HALF_PI_DEGREES_INT = 90;
    private static final float TWO_PI_DEGREES_FLOAT = 360.0F;
    private static final float RGB_MAX_FLOAT = 255.0F;
    private static double WIDTH = 0.03D;
    private static double HEIGHT = 5;
    private int alph = 1;
    private boolean invert;

    protected final Vec3 getVectorForRotation(float pitch, float yaw) {
        float f = MathHelper.cos(Math.toRadians(-yaw) - (float) Math.PI);
        float f1 = MathHelper.sin(Math.toRadians(-yaw) - (float) Math.PI);
        float f2 = -MathHelper.cos(Math.toRadians(-pitch));
        float f3 = MathHelper.sin(Math.toRadians(-pitch));
        return new Vec3(f1 * f2, f3, f * f2);
    }

    @EventTarget
    public void onRender(Render2DEvent event) {
        //region не вносить под for -> хуже перфоманс
        Minecraft minecraft = mc;
        EntityPlayerSP player = minecraft.player;
        float playerYaw = player.rotationYaw;
        WIDTH = 0.03D * (135D / radius.get().doubleValue());

        ScaledResolution resolution = event.getResolution();
        double width = resolution.getScaledWidthStatic(mc);
        double height = resolution.getScaledHeightStatic(mc);
        double centerX = width / 2.0D;
        double centerY = height / 2.0D;


        boolean onlyInvisibles = this.onlyInvisibles.get();
        int radius = this.radius.get();
        ColorProperty color = this.color;

        //endregion

        int counter = 1;

        for (Entity entity : minecraft.world.getLoadedEntityList().stream().sorted(Comparator.comparingDouble(entity ->
                toRadians(((
                        RotationUtil.getYawToPoint(
                                entity.posX, entity.posZ) +
                                TWO_PI_DEGREES_FLOAT) %
                        TWO_PI_DEGREES_FLOAT - playerYaw +
                        TWO_PI_DEGREES_FLOAT) %
                        TWO_PI_DEGREES_FLOAT -
                        HALF_PI_DEGREES_INT)
        )).collect(Collectors.toList())) {
            if (!isValidTarget(entity)) continue;
            if (RenderUtils.isInViewFrustrum(entity) && onlyInvisibles) continue;
            if (targetsOnly.get() && !novoline.getPlayerManager().hasType(entity.getName(), PlayerManager.EnumPlayerType.TARGET))
                continue;

            double distEntity2Player = entity.getDistanceToEntity(player);

            RenderUtils.start2D();
            GL11.glPushMatrix();
            ScaleUtils.scale(mc);
            GL11.glLineWidth(2);

            float alpha = 0;
            switch (alphaType.get()) {
                case "Static":
                    alpha = this.alpha.get() / RGB_MAX_FLOAT;
                    break;
                case "Distance":
                    alpha = 1.0F - clamp_float(color.getAlpha() / RGB_MAX_FLOAT * (float) distEntity2Player * 3.0F, 0, 255) / RGB_MAX_FLOAT;
                    break;
                case "Pulse":
                    alpha = alph / RGB_MAX_FLOAT;
            }

            Color temp = new Color(102, 201, 255, 255);

            switch (colorType.get()) {
                case "Static":
                    temp = color.getAwtColor();
                    break;
                case "Rainbow":
                    temp = new Color(getArrayRainbow(rainbowType.get().equalsIgnoreCase("Static") ? 1 : counter, 255));
                    break;
                case "Team":
                    if (PlayerUtils.inTeam(mc.player, entity)) {
                        temp = new Color(0, 231, 255, 255);
                    } else if (novoline.getPlayerManager().hasType(entity.getName(), PlayerManager.EnumPlayerType.FRIEND)) {
                        temp = new Color(169, 255, 43, 255);
                    } else if (novoline.getPlayerManager().hasType(entity.getName(), PlayerManager.EnumPlayerType.TARGET)) {
                        temp = new Color(255, 59, 5, 255);
                    } else {
                        temp = new Color(255, 255, 255, 255);
                    }
            }

            float red = (temp.getRGB() >> 16 & 0xFF) / RGB_MAX_FLOAT;
            float green = (temp.getRGB() >> 8 & 0xFF) / RGB_MAX_FLOAT;
            float blue = (temp.getRGB() & 0xFF) / RGB_MAX_FLOAT;

            GL11.glColor4f(red, green, blue, alpha);
            GL11.glEnable(GL11.GL_POLYGON_SMOOTH);
            GL11.glBegin(GL11.GL_TRIANGLE_STRIP);

            float yawToEntity = (RotationUtil.getYawToPoint(entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * mc.timer.elapsedPartialTicks, entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * mc.timer.elapsedPartialTicks) + TWO_PI_DEGREES_FLOAT) % TWO_PI_DEGREES_FLOAT;
            float yawDiff = (yawToEntity - playerYaw + TWO_PI_DEGREES_FLOAT) % TWO_PI_DEGREES_FLOAT - HALF_PI_DEGREES_INT;
            double yawDiffRad = toRadians(yawDiff);

            GL11.glVertex2d(centerX + cos(yawDiffRad) * radius, centerY + sin(yawDiffRad) * radius);
            GL11.glVertex2d(centerX + cos(yawDiffRad + WIDTH) * (radius - HEIGHT), centerY + sin(yawDiffRad + WIDTH) * (radius - HEIGHT));
            GL11.glVertex2d(centerX + cos(yawDiffRad - WIDTH) * (radius - HEIGHT), centerY + sin(yawDiffRad - WIDTH) * (radius - HEIGHT));
            GL11.glVertex2d(centerX + cos(yawDiffRad) * radius, centerY + sin(yawDiffRad) * radius);

            GL11.glEnd();
            GL11.glDisable(GL11.GL_POLYGON_SMOOTH);
            GL11.glPopMatrix();
            RenderUtils.stop2D();
            counter++;
        }

        if (!invert && alph < 255) {
            alph++;
        } else {
            invert = true;
        }
        if (invert && alph > 0) {
            alph--;
        } else {
            invert = false;
        }

    }

    //region TODO: переписать чеки на валидность таргета
    @SuppressWarnings({"ChainOfInstanceofChecks", "InstanceofConcreteClass", "MethodWithMultipleReturnPoints", "OverlyComplexMethod"})
    public boolean isValidTarget(Entity target) {
        ListProperty<String> targets = this.targets;
        boolean invisibleTarget = targets.contains("Invisibles");
        EntityPlayerSP player = mc.player;

        if (target.isEntityAlive() && player.getHealth() > 0.0F && !novoline.getPlayerManager().hasType(target.getName(), PlayerManager.EnumPlayerType.FRIEND)) {
            if (target instanceof EntityMob || target instanceof EntitySlime || target instanceof EntityGolem) {
                return targets.contains("Mobs")
                        && !(target.isInvisible() && !invisibleTarget);
            }

            if (target instanceof EntityAnimal || target instanceof EntityVillager) {
                return targets.contains("Animals")
                        && !(target.isInvisible() && !invisibleTarget);
            }

            if (target instanceof EntityPlayer) {
                ListProperty<String> filters = this.filters;

                return targets.contains("Players")
                        && target != player
                        && (!filters.contains("Teams") || !PlayerUtils.inTeam(player, target))
                        && !(target.isInvisible() && !invisibleTarget)
                        && !isAutismShopKeeperCheck(target);
            }
        }

        return false;
    }

    private static boolean hasArmor(EntityPlayer e) {
        ItemStack[] armorInventory = e.inventory.armorInventory;
        return armorInventory[0] != null || armorInventory[1] != null || armorInventory[2] != null || armorInventory[3] != null;
    }

    private boolean isAutismShopKeeperCheck(ICommandSender target) {
        IChatComponent component = target.getDisplayName();

        String formatted = component.getFormattedText();
        String unFormatted = component.getUnformattedText();

        boolean first = !formatted.substring(0, formatted.length() - 2).contains("\u00A7");
        boolean second = formatted.substring(formatted.length() - 2).contains("\u00A7");

        return ServerUtils.isHypixel() && ServerUtils.serverIs(Servers.BW) && first && second;
    }

    public int getArrayRainbow(int counter, int alpha) {
        final int width = 50;

        double rainbowState = Math.ceil(System.currentTimeMillis() - (long) counter * width) / 10;
        rainbowState %= 360;

        final float[] colors = color.getHSB();
        Color color = Color.getHSBColor((float) (rainbowState / 360), colors[1], colors[2]);
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha).getRGB();
    }

    //endregion
}
