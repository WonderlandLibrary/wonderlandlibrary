package club.cloverhook.ui.main.components.tab.cheat;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

import java.awt.*;
import java.util.ArrayList;

import club.cloverhook.ui.main.Interface;
import club.cloverhook.utils.ColorCreator;
import club.cloverhook.utils.Draw;
import club.cloverhook.utils.font.Fonts;
import club.cloverhook.utils.property.abs.Property;
import club.cloverhook.utils.property.impl.ItemsProperty;

/**
 * @author antja03
 */
public class PropertyItemSelection extends PropertyComponent {

    private ItemsProperty itemsProperty;
    private final ArrayList<Item> itemsDrawn = new ArrayList<>();
    private boolean extended;
    private int scrollIndex;

    public PropertyItemSelection(Interface theInterface, Property property, double x, double y, double width, double height) {
        super(theInterface, property, x, y, width, height);
        if (property instanceof ItemsProperty) {
            itemsProperty = (ItemsProperty) property;
            for (Item item : Item.itemRegistry) {
                if (!(item instanceof ItemBlock) && itemsProperty.onlyBlocks())
                    continue;

                itemsDrawn.add(item);
            }
        }
    }

    @Override public void drawComponent(double x, double y) {
        this.positionX = x - theInterface.getPositionX();
        this.positionY = y - theInterface.getPositionY();

        double boxPosX = x + 80;
        double boxPosY = y + 2;
        double boxWidth = 60;
        double boxHeight = 10;

        if (extended) {
            boxHeight = 16 * (5);
            boxWidth = 131;
        }

        Fonts.f14.drawString(getProperty().getId(), x + 6, y + maxHeight / 2 - 5.5, theInterface.getColor(255, 255, 255));

        Draw.drawRectangle(boxPosX, boxPosY, boxPosX + boxWidth, boxPosY + boxHeight, new Color(24, 24, 24).getRGB());

        double barHeight = boxHeight;

        if (extended) {
            double div = barHeight / (itemsDrawn.size() / 8);
            barHeight -= (itemsDrawn.size() / 8 - 5) * div;
            double barPosition = div * scrollIndex;

            Draw.drawRectangle(boxPosX + boxWidth, boxPosY + barPosition, boxPosX + boxWidth - 1, boxPosY + barPosition + barHeight, theInterface.getColor(255, 255, 255));
        }

        if (extended) {
            double yAdd = 0;
            double xAdd = 0;
            for (Item item : itemsDrawn) {
                if (itemsDrawn.indexOf(item) < scrollIndex * 8 || yAdd > 16 * 4)
                    continue;

                double optionPosX = boxPosX + xAdd;
                double optionPosY = boxPosY + yAdd;

                if (itemsProperty.containsItem(item)) {
                    Draw.drawRectangle(optionPosX, optionPosY, optionPosX + 16, optionPosY + 16, new Color(10, 10, 10).getRGB());
                }

                RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();
                renderItem.renderItemIntoGUI(new ItemStack(item), (int) optionPosX, (int) optionPosY);

                xAdd += 16;
                if ((itemsDrawn.indexOf(item) + 1) % 8 == 0) {
                    yAdd += 16;
                    xAdd = 0;
                }
            }
        } else {
            Fonts.f12.drawCenteredString(getBoxLabel(), boxPosX + boxWidth / 2, boxPosY + boxHeight / 2 - 1, theInterface.getColor(255, 255, 255));
        }

        if (theInterface.isMouseInBounds(
                theInterface.getPositionX() + positionX + 4,
                theInterface.getPositionX() + positionX + 8 + Fonts.f12.getStringWidth(getProperty().getId()),
                theInterface.getPositionY() + positionY + maxHeight / 2 - 7.5,
                theInterface.getPositionY() + positionY + maxHeight / 2 - 3.5 + Fonts.f12.getStringHeight(getProperty().getId()))) {

            String desc = getProperty().getDescription();
            ArrayList<String> list = new ArrayList<> (Fonts.f14.wrapWords(desc, 102));
            if (list.size() > 1) {
                double boxWidth2 = -1;
                for (String string : list) {
                    if (Fonts.f12.getStringWidth(string) > boxWidth2) {
                        boxWidth2 = Fonts.f14.getStringWidth(string);
                    }
                }
                Draw.drawRectangle(theInterface.getCurrentFrameMouseX() + 4, theInterface.getCurrentFrameMouseY(),
                        theInterface.getCurrentFrameMouseX() + 4 + boxWidth2, theInterface.getCurrentFrameMouseY() + 8 * list.size(), theInterface.getColor(40, 40, 40));
                for (String string : list) {
                    Fonts.f12.drawString(string, theInterface.getCurrentFrameMouseX() + 5, theInterface.getCurrentFrameMouseY() + 2 + list.indexOf(string) * 8, theInterface.getColor(255, 255, 255));
                }
            } else {
                double boxWidth2 = Fonts.f12.getStringWidth(desc);
                Draw.drawRectangle(theInterface.getCurrentFrameMouseX() + 4, theInterface.getCurrentFrameMouseY(),
                        theInterface.getCurrentFrameMouseX() + 4 + boxWidth2, theInterface.getCurrentFrameMouseY(), theInterface.getColor(40, 40, 40));
                Fonts.f12.drawString(desc, theInterface.getCurrentFrameMouseX() + 5, theInterface.getCurrentFrameMouseY() + 2, theInterface.getColor(255, 255, 255));
            }
        }
    }

