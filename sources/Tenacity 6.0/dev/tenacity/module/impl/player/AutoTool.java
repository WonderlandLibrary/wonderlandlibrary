// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.module.impl.player;

import dev.tenacity.utils.player.InventoryUtils;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemStack;
import net.minecraft.block.Block;
import net.minecraft.util.MovingObjectPosition;
import dev.tenacity.module.api.TargetManager;
import dev.tenacity.event.impl.player.MotionEvent;
import dev.tenacity.module.settings.Setting;
import dev.tenacity.module.Category;
import dev.tenacity.module.settings.impl.BooleanSetting;
import dev.tenacity.module.Module;

public class AutoTool extends Module
{
    private final BooleanSetting autoSword;
    
    public AutoTool() {
        super("AutoTool", "Auto Tool", Category.PLAYER, "switches to the best tool");
        this.autoSword = new BooleanSetting("AutoSword", true);
        this.addSettings(this.autoSword);
    }
    
    @Override
    public void onMotionEvent(final MotionEvent e) {
        if (e.isPre()) {
            if (AutoTool.mc.objectMouseOver != null && AutoTool.mc.gameSettings.keyBindAttack.isKeyDown()) {
                final MovingObjectPosition objectMouseOver = AutoTool.mc.objectMouseOver;
                if (objectMouseOver.entityHit != null) {
                    this.switchSword();
                }
                else if (objectMouseOver.getBlockPos() != null) {
                    final Block block = AutoTool.mc.theWorld.getBlockState(objectMouseOver.getBlockPos()).getBlock();
                    this.updateItem(block);
                }
            }
            else if (TargetManager.target != null) {
                this.switchSword();
            }
        }
    }
    
    private void updateItem(final Block block) {
        float strength = 1.0f;
        int bestItem = -1;
        for (int i = 0; i < 9; ++i) {
            final ItemStack itemStack = AutoTool.mc.thePlayer.inventory.mainInventory[i];
            if (itemStack != null) {
                final float strVsBlock = itemStack.getStrVsBlock(block);
                if (strVsBlock > strength) {
                    strength = strVsBlock;
                    bestItem = i;
                }
            }
        }
        if (bestItem != -1) {
            AutoTool.mc.thePlayer.inventory.currentItem = bestItem;
        }
    }
    
    private void switchSword() {
        if (!this.autoSword.isEnabled()) {
            return;
        }
        float damage = 1.0f;
        int bestItem = -1;
        for (int i = 0; i < 9; ++i) {
            final ItemStack is = AutoTool.mc.thePlayer.inventory.mainInventory[i];
            if (is != null && is.getItem() instanceof ItemSword && InventoryUtils.getSwordStrength(is) > damage) {
                damage = InventoryUtils.getSwordStrength(is);
                bestItem = i;
            }
        }
        if (bestItem != -1) {
            AutoTool.mc.thePlayer.inventory.currentItem = bestItem;
        }
    }
}
