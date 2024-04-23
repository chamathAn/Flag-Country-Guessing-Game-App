
package com.example.w1957959

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            display()
        }
    }

    @Composable
    fun display() {
        var timerChecked = rememberSaveable {
            mutableStateOf(false)
        }


        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF2894F4)),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {


            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .paint(
                        painterResource(id = R.drawable.ideaman),
                        alignment = Alignment.BottomCenter
                    ),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                Button(
                    onClick = {
                        Intent(applicationContext, GuessTheCountry::class.java).also {
                            it.putExtra("timeChecked",timerChecked.value)
                            startActivity(it)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    colors = ButtonDefaults.buttonColors(Color.White)
                ) {
                    Text(text = "Guess the Country", color = Color.Black)
                }
                Button(
                    onClick = {
                        Intent(applicationContext, GuessHints::class.java).also {
                            it.putExtra("timeChecked",timerChecked.value)
                            startActivity(it)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    colors = ButtonDefaults.buttonColors(Color.White)
                ) {
                    Text(text = "Guess-Hints", color = Color.Black)
                }
                Button(
                    onClick = {
                        Intent(applicationContext, GuessTheFlag::class.java).also {
                            it.putExtra("timeChecked",timerChecked.value)
                            startActivity(it)
                        }
                    }, modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    colors = ButtonDefaults.buttonColors(Color.White)

                ) {
                    Text(text = "Guess the Flag", color = Color.Black)
                }
                Button(
                    onClick = {
                        Intent(applicationContext, AdvancedLevel::class.java).also {
                            it.putExtra("timeChecked",timerChecked.value)
                            startActivity(it)
                        }
                    }, modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    colors = ButtonDefaults.buttonColors(Color.White)

                ) {
                    Text(text = "Advanced Level", color = Color.Black)
                }
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    horizontalAlignment = Alignment.End
                ) {
                    Row(
                        modifier = Modifier
                            .border(
                                width = 1.dp,
                                color = Color.Transparent,
                                shape = RoundedCornerShape(15.dp)
                            )
                            .clip(RoundedCornerShape(15.dp))
                            .widthIn(150.dp)
                            .background(Color.White),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "Timer : ")
                        Switch(
                            checked = timerChecked.value,
                            onCheckedChange = {
                                timerChecked.value = it
                            },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.White,
                                checkedTrackColor = Color(0xFF2894F4),
                                uncheckedThumbColor = Color.White,
                                uncheckedTrackColor = Color(0xFF2894F4),
                            )
                        )
                    }
                }


            }

            Image(
                painter = painterResource(id = R.drawable.ideaman), contentDescription = null,
                alignment = Alignment.BottomEnd
            )
        }

    }
}


