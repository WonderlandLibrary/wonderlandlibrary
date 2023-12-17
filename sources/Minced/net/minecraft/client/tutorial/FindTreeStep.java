// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.tutorial;

import net.minecraft.util.text.TextComponentTranslation;
import com.google.common.collect.Sets;
import net.minecraft.init.Blocks;
import net.minecraft.stats.StatBase;
import net.minecraft.stats.StatList;
import net.minecraft.item.Item;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.RayTraceResult;
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

public class FindTreeStep implements ITutorialStep
{
    private static final Set<Block> TREE_BLOCKS;
    private static final ITextComponent TITLE;
    private static final ITextComponent DESCRIPTION;
    private final Tutorial tutorial;
    private TutorialToast toast;
    private int timeWaiting;
    
    public FindTreeStep(final Tutorial tutorial) {
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
                    for (final Block block : FindTreeStep.TREE_BLOCKS) {
                        if (entityplayersp.inventory.hasItemStack(new ItemStack(block))) {
                            this.tutorial.setStep(TutorialSteps.CRAFT_PLANKS);
                            return;
                        }
                    }
                    if (hasPunchedTreesPreviously(entityplayersp)) {
                        this.tutorial.setStep(TutorialSteps.CRAFT_PLANKS);
                        return;
                    }
                }
            }
            if (this.timeWaiting >= 6000 && this.toast == null) {
                this.toast = new TutorialToast(TutorialToast.Icons.TREE, FindTreeStep.TITLE, FindTreeStep.DESCRIPTION, false);
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
    public void onMouseHover(final WorldClient worldIn, final RayTraceResult result) {
        if (result.typeOfHit == RayTraceResult.Type.BLOCK && result.getBlockPos() != null) {
            final IBlockState iblockstate = worldIn.getBlockState(result.getBlockPos());
            if (FindTreeStep.TREE_BLOCKS.contains(iblockstate.getBlock())) {
                this.tutorial.setStep(TutorialSteps.PUNCH_TREE);
            }
        }
    }
    
    @Override
    public void handleSetSlot(final ItemStack stack) {
        for (final Block block : FindTreeStep.TREE_BLOCKS) {
            if (stack.getItem() == Item.getItemFromBlock(block)) {
                this.tutorial.setStep(TutorialSteps.CRAFT_PLANKS);
            }
        }
    }
    
    public static boolean hasPunchedTreesPreviously(final EntityPlayerSP p_194070_0_) {
        for (final Block block : FindTreeStep.TREE_BLOCKS) {
            final StatBase statbase = StatList.getBlockStats(block);
            if (statbase != null && p_194070_0_.getStatFileWriter().readStat(statbase) > 0) {
                return true;
            }
        }
        return false;
    }
    
    static {
        TREE_BLOCKS = Sets.newHashSet((Object[])new Block[] { Blocks.LOG, Blocks.LOG2, Blocks.LEAVES, Blocks.LEAVES2 });
        TITLE = new TextComponentTranslation("tutorial.find_tree.title", new Object[0]);
        DESCRIPTION = new TextComponentTranslation("tutorial.find_tree.description", new Object[0]);
    }
}
