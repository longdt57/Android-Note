package com.example.note.di

import com.example.note.domain.repositories.CloudRepository
import com.example.note.data.repositories.CloudRepositoryImpl
import com.example.note.data.repositories.NoteRepositoryImpl
import com.example.note.domain.repositories.NoteRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
internal interface RepositoryModule {

    @Binds
    fun bindNoteRepository(repository: NoteRepositoryImpl): NoteRepository

    @Binds
    fun bindCloudRepository(repository: CloudRepositoryImpl): CloudRepository

}
