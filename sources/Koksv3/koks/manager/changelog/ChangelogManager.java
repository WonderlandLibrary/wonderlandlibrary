package koks.manager.changelog;

import koks.manager.changelog.impl.V1_0_0;
import koks.manager.changelog.impl.V2_1_2;
import koks.manager.changelog.impl.V2_1_3;
import koks.manager.changelog.impl.V3_0_0;

import java.util.ArrayList;
import java.util.Comparator;

/**
 * @author kroko
 * @created on 01.11.2020 : 05:10
 */
public class ChangelogManager {

    public final ArrayList<Changelog> changelogs = new ArrayList<>();

    public ChangelogManager() {
        addChangelog(new V3_0_0());
        addChangelog(new V2_1_3());
        addChangelog(new V2_1_2());
        addChangelog(new V1_0_0());

        changelogs.sort(Comparator.comparing(Changelog::getVersion).reversed());
        for(Changelog changelog : changelogs) {
            changelog.addedList.sort(Comparator.comparing(String::toString));
            changelog.fixedList.sort(Comparator.comparing(String::toString));
            changelog.removedList.sort(Comparator.comparing(String::toString));
        }
    }

    public void addChangelog(Changelog changelog) {
        this.changelogs.add(changelog);
    }

}
