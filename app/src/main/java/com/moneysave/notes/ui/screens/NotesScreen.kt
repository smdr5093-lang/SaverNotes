
package com.moneysave.notes.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.PushPin
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.moneysave.notes.MoneySaveApplication
import com.moneysave.notes.data.Note
import com.moneysave.notes.viewmodel.NoteViewModel
import com.moneysave.notes.viewmodel.NoteViewModelFactory

@Composable
fun NotesScreen(paddingValues: PaddingValues) {
    val context = LocalContext.current
    val app = context.applicationContext as MoneySaveApplication
    val vm: NoteViewModel = viewModel(
        factory = NoteViewModelFactory(app.noteRepository)
    )
    val notes by vm.notes.collectAsState()

    var search by remember { mutableStateOf("") }
    var editingNote by remember { mutableStateOf<Note?>(null) }
    var showNewNote by remember { mutableStateOf(false) }

    val filteredNotes = notes.filter {
        it.title.contains(search, ignoreCase = true) ||
        it.content.contains(search, ignoreCase = true) ||
        it.category.contains(search, ignoreCase = true)
    }

    Scaffold(
        modifier = Modifier.padding(paddingValues),
        floatingActionButton = {
            FloatingActionButton(onClick = { showNewNote = true }) {
                Icon(Icons.Default.Add, contentDescription = "New note")
            }
        }
    ) { innerPadding ->
        Column(
            Modifier.fillMaxSize().padding(innerPadding).padding(16.dp)
        ) {
            Text("My Notes", style = MaterialTheme.typography.headlineMedium)
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(
                value = search,
                onValueChange = { search = it },
                label = { Text("Search notes") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            Spacer(Modifier.height(12.dp))

            if (filteredNotes.isEmpty()) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(if (search.isBlank()) "No notes yet. Tap + to add one." else "No matching notes found.")
                }
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(filteredNotes, key = { it.id }) { note ->
                        Card(Modifier.fillMaxWidth()) {
                            Column(Modifier.padding(16.dp)) {
                                Row(
                                    Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        note.title,
                                        style = MaterialTheme.typography.titleLarge,
                                        modifier = Modifier.weight(1f)
                                    )
                                    if (note.isPinned) {
                                        Icon(Icons.Default.PushPin, contentDescription = "Pinned")
                                    }
                                }
                                Spacer(Modifier.height(6.dp))
                                Text(note.content)
                                Spacer(Modifier.height(6.dp))
                                Text("Category: ${note.category}", style = MaterialTheme.typography.bodySmall)
                                Spacer(Modifier.height(8.dp))
                                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                    OutlinedButton(onClick = {
                                        editingNote = note
                                    }) {
                                        Text("Edit")
                                    }
                                    OutlinedButton(onClick = {
                                        vm.updateNote(
                                            note.copy(
                                                isPinned = !note.isPinned,
                                                updatedAt = System.currentTimeMillis()
                                            )
                                        )
                                    }) {
                                        Text(if (note.isPinned) "Unpin" else "Pin")
                                    }
                                    TextButton(onClick = {
                                        vm.deleteNote(note)
                                    }) {
                                        Text("Delete")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    if (showNewNote) {
        NoteEditorDialog(
            initialNote = null,
            onDismiss = { showNewNote = false },
            onSave = { title, content, category ->
                vm.addNote(Note(title = title, content = content, category = category))
                showNewNote = false
            }
        )
    }

    val noteToEdit = editingNote
    if (noteToEdit != null) {
        NoteEditorDialog(
            initialNote = noteToEdit,
            onDismiss = { editingNote = null },
            onSave = { title, content, category ->
                vm.updateNote(
                    noteToEdit.copy(
                        title = title,
                        content = content,
                        category = category,
                        updatedAt = System.currentTimeMillis()
                    )
                )
                editingNote = null
            }
        )
    }
}

@Composable
private fun NoteEditorDialog(
    initialNote: Note?,
    onDismiss: () -> Unit,
    onSave: (String, String, String) -> Unit
) {
    var title by remember(initialNote?.id) { mutableStateOf(initialNote?.title ?: "") }
    var content by remember(initialNote?.id) { mutableStateOf(initialNote?.content ?: "") }
    var category by remember(initialNote?.id) { mutableStateOf(initialNote?.category ?: "Other") }
    var error by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (initialNote == null) "New Note" else "Edit Note") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Title") },
                    singleLine = true
                )
                OutlinedTextField(
                    value = content,
                    onValueChange = { content = it },
                    label = { Text("Content") },
                    minLines = 3
                )
                OutlinedTextField(
                    value = category,
                    onValueChange = { category = it },
                    label = { Text("Category") },
                    singleLine = true
                )
                if (error.isNotBlank()) {
                    Text(error, color = MaterialTheme.colorScheme.error)
                }
            }
        },
        confirmButton = {
            Button(onClick = {
                if (title.isBlank()) {
                    error = "Title enter karo."
                } else {
                    onSave(title.trim(), content.trim(), category.trim().ifBlank { "Other" })
                }
            }) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}
