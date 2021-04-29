package com.example.mycurrencyconverter.network

import com.example.mycurrencyconverter.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * @author: Vivek Singh
 * @property networkModule : This Koin module will provide the Retrofit Client to make network calls
 */
val networkModule = module {
    factory { HttpInterceptor() }
    factory { provideOkHttpClient(get()) }
    factory { provideRestApi(get()) }
    single { provideRetrofitClient(get()) }
}

fun provideRetrofitClient(okHttpClient: OkHttpClient): Retrofit {
    return Retrofit.Builder().baseUrl(BuildConfig.BASE_URL).client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create()).build()
}

fun provideOkHttpClient(authInterceptor: HttpInterceptor): OkHttpClient {
    return OkHttpClient().newBuilder().addInterceptor(authInterceptor).build()
}

fun provideRestApi(retrofit: Retrofit): NetworkApi = retrofit.create(NetworkApi::class.java)

class HttpInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val newRequest = chain.request().newBuilder().build()
        return chain.proceed(newRequest)
    }
}