    @Override public boolean mouseButtonClicked(int button) {
        double boxPosX = positionX + 80;
        double boxPosY = positionY + 2;
        double boxWidth = 60;
        double boxHeight = 10;

        if (!extended) {
            if (theInterface.isMouseInBounds(
                    theInterface.getPositionX() + boxPosX,
                    theInterface.getPositionX() + boxPosX + boxWidth,
                    theInterface.getPositionY() + boxPosY,
                    theInterface.getPositionY() + boxPosY + boxHeight)) {
                extended = true;
                return true;
            }
        } else {
            boxHeight = 16 * (5);
            boxWidth = 131;
            double yAdd = 0;
            double xAdd = 0;
            for (Item item : itemsDrawn) {
                if (itemsDrawn.indexOf(item) < scrollIndex * 8 || yAdd > 16 * 4)
                    continue;

                double optionPosX = theInterface.getPositionX() + boxPosX + xAdd;
                double optionPosY = theInterface.getPositionY() + boxPosY+ yAdd;

                if (theInterface.isMouseInBounds(optionPosX, optionPosX + 16, optionPosY, optionPosY + 16)) {
                    if (itemsProperty.containsItem(item)) {
                        itemsProperty.removeItem(item);
                    } else {
                        itemsProperty.addItem(item);
                    }
                    return true;
                }

                xAdd += 16;
                if ((itemsDrawn.indexOf(item) + 1) % 8 == 0) {
                    yAdd += 16;
                    xAdd = 0;
                }
            }
            extended = false;
        }
        return false;
    }

    @Override public void mouseScrolled(final int scrollDirection) {
        if (extended) {
            double boxPosX = positionX + 80;
            double boxPosY = positionY + 2;
            double boxWidth = 131;
            double  boxHeight = 16 * (5);

            if (scrollDirection == 1) {
                if (scrollIndex < itemsDrawn.size() / 8 - 5) {
                    scrollIndex += 1;
                }
            } else {
                if (scrollIndex > 0) {
                    scrollIndex -= 1;
                }
            }


            if (theInterface.isMouseInBounds(boxPosX, boxPosX + boxWidth, boxPosY, boxPosY + boxHeight)) {
                if (scrollDirection == 1) {
                    if (scrollIndex < (itemsDrawn.size() / 8) - 5) {
                        scrollIndex += 1;
                    }
                } else {
                    if (scrollIndex > 0) {
                        scrollIndex -= 1;
                    }
                }
            }
        }
    }

    public void onGuiClose() {
        extended = false;
    }

    public String getBoxLabel() {
        if (itemsProperty.getValue().isEmpty()) {
            return "";
        } else {
            String finalString = "";
            for (Item item : itemsProperty.getValue()) {
                finalString += new ItemStack(item).getDisplayName() + ", ";
            }
            finalString = finalString.substring(0, finalString.length() - 2);
            ArrayList<String> strings = new ArrayList<>(Fonts.f14.wrapWords(finalString, 80));
            finalString = Fonts.f14.wrapWords(finalString, 80).get(0);
            if (strings.size() > 1) {
                finalString = finalString.substring(0, finalString.length() - 3);
                finalString += "...";
            }
            return finalString;
        }
    }

    public boolean isExtended() {
        return extended;
    }
}
