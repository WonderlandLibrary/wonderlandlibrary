/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.GuiButton
 *  net.minecraft.client.gui.GuiMultiplayer
 *  net.minecraft.client.gui.GuiScreen
 *  net.minecraftforge.fml.client.config.GuiSlider
 */
package net.dev.important.injection.forge.mixins.gui;

import de.enzaxd.viaforge.ViaForge;
import de.enzaxd.viaforge.protocol.ProtocolCollection;
import net.dev.important.Client;
import net.dev.important.gui.client.GuiAntiForge;
import net.dev.important.injection.forge.mixins.gui.MixinGuiScreen;
import net.dev.important.modules.command.other.BungeeCordSpoof;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.client.config.GuiSlider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={GuiMultiplayer.class})
public abstract class MixinGuiMultiplayer
extends MixinGuiScreen {
    private GuiButton bungeeCordSpoofButton;
    private GuiSlider viaSlider;

    @Inject(method={"initGui"}, at={@At(value="RETURN")})
    private void initGui(CallbackInfo callbackInfo) {
        this.field_146292_n.add(new GuiButton(997, 5, 8, 98, 20, "AntiForge"));
        this.viaSlider = new GuiSlider(1337, this.field_146294_l - 104, 8, 98, 20, "Version: ", "", 0.0, (double)(ProtocolCollection.values().length - 1), (double)(ProtocolCollection.values().length - 1 - this.getProtocolIndex(ViaForge.getInstance().getVersion())), false, true, guiSlider -> {
            ViaForge.getInstance().setVersion(ProtocolCollection.values()[ProtocolCollection.values().length - 1 - guiSlider.getValueInt()].getVersion().getVersion());
            this.updatePortalText();
        });
        this.field_146292_n.add(this.viaSlider);
        this.bungeeCordSpoofButton = new GuiButton(998, 108, 8, 98, 20, (BungeeCordSpoof.enabled ? "\u00a7a" : "\u00a7c") + "BungeeCord Spoof");
        this.field_146292_n.add(this.bungeeCordSpoofButton);
        this.updatePortalText();
    }

    private void updatePortalText() {
        if (this.viaSlider == null) {
            return;
        }
        this.viaSlider.field_146126_j = "Version: " + ProtocolCollection.getProtocolById(ViaForge.getInstance().getVersion()).getName();
    }

    private int getProtocolIndex(int id) {
        for (int i = 0; i < ProtocolCollection.values().length; ++i) {
            if (ProtocolCollection.values()[i].getVersion().getVersion() != id) continue;
            return i;
        }
        return -1;
    }

    @Inject(method={"actionPerformed"}, at={@At(value="HEAD")})
    private void actionPerformed(GuiButton button, CallbackInfo callbackInfo) {
        switch (button.field_146127_k) {
            case 997: {
                this.field_146297_k.func_147108_a((GuiScreen)new GuiAntiForge((GuiScreen)this));
                break;
            }
            case 998: {
                BungeeCordSpoof.enabled = !BungeeCordSpoof.enabled;
                this.bungeeCordSpoofButton.field_146126_j = (BungeeCordSpoof.enabled ? "\u00a7a" : "\u00a7c") + "BungeeCord Spoof";
                Client.fileManager.saveConfig(Client.fileManager.valuesConfig);
            }
        }
    }
}

