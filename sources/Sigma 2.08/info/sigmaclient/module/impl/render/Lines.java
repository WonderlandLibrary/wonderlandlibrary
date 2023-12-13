/**
 * Time: 6:29:26 PM
 * Date: Jan 3, 2017
 * Creator: cool1
 */
package info.sigmaclient.module.impl.render;

import info.sigmaclient.event.Event;
import info.sigmaclient.event.RegisterEvent;
import info.sigmaclient.event.impl.EventRender3D;
import info.sigmaclient.module.Module;
import info.sigmaclient.module.data.ModuleData;
import info.sigmaclient.module.data.Setting;
import info.sigmaclient.util.RenderingUtil;
import info.sigmaclient.util.render.Colors;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.tileentity.TileEntitySign;

/**
 * @author cool1
 */
public class Lines extends Module {

    public static final String CHEST = "CHEST";
    public static final String SIGN = "SIGN";
    public static final String SPAWNER = "SPAWNER";
    public static final String PLAYER = "PLAYER";

    /**
     * @param data
     */
    public Lines(ModuleData data) {
        super(data);
        settings.put(SIGN, new Setting<>(SIGN, false, "Draw lines at signs."));
        settings.put(CHEST, new Setting<>(CHEST, false, "Draw lines at chests."));
        settings.put(PLAYER, new Setting<>(PLAYER, true, "Draw lines at players."));
        settings.put(SPAWNER, new Setting<>(SPAWNER, false, "Draw lines at spawners."));
    }

    /* (non-Javadoc)
     * @see EventListener#onEvent(Event)
     */
    @Override
    @RegisterEvent(events = {EventRender3D.class})
    public void onEvent(Event event) {
        EventRender3D er = (EventRender3D) event;
        for (Object o : mc.theWorld.getLoadedEntityList()) {
            if (o instanceof EntityPlayer && ((Boolean) settings.get(PLAYER).getValue())) {
                EntityPlayer ent = (EntityPlayer) o;
                if (ent != mc.thePlayer && !ent.isInvisible()) {
                    int color = Colors.getColor(190, 40, 87);
                    float posX = (float) ((float) (ent.lastTickPosX + (ent.posX - ent.lastTickPosX) * er.renderPartialTicks) - RenderManager.renderPosX);
                    float posY = (float) ((float) (ent.lastTickPosY + (ent.posY - ent.lastTickPosY) * er.renderPartialTicks) - RenderManager.renderPosY);
                    float posZ = (float) ((float) (ent.lastTickPosZ + (ent.posZ - ent.lastTickPosZ) * er.renderPartialTicks) - RenderManager.renderPosZ);
                    final boolean bobbing = mc.gameSettings.viewBobbing;
                    mc.gameSettings.viewBobbing = false;
                    RenderingUtil.draw3DLine(posX, posY, posZ, color);
                    mc.gameSettings.viewBobbing = bobbing;
                }
            }
        }
        for (Object o : mc.theWorld.loadedTileEntityList) {
            int color = -1;
            if (o instanceof TileEntityChest && ((Boolean) settings.get(CHEST).getValue())) {
                TileEntityChest ent = (TileEntityChest) o;
                color = Colors.getColor(114, 0, 187);
                float posX = (float) ((float) ent.getPos().getX() - RenderManager.renderPosX);
                float posY = (float) ((float) ent.getPos().getY() - RenderManager.renderPosY);
                float posZ = (float) ((float) ent.getPos().getZ() - RenderManager.renderPosZ);
                final boolean bobbing = mc.gameSettings.viewBobbing;
                mc.gameSettings.viewBobbing = false;
                RenderingUtil.draw3DLine(posX, posY, posZ, color);
                mc.gameSettings.viewBobbing = bobbing;
            }

            if (o instanceof TileEntityMobSpawner && ((Boolean) settings.get(SPAWNER).getValue())) {
                TileEntityMobSpawner ent = (TileEntityMobSpawner) o;
                color = Colors.getColor(255, 156, 0);
                float posX = (float) ((float) ent.getPos().getX() - RenderManager.renderPosX);
                float posY = (float) ((float) ent.getPos().getY() - RenderManager.renderPosY);
                float posZ = (float) ((float) ent.getPos().getZ() - RenderManager.renderPosZ);
                final boolean bobbing = mc.gameSettings.viewBobbing;
                mc.gameSettings.viewBobbing = false;
                RenderingUtil.draw3DLine(posX, posY, posZ, color);
                mc.gameSettings.viewBobbing = bobbing;
            }

            if (o instanceof TileEntitySign && ((Boolean) settings.get(SIGN).getValue())) {
                TileEntitySign ent = (TileEntitySign) o;
                color = Colors.getColor(130, 162, 0);
                float posX = (float) ((float) ent.getPos().getX() - RenderManager.renderPosX);
                float posY = (float) ((float) ent.getPos().getY() - RenderManager.renderPosY);
                float posZ = (float) ((float) ent.getPos().getZ() - RenderManager.renderPosZ);
                final boolean bobbing = mc.gameSettings.viewBobbing;
                mc.gameSettings.viewBobbing = false;
                RenderingUtil.draw3DLine(posX, posY, posZ, color);
                mc.gameSettings.viewBobbing = bobbing;
            }

        }
    }

}
