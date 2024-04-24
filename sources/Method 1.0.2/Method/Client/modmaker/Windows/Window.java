package Method.Client.modmaker.Windows;

import Method.Client.managers.Setting;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.MainMaker;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Blocks.Array.AddDel;
import Method.Client.modmaker.Windows.Blocks.Array.Array;
import Method.Client.modmaker.Windows.Blocks.Array.GetArray;
import Method.Client.modmaker.Windows.Blocks.Settings.*;
import Method.Client.modmaker.Windows.Blocks.Variable.*;
import Method.Client.modmaker.Windows.DragableBlockTypes.*;
import Method.Client.utils.font.CFontRenderer;
import Method.Client.utils.visual.ColorUtils;
import Method.Client.utils.visual.RenderUtils;
import Method.Client.utils.visual.ScrollBar;
import Method.Client.utils.visual.SerializableGuiTextField;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;

import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.glEnable;

public class Window implements Serializable {
    public final Tabs tab;
    private boolean isCurrent;
    private int offset;
    public ArrayList<Block> blocks;
    public ArrayList<String> VarNames;
    public static CFontRenderer customFont;

    SerializableGuiTextField Quickbox;

    boolean creatingVarible;
    public DropDownList SelectType;

    public ScrollBar scrollbar;

    public Window(Tabs tab, int offset, MainMaker mainMaker) {
        scrollbar = new ScrollBar(true, 10, 40, 2, 200, 2, 0, 180, 0, 9999, false);

        customFont = CFontRenderer.afontRenderer18;
        this.blocks = new ArrayList<>();
        this.VarNames = new ArrayList<>();
        this.tab = tab;
        this.offset = offset;
        this.creatingVarible = false;
        this.isCurrent = false;

        if (this.tab == Tabs.Arrays) {
            ArrayList<String> strings = new ArrayList<>();
            strings.add("Array");
            SelectType = new DropDownList(strings);
        }
        if (this.tab == Tabs.Settings) {
            ArrayList<String> strings = new ArrayList<>();
            for (Setting.SettingType value : Setting.SettingType.values()) {
                if (value != Setting.SettingType.SubGui)
                    strings.add(value.name());
            }
            SelectType = new DropDownList(strings);
        }
        if (this.tab == Tabs.Variables) {
            ArrayList<String> strings = new ArrayList<>();
            for (MainBlockType value : MainBlockType.values()) {
                switch (value) {
                    case Header:
                    case Array:
                    case PacketHeader:
                    case Wild:
                    case Contained:
                    case Default:
                    case Null:
                    case Setting:
                        break;
                    default:
                        strings.add(value.name());
                        break;
                }
            }
            SelectType = new DropDownList(strings);
        }
        int tY = 0;
        if (this.tab == Tabs.Variables || this.tab == Tabs.Arrays || this.tab == Tabs.Settings)
            tY += 40;
        for (Block allBlock : mainMaker.allBlocks(tab)) {
            allBlock.offsetY = tY;
            blocks.add(allBlock);
            tY += 20;
        }
    }

    public void redoSpacing() {
        int tY = 0;
        if (this.tab == Tabs.Variables || this.tab == Tabs.Arrays || this.tab == Tabs.Settings)
            tY += 40;
        for (Block block : blocks) {
            block.offsetY = tY;
            tY += 20;
        }
    }

    public void drawtab(int mouseX, int mouseY) {
        glEnable(GL_BLEND);
        RenderUtils.drawRectOutline(6, 15 + this.offset, 45, 40 + this.offset, 1, ColorUtils.rainbow().getRGB());

        customFont.drawStringWithShadow(tab.name(), (int) 6, (int) 25 + this.offset, Color.white.getRGB());


        if (this.isCurrent) {
            scrollbar.scrollCheck(mouseX, mouseY, 175, 60);

            tabCustoms(mouseX, mouseY);
            Gui.drawRect(6, 15 + this.offset, 45, 40 + this.offset, new Color(15, 230, 15, 100).getRGB());
            this.blocks.forEach(block -> block.addScroll(scrollbar.getScroll()));
            if (!creatingVarible)
                this.blocks.forEach(Block::renderComponent);
        } else scrollbar.scroll = 0;

    }

