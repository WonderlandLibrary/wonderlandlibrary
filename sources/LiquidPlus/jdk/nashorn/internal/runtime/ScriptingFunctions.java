/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.internal.runtime;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StreamTokenizer;
import java.io.StringReader;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import jdk.nashorn.internal.lookup.Lookup;
import jdk.nashorn.internal.objects.Global;
import jdk.nashorn.internal.objects.NativeArray;
import jdk.nashorn.internal.runtime.Context;
import jdk.nashorn.internal.runtime.ECMAErrors;
import jdk.nashorn.internal.runtime.JSType;
import jdk.nashorn.internal.runtime.ScriptObject;
import jdk.nashorn.internal.runtime.ScriptRuntime;
import jdk.nashorn.internal.runtime.Source;
import jdk.nashorn.internal.runtime.Undefined;

public final class ScriptingFunctions {
    public static final MethodHandle READLINE = ScriptingFunctions.findOwnMH("readLine", Object.class, Object.class, Object.class);
    public static final MethodHandle READFULLY = ScriptingFunctions.findOwnMH("readFully", Object.class, Object.class, Object.class);
    public static final MethodHandle EXEC = ScriptingFunctions.findOwnMH("exec", Object.class, Object.class, Object[].class);
    public static final String EXEC_NAME = "$EXEC";
    public static final String OUT_NAME = "$OUT";
    public static final String ERR_NAME = "$ERR";
    public static final String EXIT_NAME = "$EXIT";
    public static final String THROW_ON_ERROR_NAME = "throwOnError";
    public static final String ENV_NAME = "$ENV";
    public static final String PWD_NAME = "PWD";

    private ScriptingFunctions() {
    }

    public static Object readLine(Object self, Object prompt) throws IOException {
        if (prompt != ScriptRuntime.UNDEFINED) {
            System.out.print(JSType.toString(prompt));
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        return reader.readLine();
    }

    public static Object readFully(Object self, Object file) throws IOException {
        File f = null;
        if (file instanceof File) {
            f = (File)file;
        } else if (JSType.isString(file)) {
            f = new File(((CharSequence)file).toString());
        }
        if (f == null || !f.isFile()) {
            throw ECMAErrors.typeError("not.a.file", ScriptRuntime.safeToString(file));
        }
        return new String(Source.readFully(f));
    }

    public static Object exec(Object self, Object ... args2) throws IOException, InterruptedException {
        Object[] additionalArgs;
        Global global = Context.getGlobal();
        Undefined string = args2.length > 0 ? args2[0] : ScriptRuntime.UNDEFINED;
        Undefined input = args2.length > 1 ? args2[1] : ScriptRuntime.UNDEFINED;
        Object[] argv = args2.length > 2 ? Arrays.copyOfRange(args2, 2, args2.length) : ScriptRuntime.EMPTY_ARRAY;
        List<String> cmdLine = ScriptingFunctions.tokenizeString(JSType.toString(string));
        for (Object arg : additionalArgs = argv.length == 1 && argv[0] instanceof NativeArray ? ((NativeArray)argv[0]).asObjectArray() : argv) {
            cmdLine.add(JSType.toString(arg));
        }
        ProcessBuilder processBuilder = new ProcessBuilder(cmdLine);
        Object env = global.get(ENV_NAME);
        if (env instanceof ScriptObject) {
            File pwdFile;
            ScriptObject envProperties = (ScriptObject)env;
            Object pwd = envProperties.get(PWD_NAME);
            if (pwd != ScriptRuntime.UNDEFINED && (pwdFile = new File(JSType.toString(pwd))).exists()) {
                processBuilder.directory(pwdFile);
            }
            Map<String, String> environment = processBuilder.environment();
            environment.clear();
            for (Map.Entry<Object, Object> entry : envProperties.entrySet()) {
                environment.put(JSType.toString(entry.getKey()), JSType.toString(entry.getValue()));
            }
        }
        final Process process2 = processBuilder.start();
        final IOException[] exception = new IOException[2];
        final StringBuilder outBuffer = new StringBuilder();
        Thread outThread = new Thread(new Runnable(){

            @Override
            public void run() {
                char[] buffer = new char[1024];
                try (InputStreamReader inputStream = new InputStreamReader(process2.getInputStream());){
                    int length;
                    while ((length = inputStream.read(buffer, 0, buffer.length)) != -1) {
                        outBuffer.append(buffer, 0, length);
                    }
                }
                catch (IOException ex) {
                    exception[0] = ex;
                }
            }
        }, "$EXEC output");
        final StringBuilder errBuffer = new StringBuilder();
        Thread errThread = new Thread(new Runnable(){

            @Override
            public void run() {
                char[] buffer = new char[1024];
                try (InputStreamReader inputStream = new InputStreamReader(process2.getErrorStream());){
                    int length;
                    while ((length = inputStream.read(buffer, 0, buffer.length)) != -1) {
                        errBuffer.append(buffer, 0, length);
                    }
                }
                catch (IOException ex) {
                    exception[1] = ex;
                }
            }
        }, "$EXEC error");
        outThread.start();
        errThread.start();
        if (!JSType.nullOrUndefined(input)) {
            try (OutputStreamWriter outputStream2 = new OutputStreamWriter(process2.getOutputStream());){
                String in = JSType.toString(input);
                outputStream2.write(in, 0, in.length());
            }
            catch (IOException outputStream2) {
                // empty catch block
            }
        }
        int exit = process2.waitFor();
        outThread.join();
        errThread.join();
        String out = outBuffer.toString();
        String err = errBuffer.toString();
        global.set((Object)OUT_NAME, (Object)out, 0);
        global.set((Object)ERR_NAME, (Object)err, 0);
        global.set((Object)EXIT_NAME, exit, 0);
        for (IOException element : exception) {
            if (element == null) continue;
            throw element;
        }
        if (exit != 0) {
            Object exec = global.get(EXEC_NAME);
            assert (exec instanceof ScriptObject) : "$EXEC is not a script object!";
            if (JSType.toBoolean(((ScriptObject)exec).get(THROW_ON_ERROR_NAME))) {
                throw ECMAErrors.rangeError("exec.returned.non.zero", ScriptRuntime.safeToString(exit));
            }
        }
        return out;
    }

    private static MethodHandle findOwnMH(String name, Class<?> rtype, Class<?> ... types) {
        return Lookup.MH.findStatic(MethodHandles.lookup(), ScriptingFunctions.class, name, Lookup.MH.type(rtype, types));
    }

    public static List<String> tokenizeString(String str) {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader(str));
        tokenizer.resetSyntax();
        tokenizer.wordChars(0, 255);
        tokenizer.whitespaceChars(0, 32);
        tokenizer.commentChar(35);
        tokenizer.quoteChar(34);
        tokenizer.quoteChar(39);
        ArrayList<String> tokenList = new ArrayList<String>();
        StringBuilder toAppend = new StringBuilder();
        while (ScriptingFunctions.nextToken(tokenizer) != -1) {
            String s = tokenizer.sval;
            if (s.endsWith("\\")) {
                toAppend.append(s.substring(0, s.length() - 1)).append(' ');
                continue;
            }
            tokenList.add(toAppend.append(s).toString());
            toAppend.setLength(0);
        }
        if (toAppend.length() != 0) {
            tokenList.add(toAppend.toString());
        }
        return tokenList;
    }

    private static int nextToken(StreamTokenizer tokenizer) {
        try {
            return tokenizer.nextToken();
        }
        catch (IOException ioe) {
            return -1;
        }
    }
}

