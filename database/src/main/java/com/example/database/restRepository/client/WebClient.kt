package com.example.database.restRepository.client

import com.example.database.BuildConfig
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class WebClient {

    companion object {
        const val baseUrl = BuildConfig.BASE_URL
    }

    private val interceptor: Interceptor = object : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            var request: Request = chain.request()
            val url: HttpUrl =
                request.url.newBuilder()
                    .addQueryParameter(name = "access_key", value = BuildConfig.ACCESS_KEY)
                    .build()
            request = request.newBuilder().url(url).build()
            return chain.proceed(request)
        }
    }

    private fun getOKHTTPClient(): OkHttpClient {
        val client = OkHttpClient.Builder()

        if (BuildConfig.DEBUG) {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            client.addInterceptor(interceptor).build()
        }

        client.addInterceptor(interceptor)
        return client.build()
    }

    fun getClient(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(getOKHTTPClient())
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
    }

}