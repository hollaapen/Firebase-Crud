package com.ezra.firebase

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar



import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import android.content.Context
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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

    @OptIn(ExperimentalMaterial3Api::class)
    @SuppressLint("UnrememberedMutableState", "UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContent {
            var courseList = mutableStateListOf<Course?>()
            // on below line creating variable for freebase database
            // and database reference.
            var db: FirebaseFirestore = FirebaseFirestore.getInstance()

            db.collection("Courses").get()
                .addOnSuccessListener { queryDocumentSnapshots ->

                    if (!queryDocumentSnapshots.isEmpty) {
                        // if the snapshot is not empty we are
                        // hiding our progress bar and adding
                        // our data in a list.
                        // loadingPB.setVisibility(View.GONE)
                        val list = queryDocumentSnapshots.documents
                        for (d in list) {
                            // after getting this list we are passing that
                            // list to our object class.
                            val c: Course? = d.toObject(Course::class.java)
                            // and we will pass this object class inside
                            // our arraylist which we have created for list view.
                            courseList.add(c)

                        }
                    } else {
                        // if the snapshot is empty we are displaying
                        // a toast message.
                        Toast.makeText(
                            this@CoursesActivity,
                            "No data found in Database",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
                // if we don't get any data or any error
                // we are displaying a toast message
                // that we do not get any data
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

        Column(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth()
                .background(Color.White),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {


            LazyColumn() {
                // on below line we are setting data
                // for each item of our listview.
                itemsIndexed(courseList) { index, item ->

                    Card(
                        modifier = Modifier
                            .padding(8.dp)
                            .clickable {
                                Toast
                                    .makeText(
                                        context,
                                        courseList[index]?.courseName + " selected..",
                                        Toast.LENGTH_SHORT
                                    )
                                    .show()
                            },

                        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
                    ) {

                        Column(
                            modifier = Modifier
                                .padding(8.dp)
                                .fillMaxWidth()
                        ) {
                            Spacer(modifier = Modifier.width(5.dp))

                            courseList[index]?.courseName?.let {
                                Text(
                                    text = it,
                                    modifier = Modifier.padding(4.dp),
                                    color = Color.Green,
                                    textAlign = TextAlign.Center,
                                    style = TextStyle(
                                        fontSize = 20.sp, fontWeight = FontWeight.Bold
                                    )
                                )
                            }
                            Spacer(modifier = Modifier.height(5.dp))

                            courseList[index]?.courseDuration?.let {
                                Text(
                                    text = it,
                                    modifier = Modifier.padding(4.dp),
                                    color = Color.Black,
                                    textAlign = TextAlign.Center,
                                    style = TextStyle(
                                        fontSize = 15.sp
                                    )
                                )
                            }
                            Spacer(modifier = Modifier.width(5.dp))

                            courseList[index]?.courseDescription?.let {
                                Text(
                                    text = it,
                                    modifier = Modifier.padding(4.dp),
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
