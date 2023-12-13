package vip.radium.module.impl.player;

import io.github.nevalackin.homoBus.annotations.EventLink;
import io.github.nevalackin.homoBus.Listener;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
import vip.radium.event.impl.player.UpdatePositionEvent;
import vip.radium.module.Module;
import vip.radium.module.ModuleCategory;
import vip.radium.module.ModuleInfo;
import vip.radium.module.impl.combat.KillAura;
import vip.radium.property.Property;
import vip.radium.utils.InventoryUtils;
import vip.radium.utils.Wrapper;

@ModuleInfo(label = "Auto Tool", category = ModuleCategory.PLAYER)
public final class AutoTool extends Module {

    private final Property<Boolean> autoSwordProperty = new Property<>("Auto Sword", true);

    @EventLink
    public final Listener<UpdatePositionEvent> onUpdatePositionEvent = event -> {
        if (event.isPre()) {
            MovingObjectPosition objectMouseOver;
            if ((objectMouseOver = Wrapper.getMinecraft().objectMouseOver) != null &&
                    Wrapper.getGameSettings().keyBindAttack.isKeyDown()) {
                BlockPos blockPos;
                if (objectMouseOver.entityHit != null)
                    doSwordSwap();
                else if ((blockPos = objectMouseOver.getBlockPos()) != null) {
                    Block block = Wrapper.getWorld().getBlockState(blockPos).getBlock();
                    float strongestToolStr = 1.0F;
                    int strongestToolSlot = -1;
                    for (int i = 36; i < 45; i++) {
                        ItemStack stack = Wrapper.getStackInSlot(i);

                        if (stack != null && stack.getItem() instanceof ItemTool) {
                            float strVsBlock = stack.getStrVsBlock(block);
                            if (strVsBlock > strongestToolStr) {
                                strongestToolStr = strVsBlock;
                                strongestToolSlot = i;
                            }
                        }
                    }

                    if (strongestToolSlot != -1)
                        Wrapper.getPlayer().inventory.currentItem = strongestToolSlot - 36;
                }
            } else if (KillAura.getInstance().getTarget() != null)
                doSwordSwap();
        }
    };

    private void doSwordSwap() {
        if (!autoSwordProperty.getValue())
            return;

        double damage = 1.0;
        int slot = -1;
        for (int i = 36; i < 45; i++) {
            ItemStack stack = Wrapper.getStackInSlot(i);

            if (stack != null && stack.getItem() instanceof ItemSword) {
                double damageVs = InventoryUtils.getItemDamage(stack);
                if (damageVs > damage) {
                    damage = damageVs;
                    slot = i;
                }
            }
        }

        if (slot != -1)
            Wrapper.getPlayer().inventory.currentItem = slot - 36;
    }
}
