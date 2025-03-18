package grean

import net.pearx.kasechange.toKebabCase

enum class Pattern {
    BOW,
    SCAN,
    CIRCLE,
    RANDOM,
    SCREEN,
    SUBTLE;

    companion object {
        fun names(): Array<String> {
            return entries.map { it.name.toKebabCase() }.toTypedArray()
        }

        fun from(value: String): Pattern? {
            return entries.find { it.name.toKebabCase() == value }
        }
    }
}
