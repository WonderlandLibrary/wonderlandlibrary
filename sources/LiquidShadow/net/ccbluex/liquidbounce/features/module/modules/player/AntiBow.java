package net.ccbluex.liquidbounce.features.module.modules.player;

import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.modules.world.Scaffold;
import net.ccbluex.liquidbounce.utils.RotationUtils;
import net.ccbluex.liquidbounce.utils.VecRotation;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.EntityArrow;

import java.util.ArrayList;
import java.util.List;

@ModuleInfo(name = "AntiBow",description = "Let the arrow cant hit you.",category = ModuleCategory.PLAYER)
public class AntiBow extends Module {
    private final FloatValue distanceValue = new FloatValue("Distance",40,10,60);
    private final IntegerValue moveTicks = new IntegerValue("MoveTicks",40,1,100);
    private final BoolValue autoScaffoldValue = new BoolValue("AutoScaffold",true);

    private final List<Integer> hidedArrows = new ArrayList<>();
    private final List<Integer> ignoredArrows = new ArrayList<>();
    private int ticks;

    @EventTarget
    public void onUpdate(UpdateEvent updateEvent) {
        for (Entity entity : mc.theWorld.loadedEntityList) {
            if (entity instanceof EntityArrow && entity.getDistanceToEntity(mc.thePlayer) < 5 && !ignoredArrows.contains(entity.getEntityId())) {
                ignoredArrows.add(entity.getEntityId());
            }
            if (entity instanceof EntityArrow && entity.getDistanceToEntity(mc.thePlayer) < distanceValue.get()) {
                if (!hidedArrows.contains(entity.getEntityId()) && !ignoredArrows.contains(entity.getEntityId())) {
                    hidedArrows.add(entity.getEntityId());
                    VecRotation rotation = RotationUtils.searchCenter(
                            entity.getEntityBoundingBox(),
                            false,
                            false,
                            false,
                            false,
                            entity.getDistanceToEntity(mc.thePlayer));
                    mc.thePlayer.rotationYaw = rotation.getRotation().getYaw();
                    mc.thePlayer.rotationPitch = rotation.getRotation().getPitch();
                    if (autoScaffoldValue.get()) {
                        LiquidBounce.moduleManager.getModule(Scaffold.class).setState(true);
                    }
                    EntityArrow arrow = ((EntityArrow) entity);

                    ticks = 0;
                }
                if (ticks < moveTicks.get() && !ignoredArrows.contains(entity.getEntityId())) {
                    mc.gameSettings.keyBindLeft.pressed = true;
                }
            }
        }
        ticks++;
    }
}
