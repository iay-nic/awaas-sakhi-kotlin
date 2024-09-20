package r.rural.awaassakhi.network

import r.rural.awaassakhi.utils.ConnectivityChecker
import retrofit2.Response

abstract class BaseDataSource(private val connectivityChecker: ConnectivityChecker) {

    suspend fun <T> safeApiCall(apiCall: suspend () -> Response<T>): Resource<T> {

        if (!connectivityChecker.isNetworkConnected()) {
            return Resource.error("No Internet Connection. Please Connect to Internet.", null)
        }

        try {
            val response = apiCall()
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    return Resource.success(body)
                }
            }

            return if (response.code() == 401) Resource.error(
                "Unauthorized",
                null
            ) else error("${response.code()} ${response.message()}")
        } catch (e: Exception) {
            return error(e.message ?: e.toString())
        }
    }

    private fun <T> error(message: String): Resource<T> {
        //Log.e("remoteDataSource", message)
        return Resource.error("Network call has failed for a following reason: $message", null)
    }

    fun <T> loading(): Resource<T> {
        //Log.e("remoteDataSource", message)
        return Resource.loading(null)
    }
}