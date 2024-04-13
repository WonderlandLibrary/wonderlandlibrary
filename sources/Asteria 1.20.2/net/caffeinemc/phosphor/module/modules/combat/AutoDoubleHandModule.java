package net.caffeinemc.phosphor.module.modules.combat;

import net.caffeinemc.phosphor.api.event.events.PacketEvent;
import net.caffeinemc.phosphor.api.event.events.PlayerTickEvent;
import net.caffeinemc.phosphor.api.event.orbit.EventHandler;
import net.caffeinemc.phosphor.api.rotation.RotationUtils;
import net.caffeinemc.phosphor.api.util.DamageUtils;
import net.caffeinemc.phosphor.api.util.InvUtils;
import net.caffeinemc.phosphor.api.util.PlayerUtils;
import net.caffeinemc.phosphor.module.Module;
import net.caffeinemc.phosphor.module.setting.settings.BooleanSetting;
import net.caffeinemc.phosphor.module.setting.settings.NumberSetting;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.network.packet.s2c.play.EntityStatusS2CPacket;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;

import java.util.List;

public class AutoDoubleHandModule extends Module {
    private final BooleanSetting checkPlayersLook = new BooleanSetting("Check Players Look", this, true);
    private final BooleanSetting predictCrystals = new BooleanSetting("Predict Crystals", this, true);
    private final BooleanSetting predictSword = new BooleanSetting("Predict Sword", this, true);
    private final BooleanSetting doubleHandAfterPop = new BooleanSetting("DHand After Pop", this, true);
    private final NumberSetting delay = new NumberSetting("Delay", this, 0, 0, 10, 1);
    private final NumberSetting cooldown = new NumberSetting("Cooldown", this, 10, 0, 10, 1);

    public AutoDoubleHandModule() {
        super("AutoDoubleHand", "Automatically does double hand", Category.COMBAT);
    }

    private boolean needToDHand;
    private int cooldownClock, clock;

    @Override
    public void onEnable() {
        super.onEnable();
        needToDHand = false;
        cooldownClock = 0;
        clock = 0;
    }

    private boolean willDie(double damage) {
        return mc.player.getHealth() - damage <= 0;
    }

    private boolean arePlayersAimingAtCrystal(EndCrystalEntity crystal) {
        for (PlayerEntity player : mc.world.getPlayers()) {
            if (player == mc.player) continue;

            Vec3d start = player.getEyePos();
            Vec3d end = start.add(RotationUtils.getPlayerLookVec(player));
            Box box = new Box(start, end);
            List<EndCrystalEntity> crystalsInBox = mc.world.getEntitiesByClass(EndCrystalEntity.class, box, endCrystal -> crystal == endCrystal);

            if (crystalsInBox != null || crystalsInBox.isEmpty())
                return true;
        }
        return false;
    }

    private boolean isPlayerAimingAtMe(PlayerEntity player) {
        Vec3d start = player.getEyePos();
        Vec3d end = start.add(RotationUtils.getPlayerLookVec(player));
        Box box = new Box(start, end);
        List<PlayerEntity> playersInBox = mc.world.getEntitiesByClass(PlayerEntity.class, box, (player1) -> player1 == mc.player);

        return playersInBox != null && !playersInBox.isEmpty();
    }

    private boolean arePlayersAimingAtBlock(BlockPos blockPos) {
        for (PlayerEntity player : mc.world.getPlayers()) {
            Vec3d start = player.getEyePos();
            Vec3d end = start.add(RotationUtils.getPlayerLookVec(player));
            BlockHitResult blockHitResult = mc.world.raycast(new RaycastContext(start, end, RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, player));

            if (blockHitResult != null && blockHitResult.getType() == HitResult.Type.BLOCK && blockHitResult.getBlockPos().equals(blockPos))
                return true;
        }
        return false;
    }

    @EventHandler
    private void onPacketReceive(PacketEvent.Receive event) {
        if (!doubleHandAfterPop.isEnabled()) return;

        if (event.packet instanceof EntityStatusS2CPacket packet) {
            if (packet.getStatus() == 35 && packet.getEntity(mc.world) instanceof ClientPlayerEntity) needToDHand = true;
        }
    }

    @EventHandler
    private void onPlayerTick(PlayerTickEvent event) {
        if (mc.currentScreen != null) return;

        if (cooldownClock > 0) {
            cooldownClock--;
            return;
        }

        if (!needToDHand) {
            if (predictCrystals.isEnabled()) {
                List<EndCrystalEntity> crystals = mc.world.getEntitiesByClass(EndCrystalEntity.class, mc.player.getBoundingBox().expand(10), (endCrystal) -> true);

                for (EndCrystalEntity crystal : crystals) {
                    if (checkPlayersLook.isEnabled()) {
                        if (!arePlayersAimingAtCrystal(crystal)) continue;
                    }

                    double damage = DamageUtils.crystalDamage(mc.player, crystal.getPos());
                    if (willDie(damage)) {
                        needToDHand = true;
                        break;
                    }
                }
            }
        }

        if (!needToDHand) {
            if (predictSword.isEnabled()) {
                List<PlayerEntity> players = mc.world.getEntitiesByClass(PlayerEntity.class, mc.player.getBoundingBox().expand(5), (player) -> isPlayerAimingAtMe(player) && !(player instanceof ClientPlayerEntity));

                for (PlayerEntity player : players) {
                    double damage = DamageUtils.getSwordDamage(player, player.getAttackCooldownProgress(0f) >= 1f);
                    if (willDie(damage)) {
                        needToDHand = true;
                        break;
                    }
                }
            }
        }

        if (needToDHand) {
            if (!mc.player.getMainHandStack().isOf(Items.TOTEM_OF_UNDYING)) {
                if (clock < delay.getIValue()) {
                    clock++;
                    return;
                }

                InvUtils.selectItemFromHotbar(Items.TOTEM_OF_UNDYING);
            }

            needToDHand = false;
            cooldownClock = cooldown.getIValue();
            clock = 0;
        }
    }
}
