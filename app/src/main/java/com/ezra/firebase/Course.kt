package com.ezra.firebase
import com.google.firebase.firestore.Exclude
import java.time.Duration


// on below line creating
// a data class for course,
data class Course(
    // on below line creating variables.
    @Exclude var courseID: String? = "",
    var courseName: String? = "",
    var courseDuration: String? = "",
    var courseDescription: String? = ""
)


