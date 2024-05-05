// 
// Decompiled by Procyon v0.5.36
// 

package vip.Resolute.modules.impl.player;

import vip.Resolute.util.player.InventoryUtils;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemStack;
import net.minecraft.block.Block;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
import vip.Resolute.modules.impl.combat.KillAura;
import net.minecraft.item.ItemTool;
import vip.Resolute.Resolute;
import vip.Resolute.events.impl.EventMotion;
import vip.Resolute.events.Event;
import vip.Resolute.settings.Setting;
import vip.Resolute.settings.impl.BooleanSetting;
import vip.Resolute.modules.Module;

public class AutoTool extends Module
{
    public BooleanSetting autoSword;
    
    public AutoTool() {
        super("AutoTool", 0, "Automatically switches to the best tool", Category.PLAYER);
        this.autoSword = new BooleanSetting("Sword", true);
        this.addSettings(this.autoSword);
    }
    
    @Override
    public void onEvent(final Event e) {
        if (e instanceof EventMotion && e.isPre()) {
            final MovingObjectPosition objectMouseOver;
            if ((objectMouseOver = AutoTool.mc.objectMouseOver) != null && AutoTool.mc.gameSettings.keyBindAttack.isKeyDown()) {
                if (objectMouseOver.entityHit != null) {
                    this.doSwordSwap();
                }
                else {
                    final BlockPos blockPos;
                    if ((blockPos = objectMouseOver.getBlockPos()) != null) {
                        final Block block = AutoTool.mc.theWorld.getBlockState(blockPos).getBlock();
                        float strongestToolStr = 1.0f;
                        int strongestToolSlot = -1;
                        for (int i = 36; i < 45; ++i) {
                            final ItemStack stack = Resolute.getStackInSlot(i);
                            if (stack != null && stack.getItem() instanceof ItemTool) {
                                final float strVsBlock = stack.getStrVsBlock(block);
                                if (strVsBlock > strongestToolStr) {
                                    strongestToolStr = strVsBlock;
                                    strongestToolSlot = i;
                                }
                            }
                        }
                        if (strongestToolSlot != -1) {
                            AutoTool.mc.thePlayer.inventory.currentItem = strongestToolSlot - 36;
                        }
                    }
                }
            }
            else if (KillAura.target != null) {
                this.doSwordSwap();
            }
        }
    }
    
    private void doSwordSwap() {
        double damage = 1.0;
        int slot = -1;
        for (int i = 36; i < 45; ++i) {
            final ItemStack stack = Resolute.getStackInSlot(i);
            if (stack != null && stack.getItem() instanceof ItemSword) {
                final double damageVs = InventoryUtils.getItemDamage(stack);
                if (damageVs > damage) {
                    damage = damageVs;
                    slot = i;
                }
            }
        }
        if (slot != -1) {
            AutoTool.mc.thePlayer.inventory.currentItem = slot - 36;
        }
    }
}
