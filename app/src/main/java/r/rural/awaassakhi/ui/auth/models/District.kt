package r.rural.awaassakhi.ui.auth.models

data class District(
    val District_Code: String,
    val District_Name: String,
    val State_Code: String
){
    override fun toString(): String {
        return District_Name
    }
}