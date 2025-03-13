package hoods.com.noteapplication.domain.use_cases

import hoods.com.noteapplication.data.local.model.Note
import hoods.com.noteapplication.domain.repository.Repository
import javax.inject.Inject

//Use Cases represent the app's business logic
// and encapsulate specific user interactions

//Use Cases are a bridge between the app's rules and outer layers

//Keeping the core functionality isolated from external concerns

//this use case is adding a note
class AddUseCase @Inject constructor(
    private val repository :Repository
) {
    //operator modifier lets a class to be called as a function
    suspend operator fun invoke(note : Note) = repository.insert(note)

}