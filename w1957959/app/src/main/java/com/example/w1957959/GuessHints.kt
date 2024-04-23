package com.example.w1957959

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.json.JSONObject
import java.io.IOException
import java.io.InputStream

class GuessHints : ComponentActivity() {
    // arrays declaration
    var country_keys_json_list = mutableListOf<String>()
    var country_values_json_list = mutableListOf<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            display()
        }
    }

    //    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun display() {

        // Read the JSON file
        var json_read = read_json_file()
        // Get the array of keys of JSON
        save_keys_to_list(json_read)
        //Get the array of values of JSON
        save_values_to_list(json_read)
        var timerChecked by rememberSaveable {
            mutableStateOf(intent.getBooleanExtra("timeChecked", false))
        }
        var timeCounter by rememberSaveable {
            mutableStateOf(10)
        }

        var textInput by rememberSaveable {
            mutableStateOf("")
        }
        var wrongGuessCount by rememberSaveable {
            mutableStateOf(0)
        }
        var random_flag_key by rememberSaveable {
            mutableStateOf(country_keys_json_list.random().toLowerCase())
        }
        var answerCorrectness by rememberSaveable {
            mutableStateOf("")
        }

        var json_obj = JSONObject(json_read) // Fetch correct country name from JSON
        val correctCountryValue = json_obj.getString(random_flag_key.uppercase())


        var dashedCountryName by rememberSaveable {
            mutableStateOf(correctCountryValue.replace(Regex("[a-zA-Z]"), "-"))
        }
        var flagResourceId = resources.getIdentifier(random_flag_key, "drawable", packageName)
        val timer by remember { // Timer functionality setting up -------------------------------------
            mutableStateOf(object : CountDownTimer((timeCounter * 1000).toLong(), 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    timeCounter = (millisUntilFinished / 1000).toInt()
                }

                override fun onFinish() {
                    timeCounter = 0
                    if (textInput.isNotEmpty()&&dashedCountryName.contains("-") && checkAnswers(
                            correctCountryValue,
                            textInput
                        )
                    ) {
                        var updatedDashedCountryName =
                            StringBuilder(dashedCountryName)
                        for (i in correctCountryValue.indices) {
                            if (correctCountryValue[i].equals(
                                    textInput[0], ignoreCase = true
                                )
                            )
                                updatedDashedCountryName.setCharAt(i, textInput[0])
                        }
                        dashedCountryName = updatedDashedCountryName.toString()

                    } else if (!(++wrongGuessCount < 3)) answerCorrectness =
                        "WRONG!"


                    if (!dashedCountryName.contains("-")) answerCorrectness =
                        "CORRECT!"

                    textInput = ""
                    if (wrongGuessCount<3&&timeCounter<=0) timeCounter = 10
                }
            })

        }//------------------------------

        Column {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxSize()

                    .background(Color(0xFF2894F4)),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                item {
                    if (timerChecked) {
                        if (timeCounter == 10) {
                            timer.start() // Start the timer
                        }
                        Column(
                            modifier = Modifier
                                .width(200.dp)
                                .padding(10.dp)
                                .clip(RoundedCornerShape(0.dp, 0.dp, 20.dp, 20.dp))
                                .shadow(8.dp, ambientColor = Color.LightGray)
                                .background(Color.White)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically

                            ) {

                                Text(
                                    text = "Timer :",
                                    color = Color.Black,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "${timeCounter}",
                                    fontWeight = FontWeight.Bold
                                )
                            }

                        }
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Image(
                        painter = painterResource(id = flagResourceId),
                        contentDescription = "Country Flag",
                        Modifier.size(300.dp)
                    )


                    Spacer(modifier = Modifier.height(50.dp))

                    Text(text = dashedCountryName, fontSize = 30.sp)
                    Spacer(modifier = Modifier.height(50.dp))


                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .shadow(8.dp, RoundedCornerShape(8.dp), ambientColor = Color.LightGray)
                            .fillMaxSize()
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color.White)
                            .heightIn(310.dp)
                    ) {
                        Spacer(modifier = Modifier.height(10.dp))


                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp),
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            Text(
                                text = "Enter your guess",
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp,
//                    textAlign = TextAlign.Start
                            )
                            OutlinedTextField(

                                value = textInput,
                                onValueChange = {
                                    if (textInput.length < 1) {
                                        textInput = it
                                    }

                                },
//                    label =  { Text(text = "Guess") },
                                label = { Text(text = "Guess") },
                                maxLines = 1,
                                modifier = Modifier
                                    .width(150.dp),

//                        .padding(4.dp)
//                        .background(Color.White)
//                        .border(1.dp, Color.White)
//                        .shadow(ambientColor = Color.LightGray, elevation = 2.dp),


//                    colors = TextFieldDefaults.textFieldColors(
//                        focusedLabelColor = Color.Black,
//
//                        focusedIndicatorColor = Color.Black,
//                        unfocusedLabelColor = Color.Black,
//                        containerColor = Color.White
//                    )

                                trailingIcon = {
                                    if (!textInput.equals("")) {
                                        Icon(
                                            imageVector = Icons.Outlined.Clear,
                                            contentDescription = null,
                                            modifier = Modifier.clickable {
                                                textInput = "" // Make text field empty
                                            }
                                        )
                                    }
                                }


                            )

                        }
                        if (answerCorrectness.equals("WRONG!") || answerCorrectness.equals("CORRECT!"))
                            Button(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(10.dp),
                                onClick = {
                                    Intent(this@GuessHints, GuessHints::class.java).also {
                                        it.putExtra("timeChecked",timerChecked)
                                        startActivity(it)
                                    }

                                },
//                    colors = ButtonDefaults.buttonColors(
//                        containerColor = Color.Black
//                    )
                            ) {
                                Text(text = "Next")
                            }
                        else
                            Button(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(10.dp),
                                onClick = {

                                    if (textInput.isNotEmpty()&&dashedCountryName.contains("-") && checkAnswers(
                                            correctCountryValue,
                                            textInput
                                        )
                                    ) {
                                        var updatedDashedCountryName =
                                            StringBuilder(dashedCountryName)
                                        for (i in correctCountryValue.indices) {
                                            if (correctCountryValue[i].equals(
                                                    textInput[0], ignoreCase = true
                                                )
                                            )
                                                updatedDashedCountryName.setCharAt(i, textInput[0])
                                        }
                                        dashedCountryName = updatedDashedCountryName.toString()

                                    } else if (!(++wrongGuessCount < 3)) answerCorrectness =
                                        "WRONG!"


                                    if (!dashedCountryName.contains("-")) answerCorrectness =
                                        "CORRECT!"

                                    textInput = ""
                                    if (wrongGuessCount<3) timeCounter = 10 // set timer 10 again for next attempt
                                },
//                    colors = ButtonDefaults.buttonColors(
//                        containerColor = Color.Black
//                    )
                            ) {
                                Text(text = "Submit")
                            }
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .padding(0.dp, 10.dp, 0.dp, 0.dp)
                                .fillMaxWidth()
                        ) {
                            if (answerCorrectness.equals("WRONG!")) {
                                Text(
                                    text = answerCorrectness,
                                    fontSize = 40.sp,
                                    color = Color.Red

                                )
                                Text(
                                    text = correctCountryValue,
                                    fontSize = 20.sp,
                                    color = Color.Blue
                                )
                            } else if (answerCorrectness.equals("CORRECT!")) {
                                Text(
                                    text = answerCorrectness,
                                    fontSize = 40.sp,
                                    color = Color.Green
                                )
                            }

                        }


                    }

                }
            }
        }
    }

    fun read_json_file(): String {/*
        To read the JSON file obj from the file
         */

        return try {
            var inputStream: InputStream = assets.open("countries.json") // Open JSON file
            inputStream.bufferedReader().use { it.readText() } // Read JSON file
        } catch (e: IOException) {
            e.printStackTrace()
            ""
        }
    }

    fun save_keys_to_list(json_read: String) {/*
        To save the all keys into the mutable list
        @param: json_read - raw json obj
         */

        var json_obj_keys = JSONObject(json_read).keys() // collect all keys
        while (json_obj_keys.hasNext()) { // add to list
            val key = json_obj_keys.next()
            country_keys_json_list.add(key)
        }
    }

    fun save_values_to_list(json_read: String) {/*
        To save the all values into the mutable list
        @param: json_read - raw json obj
         */

        var json_obj = JSONObject(json_read) // get the JSON content as a obj
        for (i in country_keys_json_list) {
            val country_value = json_obj.getString("${i}")
            country_values_json_list.add(country_value)
        }
    }

    fun checkAnswers(
        flagCountryName: String,
        enteredLetter: String
    ): Boolean {/*
        To check the answers correctness
        @param: flagCountryName - name of the country
        @param: enteredLetter - user entered letter
         */

        return flagCountryName.contains(enteredLetter, ignoreCase = true)
    }

}

