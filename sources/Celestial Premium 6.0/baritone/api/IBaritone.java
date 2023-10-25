/*
 * Decompiled with CFR 0.150.
 */
package baritone.api;

import baritone.api.behavior.ILookBehavior;
import baritone.api.behavior.IPathingBehavior;
import baritone.api.cache.IWorldProvider;
import baritone.api.command.manager.ICommandManager;
import baritone.api.event.listener.IEventBus;
import baritone.api.pathing.calc.IPathingControlManager;
import baritone.api.process.IBuilderProcess;
import baritone.api.process.ICustomGoalProcess;
import baritone.api.process.IExploreProcess;
import baritone.api.process.IFarmProcess;
import baritone.api.process.IFollowProcess;
import baritone.api.process.IGetToBlockProcess;
import baritone.api.process.IMineProcess;
import baritone.api.selection.ISelectionManager;
import baritone.api.utils.IInputOverrideHandler;
import baritone.api.utils.IPlayerContext;

public interface IBaritone {
    public IPathingBehavior getPathingBehavior();

    public ILookBehavior getLookBehavior();

    public IFollowProcess getFollowProcess();

    public IMineProcess getMineProcess();

    public IBuilderProcess getBuilderProcess();

    public IExploreProcess getExploreProcess();

    public IFarmProcess getFarmProcess();

    public ICustomGoalProcess getCustomGoalProcess();

    public IGetToBlockProcess getGetToBlockProcess();

    public IWorldProvider getWorldProvider();

    public IPathingControlManager getPathingControlManager();

    public IInputOverrideHandler getInputOverrideHandler();

    public IPlayerContext getPlayerContext();

    public IEventBus getGameEventHandler();

    public ISelectionManager getSelectionManager();

    public ICommandManager getCommandManager();

    public void openClick();
}

