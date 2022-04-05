package com.masliaiev.filmspace.presentation.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.masliaiev.filmspace.FilmSpaceApp
import com.masliaiev.filmspace.data.repository.AppRepositoryImpl
import com.masliaiev.filmspace.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    private val component by lazy {
        (application as FilmSpaceApp).component
    }

    @Inject
    lateinit var appRepositoryImpl: AppRepositoryImpl

    private lateinit var binding: ActivityMainBinding

    private val scope = CoroutineScope(Dispatchers.Main.immediate)

    private var requestToken = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        component.inject(this)
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
//        binding.wvAccount.settings.javaScriptEnabled = true
//        binding.wvAccount.settings.javaScriptCanOpenWindowsAutomatically = true
//
//
//        scope.launch {
//            requestToken = appRepositoryImpl.createRequestToken().requestToken
//
//            binding.wvAccount.loadUrl("https://www.themoviedb.org/authenticate/$requestToken")
//
//
//
//
//        }

        //authenticate allow
        //authenticate deny


    }

}