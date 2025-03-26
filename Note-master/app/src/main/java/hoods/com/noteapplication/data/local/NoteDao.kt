package hoods.com.noteapplication.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import hoods.com.noteapplication.data.local.model.Note
import kotlinx.coroutines.flow.Flow

//define a method to access database
//Dao = Data access object

@Dao
interface NoteDao {
    //pattern for getting data:
    //@Query("SQL PROMPT")
    //fun nameOfFunctionThatWillExecutePrompt :Flow<List<TypeOfDatabaseObject>>

    //get all notes
    @Query("SELECT * FROM notes ORDER BY createdDate")
    fun getAllNotes() :Flow<List<Note>>

    //match provided id with id argument
    //id=:id
    @Query("SELECT * FROM notes WHERE id=:id ORDER BY createdDate")
    fun getNoteById(id :Long) :Flow<Note>

    @Query("SELECT * FROM notes WHERE isBookmarked=1 ORDER BY createdDate DESC")
    fun getBookmarkedNotes() :Flow<List<Note>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note :Note)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateNote(note :Note)

    //IF YOU DON'T DO ID AS A PARAMETER THIS WOULD DELETE ALL THE DATA
    @Query("DELETE FROM notes WHERE id=:id")
    suspend fun deleteNote(id :Long)
}

//room database is an abstraction layer between database and application