package r.rural.awaassakhi.network

data class GenericRespModel<T>(
    val message: String,
    val result: T,
    val status: Boolean
)