package com.example.nasapictures.di

import com.example.nasapictures.interactor.ImagesGridInteractor
import com.example.nasapictures.interactor.ImagesGridInteractorImpl
import com.example.nasapictures.router.ImagesGridRouter
import com.example.nasapictures.router.ImagesGridRouterImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.components.ActivityRetainedComponent


@InstallIn(ActivityRetainedComponent::class)
@Module
abstract class ImagesGridActivityRetainedModule {
    @Binds
    abstract fun bindInteractor(impl: ImagesGridInteractorImpl): ImagesGridInteractor
}

@InstallIn(ActivityComponent::class)
@Module
abstract class ImagesGridActivityModule {
    @Binds
    abstract fun bindRouter(impl: ImagesGridRouterImpl): ImagesGridRouter
}
