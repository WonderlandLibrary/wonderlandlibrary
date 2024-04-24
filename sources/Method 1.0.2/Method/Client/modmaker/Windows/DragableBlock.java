package Method.Client.modmaker.Windows;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.MainMaker;
import Method.Client.modmaker.Windows.Blocks.Array.AddDel;
import Method.Client.modmaker.Windows.Blocks.Array.GetArray;
import Method.Client.modmaker.Windows.Blocks.Settings.GetCheck;
import Method.Client.modmaker.Windows.Blocks.Settings.GetColor;
import Method.Client.modmaker.Windows.Blocks.Settings.GetCombo;
import Method.Client.modmaker.Windows.Blocks.Settings.GetSlider;
import Method.Client.modmaker.Windows.Blocks.Variable.*;
import Method.Client.modmaker.Windows.DragableBlockTypes.*;
import Method.Client.module.Module;
import Method.Client.utils.font.CFont;
import Method.Client.utils.visual.ColorUtils;
import Method.Client.utils.visual.RenderUtils;
import Method.Client.utils.visual.SerializableGuiTextField;
import Method.Client.utils.visual.TrueFalseBox;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;

import static org.lwjgl.input.Keyboard.KEY_LCONTROL;
import static org.lwjgl.input.Keyboard.KEY_LMENU;

public class DragableBlock implements Serializable {

    public Block thisblock;
    public int width = 0;
    public int x;
    public int y;
    public int dragX;
    public int dragY;
    public int offsetx;


    public boolean isDragging = false;
    public boolean isplaced = false;
    public boolean hover = false;
    public boolean clickedOn = false;
    public int clickedOnX = 0;
    public int clickedOnY = 0;
    public boolean isGlowingBelow = false;
    public DragableBlock blockPointer = null;
    public DragableBlock nextBlock = null;
    public boolean minimized = false;
    public Color niceColor;
    public ArrayList<InsertableBubble> inputBoxes = new ArrayList<>();
    public DropDownList dropDowns;
    public ArrayList<TrueFalseBox> trueFalses = new ArrayList<>();
    public ManyItems manyItems = null;
    public Module local;

    int hovertime = 0;

    public int saverNextBlock = -1;
    public int saverCurrentBlock = -1;
    public int saverblockPointer = -1;

    public void setup() {
        int trueFalse = 0;
        int typesAccepted = 0;
        for (BlockObjects blockItem : this.thisblock.getBlockItems()) {
            if (blockItem == BlockObjects.BlockSearch || blockItem == BlockObjects.ItemSearch) {
                manyItems = new ManyItems(blockItem);
            }
            if (blockItem == BlockObjects.TrueFalse) {
                trueFalses.add(trueFalse, new TrueFalseBox());
                trueFalse++;
            }
            if (blockItem == BlockObjects.DropDown && this.thisblock.ddOptions != null)
                dropDowns = new DropDownList(this.thisblock.ddOptions);
            if (blockItem == BlockObjects.BooleanTextEnter || blockItem == BlockObjects.NumericalTextEnter) {
                this.inputBoxes.add(new InsertableBubble(new SerializableGuiTextField(0, 0, 1, 20, 20)
                        , blockItem, this.thisblock.tryTypes(typesAccepted)));
                typesAccepted++;
            }
        }
    }

    public DragableBlock(Module local) {
        this.local = local;
    }

    public DragableBlock(Block block, Module local) {
        this.local = local;
        this.niceColor = ColorUtils.randomColor(true);
        this.thisblock = block;
        int trueFalse = 0;
        int typesAccepted = 0;
        for (BlockObjects blockItem : block.getBlockItems()) {
            if (blockItem == BlockObjects.BlockSearch ||
                    blockItem == BlockObjects.ItemSearch) {
                manyItems = new ManyItems(blockItem);
            }
            if (blockItem == BlockObjects.TrueFalse) {
                trueFalses.add(trueFalse, new TrueFalseBox());
                trueFalse++;
            }
            if (blockItem == BlockObjects.DropDown && block.ddOptions != null)
                dropDowns = new DropDownList(block.ddOptions);
            if (blockItem == BlockObjects.BooleanTextEnter || blockItem == BlockObjects.NumericalTextEnter) {
                this.inputBoxes.add(new InsertableBubble(new SerializableGuiTextField(0, 0, 1, 20, 20)
                        , blockItem, block.tryTypes(typesAccepted)));
                typesAccepted++;
            }
        }
    }

