package io.github.nevalackin.client.module.misc.inventory;

import io.github.nevalackin.client.module.Category;
import io.github.nevalackin.client.module.Module;
import io.github.nevalackin.client.core.KetamineClient;
import io.github.nevalackin.client.event.player.UpdatePositionEvent;
import io.github.nevalackin.client.module.combat.rage.Aura;
import io.github.nevalackin.client.property.BooleanProperty;
import io.github.nevalackin.client.util.player.InventoryUtil;
import io.github.nevalackin.homoBus.Listener;
import io.github.nevalackin.homoBus.annotations.EventLink;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;

public final class AutoTool extends Module {

    private Aura aura;

    private boolean switched;
    private int previousSlot;

    private final BooleanProperty autoWeaponProperty = new BooleanProperty("Auto Weapon", true);
    private final BooleanProperty switchBackProperty = new BooleanProperty("Switch back", true);

    public AutoTool() {
        super("Auto Tool", Category.MISC, Category.SubCategory.MISC_INVENTORY);

        this.register(this.autoWeaponProperty, this.switchBackProperty);
    }

    @EventLink(3)
    private final Listener<UpdatePositionEvent> onUpdatePosition = event -> {
        if (event.isPre()) {
            if (this.switchBackProperty.getValue() && this.switched && this.previousSlot != -1) {
                this.mc.thePlayer.inventory.currentItem = previousSlot;
                this.previousSlot = -1;
                this.switched = false;
            }

            if (this.autoWeaponProperty.getValue() && this.aura.getTarget() != null || (this.isPointedEntity() && this.mc.gameSettings.keyBindAttack.isKeyDown())) {
                double bestDamage = 1;
                int bestWeaponSlot = -1;

                for (int i = InventoryUtil.ONLY_HOT_BAR_BEGIN; i < InventoryUtil.END; i++) {
                    final ItemStack stack = this.mc.thePlayer.inventoryContainer.getSlot(i).getStack();

                    if (stack != null) {
                        final double damage = InventoryUtil.getItemDamage(stack);

                        if (damage > bestDamage) {
                            bestDamage = damage;
                            bestWeaponSlot = i;
                        }
                    }
                }

                if (bestWeaponSlot != -1) {
                    this.mc.thePlayer.inventory.currentItem = bestWeaponSlot - 36;
                    this.previousSlot = this.mc.thePlayer.inventory.currentItem;
                    this.switched = false;
                }
            } else if (this.isPointedBlock() && this.mc.gameSettings.keyBindAttack.isKeyDown()) {
                final BlockPos blockPos = this.mc.objectMouseOver.getBlockPos();

                final Block block = this.mc.theWorld.getBlockState(blockPos).getBlock();

                double bestToolEfficiency = 1;
                int bestToolSlot = -1;

                for (int i = InventoryUtil.ONLY_HOT_BAR_BEGIN; i < InventoryUtil.END; i++) {
                    final ItemStack stack = this.mc.thePlayer.inventoryContainer.getSlot(i).getStack();

                    if (stack != null && stack.getItem() instanceof ItemTool) {
                        final ItemTool tool = (ItemTool) stack.getItem();

                        final double eff = tool.getStrVsBlock(stack, block);

                        if (eff > bestToolEfficiency) {
                            bestToolEfficiency = eff;
                            bestToolSlot = i;
                        }
                    }
                }

                if (bestToolSlot != -1) {
                    this.previousSlot = this.mc.thePlayer.inventory.currentItem;
                    this.mc.thePlayer.inventory.currentItem = bestToolSlot - 36;
                    this.switched = true;
                }
            }
        }
    };

    private boolean isPointedEntity() {
        return this.mc.objectMouseOver != null && this.mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY && this.mc.objectMouseOver.entityHit != null;
    }

    private boolean isPointedBlock() {
        return this.mc.objectMouseOver != null && this.mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK;
    }

    @Override
    public void onEnable() {
        if (this.aura == null) {
            this.aura = KetamineClient.getInstance().getModuleManager().getModule(Aura.class);
        }

        this.switched = false;
        this.previousSlot = -1;
    }

    @Override
    public void onDisable() {
        if (this.switched && this.previousSlot != -1) {
            this.mc.thePlayer.inventory.currentItem = previousSlot;
            this.previousSlot = -1;
            this.switched = false;
        }
    }
}
