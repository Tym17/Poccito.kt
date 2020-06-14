package com.tym17.poccito.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.tym17.poccito.poccito
import ktx.app.KtxScreen

class MainMenuScreen(val game: poccito) : KtxScreen {
    private val camera = OrthographicCamera().apply { setToOrtho(false, 800f, 480f) }

    override fun render(delta: Float) {
        camera.update()
        game.batch.projectionMatrix = camera.combined

        game.batch.begin()
        game.font.draw(game.batch, "Welcome to the Drop", 100f, 150f)
        game.font.draw(game.batch, "Tap anywhere to begin", 100f, 100f)
        game.batch.end()

        if (Gdx.input.isTouched()) {
            game.addScreen(GameScreen(game))
            game.setScreen<GameScreen>()
            game.removeScreen<MainMenuScreen>()
            dispose();
        }
    }
}