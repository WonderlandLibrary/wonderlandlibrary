package net.minecraft.init;

import java.io.OutputStream;
import java.io.PrintStream;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDispenser;
import net.minecraft.block.BlockFire;
import net.minecraft.init.Blocks;
import net.minecraft.init.Bootstrap;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.stats.StatList;
import net.minecraft.util.LoggingPrintStream;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Bootstrap {
    private static final PrintStream SYSOUT = System.out;
    private static boolean alreadyRegistered = false;
    private static final Logger LOGGER = LogManager.getLogger();

    public static boolean isRegistered() {
        return alreadyRegistered;
    }

    static void registerDispenserBehaviors() {
        BlockDispenser.dispenseBehaviorRegistry.putObject((Object)Items.arrow, (Object)new /* Unavailable Anonymous Inner Class!! */);
        BlockDispenser.dispenseBehaviorRegistry.putObject((Object)Items.egg, (Object)new /* Unavailable Anonymous Inner Class!! */);
        BlockDispenser.dispenseBehaviorRegistry.putObject((Object)Items.snowball, (Object)new /* Unavailable Anonymous Inner Class!! */);
        BlockDispenser.dispenseBehaviorRegistry.putObject((Object)Items.experience_bottle, (Object)new /* Unavailable Anonymous Inner Class!! */);
        BlockDispenser.dispenseBehaviorRegistry.putObject((Object)Items.potionitem, (Object)new /* Unavailable Anonymous Inner Class!! */);
        BlockDispenser.dispenseBehaviorRegistry.putObject((Object)Items.spawn_egg, (Object)new /* Unavailable Anonymous Inner Class!! */);
        BlockDispenser.dispenseBehaviorRegistry.putObject((Object)Items.fireworks, (Object)new /* Unavailable Anonymous Inner Class!! */);
        BlockDispenser.dispenseBehaviorRegistry.putObject((Object)Items.fire_charge, (Object)new /* Unavailable Anonymous Inner Class!! */);
        BlockDispenser.dispenseBehaviorRegistry.putObject((Object)Items.boat, (Object)new /* Unavailable Anonymous Inner Class!! */);
        10 ibehaviordispenseitem = new /* Unavailable Anonymous Inner Class!! */;
        BlockDispenser.dispenseBehaviorRegistry.putObject((Object)Items.lava_bucket, (Object)ibehaviordispenseitem);
        BlockDispenser.dispenseBehaviorRegistry.putObject((Object)Items.water_bucket, (Object)ibehaviordispenseitem);
        BlockDispenser.dispenseBehaviorRegistry.putObject((Object)Items.bucket, (Object)new /* Unavailable Anonymous Inner Class!! */);
        BlockDispenser.dispenseBehaviorRegistry.putObject((Object)Items.flint_and_steel, (Object)new /* Unavailable Anonymous Inner Class!! */);
        BlockDispenser.dispenseBehaviorRegistry.putObject((Object)Items.dye, (Object)new /* Unavailable Anonymous Inner Class!! */);
        BlockDispenser.dispenseBehaviorRegistry.putObject((Object)Item.getItemFromBlock((Block)Blocks.tnt), (Object)new /* Unavailable Anonymous Inner Class!! */);
        BlockDispenser.dispenseBehaviorRegistry.putObject((Object)Items.skull, (Object)new /* Unavailable Anonymous Inner Class!! */);
        BlockDispenser.dispenseBehaviorRegistry.putObject((Object)Item.getItemFromBlock((Block)Blocks.pumpkin), (Object)new /* Unavailable Anonymous Inner Class!! */);
    }

    public static void register() {
        if (!alreadyRegistered) {
            alreadyRegistered = true;
            if (LOGGER.isDebugEnabled()) {
                Bootstrap.redirectOutputToLog();
            }
            Block.registerBlocks();
            BlockFire.init();
            Item.registerItems();
            StatList.init();
            Bootstrap.registerDispenserBehaviors();
        }
    }

    private static void redirectOutputToLog() {
        System.setErr((PrintStream)new LoggingPrintStream("STDERR", (OutputStream)System.err));
        System.setOut((PrintStream)new LoggingPrintStream("STDOUT", (OutputStream)SYSOUT));
    }

    public static void printToSYSOUT(String p_179870_0_) {
        SYSOUT.println(p_179870_0_);
    }
}
