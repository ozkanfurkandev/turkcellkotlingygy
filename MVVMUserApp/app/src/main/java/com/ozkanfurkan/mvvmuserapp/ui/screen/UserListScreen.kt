package com.ozkanfurkan.mvvmuserapp.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ozkanfurkan.mvvmuserapp.data.model.User
import com.ozkanfurkan.mvvmuserapp.ui.components.UserItem
import com.ozkanfurkan.mvvmuserapp.ui.theme.MVVMUserAppTheme
import com.ozkanfurkan.mvvmuserapp.viewmodel.UserUiState
import com.ozkanfurkan.mvvmuserapp.viewmodel.UserViewModel

/**
 * Ana ekran — ViewModel'i dinler ve state'i stateless [UserListScreenContent]'e iletir.
 */
@Composable
fun UserListScreen(
    modifier: Modifier = Modifier,
    viewModel: UserViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    UserListScreenContent(
        uiState = uiState,
        onRetry = viewModel::fetchUsers,
        modifier = modifier
    )
}

/**
 * Durum bağımsız ekran içeriği. Preview ve testler için doğrudan çağrılabilir.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserListScreenContent(
    uiState: UserUiState,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(text = "Kullanıcılar") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { innerPadding ->
        when (uiState) {
            is UserUiState.Loading -> LoadingContent(innerPadding)
            is UserUiState.Success -> SuccessContent(
                innerPadding = innerPadding,
                state = uiState
            )
            is UserUiState.Error -> ErrorContent(
                innerPadding = innerPadding,
                message = uiState.message,
                onRetry = onRetry
            )
        }
    }
}

@Composable
private fun LoadingContent(innerPadding: PaddingValues) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun SuccessContent(
    innerPadding: PaddingValues,
    state: UserUiState.Success
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding),
        contentPadding = PaddingValues(vertical = 8.dp)
    ) {
        items(
            items = state.users,
            key = { it.id }
        ) { user ->
            UserItem(user = user)
        }
    }
}

@Composable
private fun ErrorContent(
    innerPadding: PaddingValues,
    message: String,
    onRetry: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Bir hata oluştu",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.error
            )
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Button(onClick = onRetry) {
                Text(text = "Tekrar Dene")
            }
        }
    }
}

private val previewUsers = listOf(
    User(1, "Leanne Graham", "Bret", "Sincere@april.biz", "1-770-736-8031", "hildegard.org"),
    User(2, "Ervin Howell", "Antonette", "Shanna@melissa.tv", "010-692-6593", "anastasia.net"),
    User(3, "Clementine Bauch", "Samantha", "Nathan@yesenia.net", "1-463-123-4447", "ramiro.info")
)

@Preview(showBackground = true)
@Composable
private fun UserListSuccessPreview() {
    MVVMUserAppTheme {
        UserListScreenContent(
            uiState = UserUiState.Success(previewUsers),
            onRetry = {}
        )
    }
}
