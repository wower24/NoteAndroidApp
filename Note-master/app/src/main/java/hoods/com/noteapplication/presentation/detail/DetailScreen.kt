package hoods.com.noteapplication.presentation.detail

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.BookmarkRemove
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Update
import androidx.compose.material.icons.outlined.BookmarkAdd
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

//to be used outside this file
@Composable
fun DetailScreen(
    modifier: Modifier = Modifier,
    noteId: Long,
    assistedFactory: DetailAssistedFactory,
    navigateUp: () -> Unit
) {
    //modelClass - a reference of what we want to build
    //factory - what are we building with
    val viewModel = viewModel(
        modelClass = DetailViewModel::class.java,
        factory = DetailViewModelFactory(
            noteId = noteId,
            assistedFactory = assistedFactory
        )
    )

    //public DetailScreen - state composable
    //private DetailScreen - stateless composable
    val state = viewModel.state
    DetailScreen(
        modifier = modifier,
        isUpdatingNote = state.isUpdatingNote,
        isFormNotBlank = state.isUpdatingNote,
        title = state.title,
        content = state.content,
        isBookmarked = state.isBookmarked,
        onBookmarkChange = viewModel::onBookmarkChange,
        onContentChange = viewModel::onContentChange,
        onTitleChange = viewModel::onTitleChange,
        onButtonClick = {
            viewModel.addOrUpdateNote()
            navigateUp()
        },
        onNavigate = navigateUp
    )

}

//to be used inside this file
@Composable
private fun DetailScreen(
    modifier: Modifier,
    isUpdatingNote: Boolean,
    title: String,
    content: String,
    isBookmarked: Boolean,
    isFormNotBlank: Boolean,
    onTitleChange:(String) -> Unit,
    onContentChange:(String) -> Unit,
    onBookmarkChange: (Boolean) -> Unit,
    onButtonClick: () -> Unit,
    onNavigate: () -> Unit
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        TopSection(
            title = title,
            isBookmarked = isBookmarked,
            onBookmarkChange = onBookmarkChange,
            onTitleChange = onTitleChange,
            onNavigate = onNavigate
        )

        Spacer(modifier = Modifier.Companion.size(12.dp))

        //chow an icon button different depending on whether you're updating or not
        AnimatedVisibility(isFormNotBlank) {
            //row is needed to align the checkmark to the end
            Row(
                modifier = modifier.fillMaxWidth()
                    .padding(12.dp),
                horizontalArrangement = Arrangement.End
            ) {
                IconButton(onClick = onButtonClick) {
                    val icon = if(isUpdatingNote) Icons.Default.Update
                        else Icons.Default.Check

                    Icon(
                        imageVector = icon,
                        contentDescription = null
                    )
                }
            }
        }

        Spacer(modifier = Modifier.Companion.size(12.dp))

        NoteTextField(
            modifier = Modifier.weight(1f),
            value = content,
            label = "Content",
            onValueChange = onContentChange
        )
    }
}

@Composable
private fun TopSection(
    modifier: Modifier = Modifier,
    title: String,
    isBookmarked: Boolean,
    onBookmarkChange: (Boolean) -> Unit,
    onTitleChange: (String) -> Unit,
    onNavigate: () -> Unit
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onNavigate) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = null
            )
        }

        NoteTextField(
            modifier = Modifier.weight(1f),
            value = title,
            label = "Title",
            labelAlign = TextAlign.Center,
            onValueChange = onTitleChange
        )

        //onBookmarkChange takes a parameter so it needs to be put in curly braces
        IconButton( onClick = { onBookmarkChange( !isBookmarked ) } ) {
            val icon = if(isBookmarked) Icons.Default.BookmarkRemove
                            else Icons.Outlined.BookmarkAdd

            Icon(
                imageVector = icon,
                contentDescription = null
            )
        }
    }
}

@Composable
private fun NoteTextField(
    modifier: Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    labelAlign: TextAlign? = null
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        colors = TextFieldDefaults.colors(
            disabledContainerColor = Color.Transparent,
            focusedContainerColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent
        ),
        placeholder = {
            Text(
                text = "Insert $label",
                textAlign = labelAlign,
                modifier = modifier.fillMaxWidth()
            )
        }
    )

}