package net.caffeinemc.phosphor.module.modules.combat;

import net.caffeinemc.phosphor.api.event.events.PlayerTickEvent;
import net.caffeinemc.phosphor.api.event.orbit.EventHandler;
import net.caffeinemc.phosphor.api.rotation.RotationUtils;
import net.caffeinemc.phosphor.api.util.KeyUtils;
import net.caffeinemc.phosphor.common.Phosphor;
import net.caffeinemc.phosphor.module.Module;
import net.caffeinemc.phosphor.module.setting.settings.BooleanSetting;
import net.caffeinemc.phosphor.module.setting.settings.NumberSetting;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.mob.SlimeEntity;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import org.lwjgl.glfw.GLFW;

import static net.caffeinemc.phosphor.api.util.CrystalUtils.canPlaceCrystalServer;
import static net.caffeinemc.phosphor.api.util.CrystalUtils.isCrystalBroken;

public class AutoCrystalModule extends Module {
    private final BooleanSetting clickSimulation = new BooleanSetting("Click Simulation", this, true);
    public final BooleanSetting onRmb = new BooleanSetting("On RMB", this, true);
    public final BooleanSetting noCountGlitch = new BooleanSetting("No Count Glitch", this, true);
    public final BooleanSetting noBounce = new BooleanSetting("No Bounce", this, true);
    public final NumberSetting placeDelay = new NumberSetting("Place Delay", this, 0, 0, 10, 1);
    public final NumberSetting breakDelay = new NumberSetting("Break Delay", this, 0, 0, 10, 1);
    public final BooleanSetting fastMode = new BooleanSetting("Fast Mode", this, true);

    public AutoCrystalModule() {
        super("AutoCrystal", "Automatically crystalling", Category.COMBAT);
    }

    private int tickTimer;

    public boolean passedTicks(double time) {
        return tickTimer >= time;
    }

    public void reset() {
        tickTimer = 0;
    }

    public void placeCrystal() {
        if (passedTicks(placeDelay.getIValue())) {
            if (mc.player.getMainHandStack().isOf(Items.END_CRYSTAL)) {
                if (mc.crosshairTarget instanceof BlockHitResult blockHit && blockHit.getType() == HitResult.Type.BLOCK) {
                    if (canPlaceCrystalServer(blockHit.getBlockPos())) {
                        if (clickSimulation.isEnabled()) Phosphor.mouseSimulation().mouseClick(GLFW.GLFW_MOUSE_BUTTON_RIGHT);

                        ActionResult result = mc.interactionManager.interactBlock(mc.player, Hand.MAIN_HAND, blockHit);
                        if (result.isAccepted() && result.shouldSwingHand())
                            mc.player.swingHand(Hand.MAIN_HAND);

                        reset();
                    }
                } else if (mc.crosshairTarget instanceof EntityHitResult entityHit) {
                    if (fastMode.isEnabled()) {
                        if (entityHit.getEntity() instanceof SlimeEntity) {
                            HitResult hitResult = RotationUtils.getHitResult(mc.player, true);

                            if (hitResult instanceof EntityHitResult entityHitNoInvisibles && entityHitNoInvisibles.getEntity() instanceof EndCrystalEntity crystal) {
                                fastPlace(crystal);
                            }
                        } else if (entityHit.getEntity() instanceof EndCrystalEntity crystal) {
                            fastPlace(crystal);
                        }
                    }
                }
            }
        }
    }

    public void fastPlace(EndCrystalEntity crystal) {
        if (isCrystalBroken(crystal)) {
            double reach = mc.interactionManager.getReachDistance();
            Vec3d cameraPosVec = mc.player.getCameraPosVec(mc.getTickDelta());
            Vec3d rotationVec = RotationUtils.getPlayerLookVec(mc.player);
            Vec3d range = cameraPosVec.add(rotationVec.x * reach, rotationVec.y * reach, rotationVec.z * reach);

            BlockHitResult blockHit = mc.world.raycast(new RaycastContext(cameraPosVec, range, RaycastContext.ShapeType.OUTLINE, RaycastContext.FluidHandling.NONE, mc.player));
            BlockState blockState = mc.world.getBlockState(blockHit.getBlockPos());

            if (blockState.isOf(Blocks.OBSIDIAN) || blockState.isOf(Blocks.BEDROCK)) {
                if (clickSimulation.isEnabled()) Phosphor.mouseSimulation().mouseClick(GLFW.GLFW_MOUSE_BUTTON_RIGHT);

                ActionResult result = mc.interactionManager.interactBlock(mc.player, Hand.MAIN_HAND, blockHit);
                if (result.isAccepted() && result.shouldSwingHand())
                    mc.player.swingHand(Hand.MAIN_HAND);

                reset();
            }
        }
    }

    public void breakCrystal() {
        if (passedTicks(breakDelay.getIValue())) {
            if (mc.player.getMainHandStack().isOf(Items.END_CRYSTAL)) {
                if (mc.crosshairTarget instanceof EntityHitResult hit) {
                    if (hit.getEntity() instanceof EndCrystalEntity || hit.getEntity() instanceof SlimeEntity) {
                        if (isCrystalBroken(hit.getEntity()))
                            return;

                        if (clickSimulation.isEnabled()) Phosphor.mouseSimulation().mouseClick(GLFW.GLFW_MOUSE_BUTTON_LEFT);

                        mc.interactionManager.attackEntity(mc.player, hit.getEntity());
                        mc.player.swingHand(Hand.MAIN_HAND);

                        reset();
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlayerTick(PlayerTickEvent event) {
        if (!KeyUtils.isKeyPressed(GLFW.GLFW_MOUSE_BUTTON_RIGHT) && onRmb.isEnabled())
            return;

        if (mc.currentScreen != null)
            return;

        placeCrystal();
        breakCrystal();

        ++tickTimer;
    }
}
