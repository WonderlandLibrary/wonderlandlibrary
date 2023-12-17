// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.tutorial;

import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.stats.StatBase;
import net.minecraft.stats.StatList;
import net.minecraft.item.Item;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.toasts.IToast;
import net.minecraft.item.ItemStack;
import net.minecraft.init.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.world.GameType;
import net.minecraft.client.gui.toasts.TutorialToast;
import net.minecraft.util.text.ITextComponent;

public class CraftPlanksStep implements ITutorialStep
{
    private static final ITextComponent TITLE;
    private static final ITextComponent DESCRIPTION;
    private final Tutorial tutorial;
    private TutorialToast toast;
    private int timeWaiting;
    
    public CraftPlanksStep(final Tutorial tutorial) {
        this.tutorial = tutorial;
    }
    
    @Override
    public void update() {
        ++this.timeWaiting;
        if (this.tutorial.getGameType() != GameType.SURVIVAL) {
            this.tutorial.setStep(TutorialSteps.NONE);
        }
        else {
            if (this.timeWaiting == 1) {
                this.tutorial.getMinecraft();
                final EntityPlayerSP entityplayersp = Minecraft.player;
                if (entityplayersp != null) {
                    if (entityplayersp.inventory.hasItemStack(new ItemStack(Blocks.PLANKS))) {
                        this.tutorial.setStep(TutorialSteps.NONE);
                        return;
                    }
                    if (didPlayerCraftedPlanks(entityplayersp)) {
                        this.tutorial.setStep(TutorialSteps.NONE);
                        return;
                    }
                }
            }
            if (this.timeWaiting >= 1200 && this.toast == null) {
                this.toast = new TutorialToast(TutorialToast.Icons.WOODEN_PLANKS, CraftPlanksStep.TITLE, CraftPlanksStep.DESCRIPTION, false);
                this.tutorial.getMinecraft().getToastGui().add(this.toast);
            }
        }
    }
    
    @Override
    public void onStop() {
        if (this.toast != null) {
            this.toast.hide();
            this.toast = null;
        }
    }
    
    @Override
    public void handleSetSlot(final ItemStack stack) {
        if (stack.getItem() == Item.getItemFromBlock(Blocks.PLANKS)) {
            this.tutorial.setStep(TutorialSteps.NONE);
        }
    }
    
    public static boolean didPlayerCraftedPlanks(final EntityPlayerSP player) {
        final StatBase statbase = StatList.getCraftStats(Item.getItemFromBlock(Blocks.PLANKS));
        return statbase != null && player.getStatFileWriter().readStat(statbase) > 0;
    }
    
    static {
        TITLE = new TextComponentTranslation("tutorial.craft_planks.title", new Object[0]);
        DESCRIPTION = new TextComponentTranslation("tutorial.craft_planks.description", new Object[0]);
    }
}
