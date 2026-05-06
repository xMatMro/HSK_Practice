package com.xmatmro.hskpractice.Screens

import android.webkit.JavascriptInterface
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.xmatmro.hskpractice.Components.loadHSKData
import com.xmatmro.hskpractice.HSKCharacters.HSKCharactersClass

class WebAppInterface(
    private val onTaskCompleteCallback: () -> Unit,
    private val onFinish: () -> Unit,
    private val onIndexChangeCallback: () -> Unit
) {
    private val handler = android.os.Handler(android.os.Looper.getMainLooper())

    @JavascriptInterface
    fun onTaskComplete() {
        handler.post { onTaskCompleteCallback() }
    }

    @JavascriptInterface
    fun onExerciseFinished() {
        handler.post { onFinish() }
    }

    @JavascriptInterface
    fun onIndexChange(){
        handler.post { onIndexChangeCallback() }
    }
}


@Composable
fun TestDrawingScreen(
    level: Int,
    amount: Int,
    difficulty: Int,
    back: () -> Unit
){
    val context = LocalContext.current
    var characterList by remember{mutableStateOf<List<HSKCharactersClass>>(emptyList())}
    var currentTask by remember{mutableStateOf(0)}
    var currentIndex by remember { mutableStateOf(0) }


    LaunchedEffect(level) {
        val loadedData = loadHSKData(context,level)
        if(loadedData.isNotEmpty()){
            characterList = loadedData.shuffled().take(amount.coerceAtMost(loadedData.size))
        }

    }
    fun increment(){
        if(currentTask < characterList.size-1){
            currentTask += 1
        }
        currentIndex = 0
    }
    Surface(color = MaterialTheme.colorScheme.background){
        if(characterList.isEmpty()){
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        else{
            Column(
                modifier = Modifier
                    .statusBarsPadding()
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text="Zadanie ${currentTask+1}/${amount}",
                    style = MaterialTheme.typography.headlineSmall,
                )

                Text(
                    text = if (currentTask < characterList.size) characterList[currentTask].pinyin else "",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(16.dp)
                )

                Text(
                    text = if(currentTask<characterList.size) "Znak ${currentIndex + 1}/${characterList[currentTask].hanzi.length}" else "Znak 0/0",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(bottom=16.dp)
                )

                AndroidView(
                    modifier = Modifier
                        .width(250.dp)
                        .height(250.dp)
                        .clip(RoundedCornerShape(17.dp)),
                    factory = { factoryContext ->
                        WebView(factoryContext).apply {
                            settings.javaScriptEnabled = true
                            settings.domStorageEnabled = true
                            settings.allowFileAccess = true
                            settings.allowContentAccess = true

                            settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW

                            addJavascriptInterface(WebAppInterface(
                                onTaskCompleteCallback = {
                                    increment()
                                },
                                onFinish = {
                                    back()
                                },
                                onIndexChangeCallback = {
                                    currentIndex++
                                }
                            ),"Android")
                            WebView.setWebContentsDebuggingEnabled(true)
                            webChromeClient = WebChromeClient()
                            webViewClient = object : WebViewClient() {
                                override fun onPageFinished(view: WebView?, url: String?) {
                                    val jsArray = characterList.map {it.hanzi}.joinToString ("','", prefix = "['",postfix = "']")
                                    view?.evaluateJavascript("begin(${jsArray},${difficulty})", null)
                                }
                            }
                            loadUrl("file:///android_asset/index.html")
                        }
                    })
            }
        }

    }

}