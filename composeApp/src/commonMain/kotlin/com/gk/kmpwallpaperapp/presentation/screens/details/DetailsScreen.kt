@file:OptIn(ExperimentalMaterial3Api::class)

package com.gk.kmpwallpaperapp.presentation.screens.details

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import coil3.compose.AsyncImagePainter
import coil3.compose.rememberAsyncImagePainter
import com.gk.kmpwallpaperapp.getPlatform
import com.gk.kmpwallpaperapp.presentation.component.RatingBar
import com.gk.kmpwallpaperapp.utils.ImageErrorState
import com.gk.kmpwallpaperapp.utils.ImageLoadingState
import com.gk.kmpwallpaperapp.utils.constants.Constants.IMAGE_BASE_URL
import com.gk.kmpwallpaperapp.utils.languageMap
import moviesapp.composeapp.generated.resources.Res
import moviesapp.composeapp.generated.resources.language
import moviesapp.composeapp.generated.resources.overview
import moviesapp.composeapp.generated.resources.release_date
import moviesapp.composeapp.generated.resources.votes
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

class DetailsScreen(
    private val movieId: Int? = null
) : Screen {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val currentPlatform = getPlatform().name
        val navigator = LocalNavigator.current
        val detailsViewModel = koinViewModel<DetailsViewModel>()
        val detailsState by detailsViewModel.detailsState.collectAsState()

        LaunchedEffect(movieId) {
            if (movieId != null) {
                detailsViewModel.getMovieDetails(movieId)
            }
        }

        val backDropImage =
            rememberAsyncImagePainter("${IMAGE_BASE_URL}${detailsState.movie?.backdropPath}")
        val posterImage =
            rememberAsyncImagePainter("${IMAGE_BASE_URL}${detailsState.movie?.posterPath}")

        val backDropImageState = backDropImage.state.collectAsState().value
        val posterImageState = posterImage.state.collectAsState().value


        val lightTextColor = Color(0xFFE0E0E0)
        val semiTransparentLightTextColor = lightTextColor.copy(alpha = 0.8f)

        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = detailsState.movie?.title ?: "Movie Details",
                            fontSize = 20.sp,
                            maxLines = 1,
                            color = Color.White,
                            overflow = TextOverflow.Ellipsis
                        )
                    },
                    navigationIcon = {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            modifier = Modifier
                                .padding(8.dp)
                                .clickable { navigator?.pop() },
                            tint = Color.White
                        )
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.DarkGray
                    )
                )
            }
        ) { paddingValues ->
            BoxWithConstraints(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color(0xFF303030),
                                Color(0xFF101010)
                            )
                        )
                    )
            ) {

                val mMaxWidth = maxWidth
                val isDesktop = mMaxWidth > 600.dp

                val posterAspectRatio = 2f / 3f
                val posterWidth = maxWidth * 0.28f
                val posterHeight = posterWidth / posterAspectRatio


                if (isDesktop) {
                    // Two-column layout for desktop
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        //  Poster Image
                        Box(
                            modifier = Modifier
                                .width(posterWidth)
                                .height(posterHeight)
                                .clip(RoundedCornerShape(8.dp))
                        ) {
                            if (posterImageState is AsyncImagePainter.State.Success) {
                                Image(
                                    painter = posterImage,
                                    contentDescription = detailsState.movie?.title,
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Crop
                                )
                            } else if (posterImageState is AsyncImagePainter.State.Loading) {
                                ImageLoadingState()
                            } else {
                                ImageErrorState(isDesktop,posterWidth, posterHeight)
                            }
                        }

                        Column(
                            modifier = Modifier
                                .fillMaxHeight()
                                .weight(1f)
                                .padding(start = 10.dp)
                                .verticalScroll(rememberScrollState()),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            detailsState.movie?.let { movie ->
                                Text(
                                    text = movie.title,
                                    fontSize = 34.sp,
                                    fontWeight = FontWeight.Bold,
                                    maxLines = 2,
                                    lineHeight = 34.sp,
                                    color = lightTextColor
                                )

                                Row {
                                    RatingBar(
                                        starsModifier = Modifier.size(30.dp),
                                        rating = movie.voteAverage / 2
                                    )

                                    Text(
                                        modifier = Modifier.padding(start = 4.dp),
                                        text = "${movie.voteAverage.toString().take(3)}/10",
                                        color = semiTransparentLightTextColor,
                                        fontSize = 20.sp,
                                        maxLines = 1
                                    )
                                }
                                val languageName =
                                    languageMap[movie.originalLanguage]
                                        ?: movie.originalLanguage
                                Text(
                                    text = buildAnnotatedString {
                                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                            append(stringResource(Res.string.language))
                                        }
                                        append(languageName)
                                    },
                                    color = lightTextColor
                                )

                                Text(
                                    text = buildAnnotatedString {
                                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                            append(stringResource(Res.string.release_date))
                                        }
                                        append(movie.releaseDate)
                                    },
                                    color = lightTextColor
                                )

                                Text(
                                    text = buildAnnotatedString {
                                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                            append(stringResource(Res.string.votes))
                                        }
                                        append(movie.voteCount.toString())
                                    },
                                    color = lightTextColor
                                )


                                Text(
                                    text = buildAnnotatedString {
                                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                            append(stringResource(Res.string.overview))
                                        }
                                    },
                                    fontSize = 22.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = lightTextColor
                                )

                                detailsState.movie?.let {
                                    Text(
                                        text = it.overview,
                                        fontSize = 16.sp,
                                        color = lightTextColor
                                    )
                                }
                            }
                        }
                    }
                } else { // For mobile layout...
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                    ) {
                        if (backDropImageState is AsyncImagePainter.State.Loading) {
                            ImageLoadingState()
                        }
                        if (backDropImageState is AsyncImagePainter.State.Error) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .graphicsLayer(translationY = -80f)
                                    .height(300.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                ImageErrorState(isDesktop, posterWidth, posterHeight)
                            }
                        }


                        if (backDropImageState is AsyncImagePainter.State.Success) {
                            Image(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .graphicsLayer(translationY = -80f)
                                    .height(300.dp),
                                painter = backDropImageState.painter,
                                contentDescription = detailsState.movie?.title,
                                contentScale = ContentScale.Fit
                            )
                        }

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .width(160.dp)
                                    .height(240.dp)
                            ) {
                                if (posterImageState is AsyncImagePainter.State.Error) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .clip(RoundedCornerShape(12.dp)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        ImageErrorState(isDesktop, posterWidth, posterHeight)
                                    }
                                }

                                if (posterImageState is AsyncImagePainter.State.Success) {
                                    Image(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .clip(RoundedCornerShape(12.dp)),
                                        painter = posterImageState.painter,
                                        contentDescription = detailsState.movie?.title,
                                        contentScale = ContentScale.Crop
                                    )
                                }

                            }

                            detailsState.movie?.let { movie ->
                                Column(
                                    modifier = Modifier
                                        .padding(start = 16.dp)
                                        .fillMaxWidth()
                                ) {
                                    Text(
                                        text = movie.title,
                                        fontSize = 19.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = lightTextColor,
                                        maxLines = 2,
                                        lineHeight = 24.sp
                                    )

                                    Spacer(modifier = Modifier.height(16.dp))

                                    Row {
                                        RatingBar(
                                            starsModifier = Modifier.size(18.dp),
                                            rating = movie.voteAverage / 2
                                        )

                                        Text(
                                            modifier = Modifier.padding(start = 4.dp),
                                            text = movie.voteAverage.toString().take(3),
                                            color = semiTransparentLightTextColor,
                                            fontSize = 14.sp,
                                            maxLines = 1
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(12.dp))
                                    val languageName =
                                        languageMap[movie.originalLanguage]
                                            ?: movie.originalLanguage
                                    Text(
                                        text = buildAnnotatedString {
                                            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                                append(stringResource(Res.string.language))
                                            }
                                            append(languageName)
                                        },
                                        color = lightTextColor
                                    )

                                    Spacer(modifier = Modifier.height(10.dp))

                                    Text(
                                        text = buildAnnotatedString {
                                            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                                append(stringResource(Res.string.release_date))
                                            }
                                            append(movie.releaseDate)
                                        },
                                        color = lightTextColor
                                    )

                                    Spacer(modifier = Modifier.height(10.dp))

                                    Text(
                                        text = buildAnnotatedString {
                                            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                                append(stringResource(Res.string.votes))
                                            }
                                            append(movie.voteCount.toString())
                                        },
                                        color = semiTransparentLightTextColor
                                    )
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(32.dp))


                        Text(
                            modifier = Modifier.padding(start = 16.dp),
                            text = buildAnnotatedString {
                                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                    append(stringResource(Res.string.overview))
                                }
                            },
                            fontSize = 19.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = lightTextColor
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        detailsState.movie?.let {
                            Text(
                                modifier = Modifier.padding(start = 16.dp),
                                text = it.overview,
                                fontSize = 16.sp,
                                color = lightTextColor
                            )
                        }

                        Spacer(modifier = Modifier.height(32.dp))
                    }
                }
            }
        }
    }
}