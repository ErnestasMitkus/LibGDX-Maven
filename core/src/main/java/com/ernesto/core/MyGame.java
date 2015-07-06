package com.ernesto.core;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class MyGame implements ApplicationListener {
    private OrthographicCamera camera;
	private SpriteBatch batch;

    private Texture dropImage;
    private Texture bucketImage;
    private Sound dropSound;
    private Music rainMusic;

    private Sprite bucket;
    private List<Sprite> raindrops = new LinkedList<Sprite>();

    private Vector3 touchPos = new Vector3();
    private long lastDropTime;

	@Override
	public void create () {
		batch = new SpriteBatch();
        camera = new OrthographicCamera(800, 640);
        camera.translate(400, 320, 0);

        dropImage = new Texture(Gdx.files.internal("droplet.png"));
        bucketImage = new Texture(Gdx.files.internal("bucket.png"));

        bucket = new Sprite(bucketImage);
        bucket.setPosition(800 / 2 - bucket.getWidth() / 2, 20);

        dropSound = Gdx.audio.newSound(Gdx.files.internal("drop.wav"));
        rainMusic = Gdx.audio.newMusic(Gdx.files.internal("rain.mp3"));

        rainMusic.setLooping(true);
        rainMusic.play();

        spawnRaindrop();
	}

	@Override
	public void resize (int width, int height) {
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0, 0, 0.2f, 0.1f);
		Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);

        updateBucket();
        updateRaindrops();
        camera.update();

        batch.setProjectionMatrix(camera.combined);
		batch.begin();
        for (Sprite raindrop : raindrops) {
            raindrop.draw(batch);
        }
        bucket.draw(batch);
        batch.end();
	}

	@Override
	public void pause () {
        rainMusic.pause();
	}

	@Override
	public void resume () {
        rainMusic.play();
	}

	@Override
	public void dispose () {
        dropImage.dispose();
        bucketImage.dispose();
        dropSound.dispose();
        rainMusic.dispose();
        batch.dispose();
	}

    private void updateBucket() {
        if (Gdx.input.isTouched()) {
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchPos);
            bucket.setX(touchPos.x - bucket.getWidth() / 2);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            bucket.translateX(-200 * Gdx.graphics.getDeltaTime());
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            bucket.translateX(200 * Gdx.graphics.getDeltaTime());
        }

        if (bucket.getX() < 0) {
            bucket.setX(0);
        } else if (bucket.getX() > 800 - bucket.getWidth()) {
            bucket.setX(800 - bucket.getWidth());
        }
    }

    private void updateRaindrops() {
        if (TimeUtils.nanoTime() - lastDropTime > 1000000000) {
            spawnRaindrop();
        }
        Iterator<Sprite> each = raindrops.iterator();
        while (each.hasNext()) {
            Sprite raindrop = each.next();
            raindrop.translateY(-200 * Gdx.graphics.getDeltaTime());
            if (raindrop.getY() + raindrop.getHeight() < 0) {
                each.remove();
            }
            if (raindrop.getBoundingRectangle().overlaps(bucket.getBoundingRectangle())) {
                dropSound.play();
                each.remove();
            }
        }
    }

    private void spawnRaindrop() {
        Sprite sprite = new Sprite(dropImage);
        sprite.setPosition(MathUtils.random(800 - sprite.getWidth()), 640);
        raindrops.add(sprite);
        lastDropTime = TimeUtils.nanoTime();
    }

}