    public Insertable bubbleClone(DragableBlock containedBlock, MainMaker maker) {
        Insertable bubble = new Insertable(containedBlock.thisblock, 222, 222, maker);
        bubble.isplaced = true;
        if (containedBlock.blockPointer != null)
            bubble.blockPointer = containedBlock.blockPointer;

        for (int i = 0; i < containedBlock.inputBoxes.size(); i++) {
            if (containedBlock.inputBoxes.get(i).isFull && containedBlock.inputBoxes.get(i).containedBlock != null) {
                bubble.inputBoxes.get(i).dropAndExpand(bubbleClone(containedBlock.inputBoxes.get(i).containedBlock, maker));
            } else
                bubble.inputBoxes.get(i).inputBox.setText(containedBlock.inputBoxes.get(i).inputBox.getText());
        }
        if (containedBlock.dropDowns != null)
            bubble.dropDowns.selected = containedBlock.dropDowns.selected;

        maker.AdderBlocks.add(bubble);
        return bubble;
    }

    public DragableBlock nextBlockClone(DragableBlock inNext, MainMaker maker) {
        // inNext is "this.nextblock"
        DragableBlock clone = null;
        if (inNext instanceof Contained)
            clone = new Contained(inNext.thisblock, inNext.x, inNext.y, maker);
        if (inNext instanceof Header)
            clone = new Header(inNext.thisblock, inNext.x, inNext.y, maker);
        if (inNext instanceof Default)
            clone = new Default(inNext.thisblock, inNext.x, inNext.y, maker);
        if (inNext instanceof Insertable)
            clone = new Insertable(inNext.thisblock, inNext.x, inNext.y, maker);

        if (clone != null) {
            if (inNext.nextBlock != null)
                clone.nextBlock = nextBlockClone(inNext.nextBlock, maker);

            if (inNext.blockPointer != null)
                clone.blockPointer = inNext.blockPointer;

            for (int i = 0; i < inNext.inputBoxes.size(); i++) {
                if (inNext.inputBoxes.get(i).isFull && inNext.inputBoxes.get(i).containedBlock != null) {
                    clone.inputBoxes.get(i).dropAndExpand(bubbleClone(inNext.inputBoxes.get(i).containedBlock, maker));
                } else clone.inputBoxes.get(i).inputBox.setText(inNext.inputBoxes.get(i).inputBox.getText());
            }
            if (inNext.dropDowns != null)
                clone.dropDowns.selected = inNext.dropDowns.selected;
            if (inNext instanceof Contained)
                if (((Contained) inNext).blockcontained != null)
                    ((Contained) clone).blockcontained = ((Contained) inNext).blockcontained.nextBlockClone(((Contained) inNext).blockcontained, maker);
            clone.isplaced = true;

        }

        return clone;
    }


    public void cloneAdder(DragableBlock inClone, MainMaker maker) {
        if (inClone.nextBlock != null) {
            maker.AdderBlocks.add(inClone.nextBlock);
            cloneAdder(inClone.nextBlock, maker);
        }
    }

