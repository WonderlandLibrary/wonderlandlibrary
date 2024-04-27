/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import jdk.nashorn.api.scripting.NashornException;
import jdk.nashorn.internal.codegen.Compiler;
import jdk.nashorn.internal.ir.FunctionNode;
import jdk.nashorn.internal.ir.debug.ASTWriter;
import jdk.nashorn.internal.ir.debug.PrintVisitor;
import jdk.nashorn.internal.objects.Global;
import jdk.nashorn.internal.parser.Parser;
import jdk.nashorn.internal.runtime.Context;
import jdk.nashorn.internal.runtime.ErrorManager;
import jdk.nashorn.internal.runtime.JSType;
import jdk.nashorn.internal.runtime.ScriptEnvironment;
import jdk.nashorn.internal.runtime.ScriptFunction;
import jdk.nashorn.internal.runtime.ScriptRuntime;
import jdk.nashorn.internal.runtime.ScriptingFunctions;
import jdk.nashorn.internal.runtime.Source;
import jdk.nashorn.internal.runtime.options.Options;

public class Shell {
    private static final String MESSAGE_RESOURCE = "jdk.nashorn.tools.resources.Shell";
    private static final ResourceBundle bundle = ResourceBundle.getBundle("jdk.nashorn.tools.resources.Shell", Locale.getDefault());
    public static final int SUCCESS = 0;
    public static final int COMMANDLINE_ERROR = 100;
    public static final int COMPILATION_ERROR = 101;
    public static final int RUNTIME_ERROR = 102;
    public static final int IO_ERROR = 103;
    public static final int INTERNAL_ERROR = 104;

    protected Shell() {
    }

    public static void main(String[] args2) {
        try {
            int exitCode = Shell.main(System.in, System.out, System.err, args2);
            if (exitCode != 0) {
                System.exit(exitCode);
            }
        }
        catch (IOException e) {
            System.err.println(e);
            System.exit(103);
        }
    }

    public static int main(InputStream in, OutputStream out, OutputStream err, String[] args2) throws IOException {
        return new Shell().run(in, out, err, args2);
    }

    protected final int run(InputStream in, OutputStream out, OutputStream err, String[] args2) throws IOException {
        Context context = Shell.makeContext(in, out, err, args2);
        if (context == null) {
            return 100;
        }
        Global global = context.createGlobal();
        ScriptEnvironment env = context.getEnv();
        List<String> files = env.getFiles();
        if (files.isEmpty()) {
            return Shell.readEvalPrint(context, global);
        }
        if (env._compile_only) {
            return Shell.compileScripts(context, global, files);
        }
        if (env._fx) {
            return Shell.runFXScripts(context, global, files);
        }
        return this.runScripts(context, global, files);
    }

    private static Context makeContext(InputStream in, OutputStream out, OutputStream err, String[] args2) {
        PrintStream pout = out instanceof PrintStream ? (PrintStream)out : new PrintStream(out);
        PrintStream perr = err instanceof PrintStream ? (PrintStream)err : new PrintStream(err);
        PrintWriter wout = new PrintWriter(pout, true);
        PrintWriter werr = new PrintWriter(perr, true);
        ErrorManager errors = new ErrorManager(werr);
        Options options = new Options("nashorn", werr);
        if (args2 != null) {
            try {
                String[] prepArgs = Shell.preprocessArgs(args2);
                options.process(prepArgs);
            }
            catch (IllegalArgumentException e) {
                werr.println(bundle.getString("shell.usage"));
                options.displayHelp(e);
                return null;
            }
        }
        if (!options.getBoolean("scripting")) {
            for (String fileName : options.getFiles()) {
                File firstFile = new File(fileName);
                if (!firstFile.isFile()) continue;
                try {
                    FileReader fr = new FileReader(firstFile);
                    Throwable throwable = null;
                    try {
                        int firstChar = fr.read();
                        if (firstChar != 35) continue;
                        options.set("scripting", true);
                        break;
                    }
                    catch (Throwable throwable2) {
                        throwable = throwable2;
                        throw throwable2;
                    }
                    finally {
                        if (fr == null) continue;
                        if (throwable != null) {
                            try {
                                fr.close();
                            }
                            catch (Throwable throwable3) {
                                throwable.addSuppressed(throwable3);
                            }
                            continue;
                        }
                        fr.close();
                    }
                }
                catch (IOException iOException) {}
            }
        }
        return new Context(options, errors, wout, werr, Thread.currentThread().getContextClassLoader());
    }

