package com.imaginato.homeworkmvvm.domain

import android.app.Application
import androidx.room.Room
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.imaginato.homeworkmvvm.data.local.demo.DemoDatabase
import com.imaginato.homeworkmvvm.data.local.demo.DemoDao
import com.imaginato.homeworkmvvm.data.local.login.AppDBDao
import com.imaginato.homeworkmvvm.data.local.login.AppDatabase
import com.imaginato.homeworkmvvm.data.remote.demo.DemoApi
import com.imaginato.homeworkmvvm.data.remote.demo.DemoDataRepository
import com.imaginato.homeworkmvvm.data.remote.demo.DemoRepository
import com.imaginato.homeworkmvvm.data.remote.login.ApiDataRepository
import com.imaginato.homeworkmvvm.data.remote.login.ApiRepository
import com.imaginato.homeworkmvvm.data.remote.login.AppApi
import com.imaginato.homeworkmvvm.ui.demo.MainActivityViewModel
import com.imaginato.homeworkmvvm.ui.login.LoginViewModel
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.component.KoinApiExtension
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

private const val BASE_URL = "http://private-222d3-homework5.apiary-mock.com/api/"

/***
 * Database Module
 */
val databaseModule = module {
    single { provideDatabase(androidApplication()) }
    single { provideDao(get()) }

    single { provideAppDatabase(androidApplication()) }
    single { provideAppDao(get()) }
}

/***
 * API call Basic Modules
 */
val netModules = module {
    single { provideInterceptors() }
    single { provideOkHttpClient(get()) }
    single { provideRetrofit(get()) }
    single { provideGson() }
}

/***
 * API call Modules
 */
val apiModules = module {
    single { provideDemoApi(get()) }

    single { provideAppApi(get()) }
}

/***
 * Repositories Modules
 */
val repositoryModules = module {
    single { provideDemoRepo(get()) }

    single { provideAppAPIRepo(get()) }
}

/**
 * View Model Modules
 */
@Suppress("OPT_IN_IS_NOT_ENABLED")
@OptIn(KoinApiExtension::class)
val viewModelModules = module {
    viewModel {
        MainActivityViewModel()
    }

    viewModel {
        LoginViewModel()
    }
}

//region For Demo
private fun provideDemoRepo(api: DemoApi): DemoRepository {
    return DemoDataRepository(api)
}

private fun provideDemoApi(retrofit: Retrofit): DemoApi = retrofit.create(DemoApi::class.java)

private fun provideDatabase(application: Application): DemoDatabase {
    return Room.databaseBuilder(application, DemoDatabase::class.java, "I-Database")
        .fallbackToDestructiveMigration()
        .build()
}

private fun provideDao(database: DemoDatabase): DemoDao {
    return database.demoDao
}
//endregion

//region For App APIs
private fun provideAppAPIRepo(api: AppApi): ApiRepository {
    return ApiDataRepository(api)
}

private fun provideAppApi(retrofit: Retrofit): AppApi = retrofit.create(AppApi::class.java)

private fun provideAppDatabase(application: Application): AppDatabase {
    return Room.databaseBuilder(application, AppDatabase::class.java, "I-Database")
        .fallbackToDestructiveMigration()
        .build()
}

private fun provideAppDao(database: AppDatabase): AppDBDao {
    return database.appDao
}
//endregion

//region Network Modules
private fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
    return Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}

private fun provideOkHttpClient(interceptors: ArrayList<Interceptor>): OkHttpClient {
    val clientBuilder = OkHttpClient.Builder()
    clientBuilder.readTimeout(2, TimeUnit.MINUTES)
    clientBuilder.connectTimeout(2, TimeUnit.MINUTES)
    interceptors.forEach { clientBuilder.addInterceptor(it) }
    return clientBuilder.build()
}

private fun provideInterceptors(): ArrayList<Interceptor> {
    val interceptors = arrayListOf<Interceptor>()
    val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }
    interceptors.add(loggingInterceptor)
    return interceptors
}

fun provideGson(): Gson {
    return GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.IDENTITY).create()
}
//endregion
