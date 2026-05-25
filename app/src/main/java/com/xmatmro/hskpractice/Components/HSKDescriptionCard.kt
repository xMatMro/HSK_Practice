package com.xmatmro.hskpractice.Components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun HSKDescriptionCard(
     level:Int
){
    val cefr: Array<String> = arrayOf("A1","A2","B1","B2","C1","C2")
    val description: Array<String> = arrayOf(
        "You will understand and start using very simple phrases. You will learn to introduce yourself, talk about your age, nationality, count to 100, order food, and ask for prices.",
        "You will easily handle routine, everyday situations. You will ask for directions, talk about your family, hobbies, weather, and arrange a meeting.",
        "You are able to travel freely around China and communicate with native speakers on familiar topics from your professional and private life. You start thinking in Chinese.",
        "You can discuss a wide range of topics. You converse with Chinese people fluently, without much effort from either side. You can take up basic employment in a Chinese-speaking environment.",
        "You are able to read Chinese newspapers, internet portals, watch movies without English subtitles, and deliver longer speeches. Chinese becomes a fully-fledged tool for you.",
        "Proficiency close to that of an educated native speaker. You understand practically everything you hear or read. You fluently express opinions on highly abstract, technical, or philosophical topics."
    )
    val advice: Array<String> = arrayOf("Nie stresuj się na początku pisaniem znaków z pamięci. Skup się w 100% na opanowaniu transkrypcji Pinyin i usłyszeniu 4 tonów – to fundament, który zaprocentuje później.","To idealny moment, aby zacząć uczyć się \"kluczy\" (radicals) w znakach HanZi. Kiedy zrozumiesz z czego składa się znak, przestanie być tylko dziwnym obrazkiem.","Twój zasób słów pozwala już na rozrywkę! Zacznij oglądać proste bajki (np. Świnkę Peppę w wersji mandaryńskiej) i czytać książki typu Graded Readers dopasowane do poziomu HSK 3.","Odłóż powoli podręczniki. Przerzuć się na chińskie podcasty, zacznij pisać krótki dziennik (np. na HelloTalk), żeby uczyć się wyrażać własne, złożone myśli.","Zanurz się w prawdziwym pop-kulturowym oceanie. Chińskie dramy, programy rozrywkowe (variety shows), vlogi na Bilibili (chińskim YouTube) to teraz Twoje najlepsze źródła nauki.","Sięgnij po chińską literaturę współczesną (np. powieści sci-fi Cixina Liu), gazety branżowe i poezję. Baw się językiem, poznawaj slang i idiomy (Chengyu).")
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer.copy(alpha = 0.4f),
        ),
        elevation = CardDefaults.cardElevation(0.dp),
        modifier = Modifier
            .padding(16.dp)
            .width(250.dp),
        shape = RoundedCornerShape(25.dp),
        border = BorderStroke(2.dp, MaterialTheme.colorScheme.primary)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Text(
                text="HSK ${level}",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            HorizontalDivider()
            Text(
                text = "CEFR: ${cefr[level-1]}",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier
                    .padding(vertical = 8.dp, horizontal = 8.dp)
                    .fillMaxWidth(),
                textAlign = TextAlign.Start
            )
            HorizontalDivider()
            Text(
                text = "Opis: ${description[level-1]}",
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.padding(8.dp)
            )

        }
    }
}