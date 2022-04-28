package grean

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
