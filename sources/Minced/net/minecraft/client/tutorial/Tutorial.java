// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.tutorial;

import net.minecraft.util.text.TextComponentKeybind;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.GameType;
import net.minecraft.item.ItemStack;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.util.MouseHelper;
import net.minecraft.util.MovementInput;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;

public class Tutorial
{
    private final Minecraft minecraft;
    @Nullable
    private ITutorialStep tutorialStep;
    
    public Tutorial(final Minecraft minecraft) {
        this.minecraft = minecraft;
    }
    
    public void handleMovement(final MovementInput p_193293_1_) {
        if (this.tutorialStep != null) {
            this.tutorialStep.handleMovement(p_193293_1_);
        }
    }
    
    public void handleMouse(final MouseHelper p_193299_1_) {
        if (this.tutorialStep != null) {
            this.tutorialStep.handleMouse(p_193299_1_);
        }
    }
    
    public void onMouseHover(@Nullable final WorldClient worldIn, @Nullable final RayTraceResult result) {
        if (this.tutorialStep != null && result != null && worldIn != null) {
            this.tutorialStep.onMouseHover(worldIn, result);
        }
    }
    
    public void onHitBlock(final WorldClient worldIn, final BlockPos pos, final IBlockState state, final float diggingStage) {
        if (this.tutorialStep != null) {
            this.tutorialStep.onHitBlock(worldIn, pos, state, diggingStage);
        }
    }
    
    public void openInventory() {
        if (this.tutorialStep != null) {
            this.tutorialStep.openInventory();
        }
    }
    
    public void handleSetSlot(final ItemStack stack) {
        if (this.tutorialStep != null) {
            this.tutorialStep.handleSetSlot(stack);
        }
    }
    
    public void stop() {
        if (this.tutorialStep != null) {
            this.tutorialStep.onStop();
            this.tutorialStep = null;
        }
    }
    
    public void reload() {
        if (this.tutorialStep != null) {
            this.stop();
        }
        this.tutorialStep = this.minecraft.gameSettings.tutorialStep.create(this);
    }
    
    public void update() {
        if (this.tutorialStep != null) {
            if (this.minecraft.world != null) {
                this.tutorialStep.update();
            }
            else {
                this.stop();
            }
        }
        else if (this.minecraft.world != null) {
            this.reload();
        }
    }
    
    public void setStep(final TutorialSteps step) {
        this.minecraft.gameSettings.tutorialStep = step;
        this.minecraft.gameSettings.saveOptions();
        if (this.tutorialStep != null) {
            this.tutorialStep.onStop();
            this.tutorialStep = step.create(this);
        }
    }
    
    public Minecraft getMinecraft() {
        return this.minecraft;
    }
    
    public GameType getGameType() {
        return (this.minecraft.playerController == null) ? GameType.NOT_SET : this.minecraft.playerController.getCurrentGameType();
    }
    
    public static ITextComponent createKeybindComponent(final String keybind) {
        final TextComponentKeybind textcomponentkeybind = new TextComponentKeybind("key." + keybind);
        textcomponentkeybind.getStyle().setBold(true);
        return textcomponentkeybind;
    }
}
