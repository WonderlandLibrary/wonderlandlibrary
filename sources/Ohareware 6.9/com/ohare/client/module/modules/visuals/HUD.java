package com.ohare.client.module.modules.visuals;

import com.ohare.client.Client;
import com.ohare.client.event.events.game.FullScreenEvent;
import com.ohare.client.event.events.game.ResizeEvent;
import com.ohare.client.event.events.game.TickEvent;
import com.ohare.client.event.events.input.KeyPressEvent;
import com.ohare.client.event.events.render.Render2DEvent;
import com.ohare.client.gui.GuiHud;
import com.ohare.client.gui.hudsettings.HudSettings;
import com.ohare.client.module.Module;
import com.ohare.client.utils.font.Fonts;
import com.ohare.client.utils.value.impl.BooleanValue;
import com.ohare.client.utils.value.impl.NumberValue;
import dorkbox.messageBus.annotations.Subscribe;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

/**
 * made by oHare for Client
 *
 * @since 5/29/2019
 **/
public class HUD extends Module {

    public static NumberValue<Float> hue = new NumberValue("Hue", 1.0F, 0.001F, 1.0F, 0.001F);
    public BooleanValue staticRainbow = new BooleanValue("Static Rainbow", false);
    private BooleanValue notifications = new BooleanValue("Notifications", true);
    private BooleanValue armor = new BooleanValue("Armor", true);

    public HUD() {
        super("Hud", Category.VISUALS, -1);
        setDescription("UI");
        addValues(staticRainbow, notifications, armor, hue);
        setHidden(true);
    }

    @Override
    public void onEnable() {
        Client.INSTANCE.getHudCompManager().getHudMap().values().forEach(hudComp -> hudComp.onEnable());
    }

    @Override
    public void onDisable() {
        Client.INSTANCE.getHudCompManager().getHudMap().values().forEach(hudComp -> hudComp.onDisable());
    }

    @Subscribe
    public void onRender2D(Render2DEvent event) {
        if (mc.gameSettings.showDebugInfo || mc.thePlayer == null || (mc.currentScreen != null && (mc.currentScreen instanceof GuiHud || mc.currentScreen instanceof HudSettings)))
            return;
        Client.INSTANCE.getHudCompManager().getHudMap().values().forEach(hudComp -> {
            if (hudComp.isEnabled()) hudComp.onRender(event.getSR());
        });
        if (armor.isEnabled()) drawArmor(event.getSR());
        if (notifications.isEnabled()) drawNotifications(event.getSR());
    }

    @Subscribe
    public void onScreenResize(ResizeEvent event) {
        Client.INSTANCE.getHudCompManager().getHudMap().values().forEach(hudComp -> {
            if (hudComp.isEnabled()) {
                hudComp.onResize(event.getSr());
            }
        });
    }

    @Subscribe
    public void onFullScreen(FullScreenEvent event) {
        Client.INSTANCE.getHudCompManager().getHudMap().values().forEach(hudComp -> {
            if (hudComp.isEnabled()) {
                hudComp.onFullScreen(event.getWidth(), event.getHeight());
            }
        });
    }

    @Subscribe
    public void onKeyPress(KeyPressEvent event) {
        Client.INSTANCE.getHudCompManager().getHudMap().values().forEach(hudComp -> {
            if (hudComp.isEnabled()) {
                hudComp.onKey(event.getKey());
            }
        });
    }

    private void drawArmor(ScaledResolution sr) {
        if (mc.thePlayer.capabilities.isCreativeMode) return;
        GL11.glPushMatrix();
        int divide = 0;
        RenderItem ir = new RenderItem(mc.getTextureManager(), mc.modelManager);
        List<ItemStack> stuff = new ArrayList<>();
        int split = 15;
        for (int index = 3; index >= 0; index--) {
            ItemStack armor = mc.thePlayer.inventory.armorInventory[index];
            if (armor != null) stuff.add(armor);
        }
        for (ItemStack everything : stuff) {
            divide++;
            boolean half = divide > 2;
            int x = half ? (sr.getScaledWidth() / 2) + 93 : (sr.getScaledWidth() / 2) - 110;
            int y = split + sr.getScaledHeight() - (half ? 48 + 28 : 48);
            if (mc.theWorld != null) {
                RenderHelper.disableStandardItemLighting();
                ir.renderItemIntoGUI(everything, x, y);
                ir.renderItemOverlays(mc.fontRendererObj, everything, x, y);
                RenderHelper.enableGUIStandardItemLighting();
                split += 15;
            }
            int damage = everything.getMaxDamage() - everything.getItemDamage();
            GlStateManager.enableAlpha();
            GlStateManager.disableCull();
            GlStateManager.disableBlend();
            GlStateManager.disableLighting();
            GlStateManager.clear(256);
            Fonts.hudfont.drawStringWithShadow(String.valueOf(damage), x + (half ? 18 : -18), y + 5, 0xFFFFFFFF);
        }
        GL11.glPopMatrix();
    }

    private void drawNotifications(ScaledResolution sr) {
        float y = sr.getScaledHeight() - 18;
        for (int i = 0; i < Client.INSTANCE.getNotificationManager().getNotifications().size(); i++) {
            Client.INSTANCE.getNotificationManager().getNotifications().get(i).draw(y);
            y -= 18;
        }
    }
}