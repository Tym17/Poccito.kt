package com.tym17.poccito.screens;

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.utils.Array
import com.tym17.poccito.Entity
import com.tym17.poccito.ProtocolHandler
import com.tym17.poccito.poccito
import ktx.app.KtxScreen
import ktx.graphics.use
import ktx.log.logger

private val log = logger<GameScreen>()

class GameScreen(val game: poccito) : KtxScreen {
    private val playerImage = Texture(Gdx.files.internal("images/player.png"))
    private val npcImage = Texture(Gdx.files.internal("images/npc.png"))
    // The camera ensures we can render using our target resolution of 800x480
    //    pixels no matter what the screen resolution is.
    private val camera = OrthographicCamera().apply { setToOrtho(false, 800f, 480f) }
    private val players = Array<Entity>()
    private val npcs = Array<Entity>()
    private var protocol = ProtocolHandler("localhost", 20117, "kotlin")

    override fun render(delta: Float) {
        // generally good practice to update the camera's matrices once per frame
        camera.update()

        // tell the SpriteBatch to render in the coordinate system specified by the camera.
        game.batch.projectionMatrix = camera.combined

        // begin a new batch and draw the bucket and all drops
        game.batch.use { batch ->
            players.forEach { player ->
                game.font.setColor(255f, 255f, 255f, 255f)
                game.font.draw(game.batch, player.name, player.x.toFloat() - 20, player.y.toFloat() + 35)
                batch.draw(playerImage, player.x.toFloat(), player.y.toFloat())
            }
            npcs.forEach { npc ->
                game.font.setColor(0f, 255f, 0f, 255f)
                game.font.draw(game.batch, npc.name, npc.x.toFloat() - 20, npc.y.toFloat() + 35)
                batch.draw(npcImage, npc.x.toFloat(), npc.y.toFloat())
            }
        }

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            protocol.move(protocol.P_LEFT)
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            protocol.move(protocol.P_RIGHT)
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            protocol.move(protocol.P_UP)
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            protocol.move(protocol.P_DOWN)
        }
        protocol.receive()
        while (protocol.haveCmds()) {
            val cmd = protocol.extract().split(' ')

            when (cmd[0]) {
                "NEW" -> players.add(Entity(cmd[1].toInt(), cmd[2].toInt(), cmd[3].toInt(), cmd[4]))
                "NPC" -> npcs.add(Entity(cmd[1].toInt(), cmd[2].toInt(), cmd[3].toInt(), cmd[4]))
                "PPOS" -> {
                    players.forEach { player ->
                        if (player.id == cmd[1].toInt()) {
                            player.x = cmd[2].toInt()
                            player.y = cmd[3].toInt()
                        }
                    }
                }
                "NPOS" -> {
                    npcs.forEach { npc ->
                        if (npc.id == cmd[1].toInt()) {
                            npc.x = cmd[2].toInt()
                            npc.y = cmd[3].toInt()
                        }
                    }
                }
            }
        }

    }

    override fun show() {
    }

    override fun dispose() {
        log.debug { "Disposing ${this.javaClass.simpleName}" }
        playerImage.dispose()
        npcImage.dispose()
        protocol.disconnect()
    }
}