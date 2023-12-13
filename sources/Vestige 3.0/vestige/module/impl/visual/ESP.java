package vestige.module.impl.visual;

import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.player.EntityPlayer;
import vestige.Vestige;
import vestige.event.Listener;
import vestige.event.impl.Render3DEvent;
import vestige.module.Category;
import vestige.module.Module;
import vestige.module.impl.combat.Antibot;
import vestige.setting.impl.BooleanSetting;
import vestige.setting.impl.DoubleSetting;
import vestige.util.render.RenderUtil;

import java.awt.*;

public class ESP extends Module {

    private final DoubleSetting lineWidth = new DoubleSetting("Line width", 3.25, 0.5, 4, 0.25);
    private final DoubleSetting alpha = new DoubleSetting("Alpha", 0.8, 0.2, 1, 0.05);

    private final BooleanSetting renderInvisibles = new BooleanSetting("Render invisibles", false);

    private ClientTheme theme;
    private Antibot antibotModule;

    public ESP() {
        super("ESP", Category.VISUAL);
        this.addSettings(lineWidth, alpha, renderInvisibles);
    }

    @Listener
    public void onRender3D(Render3DEvent event) {
        if (theme == null) {
            theme = Vestige.instance.getModuleManager().getModule(ClientTheme.class);
        }

        if (antibotModule == null) {
            antibotModule = Vestige.instance.getModuleManager().getModule(Antibot.class);
        }

        Color color = new Color(theme.getColor(100));

        RenderUtil.prepareBoxRender((float) lineWidth.getValue(), color.getRed() / 255.0, color.getGreen() / 255.0, color.getBlue() / 255.0, alpha.getValue());

        RenderManager rm = mc.getRenderManager();
        float partialTicks = event.getPartialTicks();

        mc.theWorld.getLoadedEntityList().stream().filter(entity ->
                entity != mc.thePlayer &&
                        (!(entity.isInvisible() || entity.isInvisibleToPlayer(mc.thePlayer)) || renderInvisibles.isEnabled()) &&
                        entity instanceof EntityPlayer && antibotModule.canAttack((EntityPlayer) entity, this)
        ).forEach(entity -> RenderUtil.renderEntityBox(rm, partialTicks, entity));

        RenderUtil.stopBoxRender();
    }

}
