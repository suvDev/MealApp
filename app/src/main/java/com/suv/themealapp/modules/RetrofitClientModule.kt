package com.suv.themealapp.modules

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import com.suv.themealapp.BuildConfig
import com.suv.themealapp.network.ApiInterface
import com.suv.themealapp.utils.Constant
import dagger.Module
import dagger.Provides
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class RetrofitClientModule(private val context: Application) {

    @Singleton
    @Provides
    fun provideApiInterface(retrofit: Retrofit): ApiInterface {
        return retrofit.create(ApiInterface::class.java)
    }

    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(Constant.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
    }

    @Singleton
    @Provides
    fun providesOkhttpClient(interceptors:  ArrayList<Interceptor>, cache: Cache): OkHttpClient{
        val okHttpClient = OkHttpClient.Builder()
            .followRedirects(false)
            .cache(cache)
        interceptors.forEach {
            okHttpClient.addInterceptor(it)
        }

        /* cache interceptor if network is not available or API fails then it will
        use saved responses if any from last 7 days */
        okHttpClient.addInterceptor { chain ->
            var request = chain.request()
            request = if (!hasNetwork())
                request.newBuilder().header("Cache-Control", "public, only-if-cached, max-stale=" + 60 * 60 * 24 * 7).build()
            else
                request

            // check api response
            val response  = chain.proceed(request)
            if(response.code!=200){
                request.newBuilder().header("Cache-Control", "public, only-if-cached, max-stale=" + 60 * 60 * 24 * 7).build()
                chain.proceed(request)
            } else{
                response
            }
        }

        return okHttpClient.build()
    }

    // 5mb of cache to reuse saved responses in case network or api failure
    @Singleton
    @Provides
    fun providesCache(): Cache{
        return Cache(context.cacheDir, (5 * 1024 * 1024).toLong())
    }

    @Singleton
    @Provides
    fun provideInterceptors(): ArrayList<Interceptor>{
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG)
                HttpLoggingInterceptor.Level.BODY
            else
                HttpLoggingInterceptor.Level.NONE
        }

        return arrayListOf(loggingInterceptor)
    }

    private fun hasNetwork(): Boolean {
        try {
            val connectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val nw = connectivityManager.activeNetwork ?: return false
                val actNw = connectivityManager.getNetworkCapabilities(nw) ?: return false
                return when {
                    actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                    actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                    else -> false
                }
            } else {
                val nwInfo = connectivityManager.activeNetworkInfo ?: return false
                return nwInfo.isConnected
            }
        }catch (e: Exception){
            e.printStackTrace()
            return false
        }
    }

}