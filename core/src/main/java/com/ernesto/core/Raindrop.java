package com.ernesto.core;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.Pool;

public class Raindrop extends Sprite implements Pool.Poolable {

    public Raindrop(Texture texture) {
        super(texture);
    }

    @Override
    public void reset() {
    }
}
