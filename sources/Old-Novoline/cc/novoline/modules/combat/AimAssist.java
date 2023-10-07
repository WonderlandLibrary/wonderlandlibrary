package cc.novoline.modules.combat;

import cc.novoline.events.EventTarget;
import cc.novoline.events.events.MotionUpdateEvent;
import cc.novoline.gui.screen.setting.Setting;
import cc.novoline.gui.screen.setting.SettingType;
import cc.novoline.modules.AbstractModule;
import cc.novoline.modules.EnumModuleType;
import cc.novoline.modules.ModuleManager;
import cc.novoline.modules.PlayerManager;
import cc.novoline.modules.configurations.annotation.Property;
import cc.novoline.modules.configurations.property.object.BooleanProperty;
import cc.novoline.modules.configurations.property.object.DoubleProperty;
import cc.novoline.utils.RotationUtil;
import cc.novoline.utils.Timer;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemSword;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static cc.novoline.gui.screen.setting.Manager.put;
import static cc.novoline.modules.configurations.property.object.PropertyFactory.booleanFalse;
import static cc.novoline.modules.configurations.property.object.PropertyFactory.createDouble;

public final class AimAssist extends AbstractModule {

    /* fields */
    private static float yaw;
    private static float pitch;
    private static Entity target;
    private static List<Entity> targets = new ObjectArrayList<>(0);

    private final Timer timer = new Timer();
    private final Comparator<Entity> angleComparator = Comparator.comparingDouble(entity -> getRotationsNeeded(entity)[0]);
    private int index;

    /* properties @off */
    @Property("reach")
    private final DoubleProperty reach = createDouble(3.0D).minimum(1.0D).maximum(6.0D);
    @Property("aim-delay")
    private final DoubleProperty aimDelay = createDouble(0.0D).minimum(0.0D).maximum(1000.0D);
    @Property("height")
    private final DoubleProperty height = createDouble(3.5D).minimum(-8.0D).maximum(8.0D);
    @Property("horizontal-left")
    private final DoubleProperty horizontalLeft = createDouble(1.0D).minimum(0.0D).maximum(5.0D);
    @Property("horizontal-right")
    private final DoubleProperty horizontalRight = createDouble(1.0D).minimum(0.0D).maximum(5.0D);
    @Property("vertical-up")
    private final DoubleProperty verticalUp = createDouble(1.0D).minimum(0.0D).maximum(5.0D);
    @Property("vertical-down")
    private final DoubleProperty verticalDown = createDouble(1.0D).minimum(0.0D).maximum(5.0D);
    @Property("only-axe-sword")
    private final BooleanProperty onlyAxeSword = booleanFalse();
    @Property("ray-trace")
    private final BooleanProperty rayTrace = booleanFalse();
    @Property("click-aim")
    private final BooleanProperty clickAim = booleanFalse();

    /* constructors @on */
    public AimAssist(@NonNull ModuleManager moduleManager) {
        super(moduleManager, "AimAssist", "Aim Assist", Keyboard.KEY_NONE, EnumModuleType.COMBAT, "Helps you to aim");
        put(new Setting("REACH", "Reach", SettingType.SLIDER, this, this.reach, 0.1));
        put(new Setting("AIM_DELAY", "Aim Delay", SettingType.SLIDER, this, this.aimDelay, 50));
        put(new Setting("HEIGHT", "Height", SettingType.SLIDER, this, this.height, 1));
        put(new Setting("H_LEFT", "Horizontal Left", SettingType.SLIDER, this, this.horizontalLeft, 0.1));
        put(new Setting("H_RIGHT", "Horizontal Right", SettingType.SLIDER, this, this.horizontalRight, 0.1));
        put(new Setting("V_UP", "Vertical Up", SettingType.SLIDER, this, this.verticalUp, 0.1));
        put(new Setting("V_DOWN", "Vertical Down", SettingType.SLIDER, this, this.verticalDown, 0.1));
        put(new Setting("ONLY_AXESWORD", "Only Sword/Axe", SettingType.CHECKBOX, this, this.onlyAxeSword));
        put(new Setting("RAYTRACE", "Raytrace", SettingType.CHECKBOX, this, this.rayTrace));
        put(new Setting("CLICKAIM", "Click Aim", SettingType.CHECKBOX, this, this.clickAim));
    }