    public boolean mouseClicked(int mouseX, int mouseY, int mouseButton, MainMaker maker, boolean cancelOverride) {
        if (!cancelOverride)
            if (isplaced && this instanceof Insertable)
                return false;


        if (manyItems != null && !this.minimized) {
            manyItems.chooseClick();
            if (mouseX > this.x + 15 && mouseX < this.x + this.width && mouseY > this.y && mouseY < this.y + 10) {
                manyItems.mouseClicked(mouseButton);
            }
        }
        if (this.dropDowns != null && !this.minimized)
            dropDowns.mouseClicked(mouseX, mouseY);

        if (mouseButton == 1) {
            if (withinSlot(mouseX, mouseY, maker)) {

                if (!(this instanceof SettingObject) && !(this instanceof heldObject) && !(this instanceof heldArray)) {
                    DragableBlock clone = null;

                    if (this instanceof Contained)
                        clone = new Contained(this.thisblock, mouseX, mouseY, maker);
                    if (this instanceof Header)
                        clone = new Header(this.thisblock, mouseX, mouseY, maker);
                    if (this instanceof Default)
                        clone = new Default(this.thisblock, mouseX, mouseY, maker);
                    if (this instanceof Insertable)
                        clone = new Insertable(this.thisblock, mouseX, mouseY, maker);

                    if (clone != null) {
                        clone.x = this.x + 40;
                        if (this.nextBlock != null) {
                            clone.nextBlock = nextBlockClone(this.nextBlock, maker);
                        }
                        if (this.blockPointer != null)
                            clone.blockPointer = blockPointer;

                        for (int i = 0; i < inputBoxes.size(); i++) {
                            if (inputBoxes.get(i).isFull && inputBoxes.get(i).containedBlock != null)
                                clone.inputBoxes.get(i).dropAndExpand(bubbleClone(inputBoxes.get(i).containedBlock, maker));
                            else
                                clone.inputBoxes.get(i).inputBox.setText(inputBoxes.get(i).inputBox.getText());
                        }
                        if (dropDowns != null)
                            clone.dropDowns.selected = dropDowns.selected;

                        clone.isplaced = false;
                        if (this instanceof Contained && ((Contained) this).blockcontained != null)
                            ((Contained) clone).blockcontained = nextBlockClone(((Contained) this).blockcontained, maker);


                        maker.AdderBlocks.add(clone);
                        cloneAdder(clone, maker);
                        return true;
                    }
                }
            }
        }


        ArrayList<InsertableBubble> copybox = new ArrayList<>();
        for (InsertableBubble inputBox : inputBoxes) {
            if (inputBox.isFull && inputBox.containedBlock != null) {
                if (!this.minimized) {
                    copybox.add(inputBox);
                }
            }
        }
        for (InsertableBubble insertableBubble : copybox)
            if (insertableBubble.containedBlock.mouseClicked(mouseX, mouseY, mouseButton, maker, true))
                return true;

        if (withinSlot(mouseX, mouseY, maker)) {
            if (mouseX > this.x - 10 && mouseButton == 0) {
                if (Keyboard.isKeyDown(KEY_LCONTROL) &&
                        this.thisblock.MainBlockTypeGiven != MainBlockType.Header &&
                        this.thisblock.MainBlockTypeGiven != MainBlockType.Contained &&
                        this.thisblock.MainBlockTypeGiven != MainBlockType.PacketHeader) {
                    this.minimized = !this.minimized;
                    return true;
                }
            }
            if (!Keyboard.isKeyDown(KEY_LCONTROL)) {
                if (manyItems != null)
                    manyItems.open = !manyItems.open;

                this.clickedOn = true;

                this.clickedOnX = mouseX;
                this.clickedOnY = mouseY;
                return true;
            }
        }
        return false;
    }

