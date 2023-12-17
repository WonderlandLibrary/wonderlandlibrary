// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.tutorial;

import net.minecraft.util.text.TextComponentTranslation;
import com.google.common.collect.Sets;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.client.multiplayer.WorldClient;
import java.util.Iterator;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.toasts.IToast;
import net.minecraft.item.ItemStack;
import net.minecraft.client.Minecraft;
import net.minecraft.world.GameType;
import net.minecraft.client.gui.toasts.TutorialToast;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.block.Block;
import java.util.Set;

public class PunchTreeStep implements ITutorialStep
{
    private static final Set<Block> LOG_BLOCKS;
    private static final ITextComponent TITLE;
    private static final ITextComponent DESCRIPTION;
    private final Tutorial tutorial;
    private TutorialToast toast;
    private int timeWaiting;
    private int resetCount;
    
    public PunchTreeStep(final Tutorial tutorial) {
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
                    for (final Block block : PunchTreeStep.LOG_BLOCKS) {
                        if (entityplayersp.inventory.hasItemStack(new ItemStack(block))) {
                            this.tutorial.setStep(TutorialSteps.CRAFT_PLANKS);
                            return;
                        }
                    }
                    if (FindTreeStep.hasPunchedTreesPreviously(entityplayersp)) {
                        this.tutorial.setStep(TutorialSteps.CRAFT_PLANKS);
                        return;
                    }
                }
            }
            if ((this.timeWaiting >= 600 || this.resetCount > 3) && this.toast == null) {
                this.toast = new TutorialToast(TutorialToast.Icons.TREE, PunchTreeStep.TITLE, PunchTreeStep.DESCRIPTION, true);
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
    public void onHitBlock(final WorldClient worldIn, final BlockPos pos, final IBlockState state, final float diggingStage) {
        final boolean flag = PunchTreeStep.LOG_BLOCKS.contains(state.getBlock());
        if (flag && diggingStage > 0.0f) {
            if (this.toast != null) {
                this.toast.setProgress(diggingStage);
            }
            if (diggingStage >= 1.0f) {
                this.tutorial.setStep(TutorialSteps.OPEN_INVENTORY);
            }
        }
        else if (this.toast != null) {
            this.toast.setProgress(0.0f);
        }
        else if (flag) {
            ++this.resetCount;
        }
    }
    
    @Override
    public void handleSetSlot(final ItemStack stack) {
        for (final Block block : PunchTreeStep.LOG_BLOCKS) {
            if (stack.getItem() == Item.getItemFromBlock(block)) {
                this.tutorial.setStep(TutorialSteps.CRAFT_PLANKS);
            }
        }
    }
    
    static {
        LOG_BLOCKS = Sets.newHashSet((Object[])new Block[] { Blocks.LOG, Blocks.LOG2 });
        TITLE = new TextComponentTranslation("tutorial.punch_tree.title", new Object[0]);
        DESCRIPTION = new TextComponentTranslation("tutorial.punch_tree.description", new Object[] { Tutorial.createKeybindComponent("attack") });
    }
}
