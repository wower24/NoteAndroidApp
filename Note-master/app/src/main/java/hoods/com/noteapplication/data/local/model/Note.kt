package hoods.com.noteapplication.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

//this is our local "database"
//the table is named notes
//the arguments are: id, title, content, createdDate and isBookmarked
@Entity(tableName = "notes")
data class Note(
    @PrimaryKey(autoGenerate = true)
    val id:Long = 0,
    val title :String,
    val content :String,
    val createdDate :Date,
    val isBookmarked :Boolean = false
)