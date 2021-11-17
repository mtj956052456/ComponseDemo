package com.mtj.compose.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.Colors
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color

@Composable
fun ComposeDemoTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable() () -> Unit) {
    val colors = if (darkTheme) DarkColorPalette else LightColorPalette
    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}

//==========================自定义主题色=======================================

@Composable
fun CkXTheme(pallet: StylePallet = CkXTheme.pallet, content: @Composable () -> Unit) {
    val (colorPalette, lcColors) = pallet.colors
    ProvideLcColors(colors = lcColors) {
        MaterialTheme(
            colors = colorPalette,
            typography = Typography,
            shapes = Shapes,
            content = content
        )
    }
}


enum class StylePallet {
    // 默认就给两个颜色，根据需求可以定义多个
    DARK, LIGHT
}

val DarkColorPalette = darkColors(
    primary = Purple200,
    primaryVariant = Purple700,
    secondary = Teal200
)

val LightColorPalette = lightColors(
    primary = Purple500,
    primaryVariant = Purple700,
    secondary = Teal200
)

/** CkX-Compose主题管理者 */
object CkXTheme {
    /** 从CompositionLocal中取出相应的Local */
    val colors: CkColors
        @Composable
        get() = LocalLcColors.current

    /** 使用一个state维护当前主题配置,这里的写法取决于具体业务，
    如果你使用了深色模式默认配置，则无需这个变量，即app只支持深色与亮色，
    那么只需要每次读系统配置即可。但是compose本身可以做到快速切换主题，
    那么维护一个变量是肯定没法避免的 */
    var pallet by mutableStateOf(StylePallet.LIGHT)
}

@Composable
fun ProvideLcColors(colors: CkColors, content: @Composable () -> Unit) {
    val colorPalette = remember { colors.copy() }
    colorPalette.update(colors)
    CompositionLocalProvider(LocalLcColors provides colorPalette, content = content)
}

/**
 * 实际主题的颜色集,所有颜色都需要添加到其中,并使用相应的子类覆盖颜色。
 * 每一次的更改都需要将颜色配置在下方 [CkColors] 中,并同步 [CkDarkColor] 与 [CkLightColor]
 * */
@Stable
class CkColors(homeBackColor: Color, homeTitleTvColor: Color) {
    var homeBackColor by mutableStateOf(homeBackColor)
        private set
    var homeTitleTvColor by mutableStateOf(homeTitleTvColor)
        private set

    fun update(colors: CkColors) {
        this.homeBackColor = colors.homeBackColor
        this.homeTitleTvColor = colors.homeTitleTvColor
    }

    fun copy() = CkColors(homeBackColor, homeTitleTvColor)
}

/** 提前定义好的颜色模板对象 */
private val CkDarkColors = CkColors(homeBackColor = A900, homeTitleTvColor = Blue50)
private val CkLightColors = CkColors(homeBackColor = Blue200, homeTitleTvColor = WHITE)

// 创建静态 CompositionLocal ,通常情况下主题改动不会很频繁
val LocalLcColors = staticCompositionLocalOf { CkLightColors }

/* 针对当前主题配置颜色板扩展属性 */
val StylePallet.colors: Pair<Colors, CkColors>
    get() = when (this) {
        StylePallet.DARK -> DarkColorPalette to CkDarkColors
        StylePallet.LIGHT -> LightColorPalette to CkLightColors
    }
//==========================自定义主题色=======================================
