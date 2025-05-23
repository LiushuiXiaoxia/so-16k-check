package com.example.nlog

class NativeLib {

    /**
     * A native method that is implemented by the 'nlog' native library,
     * which is packaged with this application.
     */
    external fun stringFromJNI(): String

    external fun nativeTest()

    companion object {
        // Used to load the 'nlog' library on application startup.
        init {
            System.loadLibrary("nlog")
        }
    }
}