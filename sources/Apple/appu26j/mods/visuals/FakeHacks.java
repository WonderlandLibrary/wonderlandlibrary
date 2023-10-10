package appu26j.mods.visuals;

import appu26j.Cache;
import appu26j.interfaces.ModInterface;
import appu26j.mods.Category;
import appu26j.mods.Mod;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

@ModInterface(name = "Fake Hacks", description = "Displays a fake vape overlay.", category = Category.VISUALS)
public class FakeHacks extends Mod
{
    @Override
    public void onRender()
    {
        super.onRender();
        this.mc.getTextureManager().bindTexture(new ResourceLocation("fakehacks.png"));
        GlStateManager.pushMatrix();
        float scale = 1F / Cache.getSR().getScaleFactor();
        GlStateManager.scale(scale, scale, scale);
        GlStateManager.color(1, 1, 1, 1);
        GlStateManager.enableBlend();
        GlStateManager.enableAlpha();
        Gui.drawModalRectWithCustomSizedTexture(0, 0, 0, 0, 162, 131, 162, 131);
        GlStateManager.popMatrix();
    }
}
