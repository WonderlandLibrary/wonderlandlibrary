/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.authlib.Agent
 *  com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService
 *  com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication
 *  net.minecraft.client.gui.GuiButton
 *  net.minecraft.client.gui.GuiDisconnected
 *  net.minecraft.util.Session
 *  net.minecraftforge.fml.client.config.GuiSlider
 */
package net.dev.important.injection.forge.mixins.gui;

import com.mojang.authlib.Agent;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication;
import com.thealtening.AltService;
import com.thealtening.api.TheAltening;
import com.thealtening.api.data.AccountData;
import java.net.Proxy;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Random;
import net.dev.important.Client;
import net.dev.important.event.SessionEvent;
import net.dev.important.gui.client.altmanager.GuiAltManager;
import net.dev.important.gui.client.altmanager.sub.altgenerator.GuiTheAltening;
import net.dev.important.gui.font.Fonts;
import net.dev.important.injection.forge.mixins.gui.MixinGuiScreen;
import net.dev.important.modules.command.other.AntiForge;
import net.dev.important.modules.command.other.AutoReconnect;
import net.dev.important.utils.ClientUtils;
import net.dev.important.utils.ServerUtils;
import net.dev.important.utils.SessionUtils;
import net.dev.important.utils.login.LoginUtils;
import net.dev.important.utils.login.MinecraftAccount;
import net.dev.important.utils.misc.RandomUtils;
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
    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#0");
    @Shadow
    private int field_175353_i;
    private GuiButton reconnectButton;
    private GuiSlider autoReconnectDelaySlider;
    private GuiButton forgeBypassButton;
    private int reconnectTimer;

    @Inject(method={"initGui"}, at={@At(value="RETURN")})
    private void initGui(CallbackInfo callbackInfo) {
        this.reconnectTimer = 0;
        SessionUtils.handleConnection();
        this.reconnectButton = new GuiButton(1, this.field_146294_l / 2 - 100, this.field_146295_m / 2 + this.field_175353_i / 2 + this.field_146289_q.field_78288_b + 22, 98, 20, "Reconnect");
        this.field_146292_n.add(this.reconnectButton);
        this.drawReconnectDelaySlider();
        this.field_146292_n.add(new GuiButton(3, this.field_146294_l / 2 - 100, this.field_146295_m / 2 + this.field_175353_i / 2 + this.field_146289_q.field_78288_b + 44, 98, 20, GuiTheAltening.Companion.getApiKey().isEmpty() ? "Random alt" : "New TheAltening alt"));
        this.field_146292_n.add(new GuiButton(4, this.field_146294_l / 2 + 2, this.field_146295_m / 2 + this.field_175353_i / 2 + this.field_146289_q.field_78288_b + 44, 98, 20, "Random cracked"));
        this.forgeBypassButton = new GuiButton(5, this.field_146294_l / 2 - 100, this.field_146295_m / 2 + this.field_175353_i / 2 + this.field_146289_q.field_78288_b + 66, "AntiForge: " + (AntiForge.enabled ? "On" : "Off"));
        this.field_146292_n.add(this.forgeBypassButton);
        this.updateSliderText();
    }

    @Inject(method={"actionPerformed"}, at={@At(value="HEAD")})
    private void actionPerformed(GuiButton button, CallbackInfo callbackInfo) {
        switch (button.field_146127_k) {
            case 1: {
                ServerUtils.connectToLastServer();
                break;
            }
            case 3: {
                List<MinecraftAccount> accounts;
                if (!GuiTheAltening.Companion.getApiKey().isEmpty()) {
                    String apiKey = GuiTheAltening.Companion.getApiKey();
                    TheAltening theAltening = new TheAltening(apiKey);
                    try {
                        AccountData account = theAltening.getAccountData();
                        GuiAltManager.altService.switchService(AltService.EnumAltService.THEALTENING);
                        YggdrasilUserAuthentication yggdrasilUserAuthentication = new YggdrasilUserAuthentication(new YggdrasilAuthenticationService(Proxy.NO_PROXY, ""), Agent.MINECRAFT);
                        yggdrasilUserAuthentication.setUsername(account.getToken());
                        yggdrasilUserAuthentication.setPassword("LiquidPlus");
                        yggdrasilUserAuthentication.logIn();
                        this.field_146297_k.field_71449_j = new Session(yggdrasilUserAuthentication.getSelectedProfile().getName(), yggdrasilUserAuthentication.getSelectedProfile().getId().toString(), yggdrasilUserAuthentication.getAuthenticatedToken(), "mojang");
                        Client.eventManager.callEvent(new SessionEvent());
                        ServerUtils.connectToLastServer();
                        break;
                    }
                    catch (Throwable throwable) {
                        ClientUtils.getLogger().error("Failed to login into random account from TheAltening.", throwable);
                    }
                }
                if ((accounts = Client.fileManager.accountsConfig.getAccounts()).isEmpty()) break;
                MinecraftAccount minecraftAccount = accounts.get(new Random().nextInt(accounts.size()));
                GuiAltManager.login(minecraftAccount);
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
                this.forgeBypassButton.field_146126_j = "AntiForge: " + (AntiForge.enabled ? "On" : "Off");
                Client.fileManager.saveConfig(Client.fileManager.valuesConfig);
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

    @Inject(method={"drawScreen"}, at={@At(value="RETURN")})
    private void drawScreen(CallbackInfo callbackInfo) {
        if (AutoReconnect.INSTANCE.isEnabled()) {
            this.updateReconnectButton();
        }
        Fonts.fontSFUI40.drawCenteredString("Played as " + this.field_146297_k.field_71449_j.func_111285_a() + ", server: " + ServerUtils.serverData.field_78845_b, (float)this.field_146294_l / 2.0f, (float)this.field_146295_m / 2.0f + (float)this.field_175353_i / 2.0f + (float)this.field_146289_q.field_78288_b + 96.0f, -1, true);
        Fonts.fontSFUI40.drawCenteredString("You have played " + SessionUtils.getFormatLastSessionTime() + " before getting kicked.", (float)this.field_146294_l / 2.0f, (float)this.field_146295_m / 2.0f + (float)this.field_175353_i / 2.0f + (float)this.field_146289_q.field_78288_b * 2.0f + 98.0f, -1, true);
    }

    private void drawReconnectDelaySlider() {
        this.autoReconnectDelaySlider = new GuiSlider(2, this.field_146294_l / 2 + 2, this.field_146295_m / 2 + this.field_175353_i / 2 + this.field_146289_q.field_78288_b + 22, 98, 20, "Auto: ", "ms", 1000.0, 60000.0, (double)AutoReconnect.INSTANCE.getDelay(), false, true, guiSlider -> {
            AutoReconnect.INSTANCE.setDelay(guiSlider.getValueInt());
            this.reconnectTimer = 0;
            this.updateReconnectButton();
            this.updateSliderText();
        });
        this.field_146292_n.add(this.autoReconnectDelaySlider);
    }

    private void updateSliderText() {
        if (this.autoReconnectDelaySlider == null) {
            return;
        }
        this.autoReconnectDelaySlider.field_146126_j = !AutoReconnect.INSTANCE.isEnabled() ? "Auto: Off" : "Auto: " + DECIMAL_FORMAT.format((double)AutoReconnect.INSTANCE.getDelay() / 1000.0) + "s";
    }

    private void updateReconnectButton() {
        if (this.reconnectButton != null) {
            this.reconnectButton.field_146126_j = "Reconnect" + (AutoReconnect.INSTANCE.isEnabled() ? " (" + (AutoReconnect.INSTANCE.getDelay() / 1000 - this.reconnectTimer / 20) + ")" : "");
        }
    }
}

