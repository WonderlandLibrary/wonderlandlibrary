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
import net.minecraft.item.BucketItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Direction;
import org.lwjgl.glfw.GLFW;

public class WebMacroModule extends Module {
    private final BooleanSetting clickSimulation = new BooleanSetting("Click Simulation", this, true);
    private final BooleanSetting goToPrevSlot = new BooleanSetting("Go To Prev Slot", this, true);
    private final BooleanSetting placeTNT = new BooleanSetting("Place TNT/Creeper on Web", this, false);
    private final BooleanSetting placeLava = new BooleanSetting("Place Lava on Web", this, false);
    private final NumberSetting placeDelay = new NumberSetting("Place Delay", this, 0d, 0d, 10d, 1d);
    private final NumberSetting switchDelay = new NumberSetting("Switch Delay", this, 0d, 0d, 10d, 1d);
    private final KeybindSetting activateKey = new KeybindSetting("Activate Key", -1, this);

    public WebMacroModule() {
        super("WebMacro", "Automatically places web.", 0, Category.COMBAT);
    }

    private int prevSlot;
    private int placeClock, switchClock;
    private boolean selectedWeb, placedWeb;
    private boolean selectedTnt, placedTnt;
    private boolean selectedLava, placedLava;

    public void reset() {
        prevSlot = 0;

        placeClock = 0;
        switchClock = 0;

        selectedWeb = false;
        placedWeb = false;

        selectedTnt = false;
        placedTnt = false;

        selectedLava = false;
        placedLava = false;
    }

    @Override
    public void onEnable() {
        reset();
    }

    private void setPrevSlot() {
        if (switchClock < switchDelay.getIValue()) {
            switchClock++;
            return;
        }

        InvUtils.setInvSlot(prevSlot);

        switchClock = 0;
    }

    private int getTNTSlot() {
        int tntSlot = InvUtils.getItemSlot(Items.TNT);
        if (tntSlot != -1) return tntSlot;

        return InvUtils.getItemSlot(Items.CREEPER_SPAWN_EGG);
    }

    private boolean checkStack(ItemStack handStack) {
        return handStack.isOf(Items.TNT) || handStack.isOf(Items.CREEPER_SPAWN_EGG);
    }

    @EventHandler
    private void onPlayerTick(PlayerTickEvent event) {
        if (mc.currentScreen != null) return;

        if (KeyUtils.isKeyPressed(activateKey.getKeyCode())) {
            if (mc.crosshairTarget instanceof BlockHitResult blockHit && blockHit.getType() != HitResult.Type.MISS) {
                if (!BlockUtils.isBlock(Blocks.COBWEB, blockHit.getBlockPos())) {
                    int webSlot = InvUtils.getItemSlot(Items.COBWEB);

                    if (!mc.player.getMainHandStack().isOf(Items.COBWEB) && !selectedWeb) {
                        if (webSlot == -1) return;

                        if (switchClock < switchDelay.getIValue()) {
                            switchClock++;
                            return;
                        }

                        prevSlot = mc.player.getInventory().selectedSlot;
                        InvUtils.setInvSlot(webSlot);

                        selectedWeb = true;
                        switchClock = 0;
                    }
                    if (mc.player.getMainHandStack().isOf(Items.COBWEB)) {
                        if (placeClock < placeDelay.getIValue()) {
                            placeClock++;
                            return;
                        }

                        if (clickSimulation.isEnabled()) Phosphor.mouseSimulation().mouseClick(GLFW.GLFW_KEY_RIGHT);

                        ActionResult interactionResult = mc.interactionManager.interactBlock(mc.player, Hand.MAIN_HAND, blockHit);
                        if (interactionResult.isAccepted() && interactionResult.shouldSwingHand()) {
                            mc.player.swingHand(Hand.MAIN_HAND);
                        }

                        placedWeb = true;
                        placeClock = 0;
                    }
                    if ((!placeTNT.isEnabled() || getTNTSlot() == -1) && goToPrevSlot.isEnabled() && mc.player.getInventory().selectedSlot == webSlot && placedWeb) {
                        setPrevSlot();
                    }
                }
                if (BlockUtils.isBlock(Blocks.COBWEB, blockHit.getBlockPos()) && placedWeb) {
                    if (placeTNT.isEnabled()) {
                        int tntSlot = getTNTSlot();

                        if (!checkStack(mc.player.getMainHandStack()) && !selectedTnt) {
                            if (tntSlot == -1) return;

                            if (switchClock < switchDelay.getIValue()) {
                                switchClock++;
                                return;
                            }

                            InvUtils.setInvSlot(tntSlot);

                            selectedTnt = true;
                            switchClock = 0;
                        }
                        if (checkStack(mc.player.getMainHandStack())) {
                            if (placeClock < placeDelay.getIValue()) {
                                placeClock++;
                                return;
                            }

                            if (clickSimulation.isEnabled()) Phosphor.mouseSimulation().mouseClick(GLFW.GLFW_MOUSE_BUTTON_RIGHT);

                            ActionResult interactionResult = mc.interactionManager.interactBlock(mc.player, Hand.MAIN_HAND, blockHit.withSide(Direction.UP));
                            if (interactionResult.isAccepted() && interactionResult.shouldSwingHand()) {
                                mc.player.swingHand(Hand.MAIN_HAND);
                            }

                            placedTnt = true;
                            placeClock = 0;
                        }
                        if (placedTnt && goToPrevSlot.isEnabled() && mc.player.getInventory().selectedSlot == tntSlot) {
                            setPrevSlot();
                        }
                    }
                    if (placeLava.isEnabled() && !placedTnt) {
                        if (blockHit.getSide() != Direction.UP) return;

                        int lavaSlot = InvUtils.getItemSlot(Items.LAVA_BUCKET);

                        if (!mc.player.getMainHandStack().isOf(Items.LAVA_BUCKET)) {
                            if (lavaSlot == -1) return;

                            if (switchClock < switchDelay.getIValue()) {
                                switchClock++;
                                return;
                            }

                            InvUtils.setInvSlot(lavaSlot);

                            selectedLava = true;
                            switchClock = 0;
                        }
                        if (mc.player.getMainHandStack().isOf(Items.LAVA_BUCKET)) {
                            if (placeClock < placeDelay.getIValue()) {
                                placeClock++;
                                return;
                            }

                            if (clickSimulation.isEnabled()) Phosphor.mouseSimulation().mouseClick(GLFW.GLFW_MOUSE_BUTTON_RIGHT);

                            ActionResult interactionResult = mc.interactionManager.interactItem(mc.player, Hand.MAIN_HAND);
                            if (interactionResult.isAccepted() && interactionResult.shouldSwingHand()) {
                                mc.player.swingHand(Hand.MAIN_HAND);
                            }

                            placedLava = true;
                            placeClock = 0;
                        }
                        if (placedLava && goToPrevSlot.isEnabled() && mc.player.getInventory().selectedSlot == lavaSlot) {
                            setPrevSlot();
                        }
                    }
                }
            }
        } else {
            reset();
        }
    }
}
