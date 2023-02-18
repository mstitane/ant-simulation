package ch.epfl.moocprog;

import java.util.List;

import ch.epfl.moocprog.utils.Time;

public interface AnimalEnvironmentView {
    void selectSpecificBehaviorDispatch(AntWorker antWorker, Time dt);
    void selectSpecificBehaviorDispatch(AntSoldier antSoldier, Time dt);

    RotationProbability selectComputeRotationProbsDispatch(Ant ant);
    void selectAfterMoveDispatch(Ant ant, Time dt);

    void selectSpecificBehaviorDispatch(Termite termite, Time dt);
    void selectAfterMoveDispatch(Termite termite, Time dt);
    RotationProbability selectComputeRotationProbsDispatch(Termite ant);

    List<Animal> getVisibleEnemiesForAnimal(Animal from);
    boolean isVisibleFromEnemies(Animal from);
}
