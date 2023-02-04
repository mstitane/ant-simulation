package ch.epfl.moocprog;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import ch.epfl.moocprog.utils.Time;
import ch.epfl.moocprog.utils.Utils;

public final class Environment implements FoodGeneratorEnvironmentView {
    private FoodGenerator foodGenerator;
    private List<Food> foods;

    public Environment() {
        this.foodGenerator = new FoodGenerator();
        this.foods = new LinkedList<>();
    }

    public void update(Time dt) {
        foodGenerator.update(this, dt);
        foods.removeIf(f -> f.getQuantity() <= 0);
    }

    @Override
    public void addFood(Food food) {
        Utils.requireNonNull(food);
        foods.add(food);
    }

    public List<Double> getFoodQuantities() {
        return foods.stream().map(Food::getQuantity).collect(Collectors.toList());
    }
}
