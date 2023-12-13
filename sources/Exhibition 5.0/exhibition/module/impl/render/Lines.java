// 
// Decompiled by Procyon v0.5.30
// 

package exhibition.module.impl.render;

import java.util.HashMap;
import exhibition.event.RegisterEvent;
import java.util.Iterator;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.tileentity.TileEntityMobSpawner;
import exhibition.util.RenderingUtil;
import net.minecraft.client.renderer.entity.RenderManager;
import exhibition.util.render.Colors;
import net.minecraft.tileentity.TileEntityChest;
import exhibition.event.impl.EventRender3D;
import exhibition.event.Event;
import exhibition.module.data.Setting;
import exhibition.module.data.ModuleData;
import exhibition.module.Module;

public class Lines extends Module
{
    public static final String CHEST = "CHEST";
    public static final String SIGN = "SIGN";
    public static final String SPAWNER = "SPAWNER";
    public static final String PLAYER = "PLAYER";
    
    public Lines(final ModuleData data) {
        super(data);
        ((HashMap<String, Setting<Boolean>>)this.settings).put("SIGN", new Setting<Boolean>("SIGN", false, "Draw lines at signs."));
        ((HashMap<String, Setting<Boolean>>)this.settings).put("CHEST", new Setting<Boolean>("CHEST", true, "Draw lines at chests."));
        ((HashMap<String, Setting<Boolean>>)this.settings).put("SPAWNER", new Setting<Boolean>("SPAWNER", false, "Draw lines at spawners."));
    }
    
    @RegisterEvent(events = { EventRender3D.class })
    @Override
    public void onEvent(final Event event) {
        final EventRender3D er = (EventRender3D)event;
        for (final Object o : Lines.mc.theWorld.loadedTileEntityList) {
            int color = -1;
            if (o instanceof TileEntityChest && ((HashMap<K, Setting<Boolean>>)this.settings).get("CHEST").getValue()) {
                final TileEntityChest ent = (TileEntityChest)o;
                color = Colors.getColor(114, 0, 187);
                final float posX = (float)(ent.getPos().getX() - RenderManager.renderPosX);
                final float posY = (float)(ent.getPos().getY() - RenderManager.renderPosY);
                final float posZ = (float)(ent.getPos().getZ() - RenderManager.renderPosZ);
                final boolean bobbing = Lines.mc.gameSettings.viewBobbing;
                Lines.mc.gameSettings.viewBobbing = false;
                RenderingUtil.draw3DLine(posX, posY, posZ, color);
                Lines.mc.gameSettings.viewBobbing = bobbing;
            }
            if (o instanceof TileEntityChest && ((HashMap<K, Setting<Boolean>>)this.settings).get("CHEST").getValue()) {
                final TileEntityChest ent = (TileEntityChest)o;
                color = Colors.getColor(114, 0, 187);
                final float posX = (float)(ent.getPos().getX() - RenderManager.renderPosX);
                final float posY = (float)(ent.getPos().getY() - RenderManager.renderPosY);
                final float posZ = (float)(ent.getPos().getZ() - RenderManager.renderPosZ);
                final boolean bobbing = Lines.mc.gameSettings.viewBobbing;
                Lines.mc.gameSettings.viewBobbing = false;
                RenderingUtil.draw3DLine(posX, posY, posZ, color);
                Lines.mc.gameSettings.viewBobbing = bobbing;
            }
            if (o instanceof TileEntityMobSpawner && ((HashMap<K, Setting<Boolean>>)this.settings).get("SPAWNER").getValue()) {
                final TileEntityMobSpawner ent2 = (TileEntityMobSpawner)o;
                color = Colors.getColor(255, 156, 0);
                final float posX = (float)(ent2.getPos().getX() - RenderManager.renderPosX);
                final float posY = (float)(ent2.getPos().getY() - RenderManager.renderPosY);
                final float posZ = (float)(ent2.getPos().getZ() - RenderManager.renderPosZ);
                final boolean bobbing = Lines.mc.gameSettings.viewBobbing;
                Lines.mc.gameSettings.viewBobbing = false;
                RenderingUtil.draw3DLine(posX, posY, posZ, color);
                Lines.mc.gameSettings.viewBobbing = bobbing;
            }
            if (o instanceof TileEntitySign && ((HashMap<K, Setting<Boolean>>)this.settings).get("SIGN").getValue()) {
                final TileEntitySign ent3 = (TileEntitySign)o;
                color = Colors.getColor(130, 162, 0);
                final float posX = (float)(ent3.getPos().getX() - RenderManager.renderPosX);
                final float posY = (float)(ent3.getPos().getY() - RenderManager.renderPosY);
                final float posZ = (float)(ent3.getPos().getZ() - RenderManager.renderPosZ);
                final boolean bobbing = Lines.mc.gameSettings.viewBobbing;
                Lines.mc.gameSettings.viewBobbing = false;
                RenderingUtil.draw3DLine(posX, posY, posZ, color);
                Lines.mc.gameSettings.viewBobbing = bobbing;
            }
        }
    }
}
