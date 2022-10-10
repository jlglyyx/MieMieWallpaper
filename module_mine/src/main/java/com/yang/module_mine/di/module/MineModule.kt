package com.yang.module_mine.di.module

import android.app.Application
import com.yang.lib_common.scope.ActivityScope
import com.yang.module_mine.api.MineApi
import com.yang.module_mine.di.factory.MineViewModelFactory
import com.yang.module_mine.repository.MineRepository
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

/**
 * @ClassName: MineModel
 * @Description:
 * @Author: yxy
 * @Date: 2022/7/21 14:31
 */
@Module
class MineModule {

    @ActivityScope
    @Provides
    fun provideMineApi(retrofit: Retrofit): MineApi {
        return retrofit.create(MineApi::class.java)
    }

    @ActivityScope
    @Provides
    fun provideMineRepository(mainApi: MineApi): MineRepository {
        return MineRepository(mainApi)
    }

    @ActivityScope
    @Provides
    fun provideMineViewModelFactory(
        application: Application,
        mainRepository: MineRepository
    ): MineViewModelFactory {
        return MineViewModelFactory(application, mainRepository)
    }
}