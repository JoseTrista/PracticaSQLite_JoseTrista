package trista.josecarlos.practicasqlite_josetrista.presentation

import trista.josecarlos.practicasqlite_josetrista.data.Note

sealed interface NotesEvent{
    object SortNotes: NotesEvent

    data class DeleteNote(val note: Note): NotesEvent

    data class SaveNote(
        val title: String,
        val description: String
    ): NotesEvent
}
