
package Reality.Realii.mods.modules.combat;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

import java.util.Random;

import org.lwjgl.input.Mouse;

import Reality.Realii.event.EventHandler;
import Reality.Realii.event.events.world.EventPreUpdate;
import Reality.Realii.event.events.world.EventTick;
import Reality.Realii.event.value.Numbers;
import Reality.Realii.event.value.Option;
import Reality.Realii.mods.Module;
import Reality.Realii.mods.ModuleType;
import Reality.Realii.utils.cheats.player.PlayerUtils;
import Reality.Realii.utils.cheats.world.TimerUtil;

public class Reach
extends Module {
	private Numbers<Number> range = new Numbers<>("MinRReach", 3.8, 1, 6, 0.1);
	private Numbers<Number> range2 = new Numbers<>("MaxReach", 3.8, 1, 6, 0.1);
    public Reach(){
        super("Reach", ModuleType.Combat);
        addValues(range,range);
    }
    
    
   

    @EventHandler
    private void onUpdate(EventPreUpdate event) {
    	this.setSuffix("r" + range.getModeAsString() + range2.getModeAsString() + "r2");
    	
    }
}

