package com.easyflow.practiceapp.network.network_impl.providers

import com.easyflow.practiceapp.network.network_impl.api.CurrencyApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

class NetworkProvider @Inject constructor() {
    private val provider: Retrofit

    init {
        provider = createProvider()
    }

    private fun createProvider(): Retrofit {
        val logger = HttpLoggingInterceptor()
        logger.level = HttpLoggingInterceptor.Level.BODY
        val client = OkHttpClient.Builder()
            .addInterceptor(logger)
//            .addInterceptor(ApiKeyInterceptor(API_KEY))
            .build()

        return Retrofit.Builder()
            .client(client)
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun createCurrencyApi(): CurrencyApi = provider.create(CurrencyApi::class.java)

    companion object {
//        private const val API_KEY = "flds"
        private const val BASE_URL = "https://www.cbr-xml-daily.ru/"
    }
}