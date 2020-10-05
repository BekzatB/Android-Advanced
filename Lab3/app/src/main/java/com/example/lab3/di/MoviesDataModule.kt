package com.example.lab3.di

import android.util.Log
import com.example.lab3.API.ApiService
import com.example.lab3.repository.MoviesRepository
import com.example.lab3.repository.MoviesRepositoryI
import com.example.lab3.repository.RemoteDataSource
import com.example.lab3.repository.RemoteDataSourceI
import com.google.gson.GsonBuilder
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

const val IO_THREAD = "IO_THREAD"
const val MAIN_THREAD = "MAIN_THREAD"

val moviesDataModule = module {
    factory { generatePostsService(get()) }

    factory { getHttpClient(get()) }

    single { getLoggingInterceptor() }

    factory(named(IO_THREAD)) { Schedulers.io() }
    factory(named(MAIN_THREAD)) { AndroidSchedulers.mainThread() }

    factory { CompositeDisposable() }

    factory<MoviesRepositoryI> {
        MoviesRepository(
            get(),
            get(named(IO_THREAD)),
            get(named(MAIN_THREAD))
        )
    }

    factory<RemoteDataSourceI> {
        RemoteDataSource(
            get()
        )
    }
}

private fun generatePostsService(client: OkHttpClient): ApiService {
    Log.i("MSG", "generatePostsService")
    val retrofit = Retrofit.Builder()
        .baseUrl("https://api.themoviedb.org/3/")
        .client(client)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
        .build()
    return retrofit.create(ApiService::class.java)
}

private fun getHttpClient(interceptor: HttpLoggingInterceptor): OkHttpClient {
    return OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .addInterceptor(interceptor)
        .addInterceptor { chain ->
            val newUrl = chain.request().url
                .newBuilder()
                .addQueryParameter(
                    "api_key",
                    "88f972ac2b5f07d969202c8ffbaaaffa"
                )
                .build()
            val newRequest = chain.request()
                .newBuilder()
                .url(newUrl)
                .build()
            chain.proceed(newRequest)
        }
        .build()
}

private fun getLoggingInterceptor(): HttpLoggingInterceptor {
    return HttpLoggingInterceptor(logger = object : HttpLoggingInterceptor.Logger {
        override fun log(message: String) {
            Log.d("OkHttp", message)
        }
    }).apply {
        level = HttpLoggingInterceptor.Level.BODY
    }
}