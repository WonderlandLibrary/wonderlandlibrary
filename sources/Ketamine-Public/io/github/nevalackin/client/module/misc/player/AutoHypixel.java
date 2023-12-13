package io.github.nevalackin.client.module.misc.player;

import io.github.nevalackin.client.module.Category;
import io.github.nevalackin.client.module.Module;
import io.github.nevalackin.client.property.BooleanProperty;
import io.github.nevalackin.client.property.EnumProperty;

import java.io.File;
import java.io.IOException;

public final class AutoHypixel extends Module {

    private final BooleanProperty autoPlayProperty = new BooleanProperty("Auto Play", true);
    private final EnumProperty<KillSultsMode> killSults = new EnumProperty<>("Kill Sults", KillSultsMode.ENGLISH);

    private final File customKillSultsFile;

    private final String[] insults = {
        "%s shut the /fuck/ up /fatass/",
        "Childhood obesity is an epidemic, %s needs help.",
        "Solve for %s's chromosomes, x = 46 + 1.",
        "%s I can count your friends on one hand.",
        "%s got /raped/ by /trannyhack/.",
        "I /shagged/ %s's mum",
        "I'm fucking rapid mate",
        ""
    };

    public AutoHypixel() {
        super("Auto Hypixel", Category.MISC, Category.SubCategory.MISC_PLAYER);

        this.customKillSultsFile = new File("killsults.txt");
        if (!this.customKillSultsFile.exists()) {
            try {
                final boolean ignored = this.customKillSultsFile.createNewFile();
            } catch (IOException ignored) {
                // TODO :: Error log
            }
        }
    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }

    private enum KillSultsMode {
        DISABLED("Disabled"),
        ENGLISH("English"),
        CUSTOM("Custom"),
        CZECH("Czech");

        private final String name;

        KillSultsMode(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return this.name;
        }
    }
}
