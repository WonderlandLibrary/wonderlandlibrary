// 
// Decompiled by Procyon v0.5.36
// 

package summer.base.utilities;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.Entity;
import net.minecraft.client.Minecraft;

public class CombatUtils
{
    protected static Minecraft mc;
    
    static {
        CombatUtils.mc = Minecraft.getMinecraft();
    }
    
    public static boolean canAttackEntity(final Entity entity, final boolean teams) {
        return entity.getDistanceToEntity(CombatUtils.mc.thePlayer) <= 10.0f && (!teams || !isTeam(CombatUtils.mc.thePlayer, entity));
    }
    
    public static boolean isTeam(final EntityPlayer e, final Entity ep) {
        if (ep instanceof EntityPlayer && ((EntityPlayer)ep).getTeam() != null && e.getTeam() != null) {
            final Character target = ep.getDisplayName().getFormattedText().charAt(3);
            final Character player = e.getDisplayName().getFormattedText().charAt(3);
            final Character target2 = ep.getDisplayName().getFormattedText().charAt(2);
            final Character player2 = e.getDisplayName().getFormattedText().charAt(2);
            boolean isTeam = false;
            if (target.equals(player) && target2.equals(player2)) {
                isTeam = true;
            }
            else {
                final Character target3 = ep.getDisplayName().getFormattedText().charAt(1);
                final Character player3 = e.getDisplayName().getFormattedText().charAt(1);
                final Character target4 = ep.getDisplayName().getFormattedText().charAt(0);
                final Character player4 = e.getDisplayName().getFormattedText().charAt(0);
                if (target3.equals(player3) && Character.isDigit(0) && target4.equals(player4)) {
                    isTeam = true;
                }
            }
            return isTeam;
        }
        return true;
    }

	
}

