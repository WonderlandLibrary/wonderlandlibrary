package koks.event;

import koks.api.event.Event;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * @author kroko
 * @created on 05.02.2021 : 04:56
 */

@AllArgsConstructor
public class AnimationEvent extends Event {

    @Getter
    float[] bipedHead, bipedBody, bipedLeftArm, bipedRightArm, bipedLeftLeg, bipedRightLeg;

    public void setHeadPosition(float x, float y, float z) {
        this.bipedHead[0] = x;
        this.bipedHead[1] = y;
        this.bipedHead[2] = z;
    }

    public void setHeadRotation(float x, float y) {
        this.bipedHead[3] = x;
        this.bipedHead[4] = y;
    }

    public void setBodyPosition(float x, float y, float z) {
        this.bipedBody[0] = x;
        this.bipedBody[1] = y;
        this.bipedBody[2] = z;
    }

    public void setBodyRotation(float x, float y, float z) {
        this.bipedBody[3] = x;
        this.bipedBody[4] = y;
        this.bipedBody[5] = z;
    }

    public void setLeftArmPosition(float x, float y, float z) {
        this.bipedLeftArm[0] = x;
        this.bipedLeftArm[1] = y;
        this.bipedLeftArm[2] = z;
    }

    public void setLeftArmRotation(float x, float y, float z) {
        this.bipedLeftArm[3] = x;
        this.bipedLeftArm[4] = y;
        this.bipedLeftArm[5] = z;
    }

    public void setRightArmPosition(float x, float y, float z) {
        this.bipedRightArm[0] = x;
        this.bipedRightArm[1] = y;
        this.bipedRightArm[2] = z;
    }

    public void setRightArmRotation(float x, float y, float z) {
        this.bipedRightArm[3] = x;
        this.bipedRightArm[4] = y;
        this.bipedRightArm[5] = z;
    }

    public void setLeftLegPosition(float x, float y, float z) {
        this.bipedLeftLeg[0] = x;
        this.bipedLeftLeg[1] = y;
        this.bipedLeftLeg[2] = z;
    }

    public void setLeftLegRotation(float x, float y, float z) {
        this.bipedLeftLeg[3] = x;
        this.bipedLeftLeg[4] = y;
        this.bipedLeftLeg[5] = z;
    }

    public void setRightLegPosition(float x, float y, float z) {
        this.bipedRightLeg[0] = x;
        this.bipedRightLeg[1] = y;
        this.bipedRightLeg[2] = z;
    }

    public void setRightLegRotation(float x, float y, float z) {
        this.bipedRightLeg[3] = x;
        this.bipedRightLeg[4] = y;
        this.bipedRightLeg[5] = z;
    }
}