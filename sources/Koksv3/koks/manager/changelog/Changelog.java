package koks.manager.changelog;

import java.util.ArrayList;
import java.util.List;

/**
 * @author kroko
 * @created on 01.11.2020 : 05:11
 */
public abstract class Changelog {

    private final String version;
    public final List<String> addedList = new ArrayList<>();
    public final List<String> fixedList = new ArrayList<>();
    public final List<String> removedList = new ArrayList<>();

    public Changelog() {
        ChangelogInfo changelogInfo = getClass().getAnnotation(ChangelogInfo.class);
        version = changelogInfo.version();
        changes();
    }

    public abstract void changes();

    public void added(String change) {
        addedList.add(change);
    }

    public void fixed(String change) {
        fixedList.add(change);
    }

    public void removed(String change) {
        removedList.add(change);
    }

    public String getVersion() {
        return version;
    }

    public List<String> getAdded() {
        return addedList;
    }

    public List<String> getFixed() {
        return fixedList;
    }

    public List<String> getRemoved() {
        return removedList;
    }
}
