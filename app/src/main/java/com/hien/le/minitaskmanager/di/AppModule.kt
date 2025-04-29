package com.hien.le.minitaskmanager.di

import androidx.room.Room
import com.hien.le.minitaskmanager.data.datasource.database.AppDatabase
import com.hien.le.minitaskmanager.data.repository.TaskRepository
import com.hien.le.minitaskmanager.ui.screens.task.TaskViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single<AppDatabase> {
        Room.databaseBuilder(get(), AppDatabase::class.java, "mtm_database").build()
    }
    single { get<AppDatabase>().taskDao() }
    single { TaskRepository(get()) }

    viewModel { TaskViewModel(get()) }
}
