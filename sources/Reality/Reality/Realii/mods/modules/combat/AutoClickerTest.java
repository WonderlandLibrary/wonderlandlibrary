
package Reality.Realii.mods.modules.combat;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MovingObjectPosition;

import java.util.Random;

import org.lwjgl.input.Mouse;

import Reality.Realii.event.EventHandler;
import Reality.Realii.event.events.world.EventPreUpdate;
import Reality.Realii.event.events.world.EventTick;
import Reality.Realii.event.value.Option;
import Reality.Realii.mods.Module;
import Reality.Realii.mods.ModuleType;
import Reality.Realii.utils.cheats.player.PlayerUtils;
import Reality.Realii.utils.cheats.world.TimerUtil;
import Reality.Realii.utils.math.MathUtil;

public class AutoClickerTest
extends Module {
	 public static EntityLivingBase target;
	private TimerUtil StopWatch = new TimerUtil();
	
	    private int ticksDown, attackTicks;
	    private long nextSwing;
	private Option Blockhit = new Option("Blockhit", false);
    public AutoClickerTest(){
        super("AutoClickerTest", ModuleType.Combat);
        addValues(Blockhit);
    }
    
  

    @EventHandler
    private void onUpdate(EventTick event) {
             if (mc.thePlayer.hurtTime > 0 && StopWatch.hasReached(this.nextSwing) && mc.currentScreen == null) {
            final long clicks = (long) (Math.round(MathUtil.randomInt(8, 15)) * 1.5);

            if (mc.gameSettings.keyBindAttack.isKeyDown()) {
                ticksDown++;
            } else {
                ticksDown = 0;
            }

            this.nextSwing = 1000 / clicks;

            

            if (ticksDown > 1 && (Math.sin(nextSwing) + 1 > Math.random() || Math.random() > 0.25 || StopWatch.hasReached(4 * 50)) && !mc.gameSettings.keyBindUseItem.isKeyDown() && (mc.objectMouseOver == null || mc.objectMouseOver.typeOfHit != MovingObjectPosition.MovingObjectType.BLOCK)) {
                PlayerUtils.sendClick(0, true);
            }

            StopWatch.reset();
        }
        this.attackTicks++;
    }
}

