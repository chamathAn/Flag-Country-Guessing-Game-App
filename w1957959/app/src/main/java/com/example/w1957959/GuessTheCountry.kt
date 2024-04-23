package com.example.w1957959

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
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
import org.json.JSONObject
import java.io.IOException
import java.io.InputStream

class GuessTheCountry : ComponentActivity() {

    // arrays declaration
    var country_keys_json_list = mutableListOf<String>()
    var country_values_json_list = mutableListOf<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            display()
        }
    }

    @Composable
    fun display() {

        var timerChecked by rememberSaveable {
            mutableStateOf(intent.getBooleanExtra("timeChecked", false))
        }
        var timeCounter by rememberSaveable {
            mutableStateOf(10)
        }
        var isAnswerShown by rememberSaveable {
            mutableStateOf(false)
        }

        var answer_accuracy by rememberSaveable {
            mutableStateOf("")
        }
        var selectedIndex by rememberSaveable { mutableStateOf(-1) }

        // Read the JSON file
        var json_read = read_json_file()
        // Get the array of keys of JSON
        save_keys_to_list(json_read)
        //Get the array of values of JSON
        save_values_to_list(json_read)


        var random_flag_key by rememberSaveable {
            mutableStateOf(country_keys_json_list.random().toLowerCase())
        }

        var json_obj = JSONObject(json_read) // Fetch correct country name from JSON
        val correctCountryValue = json_obj.getString(random_flag_key.uppercase())

        var flagResourceId = resources.getIdentifier(random_flag_key, "drawable", packageName)

        val timer by remember { // Timer functionality setting up -------------------------------------
            mutableStateOf(object : CountDownTimer((timeCounter * 1000).toLong(), 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    timeCounter = (millisUntilFinished / 1000).toInt()
                }

                override fun onFinish() {
                    timeCounter = 0
                    isAnswerShown = true
                    answer_accuracy =
                        if (selectedIndex != -1)
                            check_answer(selectedIndex, correctCountryValue)
                        else "WRONG!"
                }
            })

        }



        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .fillMaxSize()
                .background(Color(0xFF2894F4))

        ) {
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
            Image(
                painter = painterResource(
                    id = flagResourceId
                ),
                contentDescription = "",
                Modifier
                    .size(300.dp)
            )
            LazyColumn(
                modifier = Modifier
                    .weight(1f) // Allow take available space to below composable
            ) {
                items(country_values_json_list) { i ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp, 10.dp)
                            .wrapContentHeight(align = Alignment.CenterVertically)
                            .selectable(
                                selected = selectedIndex == country_values_json_list.indexOf(i),
                                onClick = {
                                    selectedIndex =
                                        if (selectedIndex != country_values_json_list.indexOf(i))
                                            country_values_json_list.indexOf(i)
                                        else
                                            -1
                                }
                            )
                            .shadow(ambientColor = Color.DarkGray, elevation = 8.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White, //Card background color
                            contentColor = Color.Black  //Card content color
                        ),

                        shape = RoundedCornerShape(8.dp),
                    ) {

                        Column(
                            modifier = Modifier.padding(10.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = i,
                                    fontWeight = FontWeight.Bold
                                )
                                if (selectedIndex != -1 && country_values_json_list.indexOf(i) == selectedIndex) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = "Selected",
                                        tint = Color.Green,
                                        modifier = Modifier.size(20.dp),

                                        )

                                }
                            }

                        }

                    }

                }

            }

            if (answer_accuracy == "CORRECT!") {
                Text(
                    text = answer_accuracy,
                    color = Color.Green
                )
            } else if (answer_accuracy == "WRONG!") {

                Text(
                    text = answer_accuracy,
                    color = Color.Red
                )
                Text(
                    text = correctCountryValue,
                    color = Color.Blue
                )
            }
            if (isAnswerShown) {
                Button(
                    onClick = {
                        Intent(this@GuessTheCountry, GuessTheCountry::class.java).also {
                            it.putExtra("timeChecked",timerChecked)
                            startActivity(it) // Advancing to new GuessTheCountry instance
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                ) {
                    Text(
                        text = "Next",
                    )
                }
            } else {
                Button(
                    onClick = {
                        isAnswerShown = true
                        answer_accuracy =
                            if (selectedIndex != -1)
                                check_answer(selectedIndex, correctCountryValue)
                            else "WRONG!"
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)

                ) {
                    Text(text = "Submit", fontWeight = FontWeight.Bold)
                }
            }
        }
//        val timer = object: CountDownTimer(10000, 1000) {
//            override fun onTick(millisUntilFinished: Long) {
//                timeCounter--
//            }
//
//            override fun onFinish() {
//                timeCounter=0
//            }
//        }
//        timer.start()


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

    fun check_answer(selectedIndex: Int, randomCountryFlag: String): String {/*
    To check the answers accuracy
    @param: selectedIndex = selected flag index number in country_values_json_list
    @param: randomFlagIndex = shown flag index number in country_keys_json_list
    */

        if (country_values_json_list[selectedIndex] == randomCountryFlag) {
            return "CORRECT!"
        } else
            return "WRONG!"

    }

    fun countDownTimer(timer: Int) {

    }

}

