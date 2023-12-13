package dev.rise.ui.script;

import dev.rise.Rise;
import dev.rise.font.CustomFont;
import dev.rise.util.render.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.input.Keyboard;

import java.awt.*;

public class GuiScriptEditor {

    //This doesn't work its to hard

    private static final Minecraft mc = Minecraft.getMinecraft();

    private static float screenWidth, screenHeight;
    private static float windowWidth, windowHeight;
    private static boolean dragged;
    private static float windowX, windowY;
    private static int topBarHeight, bottomBarHeight;
    private static float dragOffsetX, dragOffsetY;
    private static String contents = "";
    private static int curserPosition;
    private static float codeStartX, codeStartY;

    public static void onInit() {

        //Gets scaled resolution
        final ScaledResolution sr = new ScaledResolution(mc);

        //Sets screen height and width variables
        screenWidth = sr.getScaledWidth();
        screenHeight = sr.getScaledHeight();

        //Sets starting window dimensions
        windowWidth = 430;
        windowHeight = 270;

        //Setting window position
        windowX = screenWidth / 2 - windowWidth / 2;
        windowY = screenHeight / 2 - windowHeight / 2;

        //Reseting contents
        contents = "";
    }

    public static void drawGui(final float mouseX, final float mouseY, final float partialTicks) {

        //Gets scaled resolution
        final ScaledResolution sr = new ScaledResolution(mc);

        //Sets screen height and width variables
        screenWidth = sr.getScaledWidth();
        screenHeight = sr.getScaledHeight();

        //Create colors
        final Color backgroundColor = new Color(30, 30, 30, 250);
        final Color bottomBarColor = new Color(97, 122, 57, 255);
        final Color topBarColor = new Color(56, 56, 56, 255);
        final Color textColor = new Color(220, 220, 220, 255);

        //Background
        RenderUtil.rect(windowX, windowY, windowWidth, windowHeight, backgroundColor);

        //Bottom bar
        bottomBarHeight = 10;
        RenderUtil.rect(windowX, windowY + windowHeight - bottomBarHeight, windowWidth, bottomBarHeight, bottomBarColor);

        //Top bar
        topBarHeight = 15;
        RenderUtil.rect(windowX, windowY, windowWidth, topBarHeight, topBarColor);

        //Allows dragging of the window
        if (dragged) {
            windowX = mouseX + dragOffsetX;
            windowY = mouseY + dragOffsetY;
        }

        //Draws contents
        codeStartX = windowX + 5;
        codeStartY = windowY + topBarHeight + 5;

        //Drawing text
        int lines = 0;

        //Splits contents \n per line
        final String[] contentsSplitIntoLines = contents.split("\n");

        //Sets space between lines
        final double spaceBetweenLines = 10;

        //Draws each line individually
        float characterOffset = 0;
        int charactersLoopedThrough = 0;
        for (final String line : contentsSplitIntoLines) {

            //Draws each character seperately
            for (int i = 0; i < line.length(); i++) {
                final String character = String.valueOf(line.charAt(i));
                CustomFont.drawString(character, codeStartX + characterOffset, codeStartY + lines * spaceBetweenLines, textColor.hashCode());
                characterOffset += CustomFont.getWidth(character) * 0.8;

                //Gets amount of \n's there are
                final int newLines = character.length() - character.replace("\n", "").length();
                Rise.addChatMessage(newLines);
                if (charactersLoopedThrough - newLines * 2 /*x2 because there are 2 characters in \n*/ == curserPosition - 1) {
                    RenderUtil.rect(codeStartX + characterOffset - 0.5, codeStartY + lines * spaceBetweenLines, 0.5, 10, textColor);
                }

                //Increase charactersLoopedThrough by 1
                charactersLoopedThrough++;
            }

            //Sets character offset to 0 at the end a of a line to make the text begin from the start of the next line
            characterOffset = 0;

            //Increases the amount of lines
            lines++;
        }

        //makes sure curser position is >= 0
        if (curserPosition < 0) curserPosition = 0;

        //makes sure curser position isn't > amount of characters in contents
        if (curserPosition > contents.chars().count()) curserPosition = (int) contents.chars().count();

//        //Draws curser
//        float curserX = codeStartX;
//        float curserY = (float) (codeStartY - spaceBetweenLines);
//        float widthOfStringBeforeCurser = 0;
//        int characterBeforeCurrentLine = 0;
//
//        try {
//            if (!contents.isEmpty() && curserPosition != 0) {
//                for (final String line: contentsSplitIntoLines) {
//                    final String[] splitAtCurser = splitByNumber(line, curserPosition - characterBeforeCurrentLine);
//                    widthOfStringBeforeCurser = CustomFont.getWidth(splitAtCurser[0]);
//
//                    //Increase y position of curser
//                    curserY += spaceBetweenLines;
//                    characterBeforeCurrentLine = line.length();
//                }
//            } else {
//                curserY += spaceBetweenLines;
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        Rise.addChatMessage(curserPosition);
//
//        curserX += widthOfStringBeforeCurser;
//
//        RenderUtil.rect(curserX - 0.25, curserY, 0.25, 10, textColor);

    }

    public static void mouseClicked(final float mouseX, final float mouseY, final int button) {

        final boolean leftClick = button == 0;
        final boolean rightClick = button == 1;

        //Checks if you clicked the top bar
        if (mouseOver(windowX, windowY, windowWidth, topBarHeight, mouseX, mouseY) && leftClick) {
            dragged = true;
            dragOffsetX = windowX - mouseX;
            dragOffsetY = windowY - mouseY;
        }
    }

    public static void releasedMouseButton() {
        dragged = false;
    }

    public static void onKey(final int key) {

        //Gets key name
        final String character = Keyboard.getKeyName(key);

        //Prints character

        //Moves curser using arrow keys
        if (character.equals("LEFT")) {
            curserPosition -= 1;
            return;
        }

        if (character.equals("RIGHT")) {
            curserPosition += 1;
            return;
        }

        //Allows space to work
        if (character.equals("SPACE")) {
            addString(" ");
            return;
        }

        if (character.equals("RETURN")) {
            addString("\n");
        }

        //Adds any one character string
        if (character.chars().count() == 1) {
            addString(character);
        }
    }

    private static void addString(final String string) {
        //Adds character into contents
        if (contents.isEmpty()) {
            contents += string;
        } else {
            contents = insertString(contents, string, curserPosition - 1);
        }
        //Increases curser position
        curserPosition += 1;
    }

    /*Used to check if the mouse is over a box*/
    private static boolean mouseOver(final float posX, final float posY, final float width, final float height, final float mouseX, final float mouseY) {
        if (mouseX > posX && mouseX < posX + width) {
            return mouseY > posY && mouseY < posY + height;
        }

        return false;
    }

    private static String[] splitByNumber(final String text, final int number) {

        final int inLength = text.length();
        int arLength = inLength / number;
        final int left = inLength % number;
        if (left > 0) {
            ++arLength;
        }

        final String[] ar = new String[arLength];
        String tempText = text;
        for (int x = 0; x < arLength; ++x) {

            if (tempText.length() > number) {
                ar[x] = tempText.substring(0, number);
                tempText = tempText.substring(number);
            } else {
                ar[x] = tempText;
            }

        }

        return ar;
    }

    public static String insertString(final String originalString, final String stringToBeInserted, final int index) {

        // Create a new StringBuffer
        final StringBuffer newString = new StringBuffer(originalString);

        // Insert the strings to be inserted
        // using insert() method
        newString.insert(index + 1, stringToBeInserted);

        // return the modified String
        return newString.toString();
    }
}
