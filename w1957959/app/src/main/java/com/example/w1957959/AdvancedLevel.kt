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
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.json.JSONObject
import java.io.IOException
import java.io.InputStream

class AdvancedLevel : ComponentActivity() {
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
        var allAnswersCorrectness by rememberSaveable {
            mutableStateOf("")
        }
        var threeRandomFlagsList by rememberSaveable {
            mutableStateOf(mutableListOf<String>()) // Saved three flags to fit with re-rendering
        }

        var attempts by rememberSaveable {
            mutableStateOf(3)
        }
        var score by rememberSaveable {
            mutableStateOf(0)
        }
        // Flag answers related variables declaration
        var textInput1 by rememberSaveable {
            mutableStateOf("")
        }
        var textInput2 by rememberSaveable {
            mutableStateOf("")
        }
        var textInput3 by rememberSaveable {
            mutableStateOf("")
        }
        var isTF1Editable by remember {
            mutableStateOf(false)
        }
        var isTF2Editable by remember {
            mutableStateOf(false)
        }
        var isTF3Editable by remember {
            mutableStateOf(false)
        }
        var textField1Color by remember {
            mutableStateOf(Color.Black)
        }
        var textField2Color by remember {
            mutableStateOf(Color.Black)
        }
        var textField3Color by remember {
            mutableStateOf(Color.Black)
        }
        var input1Correctness by remember {
            mutableStateOf("")
        }
        var input2Correctness by remember {
            mutableStateOf("")
        }
        var input3Correctness by remember {
            mutableStateOf("")
        }
        var correctFlagNameToDisplay1 by remember {
            mutableStateOf("")
        }
        var correctFlagNameToDisplay2 by remember {
            mutableStateOf("")
        }
        var correctFlagNameToDisplay3 by remember {
            mutableStateOf("")
        }

//---------------------------------------------------------
        // Read the JSON file
        val json_read = remember { readJsonFile() }
        // Get the array of keys of JSON
        saveKeysToList(json_read)
        // Get the array of values of JSON
        saveValuesToList(json_read)
        while (threeRandomFlagsList.size < 3) {
            val randomFlag = country_keys_json_list.random().toLowerCase()
            if (!threeRandomFlagsList.contains(randomFlag))
                threeRandomFlagsList.add(randomFlag)
        }
        val timer by remember { // Timer functionality setting up -------------------------------------
            mutableStateOf(object : CountDownTimer((timeCounter * 1000).toLong(), 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    timeCounter = (millisUntilFinished / 1000).toInt()
                }

                override fun onFinish() {
                    timeCounter = 0
                    --attempts
                    threeRandomFlagsList.forEachIndexed { index, flag ->
                        var json_obj =
                            JSONObject(json_read) // Fetch correct flag name from JSON
                        val correctFlagValue = json_obj.getString(flag.uppercase())

                        if (index == 0 && checkAnswers(
                                textInput1,
                                correctFlagValue
                            )
                        ) {

                            input1Correctness = "CORRECT!"
                            isTF1Editable = true
                            textField1Color = Color.Green
                        } else if (index == 0 && !checkAnswers(
                                textInput1,
                                correctFlagValue
                            )
                        ) {
                            input1Correctness = "WRONG!"
                            textField1Color = Color.Red
                            if (attempts <= 0) correctFlagNameToDisplay1 =
                                correctFlagValue
                        } else if (index == 1 && checkAnswers(
                                textInput2,
                                correctFlagValue
                            )
                        ) {

                            input2Correctness = "CORRECT!"
                            isTF2Editable = true
                            textField2Color = Color.Green
                        } else if (index == 1 && !checkAnswers(
                                textInput2,
                                correctFlagValue
                            )
                        ) {
                            input2Correctness = "WRONG!"
                            textField2Color = Color.Red
                            if (attempts <= 0) correctFlagNameToDisplay2 =
                                correctFlagValue
                        } else if (index == 2 && checkAnswers(
                                textInput3,
                                correctFlagValue
                            )
                        ) {

                            input3Correctness = "CORRECT!"
                            isTF3Editable = true
                            textField3Color = Color.Green
                        } else if (index == 2 && !checkAnswers(
                                textInput3,
                                correctFlagValue
                            )
                        ) {
                            input3Correctness = "WRONG!"
                            textField3Color = Color.Red
                            if (attempts <= 0) correctFlagNameToDisplay3 =
                                correctFlagValue
                        }


                    }

                    if (input1Correctness.equals("CORRECT!") && input2Correctness.equals(
                            "CORRECT!"
                        )
                        && input3Correctness.equals("CORRECT!")
                    ) {
                        score = 3
                        allAnswersCorrectness = "CORRECT!"
                    } else if (
                        attempts <= 0 &&
                        (input1Correctness.equals("WRONG!") || input2Correctness.equals(
                            "WRONG!"
                        )
                                || input3Correctness.equals("WRONG!"))
                    ) {
                        allAnswersCorrectness = "WRONG!"
                        attempts = 0
                        isTF1Editable = true
                        isTF2Editable = true
                        isTF3Editable = true
                        if (input1Correctness.equals("CORRECT!")) score++ // increasing the score based on guesses accuracy
                        if (input2Correctness.equals("CORRECT!")) score++
                        if (input3Correctness.equals("CORRECT!")) score++
                    }

                    if (attempts>0) timeCounter =10
                }
            })

        }

        Column {
            LazyColumn {

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
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(
                                modifier = Modifier
                                    .padding(10.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .width(100.dp)
                                    .height(35.dp)
                                    .background(Color.White)
//                            .align(Alignment.Start),

                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .fillMaxHeight(),
                                    horizontalArrangement = Arrangement.Center,
                                    verticalAlignment = Alignment.CenterVertically

                                ) {
                                    Text(
                                        text = "Attempts :",
                                        color = Color.Black,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = "${attempts}",
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                            if (timerChecked) {
                                if (timeCounter == 10) {
                                    timer.start() // Start the timer
                                }
                                Column(
                                    modifier = Modifier
                                        .width(100.dp)
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
                            Column(
                                modifier = Modifier
                                    .padding(10.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .width(100.dp)
                                    .height(35.dp)
                                    .background(Color.White)

                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .fillMaxHeight(),
                                    horizontalArrangement = Arrangement.Center,
                                    verticalAlignment = Alignment.CenterVertically

                                ) {
                                    Text(
                                        text = "Score :",
                                        color = Color.Black,
                                        fontWeight = FontWeight.Bold
                                    )
                                    var x = 0
                                    if (attempts <= 0 || allAnswersCorrectness.equals("CORRECT!"))
                                        x = score
                                    Text(
                                        text = "${x}",
                                        fontWeight = FontWeight.Bold
                                    )
                                }

                            }
                        }



                        Text(
                            modifier = Modifier.padding(16.dp),
                            text = "Guess The Country Name",
                            textAlign = TextAlign.Start,
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
                            if (allAnswersCorrectness.equals("CORRECT!"))
                                Text(
                                    text = allAnswersCorrectness,
                                    color = Color.Green,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            else if (allAnswersCorrectness.equals("WRONG!")) {
                                Text(
                                    text = allAnswersCorrectness,
                                    color = Color.Red,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        if (allAnswersCorrectness.equals("CORRECT!") || allAnswersCorrectness.equals(
                                "WRONG!"
                            )
                        )
                            Button(
                                onClick = {
                                    Intent(this@AdvancedLevel, AdvancedLevel::class.java).also {
                                        it.putExtra("timeChecked",timerChecked)
                                        startActivity(it)
                                    }
                                },
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
                        else
                            Button(
                                onClick = {
                                    --attempts
                                    if (attempts>0) timeCounter =10 // Set timer 10 for next attempt
                                    threeRandomFlagsList.forEachIndexed { index, flag ->
                                        var json_obj =
                                            JSONObject(json_read) // Fetch correct flag name from JSON
                                        val correctFlagValue = json_obj.getString(flag.uppercase())

                                        if (index == 0 && checkAnswers(
                                                textInput1,
                                                correctFlagValue
                                            )
                                        ) {

                                            input1Correctness = "CORRECT!"
                                            isTF1Editable = true
                                            textField1Color = Color.Green
                                        } else if (index == 0 && !checkAnswers(
                                                textInput1,
                                                correctFlagValue
                                            )
                                        ) {
                                            input1Correctness = "WRONG!"
                                            textField1Color = Color.Red
                                            if (attempts <= 0) correctFlagNameToDisplay1 =
                                                correctFlagValue
                                        } else if (index == 1 && checkAnswers(
                                                textInput2,
                                                correctFlagValue
                                            )
                                        ) {

                                            input2Correctness = "CORRECT!"
                                            isTF2Editable = true
                                            textField2Color = Color.Green
                                        } else if (index == 1 && !checkAnswers(
                                                textInput2,
                                                correctFlagValue
                                            )
                                        ) {
                                            input2Correctness = "WRONG!"
                                            textField2Color = Color.Red
                                            if (attempts <= 0) correctFlagNameToDisplay2 =
                                                correctFlagValue
                                        } else if (index == 2 && checkAnswers(
                                                textInput3,
                                                correctFlagValue
                                            )
                                        ) {

                                            input3Correctness = "CORRECT!"
                                            isTF3Editable = true
                                            textField3Color = Color.Green
                                        } else if (index == 2 && !checkAnswers(
                                                textInput3,
                                                correctFlagValue
                                            )
                                        ) {
                                            input3Correctness = "WRONG!"
                                            textField3Color = Color.Red
                                            if (attempts <= 0) correctFlagNameToDisplay3 =
                                                correctFlagValue
                                        }


                                    }

                                    if (input1Correctness.equals("CORRECT!") && input2Correctness.equals(
                                            "CORRECT!"
                                        )
                                        && input3Correctness.equals("CORRECT!")
                                    ) {
                                        score = 3
                                        allAnswersCorrectness = "CORRECT!"
                                    } else if (
                                        attempts <= 0 &&
                                        (input1Correctness.equals("WRONG!") || input2Correctness.equals(
                                            "WRONG!"
                                        )
                                                || input3Correctness.equals("WRONG!"))
                                    ) {
                                        allAnswersCorrectness = "WRONG!"
                                        isTF1Editable = true
                                        isTF2Editable = true
                                        isTF3Editable = true
                                        if (input1Correctness.equals("CORRECT!")) score++ // increasing the score based on guesses accuracy
                                        if (input2Correctness.equals("CORRECT!")) score++
                                        if (input3Correctness.equals("CORRECT!")) score++
                                    }
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(10.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color.White
                                )

                            ) {
                                Text(
                                    text = "Submit",
                                    color = Color.Black,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp
                                )
                            }

                    }
                    LazyRow {
                        item {
                            Row {
//First Image
                                Card(
                                    modifier = Modifier
                                        .padding(10.dp)
                                        .shadow(ambientColor = Color.DarkGray, elevation = 8.dp)
                                ) {
                                    val flagResourceId =
                                        resources.getIdentifier(
                                            threeRandomFlagsList[0],
                                            "drawable",
                                            packageName
                                        )
                                    Image(
                                        painter = painterResource(id = flagResourceId),
                                        contentDescription = null,
                                        modifier = Modifier.size(200.dp),
                                        contentScale = ContentScale.Inside

                                    )
//First TextField

                                    OutlinedTextField(

                                        value = textInput1,
                                        onValueChange = {
                                            textInput1 = it

                                        },
                                        textStyle = TextStyle(color = textField1Color),
                                        readOnly = isTF1Editable,
                                        label = { Text(text = "Guess") },
                                        maxLines = 1,
                                        modifier = Modifier
                                            .width(150.dp)
                                            .align(Alignment.CenterHorizontally),

                                        trailingIcon = {
                                            if (!textInput1.equals("") && !isTF1Editable) {
                                                Icon(
                                                    imageVector = Icons.Outlined.Clear,
                                                    contentDescription = null,
                                                    modifier = Modifier.clickable {
                                                        textInput1 = "" // Make text field empty
                                                    }
                                                )
                                            }
                                        }
                                    )

                                    Text(
                                        text = correctFlagNameToDisplay1,
                                        color = Color.Blue,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 15.sp,
                                        modifier = Modifier.align(Alignment.CenterHorizontally)
                                    )
                                }
//Second Image
                                Card(
                                    modifier = Modifier
                                        .padding(10.dp)
                                        .shadow(ambientColor = Color.DarkGray, elevation = 8.dp)
                                ) {
                                    val flagResourceId2 =
                                        resources.getIdentifier(
                                            threeRandomFlagsList[1],
                                            "drawable",
                                            packageName
                                        )
                                    Image(
                                        painter = painterResource(id = flagResourceId2),
                                        contentDescription = null,
                                        contentScale = ContentScale.Inside,
                                        modifier = Modifier.size(200.dp)
                                    )
//Second TextField

                                    OutlinedTextField(

                                        value = textInput2,
                                        onValueChange = {
                                            textInput2 = it

                                        },
                                        textStyle = TextStyle(color = textField2Color),
                                        readOnly = isTF2Editable,
                                        label = { Text(text = "Guess") },
                                        maxLines = 1,
                                        modifier = Modifier
                                            .width(150.dp)
                                            .align(Alignment.CenterHorizontally),

                                        trailingIcon = {
                                            if (!textInput2.equals("") && !isTF2Editable) {
                                                Icon(
                                                    imageVector = Icons.Outlined.Clear,
                                                    contentDescription = null,
                                                    modifier = Modifier.clickable {
                                                        textInput2 = "" // Make text field empty
                                                    }
                                                )
                                            }
                                        })
                                    Text(
                                        text = correctFlagNameToDisplay2,
                                        color = Color.Blue,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 15.sp,
                                        modifier = Modifier.align(Alignment.CenterHorizontally)
                                    )
                                }

//Third Image
                                Card(
                                    modifier = Modifier
                                        .padding(10.dp)
                                        .shadow(ambientColor = Color.DarkGray, elevation = 8.dp)
                                ) {
                                    val flagResourceId3 =
                                        resources.getIdentifier(
                                            threeRandomFlagsList[2],
                                            "drawable",
                                            packageName
                                        )
                                    Image(
                                        painter = painterResource(id = flagResourceId3),
                                        contentDescription = null,
                                        contentScale = ContentScale.Inside,
                                        modifier = Modifier.size(200.dp)
                                    )
//Third TextField

                                    OutlinedTextField(

                                        value = textInput3,
                                        onValueChange = {
                                            textInput3 = it

                                        },

                                        label = { Text(text = "Guess") },
                                        textStyle = TextStyle(color = textField3Color),
                                        maxLines = 1,
                                        readOnly = isTF3Editable,
                                        modifier = Modifier
                                            .width(150.dp)
                                            .align(Alignment.CenterHorizontally),

                                        trailingIcon = {
                                            if (!textInput3.equals("") && !isTF3Editable) {
                                                Icon(
                                                    imageVector = Icons.Outlined.Clear,
                                                    contentDescription = null,
                                                    modifier = Modifier.clickable {
                                                        textInput3 = "" // Make text field empty
                                                    }
                                                )
                                            }
                                        })
                                    Text(
                                        text = correctFlagNameToDisplay3,
                                        color = Color.Blue,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 15.sp,
                                        modifier = Modifier.align(Alignment.CenterHorizontally)
                                    )
                                }
                            }


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

    fun checkAnswers(enteredValue: String, actualValue: String): Boolean {
        return enteredValue.equals(actualValue, ignoreCase = true)
    }

}
