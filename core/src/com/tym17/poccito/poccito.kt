package com.tym17.poccito

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.tym17.poccito.screens.MainMenuScreen

class poccito : com.badlogic.gdx.Game() {
    lateinit internal var batch: SpriteBatch;
    lateinit internal var font: BitmapFont;

    override fun create() {
        batch = SpriteBatch();
        font = BitmapFont();
        this.setScreen(MainMenuScreen(this));
    }

    override fun render() {
        super.render();
    }

    override fun dispose() {
        this.getScreen().dispose();

        batch.dispose();
        font.dispose();
    }
}