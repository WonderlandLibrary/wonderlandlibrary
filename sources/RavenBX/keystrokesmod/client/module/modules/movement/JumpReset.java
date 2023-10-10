package keystrokesmod.client.module.modules.movement;

import keystrokesmod.client.module.Module;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import net.minecraft.entity.EntityLivingBase;

public class JumpReset extends Module {
    private boolean shouldJump = false;

    public JumpReset() {
        super("JumpReset", ModuleCategory.movement);
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.START || event.player == null)
            return;

        if (shouldJump) {
            event.player.jump();
            shouldJump = false;
        }
    }

    @SubscribeEvent
    public void onLivingHurtEvent(net.minecraftforge.event.entity.living.LivingHurtEvent event) {
        if (event.entity instanceof EntityLivingBase) {
            EntityLivingBase livingEntity = (EntityLivingBase) event.entity;

            if (livingEntity == event.entity.worldObj.getPlayerEntityByName("YourPlayerName")) {
                // You can add more conditions here to determine when the player should jump
                shouldJump = true;
            }
        }
    }

}
