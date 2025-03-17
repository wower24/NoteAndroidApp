package hoods.com.noteapplication.common

//sealed class will provide a clearer way to handle states
sealed class ScreenViewState<out T> {
    //nothing happens on loading, no data passed
    object Loading: ScreenViewState<Nothing>()
    //when data is successful
    data class Success<T>(val data: T): ScreenViewState<T>()
    //when there's an error
    data class Error(val message: String): ScreenViewState<Nothing>()
}