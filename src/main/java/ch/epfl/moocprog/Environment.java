package ch.epfl.moocprog;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import ch.epfl.moocprog.app.Context;
import ch.epfl.moocprog.config.Config;
import ch.epfl.moocprog.gfx.EnvironmentRenderer;
import ch.epfl.moocprog.utils.Time;
import ch.epfl.moocprog.utils.Utils;

public final class Environment implements FoodGeneratorEnvironmentView, AnimalEnvironmentView {
    private FoodGenerator foodGenerator;
    private List<Food> foods;
    private List<Animal> animals;

    public Environment() {
        this.foodGenerator = new FoodGenerator();
        this.foods = new LinkedList<>();
        this.animals = new LinkedList<>();
    }

    public void update(Time dt) {
        foodGenerator.update(this, dt);
        foods.removeIf(f -> f.getQuantity() <= 0);

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
    public List<ToricPosition> getAnimalsPosition(){
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
    }

    public void addAnthill(Anthill anthill) {

    }

    public void addAnimal(Animal animal) {
        Utils.requireNonNull(animal);
        animals.add(animal);
    }

    public int getWidth() {
        return Context.getConfig().getInt(Config.WORLD_WIDTH);
    }

    public int getHeight() {
        return Context.getConfig().getInt(Config.WORLD_HEIGHT);
    }
}
