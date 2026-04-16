package com.ozkanfurkan.myportfolio

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ozkanfurkan.myportfolio.ui.theme.MyPortfolioTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyPortfolioTheme {
                // Sayfa iskeleti: tam ekran yerleşim + içerik için padding sağlar.
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    // Uygulamanın tek ekranı: profil kartı + skillset + hakkımda bölümü.
                    ProfileScreen(
                        profile = Profile(
                            fullName = "Furkan Özkan",
                            university = "Erciyes Üniversitesi",
                            title = "Yazılım Geliştirici / Android Developer",
                            skills = listOf(
                                "Kotlin",
                                "Jetpack Compose",
                                "Android",
                                "Git",
                                "UI/UX",
                                "KMP"
                            )
                        ),
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

// Ekranda göstereceğimiz profil verileri
data class Profile(
    val fullName: String,
    val university: String,
    val title: String,
    val skills: List<String>
)

// Tek sayfalık profil ekranı.
@Composable
fun ProfileScreen(profile: Profile, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 20.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // PROFİL KARTI: avatar + isim + ünvan + üniversite. Box ile yatayda ortalanır.
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Card(
                modifier = Modifier.widthIn(max = 420.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(
                    modifier = Modifier.padding(18.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Avatar alanı
                    Box(
                        modifier = Modifier
                            .size(92.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primaryContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "👤", style = MaterialTheme.typography.displaySmall)
                    }

                    // Metin bloğu: isim, ünvan, üniversite alt alta.
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = profile.fullName,
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = profile.title,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = profile.university,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        // SKILLSET KARTI: skill listesini chip'ler halinde gösterir.
        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(text = "Skillset", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                HorizontalDivider(color = MaterialTheme.colorScheme.surfaceVariant)
                SkillChips(skills = profile.skills)
            }
        }

        // HAKKIMDA KARTI
        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(
                    text = "Hakkımda",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "Android tarafında Kotlin ve Jetpack Compose ile sade, hızlı ve kullanışlı arayüzler geliştiriyorum. " +
                        "Temiz kod, doğru mimari ve ekip çalışmasını önemsiyorum; UI/UX detaylarına dikkat ederek ürün odaklı ilerlemeyi seviyorum.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }
        }
    }
}

// Skill listesini 3'lü satırlara bölüp chip olarak çizer (UI daha düzenli görünür).
@Composable
private fun SkillChips(skills: List<String>, modifier: Modifier = Modifier) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(8.dp)) {
        // chunked(3) ile her satırda en fazla 3 chip olacak şekilde grupluyoruz.
        skills.chunked(3).forEach { rowSkills ->
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                rowSkills.forEach { skill ->
                    AssistChip(
                        onClick = {},
                        label = { Text(skill) }
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProfilePreview() {
    MyPortfolioTheme {
        // Android Studio Preview için örnek veri.
        ProfileScreen(
            profile = Profile(
                fullName = "Furkan Özkan",
                university = "Erciyes Üniversitesi",
                title = "Yazılım Geliştirici / Android Developer",
                skills = listOf("Kotlin", "Compose", "Android")
            )
        )
    }
}
