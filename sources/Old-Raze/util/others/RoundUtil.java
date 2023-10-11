package markgg.util.others;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;

public class RoundUtil extends Gui {

    public void drawRoundedRect(int x, int y, int width, int height, int cornerRadius, int color) {
        drawRect(x + cornerRadius, y, x + width - cornerRadius, y + height, color);
        drawRect(x, y + cornerRadius, x + cornerRadius, y + height - cornerRadius, color);
        drawRect(x + width - cornerRadius, y + cornerRadius, x + width, y + height - cornerRadius, color);
        drawArc(x + cornerRadius, y + cornerRadius, cornerRadius, 0, 90, color);
        drawArc(x + width - cornerRadius, y + cornerRadius, cornerRadius, 270, 360, color);
        drawArc(x + cornerRadius, y + height - cornerRadius, cornerRadius, 90, 180, color);
        drawArc(x + width - cornerRadius, y + height - cornerRadius, cornerRadius, 180, 270, color);
    }

    public void drawArc(int x, int y, int radius, int startAngle, int endAngle, int color) {
        for (int i = startAngle; i < endAngle; i++) {
            double angle = Math.toRadians(i);
            int xPos = (int) (x + radius * Math.cos(angle));
            int yPos = (int) (y + radius * Math.sin(angle));
            drawRect(xPos, yPos, xPos + 1, yPos + 1, color);
        }
    }
	
}
