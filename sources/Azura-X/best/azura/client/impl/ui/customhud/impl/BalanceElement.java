package best.azura.client.impl.ui.customhud.impl;

import best.azura.client.api.ui.customhud.Element;
import best.azura.client.api.ui.customhud.corner.Side;
import best.azura.client.impl.Client;
import best.azura.client.impl.module.impl.render.HUD;
import best.azura.client.impl.ui.font.Fonts;
import best.azura.client.util.color.ColorUtil;
import best.azura.client.util.other.BalanceUtil;
import best.azura.eventbus.core.EventPriority;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;

public class BalanceElement extends Element {

    public BalanceElement() {
        super("Balance", 3, 3, 200, 20);
    }

    @Override
    public void render() {
        fitInScreen(mc.displayWidth, mc.displayHeight);
        final HUD hud = (HUD) Client.INSTANCE.getModuleManager().getModule(HUD.class);
        if (hud == null) return;
        final FontRenderer fr = hud.useClientFont.getObject() ? Fonts.INSTANCE.hudFont : mc.fontRendererObj;
        final String text = "Balance: " + BalanceUtil.getBalance() + "ms";
        final Side side = getSide();
        if (!hud.useClientFont.getObject())
            GlStateManager.scale(2, 2, 1);
        if (side == Side.LEFT)
            fr.drawStringWithShadow(text, getX() / (hud.useClientFont.getObject() ? 1 : 2), getY() / (hud.useClientFont.getObject() ? 1 : 2), ColorUtil.getLastHudColor().getRGB());
        if (side == Side.RIGHT)
            fr.drawStringWithShadow(text, (getX() + getWidth()) / (hud.useClientFont.getObject() ? 1 : 2) - fr.getStringWidth(text), getY() / (hud.useClientFont.getObject() ? 1 : 2), ColorUtil.getLastHudColor().getRGB());
        if (!hud.useClientFont.getObject())
            GlStateManager.scale(1.0 / 2, 1.0 / 2, 1);
    }

    @Override
    public Element copy() {
        return new BalanceElement();
    }
}