    private List<Entity> loadTargets() {
        return this.mc.world.getLoadedEntityList().stream()
                .filter(e -> this.mc.player.getDistanceToEntity(e) <= this.reach.get() && this.qualifies(e))
                .sorted(Comparator.comparingDouble(o -> ((Entity) o).getDistanceToEntity(this.mc.player)).reversed())
                .collect(Collectors.toCollection(ObjectArrayList::new));
    }

    private boolean qualifies(Entity entity) {
        return entity != this.mc.player && entity.isEntityAlive() && !this.novoline.getPlayerManager()
                .hasType(entity.getName(), PlayerManager.EnumPlayerType.FRIEND) && entity instanceof EntityPlayer;
    }

    @EventTarget
    private void onPreUpdate(MotionUpdateEvent event) {
        if(event.getState().equals(MotionUpdateEvent.State.PRE)) {
            targets = this.loadTargets();
            targets.sort(this.angleComparator);

            if (target instanceof EntityPlayer || target instanceof EntityMob || target instanceof EntityAnimal) {
                target = null;
            }

            if (this.mc.player.ticksExisted % 50 == 0 && targets.size() > 1) ++this.index;
            if (this.clickAim.get() && !Mouse.isButtonDown(0)) return;

            if (!targets.isEmpty()) {
                if (this.index >= targets.size()) this.index = 0;

                target = targets.get(this.index);
                final double[] rotationPosition = getRotationsNeeded(target);

                if (this.timer.delay(this.aimDelay.get())) {
                    pitch = (float) (rotationPosition[1] + this.height.get());
                    yaw = (float) rotationPosition[0];

                    this.timer.reset();
                }

                final boolean notOnlyAxeSword = !this.onlyAxeSword.get();

                if (this.rayTrace.get()) {
                    if (this.mc.player.canEntityBeSeen(target) && (notOnlyAxeSword || this.mc.player
                            .getCurrentEquippedItem().getItem() instanceof ItemSword || this.mc.player
                            .getCurrentEquippedItem().getItem() instanceof ItemAxe)) {
                        aim();
                    }
                } else if (notOnlyAxeSword) {
                    aim();
                } else if (this.mc.player.getCurrentEquippedItem().getItem() instanceof ItemSword || this.mc.player
                        .getCurrentEquippedItem().getItem() instanceof ItemAxe) {
                    aim();
                }
            }
        }
    }

    private void aim() {
        final EntityPlayerSP player = this.mc.player;

        final float yaw = player.rotationYaw, // @off
                pitch = player.rotationPitch; // @on

        if (yaw < AimAssist.yaw) {
            player.rotationYaw += this.horizontalRight.get();
        } else if (player.rotationYaw > AimAssist.yaw) {
            player.rotationYaw -= this.horizontalLeft.get();
        }

        if (pitch < AimAssist.pitch) {
            player.rotationPitch += this.verticalDown.get();
        } else if (player.rotationPitch > AimAssist.pitch) {
            player.rotationPitch -= this.verticalUp.get();
        }
    }

    public static double[] getRotationsNeeded(Entity entity) {
        if (entity == null) return null;

        final EntityPlayerSP player = Minecraft.getInstance().player;
        final double diffX = entity.posX - player.posX, // @off
                diffY = ((EntityLivingBase) entity).posY + entity.getEyeHeight() * 0.9 - (player.posY + player.getEyeHeight()),
                diffZ = entity.posZ - player.posZ; // @on

        return RotationUtil.getDistance(diffX, diffZ, diffY);
    }

}
