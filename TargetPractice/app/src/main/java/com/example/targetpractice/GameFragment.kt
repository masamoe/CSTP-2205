package com.example.targetpractice

import android.animation.ObjectAnimator
import android.content.res.ColorStateList
import android.os.Bundle
import android.os.CountDownTimer
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment

class GameFragment : Fragment() {
    private lateinit var targetButton: Button
    private val bombs = mutableListOf<Button>()
    private lateinit var restartButton: Button
    private lateinit var timeView: TextView
    private lateinit var scoreView: TextView
    private lateinit var constraintLayout: View

    private var score: Int = 0
    private var gameOver: Boolean = false
    private var countdownTimer: CountDownTimer? = null
    private var delayBeforeGameStart = 3000L
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_game, container, false)

        targetButton = view.findViewById(R.id.targetButton)
        restartButton = view.findViewById(R.id.restartButton)
        timeView = view.findViewById(R.id.timeView)
        scoreView = view.findViewById(R.id.scoreView)
        constraintLayout = view.findViewById(R.id.constraintLayout)

        targetButton.setOnClickListener { onTargetClick() }
        restartButton.setOnClickListener { restartGame() }
        restartButton.text = "Start Game"
        targetButton.visibility = View.GONE

        return view
    }

    private fun startGame() {
        // Reset game state
        score = 0
        gameOver = false
        restartButton.visibility = View.GONE
        countdownTimer?.cancel()

        targetButton.postDelayed({
            targetButton.visibility = View.VISIBLE
            countdownTimer = object : CountDownTimer(60000, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    timeView.text = "Time: " + millisUntilFinished / 1000
                    moveTargetsAndBombs()
                }

                override fun onFinish() {
                    gameOver = true
                    targetButton.visibility = View.GONE
                    restartButton.text = "Restart Game"
                    restartButton.visibility = View.VISIBLE
                    bombs.forEach { bomb ->
                        (view as? ViewGroup)?.removeView(bomb)
                    }
                    bombs.clear()
                    Toast.makeText(requireContext(), "Game Over! Score: $score", Toast.LENGTH_LONG).show()
                }
            }
                (countdownTimer as CountDownTimer).start()
        }, delayBeforeGameStart)
    }

    private fun moveTargetsAndBombs() {
        if (!gameOver) {
            moveButton(targetButton)
            bombs.forEach { bomb ->
                moveButton(bomb)
                bomb.setOnClickListener { onBombClick(bomb) }
            }
        }
    }
    private fun getRandomPosition(view: View): Pair<Float, Float> {
        val maxX = constraintLayout.width - targetButton.width
        val maxY = constraintLayout.height - targetButton.height

        val randomX = (0..maxX).random().toFloat()
        val randomY = (0..maxY).random().toFloat()
        return Pair(randomX, randomY)
    }
    private fun moveButton(button: Button) {
        val randomPosition = getRandomPosition(button)

        val animatorX = ObjectAnimator.ofFloat(button, "x", button.x, randomPosition.first)
        val animatorY = ObjectAnimator.ofFloat(button, "y", button.y, randomPosition.second)

        animatorX.duration = 1000 // Adjust the duration as needed
        animatorY.duration = 1000 // Adjust the duration as needed

        animatorX.start()
        animatorY.start()
    }

    private fun spawnButton(button: Button) {
        val randomPosition = getRandomPosition(button)

        button.x = randomPosition.first
        button.y = randomPosition.second
    }

    private fun onTargetClick() {
        score += 10
        scoreView.text = "Score: " + score
        val bomb = createBombButton()
        bombs.add(bomb)
        (view as? ViewGroup)?.addView(bomb)
    }

    private fun onBombClick(bomb: Button) {
        gameOver = true
        countdownTimer?.cancel()
        targetButton.visibility = View.GONE
        restartButton.text = "Restart Game"
        restartButton.visibility = View.VISIBLE
        bombs.forEach { bomb ->
            (view as? ViewGroup)?.removeView(bomb)
        }
        bombs.clear()
        Toast.makeText(requireContext(), "Game Over! Score: $score", Toast.LENGTH_LONG).show()
    }

    private fun createBombButton(): Button {
        val bombButton = Button(requireContext())
        bombButton.layoutParams = targetButton.layoutParams
        bombButton.background = ContextCompat.getDrawable(requireContext(), R.drawable.bomb)
        bombButton.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.red)
        // Set any other properties for the bomb button as needed
        return bombButton
    }

    private fun restartGame() {
        startGame()
        countdownTimer?.cancel()
    }
}