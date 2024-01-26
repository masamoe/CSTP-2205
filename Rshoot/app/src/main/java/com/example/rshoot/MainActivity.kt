package com.example.rshoot

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Vector2

class RailShooterGame : ApplicationAdapter() {
    private lateinit var batch: SpriteBatch
    private lateinit var playerTexture: Texture
    private lateinit var bulletTexture: Texture
    private val bullets = mutableListOf<Vector2>()
    private val playerPosition = Vector2(100f, 100f)
    private val bulletSpeed = 5f

    override fun create() {
        batch = SpriteBatch()
        playerTexture = Texture("player.png")
        bulletTexture = Texture("bullet.png")
    }

    override fun render() {
        // Clear the screen
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        // Handle input
        if (Gdx.input.isTouched) {
            val touchX = Gdx.input.x.toFloat()
            val touchY = Gdx.graphics.height - Gdx.input.y.toFloat()
            playerPosition.x = touchX
            playerPosition.y = touchY
            shoot()
        }

        // Move and render bullets
        batch.begin()
        bullets.forEachIndexed { index, bullet ->
            bullet.y += bulletSpeed
            batch.draw(bulletTexture, bullet.x, bullet.y)
            if (bullet.y > Gdx.graphics.height) {
                bullets.removeAt(index)
            }
        }
        batch.draw(playerTexture, playerPosition.x, playerPosition.y)
        batch.end()
    }

    private fun shoot() {
        val bullet = Vector2(playerPosition.x + playerTexture.width / 2, playerPosition.y + playerTexture.height)
        bullets.add(bullet)
    }

    override fun dispose() {
        batch.dispose()
        playerTexture.dispose()
        bulletTexture.dispose()
    }
}

fun main() {
    val config = LwjglApplicationConfiguration()
    LwjglApplication(RailShooterGame(), config)
}
