package club.cloverhook.cheat.impl.visual;

import club.cloverhook.cheat.Cheat;
import club.cloverhook.cheat.CheatCategory;
import club.cloverhook.event.minecraft.EntityLivingRenderEvent;
import club.cloverhook.event.minecraft.EntityRenderEvent;
import club.cloverhook.utils.property.impl.BooleanProperty;
import club.cloverhook.utils.property.impl.ColorProperty;
import me.hippo.systems.lwjeb.annotation.Collect;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.opengl.GL11;

public class Chams extends Cheat {

    public static BooleanProperty fill = new BooleanProperty("Fill", "Basically a CSGO esp", null, true);
    public static BooleanProperty fillRainbow = new BooleanProperty("Fill Rainbow", "Makes the fill RGB", () -> fill.getValue(), false);
    public static ColorProperty fillColor = new ColorProperty("Fill Color", "The color of the fill esp", () -> fill.getValue() && !fillRainbow.getValue(), 1F, 0F, 1F, 255);
    public Chams() {
        super("Chams", "Wallhacc", CheatCategory.VISUAL);
        registerProperties(fill, fillRainbow, fillColor);
    }

    @Collect
    public void onRender(EntityLivingRenderEvent e) {
        if (e.isPre() && e.getEntity() instanceof EntityPlayer) {
            GL11.glEnable(32823);
            GL11.glPolygonOffset(1.0f, -1100000.0f);
        } else if (e.isPost() && e.getEntity() instanceof EntityPlayer) {
            GL11.glDisable(32823);
            GL11.glPolygonOffset(1.0f, 1100000.0f);
        }
    }
}
