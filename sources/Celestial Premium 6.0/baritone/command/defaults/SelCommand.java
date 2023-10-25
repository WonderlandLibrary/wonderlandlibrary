/*
 * Decompiled with CFR 0.150.
 */
package baritone.command.defaults;

import baritone.Baritone;
import baritone.api.IBaritone;
import baritone.api.command.Command;
import baritone.api.command.argument.IArgConsumer;
import baritone.api.command.datatypes.ForBlockOptionalMeta;
import baritone.api.command.datatypes.ForEnumFacing;
import baritone.api.command.datatypes.RelativeBlockPos;
import baritone.api.command.exception.CommandException;
import baritone.api.command.exception.CommandInvalidStateException;
import baritone.api.command.exception.CommandInvalidTypeException;
import baritone.api.command.helpers.TabCompleteHelper;
import baritone.api.event.events.RenderEvent;
import baritone.api.event.listener.AbstractGameEventListener;
import baritone.api.schematic.AbstractSchematic;
import baritone.api.schematic.CompositeSchematic;
import baritone.api.schematic.FillSchematic;
import baritone.api.schematic.ReplaceSchematic;
import baritone.api.schematic.ShellSchematic;
import baritone.api.schematic.WallsSchematic;
import baritone.api.selection.ISelection;
import baritone.api.selection.ISelectionManager;
import baritone.api.utils.BetterBlockPos;
import baritone.api.utils.BlockOptionalMeta;
import baritone.api.utils.BlockOptionalMetaLookup;
import baritone.utils.IRenderer;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;

