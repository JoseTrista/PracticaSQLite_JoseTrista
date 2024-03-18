package trista.josecarlos.practicasqlite_josetrista.presentation

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import trista.josecarlos.practicasqlite_josetrista.data.Note

data class NoteState(

    val notes: List<Note> = emptyList(),
    val title: MutableState<String> = mutableStateOf(""),
    val description: MutableState<String> = mutableStateOf("")
)
