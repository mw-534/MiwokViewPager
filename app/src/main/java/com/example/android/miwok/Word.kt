package com.example.android.miwok

/** We use -1 because negative resource IDs are not allowed. */
private const val NO_IMAGE_PROVIDED: Int = -1

/**
 * [Word] represents a vocabulary word that the user wants to learn.
 * It contains a default translation and a Miwok translation for that word.
 * @param defaultTranslation is the word in a language that the user is already familiar with (such as English).
 * @param miwokTranslation is the word in the Miwok language.
 * @param audioResourceId is the resource ID for the audio file associated with this word.
 */
class Word(
        /**
         * Default translation for the word.
         */
        val defaultTranslation: String,

        /**
         * Miwok translation for the word.
         */
        val miwokTranslation: String,

        /**
         * Resource ID of the audio file associated with this word.
         */
        val audioResourceId: Int) {

        /**
         * Create a new [Word] object.
         *
         * @param defaultTranslation is the word in a language that the user is already familiar with (such as English).
         * @param miwokTranslation is the word in the Miwok language.
         * @param audioResourceId is the resource ID for the audio file associated with this word.
         * @param imageResourceId is the resource ID of the image associated with this word.
         */
        constructor(defaultTranslation: String, miwokTranslation: String, audioResourceId: Int, imageResourceId: Int) : this(defaultTranslation, miwokTranslation, audioResourceId) {
                this.imageResourceId = imageResourceId
        }

        /**
         * Resource ID of the image.
         */
        var imageResourceId: Int = NO_IMAGE_PROVIDED
                private set

        /**
         * Returns whether the word has an image associated with it.
         */
        fun hasImage() = imageResourceId != NO_IMAGE_PROVIDED

        /**
         * Returns the string representation of the [Word] object.
         */
        override fun toString(): String {
                return "Word(defaultTranslation='$defaultTranslation', miwokTranslation='$miwokTranslation', audioResourceId=$audioResourceId, imageResourceId=$imageResourceId)"
        }


}

