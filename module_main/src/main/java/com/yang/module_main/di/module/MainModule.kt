package com.yang.module_main.di.module

import android.app.Application
import com.yang.lib_common.base.di.factory.BaseViewModelFactory
import com.yang.lib_common.scope.ActivityScope
import com.yang.module_main.api.MainApi

import com.yang.module_main.di.factory.MainViewModelFactory
import com.yang.module_main.repository.MainRepository
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

/**
 * @ClassName: MainModel
 * @Description:
 * @Author: yxy
 * @Date: 2022/7/21 14:31
 */
@Module
class MainModule {

    @ActivityScope
    @Provides
    fun provideMainApi(retrofit: Retrofit): MainApi {
        return retrofit.create(MainApi::class.java)
    }

    @ActivityScope
    @Provides
    fun provideMainRepository(mainApi: MainApi): MainRepository {
        return MainRepository(mainApi)
    }

    @ActivityScope
    @Provides
    fun provideMainViewModelFactory(
        application: Application,
        mainRepository: MainRepository
    ): MainViewModelFactory {
        return MainViewModelFactory(application, mainRepository)
    }
}