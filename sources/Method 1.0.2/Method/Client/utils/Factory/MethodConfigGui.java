package Method.Client.utils.Factory;

import Method.Client.Main;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.Tessellator;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.GuiConfigEntries;
import net.minecraftforge.fml.client.config.IConfigElement;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class MethodConfigGui extends GuiConfig {
    public MethodConfigGui(GuiScreen parent) {
        super(parent, getConfigElements(), Main.MODID, false, false, GuiConfig.getAbridgedConfigPath(MethodConfig.getString()));
    }

    private static List<IConfigElement> getConfigElements() {
        return new ArrayList<>(MethodConfig.getConfigElements());
    }

        @Override
        public void initGui() {
            if (this.entryList == null || this.needsRefresh)
            {
                this.entryList = new GuiConfigEntries(this, mc) {
                    @SuppressWarnings({ "unused", "null" })
                    @Override
                    protected void drawContainerBackground(@Nonnull Tessellator tessellator) {
                        if (mc.world == null) {
                            super.drawContainerBackground(tessellator);
                        }
                    }
                };
                this.needsRefresh = false;
            }
            super.initGui();
        }

        @Override
        public void drawDefaultBackground() {
            drawWorldBackground(0);
        }


}
