/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */
package net.ccbluex.liquidbounce.injection.forge.mixins.gui;

import me.liuli.elixir.account.CrackedAccount;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.event.SessionEvent;
import net.ccbluex.liquidbounce.features.special.AntiForge;
import net.ccbluex.liquidbounce.features.special.AutoReconnect;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.ccbluex.liquidbounce.utils.ServerUtils;
import net.ccbluex.liquidbounce.utils.SessionInfoUtils;
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

import java.awt.*;
import java.net.Proxy;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Random;

@Mixin(GuiDisconnected.class)
public abstract class MixinGuiDisconnected extends MixinGuiScreen {
    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#0");

    @Shadow
    private int field_175353_i;

    private GuiButton reconnectButton;
    private GuiSlider autoReconnectDelaySlider;
    private GuiButton forgeBypassButton;
    private int reconnectTimer;

    @Inject(method = "initGui", at = @At("RETURN"))
    private void initGui(CallbackInfo callbackInfo) {
        reconnectTimer = 0;
        buttonList.add(reconnectButton = new GuiButton(1, this.width / 2 - 100, this.height / 2 + field_175353_i / 2 + this.fontRendererObj.FONT_HEIGHT + 22, 98, 20, "Reconnect"));

        this.drawReconnectDelaySlider();

        buttonList.add(new GuiButton(4, this.width / 2 + 2, this.height / 2 + field_175353_i / 2 + this.fontRendererObj.FONT_HEIGHT + 44, 98, 20, "Random username"));
        buttonList.add(forgeBypassButton = new GuiButton(5, this.width / 2 - 100, this.height / 2 + field_175353_i / 2 + this.fontRendererObj.FONT_HEIGHT + 66, "Bypass AntiForge: " + (AntiForge.enabled ? "On" : "Off")));

        updateSliderText();

        
    }

    @Inject(method = "actionPerformed", at = @At("HEAD"))
    private void actionPerformed(GuiButton button, CallbackInfo callbackInfo) {
        switch (button.id) {
            case 1:
                ServerUtils.connectToLastServer();
                break;
            case 4:
                final CrackedAccount crackedAccount = new CrackedAccount();
                crackedAccount.setName(RandomUtils.randomString(RandomUtils.nextInt(5, 16)));
                crackedAccount.update();

                mc.session = new Session(crackedAccount.getSession().getUsername(), crackedAccount.getSession().getUuid(),
                        crackedAccount.getSession().getToken(), crackedAccount.getSession().getType());
                LiquidBounce.eventManager.callEvent(new SessionEvent());
                ServerUtils.connectToLastServer();
                break;
            case 5:
                AntiForge.enabled = !AntiForge.enabled;
                forgeBypassButton.displayString = "Bypass AntiForge: " + (AntiForge.enabled ? "On" : "Off");
                LiquidBounce.fileManager.saveConfig(LiquidBounce.fileManager.valuesConfig);
                break;
        }
    }

    @Override
    public void updateScreen() {
        if (AutoReconnect.INSTANCE.isEnabled()) {
            reconnectTimer++;
            if (reconnectTimer > AutoReconnect.INSTANCE.getDelay() / 50)
                ServerUtils.connectToLastServer();
        }
        
    }

