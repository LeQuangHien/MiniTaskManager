package com.hien.le.minitaskmanager.data.datasource.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TaskEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val description: String,
    @ColumnInfo(name = "isCompleted", defaultValue = "false")
    val isCompleted: Boolean = false
)