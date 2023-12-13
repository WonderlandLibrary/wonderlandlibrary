package io.github.nevalackin.client.module.misc.player;

import io.github.nevalackin.client.module.Category;
import io.github.nevalackin.client.module.Module;
import io.github.nevalackin.client.event.player.ServerSetPosLookEvent;
import io.github.nevalackin.homoBus.Listener;
import io.github.nevalackin.homoBus.annotations.EventLink;

public final class NoRotate extends Module {

    public NoRotate() {
        super("No Rotate", Category.MISC, Category.SubCategory.MISC_PLAYER);
    }

    @EventLink
    private final Listener<ServerSetPosLookEvent> onSetPosLook = event -> {
        // Set the player yaw and pitch
        event.setYaw(this.mc.thePlayer.rotationYaw);
        event.setPitch(this.mc.thePlayer.rotationPitch);
    };

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }
}