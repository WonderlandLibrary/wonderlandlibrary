/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.GuiChat
 *  net.minecraft.client.gui.GuiTextField
 *  net.minecraft.util.IChatComponent
 *  net.minecraftforge.fml.relauncher.Side
 *  net.minecraftforge.fml.relauncher.SideOnly
 *  org.lwjgl.input.Mouse
 */
package net.dev.important.injection.forge.mixins.gui;

import java.awt.Color;
import java.util.Comparator;
import java.util.List;
import net.dev.important.Client;
import net.dev.important.injection.forge.mixins.gui.MixinGuiScreen;
import net.dev.important.utils.AnimationUtils;
import net.dev.important.utils.render.RenderUtils;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Mouse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@SideOnly(value=Side.CLIENT)
@Mixin(value={GuiChat.class})
public abstract class MixinGuiChat
extends MixinGuiScreen {
    @Shadow
    protected GuiTextField field_146415_a;
    @Shadow
    private List<String> field_146412_t;
    @Shadow
    private boolean field_146414_r;
    private float yPosOfInputField;
    private float fade = 0.0f;

    @Shadow
    public abstract void func_146406_a(String[] var1);

    @Inject(method={"initGui"}, at={@At(value="RETURN")})
    private void init(CallbackInfo callbackInfo) {
        this.field_146415_a.field_146210_g = this.field_146295_m + 1;
        this.yPosOfInputField = this.field_146415_a.field_146210_g;
    }

    @Inject(method={"keyTyped"}, at={@At(value="RETURN")})
    private void updateLength(CallbackInfo callbackInfo) {
        if (!this.field_146415_a.func_146179_b().startsWith(String.valueOf(Client.commandManager.getPrefix()))) {
            return;
        }
        Client.commandManager.autoComplete(this.field_146415_a.func_146179_b());
        if (!this.field_146415_a.func_146179_b().startsWith(Client.commandManager.getPrefix() + "lc")) {
            this.field_146415_a.func_146203_f(10000);
        } else {
            this.field_146415_a.func_146203_f(100);
        }
    }

    @Inject(method={"updateScreen"}, at={@At(value="HEAD")})
    private void updateScreen(CallbackInfo callbackInfo) {
        int delta = RenderUtils.deltaTime;
        if (this.fade < 14.0f) {
            this.fade = AnimationUtils.animate(14.0f, this.fade, 0.02f * (float)delta);
        }
        if (this.fade > 14.0f) {
            this.fade = 14.0f;
        }
        if (this.yPosOfInputField > (float)(this.field_146295_m - 12)) {
            this.yPosOfInputField = AnimationUtils.animate(this.field_146295_m - 12, this.yPosOfInputField, 0.0175f * (float)delta);
        }
        if (this.yPosOfInputField < (float)(this.field_146295_m - 12)) {
            this.yPosOfInputField = this.field_146295_m - 12;
        }
        this.field_146415_a.field_146210_g = (int)this.yPosOfInputField;
        RenderUtils.yPosOffset = this.fade * 1.1428572f;
    }

    @Inject(method={"autocompletePlayerNames"}, at={@At(value="HEAD")})
    private void prioritizeClientFriends(CallbackInfo callbackInfo) {
        this.field_146412_t.sort(Comparator.comparing(s -> !Client.fileManager.friendsConfig.isFriend((String)s)));
    }

    @Inject(method={"sendAutocompleteRequest"}, at={@At(value="HEAD")}, cancellable=true)
    private void handleClientCommandCompletion(String full, String ignored, CallbackInfo callbackInfo) {
        if (Client.commandManager.autoComplete(full)) {
            this.field_146414_r = true;
            String[] latestAutoComplete = Client.commandManager.getLatestAutoComplete();
            if (full.toLowerCase().endsWith(latestAutoComplete[latestAutoComplete.length - 1].toLowerCase())) {
                return;
            }
            this.func_146406_a(latestAutoComplete);
            callbackInfo.cancel();
        }
    }

    @Inject(method={"onAutocompleteResponse"}, at={@At(value="INVOKE", target="Lnet/minecraft/client/gui/GuiChat;autocompletePlayerNames(F)V", shift=At.Shift.BEFORE)}, cancellable=true)
    private void onAutocompleteResponse(String[] autoCompleteResponse, CallbackInfo callbackInfo) {
        if (Client.commandManager.getLatestAutoComplete().length != 0) {
            callbackInfo.cancel();
        }
    }

    @Overwrite
    public void func_73863_a(int mouseX, int mouseY, float partialTicks) {
        IChatComponent ichatcomponent;
        RenderUtils.drawShadow2(2, (int)((float)this.field_146295_m - this.fade), this.field_146294_l - 2, (int)((float)this.field_146295_m - this.fade + 12.0f));
        RenderUtils.drawShadow2(2, (int)((float)this.field_146295_m - this.fade), this.field_146294_l - 2, (int)((float)this.field_146295_m - this.fade + 12.0f));
        RenderUtils.drawRect(2.0f, (float)this.field_146295_m - this.fade, (float)(this.field_146294_l - 2), (float)this.field_146295_m - this.fade + 12.0f, Integer.MIN_VALUE);
        this.field_146415_a.func_146194_f();
        if (Client.commandManager.getLatestAutoComplete().length > 0 && !this.field_146415_a.func_146179_b().isEmpty() && this.field_146415_a.func_146179_b().startsWith(String.valueOf(Client.commandManager.getPrefix()))) {
            String[] latestAutoComplete = Client.commandManager.getLatestAutoComplete();
            String[] textArray = this.field_146415_a.func_146179_b().split(" ");
            String trimmedString = latestAutoComplete[0].replaceFirst("(?i)" + textArray[textArray.length - 1], "");
            this.field_146297_k.field_71466_p.func_175063_a(trimmedString, (float)(this.field_146415_a.field_146209_f + this.field_146297_k.field_71466_p.func_78256_a(this.field_146415_a.func_146179_b())), (float)this.field_146415_a.field_146210_g, new Color(165, 165, 165).getRGB());
        }
        if ((ichatcomponent = this.field_146297_k.field_71456_v.func_146158_b().func_146236_a(Mouse.getX(), Mouse.getY())) != null) {
            this.func_175272_a(ichatcomponent, mouseX, mouseY);
        }
    }

    @Inject(method={"onGuiClosed"}, at={@At(value="RETURN")}, cancellable=true)
    public void injectGuiClosed(CallbackInfo callbackInfo) {
        RenderUtils.yPosOffset = 0.0f;
    }
}

