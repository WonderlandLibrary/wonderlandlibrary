package dev.zprestige.prestige.client.module.impl.misc;

import dev.zprestige.prestige.client.Prestige;
import dev.zprestige.prestige.client.event.EventListener;
import dev.zprestige.prestige.client.event.impl.TickEvent;
import dev.zprestige.prestige.client.managers.SocialsManager;
import dev.zprestige.prestige.client.module.Category;
import dev.zprestige.prestige.client.module.Module;
import net.minecraft.client.util.Window;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.hit.EntityHitResult;
import org.lwjgl.glfw.GLFW;

public class MiddleClickFriend extends Module {
    public MiddleClickFriend() {
        super("Middle Click Friend", Category.Misc, "Adds friends when middle clicking on them");
    }

    @EventListener
    public void event(TickEvent event) {
        if (getMc().currentScreen == null) {
            Window window = getMc().getWindow();
            if (GLFW.glfwGetKey(window.getHandle(), 2) == 1 && getMc().crosshairTarget instanceof EntityHitResult hitResult) {
                if (hitResult.getEntity() instanceof PlayerEntity) {
                    String name = hitResult.getEntity().getEntityName();
                    SocialsManager socialsManager = Prestige.Companion.getSocialsManager();
                    if (socialsManager.isFriend(name)) {
                        socialsManager.removeFriend(name);
                    } else {
                        socialsManager.addFriend(name);
                    }
                }
            }
        }
    }
}