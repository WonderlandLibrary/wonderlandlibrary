package wtf.dawn.module.impl.visual.HUD;

        import wtf.dawn.Dawn;
        import wtf.dawn.utils.ColorUtil;
        import net.minecraft.client.Minecraft;
        import net.minecraft.client.gui.FontRenderer;
        import net.minecraft.client.gui.ScaledResolution;

        import java.awt.*;
        import java.util.Comparator;
        import static wtf.dawn.utils.ColorUtil.getRainbow;
        import wtf.dawn.module.Module;
        import wtf.dawn.utils.font.FontUtil;

public class ArrayList {
        public Minecraft mc = Minecraft.getMinecraft();




        public void draw() {
        ScaledResolution sr = new ScaledResolution(mc);


        Dawn.getInstance().getModuleManager().getModules().sort
        (Comparator.comparingInt(m -> (int) FontUtil.normal.getStringWidth(((Module)m).getDisplayName()))
        .reversed()
        );


        int count = 0;


        for(
        Module m : Dawn.getInstance().getModuleManager().getModules()){
        if(!m.isEnabled())
        continue;

        int offset = count*(FontUtil.normal.getHeight() + 3);

        FontUtil.normal.drawStringWithShadow(m.getDisplayName(), sr.getScaledWidth() - FontUtil.normal.getStringWidth(m.getDisplayName()) - 2, (2 + offset), ColorUtil.getRainbow(4, 0.8f, 1, count * 200));

        count++;
        }


        }
        }


