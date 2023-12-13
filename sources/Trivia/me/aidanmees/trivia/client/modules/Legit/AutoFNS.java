package me.aidanmees.trivia.client.modules.Legit;

import org.lwjgl.input.Keyboard;

import me.aidanmees.trivia.client.module.state.Category;
import me.aidanmees.trivia.client.tools.Time;
import me.aidanmees.trivia.module.Module;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

public class AutoFNS extends Module {
	Time t = new Time();

	public AutoFNS() {
		super("AutoFNS", Keyboard.KEY_NONE, Category.LEGIT, "Puts fire under the player.");
	}

	@Override
	public void onDisable() {
		super.onDisable();
	}

	@Override
	public void onUpdate() {
		  EntityLivingBase ent = mc.objectMouseOver.getMouseOverEntity();
		    
		    ItemStack fns = mc.thePlayer.getCurrentEquippedItem();
		    if ((fns != null) && (ent != null) && (this.t.over(500L)))
		    {
		      String name = fns.getDisplayName();
		      BlockPos bp = ent.getPosition();
		      if (name.equals("Flint and Steel") || name.equals("Fire Charge"))
		      {
		        System.out.println("fire");
		        if (mc.playerController.onPlayerRightClick(mc.thePlayer, mc.theWorld, fns, bp.add(0, -1, 0), EnumFacing.UP, mc.objectMouseOver.hitVec)) {
		          mc.thePlayer.swingItem();
		        }
		      }
		      this.t.begin();
		    }
		  }
		}