    private void tabCustoms(int mouseX, int mouseY) {
        if (this.tab == Tabs.Variables || this.tab == Tabs.Arrays || this.tab == Tabs.Settings) {
            Gui.drawRect(50, 35, 140, 60, new Color(74, 57, 245, 100).getRGB());
            if (creatingVarible)
                Gui.drawRect(145, 40, 165, 55, new Color(255, 222, 0, 255).getRGB());
            RenderUtils.drawRectOutline(50, 35, 140, 60, 1, ColorUtils.rainbow().getRGB());
            if (!creatingVarible) {
                customFont.drawStringWithShadow("Create new Variable", (int) 50, (int) 45, Color.white.getRGB());
            } else {
                SelectType.x = 55;
                SelectType.y = 65;
                SelectType.drawScreen(mouseX, mouseY);
            }
            if (creatingVarible)
                Quickbox.drawTextBox();
            int tY = 60 - scrollbar.scroll * 30;
            if (!creatingVarible)
                for (String varName : VarNames) {
                    if (60 + tY < 90) {
                        tY += 30;
                        continue;
                    }
                    RenderUtils.drawRectOutline(50, 60 + tY, 140, 85 + tY, 1, new Color(74, 57, 245, 100).getRGB());

                    Gui.drawRect(50, 70 + tY, 80, 85 + tY, new Color(51, 238, 0, 125).getRGB());
                    if (this.tab != Tabs.Settings)
                        Gui.drawRect(80, 70 + tY, 110, 85 + tY, new Color(0, 54, 238, 125).getRGB());
                    Gui.drawRect(110, 70 + tY, 140, 85 + tY, new Color(21, 18, 18, 50).getRGB());

                    glEnable(GL_BLEND);
                    GlStateManager.pushMatrix();
                    if (this.tab == Tabs.Settings)
                        customFont.drawStringWithShadow(" Get               Del", 50, 75 + tY, Color.white.getRGB());
                    else
                        customFont.drawStringWithShadow(" Get       Set      Del", 50, 75 + tY, Color.white.getRGB());
                    customFont.drawStringWithShadow(varName, 50, 63 + tY, Color.white.getRGB());
                    GlStateManager.popMatrix();
                    tY += 30;
                }
        }
    }

    private void updateVariables(MainMaker maker) {
        VarNames.clear();
        for (DragableBlock activeBlock : maker.module.ActiveBlocks)
            if (activeBlock instanceof heldObject && this.tab == Tabs.Variables || (activeBlock instanceof heldArray && this.tab == Tabs.Arrays)
                    || (activeBlock instanceof SettingObject && this.tab == Tabs.Settings))
                if (!activeBlock.thisblock.getName().equalsIgnoreCase("Edit Module"))
                    VarNames.add(activeBlock.thisblock.getName());
    }

