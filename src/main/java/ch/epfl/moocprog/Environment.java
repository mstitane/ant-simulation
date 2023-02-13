package ch.epfl.moocprog;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import ch.epfl.moocprog.config.Config;
import ch.epfl.moocprog.gfx.EnvironmentRenderer;
import ch.epfl.moocprog.utils.Time;
import ch.epfl.moocprog.utils.Utils;

import static ch.epfl.moocprog.app.Context.getConfig;
import static ch.epfl.moocprog.config.Config.ANT_MAX_PERCEPTION_DISTANCE;

public final class Environment implements FoodGeneratorEnvironmentView, AnimalEnvironmentView, AnthillEnvironmentView, AntWorkerEnvironmentView {
    private FoodGenerator foodGenerator;
    private List<Food> foods;
    private List<Animal> animals;
    private List<Anthill> anthills;

    public Environment() {
        this.foodGenerator = new FoodGenerator();
        this.foods = new LinkedList<>();
        this.animals = new LinkedList<>();
        this.anthills = new LinkedList<>();
    }

    public void update(Time dt) {
        foodGenerator.update(this, dt);
        foods.removeIf(f -> f.getQuantity() <= 0);
        anthills.forEach(anthill -> anthill.update(this, dt));
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
        Food food = getClosestFoodForAnt(antWorker);
        return food != null;
    }

    @Override
    public void selectSpecificBehaviorDispatch(AntWorker antWorker, Time dt) {
        antWorker.seekForFood(this, dt);
    }

    @Override
    public void selectSpecificBehaviorDispatch(AntSoldier antSoldier, Time dt) {
        antSoldier.seekForEnemies(this, dt);
    }
}
