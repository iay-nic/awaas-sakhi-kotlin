package r.rural.awaassakhi.network


import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import r.rural.awaassakhi.network.crypto.CryptLib
import r.rural.awaassakhi.network.crypto.DecryptionImpl
import r.rural.awaassakhi.network.interceptor.DecryptionInterceptor
import r.rural.awaassakhi.utils.Constants.AWAASAPP_BASE_URL
import r.rural.awaassakhi.utils.Constants.BASE_URL

import java.util.concurrent.TimeUnit

object ApiClient {

    var retrofit: Retrofit? = null
    var UnEncryptedRetrofit: Retrofit? = null
    var awaasAppRetrofit: Retrofit? = null

    private var httpClientBuilder: OkHttpClient.Builder? = null
    private var cryptLib: CryptLib? = null

    fun getClient(): Retrofit? {
        if (retrofit == null) {

            cryptLib = CryptLib()

            httpClientBuilder =
                OkHttpClient.Builder().connectTimeout(5, TimeUnit.MINUTES)
                    .writeTimeout(5, TimeUnit.MINUTES)
                    .readTimeout(5, TimeUnit.MINUTES)
          //  httpClientBuilder!!.addInterceptor(EncryptionInterceptor(EncryptionImpl(cryptLib!!)))
            httpClientBuilder!!.addInterceptor(DecryptionInterceptor(DecryptionImpl(cryptLib!!)))
            initHttpLogging()

            val gson = GsonBuilder().setLenient().create()
            retrofit = Retrofit.Builder()
                .client(httpClientBuilder!!.build())
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
        }

        return retrofit

    }

    fun getUnEncryptedClient(): Retrofit? {
        if (UnEncryptedRetrofit == null) {

            cryptLib = CryptLib()

            httpClientBuilder =
                OkHttpClient.Builder().connectTimeout(5, TimeUnit.MINUTES)
                    .writeTimeout(5, TimeUnit.MINUTES)
                    .readTimeout(5, TimeUnit.MINUTES)
            //  httpClientBuilder!!.addInterceptor(EncryptionInterceptor(EncryptionImpl(cryptLib!!)))
            // httpClientBuilder!!.addInterceptor(DecryptionInterceptor(DecryptionImpl(cryptLib!!)))
            initHttpLogging()

            val gson = GsonBuilder().setLenient().create()
            UnEncryptedRetrofit = Retrofit.Builder()
                .client(httpClientBuilder!!.build())
                .baseUrl(BASE_URL)//it will be changed in repository while api calling
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
        }

        return UnEncryptedRetrofit

    }

    fun getAwaasAppClient(): Retrofit? {
        if (awaasAppRetrofit == null) {

            cryptLib = CryptLib()

            httpClientBuilder =
                OkHttpClient.Builder().connectTimeout(5, TimeUnit.MINUTES)
                    .writeTimeout(5, TimeUnit.MINUTES)
                    .readTimeout(5, TimeUnit.MINUTES)
            //  httpClientBuilder!!.addInterceptor(EncryptionInterceptor(EncryptionImpl(cryptLib!!)))
            httpClientBuilder!!.addInterceptor(DecryptionInterceptor(DecryptionImpl(cryptLib!!)))
            initHttpLogging()

            val gson = GsonBuilder().setLenient().create()
            awaasAppRetrofit = Retrofit.Builder()
                .client(httpClientBuilder!!.build())
                .baseUrl(AWAASAPP_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
        }

        return awaasAppRetrofit

    }


    private fun initHttpLogging() {
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)
      /*  if (BuildConfig.DEBUG) httpClientBuilder!!.addInterceptor(
            logging
        )*/
    }



}