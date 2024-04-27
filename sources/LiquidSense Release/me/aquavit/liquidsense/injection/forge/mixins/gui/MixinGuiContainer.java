package me.aquavit.liquidsense.injection.forge.mixins.gui;

import me.aquavit.liquidsense.event.events.ChestEvent;
import me.aquavit.liquidsense.module.modules.blatant.Aura;
import me.aquavit.liquidsense.LiquidSense;
import me.aquavit.liquidsense.module.modules.player.InvClean;
import me.aquavit.liquidsense.module.modules.world.ChestStealer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiContainer.class)
@SideOnly(Side.CLIENT)
public abstract class MixinGuiContainer extends GuiScreen{

    @Inject(method = "initGui", at = @At("RETURN"))
    private void initGui(CallbackInfo callbackInfo) {
        if(!mc.isIntegratedServerRunning())
            buttonList.add(new GuiButton(11110, 5, 10, 100, 20, "Disable KillAura"));

        buttonList.add(new GuiButton(11120,5, 34, 100, 20,"Disable InvCleaner"));
        buttonList.add(new GuiButton(11130, 5, 58, 100, 20, "Disable ChestStealer"));
        super.initGui();
    }

    @Inject(method = "mouseClicked", at = @At("RETURN"))
    private void mouseClicked(int mouseX, int mouseY, int mouseButton, CallbackInfo callbackInfo) {
        for (Object aButtonList : this.buttonList) {
            GuiButton toggleButton = (GuiButton) aButtonList;
            if (toggleButton.mousePressed(mc, mouseX, mouseY) && toggleButton.id == 11110) {
                LiquidSense.moduleManager.getModule(Aura.class).setState(false);
            }
            if (toggleButton.mousePressed(mc, mouseX, mouseY) && toggleButton.id == 11120) {
                LiquidSense.moduleManager.getModule(InvClean.class).setState(false);
            }
            if (toggleButton.mousePressed(mc, mouseX, mouseY) && toggleButton.id == 11130) {
                LiquidSense.moduleManager.getModule(ChestStealer.class).setState(false);
            }
        }
    }

    @Inject(method = "drawScreen", at = @At("HEAD"), cancellable = true)
    private void drawScreen(CallbackInfo callbackInfo) {
        final ChestEvent event = new ChestEvent();
        LiquidSense.eventManager.callEvent(event);
        if(event.isCancelled())
            callbackInfo.cancel();
    }
}
