package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.NotesMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Notes;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoteService {

    private final NotesMapper notesMapper;

    public NoteService(NotesMapper notesMapper) {
        this.notesMapper = notesMapper;
    }

    public List<Notes> getAllNotes(int userId) {
        return notesMapper.getAllNotesByUserId(userId);
    }

    public int addNote(Notes notes) {
        return notesMapper.insert(notes);
    }

    public int deleteNote(int noteId) {
        return notesMapper.delete(noteId);
    }

    public int updateNotes(Notes notes) {
        return notesMapper.update(notes);
    }
}
