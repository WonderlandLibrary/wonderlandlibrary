package Reality.Realii.guis.material.button.values;

import org.lwjgl.input.Mouse;
import Reality.Realii.event.value.Numbers;
import Reality.Realii.event.value.Value;
import Reality.Realii.guis.font.FontLoaders;
import Reality.Realii.guis.material.Main;
import Reality.Realii.guis.material.Tab;
import Reality.Realii.guis.material.button.Button;
import Reality.Realii.mods.modules.ClientSettings;
import Reality.Realii.utils.cheats.RenderUtills.Pair;
import Reality.Realii.utils.cheats.RenderUtills.Render2;
import Reality.Realii.utils.render.ColorUtils;
import Reality.Realii.utils.render.RenderUtil;

import java.awt.*;

public class BNumbers extends Button {

    public BNumbers(float x, float y, Value v, Tab moduleTab) {
        super(x, y, v, moduleTab);
    }

    @Override
    public void drawButton(float mouseX, float mouseY) {
        FontLoaders.arial16.drawString(v.getName() + ":" + v.getValue(), x, y - 4, new Color(255, 255, 255).getRGB());
        int i = 0;
		 Color startColor = new Color(ClientSettings.r.getValue().intValue(), ClientSettings.g.getValue().intValue(), ClientSettings.b.getValue().intValue()); 
		 Color endColor = new Color(ClientSettings.r2.getValue().intValue(), ClientSettings.g2.getValue().intValue(), ClientSettings.b2.getValue().intValue());
  	  Pair<Color, Color> colors = Pair.of(startColor, endColor);
  	  Color c = Render2.interpolateColorsBackAndForth(7, 3 + (i * 20), colors.getFirst(), colors.getSecond(), false);
        
        RenderUtil.drawRect(x + 5, y + 5, x + 65, y + 9, ColorUtils.reAlpha(c.getRGB(), 0.6f));
        animation = animationUtils.animate(60 * (((Number) v.getValue()).floatValue() / ((Numbers) v).getMaximum().floatValue()), animation, 0.05f);
        RenderUtil.drawRect(x + 5, y + 5, x + 5 + animation, y + 9, c.getRGB());
       

        if (Main.isHovered(x + 5, y + 4, x + 65, y + 7, mouseX, mouseY) && Mouse.isButtonDown(0)) {
            drag = true;
        } else if (!Mouse.isButtonDown(0)) {
            drag = false;
        }

        if (drag ) {
            double reach = mouseX - (x + 5);
            double percent = reach / 60f;
            double val = (((Numbers<?>) v).getMaximum().doubleValue() - ((Numbers<?>) v).getMinimum().doubleValue()) * percent;
            if (val > ((Numbers<?>) v).getMinimum().doubleValue() && val < ((Numbers<?>) v).getMaximum().doubleValue()) {
                v.setValue((int)(val*10)/10F);
            }
            
            if(val < ((Numbers<?>) v).getMinimum().doubleValue()){
                v.setValue(((Numbers<?>) v).getMinimum());
            }else if(val > ((Numbers<?>) v).getMaximum().doubleValue()){
                v.setValue(((Numbers<?>) v).getMaximum());
            }
        }

    }

    @Override
    public void mouseClicked(float mouseX, float mouseY) {
        super.mouseClicked(mouseX, mouseY);
    }
}
