package com.example.todolist.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme

import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color


private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF6730D2), // Roxo mais suave
    onPrimary = Color.White,
    primaryContainer = Color(0xFFE9DDFF), // Contêiner para o primário
    onPrimaryContainer = Color(0xFF21005E), // Texto/ícones no contêiner primário
    secondary = Color(0xFF9C27B0), // Roxo mais vibrante (opcional, para destaque)
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFF3E5F5), // Contêiner para o secundário
    onSecondaryContainer = Color(0xFF370036), // Texto/ícones no contêiner secundário
    tertiary = Color(0xFF673AB7), // Outro roxo (opcional, para variações)
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFFD1C4E9), // Contêiner para o terciário
    onTertiaryContainer = Color(0xFF210641), // Texto/ícones no contêiner terciário
    background = Color(0xFFFFFFFF), // Branco puro
    onBackground = Color(0xFF1C1B1F), // Cinza escuro para texto em fundo branco
    surface = Color(0xFFFFFFFF), // Branco puro
    onSurface = Color(0xFF1C1B1F), // Cinza escuro para texto em superfície branca
    surfaceVariant = Color(0xFFE6E1E9), // Variação da superfície (cinza claro)
    onSurfaceVariant = Color(0xFF4A454F), // Texto/ícones na variação da superfície
    outline = Color(0xFF79747E), // Contorno
    inverseOnSurface = Color(0xFF313033), // Texto/ícones em superfície invertida
    inversePrimary = Color(0xFFD0BCFF), // Primário invertido
    inverseSurface = Color(0xFF1C1B1F), // Superfície invertida
)

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFFD0BCFF), // Roxo mais claro para o tema escuro
    onPrimary = Color(0xFF370036),
    primaryContainer = Color(0xFF4A2D86),
    onPrimaryContainer = Color(0xFFE9DDFF),
    secondary = Color(0xFFCE93D8), // Roxo mais claro para o tema escuro
    onSecondary = Color(0xFF370036),
    secondaryContainer = Color(0xFF6730D2),
    onSecondaryContainer = Color(0xFFF3E5F5),
    tertiary = Color(0xFFB39DDB), // Roxo mais claro para o tema escuro
    onTertiary = Color(0xFF37066B),
    tertiaryContainer = Color(0xFF673AB7),
    onTertiaryContainer = Color(0xFFD1C4E9),
    background = Color(0xFF121212), // Preto quase puro
    onBackground = Color(0xFFE6E1E9), // Cinza claro para texto em fundo preto
    surface = Color(0xFF121212), // Preto quase puro
    onSurface = Color(0xFFE6E1E9), // Cinza claro para texto em superfície preta
    surfaceVariant = Color(0xFF424242), // Variação da superfície (cinza mais claro)
    onSurfaceVariant = Color(0xFFC5C0CA), // Texto/ícones na variação da superfície
    outline = Color(0xFF5E5C61), // Contorno
    inverseOnSurface = Color(0xFFF5F5F5), // Texto/ícones em superfície invertida
    inversePrimary = Color(0xFF6730D2), // Primário invertido
    inverseSurface = Color(0xFFF5F5F5), // Superfície invertida
)


@Composable
fun ToDoListTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = MaterialTheme.typography,
        content = content
    )
}