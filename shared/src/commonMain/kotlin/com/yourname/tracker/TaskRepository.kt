package com.yourname.tracker

import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.firestore.firestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TaskRepository {
    private val db = Firebase.firestore
    private val tasksCollection = db.collection("tasks")

    // Исправлено: добавлено <List<Task>>
    fun getTasks(): Flow<List<Task>> {
        return tasksCollection.snapshots.map { snapshot ->
            snapshot.documents.map { document ->
                // Исправлено: добавлено <Task>
                document.data<Task>()
            }
        }
    }

    suspend fun saveTask(task: Task) {
        val document = if (task.id.isEmpty()) {
            tasksCollection.document
        } else {
            tasksCollection.document(task.id)
        }

        val taskToSave = task.copy(id = document.id)
        document.set(taskToSave)
    }
}