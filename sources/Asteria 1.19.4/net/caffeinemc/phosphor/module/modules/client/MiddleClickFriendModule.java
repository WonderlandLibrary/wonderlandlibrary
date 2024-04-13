package net.caffeinemc.phosphor.module.modules.client;

import com.google.gson.JsonElement;
import imgui.ImGui;
import net.caffeinemc.phosphor.api.event.events.HudRenderEvent;
import net.caffeinemc.phosphor.api.event.events.PlayerTickEvent;
import net.caffeinemc.phosphor.api.event.orbit.EventHandler;
import net.caffeinemc.phosphor.api.util.KeyUtils;
import net.caffeinemc.phosphor.api.util.PlayerUtils;
import net.caffeinemc.phosphor.module.Module;
import net.caffeinemc.phosphor.module.setting.settings.BooleanSetting;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import org.lwjgl.glfw.GLFW;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class MiddleClickFriendModule extends Module {
    private final BooleanSetting render = new BooleanSetting("Render", this, true);

    public MiddleClickFriendModule() {
        super("MiddleClickFriend", "Adds player into friends list on middle click.", Category.CLIENT);
    }

    private HashMap<String, String> renderFriends;

    @Override
    public void onEnable() {
        renderFriends = new HashMap<>();
    }

    private void toggleFriend(PlayerEntity player) {
        if (PlayerUtils.isFriend(player)) {
            PlayerUtils.removeFriend(player);
            renderFriends.put("-", player.getName().getString());
        } else {
            PlayerUtils.addFriend(player);
            renderFriends.put("+", player.getName().getString());
        }
    }

    @EventHandler
    private void onPlayerTick(PlayerTickEvent event) {
        if (mc.currentScreen != null) return;

        if (KeyUtils.isKeyPressed(GLFW.GLFW_MOUSE_BUTTON_MIDDLE) && mc.crosshairTarget instanceof EntityHitResult entityHit) {
            if (entityHit.getEntity() instanceof PlayerEntity player) {
                toggleFriend(player);
            }
        }
    }

    @EventHandler
    private void onHudRender(HudRenderEvent event) {
        if (render.isEnabled()) {
            AtomicInteger offsetY = new AtomicInteger(5);
            renderFriends.forEach((String plusOrMinus, String playerName) -> {
                mc.textRenderer.draw(event.matrices, String.format("%s %s", plusOrMinus, playerName), 5, offsetY.get(), plusOrMinus.equals("+") ? Color.GREEN.getRGB() : Color.RED.getRGB());
                offsetY.addAndGet(mc.textRenderer.fontHeight + 1);
            });
        }
    }

    @Override
    public void renderSettings() {
        ImGui.text("Friends:");
        for (Map.Entry<String, JsonElement> entry : PlayerUtils.friends.entrySet()) {
            if (ImGui.button(entry.getValue().getAsString())) {
                PlayerUtils.friends.remove(entry.getKey());
            }
        }

        ImGui.separator();

        super.renderSettings();
    }
}
