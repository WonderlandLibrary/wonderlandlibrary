package org.dreamcore.client.ui.newclickgui;

import java.util.Comparator;

public class SorterHelper implements Comparator<FeaturePanel> {

    @Override
    public int compare(FeaturePanel featurePanel, FeaturePanel featurePanel2) {
        if (featurePanel != null && featurePanel2 != null) {
            return featurePanel.feature.getLabel().compareTo(featurePanel2.feature.getLabel());
        }
        return 0;
    }
}