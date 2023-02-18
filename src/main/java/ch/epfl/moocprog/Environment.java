package ch.epfl.moocprog;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.stream.Collectors;

import ch.epfl.moocprog.config.Config;
import ch.epfl.moocprog.gfx.EnvironmentRenderer;
import ch.epfl.moocprog.utils.Time;
import ch.epfl.moocprog.utils.Utils;

import static ch.epfl.moocprog.app.Context.getConfig;
import static ch.epfl.moocprog.config.Config.ANT_MAX_PERCEPTION_DISTANCE;
import static ch.epfl.moocprog.config.Config.ANT_SMELL_MAX_DISTANCE;

public final class Environment implements FoodGeneratorEnvironmentView, AnimalEnvironmentView, AnthillEnvironmentView, AntWorkerEnvironmentView {
    private FoodGenerator foodGenerator;
    private List<Food> foods;
    private List<Animal> animals;
    private List<Anthill> anthills;
    private List<Pheromone> pheromones;

    public Environment() {
        this.foodGenerator = new FoodGenerator();
        this.foods = new LinkedList<>();
        this.animals = new LinkedList<>();
        this.anthills = new LinkedList<>();
        this.pheromones = new LinkedList<>();
    }

    public void update(Time dt) {
        foodGenerator.update(this, dt);
        handlePheromone(dt);
        handleAnimals(dt);
        anthills.forEach(anthill -> anthill.update(this, dt));
        foods.removeIf(f -> f.getQuantity() <= 0);
    }

    public List<ToricPosition> getAnimalsPosition() {
        return animals.stream().map(Animal::getPosition).collect(Collectors.toList());
    }

    @Override
    public void addFood(Food food) {
        Utils.requireNonNull(food);
        foods.add(food);
    }

    public List<Double> getFoodQuantities() {
        return foods.stream().map(Food::getQuantity).collect(Collectors.toList());
    }

    public void renderEntities(EnvironmentRenderer environmentRenderer) {
        foods.forEach(environmentRenderer::renderFood);
        animals.forEach(environmentRenderer::renderAnimal);
        anthills.forEach(environmentRenderer::renderAnthill);
    }

    public void addAnthill(Anthill anthill) {
        Utils.requireNonNull(anthill);
        anthills.add(anthill);
    }

    public void addAnimal(Animal animal) {
        Utils.requireNonNull(animal);
        animals.add(animal);
    }

    public int getWidth() {
        return getConfig().getInt(Config.WORLD_WIDTH);
    }

    public int getHeight() {
        return getConfig().getInt(Config.WORLD_HEIGHT);
    }

    @Override
    public void addAnt(Ant ant) {
        addAnimal(ant);
    }

    @Override
    public Food getClosestFoodForAnt(AntWorker antWorker) {
        Food food = Utils.closestFromPoint(antWorker, foods);
        double perceptionDistance = getConfig().getDouble(ANT_MAX_PERCEPTION_DISTANCE);
        return food != null && food.getPosition().toricDistance(antWorker.getPosition()) < perceptionDistance ? food : null;
    }

    @Override
    public boolean dropFood(AntWorker antWorker) {
        Anthill anthill = Utils.closestFromPoint(antWorker, anthills);
        double perceptionDistance = getConfig().getDouble(ANT_MAX_PERCEPTION_DISTANCE);
        if (anthill != null && anthill.getPosition().toricDistance(antWorker.getPosition()) < perceptionDistance) {
            anthill.dropFood(antWorker.getFoodQuantity());
            return true;
        }
        return false;
    }

    @Override
    public void selectSpecificBehaviorDispatch(AntWorker antWorker, Time dt) {
        antWorker.seekForFood(this, dt);
    }

    @Override
    public void selectSpecificBehaviorDispatch(AntSoldier antSoldier, Time dt) {
        antSoldier.seekForEnemies(this, dt);
    }

    @Override
    public void addPheromone(Pheromone pheromone) {
        Utils.requireNonNull(pheromone);
        pheromones.add(pheromone);
    }

    public List<Double> getPheromonesQuantities() {
        return pheromones.stream().map(Pheromone::getQuantity).collect(Collectors.toList());
    }

    private void handlePheromone(Time dt) {
        Iterator<Pheromone> pheromoneIterator = pheromones.iterator();
        while (pheromoneIterator.hasNext()) {
            Pheromone pheromone = pheromoneIterator.next();
            if (pheromone.isNegligible())
                pheromoneIterator.remove();
            else {
                pheromone.update(dt);
            }
        }
    }

    private void handleAnimals(Time dt) {
        Iterator<Animal> animalIterator = animals.iterator();
        while (animalIterator.hasNext()) {
            Animal animal = animalIterator.next();
            if (animal.isDead())
                animalIterator.remove();
            else {
                animal.update(this, dt);
            }
        }
    }

    @Override
    public double[] getPheromoneQuantitiesPerIntervalForAnt(ToricPosition position, double directionAngleRad, double[] angles) {
        Utils.requireNonNull(position);
        Utils.requireNonNull(angles);
        final double smellMaxDistance = getConfig().getDouble(ANT_SMELL_MAX_DISTANCE);
        double[] result = new double[angles.length];
        Arrays.fill(result, 0);
        pheromones.forEach(f -> {
            SortedMap<Double, Integer> mins = new TreeMap<>();
            if (!f.isNegligible() && position.toricDistance(f.getPosition()) < smellMaxDistance) {
                double beta = position.toricVector(f.getPosition()).angle() - directionAngleRad;
                for (int i = 0, anglesLength = angles.length; i < anglesLength; i++) {
                    double angle = angles[i];
                    mins.putIfAbsent(closestAngleFrom(angle, beta), i);
                }
                result[mins.get(mins.firstKey())] += f.getQuantity();
            }
        });
        return result;
    }

    private static double normalizedAngle(double angle) {
        if (angle < 0)
            return angle + 2 * Math.PI;
        else if (angle > 2 * Math.PI)
            return angle - 2 * Math.PI;
        else
            return angle;
    }

    private static double closestAngleFrom(double angle, double target) {
        double diff = angle - target;
        diff = normalizedAngle(diff);
        return Math.min(diff, 2 * Math.PI - diff);
    }

    @Override
    public RotationProbability selectComputeRotationProbsDispatch(Ant ant) {
        return ant.computeRotationProbs(this);
    }

    @Override
    public void selectAfterMoveDispatch(Ant ant, Time dt) {
        ant.afterMoveAnt(this, dt);
    }
}
