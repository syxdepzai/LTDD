package com.example.bt10.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.bt10.models.Task
import com.example.bt10.models.User

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "TodoListDB"

        // Table Users
        private const val TABLE_USERS = "users"
        private const val COLUMN_USER_ID = "id"
        private const val COLUMN_USERNAME = "username"
        private const val COLUMN_PASSWORD = "password"
        private const val COLUMN_EMAIL = "email"

        // Table Tasks
        private const val TABLE_TASKS = "tasks"
        private const val COLUMN_TASK_ID = "id"
        private const val COLUMN_TASK_USER_ID = "user_id"
        private const val COLUMN_TASK_TITLE = "title"
        private const val COLUMN_TASK_DESCRIPTION = "description"
        private const val COLUMN_TASK_DATE = "date"
        private const val COLUMN_TASK_COMPLETED = "is_completed"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        // Create Users table
        val createUsersTable = """
            CREATE TABLE $TABLE_USERS (
                $COLUMN_USER_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_USERNAME TEXT UNIQUE NOT NULL,
                $COLUMN_PASSWORD TEXT NOT NULL,
                $COLUMN_EMAIL TEXT NOT NULL
            )
        """.trimIndent()

        // Create Tasks table
        val createTasksTable = """
            CREATE TABLE $TABLE_TASKS (
                $COLUMN_TASK_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_TASK_USER_ID INTEGER NOT NULL,
                $COLUMN_TASK_TITLE TEXT NOT NULL,
                $COLUMN_TASK_DESCRIPTION TEXT,
                $COLUMN_TASK_DATE TEXT NOT NULL,
                $COLUMN_TASK_COMPLETED INTEGER DEFAULT 0,
                FOREIGN KEY($COLUMN_TASK_USER_ID) REFERENCES $TABLE_USERS($COLUMN_USER_ID)
            )
        """.trimIndent()

        db?.execSQL(createUsersTable)
        db?.execSQL(createTasksTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_TASKS")
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_USERS")
        onCreate(db)
    }

    // User operations
    fun registerUser(user: User): Long {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_USERNAME, user.username)
            put(COLUMN_PASSWORD, user.password)
            put(COLUMN_EMAIL, user.email)
        }
        val result = db.insert(TABLE_USERS, null, values)
        db.close()
        return result
    }

    fun loginUser(username: String, password: String): User? {
        val db = this.readableDatabase
        val cursor: Cursor = db.query(
            TABLE_USERS,
            arrayOf(COLUMN_USER_ID, COLUMN_USERNAME, COLUMN_PASSWORD, COLUMN_EMAIL),
            "$COLUMN_USERNAME = ? AND $COLUMN_PASSWORD = ?",
            arrayOf(username, password),
            null, null, null
        )

        var user: User? = null
        if (cursor.moveToFirst()) {
            user = User(
                id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_USER_ID)),
                username = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USERNAME)),
                password = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PASSWORD)),
                email = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMAIL))
            )
        }
        cursor.close()
        db.close()
        return user
    }

    fun isUsernameExists(username: String): Boolean {
        val db = this.readableDatabase
        val cursor = db.query(
            TABLE_USERS,
            arrayOf(COLUMN_USER_ID),
            "$COLUMN_USERNAME = ?",
            arrayOf(username),
            null, null, null
        )
        val exists = cursor.count > 0
        cursor.close()
        db.close()
        return exists
    }

    // Task operations
    fun addTask(task: Task): Long {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_TASK_USER_ID, task.userId)
            put(COLUMN_TASK_TITLE, task.title)
            put(COLUMN_TASK_DESCRIPTION, task.description)
            put(COLUMN_TASK_DATE, task.date)
            put(COLUMN_TASK_COMPLETED, if (task.isCompleted) 1 else 0)
        }
        val result = db.insert(TABLE_TASKS, null, values)
        db.close()
        return result
    }

    fun getAllTasks(userId: Int): List<Task> {
        val taskList = mutableListOf<Task>()
        val db = this.readableDatabase
        val cursor: Cursor = db.query(
            TABLE_TASKS,
            null,
            "$COLUMN_TASK_USER_ID = ?",
            arrayOf(userId.toString()),
            null, null,
            "$COLUMN_TASK_DATE DESC"
        )

        if (cursor.moveToFirst()) {
            do {
                val task = Task(
                    id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_TASK_ID)),
                    userId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_TASK_USER_ID)),
                    title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TASK_TITLE)),
                    description = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TASK_DESCRIPTION)),
                    date = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TASK_DATE)),
                    isCompleted = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_TASK_COMPLETED)) == 1
                )
                taskList.add(task)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return taskList
    }

    fun updateTask(task: Task): Int {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_TASK_TITLE, task.title)
            put(COLUMN_TASK_DESCRIPTION, task.description)
            put(COLUMN_TASK_DATE, task.date)
            put(COLUMN_TASK_COMPLETED, if (task.isCompleted) 1 else 0)
        }
        val result = db.update(
            TABLE_TASKS,
            values,
            "$COLUMN_TASK_ID = ?",
            arrayOf(task.id.toString())
        )
        db.close()
        return result
    }

    fun deleteTask(taskId: Int): Int {
        val db = this.writableDatabase
        val result = db.delete(
            TABLE_TASKS,
            "$COLUMN_TASK_ID = ?",
            arrayOf(taskId.toString())
        )
        db.close()
        return result
    }

    fun toggleTaskCompletion(taskId: Int): Int {
        val db = this.writableDatabase
        val cursor = db.query(
            TABLE_TASKS,
            arrayOf(COLUMN_TASK_COMPLETED),
            "$COLUMN_TASK_ID = ?",
            arrayOf(taskId.toString()),
            null, null, null
        )

        var result = 0
        if (cursor.moveToFirst()) {
            val currentStatus = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_TASK_COMPLETED))
            val newStatus = if (currentStatus == 1) 0 else 1

            val values = ContentValues().apply {
                put(COLUMN_TASK_COMPLETED, newStatus)
            }
            result = db.update(
                TABLE_TASKS,
                values,
                "$COLUMN_TASK_ID = ?",
                arrayOf(taskId.toString())
            )
        }
        cursor.close()
        db.close()
        return result
    }
}

