package com.ernesto.core;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.Pool;

public class Raindrop extends Sprite implements Pool.Poolable {

    private static final int SPEED_ACC = 100;

    private float speed = 200;

    public Raindrop(Texture texture) {
        super(texture);
    }

    public void update(float delta) {
        speed += SPEED_ACC * delta;
    }

    public float getSpeed() {
        return speed;
    }

    @Override
    public void reset() {
        speed = 200;
    }
}
