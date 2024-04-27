/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.internal.codegen;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import jdk.nashorn.internal.codegen.ApplySpecialization;
import jdk.nashorn.internal.codegen.AssignSymbols;
import jdk.nashorn.internal.codegen.CacheAst;
import jdk.nashorn.internal.codegen.ClassEmitter;
import jdk.nashorn.internal.codegen.CodeGenerator;
import jdk.nashorn.internal.codegen.CompilationException;
import jdk.nashorn.internal.codegen.CompileUnit;
import jdk.nashorn.internal.codegen.Compiler;
import jdk.nashorn.internal.codegen.DumpBytecode;
import jdk.nashorn.internal.codegen.FindScopeDepths;
import jdk.nashorn.internal.codegen.FoldConstants;
import jdk.nashorn.internal.codegen.LocalVariableTypesCalculator;
import jdk.nashorn.internal.codegen.Lower;
import jdk.nashorn.internal.codegen.OptimisticTypesCalculator;
import jdk.nashorn.internal.codegen.ProgramPoints;
import jdk.nashorn.internal.codegen.ReplaceCompileUnits;
import jdk.nashorn.internal.codegen.SplitIntoFunctions;
import jdk.nashorn.internal.codegen.Splitter;
import jdk.nashorn.internal.ir.Block;
import jdk.nashorn.internal.ir.FunctionNode;
import jdk.nashorn.internal.ir.LiteralNode;
import jdk.nashorn.internal.ir.Node;
import jdk.nashorn.internal.ir.Symbol;
import jdk.nashorn.internal.ir.debug.ASTWriter;
import jdk.nashorn.internal.ir.debug.PrintVisitor;
import jdk.nashorn.internal.ir.visitor.NodeVisitor;
import jdk.nashorn.internal.ir.visitor.SimpleNodeVisitor;
import jdk.nashorn.internal.runtime.CodeInstaller;
import jdk.nashorn.internal.runtime.RecompilableScriptFunctionData;
import jdk.nashorn.internal.runtime.ScriptEnvironment;
import jdk.nashorn.internal.runtime.logging.DebugLogger;

abstract class CompilationPhase {
    static final CompilationPhase CONSTANT_FOLDING_PHASE = new ConstantFoldingPhase();
    static final CompilationPhase LOWERING_PHASE = new LoweringPhase();
    static final CompilationPhase APPLY_SPECIALIZATION_PHASE = new ApplySpecializationPhase();
    static final CompilationPhase SPLITTING_PHASE = new SplittingPhase();
    static final CompilationPhase PROGRAM_POINT_PHASE = new ProgramPointPhase();
    static final CompilationPhase CACHE_AST_PHASE = new CacheAstPhase();
    static final CompilationPhase SYMBOL_ASSIGNMENT_PHASE = new SymbolAssignmentPhase();
    static final CompilationPhase SCOPE_DEPTH_COMPUTATION_PHASE = new ScopeDepthComputationPhase();
    static final CompilationPhase DECLARE_LOCAL_SYMBOLS_PHASE = new DeclareLocalSymbolsPhase();
    static final CompilationPhase OPTIMISTIC_TYPE_ASSIGNMENT_PHASE = new OptimisticTypeAssignmentPhase();
    static final CompilationPhase LOCAL_VARIABLE_TYPE_CALCULATION_PHASE = new LocalVariableTypeCalculationPhase();
    static final CompilationPhase REUSE_COMPILE_UNITS_PHASE = new ReuseCompileUnitsPhase();
    static final CompilationPhase REINITIALIZE_CACHED = new ReinitializeCachedPhase();
    static final CompilationPhase BYTECODE_GENERATION_PHASE = new BytecodeGenerationPhase();
    static final CompilationPhase INSTALL_PHASE = new InstallPhase();
    private long startTime;
    private long endTime;
    private boolean isFinished;

    private CompilationPhase() {
    }

    protected FunctionNode begin(Compiler compiler, FunctionNode functionNode) {
        compiler.getLogger().indent();
        this.startTime = System.nanoTime();
        return functionNode;
    }

