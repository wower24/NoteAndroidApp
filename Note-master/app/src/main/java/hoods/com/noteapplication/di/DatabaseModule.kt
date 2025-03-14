package hoods.com.noteapplication.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import hoods.com.noteapplication.data.local.NoteDao
import hoods.com.noteapplication.data.local.NoteDatabase
import javax.inject.Singleton

//annotate it as a model
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    //whenever NoteDao is needed, access this method
    // that will provide required dependencies
    @Provides
    @Singleton
    fun provideNoteDao(database: NoteDatabase): NoteDao = database.noteDao

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): NoteDatabase {
       //arguments: context, db type, db name
        return Room.databaseBuilder(
            context,
            NoteDatabase::class.java,
            "notes_db"
        ).build()
    }

}