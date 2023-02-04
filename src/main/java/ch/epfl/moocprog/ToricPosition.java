package ch.epfl.moocprog;

import java.util.ArrayList;
import java.util.List;

import ch.epfl.moocprog.utils.Vec2d;

import static ch.epfl.moocprog.app.Context.getConfig;
import static ch.epfl.moocprog.config.Config.WORLD_HEIGHT;
import static ch.epfl.moocprog.config.Config.WORLD_WIDTH;

public final class ToricPosition {

    private final Vec2d position;

    public ToricPosition(double x, double y) {
        this.position = clampedPosition(x, y);
    }

    public ToricPosition(Vec2d position) {
        this.position = clampedPosition(position.getX(), position.getY());
    }

    public ToricPosition() {
        this.position = new Vec2d(0, 0);
    }

    public static Vec2d clampedPosition(double x, double y) {
        final int width = getConfig().getInt(WORLD_WIDTH);
        final int height = getConfig().getInt(WORLD_HEIGHT);

        while (x < 0) {
            x = x + width;

        }
        while (x >= width) {
            x = x - width;

        }

        while (y < 0) {
            y = y + height;

        }
        while (y >= height) {
            y = y - height;

        }
        return new Vec2d(x, y);
    }

    public ToricPosition add(ToricPosition that) {
        Vec2d add = this.position.add(that.position);
        return new ToricPosition(add);
    }

    public ToricPosition add(Vec2d vec) {
        Vec2d add = this.position.add(vec);
        return new ToricPosition(add);
    }

    public Vec2d toVec2d() {
        return position;
    }

    public Vec2d toricVector(ToricPosition that) {
        final int worldWidth = getConfig().getInt(WORLD_WIDTH);
        final int worldHeight = getConfig().getInt(WORLD_HEIGHT);

        List<Vec2d> vecTables = new ArrayList<>();
        vecTables.add(that.toVec2d());
        vecTables.add(that.toVec2d().add(new Vec2d(0, worldHeight)));
        vecTables.add(that.toVec2d().add(new Vec2d(0, -worldHeight)));
        vecTables.add(that.toVec2d().add(new Vec2d(worldHeight, 0)));
        vecTables.add(that.toVec2d().add(new Vec2d(-worldHeight, 0)));
        vecTables.add(that.toVec2d().add(new Vec2d(worldWidth, worldHeight)));

        vecTables.add(that.toVec2d().add(new Vec2d(worldWidth, -worldHeight)));
        vecTables.add(that.toVec2d().add(new Vec2d(-worldWidth, worldHeight)));
        vecTables.add(that.toVec2d().add(new Vec2d(-worldWidth, -worldHeight)));

        Vec2d minVec = vecTables.get(0);
        for (int i = 1; i < vecTables.size(); i++) {
            Vec2d obj = vecTables.get(i);
            if (this.toVec2d().distance(obj) < this.toVec2d().distance(minVec)) {
                minVec = obj;
            }
        }
        return minVec.minus(this.toVec2d());
    }

    public double toricDistance(ToricPosition that) {
        return toricVector(that).length();
    }

    @Override
    public String toString() {
        return position.toString();
    }
}
