package com.gk.kmpwallpaperapp.presentation.screens.tabs

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import cafe.adriel.voyager.transitions.SlideTransition
import com.gk.kmpwallpaperapp.presentation.screens.UpcomingMoviesScreen
import org.jetbrains.compose.resources.painterResource
import wallpaperapp.composeapp.generated.resources.Res
import wallpaperapp.composeapp.generated.resources.upcoming

object UpcomingMovieTab: Tab {
    @Composable
    override fun Content() {
        Navigator(UpcomingMoviesScreen()) { navigator ->
            SlideTransition(navigator)
        }
    }

    override val options: TabOptions
        @Composable
        get() {
            val icon = painterResource(Res.drawable.upcoming)

           return TabOptions(
                index = 1u,
                title = "Upcoming",
                icon = icon
            )
        }
}