    protected FunctionNode end(Compiler compiler, FunctionNode functionNode) {
        compiler.getLogger().unindent();
        this.endTime = System.nanoTime();
        compiler.getScriptEnvironment()._timing.accumulateTime(this.toString(), this.endTime - this.startTime);
        this.isFinished = true;
        return functionNode;
    }

    boolean isFinished() {
        return this.isFinished;
    }

    long getStartTime() {
        return this.startTime;
    }

    long getEndTime() {
        return this.endTime;
    }

    abstract FunctionNode transform(Compiler var1, Compiler.CompilationPhases var2, FunctionNode var3) throws CompilationException;

    final FunctionNode apply(Compiler compiler, Compiler.CompilationPhases phases, FunctionNode functionNode) throws CompilationException {
        assert (phases.contains(this));
        return this.end(compiler, this.transform(compiler, phases, this.begin(compiler, functionNode)));
    }

    private static FunctionNode transformFunction(FunctionNode fn, NodeVisitor<?> visitor) {
        return (FunctionNode)fn.accept(visitor);
    }

    private static CompileUnit createNewCompileUnit(Compiler compiler, Compiler.CompilationPhases phases) {
        StringBuilder sb = new StringBuilder(compiler.nextCompileUnitName());
        if (phases.isRestOfCompilation()) {
            sb.append("$restOf");
        }
        return compiler.createCompileUnit(sb.toString(), 0L);
    }

