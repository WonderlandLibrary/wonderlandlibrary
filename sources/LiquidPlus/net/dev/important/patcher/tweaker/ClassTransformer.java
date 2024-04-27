/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ArrayListMultimap
 *  com.google.common.collect.Multimap
 *  com.google.gson.Gson
 *  net.minecraft.launchwrapper.IClassTransformer
 *  net.minecraft.launchwrapper.Launch
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
 *  org.objectweb.asm.ClassReader
 *  org.objectweb.asm.ClassVisitor
 *  org.objectweb.asm.tree.ClassNode
 *  org.objectweb.asm.tree.FieldNode
 */
package net.dev.important.patcher.tweaker;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.google.gson.Gson;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import javax.net.ssl.HttpsURLConnection;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import net.dev.important.patcher.asm.forge.LightUtilTransformer;
import net.dev.important.patcher.optifine.OptiFineGenerations;
import net.dev.important.patcher.tweaker.PatcherClassWriter;
import net.dev.important.patcher.tweaker.PatcherTweaker;
import net.dev.important.patcher.tweaker.transform.PatcherTransformer;
import net.dev.important.utils.misc.MiscUtils;
import net.minecraft.launchwrapper.IClassTransformer;
import net.minecraft.launchwrapper.Launch;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.spongepowered.asm.mixin.MixinEnvironment;

