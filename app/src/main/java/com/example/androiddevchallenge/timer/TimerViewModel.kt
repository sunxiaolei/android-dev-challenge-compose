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

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

/**
 * @Description: TimeViewModel
 * @author sun
 */
class TimerViewModel : ViewModel() {

    var hour: Int by mutableStateOf(0)
    var minite: Int by mutableStateOf(0)
    var second: Int by mutableStateOf(0)

    var showStartBtn: Boolean by mutableStateOf(false)
    var stop: Boolean by mutableStateOf(false)
    var complete: Boolean by mutableStateOf(false)

    var countdownTime: Int by mutableStateOf(0)
    var passTime: Int by mutableStateOf(0)

    var currentSelectedType: TypeEnum? by mutableStateOf(null)
    var currentPage: PageEnum? by mutableStateOf(PageEnum.Edit)
}

enum class TypeEnum {
    Hour, Minite, Second
}

enum class PageEnum {
    Edit, Countdown
}
