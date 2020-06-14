package com.tym17.poccito.screens;

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.utils.Array
import com.badlogic.gdx.utils.TimeUtils
import com.tym17.poccito.poccito
import ktx.app.KtxScreen

class GameScreen(val game: poccito) : KtxScreen {
    private val dropImage = Texture(Gdx.files.internal("images/drop.png"))
    private val bucketImage = Texture(Gdx.files.internal("images/bucket.png"))
    // The camera ensures we can render using our target resolution of 800x480
    //    pixels no matter what the screen resolution is.
    private val camera = OrthographicCamera().apply { setToOrtho(false, 800f, 480f) }
    private val bucket = Rectangle(800f /2f - 64f / 2f, 20f, 64f, 64f)
    private val touchPos = Vector3()
    private val raindrops = Array<Rectangle>() // gdx, not Kotlin Array
    private var lastDropTime: Long = 0L
    private var dropsGathered: Int = 0

    private fun spawnRaindrop() {
        raindrops.add(Rectangle(MathUtils.random(0f, 800f - 64f), 480f, 64f, 64f))
        lastDropTime = TimeUtils.nanoTime()
    }

    override fun render(delta: Float) {
        // generally good practice to update the camera's matrices once per frame
        camera.update()

        // tell the SpriteBatch to render in the coordinate system specified by the camera.
        game.batch.projectionMatrix = camera.combined

        // begin a new batch and draw the bucket and all drops
        game.batch.begin()
        game.font.draw(game.batch, "Drops Collected: $dropsGathered", 0f, 480f)
        game.batch.draw(bucketImage, bucket.x, bucket.y,
                bucket.width, bucket.height)
        for (raindrop in raindrops) {
            game.batch.draw(dropImage, raindrop.x, raindrop.y)
        }
        game.batch.end()

        // process user input
        if (Gdx.input.isTouched()) {
            touchPos.set(Gdx.input.getX().toFloat(),
                    Gdx.input.getY().toFloat(),
                    0f)
            camera.unproject(touchPos)
            bucket.x = touchPos.x - 64f / 2f
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            // getDeltaTime returns the time passed between the last and the current frame in seconds
            bucket.x -= 200 * delta
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            bucket.x += 200 * delta
        }

        // make sure the bucket stays within the screen bounds
        bucket.x = MathUtils.clamp(bucket.x, 0f, 800f - 64f)

        // check if we need to create a new raindrop
        if (TimeUtils.nanoTime() - lastDropTime > 1_000_000_000L)
            spawnRaindrop()

        // move the raindrops, remove any that are beneath the bottom edge of the
        //    screen or that hit the bucket.  In the latter case, play back a sound
        //    effect also
        val iter = raindrops.iterator()
        while (iter.hasNext()) {
            val raindrop = iter.next()
            raindrop.y -= 200 * delta
            if (raindrop.y + 64 < 0)
                iter.remove()

            if (raindrop.overlaps(bucket)) {
                dropsGathered++
                iter.remove()
            }
        }
    }

    override fun show() {
        spawnRaindrop()
    }

    override fun dispose() {
        dropImage.dispose()
        bucketImage.dispose()
    }
}