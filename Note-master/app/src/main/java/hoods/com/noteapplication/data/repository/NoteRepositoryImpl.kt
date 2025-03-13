package hoods.com.noteapplication.data.repository

import hoods.com.noteapplication.data.local.NoteDao
import hoods.com.noteapplication.data.local.model.Note
import hoods.com.noteapplication.domain.repository.Repository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

//Implementation of a Repository interface
//Will be helpful while testing

//annotate class as constructor injected and pass a secondary constructor
class NoteRepositoryImpl @Inject constructor(
    private val noteDao :NoteDao
) : Repository {
    //CTRL + i to implement all interface methods
    override fun getAllNotes(): Flow<List<Note>> {
        return noteDao.getAllNotes()
    }

    override fun getNoteById(id: Long): Flow<Note> {
        return noteDao.getNoteById(id)
    }

    override fun getBookmarkedNotes(): Flow<List<Note>> {
        return noteDao.getBookmarkedNotes()
    }

    override fun insert(note: Note) {
        noteDao.insertNote(note)
    }

    override fun update(note: Note) {
        noteDao.updateNote(note)
    }

    override fun delete(id: Long) {
        noteDao.deleteNote(id)
    }
}