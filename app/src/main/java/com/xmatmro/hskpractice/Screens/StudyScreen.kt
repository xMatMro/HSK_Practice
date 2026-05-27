package com.xmatmro.hskpractice.Screens

import android.content.Context
import android.speech.tts.TextToSpeech
import android.util.Log
import android.webkit.JavascriptInterface
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.rounded.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.SearchBarDefaults.InputField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.xmatmro.hskpractice.Components.loadHSKData
import com.xmatmro.hskpractice.HSKCharacters.HSKCharactersClass
import com.xmatmro.hskpractice.Utils.rememberTextToSpeech
import java.util.Locale

class StudyScreenWebAppInterface(
    private val context: Context
){
    @JavascriptInterface
    fun getAssetData(char: String): String? {
        return try {
            context.assets.open("data/$char.json").bufferedReader().use { it.readText() }
        } catch (e: Exception) {
            Log.e("WebView", "Error loading char data for $char", e)
            null
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudyScreen(
    level: Int,
    back: () -> Unit,
){
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var charactersList by remember { mutableStateOf<List<HSKCharactersClass>>(emptyList()) }
    var searchQuery by rememberSaveable { mutableStateOf("") }
    var expanded by rememberSaveable { mutableStateOf(false) }

    val filteredList = remember(searchQuery, charactersList) {
        if (searchQuery.isBlank()) charactersList
        else {
            val query = searchQuery.normalizePinyin()
            charactersList.filter { item ->
                item.hanzi.contains(searchQuery) ||
                        item.pinyin.normalizePinyin().contains(query) ||
                        item.translations.any { it.normalizePinyin().contains(query) }
            }
        }
    }
    LaunchedEffect(level) {
        charactersList = loadHSKData(context, level)
    }
    Surface(color = MaterialTheme.colorScheme.background) {
        Box(modifier = Modifier.fillMaxSize()){
            val backgroundGradientColor = MaterialTheme.colorScheme.primaryContainer
            val colorStops = arrayOf(
                0.0f to Color.Transparent,
                0.2f to backgroundGradientColor.copy(alpha = 0.4f),
                0.5f to backgroundGradientColor.copy(alpha = 0.9f),
                0.8f to backgroundGradientColor.copy(alpha = 0.4f),
                1f to Color.Transparent
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .blur(60.dp)
                    .drawBehind {
                        drawRect(
                            brush = Brush.verticalGradient(
                                colorStops = colorStops,
                                startY = 0f,
                                endY = size.height,

                                ),
                            topLeft = Offset(size.width / 2 - size.width / 4, 0f),
                            size = Size(size.width / 2, size.height)
                        )
                    }
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .statusBarsPadding(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Learning",
                    style=MaterialTheme.typography.headlineLarge,
                    modifier = Modifier.padding(16.dp),
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                    )
                HorizontalDivider(
                    thickness = 1.dp,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.2f)
                )
                var settingsView by rememberSaveable {mutableStateOf(false) }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ){

                    Button(
                        onClick = {back()},
                        modifier = Modifier,
                        shape = RoundedCornerShape(20.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.6f)
                        )

                    ) {
                        Icon(
                            Icons.Default.Home,
                            contentDescription = "Settings",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Button(
                        onClick = {settingsView=!settingsView},
                        modifier = Modifier,
                        shape = RoundedCornerShape(20.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.6f)
                        )
                    ) {
                        Icon(
                            Icons.Default.Settings,
                            contentDescription = "Settings",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier
                                .size(24.dp)
                                .clip(RoundedCornerShape(20.dp))
                        )
                    }
                }
                AnimatedVisibility(visible = settingsView) {
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceContainer.copy(alpha = 0.4f),
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        border = BorderStroke(1.dp,MaterialTheme.colorScheme.primary),
                        modifier = Modifier.padding(16.dp),
                        shape = RoundedCornerShape(50.dp)
                    ) {
                        Text(
                            text="There is nothing here yet :)",
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
                SearchBar(
                    inputField = {
                        SearchBarDefaults.InputField(
                            query = searchQuery,
                            onQueryChange = { searchQuery = it },
                            onSearch = { expanded = false },
                            expanded = expanded,
                            onExpandedChange = { expanded = it },
                            placeholder = { Text("Search pinyin or translation...") },
                            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                            trailingIcon = {
                                if (searchQuery.isNotEmpty()) {
                                    IconButton(onClick = { searchQuery = "" }) {
                                        Icon(Icons.Default.Close, contentDescription = null)
                                    }
                                }
                            }
                        )
                    },
                    expanded = expanded,
                    onExpandedChange = { expanded = it },
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp)),
                ) {
                    Column(Modifier.verticalScroll(rememberScrollState())) {
                        filteredList.take(20).forEach { suggestion ->
                            ListItem(
                                headlineContent = { Text("${suggestion.hanzi} - ${suggestion.pinyin}") },
                                supportingContent = { Text(suggestion.translations.firstOrNull() ?: "") },
                                modifier = Modifier.clickable {
                                    searchQuery = suggestion.hanzi
                                    expanded = false
                                }
                            )
                        }
                    }
                }
                val tts = rememberTextToSpeech()
                val pagerState = rememberPagerState(pageCount = { filteredList.size })
                HorizontalPager(
                    modifier = Modifier.weight(1f),
                    state = pagerState,
                    pageSpacing = 16.dp,

                ) { pageIndex ->
                    val character = filteredList[pageIndex]
                    CharacterCard(
                        character,
                        tts
                    )
                }
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
fun CharacterCard(
    character: HSKCharactersClass,
    tts: TextToSpeech?,
){
    val context = LocalContext.current
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer.copy(alpha = 0.4f),
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        border = BorderStroke(1.dp,MaterialTheme.colorScheme.primary.copy(alpha = 0.7f)),

    ) {
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)) {
            Button(
                onClick = {
                    tts?.let { textToSpeech ->
                        val result = textToSpeech.setLanguage(Locale.SIMPLIFIED_CHINESE)

                        if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                            Toast.makeText(context, "Language not supported", Toast.LENGTH_LONG)
                                .show()
                        } else {
                            textToSpeech.speak(
                                character.hanzi,
                                TextToSpeech.QUEUE_FLUSH,
                                null,
                                null
                            )
                        }
                    }
                },
                modifier = Modifier.align(Alignment.TopEnd),
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.6f)
                )
            ) {
                Icon(
                    Icons.Rounded.VolumeUp,
                    contentDescription = "Settings",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .size(24.dp)
                        .clip(RoundedCornerShape(20.dp))
                )
            }
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                Text(
                    text = character.hanzi,
                    style = MaterialTheme.typography.headlineMedium,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = character.pinyin,
                    style = MaterialTheme.typography.titleLarge,
                    textAlign = TextAlign.Center
                )

                Text(
                    text = character.translations.take(3).joinToString("\n"),
                    style = MaterialTheme.typography.titleSmall,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.9f)
                )
                Spacer(modifier = Modifier.height(16.dp))
                val charList = rememberSaveable(character) { character.hanzi.toList() }
                AndroidView(
                    modifier = Modifier
                        .width(200.dp)
                        .height(210.dp)
                        .clip(RoundedCornerShape(17.dp)),
                    factory = { factoryContext ->
                        WebView(factoryContext).apply {
                            settings.javaScriptEnabled = true
                            settings.domStorageEnabled = true
                            settings.allowFileAccess = true
                            settings.allowContentAccess = true

                            settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW

                            addJavascriptInterface(
                                StudyScreenWebAppInterface(context = context),
                                "Android"
                            )

                            WebView.setWebContentsDebuggingEnabled(true)
                            webChromeClient = WebChromeClient()
                            webViewClient = object : WebViewClient() {
                                override fun onPageFinished(view: WebView?, url: String?) {
                                    val jsArray = charList
                                        .joinToString("','", prefix = "['", postfix = "']")
                                    view?.evaluateJavascript(
                                        "begin(${jsArray})",
                                        null
                                    )
                                }
                            }
                            loadUrl("file:///android_asset/charactercard.html")
                        }

                    },
                    update = { webView ->
                        val jsArray = character.hanzi.toList()
                            .joinToString("','", prefix = "['", postfix = "']")
                        webView.evaluateJavascript("begin(${jsArray})", null)
                    }
                )
            }
        }
    }
}
fun String.normalizePinyin(): String {    val accents = "āáǎàēéěèīíǐìōóǒòūúǔùǖǘǚǜü"
    val plain = "aaaaeeeeiiiioooouuuuvvvvu"
    return this.map { char ->
        val index = accents.indexOf(char)
        if (index != -1) plain[index] else char.lowercaseChar()
    }.joinToString("").replace(Regex("[0-9]"), "")
}