    // Right so we do this so that we need not have movement interrupts... Could redo but time-consuming
    public void drawScreen(int mouseX, int mouseY, MainMaker maker, boolean cancelOverride) {
        if ((this instanceof Insertable || this instanceof Default || this instanceof heldArray || this instanceof heldObject || this instanceof SettingObject)
                && (((this.y > maker.height + 50 || this.y < -50) ||
                ((this.x < -50 && this.x + width < -50) || (this.x + width > maker.width + 50 && this.x > maker.width + 50))) && maker.hasmoved))
            return;

        if (!cancelOverride)
            if (isplaced && this instanceof Insertable)
                return;
        if (this.clickedOn) {
            if (mouseX > clickedOnX + 2 || mouseX < clickedOnX - 2 || mouseY > clickedOnY + 2 || mouseY < clickedOnY - 2) {
                this.clickedOn = false;
                dragX = mouseX - x;
                dragY = mouseY - y;
                this.isDragging = true;
            }
        }
        if (this.isDragging) {
            if (this.isplaced) {
                this.isplaced = false;
                for (DragableBlock activeBlock : maker.module.ActiveBlocks) {
                    for (InsertableBubble inputBox : activeBlock.inputBoxes) {
                        if (inputBox.isFull && inputBox.containedBlock != null && inputBox.containedBlock.equals(this)) {
                            inputBox.isFull = false;
                            inputBox.containedBlock = null;
                        }
                    }
                }
            }
            this.x = (mouseX - dragX);
            this.y = (mouseY - dragY);

            for (DragableBlock staticBlock : maker.module.ActiveBlocks) {
                if (this == staticBlock)
                    continue;
                if (!(staticBlock instanceof Insertable) && !(this instanceof Header) && !(this instanceof Insertable)) {
                    if (staticBlock.nextBlock == null) {
                        staticBlock.isGlowingBelow = staticBlock.belowSlot(mouseX, mouseY, maker);
                        if (staticBlock instanceof Contained) {
                            Contained block = (Contained) staticBlock;
                            block.loopGlow = block.withinloop(mouseX, mouseY, maker);
                        }
                    }
                }
                for (InsertableBubble inputBox : staticBlock.inputBoxes) {
                    if ((thisblock.MainBlockTypeGiven == MainBlockType.Boolean && inputBox.onlyBoolean) || thisblock.MainBlockTypeGiven == MainBlockType.Wild)
                        inputBox.setGlowing(staticBlock.withinSlot(mouseX, mouseY, maker) && !inputBox.IsFull() && inputBox.withinSlot(mouseX, mouseY));
                    else if (!inputBox.onlyBoolean && inputBox.acceptsType(thisblock.MainBlockTypeGiven) || thisblock.MainBlockTypeGiven == MainBlockType.Wild) {
                        inputBox.setGlowing(staticBlock.withinSlot(mouseX, mouseY, maker) && !inputBox.IsFull() && inputBox.withinSlot(mouseX, mouseY));
                    }
                }
            }
        } else {
            if (!this.minimized) {
                if (withinSlot(mouseX, mouseY, maker)) {
                    for (InsertableBubble inputBox : inputBoxes) {
                        if (inputBox.withinSlot(mouseX, mouseY) && !inputBox.isFull) {
                            inputBox.displayTypes(mouseX, mouseY);
                        }
                    }
                    if (mouseX < this.x + 1 || mouseX > this.x - 1 + this.width) {
                        hovertime++;
                        if (thisblock.MainBlockTypeGiven == null)
                            return;
                        StringBuilder TypesGiven = new StringBuilder();
                        TypesGiven.append(thisblock.MainBlockTypeGiven);
                        if (hovertime > 165) {
                            if (thisblock.description != null) {
                                TypesGiven.append("\n");
                                TypesGiven.append(thisblock.description);
                            }
                        }
                        drawHoverTip(mouseX, mouseY, TypesGiven);
                        hover = true;
                    } else {
                        hover = false;
                        hovertime = 0;
                    }
                } else {
                    hover = false;
                    hovertime = 0;
                }
            } else {
                // here we are minimized
                // if key alt is held
                if (Keyboard.isKeyDown(KEY_LMENU)) {
                    if (withinSlot(mouseX, mouseY, maker)) {
                        if (thisblock.MainBlockTypeGiven == null)
                            return;
                        StringBuilder blocksContained = new StringBuilder();

                        if (inputBoxes.size() == 0)
                            return;
                        for (InsertableBubble inputBox : this.inputBoxes) {
                            if (inputBox.isFull)
                                appendNamesRecursively(inputBox, blocksContained);
                            else
                                blocksContained.append(inputBox.inputBox.text).append("\n");
                        }
                        drawHoverTip(mouseX, mouseY, blocksContained);
                    }
                }
                hover = false;
                hovertime = 0;
            }
        }

        if (nextBlock != null) {
            if (nextBlock.isDragging) {
                nextBlock = null;
            } else {
                if (this instanceof Contained) {
                    nextBlock.x = this.x;
                    nextBlock.y = this.y + 41 + ((Contained) this).yCalculator();
                } else {
                    if (this instanceof Header)
                        nextBlock.x = this.x + 10;
                    else
                        nextBlock.x = this.x;
                    nextBlock.y = this.y + 21;
                }
            }
        }

        if (manyItems != null) {
            this.manyItems.draw((this.x + offsetx), (this.y), mouseX, mouseY);
        } // we draw here to give mouse!
        if (this.dropDowns != null && !this.minimized)
            this.dropDowns.drawScreen(mouseX, mouseY);
        Blockdraw(mouseX, mouseY, maker);

        for (InsertableBubble inputBox : inputBoxes) {
            if (inputBox.isFull && inputBox.containedBlock != null && inputBox.containedBlock.isplaced) {
                inputBox.containedBlock.x = this.x + (inputBox.inputBox.x - this.x) + 8;
                inputBox.containedBlock.y = this.y;
                if (!this.minimized)
                    inputBox.containedBlock.drawScreen(mouseX, mouseY, maker, true);
            }
        }
    }

