package trista.josecarlos.practicasqlite_josetrista.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun AddNoteScreen(
    state: NoteState,
    navController: NavController,
    onEvent: (NotesEvent) -> Unit
) {
    // Mensaje de error para el título y la descripción
    val titleError = if (state.titleError.value.isNullOrEmpty()) "" else state.titleError.value!!
    val descriptionError = if (state.descriptionError.value.isNullOrEmpty()) "" else state.descriptionError.value!!

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {
                // Verificación y evento de guardar se mueven al ViewModel
                onEvent(NotesEvent.SaveNote(
                    title = state.title.value,
                    description = state.description.value
                ))
            }) {
                Icon(
                    imageVector = Icons.Rounded.Check,
                    contentDescription = "Save Note"
                )
            }
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                value = state.title.value,
                onValueChange = { state.title.value = it },
                textStyle = TextStyle(fontWeight = FontWeight.SemiBold, fontSize = 17.sp),
                placeholder = { Text(text = "Title") },
                isError = titleError.isNotEmpty()
            )

            // Mostrar mensaje de error si el título no es válido
            if (titleError.isNotEmpty()) {
                Text(
                    text = titleError,
                    color = MaterialTheme.colorScheme.error,
                    style = TextStyle(fontSize = 12.sp),
                    modifier = Modifier.padding(start = 16.dp, end = 16.dp)
                )
            }

            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                value = state.description.value,
                onValueChange = { state.description.value = it },
                placeholder = { Text(text = "Description") },
                isError = descriptionError.isNotEmpty()
            )

            // Mostrar mensaje de error si la descripción no es válida
            if (descriptionError.isNotEmpty()) {
                Text(
                    text = descriptionError,
                    color = MaterialTheme.colorScheme.error,
                    style = TextStyle(fontSize = 12.sp),
                    modifier = Modifier.padding(start = 16.dp, end = 16.dp)
                )
            }
        }
    }

    // Navegar hacia atrás si la nota se guarda exitosamente (o según lógica específica)
    LaunchedEffect(key1 = state.noteSaved) {
        if (state.noteSaved) {
            navController.popBackStack()
        }
    }
}