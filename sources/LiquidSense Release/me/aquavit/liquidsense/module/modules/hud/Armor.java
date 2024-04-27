package me.aquavit.liquidsense.module.modules.hud;

import me.aquavit.liquidsense.event.EventTarget;
import me.aquavit.liquidsense.event.events.Render2DEvent;
import me.aquavit.liquidsense.event.events.UpdateEvent;
import me.aquavit.liquidsense.module.Module;
import me.aquavit.liquidsense.module.ModuleCategory;
import me.aquavit.liquidsense.module.ModuleInfo;
import me.aquavit.liquidsense.utils.timer.TimeUtils;
import me.aquavit.liquidsense.value.ListValue;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL11;

@ModuleInfo(name = "Armor", description = "Drawing 2D Armor", category = ModuleCategory.HUD, array = false)
public class Armor extends Module {
    public Armor(double x, double y){
        this.setDefaultx(x);
        this.setDefaulty(y);

    }

    public Armor(){
        this(-13,  59);
    }

    private final ListValue horizontal = new ListValue("Horizontal", new String[] {"Left", "Middle", "Right"}, "Middle") {
        @Override
        protected void onChanged(String oldValue, String newValue) {
            switch (newValue.toLowerCase()) {
                case "left":
                    setRenderx(x);
                    break;
                case "middle":
                    setRenderx((float) new ScaledResolution(mc).getScaledWidth() / 2 - x);
                    break;
                case "right":
                    setRenderx(new ScaledResolution(mc).getScaledWidth() - x);
                    break;
            }
        }
    };
    private final ListValue vertical = new ListValue("Vertical", new String[] {"Up", "Middle", "Down"}, "Down") {
        @Override
        protected void onChanged(String oldValue, String newValue) {
            switch (newValue.toLowerCase()) {
                case "up":
                    setRendery(y);
                    break;
                case "middle":
                    setRendery((float) new ScaledResolution(mc).getScaledHeight() / 2 - y);
                    break;
                case "down":
                    setRendery(new ScaledResolution(mc).getScaledHeight() - y);
                    break;
            }
        }
    };
    int x,y;

    private final ListValue modeValue = new ListValue("Alignment", new String[] {"Horizontal", "Vertical"}, "Horizontal");

    @EventTarget
    public void onRender2D(final Render2DEvent event) {
        int count = 0;
        isLeft = horizontal.get().equalsIgnoreCase("Left");
        isUp = vertical.get().equalsIgnoreCase("Up");
        x = (int) getHUDX(horizontal.get().equalsIgnoreCase("Left"),
                horizontal.get().equalsIgnoreCase("Middle"),
                horizontal.get().equalsIgnoreCase("Right"));
        y = (int) getHUDY(vertical.get().equalsIgnoreCase("Up"),
                vertical.get().equalsIgnoreCase("Middle"),
                vertical.get().equalsIgnoreCase("Down"));

        if (mc.playerController.isNotCreative()) {

            GL11.glPushMatrix();

            RenderItem renderItem = mc.getRenderItem();

            int renderx = x;

            int rendery = y;

            for (int n = 3; n >= 0; n--) {
                ItemStack stack = mc.thePlayer.inventory.armorInventory[n];
                if (stack == null)continue;
                renderItem.renderItemIntoGUI(stack, renderx, rendery);
                renderItem.renderItemOverlays(mc.fontRendererObj, stack, renderx, rendery);
                count = count + 1;
                if (modeValue.get().equalsIgnoreCase("Horizontal")) renderx += 18;
                else if (modeValue.get().equalsIgnoreCase("Vertical")) rendery += 18;
            }


            GlStateManager.enableAlpha();
            GlStateManager.disableBlend();
            GlStateManager.disableLighting();
            GlStateManager.disableCull();
            GL11.glPopMatrix();

            if (count != 0) drawBorder(x, y,
                    modeValue.get().equalsIgnoreCase("Horizontal") ? 18 * count : 17F,
                    modeValue.get().equalsIgnoreCase("Horizontal") ? 17F : 18 * count);
        }
    }
}