    private StringBuilder appendNamesRecursively(InsertableBubble insertableBubble, StringBuilder blocksContained) {
        if (insertableBubble.containedBlock == null)
            return blocksContained;
        blocksContained.append(insertableBubble.containedBlock.thisblock.getName()).append("\n");
        if (insertableBubble.containedBlock.inputBoxes != null)
            for (InsertableBubble inputBox : insertableBubble.containedBlock.inputBoxes) {
                if (inputBox.isFull) {
                    blocksContained = appendNamesRecursively(inputBox, blocksContained);
                } else {
                    blocksContained.append(inputBox.inputBox.text).append("\n");
                }

            }
        return blocksContained;
    }

    static void drawHoverTip(int mouseX, int mouseY, StringBuilder bob) {
        String[] lines = bob.toString().split("\n");
        int longest = 0;
        for (String line : lines) {
            if (Minecraft.getMinecraft().fontRenderer.getStringWidth(line) > longest)
                longest = Minecraft.getMinecraft().fontRenderer.getStringWidth(line);
        }

        Gui.drawRect(mouseX, mouseY + 20, mouseX + longest, mouseY + 10 + (lines.length * 10), new Color(82, 82, 82, 195).getRGB());

        for (int i = 0; i < lines.length; i++) {
            Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(lines[i], mouseX + 2, mouseY + 12 + (i * 10), -1);
        }
    }


    // This is just our header to override for each Dragable block, see at
    public void Blockdraw(int mouseX, int mouseY, MainMaker maker) {
    }

    // This is on each draw cycle
    public void dragableinput(ArrayList<BlockObjects> blockItems, MainMaker maker) {

        this.drawleft(maker); // offsetx is set to 10
        if (this.minimized) {
            drawText(this.thisblock.getName().length() > 3 ? this.thisblock.getName().substring(0, 3) + "." : this.thisblock.getName());
            blockItems.stream().filter(blockItem -> blockItem.equals(BlockObjects.NumericalTextEnter) || blockItem.equals(BlockObjects.BooleanTextEnter)).
                    forEach(blockItem -> inputBoxes.forEach(inputBox -> inputBox.inputBox.x = this.x + offsetx));

        } else {
            if (this.thisblock instanceof GetArray || this.thisblock instanceof GetCombo || this.thisblock instanceof GetCheck || this.thisblock instanceof GetSlider ||
                    this.thisblock instanceof GetColor || this.thisblock instanceof GetVarBoolean || this.thisblock instanceof GetVarNum || this.thisblock instanceof GetVarObj ||
                    this.thisblock instanceof AddDel)
                drawText(this.blockPointer.thisblock.getName());

            if (this.thisblock instanceof SetVarBoolean || this.thisblock instanceof SetVarNum || this.thisblock instanceof SetVarObj)
                drawText("Set " + this.blockPointer.thisblock.getName() + " to");
            this.drawFiller(blockItems);
        }
        if (this instanceof Default || this instanceof Header)
            drawNubs(maker);
        this.drawright(maker);

    }

