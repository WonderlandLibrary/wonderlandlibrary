package io.github.raze.modules.collection.visual;

import io.github.raze.Raze;
import io.github.raze.events.collection.visual.EventRender3D;
import io.github.nevalackin.radbus.Listen;
import io.github.raze.modules.system.AbstractModule;
import io.github.raze.modules.system.information.ModuleCategory;
import io.github.raze.settings.collection.ArraySetting;
import io.github.raze.utilities.collection.math.MathUtil;
import io.github.raze.utilities.collection.visual.esp.EntityESPUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

public class ImageESP extends AbstractModule {

    private final ArraySetting mode;

    public ImageESP() {
        super("ImageEsp", "Renders an image on players.", ModuleCategory.VISUAL);

        Raze.INSTANCE.managerRegistry.settingRegistry.add(

                mode = new ArraySetting(this, "Mode", "Dream", "Dream", "Cat Girl")

        );
    }

    @Listen
    public void onRender(EventRender3D eventRender3D) {
        ResourceLocation imageESPImage = new ResourceLocation("raze/esp/" + mode.get() + ".png");

        for (final EntityPlayer player : mc.theWorld.playerEntities) {
            if (player.isEntityAlive() && player != mc.thePlayer && !player.isInvisible()) {
                double renderPosX = MathUtil.interpolate(player.posX, player.lastTickPosX) - mc.getRenderManager().renderPosX;
                double renderPosY = MathUtil.interpolate(player.posY, player.lastTickPosY) - mc.getRenderManager().renderPosY;
                double renderPosZ = MathUtil.interpolate(player.posZ, player.lastTickPosZ) - mc.getRenderManager().renderPosZ;

                EntityESPUtil.renderImageESP(player, imageESPImage, renderPosX, renderPosY, renderPosZ);
            }
        }
    }
}
