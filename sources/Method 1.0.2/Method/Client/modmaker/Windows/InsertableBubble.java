package Method.Client.modmaker.Windows;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.utils.visual.SerializableGuiTextField;

import java.io.Serializable;
import java.util.ArrayList;

public class InsertableBubble implements Serializable {

    public SerializableGuiTextField inputBox;
    private boolean glowing;
    public boolean isFull;
    public DragableBlock containedBlock;
    public int saverContainedBlock;

    public boolean onlyBoolean = false; // this is what the box accepts
    public ArrayList<MainBlockType> typesAccepted;

    public boolean withinSlot(int mouseX, int mouseY) {
        if (mouseX > this.inputBox.x - 2 && mouseX < this.inputBox.x + 2 + this.inputBox.width)
            if (mouseY > this.inputBox.y - 5 && mouseY < this.inputBox.y + 20)
                return true;
        return false;
    }

    public boolean acceptsType(MainBlockType mainBlockType) {
        if (this.typesAccepted != null)
            return this.typesAccepted.contains(mainBlockType) || this.typesAccepted.contains(MainBlockType.Wild);
        return false;
    }

    public void dropAndExpand(DragableBlock containedBlock) {
        this.containedBlock = containedBlock;
        this.isFull = true;
    }

    public void displayTypes(int mouseX, int mouseY) {
        if (this.typesAccepted == null || this.typesAccepted.size() < 1)
            return;


        StringBuilder bob = new StringBuilder();
        this.typesAccepted.forEach(mainBlockType -> bob.append(mainBlockType.name()).append(" "));

        DragableBlock.drawHoverTip(mouseX, mouseY, bob);
    }

    public InsertableBubble(SerializableGuiTextField field, BlockObjects blockObjects, ArrayList<MainBlockType> mainBlockTypes) {
        if (blockObjects == BlockObjects.BooleanTextEnter)
            onlyBoolean = true;
        if (mainBlockTypes != null)
            this.typesAccepted = mainBlockTypes;
        field.setMaxStringLength(500);
        field.setEnableBackgroundDrawing(false);
        inputBox = field;
        this.isFull = false;
    }

    public void addBubble(SerializableGuiTextField field) {
        field.setMaxStringLength(500);
        field.setEnableBackgroundDrawing(false);
        inputBox = field;
    }

    public int getWidth() {
        int defaultWidth = 18;
        if (this.inputBox.getText().length() > 3)
            defaultWidth += this.inputBox.getText().length() * 5;
        if (containedBlock != null)
            defaultWidth += containedBlock.getWidth() - 2;
        return defaultWidth;
    }

    public void setGlowing(boolean b) {
        this.glowing = b;
    }

    public SerializableGuiTextField getInputBox() {
        return inputBox;
    }

    public boolean IsFull() {
        return isFull;
    }

    public boolean isGlowing() {
        return glowing;
    }

}

