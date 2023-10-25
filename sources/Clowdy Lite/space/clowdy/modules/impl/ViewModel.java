package space.clowdy.modules.impl;

import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.lwjgl.opengl.GL11;
import space.clowdy.modules.Category;
import space.clowdy.modules.Module;

public class ViewModel extends Module {
     public ViewModel() {
          super("ViewModel", "\u0014;8==K5 @C:8 <---->", 0, Category.VISUAL);
     }

     @SubscribeEvent
     public void そ(RenderHandEvent renderHandEvent) {
          GL11.glTranslated(0.0D, 0.0D, -0.74D);
     }
}
