package me.earth.earthhack.impl.core.mixins.minecraft.gui;

import me.earth.earthhack.api.util.TextColor;
import me.earth.earthhack.impl.modules.client.pingbypass.PingBypass;
import me.earth.earthhack.impl.modules.client.pingbypass.guis.GuiAddPingBypass;
import me.earth.earthhack.impl.modules.client.pingbypass.guis.GuiButtonPingBypassOptions;
import me.earth.earthhack.impl.modules.client.pingbypass.guis.GuiConnectingPingBypass;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.multiplayer.ServerData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiMultiplayer.class)
public abstract class MixinGuiMultiPlayer extends GuiScreen
{
    private GuiButton pingBypassButton;

    @Inject(method = "createButtons", at = @At("HEAD"))
    public void createButtonsHook(CallbackInfo info)
    {
        this.buttonList.add(new GuiButtonPingBypassOptions(1339, width - 24, 5));
        pingBypassButton = addButton(new GuiButton(1337, width - 126, 5, 100, 20, getDisplayString()));
    }

    @Inject(method = "actionPerformed", at = @At("HEAD"), cancellable = true)
    protected void actionPerformed(GuiButton button, CallbackInfo info)
    {
        if (button.enabled)
        {
            if (button.id == 1337)
            {
                PingBypass.getInstance().toggle();
                pingBypassButton.displayString = getDisplayString();
                info.cancel();
            }
            else if (button.id == 1339)
            {
                mc.displayGuiScreen(new GuiAddPingBypass(this));
                info.cancel();
            }
        }
    }

    @Inject(method = "confirmClicked", at = @At("HEAD"), cancellable = true)
    public void confirmClickedHook(boolean result, int id, CallbackInfo info)
    {
        if (id == 1337)
        {
            mc.displayGuiScreen(this);
        }
    }

    @Inject(method = "connectToServer", at = @At("HEAD"), cancellable = true)
    public void connectToServerHook(ServerData data, CallbackInfo info)
    {
        if (PingBypass.getInstance().isEnabled())
        {
            mc.displayGuiScreen(new GuiConnectingPingBypass(this, mc, data));
            info.cancel();
        }
    }

    private String getDisplayString()
    {
        return "PingBypass: " + (PingBypass.getInstance().isEnabled() ? TextColor.GREEN + "On" : TextColor.RED + "Off");
    }

}
