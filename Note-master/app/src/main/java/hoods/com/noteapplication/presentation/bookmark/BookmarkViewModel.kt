package hoods.com.noteapplication.presentation.bookmark

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hoods.com.noteapplication.common.ScreenViewState
import hoods.com.noteapplication.data.local.model.Note
import hoods.com.noteapplication.domain.use_cases.BookmarkNotesUseCase
import hoods.com.noteapplication.domain.use_cases.DeleteUseCase
import hoods.com.noteapplication.domain.use_cases.UpdateUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookmarkViewModel @Inject constructor(
    private val updateUseCase: UpdateUseCase,
    private val bookmarkNotesUseCase: BookmarkNotesUseCase,
    private val deleteUseCase: DeleteUseCase
): ViewModel() {
    private val _state: MutableStateFlow<BookmarkState> = MutableStateFlow(BookmarkState())
    val state: StateFlow<BookmarkState> = _state.asStateFlow()

    private fun getBookmarkNotes() {
        bookmarkNotesUseCase.invoke()
            .onEach {
            _state.value = BookmarkState(
                notes = ScreenViewState.Success(it)
            )
            }
            .catch {
                _state.value = BookmarkState(
                    notes = ScreenViewState.Error(it.message.toString())
                )
            }
            .launchIn(viewModelScope)
    }

    fun onBookmarkChange(note: Note) {
        viewModelScope.launch {
            updateUseCase(note.copy(
                isBookmarked = !note.isBookmarked
            ))
        }
    }

    fun deleteNote(noteId: Long) {
        viewModelScope.launch {
            deleteUseCase(noteId)
        }
    }
}

data class BookmarkState(
    val notes: ScreenViewState<List<Note>> = ScreenViewState.Loading
)