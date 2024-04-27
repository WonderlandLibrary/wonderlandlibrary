package net.ccbluex.liquidbounce.ui.client.hud.element.elements;

import net.ccbluex.liquidbounce.ui.client.hud.element.Border;
import net.ccbluex.liquidbounce.ui.client.hud.element.Element;
import net.ccbluex.liquidbounce.ui.client.hud.element.ElementInfo;
import net.ccbluex.liquidbounce.ui.client.hud.element.Side;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.ccbluex.liquidbounce.value.ListValue;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.awt.*;

@ElementInfo(name = "Inventory")
public class Inventory extends Element {
    private final BoolValue titleValue = new BoolValue("Title",true);
    private final ListValue titlePositionValue = new ListValue("TitlePosition",new String[]{"Left","Middle","Right"},"Middle");
    private final IntegerValue backgroundAlphaValue = new IntegerValue("BackgroundAlpha",60,0,255);
    private final IntegerValue titleR = new IntegerValue("TitleR",65,0,255);
    private final IntegerValue titleG = new IntegerValue("TitleG",215,0,255);
    private final IntegerValue titleB = new IntegerValue("TitleB",255,0,255);
    private final BoolValue shadowValue = new BoolValue("Shadow",true);

    public Inventory() {
        super(90,125,1,new Side(Side.Horizontal.MIDDLE,Side.Vertical.DOWN));
    }

    @Nullable
    @Override
    public Border drawElement() {
        if (titleValue.get()) {
            RenderUtils.drawRect(0,0,180,54 + Fonts.font35.FONT_HEIGHT + 3,new Color(0,0,0,backgroundAlphaValue.get()));
        } else {
            RenderUtils.drawRect(0,0,180,57,new Color(0,0,0,backgroundAlphaValue.get()).getRGB());
        }

        if (titleValue.get()) {
            if (titlePositionValue.get().equalsIgnoreCase("Left")) {
                Fonts.font35.drawString("Inventory",3,3,new Color(titleR.get(),titleG.get(),titleB.get()).getRGB(),shadowValue.get());
            } else if (titlePositionValue.get().equalsIgnoreCase("Middle")) {
                Fonts.font35.drawCenteredString("Inventory",90,3,new Color(titleR.get(),titleG.get(),titleB.get()).getRGB(),shadowValue.get());
            } else if (titlePositionValue.get().equalsIgnoreCase("Right")) {
                Fonts.font35.drawString("Inventory",180 - Fonts.font35.getStringWidth("Inventory") - 3,3,new Color(titleR.get(),titleG.get(),titleB.get()).getRGB(),shadowValue.get());
            }
        }

        RenderHelper.enableGUIStandardItemLighting();
        drawItems(9,17,2,3,Fonts.font35);
        drawItems(18,26,2,18 + 3,Fonts.font35);
        drawItems(27,35,2,36 + 3,Fonts.font35);
        RenderHelper.disableStandardItemLighting();
        GlStateManager.enableAlpha();
        GlStateManager.disableBlend();
        GlStateManager.disableLighting();
        if (titleValue.get()) {
            return new Border(0,0,180,54 + Fonts.font35.FONT_HEIGHT);
        }
        return new Border(0,0,180,54);

    }

    public void drawItems(int startSlot,int endSlot,int x, int y, FontRenderer fontRenderer) {
        int renderX = x;
        for (int i = startSlot;;) {
            if (i > endSlot) {
                break;
            }
            ItemStack itemStack = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
            if (itemStack == null) {
                renderX += 20;
                i++;
                continue;
            }
            int renderY = y;
            if (titleValue.get()) {
                renderY += Fonts.font35.FONT_HEIGHT;
            }
            mc.getRenderItem().renderItemAndEffectIntoGUI(itemStack,renderX,renderY);
            mc.getRenderItem().renderItemOverlays(fontRenderer,itemStack,renderX,renderY);
            renderX += 20;
            i++;
        }
    }
}
