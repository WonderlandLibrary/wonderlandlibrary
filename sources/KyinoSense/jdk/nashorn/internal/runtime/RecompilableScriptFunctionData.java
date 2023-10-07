/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.internal.runtime;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.invoke.TypeDescriptor;
import java.lang.ref.Reference;
import java.lang.ref.SoftReference;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import jdk.internal.dynalink.support.NameCodec;
import jdk.nashorn.internal.codegen.Compiler;
import jdk.nashorn.internal.codegen.CompilerConstants;
import jdk.nashorn.internal.codegen.FunctionSignature;
import jdk.nashorn.internal.codegen.Namespace;
import jdk.nashorn.internal.codegen.OptimisticTypesPersistence;
import jdk.nashorn.internal.codegen.TypeMap;
import jdk.nashorn.internal.codegen.types.Type;
import jdk.nashorn.internal.ir.Block;
import jdk.nashorn.internal.ir.ForNode;
import jdk.nashorn.internal.ir.FunctionNode;
import jdk.nashorn.internal.ir.IdentNode;
import jdk.nashorn.internal.ir.LexicalContext;
import jdk.nashorn.internal.ir.Node;
import jdk.nashorn.internal.ir.SwitchNode;
import jdk.nashorn.internal.ir.Symbol;
import jdk.nashorn.internal.ir.TryNode;
import jdk.nashorn.internal.ir.visitor.NodeVisitor;
import jdk.nashorn.internal.ir.visitor.SimpleNodeVisitor;
import jdk.nashorn.internal.lookup.Lookup;
import jdk.nashorn.internal.objects.Global;
import jdk.nashorn.internal.parser.Parser;
import jdk.nashorn.internal.parser.Token;
import jdk.nashorn.internal.parser.TokenType;
import jdk.nashorn.internal.runtime.AllocationStrategy;
import jdk.nashorn.internal.runtime.AstDeserializer;
import jdk.nashorn.internal.runtime.AstSerializer;
import jdk.nashorn.internal.runtime.CodeInstaller;
import jdk.nashorn.internal.runtime.CodeStore;
import jdk.nashorn.internal.runtime.CompiledFunction;
import jdk.nashorn.internal.runtime.Context;
import jdk.nashorn.internal.runtime.FunctionInitializer;
import jdk.nashorn.internal.runtime.PropertyMap;
import jdk.nashorn.internal.runtime.ScriptEnvironment;
import jdk.nashorn.internal.runtime.ScriptFunction;
import jdk.nashorn.internal.runtime.ScriptFunctionData;
import jdk.nashorn.internal.runtime.ScriptObject;
import jdk.nashorn.internal.runtime.Source;
import jdk.nashorn.internal.runtime.StoredScript;
import jdk.nashorn.internal.runtime.Timing;
import jdk.nashorn.internal.runtime.logging.DebugLogger;
import jdk.nashorn.internal.runtime.logging.Loggable;
import jdk.nashorn.internal.runtime.logging.Logger;
import jdk.nashorn.internal.runtime.options.Options;

