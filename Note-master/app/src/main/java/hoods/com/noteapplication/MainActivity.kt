package hoods.com.noteapplication

import android.os.Bundle
import android.renderscript.ScriptGroup
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.InputChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import hoods.com.noteapplication.presentation.bookmark.BookmarkViewModel
import hoods.com.noteapplication.presentation.detail.DetailAssistedFactory
import hoods.com.noteapplication.presentation.home.HomeViewModel
import hoods.com.noteapplication.presentation.navigation.NoteNavigation
import hoods.com.noteapplication.presentation.navigation.Screens
import hoods.com.noteapplication.presentation.navigation.navigateToSingleTop
import hoods.com.noteapplication.ui.theme.NoteApplicationTheme
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject lateinit var assistedFactory: DetailAssistedFactory
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NoteApplicationTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                    contentColor = MaterialTheme.colorScheme.onSurface
                ) {
                    NoteApp()
                }
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun NoteApp() {
        //initialize view models
        val homeViewModel: HomeViewModel = viewModel()
        val bookmarkViewModel: BookmarkViewModel = viewModel()
        val navController = rememberNavController()
        var currentTab by remember {
            mutableStateOf(Tabs.Home)
        }

        //display bottom bar
        Scaffold(
            bottomBar = {
                BottomAppBar(
                    //define tabs
                    actions = {
                        Row(horizontalArrangement = Arrangement.Center) {
                            InputChip(
                                selected = currentTab == Tabs.Home,
                                onClick = {
                                    currentTab = Tabs.Home
                                    navController.navigateToSingleTop(route = Screens.Home.name)
                                },
                                label = {
                                    Text(text = "Home")
                                },
                                trailingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.Home,
                                        contentDescription = null
                                    )
                                }
                            )

                            Spacer(modifier = Modifier.Companion.size(12.dp))

                            InputChip(
                                selected = currentTab == Tabs.Bookmark,
                                onClick = {
                                    currentTab = Tabs.Bookmark
                                    navController.navigateToSingleTop(route = Screens.Bookmark.name)
                                },
                                label = {
                                    Text(text = "Bookmark")
                                },
                                trailingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.Bookmark,
                                        contentDescription = null
                                    )
                                }
                            )
                        }
                    },
                    //define floating action button to create a new note
                    floatingActionButton = {
                        FloatingActionButton(
                            onClick = {
                                navController.navigateToSingleTop(
                                    route = Screens.Detail.name
                                )
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = null
                            )
                        }
                    }
                )
            }
        ) {
            NoteNavigation(
                modifier = Modifier.padding(it),
                navHostController = navController,
                homeViewModel = homeViewModel,
                bookmarkViewModel = bookmarkViewModel,
                assistedFactory = assistedFactory
            )
        }
    }

    enum class Tabs {
        Home,
        Bookmark
    }
}

