package com.metinatalay.instagram

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.metinatalay.instagram.auth.LoginScreen
import com.metinatalay.instagram.auth.ProfileScreen
import com.metinatalay.instagram.auth.SignupScreen
import com.metinatalay.instagram.data.PostData
import com.metinatalay.instagram.main.*
import com.metinatalay.instagram.ui.theme.InstagramCloneTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            InstagramCloneTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    InstagramApp()
                }
            }
        }
    }
}

sealed class DestinationScreen(val route: String) {
    object Signup : DestinationScreen("signup")
    object Login : DestinationScreen("login")
    object Feed : DestinationScreen("feed")
    object Search : DestinationScreen("search")
    object MyPosts : DestinationScreen("myposts")
    object Profile : DestinationScreen("profile")
    object NewPost: DestinationScreen("newpost/{imageUri}") {
        fun createRoute(uri: String) = "newpost/$uri"
    }
    object SinglePost : DestinationScreen("singlepost")
    object CommentsScreen : DestinationScreen("comments/{postId}") {
        fun createRoute(postId: String) = "comments/$postId"
    }
}

@Composable
fun InstagramApp() {
    val vm = hiltViewModel<IgViewModel>()
    val navController = rememberNavController()

    NotificationMessage(vm = vm)

    NavHost(navController = navController, startDestination = DestinationScreen.Signup.route) {
        composable(DestinationScreen.Signup.route) {
            SignupScreen(navController = navController, vm = vm)
        }

        composable(DestinationScreen.Login.route) {
            LoginScreen(navController = navController, vm = vm)
        }

        composable(DestinationScreen.Feed.route) {
            FeedScreen(navController = navController, vm = vm)
        }

        composable(DestinationScreen.Search.route) {
            SearchScreen(navController = navController, vm = vm)
        }

        composable(DestinationScreen.MyPosts.route) {
            MyPostsScreen(navController = navController, vm = vm)
        }

        composable(DestinationScreen.Profile.route) {
            ProfileScreen(navController = navController, vm = vm)
        }

        composable(DestinationScreen.NewPost.route) { navBackStackEntry ->
            var imageUri = navBackStackEntry.arguments?.getString("imageUri")
            imageUri?.let {
                NewPostScreen(navController = navController, vm = vm, encodedUri = it)
            }
        }

        composable(DestinationScreen.SinglePost.route) {
            val postData = navController
                .previousBackStackEntry
                ?.arguments
                ?.getParcelable<PostData>("post")
            postData?.let {
                SinglePostScreen(
                    navController = navController,
                    vm = vm,
                    post = postData
                )
            }
        }
        composable(DestinationScreen.CommentsScreen.route) { navBackStackEntry ->
            val postId = navBackStackEntry.arguments?.getString("postId")
            postId?.let { CommentsScreen(navController = navController, vm = vm, postId = it) }
        }

    }

}

