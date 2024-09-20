package r.rural.awaassakhi.network.repository


import com.google.gson.Gson
import com.google.gson.GsonBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import r.rural.awaassakhi.network.BaseDataSource
import r.rural.awaassakhi.network.GenericRespModel
import r.rural.awaassakhi.network.Resource
import r.rural.awaassakhi.network.crypto.CryptLib
import r.rural.awaassakhi.network.interfaces.ApiInterface
import r.rural.awaassakhi.utils.ConnectivityChecker

import javax.inject.Inject

class NetworkRepository @Inject constructor(
    private val headers: MutableMap<String, String>,
    private val apiInterface: ApiInterface,
    private val cryptLib: CryptLib,
    connectivityChecker: ConnectivityChecker
) : BaseDataSource(connectivityChecker) {

  /*  suspend fun getEncAesKey(
    ): Flow<Resource<TokenResp>> {
        return flow {
            emit(safeApiCall {
                apiInterface.getEncAesKey(
                    "".toRequestBody("text/plain".toMediaType())
                )
            })
        }.flowOn(Dispatchers.IO)
    }



    suspend fun getHouseTypologyList(
        stateCode: String
    ): Flow<Resource<GenericRespModel<List<HouseTypologyEntity>?>>> {
        return flow {
            emit(safeApiCall {
                apiInterface
                    .getHouseTypologyList(
                        headers,
                        stateCode
                    )
            })
        }.flowOn(Dispatchers.IO)
    }
*/
}