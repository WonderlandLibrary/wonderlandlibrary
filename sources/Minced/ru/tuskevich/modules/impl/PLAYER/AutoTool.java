// 
// Decompiled by Procyon v0.5.36
// 

package ru.tuskevich.modules.impl.PLAYER;

import ru.tuskevich.event.events.impl.EventUpdate;
import ru.tuskevich.event.EventTarget;
import net.minecraft.item.ItemStack;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import ru.tuskevich.event.events.impl.EventInteractBlock;
import ru.tuskevich.ui.dropui.setting.Setting;
import java.util.ArrayList;
import java.util.List;
import ru.tuskevich.ui.dropui.setting.imp.MultiBoxSetting;
import ru.tuskevich.modules.Type;
import ru.tuskevich.modules.ModuleAnnotation;
import ru.tuskevich.modules.Module;

@ModuleAnnotation(name = "AutoTool", desc = "\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd \ufffd\ufffd\ufffd\ufffd\ufffd \ufffd\ufffd\ufffd\ufffd\ufffd\ufffd \ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd \ufffd \ufffd\ufffd\ufffd\ufffd \ufffd\ufffd\ufffd \ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd \ufffd\ufffd\ufffd\ufffd\ufffd", type = Type.PLAYER)
public class AutoTool extends Module
{
    public MultiBoxSetting toolMode;
    public int itemIndex;
    private boolean swap;
    private long swapDelay;
    private final List<Integer> lastItem;
    
    public AutoTool() {
        this.toolMode = new MultiBoxSetting("Addons", new String[] { "Swap Back", "Save Item" });
        this.lastItem = new ArrayList<Integer>();
        this.add(this.toolMode);
    }
    
    @EventTarget
    public void onInteractBlock(final EventInteractBlock eventInteractBlock) {
        if (AutoTool.mc.objectMouseOver != null && AutoTool.mc.objectMouseOver.getBlockPos() != null) {
            final List<Integer> bestItem = new ArrayList<Integer>();
            float bestSpeed = 1.0f;
            final Block block = AutoTool.mc.world.getBlockState(AutoTool.mc.objectMouseOver.getBlockPos()).getBlock();
            for (int i = 0; i < 9; ++i) {
                final Minecraft mc = AutoTool.mc;
                final ItemStack item = Minecraft.player.inventory.getStackInSlot(i);
                final float speed = item.getDestroySpeed(block.getDefaultState());
                final Minecraft mc2 = AutoTool.mc;
                final int maxDamage = Minecraft.player.inventory.getStackInSlot(i).getMaxDamage();
                final Minecraft mc3 = AutoTool.mc;
                if (maxDamage - Minecraft.player.inventory.getStackInSlot(i).getItemDamage() > 1 || !this.toolMode.get(1)) {
                    if (speed > bestSpeed) {
                        bestSpeed = speed;
                        bestItem.add(i);
                    }
                }
            }
            if (!bestItem.isEmpty() && AutoTool.mc.gameSettings.keyBindAttack.pressed) {
                final List<Integer> lastItem = this.lastItem;
                final Minecraft mc4 = AutoTool.mc;
                lastItem.add(Minecraft.player.inventory.currentItem);
                final Minecraft mc5 = AutoTool.mc;
                Minecraft.player.inventory.currentItem = bestItem.get(0);
                this.itemIndex = bestItem.get(0);
                this.swap = true;
                this.swapDelay = System.currentTimeMillis();
            }
        }
    }
    
    @EventTarget
    public void onUpdate(final EventUpdate eventUpdate) {
        if (this.swap && !this.lastItem.isEmpty() && System.currentTimeMillis() >= this.swapDelay + 300L && this.toolMode.get(0) && !AutoTool.mc.gameSettings.keyBindAttack.pressed) {
            final Minecraft mc = AutoTool.mc;
            Minecraft.player.inventory.currentItem = this.lastItem.get(0);
            this.itemIndex = this.lastItem.get(0);
            this.lastItem.clear();
            this.swap = false;
        }
    }
}
