package com.example.mobilecomputinghw4

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import java.io.File

@Composable
fun ProfileScreen(
    onNavigateToChat: () -> Unit,
) {
    val context = LocalContext.current
    val resolver = context.contentResolver

    val file = File(context.filesDir, "profile_picture")
    val usernameFile = File(context.filesDir, "username")

    var filePath by remember {
        mutableStateOf(
            file.toURI().toString()
        )
    }

    Column {
        Row(
            modifier = Modifier
                .background(Color.LightGray)
                .fillMaxWidth()
                .padding(4.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            val iconSpacerSize = 48.dp // Used to center Profile Text to center

            IconButton(
                onClick = onNavigateToChat,
                modifier = Modifier.size(iconSpacerSize)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Go back to Chat",
                    modifier = Modifier.fillMaxSize()
                )
            }

            Text(
                text = "Profile",
                fontSize = 40.sp,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.size(iconSpacerSize))
        }


        val pickMedia =
            rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                if (uri != null) {
                    resolver.openInputStream(uri).use { stream ->
                        val outputStream = file.outputStream()
                        stream?.copyTo(outputStream)

                        outputStream.close()
                        stream?.close()
                    }

                    filePath = file.toURI().toString() + "?timestamp=${System.currentTimeMillis()}"
                }

            }


        Column(modifier = Modifier.padding(8.dp)) {

            AsyncImage(
                model = filePath,
                contentDescription = "profile picture",
                modifier = Modifier
                    .clip(CircleShape)
                    .border(2.dp, MaterialTheme.colorScheme.secondary, CircleShape)
                    .size(100.dp)
                    .clickable {
                        pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                    },
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.size(8.dp))

            var text by remember {
                mutableStateOf(
                    usernameFile.readBytes().decodeToString()
                )
            }

            TextField(
                value = text,
                onValueChange = {
                    usernameFile.writeBytes(it.toByteArray())
                    text = it
                },
                label = { Text("Username") },
                singleLine = true
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewProfileScreen() {
    ProfileScreen { }
}
