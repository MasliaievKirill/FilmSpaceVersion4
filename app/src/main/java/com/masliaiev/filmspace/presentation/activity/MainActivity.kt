package com.masliaiev.filmspace.presentation.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.masliaiev.filmspace.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

class MainActivity : AppCompatActivity() {


    private lateinit var binding: ActivityMainBinding

    private val scope = CoroutineScope(Dispatchers.Main.immediate)


    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        binding.wvAccount.webViewClient = object : WebViewClient(){
//
//            override fun shouldOverrideUrlLoading(
//                view: WebView?,
//                request: WebResourceRequest?
//            ): Boolean {
//                val url = request?.url
//                Log.d("MyUrl", url.toString())
//                if (url.toString().contains("allow")){
//                    scope.launch {
//                        val sessionId = appRepositoryImpl.createSession(CreateSessionRequest(requestToken))
//                        Log.d("MySession", sessionId.sessionId)
//
//                    }
//                }
//                return false
//            }
//        }
//


    }

}