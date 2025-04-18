package hoods.com.noteapplication.presentation.bookmark

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import hoods.com.noteapplication.common.ScreenViewState
import hoods.com.noteapplication.data.local.model.Note
import hoods.com.noteapplication.presentation.home.NoteCard
import hoods.com.noteapplication.presentation.home.notes

@Composable
fun BookmarkScreen(
    state: BookmarkState,
    modifier: Modifier = Modifier,
    onBookmarkChange: (note: Note) -> Unit,
    onDelete: (Long) -> Unit,
    onNoteClick: (Long) -> Unit
) {
    when(state.notes) {
        is ScreenViewState.Loading -> {
            CircularProgressIndicator()
        }
        is ScreenViewState.Success -> {
            val notes = state.notes.data

            LazyColumn(
                contentPadding = PaddingValues(4.dp),
                modifier = modifier
            ) {
                itemsIndexed(notes) { index, note ->
                    NoteCard(
                        index = index,
                        note = note,
                        onBookmarkChange = onBookmarkChange,
                        onDeleteNote = onDelete,
                        onNoteClick = onNoteClick
                    )
                }
            }
        }
        is ScreenViewState.Error -> {
            Text(
                text = state.notes.message ?: "Unknown Error",
                color = MaterialTheme.colorScheme.error
            )
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun PrevBookmark() {
    BookmarkScreen(
        state = BookmarkState(
            notes = ScreenViewState.Success(notes)
        ),
        onBookmarkChange = {},
        onDelete = {},
        onNoteClick = {}
    )
}