package r.rural.awaassakhi.hilt

import android.app.Application
import android.content.Context
import android.os.Build
import android.text.TextUtils

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import r.rural.awaassakhi.BuildConfig
import r.rural.awaassakhi.network.ApiClient.getAwaasAppClient
import r.rural.awaassakhi.network.ApiClient.getClient
import r.rural.awaassakhi.network.ApiClient.getUnEncryptedClient

import r.rural.awaassakhi.network.crypto.CryptLib
import r.rural.awaassakhi.network.interfaces.*
import r.rural.awaassakhi.network.repository.NetworkRepository
import r.rural.awaassakhi.utils.AppPreferencesManager
import r.rural.awaassakhi.utils.ConnectivityChecker
import r.rural.awaassakhi.utils.Constants.ACCESS_TOKEN
import r.rural.awaassakhi.utils.Constants.UNENCRYPTED
import r.rural.awaassakhi.utils.Utility
import javax.inject.Named
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModules {


    @Singleton
    @Provides
    fun provideRetrofitInterface(): ApiInterface {
        return getClient()!!.create(ApiInterface::class.java)
    }


    @Singleton
    @Provides
    @Named("AWAAS_APP")
    fun provideAwaasAppRetrofitInterface(): ApiInterface {
        return getAwaasAppClient()!!.create(ApiInterface::class.java)
    }


    // Api interface for UNENCRYPTED API
    @Singleton
    @Provides
    @Named(UNENCRYPTED)
    fun provideUnencryptedRetrofitInterface(): ApiInterface {
        return getUnEncryptedClient()!!.create(ApiInterface::class.java)
    }


    @Singleton
    @Provides
    fun provideNetworkRepo(
        headers: MutableMap<String, String>,
        apiInterface: ApiInterface,
        cryptLib: CryptLib,
        connectivityChecker: ConnectivityChecker
    ): NetworkRepository {
        return NetworkRepository(headers, apiInterface, cryptLib, connectivityChecker)
    }


    // Api repository for UNENCRYPTED API
    @Singleton
    @Provides
    @Named(UNENCRYPTED)
    fun provideUnencryptedNetworkRepo(
        headers: MutableMap<String, String>,
        @Named(UNENCRYPTED) apiInterface: ApiInterface,
        cryptLib: CryptLib,
        connectivityChecker: ConnectivityChecker
    ): NetworkRepository {
        return NetworkRepository(headers, apiInterface, cryptLib, connectivityChecker)
    }

    @Singleton
    @Provides
    @Named("AWAAS_APP")
    fun provideAwaasAppNetworkRepo(
        headers: MutableMap<String, String>,
        @Named("AWAAS_APP") apiInterface: ApiInterface,
        cryptLib: CryptLib,
        connectivityChecker: ConnectivityChecker
    ): NetworkRepository {
        return NetworkRepository(headers, apiInterface, cryptLib, connectivityChecker)
    }

   // @Singleton
    @Provides
    fun provideCryptLib(): CryptLib {
        return CryptLib()
    }

    @Singleton
    @Provides
    fun provideApiHeaders(@ApplicationContext appContext: Context): MutableMap<String, String> {
        val headerMap: MutableMap<String, String> = HashMap()

        // headerMap["Content-Type"] = "application/x-www-form-urlencoded"
        headerMap["deviceType"] = "android"
        headerMap["version"] = BuildConfig.VERSION_NAME
        headerMap["authorizationKey"] =
            "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890"
        headerMap["deviceId"] = Utility.getDeviceId(appContext)
        headerMap["ipAddress"] = Utility.getIPAddress(appContext)

        headerMap["deviceModel"] =
            "${Build.MANUFACTURER} ${Build.MODEL} ${Build.DEVICE} ${Build.PRODUCT}"
        headerMap["androidVersion"] = Build.VERSION.SDK_INT.toString()

        if (!TextUtils.isEmpty(AppPreferencesManager.getString(ACCESS_TOKEN))) {
            headerMap["Authorization"] = AppPreferencesManager.getString(ACCESS_TOKEN)!!
        }
        return headerMap

    }

}