package net.smoothboot.client.util;

import static net.smoothboot.client.util.EntityUtil.mc;

public final class RotationFaker {
        private boolean fakeRotation;
        private float serverYaw;
        private float serverPitch;
        private float realYaw;
        private float realPitch;

        public float getServerYaw()
        {
                return fakeRotation ? serverYaw : mc.player.getYaw();
        }

        public float getServerPitch()
        {
                return fakeRotation ? serverPitch : mc.player.getPitch();
        }


}
