package slavikcodd3r.rainbow.module.modules.combat;

import net.minecraft.client.entity.EntityPlayerSP;
import java.util.Iterator;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import slavikcodd3r.rainbow.event.Event;
import slavikcodd3r.rainbow.event.EventTarget;
import slavikcodd3r.rainbow.event.events.UpdateEvent;
import slavikcodd3r.rainbow.module.Module;
import slavikcodd3r.rainbow.option.Option.Op;
import slavikcodd3r.rainbow.utils.ClientUtils;
import slavikcodd3r.rainbow.utils.CombatUtils;

@Module.Mod
public class AimAssist extends Module
{
    @Op(name = "Smooth", min = 1.0, max = 30.0, increment = 1.0)
    private double smooth;
    @Op(min = 0.0, max = 180.0, increment = 5.0, name = "Degrees")
    public static double degrees;
    @Op(min = 1.0, max = 7.0, increment = 0.25, name = "Range")
    public static double range;
    private EntityLivingBase target;
    
    public AimAssist() {
        this.target = null;
        this.smooth = 5.0;
        AimAssist.degrees = 45.0;
        AimAssist.range = 5.0;
    }
    
    @EventTarget(3)
    public void onUpdate(final UpdateEvent local_01) {
        if (local_01.getState().equals(Event.State.PRE) && ClientUtils.player().isEntityAlive()) {
            for (final Object local_2 : ClientUtils.world().loadedEntityList) {
                if (local_2 instanceof EntityLivingBase) {
                    if (CombatUtils.isEntityValid((Entity)local_2) && ((EntityLivingBase)local_2).isEntityAlive()) {
                        this.target = (EntityLivingBase)local_2;
                    }
                    else {
                        this.target = null;
                    }
                    if (this.target == null) {
                        continue;
                    }
                    final EntityPlayerSP player4;
                    final EntityPlayerSP entityPlayerSP;
                    final EntityPlayerSP player3 = entityPlayerSP = (player4 = ClientUtils.player());
                    entityPlayerSP.rotationPitch += (float)(CombatUtils.getPitchChange(this.target) / this.smooth);
                    final EntityPlayerSP player6;
                    final EntityPlayerSP entityPlayerSP2;
                    final EntityPlayerSP player5 = entityPlayerSP2 = (player6 = ClientUtils.player());
                    entityPlayerSP2.rotationYaw += (float)(CombatUtils.getYawChange(this.target) / this.smooth);
                }
            }
        }
    }
}
