// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.tutorial;

import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.client.gui.toasts.IToast;
import net.minecraft.world.GameType;
import net.minecraft.client.gui.toasts.TutorialToast;
import net.minecraft.util.text.ITextComponent;

public class OpenInventoryStep implements ITutorialStep
{
    private static final ITextComponent TITLE;
    private static final ITextComponent DESCRIPTION;
    private final Tutorial tutorial;
    private TutorialToast toast;
    private int timeWaiting;
    
    public OpenInventoryStep(final Tutorial tutorial) {
        this.tutorial = tutorial;
    }
    
    @Override
    public void update() {
        ++this.timeWaiting;
        if (this.tutorial.getGameType() != GameType.SURVIVAL) {
            this.tutorial.setStep(TutorialSteps.NONE);
        }
        else if (this.timeWaiting >= 600 && this.toast == null) {
            this.toast = new TutorialToast(TutorialToast.Icons.RECIPE_BOOK, OpenInventoryStep.TITLE, OpenInventoryStep.DESCRIPTION, false);
            this.tutorial.getMinecraft().getToastGui().add(this.toast);
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
    public void openInventory() {
        this.tutorial.setStep(TutorialSteps.CRAFT_PLANKS);
    }
    
    static {
        TITLE = new TextComponentTranslation("tutorial.open_inventory.title", new Object[0]);
        DESCRIPTION = new TextComponentTranslation("tutorial.open_inventory.description", new Object[] { Tutorial.createKeybindComponent("inventory") });
    }
}
