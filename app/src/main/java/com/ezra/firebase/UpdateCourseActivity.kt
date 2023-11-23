package com.ezra.firebase

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.firestore.FirebaseFirestore

class UpdateCourseActivity : ComponentActivity() {

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            firebaseUI(
                LocalContext.current,
                intent.getStringExtra("courseName"),
                intent.getStringExtra("courseDuration"),
                intent.getStringExtra("courseDescription"),
                intent.getStringExtra("courseID")
            )

        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun firebaseUI(
        context: Context,
        name: String?,
        duration: String?,
        description: String?,
        courseID: String?
    ) {

        // on below line creating variable for course name,
        // course duration and course description.
        val courseName = remember {
            mutableStateOf(name)
        }

        val courseDuration = remember {
            mutableStateOf(duration)
        }

        val courseDescription = remember {
            mutableStateOf(description)
        }

        // on below line creating a column
        Column(
            // adding modifier for our column
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth()
                .background(Color.White),
            // on below line adding vertical and
            // horizontal alignment for column.
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {


            TextField(
                // on below line we are specifying
                // value for our course name text field.
                value = courseName.value.toString(),

                // on below line we are adding on
                // value change for text field.
                onValueChange = { courseName.value = it },

                // on below line we are adding place holder
                // as text as "Enter your course name"
                placeholder = { Text(text = "Enter your course name") },

                // on below line we are adding modifier to it
                // and adding padding to it and filling max width
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),

                // on below line we are adding text style
                // specifying color and font size to it.
                textStyle = TextStyle(color = Color.Black, fontSize = 15.sp),

                // on below line we are adding
                // single line to it.
                singleLine = true,
            )

            Spacer(modifier = Modifier.height(10.dp))

            TextField(
                // on below line we are specifying
                // value for our course duration text field.
                value = courseDuration.value.toString(),

                // on below line we are adding on
                // value change for text field.
                onValueChange = { courseDuration.value = it },

                // on below line we are adding place holder
                // as text as "Enter your course duration"
                placeholder = { Text(text = "Enter your course duration") },

                // on below line we are adding modifier to it
                // and adding padding to it and filling max width
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),

                // on below line we are adding text style
                // specifying color and font size to it.
                textStyle = TextStyle(color = Color.Black, fontSize = 15.sp),

                // on below line we are adding
                // single line to it.
                singleLine = true,
            )

            Spacer(modifier = Modifier.height(10.dp))

            TextField(
                // on below line we are specifying
                // value for our course description text field.
                value = courseDescription.value.toString(),

                // on below line we are adding on
                // value change for text field.
                onValueChange = { courseDescription.value = it },

                // on below line we are adding place holder
                // as text as "Enter your course description"
                placeholder = { Text(text = "Enter your course description") },

                // on below line we are adding modifier to it
                // and adding padding to it and filling max width
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),

                // on below line we are adding text style
                // specifying color and font size to it.
                textStyle = TextStyle(color = Color.Black, fontSize = 15.sp),

                // on below line we are adding
                // single line to it.
                singleLine = true,
            )

            Spacer(modifier = Modifier.height(10.dp))

            // on below line creating button to add data
            // to firebase firestore database.
            Button(
                onClick = {
                    // on below line we are validating user input parameters.
                    if (TextUtils.isEmpty(courseName.value.toString())) {
                        Toast.makeText(context, "Please enter course name", Toast.LENGTH_SHORT)
                            .show()
                    } else if (TextUtils.isEmpty(courseDuration.value.toString())) {
                        Toast.makeText(
                            context,
                            "Please enter course Duration",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    } else if (TextUtils.isEmpty(courseDescription.value.toString())) {
                        Toast.makeText(
                            context,
                            "Please enter course description",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    } else {
                        // on below line adding data to
                        // firebase firestore database.
                        updateDataToFirebase(
                            courseID,
                            courseName.value,
                            courseDuration.value,
                            courseDescription.value,
                            context
                        )
                    }
                },
                // on below line we are
                // adding modifier to our button.
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                // on below line we are adding text for our button
                Text(text = "Update Data", modifier = Modifier.padding(8.dp))
            }

            Spacer(modifier = Modifier.height(10.dp))

            // on below line creating button to delete the course.
            Button(
                onClick = {
                    // on below line calling delete course
                    // method to delete our course.
                    deleteDataFromFirebase(courseID, context)
                },
                // on below line we are
                // adding modifier to our button.
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                // on below line we are adding text for our button
                Text(text = "Delete Course", modifier = Modifier.padding(8.dp))
            }
        }
    }

    private fun deleteDataFromFirebase(courseID: String?, context: Context) {

        // getting our instance from Firebase Firestore.
        val db = FirebaseFirestore.getInstance();

        // below line is for getting the collection
        // where we are storing our courses.
        db.collection("Courses").document(courseID.toString()).delete().addOnSuccessListener {
            // displaying toast message when our course is deleted.
            Toast.makeText(context, "Course Deleted successfully..", Toast.LENGTH_SHORT).show()
            context.startActivity(Intent(context, CoursesActivity::class.java))
        }.addOnFailureListener {
            // on below line displaying toast message when
            // we are not able to delete the course
            Toast.makeText(context, "Fail to delete course..", Toast.LENGTH_SHORT).show()
        }

    }


    private fun updateDataToFirebase(
        courseID: String?,
        name: String?,
        duration: String?,
        description: String?,
        context: Context
    ) {
        // inside this method we are passing our updated values
        // inside our object class and later on we
        // will pass our whole object tofirebase Firestore.
        val updatedCourse = Course(courseID, name, duration, description)

        // getting our instance from Firebase Firestore.
        val db = FirebaseFirestore.getInstance();
        db.collection("Courses").document(courseID.toString()).set(updatedCourse)
            .addOnSuccessListener {
                // on below line displaying toast message and opening
                // new activity to view courses.
                Toast.makeText(context, "Course Updated successfully..", Toast.LENGTH_SHORT).show()
                context.startActivity(Intent(context, CoursesActivity::class.java))
                finish()

            }.addOnFailureListener {
                Toast.makeText(context, "Fail to update course : " + it.message, Toast.LENGTH_SHORT)
                    .show()
            }
    }
}
