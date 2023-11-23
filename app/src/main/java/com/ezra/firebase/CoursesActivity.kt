package com.ezra.firebase

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material.*
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.firestore.FirebaseFirestore

class CoursesActivity : ComponentActivity() {

    @SuppressLint("UnrememberedMutableState", "UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContent {
            // on below line creating variable for list of data.
            var courseList = mutableStateListOf<Course?>()
            // on below line creating variable for freebase database
            // and database reference.
            var db: FirebaseFirestore = FirebaseFirestore.getInstance()

            // on below line getting data from our database
            db.collection("Courses").get()
                .addOnSuccessListener { queryDocumentSnapshots ->
                    // after getting the data we are
                    // calling on success method
                    // and inside this method we are
                    // checking if the received query
                    // snapshot is empty or not.
                    if (!queryDocumentSnapshots.isEmpty) {
                        // if the snapshot is not empty we are
                        // hiding our progress bar and adding
                        // our data in a list.
                        // loadingPB.setVisibility(View.GONE)
                        val list = queryDocumentSnapshots.documents
                        for (d in list) {
                            // after getting this list we are passing that list
                            // to our object class.
                            val c: Course? = d.toObject(Course::class.java)
                            c?.courseID = d.id
                            Log.e("TAG", "Course id is : " + c!!.courseID)
                            // and we will pass this object class
                            // inside our arraylist which we have
                            // created for list view.
                            courseList.add(c)

                        }
                    } else {
                        // if the snapshot is empty we are
                        // displaying a toast message.
                        Toast.makeText(
                            this@CoursesActivity,
                            "No data found in Database",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
                // if we don't get any data
                // or any error we are displaying
                // a toast message that we donot get any data
                .addOnFailureListener {
                    Toast.makeText(
                        this@CoursesActivity,
                        "Fail to get the data.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            // on below line we are calling method to display UI
            firebaseUI(LocalContext.current, courseList)

        }
    }



    @Composable
    fun firebaseUI(context: Context, courseList: SnapshotStateList<Course?>) {

        // on below line creating a column
        // to display our retrieved list.
        Column(
            // adding modifier for our column
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth()
                .background(Color.White),
            // on below line adding vertical and
            // horizontal alignment for column.
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // on below line we are calling lazy column
            // for displaying listview.
            LazyVerticalGrid(columns = GridCells.Fixed(2)){
                // on below line we are setting data
                // for each item of our listview.
                itemsIndexed(courseList) { index, item ->
                    // on below line we are creating
                    // a card for our list view item.
                    Card(
                      modifier = Modifier
                          .padding(8.dp)
                          .clickable {
                              val i = Intent(context, UpdateCourseActivity::class.java)
                              i.putExtra("courseName", item?.courseName)
                              i.putExtra("courseDuration", item?.courseDuration)
                              i.putExtra("courseDescription", item?.courseDescription)
                              i.putExtra("courseID", item?.courseID)
                              // inside on click we are opening
                              // new activity to update course details.
                              context.startActivity(i)
                          },
                        // on below line we are adding
                        // padding from our all sides.
                        elevation = CardDefaults.cardElevation( defaultElevation = 6.dp)


                        // on below line we are adding
                        // elevation for the card.

                    ) {
                        // on below line we are creating
                        // a row for our list view item.
                        Column(
                            // for our row we are adding modifier
                            // to set padding from all sides.
                            modifier = Modifier
                                .padding(8.dp)
                                .fillMaxWidth()
                        ) {
                            // on below line inside row we are adding spacer
                            Spacer(modifier = Modifier.width(5.dp))
                            // on below line we are displaying course name.
                            courseList[index]?.courseName?.let {
                                Text(
                                    // inside the text on below line we are
                                    // setting text as the language name
                                    // from our modal class.
                                    text = it,

                                    // on below line we are adding padding
                                    // for our text from all sides.
                                    modifier = Modifier.padding(4.dp),

                                    // on below line we are adding color for our text

                                    textAlign = TextAlign.Center,
                                    style = TextStyle(
                                        fontSize = 20.sp, fontWeight = FontWeight.Bold
                                    )
                                )
                            }
                            // adding spacer on below line.
                            Spacer(modifier = Modifier.height(5.dp))

                            // on below line displaying text for course duration
                            courseList[index]?.courseDuration?.let {
                                Text(
                                    // inside the text on below line we are
                                    // setting text as the language name
                                    // from our modal class.
                                    text = it,

                                    // on below line we are adding padding
                                    // for our text from all sides.
                                    modifier = Modifier.padding(4.dp),

                                    // on below line we are adding color for our text
                                    color = Color.Black,
                                    textAlign = TextAlign.Center,
                                    style = TextStyle(
                                        fontSize = 15.sp
                                    )
                                )
                            }
                            // adding spacer on below line.
                            Spacer(modifier = Modifier.width(5.dp))

                            // on below line displaying text
                            // for course description
                            courseList[index]?.courseDescription?.let {
                                Text(
                                    // inside the text on below line we are
                                    // setting text as the language name
                                    // from our modal class.
                                    text = it,

                                    // on below line we are adding padding
                                    // for our text from all sides.
                                    modifier = Modifier.padding(4.dp),

                                    // on below line we are
                                    // adding color for our text
                                    color = Color.Black,
                                    textAlign = TextAlign.Center,
                                    style = TextStyle(fontSize = 15.sp)
                                )
                            }
                        }
                    }
                }

            }
        }
    }
}
