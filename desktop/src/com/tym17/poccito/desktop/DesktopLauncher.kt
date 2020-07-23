package com.tym17.poccito.desktop

import com.badlogic.gdx.Application
import com.badlogic.gdx.backends.lwjgl.LwjglApplication
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration
import com.tym17.poccito.poccito

object DesktopLauncher {
    @JvmStatic
    fun main(arg: Array<String>) {
        val config = LwjglApplicationConfiguration()
        config.height = 480
        config.width = 800
        LwjglApplication(poccito(), config).logLevel = Application.LOG_DEBUG

    }
}