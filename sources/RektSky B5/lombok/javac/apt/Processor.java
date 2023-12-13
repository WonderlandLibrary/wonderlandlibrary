/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.sun.tools.javac.processing.JavacFiler
 *  com.sun.tools.javac.processing.JavacProcessingEnvironment
 *  com.sun.tools.javac.util.Context
 *  com.sun.tools.javac.util.Options
 *  lombok.permit.Permit
 */
package lombok.javac.apt;

import com.sun.tools.javac.processing.JavacFiler;
import com.sun.tools.javac.processing.JavacProcessingEnvironment;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.Options;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import javax.tools.JavaFileManager;
import javax.tools.StandardLocation;
import lombok.permit.Permit;

@Deprecated
@SupportedAnnotationTypes(value={"*"})
public class Processor
extends AbstractProcessor {
    @Override
    public void init(ProcessingEnvironment procEnv) {
        super.init(procEnv);
        if (System.getProperty("lombok.disable") != null) {
            return;
        }
        procEnv.getMessager().printMessage(Diagnostic.Kind.WARNING, "Wrong usage of 'lombok.javac.apt.Processor'. " + this.report(procEnv));
    }

    private String report(ProcessingEnvironment procEnv) {
        String data = this.collectData(procEnv);
        try {
            return this.writeFile(data);
        }
        catch (Exception exception) {
            return "Report:\n\n" + data;
        }
    }

    private String writeFile(String data) throws IOException {
        File file = File.createTempFile("lombok-processor-report-", ".txt");
        OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(file));
        writer.write(data);
        writer.close();
        return "Report written to '" + file.getCanonicalPath() + "'\n";
    }

    private String collectData(ProcessingEnvironment procEnv) {
        StringBuilder message = new StringBuilder();
        message.append("Problem report for usages of 'lombok.javac.apt.Processor'\n\n");
        this.listOptions(message, procEnv);
        this.findServices(message, procEnv.getFiler());
        this.addStacktrace(message);
        this.listProperties(message);
        return message.toString();
    }

    private void listOptions(StringBuilder message, ProcessingEnvironment procEnv) {
        try {
            JavacProcessingEnvironment environment = (JavacProcessingEnvironment)procEnv;
            Options instance = Options.instance((Context)environment.getContext());
            Field field = Permit.getField(Options.class, (String)"values");
            Map values = (Map)field.get(instance);
            if (values.isEmpty()) {
                message.append("Options: empty\n\n");
                return;
            }
            message.append("Compiler Options:\n");
            for (Map.Entry value : values.entrySet()) {
                message.append("- ");
                Processor.string(message, (String)value.getKey());
                message.append(" = ");
                Processor.string(message, (String)value.getValue());
                message.append("\n");
            }
            message.append("\n");
        }
        catch (Exception exception) {
            message.append("No options available\n\n");
        }
    }

    private void findServices(StringBuilder message, Filer filer) {
        try {
            Field filerFileManagerField = Permit.getField(JavacFiler.class, (String)"fileManager");
            JavaFileManager jfm = (JavaFileManager)filerFileManagerField.get(filer);
            ClassLoader processorClassLoader = jfm.hasLocation(StandardLocation.ANNOTATION_PROCESSOR_PATH) ? jfm.getClassLoader(StandardLocation.ANNOTATION_PROCESSOR_PATH) : jfm.getClassLoader(StandardLocation.CLASS_PATH);
            Enumeration<URL> resources = processorClassLoader.getResources("META-INF/services/javax.annotation.processing.Processor");
            if (!resources.hasMoreElements()) {
                message.append("No processors discovered\n\n");
                return;
            }
            message.append("Discovered processors:\n");
            while (resources.hasMoreElements()) {
                URL processorUrl = resources.nextElement();
                message.append("- '").append(processorUrl).append("'");
                InputStream content = (InputStream)processorUrl.getContent();
                if (content == null) continue;
                try {
                    InputStreamReader reader = new InputStreamReader(content, "UTF-8");
                    StringWriter sw = new StringWriter();
                    char[] buffer = new char[8192];
                    int read = 0;
                    while ((read = reader.read(buffer)) != -1) {
                        sw.write(buffer, 0, read);
                    }
                    String wholeFile = sw.toString();
                    if (wholeFile.contains("lombok.javac.apt.Processor")) {
                        message.append(" <= problem\n");
                    } else {
                        message.append(" (ok)\n");
                    }
                    message.append("    ").append(wholeFile.replace("\n", "\n    ")).append("\n");
                }
                finally {
                    content.close();
                }
            }
        }
        catch (Exception exception) {
            message.append("Filer information unavailable\n");
        }
        message.append("\n");
    }

    private void addStacktrace(StringBuilder message) {
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        if (stackTraceElements != null) {
            message.append("Called from\n");
            int i2 = 1;
            while (i2 < stackTraceElements.length) {
                StackTraceElement element = stackTraceElements[i2];
                if (!element.getClassName().equals("lombok.javac.apt.Processor")) {
                    message.append("- ").append(element).append("\n");
                }
                ++i2;
            }
        } else {
            message.append("No stacktrace available\n");
        }
        message.append("\n");
    }

    private void listProperties(StringBuilder message) {
        Properties properties = System.getProperties();
        ArrayList<String> propertyNames = new ArrayList<String>(properties.stringPropertyNames());
        Collections.sort(propertyNames);
        message.append("Properties: \n");
        for (String propertyName : propertyNames) {
            if (propertyName.startsWith("user.")) continue;
            message.append("- ").append(propertyName).append(" = ");
            Processor.string(message, System.getProperty(propertyName));
            message.append("\n");
        }
        message.append("\n");
    }

    private static void string(StringBuilder sb, String s2) {
        if (s2 == null) {
            sb.append("null");
            return;
        }
        sb.append("\"");
        int i2 = 0;
        while (i2 < s2.length()) {
            sb.append(Processor.escape(s2.charAt(i2)));
            ++i2;
        }
        sb.append("\"");
    }

    private static String escape(char ch) {
        switch (ch) {
            case '\b': {
                return "\\b";
            }
            case '\f': {
                return "\\f";
            }
            case '\n': {
                return "\\n";
            }
            case '\r': {
                return "\\r";
            }
            case '\t': {
                return "\\t";
            }
            case '\'': {
                return "\\'";
            }
            case '\"': {
                return "\\\"";
            }
            case '\\': {
                return "\\\\";
            }
        }
        if (ch < ' ') {
            return String.format("\\%03o", ch);
        }
        return String.valueOf(ch);
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latest();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        return false;
    }
}

