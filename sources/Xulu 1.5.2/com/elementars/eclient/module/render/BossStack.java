// 
// Decompiled by Procyon v0.5.36
// 

package com.elementars.eclient.module.render;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import java.util.Iterator;
import com.elementars.eclient.util.Pair;
import java.util.HashMap;
import net.minecraft.world.BossInfo;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.gui.BossInfoClient;
import java.util.UUID;
import java.util.Map;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import java.util.Collection;
import java.util.ArrayList;
import java.util.Arrays;
import com.elementars.eclient.module.Category;
import net.minecraft.util.ResourceLocation;
import dev.xulu.settings.Value;
import com.elementars.eclient.module.Module;

public class BossStack extends Module
{
    public static Value<String> mode;
    private static Value<Float> scale;
    private static final ResourceLocation GUI_BARS_TEXTURES;
    
    public BossStack() {
        super("BossStack", "Stacks boss bars", 0, Category.RENDER, true);
        BossStack.mode = this.register(new Value<String>("Mode", this, "Stack", new ArrayList<String>(Arrays.asList("Remove", "Stack", "Minimize"))));
        BossStack.scale = this.register(new Value<Float>("Scale", this, 0.5f, 0.0f, 1.0f));
    }
    
    @Override
    public void onEnable() {
        MinecraftForge.EVENT_BUS.register((Object)this);
    }
    
    @Override
    public void onDisable() {
        MinecraftForge.EVENT_BUS.unregister((Object)this);
    }
    
    @SubscribeEvent
    public void render(final RenderGameOverlayEvent.Pre event) {
        if (BossStack.mode.getValue().equalsIgnoreCase("Minimize")) {
            final Map<UUID, BossInfoClient> map = (Map<UUID, BossInfoClient>)Minecraft.getMinecraft().ingameGUI.getBossOverlay().mapBossInfos;
            if (map == null) {
                return;
            }
            final ScaledResolution scaledresolution = new ScaledResolution(Minecraft.getMinecraft());
            final int i = scaledresolution.getScaledWidth();
            int j = 12;
            for (final Map.Entry<UUID, BossInfoClient> entry : map.entrySet()) {
                final BossInfoClient info = entry.getValue();
                final String text = info.getName().getFormattedText();
                final int k = (int)(i / BossStack.scale.getValue() / 2.0f - 91.0f);
                GL11.glScaled((double)BossStack.scale.getValue(), (double)BossStack.scale.getValue(), 1.0);
                if (!event.isCanceled()) {
                    GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
                    Minecraft.getMinecraft().getTextureManager().bindTexture(BossStack.GUI_BARS_TEXTURES);
                    Minecraft.getMinecraft().ingameGUI.getBossOverlay().render(k, j, (BossInfo)info);
                    Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(text, i / BossStack.scale.getValue() / 2.0f - Minecraft.getMinecraft().fontRenderer.getStringWidth(text) / 2, (float)(j - 9), 16777215);
                }
                GL11.glScaled(1.0 / BossStack.scale.getValue(), 1.0 / BossStack.scale.getValue(), 1.0);
                j += 10 + Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT;
            }
        }
        else if (BossStack.mode.getValue().equalsIgnoreCase("Stack")) {
            final Map<UUID, BossInfoClient> map = (Map<UUID, BossInfoClient>)Minecraft.getMinecraft().ingameGUI.getBossOverlay().mapBossInfos;
            final HashMap<String, Pair<BossInfoClient, Integer>> to = new HashMap<String, Pair<BossInfoClient, Integer>>();
            for (final Map.Entry<UUID, BossInfoClient> entry2 : map.entrySet()) {
                final String s = entry2.getValue().getName().getFormattedText();
                if (to.containsKey(s)) {
                    Pair<BossInfoClient, Integer> p = to.get(s);
                    p = new Pair<BossInfoClient, Integer>(p.getKey(), p.getValue() + 1);
                    to.put(s, p);
                }
                else {
                    final Pair<BossInfoClient, Integer> p = new Pair<BossInfoClient, Integer>(entry2.getValue(), 1);
                    to.put(s, p);
                }
            }
            final ScaledResolution scaledresolution2 = new ScaledResolution(Minecraft.getMinecraft());
            final int l = scaledresolution2.getScaledWidth();
            int m = 12;
            for (final Map.Entry<String, Pair<BossInfoClient, Integer>> entry3 : to.entrySet()) {
                String text = entry3.getKey();
                final BossInfoClient info2 = entry3.getValue().getKey();
                final int a = entry3.getValue().getValue();
                text = text + " x" + a;
                final int k2 = (int)(l / BossStack.scale.getValue() / 2.0f - 91.0f);
                GL11.glScaled((double)BossStack.scale.getValue(), (double)BossStack.scale.getValue(), 1.0);
                if (!event.isCanceled()) {
                    GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
                    Minecraft.getMinecraft().getTextureManager().bindTexture(BossStack.GUI_BARS_TEXTURES);
                    Minecraft.getMinecraft().ingameGUI.getBossOverlay().render(k2, m, (BossInfo)info2);
                    Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(text, l / BossStack.scale.getValue() / 2.0f - Minecraft.getMinecraft().fontRenderer.getStringWidth(text) / 2, (float)(m - 9), 16777215);
                }
                GL11.glScaled(1.0 / BossStack.scale.getValue(), 1.0 / BossStack.scale.getValue(), 1.0);
                m += 10 + Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT;
            }
        }
    }
    
    static {
        GUI_BARS_TEXTURES = new ResourceLocation("textures/gui/bars.png");
    }
}
