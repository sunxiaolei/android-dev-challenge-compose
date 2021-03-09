/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.androiddevchallenge.timer

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandIn
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.androiddevchallenge.R
import com.example.androiddevchallenge.ui.theme.MyTheme
import com.example.androiddevchallenge.ui.theme.teal200
import com.example.androiddevchallenge.ui.theme.teal500
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.TimeUnit

/**
 * @Description:
 * @author sun
 */
@ExperimentalAnimationApi
class TimerActivity : AppCompatActivity() {

    private val viewModel: TimerViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { App() }
    }

    @Preview
    @Composable
    fun App() {
        MyTheme {
            Scaffold(
                topBar = { TopAppBar(title = { Text(text = "Timer") }) },
                content = {
                    Crossfade(targetState = viewModel.currentPage) { screen ->
                        when (screen) {
                            PageEnum.Edit -> Timer()
                            PageEnum.Countdown -> CountdownPage()
                        }
                    }
                }
            )
        }
    }

    @Composable
    fun Timer() {
        Column(modifier = Modifier.fillMaxHeight()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 50.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                TimeStr(viewModel.hour, TypeEnum.Hour)
                Text(text = ":", fontSize = 50.sp)
                TimeStr(viewModel.minite, TypeEnum.Minite)
                Text(text = ":", fontSize = 50.sp)
                TimeStr(viewModel.second, TypeEnum.Second)
            }
            Spacer(modifier = Modifier.height(40.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                NumBtn(num = 1)
                NumBtn(num = 2)
                NumBtn(num = 3)
            }
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                NumBtn(num = 4)
                NumBtn(num = 5)
                NumBtn(num = 6)
            }
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                NumBtn(num = 7)
                NumBtn(num = 8)
                NumBtn(num = 9)
            }
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                Spacer(
                    modifier = Modifier
                        .padding(horizontal = 20.dp)
                        .height(60.dp)
                        .width(60.dp)
                )
                NumBtn(num = 0)
                Button(
                    onClick = { clean() },
                    modifier = Modifier
                        .padding(horizontal = 20.dp)
                        .height(60.dp)
                        .width(60.dp)
                        .clip(RoundedCornerShape(30.dp)),
                ) {
                    Text("DEL")
                }
            }
            Spacer(modifier = Modifier.height(40.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                AnimatedVisibility(
                    modifier = Modifier.wrapContentSize(),
                    visible = viewModel.showStartBtn,
                    enter =
                    expandIn(
                        expandFrom = Alignment.Center,
                        initialSize = { IntSize(0, 0) },
                        animationSpec = tween(durationMillis = 500)
                    )
                ) {

                    StartBtn()
                }
            }
        }
    }

    private fun formatTime(t: Long): String {
        return if (t < 10) "0$t" else t.toString()
    }

    @Composable
    fun TimeStr(t: Int, typeEnum: TypeEnum) {
        val timeStr = formatTime(t.toLong())
        Column(
            modifier = Modifier
                .padding(horizontal = 5.dp)
                .clip(shape = RoundedCornerShape(4.dp))
                .clickable {
                    viewModel.currentSelectedType = typeEnum
                },
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                modifier = Modifier.padding(horizontal = 5.dp),
                text = timeStr,
                fontSize = 50.sp,
                color = if (viewModel.currentSelectedType == typeEnum) teal200 else Color.White,
            )
            Text(
                text = if (typeEnum == TypeEnum.Hour) "H" else if (typeEnum == TypeEnum.Minite) "M" else "S",
                fontSize = 13.sp,
                color = if (viewModel.currentSelectedType == typeEnum) teal200 else Color.White,
            )
        }
    }

    @Composable
    fun NumBtn(num: Int) {
        Button(
            onClick = { changeNum(num) },
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .height(60.dp)
                .width(60.dp)
                .clip(RoundedCornerShape(30.dp))
                .background(Color.Transparent),
        ) {
            Text(text = num.toString(), fontSize = 30.sp)
        }
    }

    @Composable
    fun StartBtn() {
        val backgroundShape: Shape = RoundedCornerShape(40.dp)
        Text(
            modifier =
            Modifier
                .padding(6.dp)
                .width(80.dp)
                .height(80.dp)
                .shadow(3.dp, shape = backgroundShape)
                .clip(backgroundShape)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            teal200,
                            teal500,
                        ),
                        startY = 0f,
                        endY = 80f
                    )
                )
                .clickable {
                    viewModel.currentPage = PageEnum.Countdown
                    viewModel.countdownTime = calcTime()
                    start()
                }
                .wrapContentSize(),
            text = "START",
            style = typography.body1.copy(color = Color.White),
            textAlign = TextAlign.Center,
        )
    }

    private fun changeNum(num: Int) {
        when (viewModel.currentSelectedType) {
            TypeEnum.Hour -> {
                val hour = viewModel.hour
                if (hour == 0) {
                    viewModel.hour = num
                }
                if (hour in 1..9) {
                    viewModel.hour = hour * 10 + num
                }
            }
            TypeEnum.Minite -> {
                val minite = viewModel.minite
                if (minite == 0) {
                    viewModel.minite = num
                }
                if (minite in 1..9) {
                    viewModel.minite = minite * 10 + num
                }
            }
            TypeEnum.Second -> {
                val second = viewModel.second
                if (second == 0) {
                    viewModel.second = num
                }
                if (second in 1..9) {
                    viewModel.second = second * 10 + num
                }
            }
        }
        if ((viewModel.hour > 0 || viewModel.minite > 0 || viewModel.second > 0) && !viewModel.showStartBtn) {
            viewModel.showStartBtn = true
        }
    }

    private fun calcTime(): Int {
        return viewModel.hour * 3600 + viewModel.minite * 60 + viewModel.second
    }

    private fun clean() {
        viewModel.hour = 0
        viewModel.minite = 0
        viewModel.second = 0
        viewModel.showStartBtn = false
    }

    @Composable
    fun CountdownPage() {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .wrapContentSize()
                    .padding(top = 50.dp),
                contentAlignment = Alignment.Center
            ) {
                Canvas(
                    modifier = Modifier
                        .width(300.dp)
                        .height(300.dp)
                ) {
                    val canvasWidth = size.width
                    val canvasHeight = size.height
                    drawCircle(
                        color = Color.Gray,
                        center = Offset(x = canvasWidth / 2, y = canvasHeight / 2),
                        radius = size.minDimension / 2,
                        style = Stroke(width = 5f)
                    )
                }
                Text(
                    text = lastTime(),
                    fontSize = 60.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Row {
                IconButton(
                    onClick = {
                        viewModel.stop = true
                        viewModel.countdownTime = 0
                        viewModel.passTime = 0
                        clean()
                        viewModel.currentPage = PageEnum.Edit
                    },
                    modifier = Modifier
                        .padding(horizontal = 10.dp)
                        .height(80.dp)
                        .width(80.dp)
                        .clip(RoundedCornerShape(40.dp)),
                ) {
                    val icRefresh: Painter = painterResource(R.drawable.ic_refresh)
                    Icon(painter = icRefresh, contentDescription = "ic_refresh")
                }
                AnimatedVisibility(visible = !viewModel.complete) {
                    IconButton(
                        onClick = {
                            viewModel.stop = !viewModel.stop
                            if (!viewModel.stop) {
                                start()
                            }
                        },
                        modifier = Modifier
                            .padding(horizontal = 10.dp)
                            .height(80.dp)
                            .width(80.dp)
                            .clip(RoundedCornerShape(40.dp)),
                    ) {
                        val backIcon: Painter =
                            painterResource(if (viewModel.stop) R.drawable.ic_play else R.drawable.ic_pause)
                        Icon(painter = backIcon, contentDescription = "ic_opr")
                    }
                }
            }
        }
    }

    private fun lastTime(): String {
        return if (viewModel.complete) {
            "Complete!"
        } else {
            val sec = (viewModel.countdownTime - viewModel.passTime).toLong()
            val hours = TimeUnit.SECONDS.toHours(sec)
            val minutes = TimeUnit.SECONDS.toMinutes(sec) - hours * 60
            val seconds = TimeUnit.SECONDS.toSeconds(sec) - hours * 3600 - minutes * 60
            "${formatTime(hours)}:${formatTime(minutes)}:${formatTime(seconds)}"
        }
    }

    private fun start() {
        viewModel.complete = false
        viewModel.stop = false
        Flowable.intervalRange(0, 10, 1, 1, TimeUnit.SECONDS)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .takeWhile { !viewModel.stop }
            .subscribe {
                viewModel.passTime += 1
                if (viewModel.passTime >= viewModel.countdownTime) {
                    viewModel.complete = true
                }
            }
    }
}
