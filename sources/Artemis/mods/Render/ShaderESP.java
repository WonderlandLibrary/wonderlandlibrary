package artemis.mods.Render;

import java.util.Iterator;

import org.lwjgl.input.Keyboard;

import artemis.Category;
import artemis.module;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.EntityLivingBase;

public class ShaderESP extends module{
		  public ShaderESP() {
		    super("ShaderESP", "Outlines entitys", Keyboard.KEY_NONE, 69, Category.RENDER);
		    
		  }
		  @Override
		  public void onUpdate() {
		     if(this.isToggled())
		          		     
		      super.onUpdate();
		  }

		}

