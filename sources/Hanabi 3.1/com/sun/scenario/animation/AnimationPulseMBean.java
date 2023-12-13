package com.sun.scenario.animation;

public interface AnimationPulseMBean {
   boolean getEnabled();

   void setEnabled(boolean var1);

   long getPULSE_DURATION();

   long getSkippedPulses();

   long getSkippedPulsesIn1Sec();

   long getStartMax();

   long getStartMaxIn1Sec();

   long getStartAv();

   long getStartAvIn100Millis();

   long getEndMax();

   long getEndMaxIn1Sec();

   long getEndAv();

   long getEndAvIn100Millis();

   long getAnimationDurationMax();

   long getAnimationMaxIn1Sec();

   long getAnimationDurationAv();

   long getAnimationDurationAvIn100Millis();

   long getPaintingDurationMax();

   long getPaintingDurationMaxIn1Sec();

   long getPaintingDurationAv();

   long getPaintingDurationAvIn100Millis();

   long getScenePaintingDurationMaxIn1Sec();

   long getPaintingPreparationDurationMaxIn1Sec();

   long getPaintingFinalizationDurationMaxIn1Sec();

   long getPulseDurationMax();

   long getPulseDurationMaxIn1Sec();

   long getPulseDurationAv();

   long getPulseDurationAvIn100Millis();
}
