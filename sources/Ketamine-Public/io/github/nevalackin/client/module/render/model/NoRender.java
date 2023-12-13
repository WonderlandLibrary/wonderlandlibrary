package io.github.nevalackin.client.module.render.model;

import io.github.nevalackin.client.module.Category;
import io.github.nevalackin.client.module.Module;
import io.github.nevalackin.client.event.render.model.RenderLivingEntityEvent;
import io.github.nevalackin.client.property.BooleanProperty;
import io.github.nevalackin.client.util.player.TeamsUtil;
import io.github.nevalackin.homoBus.Listener;
import io.github.nevalackin.homoBus.annotations.EventLink;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;

public final class NoRender extends Module {

    private final BooleanProperty drawTeammatesProperty = new BooleanProperty("Draw Teammates", true);

    public NoRender() {
        super("No Render", Category.RENDER, Category.SubCategory.RENDER_MODEL);

        this.register(this.drawTeammatesProperty);
    }

    @EventLink(6)
    private final Listener<RenderLivingEntityEvent> onRenderLivingEntity = event -> {
        final EntityLivingBase entity = event.getEntity();

        if (!this.drawTeammatesProperty.getValue() &&
            entity instanceof EntityOtherPlayerMP &&
            TeamsUtil.TeamsMode.NAME.getComparator().isOnSameTeam(this.mc.thePlayer, (EntityPlayer) entity)) {
            event.setCancelled();
        }
    };

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }
}
