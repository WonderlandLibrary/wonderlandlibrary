package com.initial.configs;

import com.initial.*;
import com.initial.modules.*;
import com.initial.settings.impl.*;
import com.initial.settings.*;
import java.util.*;
import java.io.*;

public class Configs
{
    private File dir;
    private File dataFile;
    
    public void save(final String name) {
        this.dir = new File(String.valueOf(Astomero.instance.dir));
        if (!this.dir.exists()) {
            this.dir.mkdir();
        }
        this.dataFile = new File(this.dir, name + ".file");
        if (!this.dataFile.exists()) {
            try {
                this.dataFile.createNewFile();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        final ArrayList<String> toSave = new ArrayList<String>();
        for (final Module m : Astomero.instance.moduleManager.getModules()) {
            toSave.add("Module:" + m.getName() + ":" + m.isToggled() + ":" + m.getKey() + ":" + '\u0001');
            for (final Setting s : m.settings) {
                if (s instanceof DoubleSet) {
                    final DoubleSet set = (DoubleSet)s;
                    toSave.add("NumberSet:" + m.getName() + ":" + set.name + ":" + set.getValue());
                }
                else if (s instanceof BooleanSet) {
                    final BooleanSet set2 = (BooleanSet)s;
                    toSave.add("BooleanSet:" + m.getName() + ":" + set2.name + ":" + set2.enabled);
                }
                else if (s instanceof ModeSet) {
                    final ModeSet set3 = (ModeSet)s;
                    toSave.add("ModeSet:" + m.getName() + ":" + set3.name + ":" + set3.getMode());
                }
                else if (s instanceof StringSet) {
                    final StringSet set4 = (StringSet)s;
                    toSave.add("StringSet:" + m.getName() + ":" + set4.name + ":" + set4.getText());
                }
                else {
                    if (!(s instanceof ModuleCategory)) {
                        continue;
                    }
                    final ModuleCategory category = (ModuleCategory)s;
                    for (final Setting set5 : category.settingsOnCat) {
                        if (set5 instanceof DoubleSet) {
                            final DoubleSet dset = (DoubleSet)set5;
                            toSave.add("CategoryNumberSet:" + m.getName() + ":" + category.name + ":" + set5.name + ":" + dset.getValue());
                        }
                        else if (set5 instanceof BooleanSet) {
                            final BooleanSet bset = (BooleanSet)set5;
                            toSave.add("CategoryBooleanSet:" + m.getName() + ":" + category.name + ":" + set5.name + ":" + bset.enabled);
                        }
                        else if (set5 instanceof ModeSet) {
                            final ModeSet mset = (ModeSet)set5;
                            toSave.add("CategoryModeSet:" + m.getName() + ":" + category.name + ":" + set5.name + ":" + mset.getMode());
                        }
                        else {
                            if (!(set5 instanceof StringSet)) {
                                continue;
                            }
                            final StringSet mset2 = (StringSet)set5;
                            toSave.add("CategoryStringSet:" + m.getName() + ":" + category.name + ":" + set5.name + ":" + mset2.getText());
                        }
                    }
                }
            }
        }
        try {
            final PrintWriter pw = new PrintWriter(this.dataFile);
            for (final String str : toSave) {
                pw.println(str);
            }
            pw.close();
        }
        catch (FileNotFoundException e2) {
            e2.printStackTrace();
        }
    }
    
    public void delete(final String name) {
        this.dir = new File(String.valueOf(Astomero.instance.dir));
        if (!this.dir.exists()) {
            this.dir.mkdir();
        }
        this.dataFile = new File(this.dir, name + ".file");
        try {
            this.dataFile.delete();
        }
        catch (Exception ex) {}
    }
    
    public void load(final String name) {
        this.dir = new File(String.valueOf(Astomero.instance.dir));
        if (!this.dir.exists()) {
            this.dir.mkdir();
        }
        this.dataFile = new File(this.dir, name + ".file");
        final ArrayList<String> lines = new ArrayList<String>();
        try {
            final BufferedReader reader = new BufferedReader(new FileReader(this.dataFile));
            for (String line = reader.readLine(); line != null; line = reader.readLine()) {
                lines.add(line);
            }
            reader.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        try {
            for (final String s : lines) {
                final String[] args = s.split(":");
                if (s.toLowerCase().startsWith("module:")) {
                    for (final Module m : Astomero.instance.moduleManager.getModules()) {
                        if (m.getName().equalsIgnoreCase(args[1])) {
                            final boolean shouldEnable = Boolean.parseBoolean(args[2]);
                            if (shouldEnable && !m.isToggled()) {
                                m.setToggled(true);
                            }
                            if (args.length > 4) {}
                        }
                    }
                }
                if (s.toLowerCase().startsWith("numberset:")) {
                    for (final Module m : Astomero.instance.moduleManager.getModules()) {
                        if (m.getName().equalsIgnoreCase(args[1])) {
                            for (final Setting setting : m.settings) {
                                if (!(setting instanceof DoubleSet)) {
                                    continue;
                                }
                                if (!setting.name.equalsIgnoreCase(args[2])) {
                                    continue;
                                }
                                final DoubleSet setting2 = (DoubleSet)setting;
                                setting2.setValue(Double.parseDouble(args[3]));
                            }
                        }
                    }
                }
                if (s.toLowerCase().startsWith("booleanset:")) {
                    for (final Module m : Astomero.instance.moduleManager.getModules()) {
                        if (m.getName().equalsIgnoreCase(args[1])) {
                            for (final Setting setting : m.settings) {
                                if (!(setting instanceof BooleanSet)) {
                                    continue;
                                }
                                if (!setting.name.equalsIgnoreCase(args[2])) {
                                    continue;
                                }
                                final BooleanSet setting3 = (BooleanSet)setting;
                                setting3.setEnabled(Boolean.parseBoolean(args[3]));
                            }
                        }
                    }
                }
                if (s.toLowerCase().startsWith("modeset:")) {
                    for (final Module m : Astomero.instance.moduleManager.getModules()) {
                        if (m.getName().equalsIgnoreCase(args[1])) {
                            for (final Setting setting : m.settings) {
                                if (!(setting instanceof ModeSet)) {
                                    continue;
                                }
                                for (final String str : ((ModeSet)setting).modes) {
                                    if (setting.name.equalsIgnoreCase(args[2]) && args[3].equalsIgnoreCase(str)) {
                                        final ModeSet setting4 = (ModeSet)setting;
                                        setting4.setSelected(args[3]);
                                    }
                                }
                            }
                        }
                    }
                }
                if (s.toLowerCase().startsWith("stringset:")) {
                    for (final Module m : Astomero.instance.moduleManager.getModules()) {
                        if (m.getName().equalsIgnoreCase(args[1])) {
                            for (final Setting setting : m.settings) {
                                if (!(setting instanceof StringSet)) {
                                    continue;
                                }
                                if (!setting.name.equalsIgnoreCase(args[2])) {
                                    continue;
                                }
                                final StringSet setting5 = (StringSet)setting;
                                setting5.setText(args[3]);
                            }
                        }
                    }
                }
                if (s.toLowerCase().startsWith("categorynumberset:")) {
                    for (final Module m : Astomero.instance.moduleManager.getModules()) {
                        if (m.getName().equalsIgnoreCase(args[1])) {
                            for (final Setting setting : m.settings) {
                                if (!(setting instanceof ModuleCategory)) {
                                    continue;
                                }
                                if (!setting.name.equalsIgnoreCase(args[2])) {
                                    continue;
                                }
                                for (final Setting setting6 : ((ModuleCategory)setting).settingsOnCat) {
                                    if (setting6.name.equalsIgnoreCase(args[3])) {
                                        final DoubleSet setting7 = (DoubleSet)setting6;
                                        setting7.setValue(Double.parseDouble(args[4]));
                                    }
                                }
                            }
                        }
                    }
                }
                if (s.toLowerCase().startsWith("categorybooleanset:")) {
                    for (final Module m : Astomero.instance.moduleManager.getModules()) {
                        if (m.getName().equalsIgnoreCase(args[1])) {
                            for (final Setting setting : m.settings) {
                                if (!(setting instanceof ModuleCategory)) {
                                    continue;
                                }
                                if (!setting.name.equalsIgnoreCase(args[2])) {
                                    continue;
                                }
                                try {
                                    for (final Setting setting6 : ((ModuleCategory)setting).settingsOnCat) {
                                        try {
                                            if (!setting6.name.equalsIgnoreCase(args[3])) {
                                                continue;
                                            }
                                            final BooleanSet setting8 = (BooleanSet)setting6;
                                            setting8.setEnabled(Boolean.parseBoolean(args[4]));
                                        }
                                        catch (Exception e2) {
                                            System.out.println("A: " + setting6.name + " B: " + setting.name + " C: " + m.getName());
                                        }
                                    }
                                }
                                catch (Exception e3) {
                                    System.out.println(setting.name);
                                }
                            }
                        }
                    }
                }
                if (s.toLowerCase().startsWith("categorymodeset:")) {
                    for (final Module m : Astomero.instance.moduleManager.getModules()) {
                        if (m.getName().equalsIgnoreCase(args[1])) {
                            for (final Setting setting : m.settings) {
                                if (!(setting instanceof ModuleCategory)) {
                                    continue;
                                }
                                if (!setting.name.equalsIgnoreCase(args[2])) {
                                    continue;
                                }
                                for (final Setting setting6 : ((ModuleCategory)setting).settingsOnCat) {
                                    if (!(setting6 instanceof ModeSet)) {
                                        continue;
                                    }
                                    for (final String str2 : ((ModeSet)setting6).modes) {
                                        if (setting6.name.equalsIgnoreCase(args[3]) && args[4].equalsIgnoreCase(str2)) {
                                            final ModeSet setting9 = (ModeSet)setting6;
                                            setting9.setSelected(args[4]);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                if (s.toLowerCase().startsWith("categorystringset:")) {
                    for (final Module m : Astomero.instance.moduleManager.getModules()) {
                        if (m.getName().equalsIgnoreCase(args[1])) {
                            for (final Setting setting : m.settings) {
                                if (!(setting instanceof ModuleCategory)) {
                                    continue;
                                }
                                if (!setting.name.equalsIgnoreCase(args[2])) {
                                    continue;
                                }
                                for (final Setting setting6 : ((ModuleCategory)setting).settingsOnCat) {
                                    if (setting6.name.equalsIgnoreCase(args[3])) {
                                        final StringSet setting10 = (StringSet)setting6;
                                        setting10.setText(args[4]);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
