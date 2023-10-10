package Reality.Realii.guis.material.button.values;

import Reality.Realii.event.value.Value;
import Reality.Realii.guis.font.FontLoaders;
import Reality.Realii.guis.material.Main;
import Reality.Realii.guis.material.Tab;
import Reality.Realii.guis.material.button.Button;
import Reality.Realii.mods.modules.ClientSettings;
import Reality.Realii.utils.cheats.RenderUtills.Pair;
import Reality.Realii.utils.cheats.RenderUtills.Render2;
import Reality.Realii.utils.math.AnimationUtils;
import Reality.Realii.utils.render.ColorUtils;
import Reality.Realii.utils.render.RenderUtil;

import java.awt.*;

public class BOption extends Button {
    AnimationUtils au = new AnimationUtils();

    public BOption(float x, float y, Value v, Tab moduleTab) {
        super(x, y, v, moduleTab);
    }

    @Override
    public void draw(float mouseX, float mouseY) {
        super.draw(mouseX, mouseY);
        int i = 0;
		 Color startColor = new Color(ClientSettings.r.getValue().intValue(), ClientSettings.g.getValue().intValue(), ClientSettings.b.getValue().intValue()); 
		 Color endColor = new Color(ClientSettings.r2.getValue().intValue(), ClientSettings.g2.getValue().intValue(), ClientSettings.b2.getValue().intValue());
 	  Pair<Color, Color> colors = Pair.of(startColor, endColor);
 	  Color c = Render2.interpolateColorsBackAndForth(7, 3 + (i * 20), colors.getFirst(), colors.getSecond(), false);
        FontLoaders.arial16.drawString(v.getName(), x + 25, y + 2, new Color(255, 255, 255).getRGB());
        if (((boolean) v.getValue())) {
            RenderUtil.drawBorderedRect(x, y + -5, x + 17, y + 10, 2, c.getRGB(),c.getRGB());
            //RenderUtil.drawBorderedRect(x, y + -5, x + 20, y + 13, 2 ,c.getRGB(), c.getRGB());
            animation = au.animate(20, animation, 0.05f);
         
   
        

        } else {
        	
            RenderUtil.drawBorderedRect(x, y + -5, x + 17, y + 10, 2 ,c.getRGB(), new Color(000, 000, 000).getRGB());
            animation = au.animate(0, animation, 0.05f);
           
            
        }
    }

    @Override
    public void mouseClicked(float mouseX, float mouseY) {
        super.mouseClicked(mouseX, mouseY);
        if (Main.isHovered(x, y, x + 20, y + 10, mouseX, mouseY)) {
            v.setValue(!((boolean) v.getValue()));
        }
    }
}
