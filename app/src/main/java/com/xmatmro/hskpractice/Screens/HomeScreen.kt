package com.xmatmro.hskpractice.Screens

import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.xmatmro.hskpractice.Components.SegmentedControl
import com.xmatmro.hskpractice.Components.SegmentedControlButton

@Composable
fun HomeScreen(
    onStartClick: (Int) -> Unit
){
    var level by rememberSaveable { mutableStateOf(1) }

    Surface(color = MaterialTheme.colorScheme.background) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .statusBarsPadding(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ){
            Text(
                "Witaj w HSK Practice!",
                color = MaterialTheme.colorScheme.primary,
                style= MaterialTheme.typography.headlineMedium,
                fontWeight = Bold
            )
            //Miał być dropdown, ale z pasji postanowił crashować aplikację
            HorizontalDivider(thickness = 1.dp, color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.2f))
            Column(
                modifier = Modifier,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text("Wybierz poziom HSK",
                    style=MaterialTheme.typography.titleLarge,
                    modifier = Modifier,
                    textAlign = TextAlign.Start,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                )
                SegmentedControl {
                    listOf<Int>(1,2,3,4,5,6).forEach { hskLevel ->
                        SegmentedControlButton(
                            onClick = { level = hskLevel },
                            text = hskLevel.toString(),
                            selected = level == hskLevel
                        )
                    }
                }
            }

            Button(
                onClick = { onStartClick(level) },
                modifier = Modifier.padding(vertical = 8.dp)
            ) {
                Text("Start", fontSize = 18.sp, modifier = Modifier.padding(4.dp))

            }
            AndroidView(
                modifier = Modifier
                    .width(250.dp)
                    .height(250.dp)
                    .clip(RoundedCornerShape(17.dp)),
                factory = { context ->
                    WebView(context).apply {
                        settings.javaScriptEnabled = true
                        settings.domStorageEnabled = true
                        settings.allowFileAccess = true
                        settings.allowContentAccess = true

                        settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW

                        WebView.setWebContentsDebuggingEnabled(true)
                        webChromeClient = WebChromeClient()
                        webViewClient = object : WebViewClient() {
                            override fun onPageFinished(view: WebView?, url: String?) {
                                view?.evaluateJavascript("drawCharacter('好')", null)
                            }
                        }
                        loadUrl("file:///android_asset/index.html")
                    }
                },
                update = { webView ->
                    webView.evaluateJavascript("drawCharacter('好')", null)
                }
            )
        }
    }


}


