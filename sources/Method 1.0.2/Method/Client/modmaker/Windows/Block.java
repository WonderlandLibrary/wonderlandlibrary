package Method.Client.modmaker.Windows;


import Method.Client.modmaker.*;
import Method.Client.modmaker.Windows.DragableBlockTypes.Contained;
import Method.Client.modmaker.Windows.DragableBlockTypes.Default;
import Method.Client.modmaker.Windows.DragableBlockTypes.Header;
import Method.Client.modmaker.Windows.DragableBlockTypes.Insertable;
import Method.Client.utils.font.CFontRenderer;
import Method.Client.utils.visual.ColorUtils;
import Method.Client.utils.visual.RenderUtils;
import net.minecraft.client.gui.Gui;

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

import static Method.Client.utils.font.CFont.afontRenderer16;
import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.glEnable;

public class Block implements Serializable {

    public MainBlockType MainBlockTypeGiven;
    public Headers header;
    public int StaticX = 50;
    public int StaticY = 50;
    public int height = 20;
    public int offsetY;
    public String[] words = new String[10];

    public void setName(String name) {
        this.name = name;
    }

    private String name;
    public Tabs tab;
    public String description = "";
    public int scrollBlock;
    public static CFontRenderer blockFont;
    private final ArrayList<BlockObjects> blockItems = new ArrayList<>();
    public ArrayList<ArrayList<MainBlockType>> typesAccepted = new ArrayList<>();
    public ArrayList<String> ddOptions = new ArrayList<>();


    public ArrayList<Class> packetclasses = new ArrayList<>();

    public Block(String name, MainBlockType blockType, Tabs tab, BlockObjects... types) {

        this.name = name;
        this.tab = tab;
        this.MainBlockTypeGiven = blockType;

        if (!name.isEmpty()) {
            blockFont = afontRenderer16;
        }

        this.blockItems.addAll(Arrays.asList(types)); // Add the parts of the block!
    }

    public Block(String name, MainBlockType blockType, Tabs tab, BlockObjects types, Headers headers) {
        this.name = name;
        this.tab = tab;
        this.MainBlockTypeGiven = blockType;
        this.header = headers;
        if (!name.isEmpty()) {
            blockFont = afontRenderer16;
        }
        this.blockItems.add(types); // Add the parts of the block!
        // for header
    }

    public Block(String name, MainBlockType blockType, Tabs tab, Headers headers, BlockObjects... types) {
        this.name = name;
        this.tab = tab;
        this.MainBlockTypeGiven = blockType;
        this.header = headers;
        if (!name.isEmpty()) {
            blockFont = afontRenderer16;
        }
        this.blockItems.addAll(Arrays.asList(types)); // Add the parts of the block!
        // for header
    }


    public ArrayList<BlockObjects> getBlockItems() {
        return this.blockItems;
    }

    public ArrayList<MainBlockType> tryTypes(int typesAccepted) {
        if (this.typesAccepted.isEmpty())
            return null;

        return this.typesAccepted.get(typesAccepted);
    }

    public ArrayList<MainBlockType> typesCollapse(MainBlockType... types) {
        return new ArrayList<>(Arrays.asList(types));
    }

    /**
     * This is for our static part of the block
     */

    public void renderComponent() {
        if (this.StaticY + (scrollBlock * 10) + this.offsetY + (height) + (this.tab == Tabs.Variables || this.tab == Tabs.Arrays ? 50 : 0) < 45)
            return;

        RenderUtils.drawRectOutline(StaticX, this.StaticY + (scrollBlock * 10) + (this.offsetY), StaticX + 90, this.StaticY + (scrollBlock * 10) + (this.offsetY) + height, 1, ColorUtils.rainbow().getRGB());
        Gui.drawRect(StaticX, this.StaticY + (scrollBlock * 10) + (this.offsetY), StaticX + 90, this.StaticY + (scrollBlock * 10) + (this.offsetY) + height, new Color(45, 45, 45, 205).getRGB());

        glEnable(GL_BLEND);

        blockFont.drawStringNoShadow(this.getName(), (int) this.StaticX + 48 - (blockFont.getStringWidth(this.getName()) / 2), (int) this.StaticY + (scrollBlock * 10) + this.offsetY + 5, new Color(222, 218, 218, 255).getRGB());


        if (this.parentBlock == this) {
            RenderUtils.drawRectOutline(StaticX + 95, this.StaticY + (scrollBlock * 10) + (this.offsetY), StaticX + 115, this.StaticY + (scrollBlock * 10) + (this.offsetY) + height, 1, Color.YELLOW.getRGB());
        }
    }

    public void drawDescription(int mouseX, int mouseY) {
        if (this.description.length() > 0 && mouseWithinBlock(mouseX, mouseY)) {
            Gui.drawRect(mouseX + 50, mouseY, mouseX + 250, mouseY + 75, new Color(124, 124, 124, 195).getRGB());
            glEnable(GL_BLEND);

            // if text is too long, we need to split it up
            String[] split = this.description.split("\n");
            for (int i = 0; i < split.length; i++) {
                blockFont.drawStringNoShadow(split[i], mouseX + 55, mouseY + 5 + (i * 10), new Color(222, 218, 218, 255).getRGB());
            }

        }
    }


