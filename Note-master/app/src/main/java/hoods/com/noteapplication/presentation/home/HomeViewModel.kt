package hoods.com.noteapplication.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hoods.com.noteapplication.common.ScreenViewState
import hoods.com.noteapplication.data.local.model.Note
import hoods.com.noteapplication.domain.use_cases.DeleteUseCase
import hoods.com.noteapplication.domain.use_cases.GetAllNotesUseCase
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
class HomeViewModel @Inject constructor(
    //give use cases here
    private val getAllNotesUseCase: GetAllNotesUseCase,
    private val deleteUseCase: DeleteUseCase,
    private val updateUseCase: UpdateUseCase
): ViewModel() {
    //create a state that can be easily observed from ViewModel to UI layer
    private val _state: MutableStateFlow<HomeState> = MutableStateFlow(HomeState())
    //create READ ONLY state
    val state: StateFlow<HomeState> = _state.asStateFlow()

    //call getAllNotes() when application is launched
    init {
        getAllNotes()
    }

    //first thing in the app - see all notes
    private fun getAllNotes() {
        //we can call it like that because of OPERATOR FUN, except we can't
        getAllNotesUseCase.invoke()
            .onEach {
            _state.value = HomeState(notes = ScreenViewState.Success(it))
            }
            .catch {
                _state.value = HomeState(notes = ScreenViewState.Error(it.message.toString()))
            }
            .launchIn(viewModelScope)
    }

    fun deleteNote(noteId: Long) = viewModelScope.launch {
        deleteUseCase(noteId)
    }

    fun onBookmarkChange(note: Note) {
        viewModelScope.launch {
            //update bookmark value
            updateUseCase(note.copy(isBookmarked = !note.isBookmarked))
        }
    }
}

data class HomeState(
    //I don't understand why ScreenViewState yet
    val notes: ScreenViewState<List<Note>> = ScreenViewState.Loading,

)