    private void drawNubs(MainMaker maker) {
        if (isGlowingBelow)
            Gui.drawRect(x, y + 15, x + 30, y + 20, new Color(231, 16, 16, 100).getRGB());
        else
            Gui.drawRect(x, y + 15, x + 30, y + 20, new Color(16, 231, 141, 190).getRGB());
        if (this instanceof Default && !this.isplaced)
            RenderUtils.drawRectOutline(x, y - 5, x + 30, y, 1, ColorUtils.rainbow().getRGB());

    }

    private void drawFiller(ArrayList<BlockObjects> blockItems) {
        int textnum = 0;
        int pos = 0;
        for (BlockObjects blockItem : blockItems) {
            switch (blockItem) {
                case Words:
                    drawText(this.thisblock.words[textnum]);
                    textnum++;
                    break;
                case Name:
                    drawText(this.thisblock.getName());
                    break;
                case BlockSearch:
                case ItemSearch:
                    offsetx += 45;
                    break;
                case DropDown:
                    this.dropDowns.x = this.x + offsetx;
                    this.dropDowns.y = this.y + 3;
                    // drawBubble(this.x, this.y, this.dropDowns.get(dpos).width);
                    this.offsetx += this.dropDowns.getWidth() + 3;
                    break;
                case NumericalTextEnter:
                case BooleanTextEnter:
                    inputBoxes.get(pos).inputBox.x = this.x + offsetx + 2;
                    inputBoxes.get(pos).inputBox.y = this.y + 3;
                    if (!inputBoxes.get(pos).isFull)
                        inputBoxes.get(pos).inputBox.drawTextBox();
                    if (this.inputBoxes.get(pos).isGlowing()) {
                        if (this.inputBoxes.get(pos).onlyBoolean)
                            Gui.drawRect(x + offsetx, y, x + inputBoxes.get(pos).getWidth() + offsetx, y + 15, new Color(245, 255, 1, 195).getRGB());
                        else
                            RenderUtils.drawCircfilled(x + offsetx + 11, y + 7, 7, new Color(245, 255, 1, 195).getRGB(), 1.75, 1);
                    }
                    if (this.inputBoxes.get(pos).isFull)
                        offsetx += inputBoxes.get(pos).getWidth();
                    else
                        drawBubble(this.x, this.y, inputBoxes.get(pos).getWidth(), this.inputBoxes.get(pos).onlyBoolean);
                    pos++;
                    break;

            }
        }
    }

    public DragableBlock forwardBlock() {
        if (nextBlock != null)
            return nextBlock.forwardBlock();
        return this;
    }

    public void drawBubble(int x, int y, int length, boolean onlyBoolean) {

        RenderUtils.drawRectTopBottom(x + offsetx, y, x + length + offsetx, y + 15, 1, ColorUtils.rainbow().getRGB()); // Box

        if (onlyBoolean)
            Gui.drawRect(x + offsetx, y, x + length + offsetx, y + 15, new Color(100, 252, 95, 50).getRGB());
        else
            RenderUtils.drawCircfilled(x + offsetx + (length / 2), y + 7, 7, new Color(100, 252, 95, 50).getRGB(), 1 + (length / 20), 1);


        offsetx += length;
    }

    // Overwritten by each DragableBlockType
    public void mouseReleased(int mouseX, int mouseY, int state, MainMaker maker) {
        if (manyItems != null)
            this.manyItems.mouseReleased(mouseX, mouseY, state);
        if (!this.isDragging && !this.minimized) {
            for (InsertableBubble inputBox : this.inputBoxes) {
                if (!inputBox.isFull) {
                    if (inputBox.typesAccepted != null)
                        if (inputBox.typesAccepted.contains(MainBlockType.Numbers) || inputBox.typesAccepted.contains(MainBlockType.String) || inputBox.typesAccepted.contains(MainBlockType.Boolean)) {

                            inputBox.inputBox.mouseClicked(mouseX, mouseY, 0);
                        } else inputBox.inputBox.isFocused = false;
                } else {
                    inputBox.inputBox.isFocused = false;
                }
            }
        }
        this.clickedOn = false;
        this.isDragging = false;
    }