    private static final class InstallPhase
    extends CompilationPhase {
        private InstallPhase() {
        }

        @Override
        FunctionNode transform(Compiler compiler, Compiler.CompilationPhases phases, FunctionNode fn) {
            DebugLogger log = compiler.getLogger();
            LinkedHashMap installedClasses = new LinkedHashMap();
            boolean first = true;
            Class<?> rootClass = null;
            long length = 0L;
            CodeInstaller codeInstaller = compiler.getCodeInstaller();
            Map<String, byte[]> bytecode = compiler.getBytecode();
            for (Map.Entry<String, byte[]> entry : bytecode.entrySet()) {
                String className = entry.getKey();
                byte[] code = entry.getValue();
                length += (long)code.length;
                Class<?> clazz = codeInstaller.install(className, code);
                if (first) {
                    rootClass = clazz;
                    first = false;
                }
                installedClasses.put(className, clazz);
            }
            if (rootClass == null) {
                throw new CompilationException("Internal compiler error: root class not found!");
            }
            Object[] constants = compiler.getConstantData().toArray();
            codeInstaller.initialize(installedClasses.values(), compiler.getSource(), constants);
            for (Object constant : constants) {
                if (!(constant instanceof RecompilableScriptFunctionData)) continue;
                ((RecompilableScriptFunctionData)constant).initTransients(compiler.getSource(), codeInstaller);
            }
            for (CompileUnit unit : compiler.getCompileUnits()) {
                if (!unit.isUsed()) continue;
                unit.setCode((Class)installedClasses.get(unit.getUnitClassName()));
                unit.initializeFunctionsCode();
            }
            if (log.isEnabled()) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Installed class '").append(rootClass.getSimpleName()).append('\'').append(" [").append(rootClass.getName()).append(", size=").append(length).append(" bytes, ").append(compiler.getCompileUnits().size()).append(" compile unit(s)]");
                log.fine(stringBuilder.toString());
            }
            return fn.setRootClass(null, rootClass);
        }

        public String toString() {
            return "'Class Installation'";
        }
    }

    private static final class BytecodeGenerationPhase
    extends CompilationPhase {
        private BytecodeGenerationPhase() {
        }

        @Override
        FunctionNode transform(Compiler compiler, Compiler.CompilationPhases phases, FunctionNode fn) {
            ScriptEnvironment senv = compiler.getScriptEnvironment();
            FunctionNode newFunctionNode = fn;
            fn.getCompileUnit().setUsed();
            compiler.getLogger().fine("Starting bytecode generation for ", DebugLogger.quote(fn.getName()), " - restOf=", phases.isRestOfCompilation());
            CodeGenerator codegen = new CodeGenerator(compiler, phases.isRestOfCompilation() ? compiler.getContinuationEntryPoints() : null);
            try {
                newFunctionNode = CompilationPhase.transformFunction(newFunctionNode, codegen);
                codegen.generateScopeCalls();
            }
            catch (VerifyError e) {
                if (senv._verify_code || senv._print_code) {
                    senv.getErr().println(e.getClass().getSimpleName() + ": " + e.getMessage());
                    if (senv._dump_on_error) {
                        e.printStackTrace(senv.getErr());
                    }
                }
                throw e;
            }
            catch (Throwable e) {
                throw new AssertionError("Failed generating bytecode for " + fn.getSourceName() + ":" + codegen.getLastLineNumber(), e);
            }
            for (CompileUnit compileUnit : compiler.getCompileUnits()) {
                ClassEmitter classEmitter = compileUnit.getClassEmitter();
                classEmitter.end();
                if (!compileUnit.isUsed()) {
                    compiler.getLogger().fine("Skipping unused compile unit ", compileUnit);
                    continue;
                }
                byte[] bytecode = classEmitter.toByteArray();
                assert (bytecode != null);
                String className = compileUnit.getUnitClassName();
                compiler.addClass(className, bytecode);
                CompileUnit.increaseEmitCount();
                if (senv._verify_code) {
                    compiler.getCodeInstaller().verify(bytecode);
                }
                DumpBytecode.dumpBytecode(senv, compiler.getLogger(), bytecode, className);
            }
            return newFunctionNode;
        }

        public String toString() {
            return "'Bytecode Generation'";
        }
    }

    private static final class ReinitializeCachedPhase
    extends CompilationPhase {
        private ReinitializeCachedPhase() {
        }

        @Override
        FunctionNode transform(final Compiler compiler, final Compiler.CompilationPhases phases, FunctionNode fn) {
            final Set<CompileUnit> unitSet = CompileUnit.createCompileUnitSet();
            final HashMap<CompileUnit, CompileUnit> unitMap = new HashMap<CompileUnit, CompileUnit>();
            this.createCompileUnit(fn.getCompileUnit(), unitSet, unitMap, compiler, phases);
            FunctionNode newFn = CompilationPhase.transformFunction(fn, new ReplaceCompileUnits(){

                @Override
                CompileUnit getReplacement(CompileUnit oldUnit) {
                    CompileUnit existing = (CompileUnit)unitMap.get(oldUnit);
                    if (existing != null) {
                        return existing;
                    }
                    return ReinitializeCachedPhase.this.createCompileUnit(oldUnit, unitSet, unitMap, compiler, phases);
                }

                @Override
                public Node leaveFunctionNode(FunctionNode fn2) {
                    return super.leaveFunctionNode(compiler.getScriptFunctionData(fn2.getId()).restoreFlags(this.lc, fn2));
                }
            });
            compiler.replaceCompileUnits(unitSet);
            return newFn;
        }

        private CompileUnit createCompileUnit(CompileUnit oldUnit, Set<CompileUnit> unitSet, Map<CompileUnit, CompileUnit> unitMap, Compiler compiler, Compiler.CompilationPhases phases) {
            CompileUnit newUnit = CompilationPhase.createNewCompileUnit(compiler, phases);
            unitMap.put(oldUnit, newUnit);
            unitSet.add(newUnit);
            return newUnit;
        }

        public String toString() {
            return "'Reinitialize cached'";
        }
    }

    private static final class ReuseCompileUnitsPhase
    extends CompilationPhase {
        private ReuseCompileUnitsPhase() {
        }

        @Override
        FunctionNode transform(Compiler compiler, Compiler.CompilationPhases phases, FunctionNode fn) {
            assert (phases.isRestOfCompilation()) : "reuse compile units currently only used for Rest-Of methods";
            final HashMap<CompileUnit, CompileUnit> map = new HashMap<CompileUnit, CompileUnit>();
            Set<CompileUnit> newUnits = CompileUnit.createCompileUnitSet();
            DebugLogger log = compiler.getLogger();
            log.fine("Clearing bytecode cache");
            compiler.clearBytecode();
            for (CompileUnit oldUnit : compiler.getCompileUnits()) {
                assert (map.get(oldUnit) == null);
                CompileUnit newUnit = CompilationPhase.createNewCompileUnit(compiler, phases);
                log.fine("Creating new compile unit ", oldUnit, " => ", newUnit);
                map.put(oldUnit, newUnit);
                assert (newUnit != null);
                newUnits.add(newUnit);
            }
            log.fine("Replacing compile units in Compiler...");
            compiler.replaceCompileUnits(newUnits);
            log.fine("Done");
            FunctionNode newFunctionNode = CompilationPhase.transformFunction(fn, new ReplaceCompileUnits(){

                @Override
                CompileUnit getReplacement(CompileUnit original) {
                    return (CompileUnit)map.get(original);
                }

                @Override
                public Node leaveDefault(Node node) {
                    return node.ensureUniqueLabels(this.lc);
                }
            });
            return newFunctionNode;
        }

        public String toString() {
            return "'Reuse Compile Units'";
        }
    }

    private static final class LocalVariableTypeCalculationPhase
    extends CompilationPhase {
        private LocalVariableTypeCalculationPhase() {
        }

        @Override
        FunctionNode transform(Compiler compiler, Compiler.CompilationPhases phases, FunctionNode fn) {
            FunctionNode newFunctionNode = CompilationPhase.transformFunction(fn, new LocalVariableTypesCalculator(compiler));
            ScriptEnvironment senv = compiler.getScriptEnvironment();
            PrintWriter err = senv.getErr();
            if (senv._print_lower_ast || fn.getFlag(0x100000)) {
                err.println("Lower AST for: " + DebugLogger.quote(newFunctionNode.getName()));
                err.println(new ASTWriter(newFunctionNode));
            }
            if (senv._print_lower_parse || fn.getFlag(262144)) {
                err.println("Lower AST for: " + DebugLogger.quote(newFunctionNode.getName()));
                err.println(new PrintVisitor(newFunctionNode));
            }
            return newFunctionNode;
        }

        public String toString() {
            return "'Local Variable Type Calculation'";
        }
    }

    private static final class OptimisticTypeAssignmentPhase
    extends CompilationPhase {
        private OptimisticTypeAssignmentPhase() {
        }

        @Override
        FunctionNode transform(Compiler compiler, Compiler.CompilationPhases phases, FunctionNode fn) {
            if (compiler.useOptimisticTypes()) {
                return CompilationPhase.transformFunction(fn, new OptimisticTypesCalculator(compiler));
            }
            return fn;
        }

        public String toString() {
            return "'Optimistic Type Assignment'";
        }
    }

    private static final class DeclareLocalSymbolsPhase
    extends CompilationPhase {
        private DeclareLocalSymbolsPhase() {
        }

        @Override
        FunctionNode transform(final Compiler compiler, Compiler.CompilationPhases phases, FunctionNode fn) {
            if (compiler.useOptimisticTypes() && compiler.isOnDemandCompilation()) {
                fn.getBody().accept(new SimpleNodeVisitor(){

                    @Override
                    public boolean enterFunctionNode(FunctionNode functionNode) {
                        return false;
                    }

                    @Override
                    public boolean enterBlock(Block block) {
                        for (Symbol symbol : block.getSymbols()) {
                            if (symbol.isScope()) continue;
                            compiler.declareLocalSymbol(symbol.getName());
                        }
                        return true;
                    }
                });
            }
            return fn;
        }

        public String toString() {
            return "'Local Symbols Declaration'";
        }
    }

    private static final class ScopeDepthComputationPhase
    extends CompilationPhase {
        private ScopeDepthComputationPhase() {
        }

        @Override
        FunctionNode transform(Compiler compiler, Compiler.CompilationPhases phases, FunctionNode fn) {
            return CompilationPhase.transformFunction(fn, new FindScopeDepths(compiler));
        }

        public String toString() {
            return "'Scope Depth Computation'";
        }
    }

    private static final class SymbolAssignmentPhase
    extends CompilationPhase {
        private SymbolAssignmentPhase() {
        }

        @Override
        FunctionNode transform(Compiler compiler, Compiler.CompilationPhases phases, FunctionNode fn) {
            return CompilationPhase.transformFunction(fn, new AssignSymbols(compiler));
        }

        public String toString() {
            return "'Symbol Assignment'";
        }
    }

    private static final class CacheAstPhase
    extends CompilationPhase {
        private CacheAstPhase() {
        }

        @Override
        FunctionNode transform(Compiler compiler, Compiler.CompilationPhases phases, FunctionNode fn) {
            if (!compiler.isOnDemandCompilation()) {
                CompilationPhase.transformFunction(fn, new CacheAst(compiler));
            }
            return fn;
        }

        public String toString() {
            return "'Cache ASTs'";
        }
    }

    private static final class ProgramPointPhase
    extends CompilationPhase {
        private ProgramPointPhase() {
        }

        @Override
        FunctionNode transform(Compiler compiler, Compiler.CompilationPhases phases, FunctionNode fn) {
            return CompilationPhase.transformFunction(fn, new ProgramPoints());
        }

        public String toString() {
            return "'Program Point Calculation'";
        }
    }

    private static final class SplittingPhase
    extends CompilationPhase {
        private SplittingPhase() {
        }

        @Override
        FunctionNode transform(Compiler compiler, Compiler.CompilationPhases phases, FunctionNode fn) {
            CompileUnit outermostCompileUnit = compiler.addCompileUnit(0L);
            FunctionNode newFunctionNode = CompilationPhase.transformFunction(fn, new SimpleNodeVisitor(){

                @Override
                public LiteralNode<?> leaveLiteralNode(LiteralNode<?> literalNode) {
                    return literalNode.initialize(this.lc);
                }
            });
            newFunctionNode = new Splitter(compiler, newFunctionNode, outermostCompileUnit).split(newFunctionNode, true);
            newFunctionNode = CompilationPhase.transformFunction(newFunctionNode, new SplitIntoFunctions(compiler));
            assert (newFunctionNode.getCompileUnit() == outermostCompileUnit) : "fn=" + fn.getName() + ", fn.compileUnit (" + newFunctionNode.getCompileUnit() + ") != " + outermostCompileUnit;
            assert (newFunctionNode.isStrict() == compiler.isStrict()) : "functionNode.isStrict() != compiler.isStrict() for " + DebugLogger.quote(newFunctionNode.getName());
            return newFunctionNode;
        }

        public String toString() {
            return "'Code Splitting'";
        }
    }

    private static final class ApplySpecializationPhase
    extends CompilationPhase {
        private ApplySpecializationPhase() {
        }

        @Override
        FunctionNode transform(Compiler compiler, Compiler.CompilationPhases phases, FunctionNode fn) {
            return CompilationPhase.transformFunction(fn, new ApplySpecialization(compiler));
        }

        public String toString() {
            return "'Builtin Replacement'";
        }
    }

    private static final class LoweringPhase
    extends CompilationPhase {
        private LoweringPhase() {
        }

        @Override
        FunctionNode transform(Compiler compiler, Compiler.CompilationPhases phases, FunctionNode fn) {
            return CompilationPhase.transformFunction(fn, new Lower(compiler));
        }

        public String toString() {
            return "'Control Flow Lowering'";
        }
    }

    private static final class ConstantFoldingPhase
    extends CompilationPhase {
        private ConstantFoldingPhase() {
        }

        @Override
        FunctionNode transform(Compiler compiler, Compiler.CompilationPhases phases, FunctionNode fn) {
            return CompilationPhase.transformFunction(fn, new FoldConstants(compiler));
        }

        public String toString() {
            return "'Constant Folding'";
        }
    }
}

