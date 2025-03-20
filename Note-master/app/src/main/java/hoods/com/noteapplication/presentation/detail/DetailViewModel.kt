package hoods.com.noteapplication.presentation.detail

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import hoods.com.noteapplication.domain.use_cases.AddUseCase
import hoods.com.noteapplication.domain.use_cases.GetNoteByIdUseCase
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