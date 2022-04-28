package grean

import net.pearx.kasechange.toKebabCase

enum class Pattern() {
    BOW,
    SCAN,
    CIRCLE,
    RANDOM,
    SCREEN;

    companion object {
        fun names(): Array<String> {
            return values().map { it.name.toKebabCase() }.toTypedArray()
        }

        fun from(value: String): Pattern? {
            return values().find { it.name.toKebabCase() == value }
        }
    }
}