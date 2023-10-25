/*
 * Decompiled with CFR 0.150.
 */
package baritone.command.defaults;

import baritone.api.IBaritone;
import baritone.api.command.Command;
import baritone.api.command.argument.IArgConsumer;
import baritone.api.command.exception.CommandException;
import baritone.api.command.exception.CommandInvalidStateException;
import baritone.api.process.IBaritoneProcess;
import baritone.api.process.PathingCommand;
import baritone.api.process.PathingCommandType;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class ExecutionControlCommands {
    Command pauseCommand;
    Command resumeCommand;
    Command pausedCommand;
    Command cancelCommand;

    public ExecutionControlCommands(IBaritone baritone) {
        final boolean[] paused = new boolean[]{false};
        baritone.getPathingControlManager().registerProcess(new IBaritoneProcess(){

            @Override
            public boolean isActive() {
                return paused[0];
            }

            @Override
            public PathingCommand onTick(boolean calcFailed, boolean isSafeToCancel) {
                return new PathingCommand(null, PathingCommandType.REQUEST_PAUSE);
            }

            @Override
            public boolean isTemporary() {
                return true;
            }

            @Override
            public void onLostControl() {
            }

            @Override
            public double priority() {
                return 0.0;
            }

            @Override
            public String displayName0() {
                return "Pause/Resume Commands";
            }
        });
        this.pauseCommand = new Command(baritone, new String[]{"pause", "p"}){

            @Override
            public void execute(String label, IArgConsumer args) throws CommandException {
                args.requireMax(0);
                if (paused[0]) {
                    throw new CommandInvalidStateException("Already paused");
                }
                paused[0] = true;
                this.logDirect("Paused");
            }

            @Override
            public Stream<String> tabComplete(String label, IArgConsumer args) {
                return Stream.empty();
            }

            @Override
            public String getShortDesc() {
                return "Pauses Baritone until you use resume";
            }

            @Override
            public List<String> getLongDesc() {
                return Arrays.asList("The pause command tells Baritone to temporarily stop whatever it's doing.", "", "This can be used to pause pathing, building, following, whatever. A single use of the resume command will start it right back up again!", "", "Usage:", "> pause");
            }
        };
        this.resumeCommand = new Command(baritone, new String[]{"resume", "r"}){

            @Override
            public void execute(String label, IArgConsumer args) throws CommandException {
                args.requireMax(0);
                this.baritone.getBuilderProcess().resume();
                if (!paused[0]) {
                    throw new CommandInvalidStateException("Not paused");
                }
                paused[0] = false;
                this.logDirect("Resumed");
            }

            @Override
            public Stream<String> tabComplete(String label, IArgConsumer args) {
                return Stream.empty();
            }

            @Override
            public String getShortDesc() {
                return "Resumes Baritone after a pause";
            }

            @Override
            public List<String> getLongDesc() {
                return Arrays.asList("The resume command tells Baritone to resume whatever it was doing when you last used pause.", "", "Usage:", "> resume");
            }
        };
        this.pausedCommand = new Command(baritone, new String[]{"paused"}){

            @Override
            public void execute(String label, IArgConsumer args) throws CommandException {
                args.requireMax(0);
                this.logDirect(String.format("Baritone is %spaused", paused[0] ? "" : "not "));
            }

            @Override
            public Stream<String> tabComplete(String label, IArgConsumer args) {
                return Stream.empty();
            }

            @Override
            public String getShortDesc() {
                return "Tells you if Baritone is paused";
            }

            @Override
            public List<String> getLongDesc() {
                return Arrays.asList("The paused command tells you if Baritone is currently paused by use of the pause command.", "", "Usage:", "> paused");
            }
        };
        this.cancelCommand = new Command(baritone, new String[]{"cancel", "c", "stop"}){

            @Override
            public void execute(String label, IArgConsumer args) throws CommandException {
                args.requireMax(0);
                if (paused[0]) {
                    paused[0] = false;
                }
                this.baritone.getPathingBehavior().cancelEverything();
                this.logDirect("Cancelled all processes");
            }

            @Override
            public Stream<String> tabComplete(String label, IArgConsumer args) {
                return Stream.empty();
            }

            @Override
            public String getShortDesc() {
                return "Cancel what Baritone is currently doing";
            }

            @Override
            public List<String> getLongDesc() {
                return Arrays.asList("The cancel command tells Baritone to stop whatever it's currently doing.", "", "Usage:", "> cancel");
            }
        };
    }
}

