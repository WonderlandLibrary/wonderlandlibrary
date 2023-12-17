// 
// Decompiled by Procyon v0.5.36
// 

package ru.tuskevich.modules.impl.COMBAT;

import ru.tuskevich.event.EventTarget;
import java.util.Iterator;
import net.minecraft.client.Minecraft;
import ru.tuskevich.util.movement.MoveUtility;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import ru.tuskevich.event.events.impl.EventUpdate;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.Entity;
import java.util.List;
import ru.tuskevich.modules.Type;
import ru.tuskevich.modules.ModuleAnnotation;
import ru.tuskevich.modules.Module;

@ModuleAnnotation(name = "AntiBot", desc = "", type = Type.COMBAT)
public class AntiBot extends Module
{
    public static List<Entity> isBotPlayer;
    
    @Override
    public void onDisable() {
    }
    
    public static boolean isBot(final Entity var0) {
        final EntityPlayer var = (EntityPlayer)var0;
        return false;
    }
    
    @EventTarget
    public void onUpdate(final EventUpdate var1) {
        for (final Entity target : AntiBot.mc.world.loadedEntityList) {
            if (target.ticksExisted < 20.0f && target instanceof EntityOtherPlayerMP && ((EntityOtherPlayerMP)target).getHealth() < 20.0f && MoveUtility.getEntityDirection((EntityLivingBase)target) != MoveUtility.getPlayerDirection() && !target.isDead && ((EntityOtherPlayerMP)target).hurtTime > 0) {
                final double posY = target.posY;
                final Minecraft mc = AntiBot.mc;
                final double pos2 = Minecraft.player.posY - 3.0;
                final Minecraft mc2 = AntiBot.mc;
                if (!this.checkPosition(posY, pos2, Minecraft.player.posY + 3.0) || ((EntityOtherPlayerMP)target).getTotalArmorValue() != 0) {
                    continue;
                }
                final Minecraft mc3 = AntiBot.mc;
                if (Minecraft.player.getDistance(target) > 25.0f) {
                    continue;
                }
                final Minecraft mc4 = AntiBot.mc;
                if (Minecraft.player.connection.getPlayerInfo(target.getUniqueID()).getResponseTime() == 0) {
                    continue;
                }
                final Minecraft mc5 = AntiBot.mc;
                if (Minecraft.player.connection.getPlayerInfo(target.getUniqueID()).getResponseTime() > 40) {
                    continue;
                }
                if (((EntityOtherPlayerMP)target).getLastDamageSource() != null) {
                    continue;
                }
                AntiBot.mc.world.removeEntity(target);
            }
        }
    }
    
    private boolean checkPosition(final double pos1, final double pos2, final double pos3) {
        return pos1 <= pos3 && pos1 >= pos2;
    }
}
