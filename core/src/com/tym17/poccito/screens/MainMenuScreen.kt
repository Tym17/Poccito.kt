package com.tym17.poccito.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.tym17.poccito.poccito

class MainMenuScreen(val game: poccito) : Screen {
    private var camera: OrthographicCamera;

    init {
        camera = OrthographicCamera();
        camera.setToOrtho(false, 800f, 480f);
    }

    override fun render(delta: Float) {
        Gdx.gl.glClearColor(0f, 0f, 0.2f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();
        game.font.draw(game.batch, "Welcome to the Drop", 100f, 150f);
        game.font.draw(game.batch, "Tap anywhere to begin", 100f, 100f);
        game.batch.end();

        if (Gdx.input.isTouched()) {
            game.setScreen(GameScreen(game));
            dispose();
        }
    }

    override fun hide() {
    }

    override fun show() {
    }

    override fun pause() {
    }

    override fun resume() {
    }

    override fun resize(width: Int, height: Int) {
    }

    override fun dispose() {
    }
}