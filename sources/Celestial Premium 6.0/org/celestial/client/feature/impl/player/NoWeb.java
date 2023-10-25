/*
 * Decompiled with CFR 0.150.
 */
package org.celestial.client.feature.impl.player;

import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import org.celestial.client.event.EventTarget;
import org.celestial.client.event.events.impl.motion.EventMove;
import org.celestial.client.event.events.impl.player.EventPreMotion;
import org.celestial.client.feature.Feature;
import org.celestial.client.feature.impl.Type;
import org.celestial.client.helpers.player.MovementHelper;
import org.celestial.client.settings.impl.ListSetting;
import org.celestial.client.settings.impl.NumberSetting;

public class NoWeb
extends Feature {
    public ListSetting noWebMode = new ListSetting("NoWeb Mode", "Matrix", () -> true, "Matrix", "Matrix New", "NCP");
    public NumberSetting webSpeed = new NumberSetting("Web Speed", 0.8f, 0.1f, 2.0f, 0.1f, () -> this.noWebMode.currentMode.equals("Matrix New"));
    public NumberSetting webJumpMotion = new NumberSetting("Jump Motion", 2.0f, 0.0f, 10.0f, 1.0f, () -> this.noWebMode.currentMode.equals("Matrix New"));

    public NoWeb() {
        super("NoWeb", "\u041f\u043e\u0437\u0432\u043e\u043b\u044f\u0435\u0442 \u0431\u044b\u0441\u0442\u0440\u043e \u0445\u043e\u0434\u0438\u0442\u044c \u0432 \u043f\u0430\u0443\u0442\u0438\u043d\u0435", Type.Player);
        this.addSettings(this.noWebMode, this.webJumpMotion, this.webSpeed);
    }

    @EventTarget
    public void onPreMotion(EventPreMotion event) {
        String mode = this.noWebMode.getOptions();
        this.setSuffix(mode);
        if (mode.equalsIgnoreCase("Matrix New")) {
            BlockPos blockPos = new BlockPos(NoWeb.mc.player.posX, NoWeb.mc.player.posY - 0.6, NoWeb.mc.player.posZ);
            Block block = NoWeb.mc.world.getBlockState(blockPos).getBlock();
            if (NoWeb.mc.player.isInWeb) {
                NoWeb.mc.player.motionY += 2.0;
            } else if (Block.getIdFromBlock(block) == 30) {
                NoWeb.mc.player.motionY = this.webJumpMotion.getCurrentValue() > 0.0f ? (NoWeb.mc.player.motionY += (double)this.webJumpMotion.getCurrentValue()) : 0.0;
                MovementHelper.setSpeed(this.webSpeed.getCurrentValue());
                NoWeb.mc.gameSettings.keyBindJump.pressed = false;
            }
        }
    }

    @EventTarget
    public void onMove(EventMove event) {
        String mode = this.noWebMode.getOptions();
        this.setSuffix(mode);
        if (this.getState()) {
            if (mode.equalsIgnoreCase("Matrix")) {
                if (NoWeb.mc.player.onGround && NoWeb.mc.player.isInWeb) {
                    NoWeb.mc.player.isInWeb = true;
                } else {
                    if (NoWeb.mc.gameSettings.keyBindJump.isKeyDown()) {
                        return;
                    }
                    NoWeb.mc.player.isInWeb = false;
                }
                if (NoWeb.mc.player.isInWeb && !NoWeb.mc.gameSettings.keyBindSneak.isKeyDown()) {
                    MovementHelper.setEventSpeed(event, 0.483);
                }
            } else if (mode.equalsIgnoreCase("NCP")) {
                if (NoWeb.mc.player.onGround && NoWeb.mc.player.isInWeb) {
                    NoWeb.mc.player.isInWeb = true;
                } else {
                    if (NoWeb.mc.gameSettings.keyBindJump.isKeyDown()) {
                        return;
                    }
                    NoWeb.mc.player.isInWeb = false;
                }
                if (NoWeb.mc.player.isInWeb && !NoWeb.mc.gameSettings.keyBindSneak.isKeyDown()) {
                    MovementHelper.setEventSpeed(event, 0.403);
                }
            }
        }
    }
}

