package grean

import com.github.ajalt.clikt.core.CliktCommand as Clikt
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.versionOption
import com.github.ajalt.clikt.parameters.types.choice
import java.awt.Dimension
import java.awt.MouseInfo
import java.awt.Robot
import java.awt.Toolkit
import java.security.SecureRandom
import java.util.concurrent.TimeUnit
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.PI
import kotlin.math.roundToInt
import kotlin.math.sin

class Command : Clikt(name = Resources.name()) {
    private val pattern: String by option(*PATTERN_NAMES, help = PATTERN_HELP).choice(*PATTERN_CHOICES).default("")
    private val screen: Dimension by lazy { Toolkit.getDefaultToolkit().screenSize }
    private val robot: Robot by lazy { Robot() }

    init {
        versionOption(Resources.version(), names = VERSION_NAMES.toSet())
    }

    override fun run() {
        when (Pattern.from(pattern)) {
            Pattern.BOW -> bow()
            Pattern.SCAN -> scan()
            Pattern.CIRCLE -> circle()
            Pattern.RANDOM -> random()
            Pattern.SCREEN -> screen()
            Pattern.SUBTLE -> subtle()
            else -> subtle()
        }
    }

    private fun bow(rate: Int = 5) {
        val m = screen.height.toDouble() / screen.width.toDouble()

        fun goDownAndRight() {
            for (x in 0..screen.width step rate) {
                throttle {
                    robot.mouseMove(x, (m * x).roundToInt())
                }
            }
        }

        fun goUpRightSide() {
            for (y in screen.height downTo 0 step rate) {
                throttle {
                    robot.mouseMove(screen.width, y)
                }
            }
        }

        fun goDownAndLeft() {
            for (x in screen.width downTo 0 step rate) {
                throttle {
                    robot.mouseMove(x, (-m * x + screen.height).roundToInt())
                }
            }
        }

        fun goUpLeftSide() {
            for (y in screen.height downTo 0 step rate) {
                throttle {
                    robot.mouseMove(0, y)
                }
            }
        }

        while (true) {
            for (i in 0..3) {
                when (i) {
                    0 -> goDownAndRight()
                    1 -> goUpRightSide()
                    2 -> goDownAndLeft()
                    3 -> goUpLeftSide()
                }
            }
        }
    }

    private fun scan(rate: Int = 5) {
        while (true) {
            for (y in 0..screen.height step 25) {
                when (y % 2 == 0) {
                    true -> for (x in 0..screen.width step rate) {
                        throttle {
                            robot.mouseMove(x, y)
                        }
                    }
                    false -> for (x in screen.width downTo 0 step rate) {
                        throttle {
                            robot.mouseMove(x, y)
                        }
                    }
                }
            }
        }
    }

    private fun circle(rate: Int = 1) {
        val x = screen.width.toDouble() / 2
        val y = screen.height.toDouble() / 2
        val d = min(screen.width.toDouble(), screen.height.toDouble())
        val r = d / 2

        fun degreesToRadians(a: Double): Double {
            return a * (PI / 180)
        }

        while (true) {
            for (a in 0..<360 step rate) {
                throttle {
                    robot.mouseMove((x + r * cos(degreesToRadians(a.toDouble()))).roundToInt(), (y + r * sin(degreesToRadians(a.toDouble()))).roundToInt())
                }
            }
        }
    }

    private fun random(rate: Int = 500) {
        val random = SecureRandom()

        while (true) {
            throttle(rate) {
                robot.mouseMove(random.nextInt(screen.width + 1), random.nextInt(screen.height + 1))
            }
        }
    }

    private fun screen(rate: Int = 5) {
        fun goRight() {
            for (x in 0..screen.width step rate) {
                throttle {
                    robot.mouseMove(x, 0)
                }
            }
        }

        fun goDown() {
            for (y in 0..screen.height step rate) {
                throttle {
                    robot.mouseMove(screen.width, y)
                }
            }
        }

        fun goLeft() {
            for (x in screen.width downTo 0 step rate) {
                throttle {
                    robot.mouseMove(x, screen.height)
                }
            }
        }

        fun goUp() {
            for (y in screen.height downTo 0 step rate) {
                throttle {
                    robot.mouseMove(0, y)
                }
            }
        }

        while (true) {
            for (i in 0..3) {
                when (i) {
                    0 -> goRight()
                    1 -> goDown()
                    2 -> goLeft()
                    3 -> goUp()
                }
            }
        }
    }

    private fun subtle(rate: Int = 1000) {
        val random = SecureRandom()

        fun getNextPosition(current: Int): Int {
            return current + random.nextInt(-1, 2)
        }

        while (true) {
            throttle(rate) {
                val location = MouseInfo.getPointerInfo().location
                robot.mouseMove(getNextPosition(location.x), getNextPosition(location.y))
            }
        }
    }

    private fun throttle(time: Int = 5, action: () -> Unit) {
        action()

        // Sleep for a moment so the app will be easier to stop
        TimeUnit.MILLISECONDS.sleep(time.toLong())
    }

    companion object {
        // Option names
        private val PATTERN_NAMES = arrayOf("--pattern", "-p")
        private val VERSION_NAMES = arrayOf("--version", "-v")

        // Help messages
        private const val PATTERN_HELP = "The pattern to move the mouse in."

        // Extra options
        private val PATTERN_CHOICES = Pattern.names()
    }
}
