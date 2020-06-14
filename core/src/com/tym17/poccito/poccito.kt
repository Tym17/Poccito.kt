package com.tym17.poccito

import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.tym17.poccito.screens.MainMenuScreen
import ktx.app.KtxGame
import ktx.app.KtxScreen

class poccito : KtxGame<KtxScreen>() {
    val batch  by lazy { SpriteBatch() }
    val font by lazy { BitmapFont() }

    override fun create() {
        addScreen(MainMenuScreen(this))
        setScreen<MainMenuScreen>()
        super.create()
    }

    override fun dispose() {
        batch.dispose()
        font.dispose()
        super.dispose()
    }
}