@Logger(name="recompile")
public final class RecompilableScriptFunctionData
extends ScriptFunctionData
implements Loggable {
    public static final String RECOMPILATION_PREFIX = "Recompilation$";
    private static final ExecutorService astSerializerExecutorService = RecompilableScriptFunctionData.createAstSerializerExecutorService();
    private final int functionNodeId;
    private final String functionName;
    private final int lineNumber;
    private transient Source source;
    private volatile Object cachedAst;
    private final long token;
    private final AllocationStrategy allocationStrategy;
    private final Object endParserState;
    private transient CodeInstaller installer;
    private final Map<Integer, RecompilableScriptFunctionData> nestedFunctions;
    private RecompilableScriptFunctionData parent;
    private final int functionFlags;
    private static final MethodHandles.Lookup LOOKUP = MethodHandles.lookup();
    private transient DebugLogger log;
    private final Map<String, Integer> externalScopeDepths;
    private final Set<String> internalSymbols;
    private static final int GET_SET_PREFIX_LENGTH = "*et ".length();
    private static final long serialVersionUID = 4914839316174633726L;

    public RecompilableScriptFunctionData(FunctionNode functionNode, CodeInstaller installer, AllocationStrategy allocationStrategy, Map<Integer, RecompilableScriptFunctionData> nestedFunctions, Map<String, Integer> externalScopeDepths, Set<String> internalSymbols) {
        super(RecompilableScriptFunctionData.functionName(functionNode), Math.min(functionNode.getParameters().size(), 250), RecompilableScriptFunctionData.getDataFlags(functionNode));
        this.functionName = functionNode.getName();
        this.lineNumber = functionNode.getLineNumber();
        this.functionFlags = functionNode.getFlags() | (functionNode.needsCallee() ? 0x4000000 : 0);
        this.functionNodeId = functionNode.getId();
        this.source = functionNode.getSource();
        this.endParserState = functionNode.getEndParserState();
        this.token = RecompilableScriptFunctionData.tokenFor(functionNode);
        this.installer = installer;
        this.allocationStrategy = allocationStrategy;
        this.nestedFunctions = RecompilableScriptFunctionData.smallMap(nestedFunctions);
        this.externalScopeDepths = RecompilableScriptFunctionData.smallMap(externalScopeDepths);
        this.internalSymbols = RecompilableScriptFunctionData.smallSet(new HashSet<String>(internalSymbols));
        for (RecompilableScriptFunctionData nfn : nestedFunctions.values()) {
            assert (nfn.getParent() == null);
            nfn.setParent(this);
        }
        this.createLogger();
    }

    private static <K, V> Map<K, V> smallMap(Map<K, V> map) {
        if (map == null || map.isEmpty()) {
            return Collections.emptyMap();
        }
        if (map.size() == 1) {
            Map.Entry<K, V> entry = map.entrySet().iterator().next();
            return Collections.singletonMap(entry.getKey(), entry.getValue());
        }
        return map;
    }

    private static <T> Set<T> smallSet(Set<T> set) {
        if (set == null || set.isEmpty()) {
            return Collections.emptySet();
        }
        if (set.size() == 1) {
            return Collections.singleton(set.iterator().next());
        }
        return set;
    }

    @Override
    public DebugLogger getLogger() {
        return this.log;
    }

    @Override
    public DebugLogger initLogger(Context ctxt) {
        return ctxt.getLogger(this.getClass());
    }

    public boolean hasInternalSymbol(String symbolName) {
        return this.internalSymbols.contains(symbolName);
    }

    public int getExternalSymbolDepth(String symbolName) {
        Integer depth = this.externalScopeDepths.get(symbolName);
        return depth == null ? -1 : depth;
    }

    public Set<String> getExternalSymbolNames() {
        return Collections.unmodifiableSet(this.externalScopeDepths.keySet());
    }

    public Object getEndParserState() {
        return this.endParserState;
    }

    public RecompilableScriptFunctionData getParent() {
        return this.parent;
    }

    void setParent(RecompilableScriptFunctionData parent) {
        this.parent = parent;
    }

    @Override
    String toSource() {
        if (this.source != null && this.token != 0L) {
            return this.source.getString(Token.descPosition(this.token), Token.descLength(this.token));
        }
        return "function " + (this.name == null ? "" : this.name) + "() { [native code] }";
    }

    public void initTransients(Source src, CodeInstaller inst) {
        if (this.source == null && this.installer == null) {
            this.source = src;
            this.installer = inst;
        } else if (this.source != src || !this.installer.isCompatibleWith(inst)) {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public String toString() {
        return super.toString() + '@' + this.functionNodeId;
    }

    @Override
    public String toStringVerbose() {
        StringBuilder sb = new StringBuilder();
        sb.append("fnId=").append(this.functionNodeId).append(' ');
        if (this.source != null) {
            sb.append(this.source.getName()).append(':').append(this.lineNumber).append(' ');
        }
        return sb.toString() + super.toString();
    }

    @Override
    public String getFunctionName() {
        return this.functionName;
    }

    @Override
    public boolean inDynamicContext() {
        return this.getFunctionFlag(65536);
    }

    private static String functionName(FunctionNode fn) {
        if (fn.isAnonymous()) {
            return "";
        }
        FunctionNode.Kind kind = fn.getKind();
        if (kind == FunctionNode.Kind.GETTER || kind == FunctionNode.Kind.SETTER) {
            String name = NameCodec.decode(fn.getIdent().getName());
            return name.substring(GET_SET_PREFIX_LENGTH);
        }
        return fn.getIdent().getName();
    }

    private static long tokenFor(FunctionNode fn) {
        int position = Token.descPosition(fn.getFirstToken());
        long lastToken = Token.withDelimiter(fn.getLastToken());
        int length = Token.descPosition(lastToken) - position + (Token.descType(lastToken) == TokenType.EOL ? 0 : Token.descLength(lastToken));
        return Token.toDesc(TokenType.FUNCTION, position, length);
    }

    private static int getDataFlags(FunctionNode functionNode) {
        int flags = 4;
        if (functionNode.isStrict()) {
            flags |= 1;
        }
        if (functionNode.needsCallee()) {
            flags |= 8;
        }
        if (functionNode.usesThis() || functionNode.hasEval()) {
            flags |= 0x10;
        }
        if (functionNode.isVarArg()) {
            flags |= 0x20;
        }
        if (functionNode.getKind() == FunctionNode.Kind.GETTER || functionNode.getKind() == FunctionNode.Kind.SETTER) {
            flags |= 0x40;
        }
        return flags;
    }

    @Override
    PropertyMap getAllocatorMap(ScriptObject prototype) {
        return this.allocationStrategy.getAllocatorMap(prototype);
    }

    @Override
    ScriptObject allocate(PropertyMap map) {
        return this.allocationStrategy.allocate(map);
    }

    FunctionNode reparse() {
        FunctionNode cachedFunction = this.getCachedAst();
        if (cachedFunction != null) {
            assert (cachedFunction.isCached());
            return cachedFunction;
        }
        int descPosition = Token.descPosition(this.token);
        Context context = Context.getContextTrusted();
        Parser parser = new Parser(context.getEnv(), this.source, new Context.ThrowErrorManager(), this.isStrict(), this.lineNumber - 1, context.getLogger(Parser.class));
        if (this.getFunctionFlag(1)) {
            parser.setFunctionName(this.functionName);
        }
        parser.setReparsedFunction(this);
        FunctionNode program = parser.parse(CompilerConstants.PROGRAM.symbolName(), descPosition, Token.descLength(this.token), this.isPropertyAccessor());
        return (this.isProgram() ? program : this.extractFunctionFromScript(program)).setName(null, this.functionName);
    }

    private FunctionNode getCachedAst() {
        Object lCachedAst = this.cachedAst;
        if (lCachedAst instanceof Reference) {
            FunctionNode fn = (FunctionNode)((Reference)lCachedAst).get();
            if (fn != null) {
                return this.cloneSymbols(fn);
            }
        } else if (lCachedAst instanceof SerializedAst) {
            SerializedAst serializedAst = (SerializedAst)lCachedAst;
            FunctionNode cachedFn = (FunctionNode)serializedAst.cachedAst.get();
            if (cachedFn != null) {
                return this.cloneSymbols(cachedFn);
            }
            FunctionNode deserializedFn = this.deserialize(serializedAst.serializedAst);
            serializedAst.cachedAst = new SoftReference<FunctionNode>(deserializedFn);
            return deserializedFn;
        }
        return null;
    }

    public void setCachedAst(FunctionNode astToCache) {
        SoftReference<FunctionNode> ref;
        assert (astToCache.getId() == this.functionNodeId);
        assert (!(this.cachedAst instanceof SerializedAst));
        boolean isSplit = astToCache.isSplit();
        assert (!isSplit || this.cachedAst == null);
        final FunctionNode symbolClonedAst = this.cloneSymbols(astToCache);
        this.cachedAst = ref = new SoftReference<FunctionNode>(symbolClonedAst);
        if (isSplit) {
            astSerializerExecutorService.execute(new Runnable(){

                @Override
                public void run() {
                    RecompilableScriptFunctionData.this.cachedAst = new SerializedAst(symbolClonedAst, ref);
                }
            });
        }
    }

    private static ExecutorService createAstSerializerExecutorService() {
        int threads = Math.max(1, Options.getIntProperty("nashorn.serialize.threads", Runtime.getRuntime().availableProcessors() / 2));
        ThreadPoolExecutor service = new ThreadPoolExecutor(threads, threads, 1L, TimeUnit.MINUTES, new LinkedBlockingDeque<Runnable>(), new ThreadFactory(){

            @Override
            public Thread newThread(Runnable r) {
                Thread t = new Thread(r, "Nashorn AST Serializer");
                t.setDaemon(true);
                t.setPriority(4);
                return t;
            }
        });
        service.allowCoreThreadTimeOut(true);
        return service;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private FunctionNode deserialize(byte[] serializedAst) {
        ScriptEnvironment env = this.installer.getContext().getEnv();
        Timing timing = env._timing;
        long t1 = System.nanoTime();
        try {
            FunctionNode functionNode = AstDeserializer.deserialize(serializedAst).initializeDeserialized(this.source, new Namespace(env.getNamespace()));
            return functionNode;
        }
        finally {
            timing.accumulateTime("'Deserialize'", System.nanoTime() - t1);
        }
    }

    private FunctionNode cloneSymbols(FunctionNode fn) {
        final IdentityHashMap symbolReplacements = new IdentityHashMap();
        final boolean cached = fn.isCached();
        final Set blockDefinedSymbols = fn.isSplit() && !cached ? Collections.newSetFromMap(new IdentityHashMap()) : null;
        FunctionNode newFn = (FunctionNode)fn.accept((NodeVisitor)new SimpleNodeVisitor(){

            private Symbol getReplacement(Symbol original) {
                if (original == null) {
                    return null;
                }
                Symbol existingReplacement = (Symbol)symbolReplacements.get(original);
                if (existingReplacement != null) {
                    return existingReplacement;
                }
                Symbol newReplacement = original.clone();
                symbolReplacements.put(original, newReplacement);
                return newReplacement;
            }

            @Override
            public Node leaveIdentNode(IdentNode identNode) {
                Symbol oldSymbol = identNode.getSymbol();
                if (oldSymbol != null) {
                    Symbol replacement = this.getReplacement(oldSymbol);
                    return identNode.setSymbol(replacement);
                }
                return identNode;
            }

            @Override
            public Node leaveForNode(ForNode forNode) {
                return this.ensureUniqueLabels(forNode.setIterator(this.lc, this.getReplacement(forNode.getIterator())));
            }

            @Override
            public Node leaveSwitchNode(SwitchNode switchNode) {
                return this.ensureUniqueLabels(switchNode.setTag(this.lc, this.getReplacement(switchNode.getTag())));
            }

            @Override
            public Node leaveTryNode(TryNode tryNode) {
                return this.ensureUniqueLabels(tryNode.setException(this.lc, this.getReplacement(tryNode.getException())));
            }

            @Override
            public boolean enterBlock(Block block) {
                for (Symbol symbol : block.getSymbols()) {
                    Symbol replacement = this.getReplacement(symbol);
                    if (blockDefinedSymbols == null) continue;
                    blockDefinedSymbols.add(replacement);
                }
                return true;
            }

            @Override
            public Node leaveBlock(Block block) {
                return this.ensureUniqueLabels(block.replaceSymbols(this.lc, symbolReplacements));
            }

            @Override
            public Node leaveFunctionNode(FunctionNode functionNode) {
                return functionNode.setParameters(this.lc, functionNode.visitParameters(this));
            }

            @Override
            protected Node leaveDefault(Node node) {
                return this.ensureUniqueLabels(node);
            }

            private Node ensureUniqueLabels(Node node) {
                return cached ? node.ensureUniqueLabels(this.lc) : node;
            }
        });
        if (blockDefinedSymbols != null) {
            Block newBody = null;
            for (Symbol symbol : symbolReplacements.values()) {
                if (blockDefinedSymbols.contains(symbol)) continue;
                assert (symbol.isScope());
                assert (this.externalScopeDepths.containsKey(symbol.getName()));
                symbol.setFlags(symbol.getFlags() & 0xFFFFFFFC | 1);
                if (newBody == null) {
                    newBody = newFn.getBody().copyWithNewSymbols();
                    newFn = newFn.setBody(null, newBody);
                }
                assert (newBody.getExistingSymbol(symbol.getName()) == null);
                newBody.putSymbol(symbol);
            }
        }
        return newFn.setCached(null);
    }

    private boolean getFunctionFlag(int flag) {
        return (this.functionFlags & flag) != 0;
    }

    private boolean isProgram() {
        return this.getFunctionFlag(8192);
    }

    TypeMap typeMap(MethodType fnCallSiteType) {
        if (fnCallSiteType == null) {
            return null;
        }
        if (CompiledFunction.isVarArgsType(fnCallSiteType)) {
            return null;
        }
        return new TypeMap(this.functionNodeId, this.explicitParams(fnCallSiteType), this.needsCallee());
    }

    private static ScriptObject newLocals(ScriptObject runtimeScope) {
        ScriptObject locals = Global.newEmptyInstance();
        locals.setProto(runtimeScope);
        return locals;
    }

    private Compiler getCompiler(FunctionNode fn, MethodType actualCallSiteType, ScriptObject runtimeScope) {
        return this.getCompiler(fn, actualCallSiteType, RecompilableScriptFunctionData.newLocals(runtimeScope), null, null);
    }

    private CodeInstaller getInstallerForNewCode() {
        ScriptEnvironment env = this.installer.getContext().getEnv();
        return env._optimistic_types || env._loader_per_compile ? this.installer.withNewLoader() : this.installer;
    }

    Compiler getCompiler(FunctionNode functionNode, MethodType actualCallSiteType, ScriptObject runtimeScope, Map<Integer, Type> invalidatedProgramPoints, int[] continuationEntryPoints) {
        TypeMap typeMap = this.typeMap(actualCallSiteType);
        Type[] paramTypes = typeMap == null ? null : typeMap.getParameterTypes(this.functionNodeId);
        Object typeInformationFile = OptimisticTypesPersistence.getLocationDescriptor(this.source, this.functionNodeId, paramTypes);
        return Compiler.forOnDemandCompilation(this.getInstallerForNewCode(), functionNode.getSource(), this.isStrict() | functionNode.isStrict(), this, typeMap, RecompilableScriptFunctionData.getEffectiveInvalidatedProgramPoints(invalidatedProgramPoints, typeInformationFile), typeInformationFile, continuationEntryPoints, runtimeScope);
    }

    private static Map<Integer, Type> getEffectiveInvalidatedProgramPoints(Map<Integer, Type> invalidatedProgramPoints, Object typeInformationFile) {
        if (invalidatedProgramPoints != null) {
            return invalidatedProgramPoints;
        }
        TreeMap loadedProgramPoints = OptimisticTypesPersistence.load(typeInformationFile);
        return loadedProgramPoints != null ? loadedProgramPoints : new TreeMap();
    }

    private FunctionInitializer compileTypeSpecialization(MethodType actualCallSiteType, ScriptObject runtimeScope, boolean persist) {
        if (this.log.isEnabled()) {
            this.log.info("Parameter type specialization of '", this.functionName, "' signature: ", actualCallSiteType);
        }
        boolean persistentCache = persist && this.usePersistentCodeCache();
        String cacheKey = null;
        if (persistentCache) {
            TypeMap typeMap = this.typeMap(actualCallSiteType);
            Type[] paramTypes = typeMap == null ? null : typeMap.getParameterTypes(this.functionNodeId);
            cacheKey = CodeStore.getCacheKey(this.functionNodeId, paramTypes);
            CodeInstaller newInstaller = this.getInstallerForNewCode();
            StoredScript script = newInstaller.loadScript(this.source, cacheKey);
            if (script != null) {
                Compiler.updateCompilationId(script.getCompilationId());
                return script.installFunction(this, newInstaller);
            }
        }
        FunctionNode fn = this.reparse();
        Compiler compiler = this.getCompiler(fn, actualCallSiteType, runtimeScope);
        FunctionNode compiledFn = compiler.compile(fn, fn.isCached() ? Compiler.CompilationPhases.COMPILE_ALL_CACHED : Compiler.CompilationPhases.COMPILE_ALL);
        if (persist && !compiledFn.hasApplyToCallSpecialization()) {
            compiler.persistClassInfo(cacheKey, compiledFn);
        }
        return new FunctionInitializer(compiledFn, compiler.getInvalidatedProgramPoints());
    }

    boolean usePersistentCodeCache() {
        return this.installer != null && this.installer.getContext().getEnv()._persistent_cache;
    }

    private MethodType explicitParams(MethodType callSiteType) {
        MethodType generalized;
        if (CompiledFunction.isVarArgsType(callSiteType)) {
            return null;
        }
        MethodType noCalleeThisType = callSiteType.dropParameterTypes(0, 2);
        int callSiteParamCount = noCalleeThisType.parameterCount();
        Class<?>[] paramTypes = noCalleeThisType.parameterArray();
        boolean changed = false;
        for (int i = 0; i < paramTypes.length; ++i) {
            Class<?> paramType = paramTypes[i];
            if (paramType.isPrimitive() || paramType == Object.class) continue;
            paramTypes[i] = Object.class;
            changed = true;
        }
        MethodType methodType = generalized = changed ? MethodType.methodType(noCalleeThisType.returnType(), paramTypes) : noCalleeThisType;
        if (callSiteParamCount < this.getArity()) {
            return generalized.appendParameterTypes(Collections.nCopies(this.getArity() - callSiteParamCount, Object.class));
        }
        return generalized;
    }

    private FunctionNode extractFunctionFromScript(FunctionNode script) {
        final HashSet fns = new HashSet();
        script.getBody().accept(new SimpleNodeVisitor(){

            @Override
            public boolean enterFunctionNode(FunctionNode fn) {
                fns.add(fn);
                return false;
            }
        });
        assert (fns.size() == 1) : "got back more than one method in recompilation";
        FunctionNode f = (FunctionNode)fns.iterator().next();
        assert (f.getId() == this.functionNodeId);
        if (!this.getFunctionFlag(2) && f.isDeclared()) {
            return f.clearFlag(null, 2);
        }
        return f;
    }

    private void logLookup(boolean shouldLog, MethodType targetType) {
        if (shouldLog && this.log.isEnabled()) {
            this.log.info("Looking up ", DebugLogger.quote(this.functionName), " type=", targetType);
        }
    }

    private MethodHandle lookup(FunctionInitializer fnInit, boolean shouldLog) {
        MethodType type = fnInit.getMethodType();
        this.logLookup(shouldLog, type);
        return this.lookupCodeMethod(fnInit.getCode(), type);
    }

    MethodHandle lookup(FunctionNode fn) {
        MethodType type = new FunctionSignature(fn).getMethodType();
        this.logLookup(true, type);
        return this.lookupCodeMethod(fn.getCompileUnit().getCode(), type);
    }

    MethodHandle lookupCodeMethod(Class<?> codeClass, MethodType targetType) {
        return Lookup.MH.findStatic(LOOKUP, codeClass, this.functionName, targetType);
    }

    public void initializeCode(FunctionNode functionNode) {
        if (!this.code.isEmpty() || functionNode.getId() != this.functionNodeId || !functionNode.getCompileUnit().isInitializing(this, functionNode)) {
            throw new IllegalStateException(this.name);
        }
        this.addCode(this.lookup(functionNode), null, null, functionNode.getFlags());
    }

    void initializeCode(FunctionInitializer initializer) {
        this.addCode(this.lookup(initializer, true), null, null, initializer.getFlags());
    }

    private CompiledFunction addCode(MethodHandle target, Map<Integer, Type> invalidatedProgramPoints, MethodType callSiteType, int fnFlags) {
        CompiledFunction cfn = new CompiledFunction(target, this, invalidatedProgramPoints, callSiteType, fnFlags);
        assert (this.noDuplicateCode(cfn)) : "duplicate code";
        this.code.add(cfn);
        return cfn;
    }

    private CompiledFunction addCode(FunctionInitializer fnInit, MethodType callSiteType) {
        if (this.isVariableArity()) {
            return this.addCode(this.lookup(fnInit, true), fnInit.getInvalidatedProgramPoints(), callSiteType, fnInit.getFlags());
        }
        MethodHandle handle = this.lookup(fnInit, true);
        MethodType fromType = handle.type();
        MethodType toType = RecompilableScriptFunctionData.needsCallee(fromType) ? callSiteType.changeParameterType(0, ScriptFunction.class) : callSiteType.dropParameterTypes(0, 1);
        toType = toType.changeReturnType((Class<?>)fromType.returnType());
        int toCount = toType.parameterCount();
        int fromCount = fromType.parameterCount();
        int minCount = Math.min(fromCount, toCount);
        for (int i = 0; i < minCount; ++i) {
            TypeDescriptor.OfField toParam;
            TypeDescriptor.OfField fromParam = fromType.parameterType(i);
            if (fromParam == (toParam = toType.parameterType(i)) || ((Class)fromParam).isPrimitive() || ((Class)toParam).isPrimitive()) continue;
            assert (((Class)fromParam).isAssignableFrom((Class<?>)toParam));
            toType = toType.changeParameterType(i, (Class<?>)fromParam);
        }
        if (fromCount > toCount) {
            toType = toType.appendParameterTypes(fromType.parameterList().subList(toCount, fromCount));
        } else if (fromCount < toCount) {
            toType = toType.dropParameterTypes(fromCount, toCount);
        }
        return this.addCode(this.lookup(fnInit, false).asType(toType), fnInit.getInvalidatedProgramPoints(), callSiteType, fnInit.getFlags());
    }

    public Class<?> getReturnType(MethodType callSiteType, ScriptObject runtimeScope) {
        return this.getBest(callSiteType, runtimeScope, CompiledFunction.NO_FUNCTIONS).type().returnType();
    }

    @Override
    synchronized CompiledFunction getBest(MethodType callSiteType, ScriptObject runtimeScope, Collection<CompiledFunction> forbidden, boolean linkLogicOkay) {
        assert (this.isValidCallSite(callSiteType)) : callSiteType;
        CompiledFunction existingBest = this.pickFunction(callSiteType, false);
        if (existingBest == null) {
            existingBest = this.pickFunction(callSiteType, true);
        }
        if (existingBest == null) {
            existingBest = this.addCode(this.compileTypeSpecialization(callSiteType, runtimeScope, true), callSiteType);
        }
        assert (existingBest != null);
        if (existingBest.isApplyToCall()) {
            CompiledFunction best = this.lookupExactApplyToCall(callSiteType);
            if (best != null) {
                return best;
            }
            existingBest = this.addCode(this.compileTypeSpecialization(callSiteType, runtimeScope, false), callSiteType);
        }
        return existingBest;
    }

    @Override
    public boolean needsCallee() {
        return this.getFunctionFlag(0x4000000);
    }

    public int getFunctionFlags() {
        return this.functionFlags;
    }

    @Override
    MethodType getGenericType() {
        if (this.isVariableArity()) {
            return MethodType.genericMethodType(2, true);
        }
        return MethodType.genericMethodType(2 + this.getArity());
    }

    public int getFunctionNodeId() {
        return this.functionNodeId;
    }

    public Source getSource() {
        return this.source;
    }

    public RecompilableScriptFunctionData getScriptFunctionData(int functionId) {
        RecompilableScriptFunctionData data;
        if (functionId == this.functionNodeId) {
            return this;
        }
        RecompilableScriptFunctionData recompilableScriptFunctionData = data = this.nestedFunctions == null ? null : this.nestedFunctions.get(functionId);
        if (data != null) {
            return data;
        }
        for (RecompilableScriptFunctionData ndata : this.nestedFunctions.values()) {
            data = ndata.getScriptFunctionData(functionId);
            if (data == null) continue;
            return data;
        }
        return null;
    }

    public boolean isGlobalSymbol(FunctionNode functionNode, String symbolName) {
        RecompilableScriptFunctionData data = this.getScriptFunctionData(functionNode.getId());
        assert (data != null);
        do {
            if (!data.hasInternalSymbol(symbolName)) continue;
            return false;
        } while ((data = data.getParent()) != null);
        return true;
    }

    public FunctionNode restoreFlags(LexicalContext lc, FunctionNode fn) {
        assert (fn.getId() == this.functionNodeId);
        FunctionNode newFn = fn.setFlags(lc, this.functionFlags);
        if (newFn.hasNestedEval()) {
            assert (newFn.hasScopeBlock());
            newFn = newFn.setBody(lc, newFn.getBody().setNeedsScope(null));
        }
        return newFn;
    }

    private boolean noDuplicateCode(CompiledFunction compiledFunction) {
        for (CompiledFunction cf : this.code) {
            if (!cf.type().equals((Object)compiledFunction.type())) continue;
            return false;
        }
        return true;
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        this.createLogger();
    }

    private void createLogger() {
        this.log = this.initLogger(Context.getContextTrusted());
    }

    private static class SerializedAst {
        private final byte[] serializedAst;
        private volatile Reference<FunctionNode> cachedAst;

        SerializedAst(FunctionNode fn, Reference<FunctionNode> cachedAst) {
            this.serializedAst = AstSerializer.serialize(fn);
            this.cachedAst = cachedAst;
        }
    }
}

