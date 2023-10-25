package viamcp.gui;

import com.github.creeper123123321.viafabric.ViaFabric;
import com.github.creeper123123321.viafabric.util.ProtocolUtils;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiSlot;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.EnumChatFormatting;
import skidmonke.Client;
import skidmonke.Minecraft;
import tech.dort.dortware.api.font.CustomFontRenderer;
import viamcp.utils.ProtocolSorter;

import java.io.IOException;

public class GuiProtocolSelector extends GuiScreen {

    public SlotList list;

    private final GuiScreen parent;

    final CustomFontRenderer font = Client.INSTANCE.getFontManager().getFont("Large").getRenderer();

    public GuiProtocolSelector(GuiScreen parent) {
        this.parent = parent;
    }

    @Override
    public void initGui() {
        super.initGui();
        buttonList.add(new GuiButton(1, width / 2F - 100, height - 27, 200, 20, I18n.format("gui.cancel")));
        list = new SlotList(mc, width, height, 32, height - 32, 10);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        list.actionPerformed(button);

        if (button.id == 1)
            mc.displayGuiScreen(parent);
    }

    @Override
    public void handleMouseInput() throws IOException {
        list.func_178039_p();
        super.handleMouseInput();
    }

    @Override
    public void drawScreen(int drawScreen, int mouseX, float mouseY) {
        list.drawScreen(drawScreen, mouseX, mouseY);
        font.drawCenteredString("Version", this.width / 2F, 24, -1);
        font.drawString("ViaMCP", this.width - font.getWidth("ViaMCP") - 2, this.height - 14, -1);
        super.drawScreen(drawScreen, mouseX, mouseY);
    }

    class SlotList extends GuiSlot {

        public SlotList(Minecraft p_i1052_1_, int p_i1052_2_, int p_i1052_3_, int p_i1052_4_, int p_i1052_5_, int p_i1052_6_) {
            super(p_i1052_1_, p_i1052_2_, p_i1052_3_, p_i1052_4_, p_i1052_5_, p_i1052_6_);
        }

        @Override
        protected int getSize() {
            return ProtocolSorter.getProtocolVersions().size();
        }

        @Override
        protected void elementClicked(int i, boolean b, int i1, int i2) {
            ViaFabric.clientSideVersion = ProtocolSorter.getProtocolVersions().get(i).getVersion();
        }

        @Override
        protected boolean isSelected(int i) {
            return false;
        }

        @Override
        protected void drawBackground() {
            drawDefaultBackground();
        }

        @Override
        protected void drawSlot(int i, int i1, int i2, int i3, int i4, int i5) {
            font.drawCenteredString((ViaFabric.clientSideVersion == ProtocolSorter.getProtocolVersions().get(i).getVersion() ? EnumChatFormatting.BLUE.toString() : EnumChatFormatting.WHITE.toString()) + ProtocolUtils.getProtocolName(ProtocolSorter.getProtocolVersions().get(i).getVersion()), width / 2, i2 + 4, -1);
        }
    }
}
