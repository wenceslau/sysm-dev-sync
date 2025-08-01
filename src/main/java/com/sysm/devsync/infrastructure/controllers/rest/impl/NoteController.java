package com.sysm.devsync.infrastructure.controllers.rest.impl;

import com.sysm.devsync.application.NoteService;
import com.sysm.devsync.domain.Page;
import com.sysm.devsync.domain.Pagination;
import com.sysm.devsync.domain.SearchQuery;
import com.sysm.devsync.infrastructure.controllers.rest.NoteAPI;
import com.sysm.devsync.infrastructure.controllers.dto.request.NoteCreateUpdate;
import com.sysm.devsync.infrastructure.controllers.dto.response.NoteResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Map;

@RestController
public class NoteController extends AbstractController implements NoteAPI {

    private final NoteService noteService;
    // In a real app, this would come from the Spring Security context

    public NoteController(NoteService noteService) {
        this.noteService = noteService;
    }

    @Override
    public ResponseEntity<?> createNote(@Valid @RequestBody NoteCreateUpdate request) {
        var response = noteService.createNote(request, authenticatedUserId());
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri();
        return ResponseEntity.created(location).body(response);
    }

    @Override
    public ResponseEntity<NoteResponse> getNoteById(String id) {
        var note = noteService.getNoteById(id);
        return ResponseEntity.ok(NoteResponse.from(note));
    }

    @Override
    public Pagination<NoteResponse> searchNotes(int pageNumber, int pageSize, String sort,
                                                String direction, Map<String, String> filters) {

        var page = Page.of(pageNumber, pageSize, sort, direction);
        var searchQuery = SearchQuery.of(page, filters);

        return noteService.getAllNotes(searchQuery).map(NoteResponse::from);
    }

    @Override
    public ResponseEntity<?> updateNote(String id, @Valid @RequestBody NoteCreateUpdate request) {
        noteService.updateNote(id, request);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<?> updateNoteContent(String id, @RequestBody NoteCreateUpdate request) {
        // Calling the refactored service method
        noteService.updateNoteContent(id, request);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<?> deleteNote(String id) {
        noteService.deleteNote(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<?> addTag(String id, String tagId) {
        noteService.addTagToNote(id, tagId);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<?> removeTag(String id, String tagId) {
        noteService.removeTagFromNote(id, tagId);
        return ResponseEntity.noContent().build();
    }
}
