package r.rural.awaassakhi.ui.auth.models

data class Block(
    val District_Code: String,
    val State_Code: String,
    val block_code: String,
    val block_name: String
) {
    override fun toString(): String {
        return block_name
    }
}