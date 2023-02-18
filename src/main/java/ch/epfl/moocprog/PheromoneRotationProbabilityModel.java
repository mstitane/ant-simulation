package ch.epfl.moocprog;

import ch.epfl.moocprog.app.Context;
import ch.epfl.moocprog.config.Config;
import ch.epfl.moocprog.config.ConfigManager;

public class PheromoneRotationProbabilityModel implements AntRotationProbabilityModel {
    private static final ConfigManager config = Context.getConfig();

    @Override
    public RotationProbability computeRotationProbs(RotationProbability movementMatrix, ToricPosition position, double directionAngle, AntEnvironmentView env) {
        double[] arrI = movementMatrix.getAngles();
        double[] arrP = movementMatrix.getProbabilities();
        double[] arrQ = env.getPheromoneQuantitiesPerIntervalForAnt(position, directionAngle, movementMatrix.getAngles());
        double sum = 0;
        int alpha = config.getInt(Config.ALPHA);
        double[] arrP1 = new double[arrP.length];
        for (int i = 0; i < arrP.length; i++) {
            arrP1[i] = arrP[i] * Math.pow(detection(arrQ[i]), alpha);
            sum += arrP1[i];
        }
        if (sum > 0)
            for (int i = 0; i < arrP.length; i++) {
                arrP1[i] /= sum;
            }

        return new RotationProbability(arrI, arrP1);
    }

    private double detection(double x) {
        double betaD = config.getDouble(Config.BETA_D);
        double qZero = config.getDouble(Config.Q_ZERO);
        return 1.0 / (1.0 + Math.exp(-betaD * (x - qZero)));
    }
}
