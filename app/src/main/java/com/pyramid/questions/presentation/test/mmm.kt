import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.pyramid.questions.R@Composable
fun GameLevelItem(
    level: GameLevel,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(80.dp)
            .padding(horizontal = 16.dp, vertical = 4.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = when (level.state) {
                LevelState.UNLOCKED -> Color(0xFF4A90E2)
                LevelState.LOCKED -> Color(0xFF6B7280)
                LevelState.VIP -> Color(0xFFD4A574)
            }
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Level Image
            Card(
                modifier = Modifier.size(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                AsyncImage(
                    model = level.imageUrl,
                    contentDescription = level.title,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Level Info
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = level.title,
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Progress or Stars
                when (level.state) {
                    LevelState.UNLOCKED -> {
                        if (level.progress != null) {
                            ProgressIndicator(
                                current = level.progress.current,
                                total = level.progress.total
                            )
                        }
                    }
                    LevelState.LOCKED -> {
                        if (level.stars != null) {
                            StarsIndicator(stars = level.stars)
                        }
                    }
                    LevelState.VIP -> {
                        if (level.progress != null) {
                            ProgressIndicator(
                                current = level.progress.current,
                                total = level.progress.total
                            )
                        }
                    }
                }

                // Subtitle for special levels
                level.subtitle?.let { subtitle ->
                    Text(
                        text = subtitle,
                        color = Color.White.copy(alpha = 0.8f),
                        fontSize = 12.sp
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Right side elements
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                // VIP Badge
                if (level.state == LevelState.VIP) {
                    VipBadge()
                }

                // Lock/Hint/Coins
                when (level.state) {
                    LevelState.UNLOCKED -> {
                        if (level.hintAvailable) {
                            HintIcon()
                        }
                    }
                    LevelState.LOCKED -> {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            LockIcon()
                            level.unlockCost?.let { cost ->
                                CoinIndicator(cost = cost)
                            }
                        }
                    }
                    LevelState.VIP -> {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            LockIcon()
                            level.unlockCost?.let { cost ->
                                CoinIndicator(cost = cost)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ProgressIndicator(
    current: Int,
    total: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .background(
                    Color.Black.copy(alpha = 0.3f),
                    RoundedCornerShape(8.dp)
                )
                .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            Text(
                text = "$current/$total",
                color = Color.White,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        if (current > 0) {
            HintIcon()
        }
    }
}

@Composable
fun StarsIndicator(
    stars: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.Star,
            contentDescription = "Stars",
            tint = Color(0xFFFFD700),
            modifier = Modifier.size(16.dp)
        )

        Spacer(modifier = Modifier.width(4.dp))

        Box(
            modifier = Modifier
                .background(
                    Color.Black.copy(alpha = 0.3f),
                    RoundedCornerShape(8.dp)
                )
                .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            Text(
                text = stars.toString(),
                color = Color.White,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun VipBadge() {
    Box(
        modifier = Modifier
            .background(
                Brush.horizontalGradient(
                    colors = listOf(Color(0xFFFFD700), Color(0xFFFFA500))
                ),
                RoundedCornerShape(topStart = 8.dp, bottomStart = 8.dp)
            )
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(
            text = "VIP",
            color = Color.White,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun HintIcon() {
    Icon(
        painterResource(R.drawable.idea_ic),
        contentDescription = "Hint",
        tint = Color(0xFFFFD700),
        modifier = Modifier.size(20.dp)
    )
}

@Composable
fun LockIcon() {
    Icon(
        imageVector = Icons.Default.Lock,
        contentDescription = "Locked",
        tint = Color(0xFFFFD700),
        modifier = Modifier.size(24.dp)
    )
}

@Composable
fun CoinIndicator(cost: Int) {
    Row(
        modifier = Modifier
            .background(
                Color(0xFF4CAF50),
                RoundedCornerShape(12.dp)
            )
            .padding(horizontal = 8.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painterResource(R.drawable.coin1),
            contentDescription = "Coins",
            tint = Color(0xFFFFD700),
            modifier = Modifier.size(16.dp)
        )

        Spacer(modifier = Modifier.width(4.dp))

        Text(
            text = cost.toString(),
            color = Color.White,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

// Data classes
data class GameLevel(
    val id: String,
    val title: String,
    val subtitle: String? = null,
    val imageUrl: String,
    val state: LevelState,
    val progress: Progress? = null,
    val stars: Int? = null,
    val unlockCost: Int? = null,
    val hintAvailable: Boolean = false
)

data class Progress(
    val current: Int,
    val total: Int
)

enum class LevelState {
    UNLOCKED,
    LOCKED,
    VIP
}

// Usage Example
@Composable
fun GameLevelsScreen() {
    val levels = remember {
        listOf(
            GameLevel(
                id = "1",
                title = "مستوى 1",
                imageUrl = "https://example.com/mosque.jpg",
                state = LevelState.UNLOCKED,
                progress = Progress(1, 18),
                hintAvailable = true
            ),
            GameLevel(
                id = "2",
                title = "مستوى 2",
                imageUrl = "https://example.com/panda.jpg",
                state = LevelState.LOCKED,
                stars = 10,
                unlockCost = 100
            ),
            GameLevel(
                id = "3",
                title = "مستوى للمحترفين",
                subtitle = "اعلام الدول #1",
                imageUrl = "https://example.com/flag.jpg",
                state = LevelState.VIP,
                progress = Progress(1, 2),
                unlockCost = 500
            ),
            GameLevel(
                id = "4",
                title = "مستوى 3",
                imageUrl = "https://example.com/pyramid.jpg",
                state = LevelState.UNLOCKED,
                progress = Progress(1, 18),
                hintAvailable = true
            ),
            GameLevel(
                id = "5",
                title = "مستوى 4",
                imageUrl = "https://example.com/brain.jpg",
                state = LevelState.LOCKED,
                stars = 34,
                unlockCost = 200
            )
        )
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(vertical = 16.dp)
    ) {
        items(levels) { level ->
            GameLevelItem(level = level)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GameLevelsScreenPreview() {
    GameLevelsScreen()
}