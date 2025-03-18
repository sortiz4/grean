package grean

import com.github.ajalt.clikt.core.main
import kotlin.system.exitProcess

class Main {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            Command().main(args)
            exitProcess(0)
        }
    }
}
