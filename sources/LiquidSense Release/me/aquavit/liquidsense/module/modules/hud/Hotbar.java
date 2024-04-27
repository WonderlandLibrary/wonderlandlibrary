package me.aquavit.liquidsense.module.modules.hud;

import me.aquavit.liquidsense.event.EventTarget;
import me.aquavit.liquidsense.event.events.Render2DEvent;
import me.aquavit.liquidsense.module.Module;
import me.aquavit.liquidsense.module.ModuleCategory;
import me.aquavit.liquidsense.module.ModuleInfo;
import me.aquavit.liquidsense.ui.font.Fonts;
import me.aquavit.liquidsense.utils.item.HotbarUtil;
import me.aquavit.liquidsense.value.BoolValue;
import me.aquavit.liquidsense.value.ListValue;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

@ModuleInfo(name = "Hotbar", description = "Drawing 2D Hotbar", category = ModuleCategory.HUD, array = false)
public class Hotbar extends Module {

    public List<HotbarUtil> slot = new ArrayList<>();

    public Hotbar(double x, double y){
        this.setDefaultx(x);
        this.setDefaulty(y);

    }

    public Hotbar(){
        this(68,  92);

        for (int i = 0; i < 9 ; ++i) {
            HotbarUtil hotbarutils = new HotbarUtil();
            slot.add(hotbarutils);
        }
    }

    private final ListValue horizontal = new ListValue("Horizontal", new String[] {"Left", "Middle", "Right"}, "Middle") {
        @Override
        protected void onChanged(String oldValue, String newValue) {
            switch (newValue.toLowerCase()) {
                case "left":
                    setRenderx(hudx);
                    break;
                case "middle":
                    setRenderx((float) new ScaledResolution(mc).getScaledWidth() / 2 - hudx);
                    break;
                case "right":
                    setRenderx(new ScaledResolution(mc).getScaledWidth() - hudx);
                    break;
            }
        }
    };
    private final ListValue vertical = new ListValue("Vertical", new String[] {"Up", "Middle", "Down"}, "Down") {
        @Override
        protected void onChanged(String oldValue, String newValue) {
            switch (newValue.toLowerCase()) {
                case "up":
                    setRendery(hudy);
                    break;
                case "middle":
                    setRendery((float) new ScaledResolution(mc).getScaledHeight() / 2 - hudy);
                    break;
                case "down":
                    setRendery(new ScaledResolution(mc).getScaledHeight() - hudy);
                    break;
            }
        }
    };
    int hudx,hudy;

    public final BoolValue cancelHotbar = new BoolValue("CancelHotbar", true);

    @EventTarget
    public void onRender2D(final Render2DEvent event) {
        isLeft = horizontal.get().equalsIgnoreCase("Left");
        isUp = vertical.get().equalsIgnoreCase("Up");
        hudx = (int) getHUDX(horizontal.get().equalsIgnoreCase("Left"),
                horizontal.get().equalsIgnoreCase("Middle"),
                horizontal.get().equalsIgnoreCase("Right"));
        hudy = (int) getHUDY(vertical.get().equalsIgnoreCase("Up"),
                vertical.get().equalsIgnoreCase("Middle"),
                vertical.get().equalsIgnoreCase("Down"));

        GlStateManager.pushMatrix();
        GL11.glTranslated(hudx, hudy, 0.0);
        GlStateManager.enableRescaleNormal();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        RenderHelper.enableGUIStandardItemLighting();

        for(int i = 0; i < slot.size(); ++i) {
            HotbarUtil hotbarUtil = slot.get(i);
            float posY;

            if (i == mc.thePlayer.inventory.currentItem && mc.thePlayer.inventory.mainInventory[i] != null) {
                hotbarUtil.size = 1.5f;
                posY = -3;
            } else {
                hotbarUtil.size = 1.0f;
                posY = 0;
            }

            hotbarUtil.translate.translate(hotbarUtil.size, posY);
            float scale = hotbarUtil.translate.getX();
            GlStateManager.pushMatrix();
            GlStateManager.scale(scale, scale, scale);
            float x = i * 25f / scale - scale * 2f;
            hotbarUtil.renderXHotbarItem(i, x + scale, hotbarUtil.translate.getY() - scale / 2f - 2f / scale, mc.timer.renderPartialTicks);
            GlStateManager.popMatrix();

            mc.getRenderItem().renderItemOverlays(scale == 1f ? Fonts.font15 : Fonts.font18, mc.thePlayer.inventory.mainInventory[i], (int) (i * 25f), -3);
        }

        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableRescaleNormal();
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();

        drawBorder(hudx - 9, hudy - 10, 238F, 29F);
    }
}
