package cl.duoc.gameverse.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val LightColorScheme = lightColorScheme(
    primary = GameVerseGreen,
    onPrimary = GameVerseWhite,
    secondary = GameVerseGreenDark,
    tertiary = GameVerseGray,
    outline = GameVerseGray,
    background = GameVerseBackground,
    surface = GameVerseWhite
)

private val DarkColorScheme = darkColorScheme(
    primary = GameVerseGreenLight,
    onPrimary = GameVerseBlack,
    secondary = GameVerseGreenLight,
    tertiary = GameVerseGrayLight,
    outline = GameVerseGrayLight,
    background = GameVerseBackgroundDark,
    surface = GameVerseBackgroundDark
)

@Composable
fun GameVerseTheme( // <-- Aquí se define
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}