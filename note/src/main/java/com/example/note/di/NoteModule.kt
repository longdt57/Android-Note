package com.example.note.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import leegroup.module.compose.support.util.DispatchersProvider
import leegroup.module.compose.support.util.DispatchersProviderImpl
import javax.inject.Qualifier

@Module
@InstallIn(SingletonComponent::class)
class NoteModule {

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class NoteDispatcherProvider

    @Provides
    @NoteDispatcherProvider
    fun provideDispatchersProvider(dispatcher: DispatchersProviderImpl): DispatchersProvider =
        dispatcher
}
