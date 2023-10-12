package wtf.diablo.module.impl.Render;

import lombok.Getter;
import lombok.Setter;
import org.lwjgl.input.Keyboard;
import wtf.diablo.gui.clickGuiAlternate.ClickGui;
import wtf.diablo.module.data.Category;
import wtf.diablo.module.Module;
import wtf.diablo.module.data.ServerType;
import wtf.diablo.settings.impl.ModeSetting;


@Setter@Getter
public class ClickGUI extends Module  {
    public static ModeSetting clickGuiMode = new ModeSetting("ClickGUI","Diablo","Simple");
    public ClickGUI(){
        super("ClickGUI", "A Clickable Minecraft Ui Implementation", Category.RENDER, ServerType.All);
        setKey(Keyboard.KEY_RSHIFT);
        this.addSettings(clickGuiMode);
    }
    public void updateSettings() {

    }

    ClickGui clickGui;
    wtf.diablo.gui.clickGui.ClickGui diabloGui;

    wtf.diablo.gui.clickGuiMaterial.ClickGui materialGui;
    @Override
    public void onEnable() {
        if(clickGui == null)
            clickGui = new ClickGui();

        if(diabloGui == null)
            diabloGui = new wtf.diablo.gui.clickGui.ClickGui();

        if(materialGui == null)
            materialGui = new wtf.diablo.gui.clickGuiMaterial.ClickGui();

        switch (clickGuiMode.getMode()){
            case "Diablo":
                diabloGui.openClickGUI();
                break;

            case "Simple":
                clickGui.openClickGUI();
                break;

            case "Material":
                materialGui.openClickGUI();
                break;
        }

        toggle();
    }
}

