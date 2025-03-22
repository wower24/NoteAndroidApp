package hoods.com.noteapplication.presentation.detail

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import hoods.com.noteapplication.data.local.model.Note
import hoods.com.noteapplication.domain.use_cases.AddUseCase
import hoods.com.noteapplication.domain.use_cases.GetNoteByIdUseCase
import kotlinx.coroutines.launch
import java.util.Date


//Assisted Inject lets pass a dependency at runtime, not compile time. Needed for passing note id.
//no HiltViewModel because we use AssistedInject
//Instead, create factories that for assisted stuff
class DetailViewModel @AssistedInject constructor(
    private val addUseCase: AddUseCase,
    private val getNoteByIdUseCase: GetNoteByIdUseCase,
    @Assisted private val noteId: Long
): ViewModel() {
    var state by mutableStateOf(DetailState())
        private set
    //check if title && content !empty
    val isFormNotBlank: Boolean
        get() = state.title.isNotEmpty() &&
                state.content.isNotEmpty()

    //construct the note according to state
    private val note: Note
        get() = state.run {
            Note(
                id = id,
                title = title,
                content = content,
                createdDate = createdDate
            )
        }

    //create an initializer
    private fun initialize() {
        //notes in update or in creation will have an id of -1
        val isUpdatingNote = noteId != -1L
        state = state.copy(isUpdatingNote = isUpdatingNote)

        //if note is updating, get it by id
        if(isUpdatingNote) {
            getNoteById()
        }
    }

    private fun getNoteById() = viewModelScope.launch {
        //here we get the note by noteId and this note is collected
        //in the tracking lambda we can use arguments of the collected note
        //then load the state with this note data
        getNoteByIdUseCase(noteId).collect { note ->
            state = state.copy(
                id = note.id,
                title = note.title,
                content = note.content,
                isBookmarked = note.isBookmarked,
                createdDate = note.createdDate
            )
        }
    }

    //define events that can be changed to update states
    fun onTitleChange(title: String) {
        state = state.copy(title = title)
    }

     fun onContentChange(content: String) {
        state = state.copy(content = content)
    }

     fun onBookmarkChange(isBookmarked: Boolean) {
        state = state.copy(isBookmarked = isBookmarked)
    }

    //add or update data
    fun addOrUpdateNote() = viewModelScope.launch {
        addUseCase(note = note)
    }


}

data class DetailState(
    val id: Long = 0,
    val title: String = "",
    val content: String = "",
    val isBookmarked: Boolean = false,
    val createdDate: Date = Date(),
    val isUpdatingNote: Boolean = false
)

//factory that can easily pass id at runtime
class DetailViewModelFactory(
    private val noteId: Long,
    private val assistedFactory: DetailAssistedFactory
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return assistedFactory.create(noteId) as T
    }
}

//interface assisting for the injection
@AssistedFactory
interface DetailAssistedFactory {
    fun create(noteId: Long): DetailViewModel
}