    public void mouseClicked(int mouseX, int mouseY, int button, MainMaker maker) {
        if (button == 0) {
            if (this.parentBlock == this && extraClick(mouseX, mouseY)) {
                maker.tempparent = this;
            }
            if (mouseWithinBlock(mouseX, mouseY)) createBlock(mouseX, mouseY, maker);
        }
    }

    public boolean mouseWithinBlock(int mouseX, int mouseY) {
        if (this.StaticY + (scrollBlock * 10) + this.offsetY + (height) + (this.tab == Tabs.Variables || this.tab == Tabs.Arrays ? 50 : 0) > 45)
            return withinBoundsComponent(mouseX, mouseY);
        return false;
    }


    private void createBlock(int mouseX, int mouseY, MainMaker maker) {
        switch (this.MainBlockTypeGiven) {
            case Header:
            case PacketHeader:
                maker.module.ActiveBlocks.add(new Header(this, mouseX, mouseY + (scrollBlock * 10), maker));
                break;
            case Boolean:
            case Array:
            case Setting:
            case Numbers:
            case Timer:
            case Entity:
            case Null:
            case Worlds:
            case Gui:
            case Items:
            case Blocks:
            case Facing:
            case Chunk:
            case BlockPos:
            case String:
            case EnumHand:
            case UUID:
            case ClickType:
            case IBlockState:
            case ClientStatus:
            case EntityAction:
            case PlayerDigging:
            case EnumHandSide:
            case Vec3d:
            case Hole:
            case HoleType:
            case ScoreAction:
            case ChatVisibility:
            case ConnectionState:
            case SoundCategory:
            case SoundEvent:
            case RecipeBook:
            case CombatEvent:
            case ItemStack:
            case EnumDifficulty:
            case BoundingBox:
            case ParticleTypes:
            case PackStatus:
            case TitleType:
            case Potion:
            case ListItem:
            case GameType:
            case EquipmentSlot:
            case UseEntity:
            case PlaceLocation:
            case ElementType:
            case TileEntity:
            case RayTraceResult:
            case Wild:
                maker.module.ActiveBlocks.add(new Insertable(this, mouseX, mouseY + (scrollBlock * 10), maker));
                break;
            case Contained:
                maker.module.ActiveBlocks.add(new Contained(this, mouseX, mouseY + (scrollBlock * 10), maker));
                break;
            case Default:
                maker.module.ActiveBlocks.add(new Default(this, mouseX, mouseY + (scrollBlock * 10), maker));
                break;
        }
    }


    private boolean extraClick(int mouseX, int mouseY) {
        if (mouseX > this.StaticX + 95 && mouseX < this.StaticX + 115) {
            if (mouseY > this.StaticY + (scrollBlock * 10) + this.offsetY && mouseY < this.StaticY + (scrollBlock * 10) + this.offsetY + (height)) {
                return true;
            }
        }
        return false;
    }

    private boolean withinBoundsComponent(int mouseX, int mouseY) {
        if (mouseX > this.StaticX && mouseX < this.StaticX + 90) {
            if (mouseY > this.StaticY + (scrollBlock * 10) + this.offsetY && mouseY < this.StaticY + (scrollBlock * 10) + this.offsetY + (height)) {
                return true;
            }
        }
        return false;
    }

    public static int[] arrayAdd(int[] array, double solveNum) {
        int[] newArray = new int[array.length + 1];
        System.arraycopy(array, 0, newArray, 0, array.length + 1);
        newArray[array.length + 1] = (int) solveNum;
        return newArray;
    }

    public static int[] arrayRemove(int[] array, double solveNum) {
        for (int i = 0; i < array.length; i++) {
            if (array[i] == (int) solveNum) {
                array[i] = 0;
            }
        }
        return array;
    }

    public static String[] arrayAdd(String[] array, String solveNum) {
        String[] newArray = new String[array.length + 1];
        System.arraycopy(array, 0, newArray, 0, array.length + 1);
        newArray[array.length + 1] = (String) solveNum;
        return newArray;
    }

    public static String[] arrayRemove(String[] array, String solveNum) {
        for (int i = 0; i < array.length; i++) {
            if (Objects.equals(array[i], solveNum)) {
                array[i] = null;
            }
        }
        return array;
    }

    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
    }

    public void keyTyped(char typedChar, int key) {
    }

    public String getName() {
        return name;
    }

    public Tabs getCategory() {
        return tab;
    }

    public void addScroll(int scroll) {
        this.scrollBlock = scroll;
    }

    // These are (HAVE) to be @Overwritten

    public void finalInit(DragableBlock dragableBlock) {
    }

    //          super.runCode(dragableBlock, event);
    public void runCode(DragableBlock dragableBlock, Object event) {
        if (dragableBlock.nextBlock != null)
            dragableBlock.nextBlock.thisblock.runCode(dragableBlock.nextBlock, event);
    }
    // dont run code on this block if called
    public void runCode() {
    }

    public double runCodeSolve(DragableBlock dragableBlock, Object event) {
        return 0;
    }

    public boolean runCodeBoolean(DragableBlock dragableBlock, Object event) {
        return false;
    }

    public Object runCodeObject(DragableBlock dragableBlock, Object event) {
        return false;
    }

    public Block parentBlock;

    public void setparentBlock(Block declaredClass) {
        parentBlock = declaredClass;
    }

}
