package trista.josecarlos.practicasqlite_josetrista.presentation

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import androidx.lifecycle.ViewModel
import trista.josecarlos.practicasqlite_josetrista.data.NoteDao
import trista.josecarlos.practicasqlite_josetrista.data.Note

class NotesViewModel(private val dao: NoteDao): ViewModel() {

    private val isSortedByDateAdded = MutableStateFlow(true)

    private var notes =
        isSortedByDateAdded.flatMapLatest { sort ->
            if (sort) {
                dao.getNotesOrderdByDateAdded()
            } else {
                dao.getNotesOrderdByTitle()
            }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    val _state = MutableStateFlow(NoteState())
    val state =
        combine(_state, isSortedByDateAdded, notes) { state, isSortedByDateAdded, notes ->
            state.copy(
                notes = notes
            )
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), NoteState())

    fun onEvent(event: NotesEvent) {
        when (event) {
            is NotesEvent.DeleteNote -> {
                viewModelScope.launch {
                    dao.deleteNote(event.note)
                }
            }

            is NotesEvent.SaveNote -> {
                var isValid = true

                if (event.title.isBlank()) {
                    _state.value = _state.value.copy(titleError = mutableStateOf("El título no puede estar vacío."))
                    isValid = false
                } else {
                    _state.value = _state.value.copy(titleError = mutableStateOf(null))
                }

                if (event.description.isBlank()) {
                    _state.value = _state.value.copy(descriptionError = mutableStateOf("La descripción no puede estar vacía."))
                    isValid = false
                } else {
                    _state.value = _state.value.copy(descriptionError = mutableStateOf(null))
                }

                if (isValid) {
                    val note = Note(
                        title = event.title.trim(),
                        description = event.description.trim(),
                        dateAdded = System.currentTimeMillis()
                    )

                    viewModelScope.launch {
                        dao.upsertNote(note)
                        _state.update { currentState ->
                            currentState.copy(
                                noteSaved = true, // Indica que la nota ha sido guardada exitosamente
                                title = mutableStateOf(""), // Resetea el título
                                description = mutableStateOf(""), // Resetea la descripción
                                titleError = mutableStateOf(null), // Limpia los errores
                                descriptionError = mutableStateOf(null)
                            )
                        }
                    }

                    _state.update {
                        it.copy(
                            title = mutableStateOf(""),
                            description = mutableStateOf(""),
                            titleError = mutableStateOf(null),
                            descriptionError = mutableStateOf(null)
                        )
                    }
                }
            }

            NotesEvent.SortNotes -> {
                isSortedByDateAdded.value = !isSortedByDateAdded.value
            }
        }
    }
}