    private void customsClick(int mouseX, int mouseY, MainMaker maker) {
        if (this.isCurrent) {
            if (creatingVarible) {
                if (SelectType != null)
                    SelectType.mouseClicked(mouseX, mouseY);
            }
            if (mouseX > 50 && mouseX < 180) {
                if (!creatingVarible) {
                    if (mouseY > 70) {
                        int looppos = 0;
                        for (int i = 0; i < 40; i++) {
                            if (mouseY < 145 + (-scrollbar.scroll * 30) + looppos * 30 && mouseY > 130 + (-scrollbar.scroll * 30) + looppos * 30)
                                break;
                            looppos++;
                        }
                        if (looppos > VarNames.size())
                            return;
                        if (mouseX < 80) {
                            // Get
                            for (DragableBlock activeBlock : maker.module.ActiveBlocks) {
                                if (activeBlock instanceof heldArray && activeBlock.thisblock.getName().equalsIgnoreCase(VarNames.get(looppos))) {
                                    Insertable i = new Insertable(new GetArray(), mouseX, mouseY, maker);
                                    i.blockPointer = activeBlock;
                                    maker.module.ActiveBlocks.add(i);
                                    break;
                                }

                                if (activeBlock instanceof SettingObject && activeBlock.thisblock.getName().equalsIgnoreCase(VarNames.get(looppos))) {
                                    SettingObject setting = (SettingObject) activeBlock;
                                    if (setting.setting.getMode() == Setting.SettingType.Combo) {
                                        Insertable i = new Insertable(new GetCombo(), mouseX, mouseY, maker);
                                        i.blockPointer = activeBlock;
                                        maker.module.ActiveBlocks.add(i);
                                        break;
                                    }
                                    if (setting.setting.getMode() == Setting.SettingType.Check) {
                                        Insertable i = new Insertable(new GetCheck(), mouseX, mouseY, maker);
                                        i.blockPointer = activeBlock;
                                        maker.module.ActiveBlocks.add(i);
                                        break;
                                    }
                                    if (setting.setting.getMode() == Setting.SettingType.Slider) {
                                        Insertable i = new Insertable(new GetSlider(), mouseX, mouseY, maker);
                                        i.blockPointer = activeBlock;
                                        maker.module.ActiveBlocks.add(i);
                                        break;
                                    }
                                    if (setting.setting.getMode() == Setting.SettingType.Color) {
                                        Insertable i = new Insertable(new GetColor(), mouseX, mouseY, maker);
                                        i.blockPointer = activeBlock;
                                        maker.module.ActiveBlocks.add(i);
                                        break;
                                    }
                                }

                                if (activeBlock instanceof heldObject && activeBlock.thisblock.getName().equalsIgnoreCase(VarNames.get(looppos))) {
                                    if (activeBlock.thisblock.MainBlockTypeGiven == MainBlockType.Boolean) {
                                        Insertable i = new Insertable(new GetVarBoolean(), mouseX, mouseY, maker);
                                        i.blockPointer = activeBlock;
                                        maker.module.ActiveBlocks.add(i);
                                        break;
                                    } else if (activeBlock.thisblock.MainBlockTypeGiven == MainBlockType.Numbers) {
                                        Insertable i = new Insertable(new GetVarNum(), mouseX, mouseY, maker);
                                        i.blockPointer = activeBlock;
                                        maker.module.ActiveBlocks.add(i);
                                        break;
                                    } else {
                                        Insertable i = new Insertable(new GetVarObj(activeBlock.thisblock.MainBlockTypeGiven), mouseX, mouseY, maker);
                                        i.blockPointer = activeBlock;
                                        maker.module.ActiveBlocks.add(i);
                                        break;
                                    }
                                }
                            }

                        }

                        if (mouseX > 80 && mouseX < 110) {
                            // Set

                            for (DragableBlock activeBlock : maker.module.ActiveBlocks) {
                                if (activeBlock instanceof heldArray && activeBlock.thisblock.getName().equalsIgnoreCase(VarNames.get(looppos))) {
                                    Default i = new Default(new AddDel(), mouseX, mouseY, maker);
                                    i.blockPointer = activeBlock;
                                    maker.module.ActiveBlocks.add(i);
                                    break;
                                }

                                if (activeBlock instanceof heldObject && activeBlock.thisblock.getName().equalsIgnoreCase(VarNames.get(looppos))) {
                                    if (activeBlock.thisblock.MainBlockTypeGiven == MainBlockType.Boolean) {
                                        Default i = new Default(new SetVarBoolean(), mouseX, mouseY, maker);
                                        i.blockPointer = activeBlock;
                                        maker.module.ActiveBlocks.add(i);
                                        break;
                                    } else if (activeBlock.thisblock.MainBlockTypeGiven == MainBlockType.Numbers) {
                                        Default i = new Default(new SetVarNum(), mouseX, mouseY, maker);
                                        i.blockPointer = activeBlock;
                                        maker.module.ActiveBlocks.add(i);
                                        break;
                                    } else {
                                        Default i = new Default(new SetVarObj(activeBlock.thisblock.MainBlockTypeGiven), mouseX, mouseY, maker);
                                        i.blockPointer = activeBlock;
                                        maker.module.ActiveBlocks.add(i);
                                        break;
                                    }
                                }
                            }
                        }

                        if (mouseX > 110 && mouseX < 140) {
                            // Delete
                            deleteVariable(VarNames.get(looppos), maker);
                        }
                        updateVariables(maker);
                    }
                }
            }

            if (mouseY > 35 && mouseY < 60 && mouseX > 50 && mouseX < 180) {
                Quickbox.y = 40;
                if (!creatingVarible) {
                    Quickbox.setText("");
                    Quickbox.setFocused(true);
                    creatingVarible = true;
                } else if (Quickbox.getText().length() > 2) {
                    boolean foundsamename = maker.module.ActiveBlocks.stream().anyMatch(activeBlock -> activeBlock.thisblock.getName().equalsIgnoreCase(Quickbox.text));
                    if (mouseX > 140 && !foundsamename) {
                        createNewVariable(Quickbox.getText(), maker);
                        creatingVarible = false;
                        Quickbox.setText("");
                    }
                }
            }
        }
    }