    private static String[] preprocessArgs(String[] args2) {
        if (args2.length == 0) {
            return args2;
        }
        ArrayList<String> processedArgs = new ArrayList<String>();
        processedArgs.addAll(Arrays.asList(args2));
        if (args2[0].startsWith("-") && !System.getProperty("os.name", "generic").startsWith("Mac OS X")) {
            processedArgs.addAll(0, ScriptingFunctions.tokenizeString((String)processedArgs.remove(0)));
        }
        int shebangFilePos = -1;
        for (int i = 0; i < processedArgs.size(); ++i) {
            String a = (String)processedArgs.get(i);
            if (a.startsWith("-")) continue;
            Path p = Paths.get(a, new String[0]);
            String l = "";
            try (BufferedReader r = Files.newBufferedReader(p);){
                l = r.readLine();
            }
            catch (IOException iOException) {
                // empty catch block
            }
            if (!l.startsWith("#!")) break;
            shebangFilePos = i;
            break;
        }
        if (shebangFilePos != -1) {
            processedArgs.add(shebangFilePos + 1, "--");
        }
        return processedArgs.toArray(new String[0]);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private static int compileScripts(Context context, Global global, List<String> files) throws IOException {
        Global oldGlobal = Context.getGlobal();
        boolean globalChanged = oldGlobal != global;
        ScriptEnvironment env = context.getEnv();
        try {
            if (globalChanged) {
                Context.setGlobal(global);
            }
            ErrorManager errors = context.getErrorManager();
            for (String fileName : files) {
                FunctionNode functionNode = new Parser(env, Source.sourceFor(fileName, new File(fileName)), errors, env._strict, 0, context.getLogger(Parser.class)).parse();
                if (errors.getNumberOfErrors() != 0) {
                    int n = 101;
                    return n;
                }
                Compiler.forNoInstallerCompilation(context, functionNode.getSource(), env._strict | functionNode.isStrict()).compile(functionNode, Compiler.CompilationPhases.COMPILE_ALL_NO_INSTALL);
                if (env._print_ast) {
                    context.getErr().println(new ASTWriter(functionNode));
                }
                if (env._print_parse) {
                    context.getErr().println(new PrintVisitor(functionNode));
                }
                if (errors.getNumberOfErrors() == 0) continue;
                int n = 101;
                return n;
            }
        }
        finally {
            env.getOut().flush();
            env.getErr().flush();
            if (globalChanged) {
                Context.setGlobal(oldGlobal);
            }
        }
        return 0;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private int runScripts(Context context, Global global, List<String> files) throws IOException {
        Global oldGlobal = Context.getGlobal();
        boolean globalChanged = oldGlobal != global;
        try {
            if (globalChanged) {
                Context.setGlobal(global);
            }
            ErrorManager errors = context.getErrorManager();
            for (String fileName : files) {
                if ("-".equals(fileName)) {
                    int res = Shell.readEvalPrint(context, global);
                    if (res == 0) continue;
                    int n = res;
                    return n;
                }
                File file = new File(fileName);
                ScriptFunction script = context.compileScript(Source.sourceFor(fileName, file), global);
                if (script == null || errors.getNumberOfErrors() != 0) {
                    int n = 101;
                    return n;
                }
                try {
                    this.apply(script, global);
                }
                catch (NashornException e) {
                    errors.error(e.toString());
                    if (context.getEnv()._dump_on_error) {
                        e.printStackTrace(context.getErr());
                    }
                    int n = 102;
                    context.getOut().flush();
                    context.getErr().flush();
                    if (globalChanged) {
                        Context.setGlobal(oldGlobal);
                    }
                    return n;
                }
            }
        }
        finally {
            context.getOut().flush();
            context.getErr().flush();
            if (globalChanged) {
                Context.setGlobal(oldGlobal);
            }
        }
        return 0;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private static int runFXScripts(Context context, Global global, List<String> files) throws IOException {
        Global oldGlobal = Context.getGlobal();
        boolean globalChanged = oldGlobal != global;
        try {
            if (globalChanged) {
                Context.setGlobal(global);
            }
            global.addOwnProperty("$GLOBAL", 2, global);
            global.addOwnProperty("$SCRIPTS", 2, files);
            context.load(global, "fx:bootstrap.js");
        }
        catch (NashornException e) {
            context.getErrorManager().error(e.toString());
            if (context.getEnv()._dump_on_error) {
                e.printStackTrace(context.getErr());
            }
            int n = 102;
            return n;
        }
        finally {
            context.getOut().flush();
            context.getErr().flush();
            if (globalChanged) {
                Context.setGlobal(oldGlobal);
            }
        }
        return 0;
    }

    protected Object apply(ScriptFunction target, Object self) {
        return ScriptRuntime.apply(target, self, new Object[0]);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private static int readEvalPrint(Context context, Global global) {
        String prompt = bundle.getString("shell.prompt");
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter err = context.getErr();
        Global oldGlobal = Context.getGlobal();
        boolean globalChanged = oldGlobal != global;
        ScriptEnvironment env = context.getEnv();
        try {
            if (globalChanged) {
                Context.setGlobal(global);
            }
            global.addShellBuiltins();
            while (true) {
                err.print(prompt);
                err.flush();
                String source = "";
                try {
                    source = in.readLine();
                }
                catch (IOException ioe) {
                    err.println(ioe.toString());
                }
                if (source == null) {
                    break;
                }
                if (source.isEmpty()) continue;
                try {
                    Object res = context.eval(global, source, global, "<shell>");
                    if (res == ScriptRuntime.UNDEFINED) continue;
                    err.println(JSType.toString(res));
                }
                catch (Exception e) {
                    err.println(e);
                    if (!env._dump_on_error) continue;
                    e.printStackTrace(err);
                }
            }
        }
        finally {
            if (globalChanged) {
                Context.setGlobal(oldGlobal);
            }
        }
        return 0;
    }
}

