
package Reality.Realii.mods.modules.combat;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;

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

public class CustomRotations
extends Module {
	public static Option Vertical = new Option("Vertical", true);
    public static Option AimLock = new Option("AimLock", true);
    public static Option Randomisze = new Option("Randomize", true);
    public static Option ClamPitch = new Option("PitchUpdate", true);
    public static Option MouseSens = new Option("MouseFixSpeed", true);



	public static Numbers<Number> PitchRandom = new Numbers<>("PitchRandomMin", 0.1, 0.1, 4, 4);
	public static Numbers<Number> PitchRandom2 = new Numbers<>("PitchRandoMax", 0.1, 0.1, 4, 4);
	public static Numbers<Number> YawRandom2 = new Numbers<>("YawRandomMax", 0.1, 0.1, 4, 4);
	public  static Numbers<Number> YawRandom = new Numbers<>("YawRandomMin", 0.1, 0.1, 4, 4);
	public static  Numbers<Number> Speed = new Numbers<>("Speed", 0.1, 0.1, 3, 10);
	
    public CustomRotations(){
        super("CustomRotations", ModuleType.Combat);
        addValues(PitchRandom,PitchRandom2,YawRandom,YawRandom2,Speed,Vertical,AimLock,Randomisze,ClamPitch,MouseSens);
        
       
    }
    
    
  
   

    @EventHandler
    private void onUpdate(EventPreUpdate event) {
    	
    	
    }
}

