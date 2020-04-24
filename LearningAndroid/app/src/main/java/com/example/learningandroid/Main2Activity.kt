package com.example.learningandroid

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

class Main2Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        Log.d("onCreate()","Activity2 LifeCycle onCreate()")
    }

    override fun onStart() {
        super.onStart()
        Log.d("onStart()","Activity2 LifeCycle onStart()")
    }

    override fun onResume() {
        super.onResume()
        Log.d("onResume()","Activity2 LifeCycle onResume()")
    }

    override fun onPause() {
        super.onPause()
        Log.d("onPause()","Activity2 LifeCycle onPause()")
    }

    override fun onStop() {
        super.onStop()
        Log.d("onStop()","Activity2 LifeCycle onStop()")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("onDestroy()","Activity2 LifeCycle onDestroy()")
    }

    override fun onRestart() {
        super.onRestart()
        Log.d("onRestart()","Activity2 LifeCycle onRestart()")
    }


}
