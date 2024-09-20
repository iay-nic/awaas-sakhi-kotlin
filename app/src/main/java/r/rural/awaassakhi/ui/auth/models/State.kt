package r.rural.awaassakhi.ui.auth.models

data class State(
    val abbreviation: String,
    val id: String,
    val name: String
) {
    override fun toString(): String {
        return name
    }
}