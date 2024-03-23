package dev.zprestige.prestige.client.module.impl.visuals;

import dev.zprestige.prestige.client.Prestige;
import dev.zprestige.prestige.client.event.EventListener;
import dev.zprestige.prestige.client.event.impl.NametagRenderEvent;
import dev.zprestige.prestige.client.event.impl.Render2DEvent;
import dev.zprestige.prestige.client.managers.AntiBotManager;
import dev.zprestige.prestige.client.module.Category;
import dev.zprestige.prestige.client.module.Module;
import dev.zprestige.prestige.client.setting.impl.BooleanSetting;
import dev.zprestige.prestige.client.ui.font.FontRenderer;
import dev.zprestige.prestige.client.util.impl.MathUtil;
import dev.zprestige.prestige.client.util.impl.RenderHelper;
import java.awt.Color;

import dev.zprestige.prestige.client.util.impl.RenderUtil;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;
import org.joml.Vector4d;

public class NameTags
extends Module {

    public BooleanSetting field240;

    public NameTags() {
        super("Name Tags", Category.Visual, "Renders a name tag above players");
        field240 = setting("Glow", true).description("Renders a glow around each nametag");
    }

    @EventListener
    public void method304(Render2DEvent render2DEvent) {
        Prestige.Companion.getFontManager().setMatrixStack(render2DEvent.getMatrixStack());
        RenderHelper.setMatrixStack(render2DEvent.getMatrixStack());
        float f = 125;
        float f2 = 20;
        for (PlayerEntity player : getMc().world.getPlayers()) {
            AntiBotManager antiBotManager = Prestige.Companion.getAntiBotManager();
            if (antiBotManager.isNotBot(player)) {
                double d = player.prevX + (player.getX() - player.prevX) * getMc().getTickDelta();
                double d2 = player.prevY + (player.getY() - player.prevY) * getMc().getTickDelta();
                double d3 = player.prevZ + (player.getZ() - player.prevZ) * getMc().getTickDelta();
                Vec3d vec3d = new Vec3d(d, d2 + 2, d3);
                Vector4d vector4d = null;
                vec3d = RenderUtil.worldSpaceToScreenSpace(new Vec3d(vec3d.x, vec3d.y, vec3d.z));
                if (vec3d.z > 0 && vec3d.z < 1) {
                    vector4d = new Vector4d(vec3d.x, vec3d.y, vec3d.z, 0);
                    vector4d.x = Math.min(vec3d.x, vector4d.x);
                    vector4d.y = Math.min(vec3d.y, vector4d.y);
                    vector4d.z = Math.max(vec3d.x, vector4d.z);
                }
                if (vector4d == null) continue;
                float f3 = (float) vector4d.x;
                float f4 = (float) vector4d.y - f2 * 2;
                float f5 = (float) vector4d.z;
                float f6 = (f5 - f3) / 2;
                float f7 = (f3 + f6 - f / 2) * 1;
                Color themeColor = Prestige.Companion.getModuleManager().getMenu().getColor().getObject();
                if (field240.getObject()) {
                    RenderUtil.renderShaderRect(render2DEvent.getMatrixStack(), themeColor, themeColor, themeColor, themeColor, f7, f4, 125, f2, 4, 10);
                }
                RenderUtil.renderRoundedRect(f7, f4, f7 + f, f4 + f2, new Color(12, 12, 12), 4);
                PlayerListEntry playerListEntry = getMc().getNetworkHandler().getPlayerListEntry(player.getUuid());
                if (getMc().getNetworkHandler().getPlayerListEntry(player.getUuid()) == null) continue;
                RenderUtil.renderTexturedQuad(playerListEntry.getSkinTexture(), f7 + 2.5f, f4 + 2.5f, 0, 15, 15, 15, 15, 120, 120);
                float f8 = MathUtil.findMiddleValue((player.getHealth() + player.getAbsorptionAmount()) / 20, 0, 1);
                float f9 = MathUtil.findMiddleValue(f8, 0, 1);
                float f10 = MathUtil.findMiddleValue(1 - f8, 0, 1);
                float f11 = 3;
                if (f8 < 7) {
                    f11 = 2;
                }
                if (f8 < 4) {
                    f11 = 1;
                }
                RenderUtil.renderRoundedRect(f7 + 20, f4 + f2 - 7.5f, f7 + f - 25 - (f - 25) * (1 - f8), f4 + f2 - 2.5f, new Color(f10, f9, 0), f11);
                FontRenderer font = Prestige.Companion.getFontManager().getFontRenderer();
                String string = String.valueOf((float) Math.ceil(player.getHealth() + player.getAbsorptionAmount()));
                font.drawString(string, f7 + f - font.getStringWidth(string) - 2.5f, f4 + 6, Color.WHITE);
                Color color;
                if (Prestige.Companion.getSocialsManager().isFriend(player.getEntityName())) {
                    color = new Color(0, 255, 255);
                } else {
                    color = Prestige.Companion.getSocialsManager().isEnemy(player.getEntityName()) ? new Color(255, 0, 0) : Color.WHITE;
                }
                font.drawString(player.getEntityName(), f7 + 20, f4 - 1, color);
            }
        }
    }

    @EventListener
    public void method307(NametagRenderEvent nametagRenderEvent) {
        if (Prestige.Companion.getAntiBotManager().isNotBot(nametagRenderEvent.getEntity())) {
            nametagRenderEvent.setCancelled();
        }
    }
}