    private void createNewVariable(String name, MainMaker maker) {
        for (Block block : this.blocks)
            if (block.getName().equalsIgnoreCase(name))
                return;
        if (this.tab == Tabs.Variables && this.isCurrent) {
            Variables Variable = new Variables(name);
            Variable.MainBlockTypeGiven = MainBlockType.valueOf(SelectType.getSelected());
            maker.module.ActiveBlocks.add(new heldObject(Variable, MainBlockType.valueOf(SelectType.getSelected()), maker));
        }
        if (this.tab == Tabs.Arrays && this.isCurrent) {
            Array array1 = new Array(name);
            array1.MainBlockTypeGiven = MainBlockType.Array;
            maker.module.ActiveBlocks.add(new heldArray(array1, maker));
        }
        if (this.tab == Tabs.Settings && this.isCurrent) {
            switch (SelectType.getSelected()) {
                case "Combo":
                    SettingObject setting = new SettingObject(new SCombo(name), maker);
                    setting.setSetting(new Setting(name, maker.module, "empty", "empty"));
                    maker.module.ActiveBlocks.add(setting);
                    maker.needsUpdate = true;
                    break;
                case "Check":
                    SettingObject settingCheck = new SettingObject(new SCheck(name), maker);
                    settingCheck.setSetting(new Setting(name, maker.module, false));
                    maker.module.ActiveBlocks.add(settingCheck);
                    maker.needsUpdate = true;
                    break;
                case "Slider":
                    SettingObject settingSlider = new SettingObject(new SSlider(name), maker);
                    settingSlider.setSetting(new Setting(name, maker.module, 0, 0, 1, false));
                    maker.module.ActiveBlocks.add(settingSlider);
                    maker.needsUpdate = true;
                    break;
                case "Color":
                    SettingObject settingColor = new SettingObject(new SColor(name), maker);
                    settingColor.setSetting(new Setting(name, maker.module, 0, 1, 1, 1));
                    maker.module.ActiveBlocks.add(settingColor);
                    maker.needsUpdate = true;
                    break;
            }
        }
        updateVariables(maker);
        redoSpacing();
    }

    private void deleteVariable(String name, MainMaker maker) {
        DragableBlock remove = null;
        for (DragableBlock activeBlock : maker.module.ActiveBlocks) {
            if ((activeBlock instanceof SettingObject && this.tab == Tabs.Settings) ||
                    (activeBlock instanceof heldObject && this.tab == Tabs.Variables) ||
                    (activeBlock instanceof heldArray && this.tab == Tabs.Arrays)) {
                if (Objects.equals(activeBlock.thisblock.getName(), name)) {
                    remove = activeBlock;
                    break;
                }
            }
        }
        if (remove != null) {
            maker.module.ActiveBlocks.remove(remove);
            updateVariables(maker);
            redoSpacing();
        }
    }

    public void MouseClick(int mouseX, int mouseY, int mouseButton, MainMaker maker) {
        scrollbar.MouseClicked(mouseX, mouseY, 175, 60);
        if (mouseButton == 0 && this.tab == Tabs.Variables || this.tab == Tabs.Arrays || this.tab == Tabs.Settings)
            customsClick(mouseX, mouseY, maker);
        if (creatingVarible)
            Quickbox.mouseClicked(mouseX, mouseY, mouseButton);
        if (this.isCurrent && !creatingVarible) {
            for (Block block : this.blocks) {
                block.mouseClicked(mouseX, mouseY, mouseButton, maker);
            }
        }
        if (mouseButton == 0)
            if (mouseX > 6 && mouseX < 45) {
                this.isCurrent = mouseY > 15 + this.offset && mouseY < 40 + this.offset;
                if (this.isCurrent) {
                    if (this.tab == Tabs.Variables || this.tab == Tabs.Arrays || this.tab == Tabs.Settings)
                        updateVariables(maker);
                    maker.searchCheck(true);
                } else VarNames.clear();
            }
    }




    public void init() {
        Quickbox = new SerializableGuiTextField(0, 50, 40, 85, Minecraft.getMinecraft().displayWidth / 130);
        Quickbox.setMaxStringLength(20);
    }

    public void keyTyped(char typedChar, int keyCode) {
        if (creatingVarible) {
            Quickbox.textboxKeyTyped(typedChar, keyCode);
        }
    }

    public boolean isCurrent() {
        return isCurrent;
    }

    public void setCurrent(boolean current) {
        isCurrent = current;
    }

    public void mouseReleased(int mouseX, int mouseY, int state) {
        scrollbar.MouseRelease();
    }


    public void updateScreen() {
        Quickbox.updateCursorCounter();
    }
}
