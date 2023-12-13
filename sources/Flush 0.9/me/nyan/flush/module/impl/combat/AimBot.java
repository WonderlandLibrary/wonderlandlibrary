package me.nyan.flush.module.impl.combat;

import me.nyan.flush.event.SubscribeEvent;
import me.nyan.flush.event.impl.EventMotion;
import me.nyan.flush.module.Module;
import me.nyan.flush.module.settings.BooleanSetting;
import me.nyan.flush.module.settings.NumberSetting;
import me.nyan.flush.utils.combat.CombatUtils;
import me.nyan.flush.utils.player.PlayerUtils;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.entity.EntityLivingBase;
import org.lwjgl.input.Mouse;

public class AimBot extends Module {
    private final BooleanSetting instant = new BooleanSetting("Instant", this, false),
            verticalAim = new BooleanSetting("Vertical Aim", this, false);
    private final NumberSetting reach = new NumberSetting("Reach", this, 4.2, 3, 7, 0.05),
            yawSpeed = new NumberSetting("Yaw Speed", this, 5, 1, 10,
                    () -> !instant.getValue()),
            pitchSpeed = new NumberSetting("Pitch Speed", this, 3, 1, 10,
                    () -> !instant.getValue() && verticalAim.getValue());
    private final BooleanSetting holdLeftClick = new BooleanSetting("Hold Left Click", this, true),
            weaponOnly = new BooleanSetting("Weapon Only", this, false),
            throughWalls = new BooleanSetting("Through Walls", this, false),
            randomize = new BooleanSetting("Randomize", this, false),
            players = new BooleanSetting("Players", this, true),
            creatures = new BooleanSetting("Creatures", this, false),
            villagers = new BooleanSetting("Villagers", this, false),
            invisibles = new BooleanSetting("Invisibles", this, false),
            ignoreTeam = new BooleanSetting("Ignore Team", this, false);

    public AimBot() {
        super("AimBot", Category.COMBAT);
    }

    @SubscribeEvent
    public void onMotion(EventMotion e) {
        if ((weaponOnly.getValue() && !PlayerUtils.isHoldingWeapon()) ||
                (holdLeftClick.getValue() && !Mouse.isButtonDown(0)) || mc.currentScreen != null)
            return;

        EntityLivingBase target = (EntityLivingBase) mc.theWorld.loadedEntityList.stream().filter(entity -> entity instanceof
                EntityLivingBase && isValid((EntityLivingBase) entity)).findFirst().orElse(null);
        float[] rotations = CombatUtils.getRotations(target, randomize.getValue());

        if (!instant.getValue()) {
            float rotationYaw = mc.thePlayer.rotationYaw;
            float rotationPitch = mc.thePlayer.rotationPitch;
            float targetYaw = rotations[0];
            float targetPitch = rotations[1];

            if (CombatUtils.rayCast(reach.getValueFloat(), rotationYaw, rotationPitch) == target) {
                return;
            }

            rotationYaw += CombatUtils.getFacingDifference(targetYaw, rotationYaw) * (yawSpeed.getValueFloat() / 100F);

            if (verticalAim.getValue()) {
                rotationPitch += CombatUtils.getFacingDifference(targetPitch, rotationPitch) * (pitchSpeed.getValueFloat() / 100F);
            }

            mc.thePlayer.rotationYaw = rotationYaw;
            mc.thePlayer.rotationPitch = rotationPitch;
            return;
        }

        mc.thePlayer.rotationYaw = rotations[0];

        if (verticalAim.getValue()) {
            mc.thePlayer.rotationPitch = rotations[1];
        }
    }

    public boolean isValid(EntityLivingBase entity) {
        if (entity instanceof AbstractClientPlayer && !mc.isIntegratedServerRunning() &&
                mc.getCurrentServerData().serverIP.toLowerCase().contains("funcraft.net") &&
                ((((AbstractClientPlayer) entity).getLocationSkin().getResourcePath()
                        .equalsIgnoreCase("textures/entity/alex.png") || ((AbstractClientPlayer) entity).getLocationSkin()
                        .getResourcePath().equalsIgnoreCase("textures/entity/steve.png")) && entity.ticksExisted < 30)) {
            return false;
        }

        return PlayerUtils.isValid(entity, players.getValue(), creatures.getValue(), villagers.getValue(), invisibles.getValue(),
                                   ignoreTeam.getValue()) && entity.getDistanceToEntity(mc.thePlayer) <= reach.getValue() &&
                (throughWalls.getValue() || mc.thePlayer.canEntityBeSeen(entity));
    }
}
