package koks.module.player;

import god.buddy.aot.BCompiler;
import koks.api.event.Event;
import koks.api.font.Fonts;
import koks.api.manager.value.annotation.Value;
import koks.api.registry.module.Module;
import koks.api.registry.module.ModuleRegistry;
import koks.api.utils.InventoryUtil;
import koks.api.utils.RenderUtil;
import koks.api.utils.Resolution;
import koks.api.utils.TimeHelper;
import koks.event.*;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@Module.Info(name = "AutoHeal", description = "You heal your self automatically", category = Module.Category.PLAYER)
public class AutoHeal extends Module {

    boolean hasFastUsed, hasSilent;
    final TimeHelper timeHelper = new TimeHelper();
    final TimeHelper fastUseTimeHelper = new TimeHelper();

    @Value(name = "Damage", minimum = 0, maximum = 20)
    int damage = 5;

    @Value(name = "Eat-Delay", minimum = 0, maximum = 10000)
    int eatDelay = 500;

    @Value(name = "FastUse-Delay", minimum = 0, maximum = 10000)
    int fastUseDelay = 50;

    @Value(name = "InAir")
    boolean inAir = false;

    @Value(name = "Souping")
    boolean souping = false;

    @Value(name = "Silent")
    boolean silent = false;

    final List<Item> validItems = new ArrayList<>();
    boolean wasEating, switchedBack;
    int lastSlot;

    @Override
    public boolean isVisible(koks.api.manager.value.Value<?> value, String name) {
        switch (name) {
            case "FastUse-Delay":
                return ModuleRegistry.getModule(FastUse.class).isToggled();
        }
        return super.isVisible(value, name);
    }

    @BCompiler(aot = BCompiler.AOT.AGGRESSIVE)
    @Override
    @Event.Info
    public void onEvent(Event event) {
        if (event instanceof ItemSyncEvent) {
            event.setCanceled(true);
        }
        if (event instanceof final PacketEvent packetEvent) {
            final Packet<?> packet = packetEvent.getPacket();
            if (packet instanceof C09PacketHeldItemChange)
                if (getPlayer().getHealth() < getPlayer().getMaxHealth() - damage && (inAir || getPlayer().onGround)) {
                    event.setCanceled(true);
                }
        }
        if (event instanceof ValueChangeEvent) {
            validItems.clear();
            validItems.add(Items.golden_apple);
            if (souping)
                validItems.add(Items.mushroom_stew);
        }
        if (event instanceof UpdateEvent) {
            if (getPlayer().getHealth() < getPlayer().getMaxHealth() - damage && (inAir || getPlayer().onGround)) {
                final InventoryUtil inventoryUtil = InventoryUtil.getInstance();
                final int slot = inventoryUtil.searchItemSlot(validItems, 0, 9);
                if (slot != -1) {
                    final ItemStack stack = getPlayer().inventory.getStackInSlot(slot);
                    if (!wasEating && (getPlayer().getFakeItem() == null || !(getPlayer().getFakeItem().getItem() instanceof ItemFood))) {
                        if (timeHelper.hasReached(eatDelay)) {
                            lastSlot = getPlayer().inventory.currentItem;
                            if (silent) {
                                sendPacketUnlogged(new C09PacketHeldItemChange(slot));
                                sendPacketUnlogged(new C08PacketPlayerBlockPlacement(stack));
                                getPlayer().setEating(true);
                            } else {
                                getPlayer().inventory.currentItem = slot;
                                sendPacketUnlogged(new C09PacketHeldItemChange(slot));
                                getPlayerController().sendUseItem(getPlayer(), getWorld(), stack);
                            }
                            getPlayer().inventory.fakeItem = slot;
                            wasEating = true;
                            hasFastUsed = false;
                        }
                    } else {
                        final FastUse fastUse = ModuleRegistry.getModule(FastUse.class);
                        if (fastUse.isToggled() && !hasFastUsed) {
                            fastUse.doFastUse(getPlayer().getFakeItem());
                            hasFastUsed = true;
                        }

                        if (silent) {
                            sendPacketUnlogged(new C08PacketPlayerBlockPlacement(stack));
                        } else {
                            getGameSettings().keyBindUseItem.pressed = true;
                        }

                        switchedBack = false;
                        wasEating = false;
                        timeHelper.reset();
                    }
                }
            } else {
                if (!switchedBack) {
                    sendPacketUnlogged(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
                    if (!silent) {
                        getPlayer().inventory.currentItem = lastSlot;
                    }
                    sendPacketUnlogged(new C09PacketHeldItemChange(lastSlot));
                    switchedBack = true;
                    wasEating = false;
                }
            }
        }

        if (event instanceof Render2DEvent) {
            final Resolution sr = Resolution.getResolution();
            final RenderUtil renderUtil = RenderUtil.getInstance();
            final InventoryUtil inventoryUtil = InventoryUtil.getInstance();
            final String food = String.valueOf(inventoryUtil.searchFood(validItems));
            renderUtil.drawRoundedRect(sr.getWidth() / 2f - 20 + 70, sr.getHeight() / 2F + 16, Fonts.arial25.getStringWidth(food), 15, 8, new Color(35, 35, 50));
            GlStateManager.enableRescaleNormal();
            RenderHelper.enableGUIStandardItemLighting();
            RenderHelper.disableStandardItemLighting();
            GlStateManager.disableRescaleNormal();
            Fonts.arial25.drawString(food, sr.getWidth() / 2f - 20 + 70, sr.getHeight() / 2F + 16, Color.white, false);
        }
    }

    @Override
    public void onEnable() {
        hasSilent = false;
        hasFastUsed = false;
        validItems.clear();
        validItems.add(Items.golden_apple);
        if (souping)
            validItems.add(Items.mushroom_stew);

        wasEating = false;
        switchedBack = true;
        lastSlot = getPlayer().inventory.currentItem;
    }

    @Override
    public void onDisable() {
        if (silent) {
            if (getPlayer().inventory.fakeItem != getPlayer().inventory.currentItem)
                sendPacketUnlogged(new C09PacketHeldItemChange(getPlayer().inventory.currentItem));
        } else {
            if (getPlayer().inventory.fakeItem != getPlayer().inventory.currentItem) {
                getPlayer().inventory.currentItem = lastSlot;
                sendPacketUnlogged(new C09PacketHeldItemChange(lastSlot));
            }
        }
    }
}
