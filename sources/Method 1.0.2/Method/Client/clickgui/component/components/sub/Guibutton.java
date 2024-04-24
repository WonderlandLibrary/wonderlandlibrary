package Method.Client.clickgui.component.components.sub;

import Method.Client.Main;
import Method.Client.clickgui.component.Component;
import Method.Client.clickgui.component.components.Button;
import Method.Client.managers.FileManager;
import Method.Client.managers.Setting;
import Method.Client.modmaker.DuplicateScreen;
import Method.Client.modmaker.MainMaker;
import Method.Client.module.Category;
import Method.Client.module.Module;
import Method.Client.module.ModuleManager;
import Method.Client.module.misc.GuiModule;
import Method.Client.utils.Screens.SubGui;
import com.google.gson.JsonObject;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.IResource;
import net.minecraft.util.ResourceLocation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import static Method.Client.utils.system.Wrapper.mc;
import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.glEnable;


public class Guibutton extends Component {
    private final Setting op;
    private GuiScreen screen;
    private SubGui subGui;

    public Guibutton(Setting option, Button button, int offset, GuiScreen screen) {
        this.op = option;
        this.parent = button;
        this.offset = offset;
        this.screen = screen;
    }

    public Guibutton(Setting option, Button button, int offset, SubGui subGui) {
        this.op = option;
        this.parent = button;
        this.offset = offset;
        this.subGui = subGui;
    }

    @Override
    public void renderComponent() {
        glEnable(GL_BLEND);
        Gui.drawRect(parent.parent.getX() + 2, parent.parent.getY() + offset, parent.parent.getX() + (parent.parent.getWidth()), parent.parent.getY() + offset + 12, this.hovered ? GuiModule.Hover.getcolor() : GuiModule.innercolor.getcolor());
        Gui.drawRect(parent.parent.getX(), parent.parent.getY() + offset, parent.parent.getX() + 2, parent.parent.getY() + offset + 12, 0xA6111111);
        GlStateManager.pushMatrix();
        Button.fontSelectButton(this.op.getName(), (parent.parent.getX() + 15), (parent.parent.getY() + offset + 2), -1);
        GlStateManager.popMatrix();
    }

    @Override
    public String getName() {
        return this.op.getName();
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int button) {
        if (this.screen != null) {
            if (this.hovered && button == 0 && this.parent.open && this.screen instanceof MainMaker) {
                Main.Maker.setModule(this.op.getParentMod());
            }
            if (this.hovered && button == 0 && this.parent.open && this.screen instanceof DuplicateScreen) {
                // new thread wait 1 second then run
                Main.ClickGui.runlater = this;
                return;
            }
            if (this.hovered && button == 0 && this.parent.open) {
                this.parent.mod.settingChanged(ClickType.Gui);
                mc.displayGuiScreen(this.screen);
            }
        } else if (subGui != null) {
            if (this.hovered && button == 0 && this.parent.open) {
                this.subGui.open();
                this.parent.mod.settingChanged(ClickType.Gui);
                Main.ClickGui.subGui = this.subGui;
            }
        }
    }

    public void dupliacteThis() {
        int rand = (int) (Math.random() * 999 + 1);
        String newname = this.op.getParentMod().getName() + rand;
        while (ModuleManager.getModuleByName(newname) != null) { // if you have 999 modules with the same name, you have a problem bigger than this
            rand = (int) (Math.random() * 999 + 1);
            newname = this.op.getParentMod().getName() + rand;
        }
        try {
            String location = this.op.getParentMod().getCategory().toString().toLowerCase();
            location += "/" + this.op.getParentMod().getName().toLowerCase() + ".mtd";
            IResource resource = Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation(Main.MODID, location));
            BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream()));
            JsonObject moduleJason = (JsonObject) FileManager.jsonParser.parse(reader);
            reader.close();
            Module mod = Main.blockSaver.initModulefromFile(this.op.getParentMod().getName().toLowerCase(), Category.MAKER, true, newname, moduleJason);
            Main.Maker.setModule(mod);

            mc.displayGuiScreen(Main.Maker);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
