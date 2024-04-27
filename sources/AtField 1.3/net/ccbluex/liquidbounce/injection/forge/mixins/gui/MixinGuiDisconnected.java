/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.authlib.Agent
 *  com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService
 *  com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication
 *  com.thealtening.AltService$EnumAltService
 *  com.thealtening.api.TheAltening
 *  com.thealtening.api.data.AccountData
 *  net.minecraft.client.gui.GuiButton
 *  net.minecraft.client.gui.GuiDisconnected
 *  net.minecraft.util.Session
 *  net.minecraftforge.fml.client.config.GuiSlider
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Shadow
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
package net.ccbluex.liquidbounce.injection.forge.mixins.gui;

import com.mojang.authlib.Agent;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication;
import com.thealtening.AltService;
import com.thealtening.api.TheAltening;
import com.thealtening.api.data.AccountData;
import java.net.Proxy;
import java.text.DecimalFormat;
import java.util.Random;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.event.SessionEvent;
import net.ccbluex.liquidbounce.features.special.AntiForge;
import net.ccbluex.liquidbounce.features.special.AutoReconnect;
import net.ccbluex.liquidbounce.injection.forge.mixins.gui.MixinGuiScreen;
import net.ccbluex.liquidbounce.ui.client.altmanager.GuiAltManager;
import net.ccbluex.liquidbounce.ui.client.altmanager.sub.altgenerator.GuiTheAltening;
import net.ccbluex.liquidbounce.utils.ClientUtils;
import net.ccbluex.liquidbounce.utils.ServerUtils;
import net.ccbluex.liquidbounce.utils.login.LoginUtils;
import net.ccbluex.liquidbounce.utils.login.MinecraftAccount;
import net.ccbluex.liquidbounce.utils.misc.RandomUtils;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiDisconnected;
import net.minecraft.util.Session;
import net.minecraftforge.fml.client.config.GuiSlider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={GuiDisconnected.class})
public abstract class MixinGuiDisconnected
extends MixinGuiScreen {
    @Shadow
    private int field_175353_i;
    private GuiButton reconnectButton;
    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#0");
    private int reconnectTimer;
    private GuiSlider autoReconnectDelaySlider;
    private GuiButton forgeBypassButton;

    private void updateReconnectButton() {
        if (this.reconnectButton != null) {
            this.reconnectButton.field_146126_j = "Reconnect" + (AutoReconnect.INSTANCE.isEnabled() ? " (" + (AutoReconnect.INSTANCE.getDelay() / 1000 - this.reconnectTimer / 20) + ")" : "");
        }
    }

    @Inject(method={"drawScreen"}, at={@At(value="RETURN")})
    private void drawScreen(CallbackInfo callbackInfo) {
        if (AutoReconnect.INSTANCE.isEnabled()) {
            this.updateReconnectButton();
        }
    }

    private void updateSliderText() {
        if (this.autoReconnectDelaySlider == null) {
            return;
        }
        this.autoReconnectDelaySlider.field_146126_j = !AutoReconnect.INSTANCE.isEnabled() ? "AutoReconnect: Off" : "AutoReconnect: " + DECIMAL_FORMAT.format((double)AutoReconnect.INSTANCE.getDelay() / 1000.0) + "s";
    }

    private void lambda$drawReconnectDelaySlider$0(GuiSlider guiSlider) {
        AutoReconnect.INSTANCE.setDelay(guiSlider.getValueInt());
        this.reconnectTimer = 0;
        this.updateReconnectButton();
        this.updateSliderText();
    }

    private void drawReconnectDelaySlider() {
        this.autoReconnectDelaySlider = new GuiSlider(2, this.field_146294_l / 2 + 2, this.field_146295_m / 2 + this.field_175353_i / 2 + this.field_146289_q.field_78288_b + 22, 98, 20, "AutoReconnect: ", "ms", 1000.0, 60000.0, (double)AutoReconnect.INSTANCE.getDelay(), false, true, this::lambda$drawReconnectDelaySlider$0);
        this.field_146292_n.add(this.autoReconnectDelaySlider);
    }

    @Inject(method={"actionPerformed"}, at={@At(value="HEAD")})
    private void actionPerformed(GuiButton guiButton, CallbackInfo callbackInfo) {
        switch (guiButton.field_146127_k) {
            case 1: {
                ServerUtils.connectToLastServer();
                break;
            }
            case 3: {
                Object object;
                Object object2;
                if (!GuiTheAltening.Companion.getApiKey().isEmpty()) {
                    object2 = GuiTheAltening.Companion.getApiKey();
                    object = new TheAltening((String)object2);
                    try {
                        AccountData accountData = object.getAccountData();
                        GuiAltManager.altService.switchService(AltService.EnumAltService.THEALTENING);
                        YggdrasilUserAuthentication yggdrasilUserAuthentication = new YggdrasilUserAuthentication(new YggdrasilAuthenticationService(Proxy.NO_PROXY, ""), Agent.MINECRAFT);
                        yggdrasilUserAuthentication.setUsername(accountData.getToken());
                        yggdrasilUserAuthentication.setPassword("AtField");
                        yggdrasilUserAuthentication.logIn();
                        this.field_146297_k.field_71449_j = new Session(yggdrasilUserAuthentication.getSelectedProfile().getName(), yggdrasilUserAuthentication.getSelectedProfile().getId().toString(), yggdrasilUserAuthentication.getAuthenticatedToken(), "mojang");
                        LiquidBounce.eventManager.callEvent(new SessionEvent());
                        ServerUtils.connectToLastServer();
                        break;
                    }
                    catch (Throwable throwable) {
                        ClientUtils.getLogger().error("Failed to login into random account from TheAltening.", throwable);
                    }
                }
                if ((object2 = LiquidBounce.fileManager.accountsConfig.getAccounts()).isEmpty()) break;
                object = (MinecraftAccount)object2.get(new Random().nextInt(object2.size()));
                GuiAltManager.login((MinecraftAccount)object);
                ServerUtils.connectToLastServer();
                break;
            }
            case 4: {
                LoginUtils.loginCracked(RandomUtils.randomString(RandomUtils.nextInt(5, 16)));
                ServerUtils.connectToLastServer();
                break;
            }
            case 5: {
                AntiForge.enabled = !AntiForge.enabled;
                this.forgeBypassButton.field_146126_j = "Bypass AntiForge: " + (AntiForge.enabled ? "On" : "Off");
                LiquidBounce.fileManager.saveConfig(LiquidBounce.fileManager.valuesConfig);
            }
        }
    }

    @Override
    public void func_73876_c() {
        if (AutoReconnect.INSTANCE.isEnabled()) {
            ++this.reconnectTimer;
            if (this.reconnectTimer > AutoReconnect.INSTANCE.getDelay() / 50) {
                ServerUtils.connectToLastServer();
            }
        }
    }

    @Inject(method={"initGui"}, at={@At(value="RETURN")})
    private void initGui(CallbackInfo callbackInfo) {
        this.reconnectTimer = 0;
        this.reconnectButton = new GuiButton(1, this.field_146294_l / 2 - 100, this.field_146295_m / 2 + this.field_175353_i / 2 + this.field_146289_q.field_78288_b + 22, 98, 20, "Reconnect");
        this.field_146292_n.add(this.reconnectButton);
        this.drawReconnectDelaySlider();
        this.field_146292_n.add(new GuiButton(3, this.field_146294_l / 2 - 100, this.field_146295_m / 2 + this.field_175353_i / 2 + this.field_146289_q.field_78288_b + 44, 98, 20, GuiTheAltening.Companion.getApiKey().isEmpty() ? "Random alt" : "New TheAltening alt"));
        this.field_146292_n.add(new GuiButton(4, this.field_146294_l / 2 + 2, this.field_146295_m / 2 + this.field_175353_i / 2 + this.field_146289_q.field_78288_b + 44, 98, 20, "Random username"));
        this.forgeBypassButton = new GuiButton(5, this.field_146294_l / 2 - 100, this.field_146295_m / 2 + this.field_175353_i / 2 + this.field_146289_q.field_78288_b + 66, "Bypass AntiForge: " + (AntiForge.enabled ? "On" : "Off"));
        this.field_146292_n.add(this.forgeBypassButton);
        this.updateSliderText();
    }
}