    public boolean withinSlot(int mouseX, int mouseY, MainMaker maker) {
        if (mouseX > this.x - 8 && mouseX < this.x + 8 + this.offsetx) {
            if (mouseY > this.y - 10 && mouseY < this.y + 20) {
                return true;
            }
        }
        return false;
    }

    // Are we below the slot, for adding a block in a chain onto it
    public boolean belowSlot(int mouseX, int mouseY, MainMaker maker) {
        if (mouseX > this.x - 25 && mouseX < this.x + 35) {
            if (mouseY > this.y + 10 && mouseY < this.y + 45) {
                return true;
            }
        }
        return false;
    }

    protected void drawleft(MainMaker maker) {
        int adder = 0;
        if (hover)
            adder += 3;
        if (thisblock.MainBlockTypeGiven == MainBlockType.Boolean)
            RenderUtils.drawline(y - adder, x - 2, y + 15 + adder, 1, niceColor.getRGB());
        else
            RenderUtils.drawCirc(x, y + 7, 8 + adder, niceColor.getRGB(), Math.PI / 2, Math.PI + Math.PI / 2); // left

        offsetx = 0;
    }

    protected void drawright(MainMaker maker) {
        int adder = 0;
        if (hover)
            adder += 3;
        if (thisblock.MainBlockTypeGiven == MainBlockType.Boolean)
            RenderUtils.drawline(y - adder, x + offsetx, y + 15 + adder, 1, niceColor.getRGB());
        else
            RenderUtils.drawCirc(x + offsetx, y + 7, 8 + adder, niceColor.getRGB(), -(Math.PI / 2), Math.PI / 2);// Right
        this.width = offsetx + 2;
    }

    public int getWidth() {
        return this.width;
    }

    protected void drawText(String s) {
        //    Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(s, x + offsetx, y + 4, -1);
        CFont.afontRenderer16.drawStringNoShadow(s, x + offsetx, y + 4, -1);
        // offsetx += Wrapper.mc.fontRenderer.getStringWidth(s) ;
        offsetx += CFont.afontRenderer16.getStringWidth(s) + 2;
    }

    public void updateScreen() {
        for (InsertableBubble inputBox : this.inputBoxes) {
            inputBox.inputBox.updateCursorCounter();
        }
        if (this.dropDowns != null && !this.minimized)
            this.dropDowns.updateScreen();
    }

    public void keyTyped(char typedChar, int keyCode) {
        if (manyItems != null)
            this.manyItems.keyTyped(typedChar, keyCode);
        for (InsertableBubble inputBox : this.inputBoxes) {
            if (inputBox.inputBox.isFocused() && !inputBox.isFull)
                inputBox.inputBox.textboxKeyTyped(typedChar, keyCode);
            inputBox.inputBox.width = Math.max(inputBox.inputBox.text.length() * 8, 20);
        }
        if (this.dropDowns != null && !this.minimized)
            this.dropDowns.keyTyped(typedChar, keyCode);
    }

    public void runCode(Object event) {
        this.thisblock.runCode(this, event);
    }

    public int getBottom() {
        if (this instanceof Contained) {
            return ((Contained) this).yCalculator() + this.y + 20;
        } else {
            return this.y + 10;
        }
    }

    public ArrayList<DragableBlock> allcontained() {
        ArrayList<DragableBlock> all = new ArrayList<>();
        all.add(this);
        for (InsertableBubble inputBox : this.inputBoxes) {
            if (inputBox.isFull && inputBox.containedBlock != null)
                all.addAll(inputBox.containedBlock.allcontained());
        }

        if (this.nextBlock != null)
            all.addAll(this.nextBlock.allcontained());
        if (this instanceof Contained && ((Contained) this).blockcontained != null)
            all.addAll(((Contained) this).blockcontained.allcontained());

        return all;
    }
}
