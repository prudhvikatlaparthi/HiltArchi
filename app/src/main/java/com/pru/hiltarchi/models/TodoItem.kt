package com.pru.hiltarchi.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TodoItem(
    val completed: Boolean?,
    val id: Int?,
    val title: String?,
    val userId: Int?
) : Parcelable