// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.module.impl.render;

import net.minecraft.client.entity.AbstractClientPlayer;
import dev.tenacity.utils.render.RenderUtil;
import dev.tenacity.utils.misc.MathUtils;
import net.minecraft.util.MathHelper;
import net.minecraft.client.gui.Gui;
import java.awt.Color;
import net.minecraft.client.renderer.GlStateManager;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import net.minecraft.entity.player.EntityPlayer;
import java.util.List;
import dev.tenacity.event.impl.render.Render2DEvent;
import dev.tenacity.Tenacity;
import dev.tenacity.module.Category;
import dev.tenacity.utils.objects.Dragging;
import dev.tenacity.module.Module;

public class PlayerList extends Module
{
    private final Dragging pos;
    
    public PlayerList() {
        super("PlayerList", "Player List", Category.RENDER, "Displays a list of players in your world");
        this.pos = Tenacity.INSTANCE.createDrag(this, "playerList", 4.0f, 30.0f);
    }
    
    @Override
    public void onRender2DEvent(final Render2DEvent event) {
        if (PlayerList.mc.thePlayer == null || PlayerList.mc.theWorld == null) {
            return;
        }
        final List<EntityPlayer> players = PlayerList.mc.theWorld.playerEntities.stream().filter(p -> p != null && !p.isDead).collect((Collector<? super Object, ?, List<EntityPlayer>>)Collectors.toList());
        final float height = (float)(35 + (players.size() - 1) * (PlayerList.tenacityFont16.getHeight() + 8));
        final float width = 175.0f;
        final float x = this.pos.getX();
        float y = this.pos.getY();
        this.pos.setWidth(width - 6.0f);
        this.pos.setHeight(height);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        Gui.drawRect2(x, y, width - 6.0f, height, new Color(0, 0, 0, 60).getRGB());
        Gui.drawRect2(x, y, width - 6.0f, 1.0, HUDMod.getClientColors().getFirst().getRGB());
        Gui.drawRect2(x + 4.0f, y + 17.0f, width - 14.0f, 0.5, -5592406);
        PlayerList.tenacityBoldFont22.drawString("Player List", x + 4.0f, y + 5.0f, -1, true);
        PlayerList.tenacityFont18.drawString(String.valueOf(players.size() + 1), x + width - PlayerList.tenacityFont18.getStringWidth(String.valueOf(players.size() + 1)) - 10.0f, y + 6.0f, -1, true);
        y += 18.0f;
        for (int i = 0; i < players.size(); ++i) {
            final EntityPlayer player = players.get(i);
            this.renderPlayer(player, i, x, y);
        }
    }
    
    private void renderPlayer(final EntityPlayer player, final int i, final float x, final float y) {
        final float height = (float)(PlayerList.tenacityFont16.getHeight() + 8);
        final float offset = i * height;
        final float healthPercent = MathHelper.clamp_float((player.getHealth() + player.getAbsorptionAmount()) / (player.getMaxHealth() + player.getAbsorptionAmount()), 0.0f, 1.0f);
        final Color healthColor = (healthPercent > 0.75) ? new Color(66, 246, 123) : ((healthPercent > 0.5) ? new Color(228, 255, 105) : ((healthPercent > 0.35) ? new Color(236, 100, 64) : new Color(255, 65, 68)));
        final String healthText = (int)MathUtils.round(healthPercent * 100.0f, 0.01) + "%";
        PlayerList.tenacityFont16.drawStringWithShadow("§f§l" + player.getName() + "§r " + healthText, x + 18.0f, y + offset + PlayerList.tenacityFont16.getMiddleOfBox(height), healthColor);
        final float headX = x + 4.0f;
        final float headWH = 32.0f;
        final float headY = y + offset + height / 2.0f - 6.0f;
        final float f = 0.35f;
        RenderUtil.resetColor();
        RenderUtil.scaleStart(headX, headY, f);
        PlayerList.mc.getTextureManager().bindTexture(((AbstractClientPlayer)player).getLocationSkin());
        Gui.drawTexturedModalRect(headX, headY, (int)headWH, (int)headWH, (int)headWH, (int)headWH);
        RenderUtil.scaleEnd();
        if (player == PlayerList.mc.thePlayer) {
            PlayerList.tenacityFont18.drawStringWithShadow("*", x + 139.0f - PlayerList.tenacityFont18.getStringWidth("*"), y + offset + 6.75f, HUDMod.getClientColors().getFirst().getRGB());
        }
    }
}
