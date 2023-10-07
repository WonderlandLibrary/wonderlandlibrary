/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.internal.codegen;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import jdk.nashorn.internal.codegen.ClassEmitter;
import jdk.nashorn.internal.runtime.ECMAErrors;
import jdk.nashorn.internal.runtime.ScriptEnvironment;
import jdk.nashorn.internal.runtime.logging.DebugLogger;

public final class DumpBytecode {
    public static void dumpBytecode(ScriptEnvironment env, DebugLogger logger, byte[] bytecode, String className) {
        block33: {
            File dir = null;
            try {
                if (env._print_code) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("class: " + className).append('\n').append(ClassEmitter.disassemble(bytecode)).append("=====");
                    if (env._print_code_dir != null) {
                        File file;
                        String name = className;
                        int dollar = name.lastIndexOf(36);
                        if (dollar != -1) {
                            name = name.substring(dollar + 1);
                        }
                        if (!(dir = new File(env._print_code_dir)).exists() && !dir.mkdirs()) {
                            throw new IOException(dir.toString());
                        }
                        int uniqueId = 0;
                        do {
                            String fileName = name + (uniqueId == 0 ? "" : "_" + uniqueId) + ".bytecode";
                            file = new File(env._print_code_dir, fileName);
                            ++uniqueId;
                        } while (file.exists());
                        try (PrintWriter pw = new PrintWriter(new FileOutputStream(file));){
                            pw.print(sb.toString());
                            pw.flush();
                        }
                    } else {
                        env.getErr().println(sb);
                    }
                }
                if (env._dest_dir == null) break block33;
                String fileName = className.replace('.', File.separatorChar) + ".class";
                int index = fileName.lastIndexOf(File.separatorChar);
                dir = index != -1 ? new File(env._dest_dir, fileName.substring(0, index)) : new File(env._dest_dir);
                if (!dir.exists() && !dir.mkdirs()) {
                    throw new IOException(dir.toString());
                }
                File file = new File(env._dest_dir, fileName);
                try (FileOutputStream fos = new FileOutputStream(file);){
                    fos.write(bytecode);
                }
                logger.info("Wrote class to '" + file.getAbsolutePath() + '\'');
            }
            catch (IOException e) {
                logger.warning("Skipping class dump for ", className, ": ", ECMAErrors.getMessage("io.error.cant.write", dir.toString()));
            }
        }
    }
}

