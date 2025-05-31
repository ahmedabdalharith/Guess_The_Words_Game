import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import com.pyramid.questions.R
import com.pyramid.questions.presentation.components.RedCloseButton
import com.pyramid.questions.presentation.test.Button3D

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LevelUnlockDialog(
    starsRequired: Int = 10,
    coinsCost: Int = 1000,
    onUnlock: () -> Unit = {},
    onClose: () -> Unit = {},
) {

    BasicAlertDialog(
        onDismissRequest = onClose,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true,
            usePlatformDefaultWidth = false
        )
    ){
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Transparent)
                .aspectRatio(0.9f)
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) { /* Prevent clicks from propagating */ }
        ) {
            Box(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxSize()
                    .align(Alignment.Center)
                    .clip(RoundedCornerShape(24.dp))
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(Color(0xFF4C7ED3), Color(0xFF1E4998)),
                            center = Offset.Infinite,
                            radius = 900f
                        )
                    )
                    .border(
                        width = 8.dp,
                        color = Color(0xFF3D9CFF),
                        shape = RoundedCornerShape(24.dp)
                    )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Title with shadow effect
                    Text(
                        text = stringResource(R.string.level_unlock_title),
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        fontFamily = FontFamily(Font(R.font.arbic_font_bold_2)),
                        textAlign = TextAlign.Center,
                        style = TextStyle(
                            shadow = Shadow(
                                color = Color.Black.copy(alpha = 0.5f),
                                offset = Offset(3f, 3f),
                                blurRadius = 5f
                            )
                        ),
                        modifier = Modifier.padding(top = 24.dp, bottom = 16.dp)
                    )
                    Image(
                        painterResource(R.drawable.lock_open),
                        contentDescription = stringResource(R.string.content_description_unlock_icon),
                        modifier = Modifier
                            .size(100.dp)
                            .padding(bottom = 16.dp),
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        // Star icon
                        Image(
                            painter = painterResource(id = R.drawable.star),
                            contentDescription = stringResource(R.string.content_description_stars),
                            modifier = Modifier.size(40.dp),
                            contentScale = ContentScale.Crop
                        )

                        // Number of stars
                        Text(
                            text = starsRequired.toString(),
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            style = TextStyle(
                                shadow = Shadow(
                                    color = Color.Black.copy(alpha = 0.5f),
                                    offset = Offset(2f, 2f),
                                    blurRadius = 4f
                                )
                            ),
                            fontFamily = FontFamily(Font(R.font.arbic_font_bold_2)),
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }

                    // Subtitle
                    Text(
                        text = stringResource(R.string.level_unlock_subtitle),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        fontFamily = FontFamily(Font(R.font.arbic_font_bold_2)),
                        textAlign = TextAlign.Center,
                        style = TextStyle(
                            shadow = Shadow(
                                color = Color.Black.copy(alpha = 0.5f),
                                offset = Offset(2f, 2f),
                                blurRadius = 4f
                            )
                        ),
                        modifier = Modifier.padding(vertical = 8.dp)
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    Button3D(
                        modifier = Modifier.padding(bottom = 16.dp),
                        width = 280.dp,
                        height = 70.dp,
                        cornerRadius = 16,
                        onClick = onUnlock,
                        text = stringResource(R.string.unlock_button, coinsCost),
                        textSize = 14,
                    )
                }


            }
            RedCloseButton(
                icon = R.drawable.close_ic,
                onClick = onClose,
                modifier = Modifier
                    .size(36.dp)
                    .align(Alignment.TopEnd)
                    .offset(x = (-32).dp, y = 2.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LevelUnlockDialogPreview() {
    LevelUnlockDialog(
        starsRequired = 10,
        coinsCost = 100,
        onUnlock = { /* Handle unlock */ },
        onClose = { /* Handle close */ },
    )
}