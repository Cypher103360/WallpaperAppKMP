@file:OptIn(ExperimentalMaterial3Api::class)

package com.gk.kmpwallpaperapp.presentation.screens

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.tab.CurrentTab
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabNavigator
import com.gk.kmpwallpaperapp.presentation.MovieListViewModel
import com.gk.kmpwallpaperapp.presentation.screens.tabs.PopularMoviesScreen
import com.gk.kmpwallpaperapp.presentation.screens.tabs.UpcomingMoviesScreen
import org.koin.compose.viewmodel.koinViewModel


class HomeScreen: Screen {
    @Composable
    override fun Content() {
        val movieListViewModel: MovieListViewModel = koinViewModel<MovieListViewModel>()
        val movieListState = movieListViewModel.movieListState.collectAsState().value
        TabNavigator(PopularMoviesScreen) {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = {
                            Text(
                                text = if (movieListState.isCurrentPopularScreen) {
                                    "Popular Movies"
                                } else {
                                    "Upcoming Movies"
                                },
                                fontSize = 20.sp
                            )
                        },
                        modifier = Modifier.shadow(2.dp),
                        colors = topAppBarColors(
                            MaterialTheme.colorScheme.inverseOnSurface
                        )
                    )
                },
                bottomBar = {
                    NavigationBar {
                        TabNavigationItem(PopularMoviesScreen)
                        TabNavigationItem(UpcomingMoviesScreen)
                    }
                }
            ) {
                CurrentTab()
            }
        }
    }
}

@Composable
private fun RowScope.TabNavigationItem(tab: Tab) {
    val tabNavigator = LocalTabNavigator.current
    NavigationBarItem(
        selected = tabNavigator.current == tab,
        onClick = { tabNavigator.current = tab },
        label = { androidx.compose.material3.Text(text = tab.options.title) },
        icon = {}
    )
}