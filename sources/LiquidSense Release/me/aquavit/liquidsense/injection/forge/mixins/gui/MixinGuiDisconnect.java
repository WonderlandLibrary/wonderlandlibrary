package me.aquavit.liquidsense.injection.forge.mixins.gui;

import me.aquavit.liquidsense.LiquidSense;
import me.aquavit.liquidsense.ui.client.gui.elements.AntiForge;
import me.aquavit.liquidsense.utils.misc.ServerUtils;
import me.aquavit.liquidsense.utils.login.LoginUtils;
import me.aquavit.liquidsense.utils.misc.RandomUtils;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiDisconnected;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiDisconnected.class)
public abstract class MixinGuiDisconnect extends MixinGuiScreen {

    @Shadow
    private int field_175353_i;

    private GuiButton reconnectButton;
    private GuiButton forgeBypassButton;
    private int reconnectTimer;

    @Inject(method = "initGui", at = @At("RETURN"))
    private void initGui(CallbackInfo callbackInfo) {
        reconnectTimer = 0;
        buttonList.add(reconnectButton = new GuiButton(1, this.width / 2 - 100, this.height / 2 + field_175353_i / 2 + this.fontRendererObj.FONT_HEIGHT + 22, "Reconnect"));
        buttonList.add(new GuiButton(4, this.width / 2 - 100, this.height / 2 + field_175353_i / 2 + this.fontRendererObj.FONT_HEIGHT + 44, 200, 20, "Random username"));
        buttonList.add(forgeBypassButton = new GuiButton(2, this.width / 2 - 100, this.height / 2 + field_175353_i / 2 + this.fontRendererObj.FONT_HEIGHT + 66, "Bypass AntiForge: " + (AntiForge.enabled ? "On" : "Off")));
    }

    @Inject(method = "actionPerformed", at = @At("HEAD"))
    private void actionPerformed(GuiButton button, CallbackInfo callbackInfo) {
        switch (button.id) {
            case 1:
                ServerUtils.connectToLastServer();
                break;
            case 2:
                AntiForge.enabled = !AntiForge.enabled;
                forgeBypassButton.displayString = "Bypass AntiForge: " + (AntiForge.enabled ? "On" : "Off");
                LiquidSense.fileManager.saveConfig(LiquidSense.fileManager.valuesConfig);
                break;
            case 4:
                LoginUtils.loginCracked(RandomUtils.randomString(RandomUtils.nextInt(5, 16)));
                ServerUtils.connectToLastServer();
                break;
        }
    }

    @Override
    public void updateScreen() {
        reconnectTimer++;
        if (reconnectTimer > 100)
            ServerUtils.connectToLastServer();
    }

    @Inject(method = "drawScreen", at = @At("RETURN"))
    private void drawScreen(CallbackInfo callbackInfo) {
        reconnectButton.displayString = "Reconnect (" + (5 - reconnectTimer / 20) + ")";
    }
}