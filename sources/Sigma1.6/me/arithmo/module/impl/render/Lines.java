/*
 * Decompiled with CFR 0_122.
 */
package me.arithmo.module.impl.render;

import java.util.List;
import me.arithmo.event.Event;
import me.arithmo.event.RegisterEvent;
import me.arithmo.event.impl.EventRender3D;
import me.arithmo.module.Module;
import me.arithmo.module.data.ModuleData;
import me.arithmo.module.data.Setting;
import me.arithmo.module.data.SettingsMap;
import me.arithmo.util.RenderingUtil;
import me.arithmo.util.render.Colors;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.BlockPos;

public class Lines
extends Module {
    public static final String CHEST = "CHEST";
    public static final String SIGN = "SIGN";
    public static final String SPAWNER = "SPAWNER";
    public static final String PLAYER = "PLAYER";

    public Lines(ModuleData data) {
        super(data);
        this.settings.put("SIGN", new Setting<Boolean>("SIGN", false, "Draw lines at signs."));
        this.settings.put("CHEST", new Setting<Boolean>("CHEST", true, "Draw lines at chests."));
        this.settings.put("PLAYER", new Setting<Boolean>("PLAYER", false, "Draw lines at players."));
        this.settings.put("SPAWNER", new Setting<Boolean>("SPAWNER", false, "Draw lines at spawners."));
    }

    @RegisterEvent(events={EventRender3D.class})
    public void onEvent(Event event) {
        boolean bobbing;
        float posY;
        float posX;
        float posZ;
        EventRender3D er = (EventRender3D)event;
        for (Object o : Lines.mc.theWorld.getLoadedEntityList()) {
            EntityPlayer ent;
            if (!(o instanceof EntityPlayer) || !((Boolean)((Setting)this.settings.get("PLAYER")).getValue()).booleanValue() || (ent = (EntityPlayer)o) == Lines.mc.thePlayer) continue;
            int color = Colors.getColor(190, 40, 87);
            posX = (float)((double)((float)ent.posX) - RenderManager.renderPosX);
            posY = (float)((double)((float)ent.posY) - RenderManager.renderPosY);
            posZ = (float)((double)((float)ent.posZ) - RenderManager.renderPosZ);
            bobbing = Lines.mc.gameSettings.viewBobbing;
            Lines.mc.gameSettings.viewBobbing = false;
            RenderingUtil.draw3DLine(posX, posY, posZ, color);
            Lines.mc.gameSettings.viewBobbing = bobbing;
        }
        for (Object o : Lines.mc.theWorld.loadedTileEntityList) {
            TileEntity ent;
            int color = -1;
            if (o instanceof TileEntityChest && ((Boolean)((Setting)this.settings.get("CHEST")).getValue()).booleanValue()) {
                TileEntityChest ent2 = (TileEntityChest)o;
                color = Colors.getColor(114, 0, 187);
                posX = (float)((double)ent2.getPos().getX() - RenderManager.renderPosX);
                posY = (float)((double)ent2.getPos().getY() - RenderManager.renderPosY);
                posZ = (float)((double)ent2.getPos().getZ() - RenderManager.renderPosZ);
                bobbing = Lines.mc.gameSettings.viewBobbing;
                Lines.mc.gameSettings.viewBobbing = false;
                RenderingUtil.draw3DLine(posX, posY, posZ, color);
                Lines.mc.gameSettings.viewBobbing = bobbing;
            }
            if (o instanceof TileEntityChest && ((Boolean)((Setting)this.settings.get("CHEST")).getValue()).booleanValue()) {
                ent = (TileEntityChest)o;
                color = Colors.getColor(114, 0, 187);
                posX = (float)((double)ent.getPos().getX() - RenderManager.renderPosX);
                posY = (float)((double)ent.getPos().getY() - RenderManager.renderPosY);
                posZ = (float)((double)ent.getPos().getZ() - RenderManager.renderPosZ);
                bobbing = Lines.mc.gameSettings.viewBobbing;
                Lines.mc.gameSettings.viewBobbing = false;
                RenderingUtil.draw3DLine(posX, posY, posZ, color);
                Lines.mc.gameSettings.viewBobbing = bobbing;
            }
            if (o instanceof TileEntityMobSpawner && ((Boolean)((Setting)this.settings.get("SPAWNER")).getValue()).booleanValue()) {
                ent = (TileEntityMobSpawner)o;
                color = Colors.getColor(255, 156, 0);
                posX = (float)((double)ent.getPos().getX() - RenderManager.renderPosX);
                posY = (float)((double)ent.getPos().getY() - RenderManager.renderPosY);
                posZ = (float)((double)ent.getPos().getZ() - RenderManager.renderPosZ);
                bobbing = Lines.mc.gameSettings.viewBobbing;
                Lines.mc.gameSettings.viewBobbing = false;
                RenderingUtil.draw3DLine(posX, posY, posZ, color);
                Lines.mc.gameSettings.viewBobbing = bobbing;
            }
            if (!(o instanceof TileEntitySign) || !((Boolean)((Setting)this.settings.get("SIGN")).getValue()).booleanValue()) continue;
            ent = (TileEntitySign)o;
            color = Colors.getColor(130, 162, 0);
            posX = (float)((double)ent.getPos().getX() - RenderManager.renderPosX);
            posY = (float)((double)ent.getPos().getY() - RenderManager.renderPosY);
            posZ = (float)((double)ent.getPos().getZ() - RenderManager.renderPosZ);
            bobbing = Lines.mc.gameSettings.viewBobbing;
            Lines.mc.gameSettings.viewBobbing = false;
            RenderingUtil.draw3DLine(posX, posY, posZ, color);
            Lines.mc.gameSettings.viewBobbing = bobbing;
        }
    }
}

