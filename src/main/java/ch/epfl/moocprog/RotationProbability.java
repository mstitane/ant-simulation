package ch.epfl.moocprog;

import ch.epfl.moocprog.utils.Utils;

public class RotationProbability {

    private final double[] angles;
    private final double[] probabilities;

    public RotationProbability(double[] arr1, double[] arr2) {
        Utils.requireNonNull(arr1);
        Utils.requireNonNull(arr2);
        Utils.require(arr1.length == arr2.length);
        this.angles = arr1.clone();
        this.probabilities = arr2.clone();
    }

    public double[] getAngles() {
        return angles.clone();
    }

    public double[] getProbabilities() {
        return probabilities.clone();
    }
}