public class ClassTransformer
implements IClassTransformer {
    public static final boolean outputBytecode = "true".equals(System.getProperty("patcher.debugBytecode", "false"));
    public static String optifineVersion = "NONE";
    private final Logger logger = LogManager.getLogger((String)"Patcher - Class Transformer");
    private final Multimap<String, PatcherTransformer> transformerMap = ArrayListMultimap.create();
    public static boolean smoothFontDetected;
    public static final Set<String> supportedOptiFineVersions;
    public static OptiFineGenerations generations;

    public ClassTransformer() {
        MixinEnvironment.getCurrentEnvironment().addTransformerExclusion(this.getClass().getName());
        try {
            ClassLoader classLoader = this.getClass().getClassLoader();
            if (classLoader.getResource("bre/smoothfont/mod_SmoothFont.class") != null) {
                smoothFontDetected = true;
                this.logger.warn("SmoothFont detected, disabling FontRenderer optimizations.");
            }
            this.fetchSupportedOptiFineVersions();
            this.updateOptiFineGenerations();
            ClassNode classNode = new ClassNode();
            ClassReader classReader = new ClassReader("Config");
            classReader.accept((ClassVisitor)classNode, 1);
            for (FieldNode fieldNode : classNode.fields) {
                if (!fieldNode.name.equals("OF_RELEASE")) continue;
                optifineVersion = (String)fieldNode.value;
                break;
            }
            if (!supportedOptiFineVersions.contains(optifineVersion)) {
                this.logger.info("User has outdated OptiFine. (version: OptiFine-{})", new Object[]{optifineVersion});
                this.haltForOptifine("OptiFine " + optifineVersion + " has been detected, which is not supported by Patcher and will crash.\nPlease update to a newer version of OptiFine (i7 and above are supported) before trying to launch.");
                return;
            }
        }
        catch (IOException iOException) {
            // empty catch block
        }
        this.registerTransformer(new LightUtilTransformer());
    }

    public static byte[] createTransformer(String transformedName, byte[] bytes, Multimap<String, PatcherTransformer> transformerMap, Logger logger) {
        if (bytes == null) {
            return null;
        }
        Collection transformers = transformerMap.get((Object)transformedName);
        if (transformers.isEmpty()) {
            return bytes;
        }
        ClassReader classReader = new ClassReader(bytes);
        ClassNode classNode = new ClassNode();
        classReader.accept((ClassVisitor)classNode, 8);
        for (PatcherTransformer transformer : transformers) {
            transformer.transform(classNode, transformedName);
        }
        PatcherClassWriter classWriter = new PatcherClassWriter(2);
        try {
            classNode.accept((ClassVisitor)classWriter);
        }
        catch (Throwable e) {
            logger.error("Exception when transforming {} : {}", new Object[]{transformedName, e.getClass().getSimpleName(), e});
        }
        if (outputBytecode) {
            int lastIndex;
            File bytecodeDirectory = new File("bytecode");
            if (!bytecodeDirectory.exists()) {
                bytecodeDirectory.mkdirs();
            }
            if ((lastIndex = transformedName.lastIndexOf(46)) != -1) {
                transformedName = transformedName.substring(lastIndex + 1) + ".class";
            }
            try {
                File bytecodeOutput = new File(bytecodeDirectory, transformedName);
                if (!bytecodeOutput.exists()) {
                    bytecodeOutput.createNewFile();
                }
                try (FileOutputStream os = new FileOutputStream(bytecodeOutput);){
                    os.write(classWriter.toByteArray());
                }
                catch (IOException e) {
                    logger.error("Failed to create bytecode output for {}.", new Object[]{transformedName, e});
                }
            }
            catch (Exception exception) {
                // empty catch block
            }
        }
        return classWriter.toByteArray();
    }

    private void registerTransformer(PatcherTransformer transformer) {
        for (String cls : transformer.getClassName()) {
            this.transformerMap.put((Object)cls, (Object)transformer);
        }
    }

    public byte[] transform(String name, String transformedName, byte[] bytes) {
        return ClassTransformer.createTransformer(transformedName, bytes, this.transformerMap, this.logger);
    }

    private void haltForOptifine(String message) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        JButton openOptifine = new JButton("Open OptiFine Website");
        openOptifine.addMouseListener(new MouseAdapter(){

            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    MiscUtils.showURL("https://optifine.net/downloads/");
                }
                catch (Exception ex) {
                    JLabel label = new JLabel();
                    label.setText("Failed to open OptiFine website.");
                    label.setAlignmentX(0.5f);
                    label.setAlignmentY(0.5f);
                }
            }
        });
        JButton close = new JButton("Close");
        close.addMouseListener(new MouseAdapter(){

            @Override
            public void mouseClicked(MouseEvent e) {
                PatcherTweaker.invokeExit();
            }
        });
        Object[] options = new Object[]{openOptifine, close};
        JOptionPane.showOptionDialog(null, message, "Launch Aborted", -1, 0, null, options, options[0]);
        PatcherTweaker.invokeExit();
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private void fetchSupportedOptiFineVersions() {
        HttpURLConnection connection = null;
        try {
            URL optifineVersions = new URL("https://static.sk1er.club/patcher/optifine.txt");
            connection = (HttpsURLConnection)optifineVersions.openConnection();
            connection.setRequestProperty("User-Agent", "Patcher OptiFine Fetcher");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));){
                String version;
                while ((version = reader.readLine()) != null) {
                    supportedOptiFineVersions.add(version);
                }
            }
        }
        catch (Exception e) {
            this.logger.error("Failed to read supported OptiFine versions, adding defaults.", (Throwable)e);
            supportedOptiFineVersions.addAll(Arrays.asList("I7", "L5", "M5", "M6_pre1", "M6"));
        }
        finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private void updateOptiFineGenerations() {
        HttpURLConnection connection = null;
        try {
            URL optifineGenerations = new URL("https://static.sk1er.club/patcher/optifine_generations.json");
            connection = (HttpsURLConnection)optifineGenerations.openConnection();
            connection.setRequestProperty("User-Agent", "Patcher OptiFine Fetcher");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            try (InputStreamReader reader = new InputStreamReader(connection.getInputStream());){
                generations = (OptiFineGenerations)new Gson().fromJson((Reader)reader, OptiFineGenerations.class);
            }
        }
        catch (Exception e) {
            this.logger.error("Failed to read OptiFine generations list. Supplying default supported generations.", (Throwable)e);
            generations = new OptiFineGenerations();
            generations.getIGeneration().add("I7");
            generations.getLGeneration().add("L5");
            generations.getLGeneration().add("L6");
            generations.getMGeneration().add("M5");
            generations.getMGeneration().add("M6-pre1");
            generations.getMGeneration().add("M6");
        }
        finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    public static boolean isDevelopment() {
        Object o = Launch.blackboard.get("fml.deobfuscatedEnvironment");
        return o != null && (Boolean)o != false;
    }

    static {
        supportedOptiFineVersions = new HashSet<String>();
    }
}

