package grean

import net.pearx.kasechange.toTitleCase

object Resources {
    fun name(): String {
        return Resources::class.java.`package`.implementationTitle ?: ""
    }

    fun title(): String {
        return name().toTitleCase()
    }

    fun version(): String {
        return Resources::class.java.`package`.implementationVersion ?: ""
    }
}
