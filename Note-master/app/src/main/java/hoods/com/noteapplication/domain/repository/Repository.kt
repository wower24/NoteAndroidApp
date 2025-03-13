package hoods.com.noteapplication.domain.repository

import hoods.com.noteapplication.data.local.model.Note
import kotlinx.coroutines.flow.Flow

interface Repository {
    //methods to access the database
    fun getAllNotes() :Flow<List<Note>>
    fun getNoteById(id :Long) :Flow<Note>
    fun getBookmarkedNotes() :Flow<List<Note>>
    fun insert(note :Note)
    fun update(note :Note)
    fun delete(id :Long)
}