package com.ernesto.core;

import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;

public class MyGame implements ApplicationListener {

	@Override
	public void create () {
	}

	@Override
	public void resize (int width, int height) {
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);
	}

	@Override
	public void pause () {
	}

	@Override
	public void resume () {
	}

	@Override
	public void dispose () {
	}
}
