package com.yang.module_square.di.module

import com.yang.module_square.api.SquareApi



import android.app.Application
import com.yang.lib_common.scope.ActivityScope
import com.yang.module_square.di.factory.SquareViewModelFactory
import com.yang.module_square.repository.SquareRepository
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

/**
 * @ClassName: SquareModel
 * @Description:
 * @Author: yxy
 * @Date: 2022/7/21 14:31
 */
@Module
class SquareModule {

    @ActivityScope
    @Provides
    fun provideSquareApi(retrofit: Retrofit): SquareApi {
        return retrofit.create(SquareApi::class.java)
    }

    @ActivityScope
    @Provides
    fun provideSquareRepository(squareApi: SquareApi): SquareRepository {
        return SquareRepository(squareApi)
    }

    @ActivityScope
    @Provides
    fun provideSquareViewModelFactory(
        application: Application,
        squareRepository: SquareRepository
    ): SquareViewModelFactory {
        return SquareViewModelFactory(application, squareRepository)
    }
}