    @Inject(method = "drawScreen",at = @At("TAIL"))
    public void drawScreen(int p_drawScreen_1_, int p_drawScreen_2_, float p_drawScreen_3_,CallbackInfo callbackInfo) {
        Fonts.font35.drawString("Player",width / 2 - 75,5 + Fonts.font40.FONT_HEIGHT + 5,new Color(175,175,175).getRGB(),true);
        Fonts.font35.drawString(mc.session.getUsername(),width / 2 + 75 - Fonts.font35.getStringWidth(mc.session.getUsername()),5 + Fonts.font40.FONT_HEIGHT + 5,new Color(255,255,255).getRGB(),true);
        Fonts.font35.drawString("Kills/Deaths",width / 2 - 75,5 + Fonts.font40.FONT_HEIGHT + 5 + Fonts.font35.FONT_HEIGHT + 2,new Color(175,175,175).getRGB(),true);
        Fonts.font35.drawString(SessionInfoUtils.getKills() + "/" + SessionInfoUtils.getDeaths(),width / 2 + 75 - Fonts.font35.getStringWidth(SessionInfoUtils.getKills() + "/" + SessionInfoUtils.getDeaths()),5 + Fonts.font40.FONT_HEIGHT + 5 + Fonts.font35.FONT_HEIGHT + 2,new Color(255,255,255).getRGB(),true);
        Fonts.font35.drawString("Win/Lose",width / 2 - 75,5 + Fonts.font40.FONT_HEIGHT + 5 + (Fonts.font35.FONT_HEIGHT + 2) * 2,new Color(175,175,175).getRGB(),true);
        if (SessionInfoUtils.isInSupportedServer()) {
            Fonts.font35.drawString(SessionInfoUtils.getWins() + "/" + SessionInfoUtils.getLoses(),width / 2 + 75 - Fonts.font35.getStringWidth(SessionInfoUtils.getWins() + "/" + SessionInfoUtils.getLoses()),5 + Fonts.font40.FONT_HEIGHT + 5 + (Fonts.font35.FONT_HEIGHT + 2) * 2,new Color(255,255,255).getRGB(),true);
        } else {
            Fonts.font35.drawString("Not supported server",width / 2 + 75 - Fonts.font35.getStringWidth("Not supported server"),5 + Fonts.font40.FONT_HEIGHT + 5 + (Fonts.font35.FONT_HEIGHT + 2) * 2,new Color(255,80,80).getRGB(),true);
        }
        Fonts.font35.drawString("PlayTime",width / 2 - 75,
                5 + Fonts.font40.FONT_HEIGHT + 5 + (Fonts.font35.FONT_HEIGHT + 2) * 3
                ,new Color(175,175,175).getRGB(),true);
        Fonts.font35.drawString(SessionInfoUtils.getPlayTimeH() + "h " + SessionInfoUtils.getPlayTimeM() + "m " +
                SessionInfoUtils.getPlayTimeS() + "s",width / 2 + 75 - Fonts.font35.getStringWidth(
                SessionInfoUtils.getPlayTimeH() + "h " + SessionInfoUtils.getPlayTimeM() + "m " + SessionInfoUtils.getPlayTimeS() + "s"
        ),5 + Fonts.font40.FONT_HEIGHT + 5 + (Fonts.font35.FONT_HEIGHT + 2) *
                3,new Color(255,255,255).getRGB(),true);


    }

    @Inject(method = "drawScreen", at = @At("RETURN"))
    private void drawScreen(CallbackInfo callbackInfo) {
        if (AutoReconnect.INSTANCE.isEnabled()) {
            this.updateReconnectButton();
        }

        
    }

    private void drawReconnectDelaySlider() {
        buttonList.add(autoReconnectDelaySlider =
                new GuiSlider(2, this.width / 2 + 2, this.height / 2 + field_175353_i / 2
                        + this.fontRendererObj.FONT_HEIGHT + 22, 98, 20, "AutoReconnect: ",
                        "ms", AutoReconnect.MIN, AutoReconnect.MAX, AutoReconnect.INSTANCE.getDelay(), false, true,
                        guiSlider -> {
                            AutoReconnect.INSTANCE.setDelay(guiSlider.getValueInt());

                            this.reconnectTimer = 0;
                            this.updateReconnectButton();
                            this.updateSliderText();
                        }));
    }

    private void updateSliderText() {
        if (this.autoReconnectDelaySlider == null)
            return;

        if (!AutoReconnect.INSTANCE.isEnabled()) {
            this.autoReconnectDelaySlider.displayString = "AutoReconnect: Off";
        } else {
            this.autoReconnectDelaySlider.displayString = "AutoReconnect: " + DECIMAL_FORMAT.format(AutoReconnect.INSTANCE.getDelay() / 1000.0) + "s";
        }
    }

    private void updateReconnectButton() {
        if (reconnectButton != null)
            reconnectButton.displayString = "Reconnect" + (AutoReconnect.INSTANCE.isEnabled() ? " (" + (AutoReconnect.INSTANCE.getDelay() / 1000 - reconnectTimer / 20) + ")" : "");
    }
}