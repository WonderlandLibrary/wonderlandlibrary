//package dev.tenacity.util.misc;
//
//import dev.tenacity.module.Module;
//
//import java.util.Collections;
//import java.util.Comparator;
//import java.util.List;
//import java.util.stream.Collectors;
//
//public class StringUtils {
//
//    public static String findLongestModuleName(List<Module> modules) {
//        return Collections.max(modules, Comparator.comparing(module -> (module.getName() + (module.is() ? " " + module.getSuffix() : "")).length())).getName();
//    }
//
//    public static List<Module> getToggledModules(List<Module> modules) {
//        return modules.stream().filter(Module::isEnabled).collect(Collectors.toList());
//    }
//
//}
