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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.json.JSONObject
import java.io.IOException
import java.io.InputStream

class GuessTheFlag : ComponentActivity() {
    // arrays declaration
    var country_keys_json_list = mutableListOf<String>()
    var country_values_json_list = mutableListOf<String>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Display()
        }
    }

    @Composable
    fun Display() {
        var timerChecked by rememberSaveable {
            mutableStateOf(intent.getBooleanExtra("timeChecked", false))
        }
        var timeCounter by rememberSaveable {
            mutableStateOf(10)
        }
        var oneAttemptAllowed by rememberSaveable {
            mutableStateOf(true)
        }

        var nextButtonEnable by rememberSaveable {
            mutableStateOf(false)
        }
        var answerCorrectness by rememberSaveable {
            mutableStateOf("")
        }
        var threeRandomFlagsList by rememberSaveable {
            mutableStateOf(mutableListOf<String>()) // Saved three flags to fit with re-rendering
        }

        var selectedFlag by rememberSaveable {
            mutableStateOf(-1)
        }

        // Read the JSON file
        val json_read = rememberSaveable { readJsonFile() }
        // Get the array of keys of JSON
        saveKeysToList(json_read)
        // Get the array of values of JSON
        saveValuesToList(json_read)

        while (threeRandomFlagsList.size < 3) {
            val randomFlag = country_keys_json_list.random().toLowerCase()
            if (!threeRandomFlagsList.contains(randomFlag))
                threeRandomFlagsList.add(randomFlag)
        }

        var randomFlagKeyDisplayedOnScreen by rememberSaveable {
            mutableStateOf(threeRandomFlagsList.random()) // Take random flag that shown on screen
        }

        var json_obj = JSONObject(json_read) // Fetch correct flag name from JSON
        val correctFlagValue = json_obj.getString(randomFlagKeyDisplayedOnScreen.uppercase())

        var selectedFlagFileName by rememberSaveable {
            mutableStateOf("")
        }
        val timer by remember { // Timer functionality setting up -------------------------------------
            mutableStateOf(object : CountDownTimer((timeCounter * 1000).toLong(), 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    timeCounter = (millisUntilFinished / 1000).toInt()
                }

                override fun onFinish() {
                    timeCounter = 0
                    selectedFlag =
                        if (selectedFlag != threeRandomFlagsList.indexOf(selectedFlagFileName))
                            threeRandomFlagsList.indexOf(selectedFlagFileName)
                        else
                            -1
                    answerCorrectness = if (
                        checkAnswer(
                            selectedFlagFileName,
                            randomFlagKeyDisplayedOnScreen
                        )
                    ) "CORRECT!"
                    else {

                        "WRONG!"
                    }

                    nextButtonEnable = true
                    oneAttemptAllowed = false
                }
            })

        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                item {

                    Column(
                        verticalArrangement = Arrangement.SpaceBetween,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(450.dp)
                            .clip(RoundedCornerShape(0.dp, 0.dp, 20.dp, 20.dp))
                            .shadow(8.dp, ambientColor = Color.LightGray)
                            .weight(1f)
                            .background(Color(0xFF2894F4))
                        //                    .fillMaxHeight()

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
                        Text(
                            modifier = Modifier.padding(16.dp),
                            text = "Guess The Flag",
                            textAlign = TextAlign.Center,
                            fontSize = 30.sp,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(70.dp)
                                .background(Color.White),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            if (answerCorrectness.equals("CORRECT!"))
                                Text(
                                    text = answerCorrectness,
                                    color = Color.Green,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            else if (answerCorrectness.equals("WRONG!")) {
                                Text(
                                    text = answerCorrectness,
                                    color = Color.Red,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        Button(
                            onClick = {
                                Intent(this@GuessTheFlag, GuessTheFlag::class.java).also {
                                    it.putExtra("timeChecked", timerChecked)
                                    startActivity(it)
                                }
                            },
                            enabled = nextButtonEnable,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.White
                            )

                        ) {
                            Text(
                                text = "Next",
                                color = Color.Black,
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp
                            )
                        }

                    }
                    //            Spacer(modifier = Modifier.height(30.dp))
                    Text(
                        modifier = Modifier
                            .padding(0.dp, 10.dp, 0.dp, 0.dp)
                            .align(Alignment.CenterHorizontally),
                        text = correctFlagValue,
                        textAlign = TextAlign.Center,
                        fontSize = 20.sp,
                        color = Color.Black,
                        fontWeight = FontWeight.Bold
                    )
                    LazyRow(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .size(280.dp)
                    ) {
                        items(threeRandomFlagsList) { i ->
                            val flagResourceId =
                                resources.getIdentifier(i, "drawable", packageName)
                            Image(
                                painter = painterResource(id = flagResourceId),
                                contentDescription = null,
                                modifier = Modifier
                                    .padding(10.dp, 10.dp)

                                    .clickable(
                                        onClick = {
                                            selectedFlagFileName = i
                                            selectedFlag =
                                                if (selectedFlag != threeRandomFlagsList.indexOf(i))
                                                    threeRandomFlagsList.indexOf(i)
                                                else
                                                    -1
                                            answerCorrectness = if (
                                                checkAnswer(
                                                    i,
                                                    randomFlagKeyDisplayedOnScreen
                                                )
                                            ) "CORRECT!"
                                            else {

                                                "WRONG!"
                                            }

                                            nextButtonEnable = true
                                            oneAttemptAllowed = false
                                        },
                                        enabled = oneAttemptAllowed
                                    )
                                    .shadow(8.dp, ambientColor = Color.DarkGray),

                                )

                        }

                    }


                }
            }
        }
    }

    fun readJsonFile(): String {
        /*
        To read the JSON file obj from the file
         */
        return try {
            val inputStream: InputStream = assets.open("countries.json") // Open JSON file
            inputStream.bufferedReader().use { it.readText() } // Read JSON file
        } catch (e: IOException) {
            e.printStackTrace()
            ""
        }
    }

    fun saveKeysToList(json_read: String) {
        /*
        To save the all keys into the mutable list
        @param: json_read - raw json obj
         */
        val json_obj_keys = JSONObject(json_read).keys() // collect all keys
        while (json_obj_keys.hasNext()) { // add to list
            val key = json_obj_keys.next()
            country_keys_json_list.add(key)
        }
    }

    fun saveValuesToList(json_read: String) {
        /*
        To save the all values into the mutable list
        @param: json_read - raw json obj
         */
        val json_obj = JSONObject(json_read) // get the JSON content as a obj
        for (i in country_keys_json_list) {
            val country_value = json_obj.getString("${i}")
            country_values_json_list.add(country_value)
        }
    }


    fun checkAnswer(selectedFlagName: String, shownFlagName: String): Boolean {
        return selectedFlagName == shownFlagName
    }

}
