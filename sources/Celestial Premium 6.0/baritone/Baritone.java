/*
 * Decompiled with CFR 0.150.
 */
package baritone;

import baritone.api.BaritoneAPI;
import baritone.api.IBaritone;
import baritone.api.Settings;
import baritone.api.event.listener.IEventBus;
import baritone.api.utils.Helper;
import baritone.api.utils.IPlayerContext;
import baritone.behavior.Behavior;
import baritone.behavior.InventoryBehavior;
import baritone.behavior.LookBehavior;
import baritone.behavior.MemoryBehavior;
import baritone.behavior.PathingBehavior;
import baritone.cache.WorldProvider;
import baritone.command.manager.CommandManager;
import baritone.event.GameEventHandler;
import baritone.process.BackfillProcess;
import baritone.process.BuilderProcess;
import baritone.process.CustomGoalProcess;
import baritone.process.ExploreProcess;
import baritone.process.FarmProcess;
import baritone.process.FollowProcess;
import baritone.process.GetToBlockProcess;
import baritone.process.MineProcess;
import baritone.selection.SelectionManager;
import baritone.utils.BlockStateInterface;
import baritone.utils.GuiClick;
import baritone.utils.InputOverrideHandler;
import baritone.utils.PathingControlManager;
import baritone.utils.player.PrimaryPlayerContext;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.attribute.FileAttribute;
import java.util.concurrent.Executor;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import net.minecraft.client.Minecraft;

public class Baritone
implements IBaritone {
    private static ThreadPoolExecutor threadPool = new ThreadPoolExecutor(4, Integer.MAX_VALUE, 60L, TimeUnit.SECONDS, new SynchronousQueue<Runnable>());
    private static File dir = new File(Minecraft.getMinecraft().gameDir, "baritone");
    private GameEventHandler gameEventHandler = new GameEventHandler(this);
    private PathingBehavior pathingBehavior;
    private LookBehavior lookBehavior;
    private MemoryBehavior memoryBehavior;
    private InventoryBehavior inventoryBehavior;
    private InputOverrideHandler inputOverrideHandler;
    private FollowProcess followProcess;
    private MineProcess mineProcess;
    private GetToBlockProcess getToBlockProcess;
    private CustomGoalProcess customGoalProcess;
    private BuilderProcess builderProcess;
    private ExploreProcess exploreProcess;
    private BackfillProcess backfillProcess;
    private FarmProcess farmProcess;
    private PathingControlManager pathingControlManager;
    private SelectionManager selectionManager;
    private CommandManager commandManager;
    private IPlayerContext playerContext = PrimaryPlayerContext.INSTANCE;
    private WorldProvider worldProvider;
    public BlockStateInterface bsi;

    Baritone() {
        this.pathingBehavior = new PathingBehavior(this);
        this.lookBehavior = new LookBehavior(this);
        this.memoryBehavior = new MemoryBehavior(this);
        this.inventoryBehavior = new InventoryBehavior(this);
        this.inputOverrideHandler = new InputOverrideHandler(this);
        this.pathingControlManager = new PathingControlManager(this);
        this.followProcess = new FollowProcess(this);
        this.pathingControlManager.registerProcess(this.followProcess);
        this.mineProcess = new MineProcess(this);
        this.pathingControlManager.registerProcess(this.mineProcess);
        this.customGoalProcess = new CustomGoalProcess(this);
        this.pathingControlManager.registerProcess(this.customGoalProcess);
        this.getToBlockProcess = new GetToBlockProcess(this);
        this.pathingControlManager.registerProcess(this.getToBlockProcess);
        this.builderProcess = new BuilderProcess(this);
        this.pathingControlManager.registerProcess(this.builderProcess);
        this.exploreProcess = new ExploreProcess(this);
        this.pathingControlManager.registerProcess(this.exploreProcess);
        this.backfillProcess = new BackfillProcess(this);
        this.pathingControlManager.registerProcess(this.backfillProcess);
        this.farmProcess = new FarmProcess(this);
        this.pathingControlManager.registerProcess(this.farmProcess);
        this.worldProvider = new WorldProvider();
        this.selectionManager = new SelectionManager(this);
        this.commandManager = new CommandManager(this);
    }

    @Override
    public PathingControlManager getPathingControlManager() {
        return this.pathingControlManager;
    }

    public void registerBehavior(Behavior behavior) {
        this.gameEventHandler.registerEventListener(behavior);
    }

    @Override
    public InputOverrideHandler getInputOverrideHandler() {
        return this.inputOverrideHandler;
    }

    @Override
    public CustomGoalProcess getCustomGoalProcess() {
        return this.customGoalProcess;
    }

    @Override
    public GetToBlockProcess getGetToBlockProcess() {
        return this.getToBlockProcess;
    }

    @Override
    public IPlayerContext getPlayerContext() {
        return this.playerContext;
    }

    public MemoryBehavior getMemoryBehavior() {
        return this.memoryBehavior;
    }

    @Override
    public FollowProcess getFollowProcess() {
        return this.followProcess;
    }

    @Override
    public BuilderProcess getBuilderProcess() {
        return this.builderProcess;
    }

    public InventoryBehavior getInventoryBehavior() {
        return this.inventoryBehavior;
    }

    @Override
    public LookBehavior getLookBehavior() {
        return this.lookBehavior;
    }

    @Override
    public ExploreProcess getExploreProcess() {
        return this.exploreProcess;
    }

    @Override
    public MineProcess getMineProcess() {
        return this.mineProcess;
    }

    @Override
    public FarmProcess getFarmProcess() {
        return this.farmProcess;
    }

    @Override
    public PathingBehavior getPathingBehavior() {
        return this.pathingBehavior;
    }

    @Override
    public SelectionManager getSelectionManager() {
        return this.selectionManager;
    }

    @Override
    public WorldProvider getWorldProvider() {
        return this.worldProvider;
    }

    @Override
    public IEventBus getGameEventHandler() {
        return this.gameEventHandler;
    }

    @Override
    public CommandManager getCommandManager() {
        return this.commandManager;
    }

    @Override
    public void openClick() {
        new Thread(() -> {
            try {
                Thread.sleep(100L);
                Helper.mc.addScheduledTask(() -> Helper.mc.displayGuiScreen(new GuiClick()));
            }
            catch (Exception exception) {
                // empty catch block
            }
        }).start();
    }

    public static Settings settings() {
        return BaritoneAPI.getSettings();
    }

    public static File getDir() {
        return dir;
    }

    public static Executor getExecutor() {
        return threadPool;
    }

    static {
        if (!Files.exists(dir.toPath(), new LinkOption[0])) {
            try {
                Files.createDirectories(dir.toPath(), new FileAttribute[0]);
            }
            catch (IOException iOException) {
                // empty catch block
            }
        }
    }
}

