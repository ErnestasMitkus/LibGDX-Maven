package com.ernesto.core;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Pool;
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

    private BitmapFont font;
    private int dropCount;

    private Sprite bucket;
    private Pool<Raindrop> raindropPool = new Pool<Raindrop>(3) {
        @Override
        protected Raindrop newObject() {
            return new Raindrop(dropImage);
        }
    };
    private List<Raindrop> raindrops = new LinkedList<Raindrop>();

    private Vector3 touchPos = new Vector3();
    private long lastDropTime;

    private float elapsed;

	@Override
	public void create () {
		batch = new SpriteBatch();
        camera = new OrthographicCamera(800, 640);
        camera.translate(400, 320, 0);

        font = new BitmapFont();
        font.scale(1.2f);

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

        elapsed += Gdx.graphics.getDeltaTime();
        updateBucket();
        updateRaindrops();
        camera.update();

        batch.setProjectionMatrix(camera.combined);
		batch.begin();
        for (Sprite raindrop : raindrops) {
            raindrop.draw(batch);
        }
        bucket.draw(batch);
        font.draw(batch, "Score: " + dropCount, 10, 640 - 10);
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
        if (TimeUtils.nanoTime() - lastDropTime > 1000000000 / getDifficultyCoef()) {
            spawnRaindrop();
        }
        Iterator<Raindrop> each = raindrops.iterator();
        while (each.hasNext()) {
            Raindrop raindrop = each.next();
            raindrop.update(Gdx.graphics.getDeltaTime());
            raindrop.translateY(-raindrop.getSpeed() * Gdx.graphics.getDeltaTime() * getDifficultyCoef());
            if (raindrop.getY() + raindrop.getHeight() < 0) {
                each.remove();
                raindropPool.free(raindrop);
            }
            if (raindrop.getBoundingRectangle().overlaps(bucket.getBoundingRectangle())) {
                dropSound.play();
                dropCount++;
                each.remove();
                raindropPool.free(raindrop);
            }
        }
    }

    private float getDifficultyCoef() {
        return 1 + elapsed / 10;
    }

    private void spawnRaindrop() {
        Raindrop raindrop = raindropPool.obtain();
        raindrop.setPosition(MathUtils.random(800 - raindrop.getWidth()), 640);
        raindrops.add(raindrop);
        lastDropTime = TimeUtils.nanoTime();
    }

}
