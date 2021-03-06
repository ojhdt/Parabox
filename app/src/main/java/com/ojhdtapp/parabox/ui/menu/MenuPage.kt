package com.ojhdtapp.parabox.ui.menu

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.ojhdtapp.parabox.ui.MainSharedViewModel
import com.ojhdtapp.parabox.ui.NavGraphs
import com.ojhdtapp.parabox.ui.destinations.FilePageDestination
import com.ojhdtapp.parabox.ui.destinations.MessagePageDestination
import com.ojhdtapp.parabox.ui.file.FilePage
import com.ojhdtapp.parabox.ui.message.MessagePage
import com.ojhdtapp.parabox.ui.util.NavigationBar
import com.ojhdtapp.parabox.ui.util.NavigationRail
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.animations.defaults.RootNavGraphDefaultAnimations
import com.ramcosta.composedestinations.animations.rememberAnimatedNavHostEngine
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.manualcomposablecalls.composable
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.dependency
import kotlinx.coroutines.launch

@OptIn(
    ExperimentalAnimationApi::class, ExperimentalMaterialNavigationApi::class,
    ExperimentalMaterial3Api::class
)
@Destination
@RootNavGraph(start = true)
@Composable
fun MenuPage(
    modifier: Modifier = Modifier,
    navigator: DestinationsNavigator,
    navController: NavController,
    mainSharedViewModel: MainSharedViewModel,
    sizeClass: WindowSizeClass
) {
    // Destination
    val menuNavController = rememberAnimatedNavController()
    val menuNavHostEngine = rememberAnimatedNavHostEngine(
        navHostContentAlignment = Alignment.TopCenter,
        rootDefaultAnimations = RootNavGraphDefaultAnimations(),
        defaultAnimationsForNestedNavGraph = mapOf(
//                    NavGraphs.message to NestedNavGraphDefaultAnimations(
//                        enterTransition = { scaleIn(tween(200), 0.9f) + fadeIn(tween(200)) },
//                        exitTransition = { scaleOut(tween(200), 1.1f) + fadeOut(tween(200)) },
//                        popEnterTransition = {scaleIn(tween(200), 1.1f) + fadeIn(tween(200))},
//                        popExitTransition = {scaleOut(tween(200), 0.9f) + fadeOut(tween(200))}
//                    )
        )
    )
    // Menu Shared View Model (Not Used)
    val sharedViewModel =
        hiltViewModel<MenuSharedViewModel>()
    // Drawer
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()
    com.ojhdtapp.parabox.ui.util.NavigationDrawer(
        modifier = modifier.fillMaxSize(),
        navController = menuNavController,
        messageBadge = mainSharedViewModel.messageBadge.value,
        onSelfItemClick = {},
        drawerState = drawerState,
        gesturesEnabled = sizeClass.widthSizeClass != WindowWidthSizeClass.Compact,
        sizeClass = sizeClass
    ) {
        Column() {
            Row(modifier = Modifier.weight(1f)) {
//                              if (sizeClass.widthSizeClass == WindowWidthSizeClass.Expanded) { }
                if (sizeClass.widthSizeClass == WindowWidthSizeClass.Medium) {
                    NavigationRail(
                        modifier = Modifier.zIndex(1f),
                        navController = menuNavController,
                        messageBadge = mainSharedViewModel.messageBadge.value,
                        onSelfItemClick = {},
                        onMenuClick = {
                            coroutineScope.launch {
                                if (drawerState.isOpen) drawerState.close() else drawerState.open()
                            }
                        },
                        onFABClick = {})
                }
                DestinationsNavHost(
                    navGraph = NavGraphs.menu,
                    engine = menuNavHostEngine,
                    navController = menuNavController,
                    dependenciesContainerBuilder = {
//                        dependency(NavGraphs.message) {
//                            val parentEntry = remember(navBackStackEntry) {
//                                menuNavController.getBackStackEntry(NavGraphs.message.route)
//                            }
//                            hiltViewModel<MessagePageViewModel>(parentEntry)
//                        }
                        dependency(mainSharedViewModel)
                        dependency(drawerState)
                        dependency(sizeClass)
                    }
                ){
                    composable(MessagePageDestination){
                        MessagePage(
                            navigator = destinationsNavigator,
                            mainNavController = navController,
                            mainSharedViewModel = mainSharedViewModel,
                            sizeClass = sizeClass,
                            drawerState = drawerState
                        )
                    }
                    composable(FilePageDestination){
                        FilePage(
                            navigator = destinationsNavigator,
                            mainNavController = navController,
                            mainSharedViewModel = mainSharedViewModel,
                            sizeClass = sizeClass,
                            drawerState = drawerState
                        )
                    }
                }
            }
            if (sizeClass.widthSizeClass == WindowWidthSizeClass.Compact
            ) {
                NavigationBar(
                    navController = menuNavController,
                    messageBadge = mainSharedViewModel.messageBadge.value,
                    onSelfItemClick = {},
                )
            }
        }
    }
}