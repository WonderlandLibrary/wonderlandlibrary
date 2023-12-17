// 
// Decompiled by Procyon v0.5.36
// 

package ru.tuskevich.modules.impl.MOVEMENT;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
import ru.tuskevich.event.EventTarget;
import net.minecraft.client.Minecraft;
import ru.tuskevich.event.events.impl.EventMove;
import ru.tuskevich.ui.dropui.setting.Setting;
import ru.tuskevich.ui.dropui.setting.imp.ModeSetting;
import ru.tuskevich.modules.Type;
import ru.tuskevich.modules.ModuleAnnotation;
import ru.tuskevich.modules.Module;

@ModuleAnnotation(name = "NoClip", desc = "\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd \ufffd\ufffd\ufffd\ufffd\ufffd\ufffd \ufffd\ufffd\ufffd\ufffd\ufffd\ufffd \ufffd\ufffd\ufffd\ufffd\ufffd", type = Type.MOVEMENT)
public class NoClip extends Module
{
    private final ModeSetting mode;
    
    public NoClip() {
        this.mode = new ModeSetting("Mode", "Default", new String[] { "Default", "Sunrise" });
        this.add(new Setting[0]);
    }
    
    @EventTarget
    public void move(final EventMove move) {
        if (!this.collisionPredict(move.to())) {
            if (move.isCollidedHorizontal()) {
                move.setIgnoreHorizontalCollision();
            }
            Label_0051: {
                if (move.motion().y <= 0.0) {
                    final Minecraft mc = NoClip.mc;
                    if (!Minecraft.player.isSneaking()) {
                        break Label_0051;
                    }
                }
                move.setIgnoreVerticalCollision();
            }
            move.motion().y = Math.min(move.motion().y, this.mode.is("Default") ? 0.39 : 99999.0);
        }
    }
    
    public boolean collisionPredict(final Vec3d to) {
        final WorldClient world = NoClip.mc.world;
        final Minecraft mc = NoClip.mc;
        final EntityPlayerSP player = Minecraft.player;
        final Minecraft mc2 = NoClip.mc;
        final boolean prevCollision = world.getCollisionBoxes(player, Minecraft.player.getEntityBoundingBox().contract(0.0625)).isEmpty();
        final Minecraft mc3 = NoClip.mc;
        final double posX = Minecraft.player.posX;
        final Minecraft mc4 = NoClip.mc;
        final double posY = Minecraft.player.posY;
        final Minecraft mc5 = NoClip.mc;
        final Vec3d backUp = new Vec3d(posX, posY, Minecraft.player.posZ);
        final Minecraft mc6 = NoClip.mc;
        Minecraft.player.setPosition(to.x, to.y, to.z);
        final WorldClient world2 = NoClip.mc.world;
        final Minecraft mc7 = NoClip.mc;
        final EntityPlayerSP player2 = Minecraft.player;
        final Minecraft mc8 = NoClip.mc;
        final boolean collision = world2.getCollisionBoxes(player2, Minecraft.player.getEntityBoundingBox().contract(0.0625)).isEmpty() && prevCollision;
        final Minecraft mc9 = NoClip.mc;
        Minecraft.player.setPosition(backUp.x, backUp.y, backUp.z);
        return collision;
    }
    
    @Override
    public void onDisable() {
        super.onDisable();
    }
}
