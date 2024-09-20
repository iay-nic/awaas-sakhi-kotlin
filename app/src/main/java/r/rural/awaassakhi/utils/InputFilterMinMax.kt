package r.rural.awaassakhi.utils

import android.text.InputFilter
import android.text.Spanned

class InputFilterMinMax (private val min: Int, private val max: Int) : InputFilter {
    override fun filter(
        source: CharSequence,
        start: Int,
        end: Int,
        dest: Spanned,
        dstart: Int,
        dend: Int
    ): CharSequence? {
        try {
            // Concatenate the existing text with the new input
            val inputString = dest.toString().substring(0, dstart) + source.toString() + dest.toString().substring(dend)

           // Remove leading zeros
            val input = inputString.toIntOrNull() ?: return ""

           // Check if the number is within the specified range
            if (input in min..max) {
                return null // Accept the input
            }
        } catch (nfe: NumberFormatException) {
            // Ignore the input if it's not a valid number
        }
        return "" // Reject the input
    }
}

