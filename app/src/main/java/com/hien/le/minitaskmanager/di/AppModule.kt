package com.hien.le.minitaskmanager.di

import com.hien.le.minitaskmanager.ui.screens.task.TaskViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel { TaskViewModel() }
}
