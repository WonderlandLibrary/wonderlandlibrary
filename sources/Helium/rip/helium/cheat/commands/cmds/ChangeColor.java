package rip.helium.cheat.commands.cmds;

import rip.helium.Helium;
import rip.helium.cheat.Cheat;
import rip.helium.cheat.commands.Command;

import java.awt.*;
import java.util.Random;

public class ChangeColor extends Command {

    private final Random random = new Random();

    public ChangeColor() {
        super("cc", "change");
    }

    @Override
    public void run(String[] args) {

        try {

            for (Cheat cheat : Helium.instance.cheatManager.getCheatRegistry().values()) {
                if (cheat != null) {
                    final float hue = random.nextFloat();
                    final float saturation = (random.nextInt(8000) + 500) / 10000f;
                    cheat.setColor(Color.getHSBColor(hue, saturation, 0.9F).getRGB());
                }
            }

        } catch (Exception e) {
            System.out.println("No");
        }

    }
}