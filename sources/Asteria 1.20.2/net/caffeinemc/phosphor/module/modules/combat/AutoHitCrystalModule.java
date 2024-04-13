package net.caffeinemc.phosphor.module.modules.combat;

import net.caffeinemc.phosphor.api.event.events.PlayerTickEvent;
import net.caffeinemc.phosphor.api.event.orbit.EventHandler;
import net.caffeinemc.phosphor.api.util.BlockUtils;
import net.caffeinemc.phosphor.api.util.InvUtils;
import net.caffeinemc.phosphor.api.util.KeyUtils;
import net.caffeinemc.phosphor.common.Phosphor;
import net.caffeinemc.phosphor.module.Module;
import net.caffeinemc.phosphor.module.setting.settings.BooleanSetting;
import net.caffeinemc.phosphor.module.setting.settings.KeybindSetting;
import net.caffeinemc.phosphor.module.setting.settings.NumberSetting;
import net.minecraft.block.Blocks;
import net.minecraft.block.SignBlock;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.mob.SlimeEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.SwordItem;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import org.lwjgl.glfw.GLFW;

public class AutoHitCrystalModule extends Module {
    private final BooleanSetting clickSimulation = new BooleanSetting("Click Simulation", this, true);
    private final BooleanSetting workWithTotem = new BooleanSetting("Work With Totem", this, true);
    private final NumberSetting placeDelay = new NumberSetting("Obsidian Place Delay", this, 0d, 0d, 10d, 1d);
    private final NumberSetting switchDelay = new NumberSetting("Switch Delay", this, 0d, 0d, 10d, 1d);
    private final KeybindSetting activateKey = new KeybindSetting("Activate Key", GLFW.GLFW_MOUSE_BUTTON_2, this);

    public AutoHitCrystalModule() {
        super("AutoHitCrystal", "Automatically hit crystals.", 0, Category.COMBAT);
    }

    private int placeClock = 0;
    private int switchClock = 0;
    private boolean activated;
    private boolean crystalling;
    private boolean selectedCrystal;

    public void reset() {
        placeClock = placeDelay.getIValue();
        switchClock = switchDelay.getIValue();
        activated = false;
        crystalling = false;
        selectedCrystal = false;
    }

    @Override
    public void onEnable() {
        reset();
    }

    @EventHandler
    private void onPlayerTick(PlayerTickEvent event) {
        if (mc.currentScreen != null) return;

        if (KeyUtils.isKeyPressed(activateKey.getKeyCode())) {
            ItemStack mainHand = mc.player.getMainHandStack();

            if (activateKey.getKeyCode() == GLFW.GLFW_MOUSE_BUTTON_RIGHT) {
                if (!(mainHand.getItem() instanceof SwordItem || (workWithTotem.isEnabled() && mainHand.isOf(Items.TOTEM_OF_UNDYING))) && !activated)
                    return;

                activated = true;
            }

            if (!crystalling) {
                if (mc.crosshairTarget instanceof BlockHitResult blockHit) {
                    if (blockHit.getType() == HitResult.Type.MISS)
                        return;

                    if (BlockUtils.getBlockState(blockHit.getBlockPos()).getBlock() instanceof SignBlock)
                        return;

                    if (!BlockUtils.isBlock(Blocks.OBSIDIAN, blockHit.getBlockPos())) {
                        mc.options.useKey.setPressed(false);

                        if (!mainHand.isOf(Items.OBSIDIAN)) {
                            if (switchClock > 0) {
                                switchClock--;
                                return;
                            }

                            InvUtils.selectItemFromHotbar(Items.OBSIDIAN);

                            switchClock = switchDelay.getIValue();
                        }

                        if (placeClock > 0) {
                            placeClock--;
                            return;
                        }

                        if (clickSimulation.isEnabled()) Phosphor.mouseSimulation().mouseClick(GLFW.GLFW_MOUSE_BUTTON_RIGHT);

                        ActionResult interactionResult = mc.interactionManager.interactBlock(mc.player, Hand.MAIN_HAND, blockHit);
                        if (interactionResult.isAccepted() && interactionResult.shouldSwingHand()) {
                            mc.player.swingHand(Hand.MAIN_HAND);
                        }

                        placeClock = placeDelay.getIValue();
                        crystalling = true;
                    }
                }
            }

            if (crystalling ||
                    ((mc.crosshairTarget instanceof BlockHitResult blockHit && BlockUtils.isBlock(Blocks.OBSIDIAN, blockHit.getBlockPos()))
                            || mc.crosshairTarget instanceof EntityHitResult entityHit && (entityHit.getEntity() instanceof EndCrystalEntity || entityHit.getEntity() instanceof SlimeEntity))) {
                crystalling = true;

                if (!mc.player.getMainHandStack().isOf(Items.END_CRYSTAL) && !selectedCrystal) {
                    if (switchClock > 0) {
                        switchClock--;
                        return;
                    }

                    selectedCrystal = InvUtils.selectItemFromHotbar(Items.END_CRYSTAL);

                    switchClock = switchDelay.getIValue();
                }

                AutoCrystalModule autoCrystal = Phosphor.moduleManager().getModule(AutoCrystalModule.class);

                if (!autoCrystal.isEnabled())
                    autoCrystal.onPlayerTick(event);
            }
        } else {
            reset();
        }
    }
}
