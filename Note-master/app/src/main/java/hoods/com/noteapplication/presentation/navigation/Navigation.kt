package hoods.com.noteapplication.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import hoods.com.noteapplication.presentation.bookmark.BookmarkScreen
import hoods.com.noteapplication.presentation.bookmark.BookmarkViewModel
import hoods.com.noteapplication.presentation.detail.DetailAssistedFactory
import hoods.com.noteapplication.presentation.home.HomeScreen
import hoods.com.noteapplication.presentation.home.HomeViewModel
import androidx.compose.runtime.getValue
import androidx.navigation.NavType
import androidx.navigation.navArgument
import hoods.com.noteapplication.presentation.detail.DetailScreen

//routes based on enum class
enum class Screens {
    Home,
    Detail,
    Bookmark
}

@Composable
fun NoteNavigation(
    modifier: Modifier = Modifier,
    navHostController: NavHostController,
    homeViewModel: HomeViewModel,
    bookmarkViewModel: BookmarkViewModel,
    assistedFactory: DetailAssistedFactory
) {
    NavHost(
        navController = navHostController,
        startDestination = Screens.Home.name
    ) {
        composable(route = Screens.Home.name) {
            val state by homeViewModel.state.collectAsState()
            HomeScreen(
                state = state,
                onBookmarkChange = homeViewModel::onBookmarkChange,
                onDeleteNote = homeViewModel::deleteNote,
                onNoteClick = {
                    //add an optional parameter '?id'
                    navHostController.navigateToSingleTop("${Screens.Detail.name}?id=$it")
                }
            )
        }

        composable(route = Screens.Bookmark.name) {
            val state by bookmarkViewModel.state.collectAsState()
            BookmarkScreen(
                state = state,
                modifier = modifier,
                onBookmarkChange = bookmarkViewModel::onBookmarkChange,
                onDelete = bookmarkViewModel::deleteNote,
                onNoteClick = {
                    navHostController.navigateToSingleTop("${Screens.Detail.name}?id=$it")
                }
            )
        }
        //for this screen argument (id) is needed so the route is different
        //the {id} will be replaced by $it passed from other screens
        composable(
            route = "${Screens.Detail.name}?id={id}",
            arguments = listOf(navArgument("id") {
                NavType.LongType
                defaultValue = -1L
            })
            ) { backStackEntry ->
            //get id from the arguments
            //val name = it.arguments?.getType("argumentName")
            val id = backStackEntry.arguments?.getLong("id") ?: -1L

            DetailScreen(
                noteId = id,
                assistedFactory = assistedFactory,
                navigateUp = {
                    navHostController.navigateUp()
                }
            )
        }
    }
}

//manage the screen switch
fun NavHostController.navigateToSingleTop(route: String) {
    navigate(route) {
        popUpTo(graph.findStartDestination().id) { saveState = true }
        launchSingleTop = true
        restoreState = true
    }
}