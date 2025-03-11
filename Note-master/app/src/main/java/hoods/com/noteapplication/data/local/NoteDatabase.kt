package hoods.com.noteapplication.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import hoods.com.noteapplication.data.local.converters.DateConverter
import hoods.com.noteapplication.data.local.model.Note

//entry point of application, actual database

//declare used TypeConverters
@TypeConverters(value = [DateConverter::class])
//declare all entities, version and whether it will be exported
@Database(
    entities = [Note::class],
    version = 1,
    exportSchema = false
)
abstract class NoteDatabase: RoomDatabase() {
    //access object
    abstract val noteDao: NoteDao
}