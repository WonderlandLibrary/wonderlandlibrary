
package info.sigmaclient.sigma.gui.hud;

import info.sigmaclient.sigma.SigmaNG;
import info.sigmaclient.sigma.modules.Category;
import info.sigmaclient.sigma.utils.render.anims.PartialTicksAnim;

import java.awt.*;
import java.util.Arrays;
import java.util.List;

import static info.sigmaclient.sigma.utils.render.ColorUtils.blend;

public class JelloTabGUI {
    public static int tRed;
    public static int tGreen;
    public static int tBlue;
    public static int lasttRed, lasttGreen, lasttBlue;

    public static int nRed, nGreen, nBlue;
    public static int lastnRed, lastnGreen, lastnBlue;


    public static int nbRed, nbGreen, nbBlue;
    public static int lastnbRed, lastnbGreen, lastnbBlue;

    public static int bRed, bGreen, bBlue;
    public static int lastbRed, lastbGreen, lastbBlue;
    public static int colorTop, colorTopRight, colorBottom, colorBottomRight, colorNotification = 0, colorNotificationBottom = 0;

    public Color top = new Color(255,255,255,255);
    public Color bottom = new Color(255,255,255,255);

    public static Color colorFromInt(int color) {
        Color c = new Color(color);
        return new Color(c.getRed(), c.getGreen(), c.getBlue(), 255);
    }
    public void tick(){
        lasttRed = tRed;
        lasttGreen = tGreen;
        lasttBlue = tBlue;

        lastnRed = nRed;
        lastnGreen = nGreen;
        lastnBlue = nBlue;

        lastnbRed = nbRed;
        lastnbGreen = nbGreen;
        lastnbBlue = nbBlue;

        lastbRed = bRed;
        lastbGreen = bGreen;
        lastbBlue = bBlue;

        Color top = blend(colorFromInt(colorTop), colorFromInt(colorTopRight));
        Color bottom = blend(colorFromInt(colorBottom), colorFromInt(colorBottomRight));

        bRed += ((bottom.getRed()-bRed)/(5))+0.1;
        bGreen += ((bottom.getGreen()-bGreen)/(5))+0.1;
        bBlue += ((bottom.getBlue()-bBlue)/(5))+0.1;

        tRed += ((top.getRed()-tRed)/(5))+0.1;
        tGreen += ((top.getGreen()-tGreen)/(5))+0.1;
        tBlue += ((top.getBlue()-tBlue)/(5))+0.1;

        nRed += ((colorFromInt(colorNotification).getRed()-nRed)/(5))+0.1;
        nGreen += ((colorFromInt(colorNotification).getGreen()-nGreen)/(5))+0.1;
        nBlue += ((colorFromInt(colorNotification).getBlue()-nBlue)/(5))+0.1;

        nbRed += ((colorFromInt(colorNotificationBottom).getRed()-nbRed)/(5))+0.1;
        nbGreen += ((colorFromInt(colorNotificationBottom).getGreen()-nbGreen)/(5))+0.1;
        nbBlue += ((colorFromInt(colorNotificationBottom).getBlue()-nbBlue)/(5))+0.1;

        tRed = Math.min((int)tRed, 255);
        tGreen = Math.min((int)tGreen, 255);
        tBlue = Math.min((int)tBlue, 255);
        tRed = Math.max((int)tRed, 0);
        tGreen = Math.max((int)tGreen, 0);
        tBlue = Math.max((int)tBlue, 0);

        nRed = Math.min((int)nRed, 255);
        nGreen = Math.min((int)nGreen, 255);
        nBlue = Math.min((int)nBlue, 255);
        nRed = Math.max((int)nRed, 0);
        nGreen = Math.max((int)nGreen, 0);
        nBlue = Math.max((int)nBlue, 0);

        nbRed = Math.min((int)nbRed, 255);
        nbGreen = Math.min((int)nbGreen, 255);
        nbBlue = Math.min((int)nbBlue, 255);
        nbRed = Math.max((int)nbRed, 0);
        nbGreen = Math.max((int)nbGreen, 0);
        nbBlue = Math.max((int)nbBlue, 0);


        bRed = Math.min((int)bRed, 255);
        bGreen = Math.min((int)bGreen, 255);
        bBlue = Math.min((int)bBlue, 255);
        bRed = Math.max((int)bRed, 0);
        bGreen = Math.max((int)bGreen, 0);
        bBlue = Math.max((int)bBlue, 0);
    }
    public int currentCategory;
    public int currentModule;
    public boolean viewingCats = true;
    public float seenTrans = 0;
    public boolean showModules;
    public PartialTicksAnim anim = new PartialTicksAnim(0);


    public void keyRight() {
        if (viewingCats) {
            if (!showModules) {
                showModules = true;
                int categoryIndex = currentCategory;
                Category category = cats.get(categoryIndex);
                category.selectedIndex = 0;
            } else {
                if (SigmaNG.getSigmaNG().moduleManager.getModulesInType(cats.get(currentCategory)).size() != 0)
                    SigmaNG.getSigmaNG().moduleManager.getModulesInType(cats.get(currentCategory))
                            .get(cats.get(currentCategory).selectedIndex).toggle();
            }
        }
    }

    public void keyLeft() {
        if (viewingCats) {
            if (showModules) {
                showModules = false;
            }
        }
    }

    public void keyDown() {
        if (viewingCats) {
            if (!showModules) {
                if (currentCategory < cats.size() - 1) {
                    currentCategory++;
                } else {
                    currentCategory = 0;
                }
            } else {
                if (cats.get(currentCategory).selectedIndex < SigmaNG.getSigmaNG().moduleManager.getModulesInType(
                        cats.get(currentCategory)).size() - 1
                ) {
                    cats.get(currentCategory).selectedIndex++;
                } else {
                    cats.get(currentCategory).selectedIndex = 0;
                }
            }
        }
    }

    public void keyUp() {
        if (viewingCats) {
            if (!showModules) {
                if (currentCategory > 0) {
                    currentCategory--;
                } else {
                    currentCategory = cats.size() - 1;
                }
            } else {
                if (cats.get(currentCategory).selectedIndex > 0) {
                    cats.get(currentCategory).selectedIndex--;
                } else {
                    cats.get(currentCategory).selectedIndex = SigmaNG.getSigmaNG().moduleManager.getModulesInType(cats.get(currentCategory)).size() - 1;
                }
            }
        }
    }

    public List<Category> cats = Arrays.asList(Category.Movement, Category.Player,
            Category.Combat, Category.Item, Category.Render);

}