public class SelCommand
extends Command {
    private ISelectionManager manager;
    private BetterBlockPos pos1;

    public SelCommand(IBaritone baritone) {
        super(baritone, "sel", "selection", "s");
        this.manager = this.baritone.getSelectionManager();
        this.pos1 = null;
        baritone.getGameEventHandler().registerEventListener(new AbstractGameEventListener(){

            @Override
            public void onRenderPass(RenderEvent event) {
                if (!((Boolean)Baritone.settings().renderSelectionCorners.value).booleanValue() || SelCommand.this.pos1 == null) {
                    return;
                }
                Color color = (Color)Baritone.settings().colorSelectionPos1.value;
                float opacity = ((Float)Baritone.settings().selectionOpacity.value).floatValue();
                float lineWidth = ((Float)Baritone.settings().selectionLineWidth.value).floatValue();
                boolean ignoreDepth = (Boolean)Baritone.settings().renderSelectionIgnoreDepth.value;
                IRenderer.startLines(color, opacity, lineWidth, ignoreDepth);
                IRenderer.drawAABB(new AxisAlignedBB(SelCommand.this.pos1, SelCommand.this.pos1.add(1, 1, 1)));
                IRenderer.endLines(ignoreDepth);
            }
        });
    }

    @Override
    public void execute(String label, IArgConsumer args) throws CommandException {
        Action action = Action.getByName(args.getString());
        if (action == null) {
            throw new CommandInvalidTypeException(args.consumed(), "an action");
        }
        if (action == Action.POS1 || action == Action.POS2) {
            if (action == Action.POS2 && this.pos1 == null) {
                throw new CommandInvalidStateException("Set pos1 first before using pos2");
            }
            BetterBlockPos playerPos = mc.getRenderViewEntity() != null ? BetterBlockPos.from(new BlockPos(mc.getRenderViewEntity())) : this.ctx.playerFeet();
            BetterBlockPos pos = args.hasAny() ? (BetterBlockPos)args.getDatatypePost(RelativeBlockPos.INSTANCE, playerPos) : playerPos;
            args.requireMax(0);
            if (action == Action.POS1) {
                this.pos1 = pos;
                this.logDirect("Position 1 has been set");
            } else {
                this.manager.addSelection(this.pos1, pos);
                this.pos1 = null;
                this.logDirect("Selection added");
            }
        } else if (action == Action.CLEAR) {
            args.requireMax(0);
            this.pos1 = null;
            this.logDirect(String.format("Removed %d selections", this.manager.removeAllSelections().length));
        } else if (action == Action.UNDO) {
            args.requireMax(0);
            if (this.pos1 != null) {
                this.pos1 = null;
                this.logDirect("Undid pos1");
            } else {
                ISelection[] selections = this.manager.getSelections();
                if (selections.length < 1) {
                    throw new CommandInvalidStateException("Nothing to undo!");
                }
                this.pos1 = this.manager.removeSelection(selections[selections.length - 1]).pos1();
                this.logDirect("Undid pos2");
            }
        } else if (action == Action.SET || action == Action.WALLS || action == Action.SHELL || action == Action.CLEARAREA || action == Action.REPLACE) {
            BlockOptionalMeta type = action == Action.CLEARAREA ? new BlockOptionalMeta(Blocks.AIR) : (BlockOptionalMeta)args.getDatatypeFor(ForBlockOptionalMeta.INSTANCE);
            BlockOptionalMetaLookup replaces = null;
            if (action == Action.REPLACE) {
                args.requireMin(1);
                ArrayList<BlockOptionalMeta> replacesList = new ArrayList<BlockOptionalMeta>();
                replacesList.add(type);
                while (args.has(2)) {
                    replacesList.add((BlockOptionalMeta)args.getDatatypeFor(ForBlockOptionalMeta.INSTANCE));
                }
                type = (BlockOptionalMeta)args.getDatatypeFor(ForBlockOptionalMeta.INSTANCE);
                replaces = new BlockOptionalMetaLookup(replacesList.toArray(new BlockOptionalMeta[0]));
            } else {
                args.requireMax(0);
            }
            ISelection[] selections = this.manager.getSelections();
            if (selections.length == 0) {
                throw new CommandInvalidStateException("No selections");
            }
            BetterBlockPos origin = selections[0].min();
            CompositeSchematic composite = new CompositeSchematic(0, 0, 0);
            for (ISelection selection : selections) {
                BetterBlockPos min = selection.min();
                origin = new BetterBlockPos(Math.min(origin.x, min.x), Math.min(origin.y, min.y), Math.min(origin.z, min.z));
            }
            for (ISelection selection : selections) {
                Vec3i size = selection.size();
                BetterBlockPos min = selection.min();
                AbstractSchematic schematic = new FillSchematic(size.getX(), size.getY(), size.getZ(), type);
                if (action == Action.WALLS) {
                    schematic = new WallsSchematic(schematic);
                } else if (action == Action.SHELL) {
                    schematic = new ShellSchematic(schematic);
                } else if (action == Action.REPLACE) {
                    schematic = new ReplaceSchematic(schematic, replaces);
                }
                composite.put(schematic, min.x - origin.x, min.y - origin.y, min.z - origin.z);
            }
            this.baritone.getBuilderProcess().build("Fill", composite, (Vec3i)origin);
            this.logDirect("Filling now");
        } else if (action == Action.EXPAND || action == Action.CONTRACT || action == Action.SHIFT) {
            args.requireExactly(3);
            TransformTarget transformTarget = TransformTarget.getByName(args.getString());
            if (transformTarget == null) {
                throw new CommandInvalidStateException("Invalid transform type");
            }
            EnumFacing direction = (EnumFacing)args.getDatatypeFor(ForEnumFacing.INSTANCE);
            int blocks = args.getAs(Integer.class);
            ISelection[] selections = this.manager.getSelections();
            if (selections.length < 1) {
                throw new CommandInvalidStateException("No selections found");
            }
            for (ISelection selection : selections = transformTarget.transform(selections)) {
                if (action == Action.EXPAND) {
                    this.manager.expand(selection, direction, blocks);
                    continue;
                }
                if (action == Action.CONTRACT) {
                    this.manager.contract(selection, direction, blocks);
                    continue;
                }
                this.manager.shift(selection, direction, blocks);
            }
            this.logDirect(String.format("Transformed %d selections", selections.length));
        }
    }

    @Override
    public Stream<String> tabComplete(String label, IArgConsumer args) throws CommandException {
        if (args.hasExactlyOne()) {
            return new TabCompleteHelper().append(Action.getAllNames()).filterPrefix(args.getString()).sortAlphabetically().stream();
        }
        Action action = Action.getByName(args.getString());
        if (action != null) {
            if (action == Action.POS1 || action == Action.POS2) {
                if (args.hasAtMost(3)) {
                    return args.tabCompleteDatatype(RelativeBlockPos.INSTANCE);
                }
            } else if (action == Action.SET || action == Action.WALLS || action == Action.CLEARAREA || action == Action.REPLACE) {
                if (args.hasExactlyOne() || action == Action.REPLACE) {
                    while (args.has(2)) {
                        args.get();
                    }
                    return args.tabCompleteDatatype(ForBlockOptionalMeta.INSTANCE);
                }
            } else if (action == Action.EXPAND || action == Action.CONTRACT || action == Action.SHIFT) {
                if (args.hasExactlyOne()) {
                    return new TabCompleteHelper().append(TransformTarget.getAllNames()).filterPrefix(args.getString()).sortAlphabetically().stream();
                }
                TransformTarget target = TransformTarget.getByName(args.getString());
                if (target != null && args.hasExactlyOne()) {
                    return args.tabCompleteDatatype(ForEnumFacing.INSTANCE);
                }
            }
        }
        return Stream.empty();
    }

    @Override
    public String getShortDesc() {
        return "WorldEdit-like commands";
    }

    @Override
    public List<String> getLongDesc() {
        return Arrays.asList("The sel command allows you to manipulate Baritone's selections, similarly to WorldEdit.", "", "Using these selections, you can clear areas, fill them with blocks, or something else.", "", "The expand/contract/shift commands use a kind of selector to choose which selections to target. Supported ones are a/all, n/newest, and o/oldest.", "", "Usage:", "> sel pos1/p1/1 - Set position 1 to your current position.", "> sel pos1/p1/1 <x> <y> <z> - Set position 1 to a relative position.", "> sel pos2/p2/2 - Set position 2 to your current position.", "> sel pos2/p2/2 <x> <y> <z> - Set position 2 to a relative position.", "", "> sel clear/c - Clear the selection.", "> sel undo/u - Undo the last action (setting positions, creating selections, etc.)", "> sel set/fill/s/f [block] - Completely fill all selections with a block.", "> sel walls/w [block] - Fill in the walls of the selection with a specified block.", "> sel shell/shl [block] - The same as walls, but fills in a ceiling and floor too.", "> sel cleararea/ca - Basically 'set air'.", "> sel replace/r <blocks...> <with> - Replaces blocks with another block.", "", "> sel expand <target> <direction> <blocks> - Expand the targets.", "> sel contract <target> <direction> <blocks> - Contract the targets.", "> sel shift <target> <direction> <blocks> - Shift the targets (does not resize).");
    }

    /*
     * Exception performing whole class analysis.
     */
    static final class TransformTarget
    extends Enum<TransformTarget> {
        public static final /* enum */ TransformTarget ALL;
        public static final /* enum */ TransformTarget NEWEST;
        public static final /* enum */ TransformTarget OLDEST;
        private final Function<ISelection[], ISelection[]> transform;
        private final String[] names;
        private static final /* synthetic */ TransformTarget[] $VALUES;

        public static TransformTarget[] values() {
            return (TransformTarget[])$VALUES.clone();
        }

        public static TransformTarget valueOf(String name) {
            return Enum.valueOf(TransformTarget.class, name);
        }

        private TransformTarget(Function<ISelection[], ISelection[]> transform, String ... names) {
            super(string, n);
            this.transform = transform;
            this.names = names;
        }

        public ISelection[] transform(ISelection[] selections) {
            return this.transform.apply(selections);
        }

        public static TransformTarget getByName(String name) {
            for (TransformTarget target : TransformTarget.values()) {
                for (String alias : target.names) {
                    if (!alias.equalsIgnoreCase(name)) continue;
                    return target;
                }
            }
            return null;
        }

        public static String[] getAllNames() {
            HashSet<String> names = new HashSet<String>();
            for (TransformTarget target : TransformTarget.values()) {
                names.addAll(Arrays.asList(target.names));
            }
            return names.toArray(new String[0]);
        }

        private static /* synthetic */ ISelection[] lambda$static$2(ISelection[] sels) {
            return new ISelection[]{sels[0]};
        }

        /*
         * Exception decompiling
         */
        static {
            /*
             * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
             * java.lang.UnsupportedOperationException
             * org.benf.cfr.reader.bytecode.analysis.parse.expression.NewAnonymousArray.getDimSize(NewAnonymousArray.java:136)
             * org.benf.cfr.reader.bytecode.analysis.opgraph.op4rewriters.LambdaRewriter.isNewArrayLambda(LambdaRewriter.java:454)
             * org.benf.cfr.reader.bytecode.analysis.opgraph.op4rewriters.LambdaRewriter.rewriteDynamicExpression(LambdaRewriter.java:408)
             * org.benf.cfr.reader.bytecode.analysis.opgraph.op4rewriters.LambdaRewriter.rewriteDynamicExpression(LambdaRewriter.java:166)
             * org.benf.cfr.reader.bytecode.analysis.opgraph.op4rewriters.LambdaRewriter.rewriteExpression(LambdaRewriter.java:104)
             * org.benf.cfr.reader.bytecode.analysis.parse.rewriters.ExpressionRewriterHelper.applyForwards(ExpressionRewriterHelper.java:12)
             * org.benf.cfr.reader.bytecode.analysis.parse.expression.AbstractConstructorInvokation.applyExpressionRewriter(AbstractConstructorInvokation.java:64)
             * org.benf.cfr.reader.bytecode.analysis.opgraph.op4rewriters.LambdaRewriter.rewriteExpression(LambdaRewriter.java:102)
             * org.benf.cfr.reader.bytecode.analysis.structured.statement.StructuredAssignment.rewriteExpressions(StructuredAssignment.java:138)
             * org.benf.cfr.reader.bytecode.analysis.opgraph.op4rewriters.LambdaRewriter.rewrite(LambdaRewriter.java:87)
             * org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.rewriteLambdas(Op04StructuredStatement.java:1125)
             * org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:868)
             * org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:258)
             * org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:192)
             * org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:94)
             * org.benf.cfr.reader.entities.Method.analyse(Method.java:521)
             * org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:1035)
             * org.benf.cfr.reader.entities.ClassFile.analyseInnerClassesPass1(ClassFile.java:903)
             * org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:1015)
             * org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:922)
             * org.benf.cfr.reader.Driver.doJarVersionTypes(Driver.java:253)
             * org.benf.cfr.reader.Driver.doJar(Driver.java:135)
             * org.benf.cfr.reader.CfrDriverImpl.analyse(CfrDriverImpl.java:65)
             * org.benf.cfr.reader.Main.main(Main.java:49)
             */
            throw new IllegalStateException(Decompilation failed);
        }
    }

    static enum Action {
        POS1("pos1", "p1", "1"),
        POS2("pos2", "p2", "2"),
        CLEAR("clear", "c"),
        UNDO("undo", "u"),
        SET("set", "fill", "s", "f"),
        WALLS("walls", "w"),
        SHELL("shell", "shl"),
        CLEARAREA("cleararea", "ca"),
        REPLACE("replace", "r"),
        EXPAND("expand", "ex"),
        CONTRACT("contract", "ct"),
        SHIFT("shift", "sh");

        private final String[] names;

        private Action(String ... names) {
            this.names = names;
        }

        public static Action getByName(String name) {
            for (Action action : Action.values()) {
                for (String alias : action.names) {
                    if (!alias.equalsIgnoreCase(name)) continue;
                    return action;
                }
            }
            return null;
        }

        public static String[] getAllNames() {
            HashSet<String> names = new HashSet<String>();
            for (Action action : Action.values()) {
                names.addAll(Arrays.asList(action.names));
            }
            return names.toArray(new String[0]);
        }
    }
}

