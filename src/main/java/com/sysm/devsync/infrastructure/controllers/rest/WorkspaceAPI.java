package com.sysm.devsync.infrastructure.controllers.rest;

import com.sysm.devsync.domain.Pagination;
import com.sysm.devsync.infrastructure.config.security.IsMemberOrAdmin;
import com.sysm.devsync.infrastructure.controllers.dto.request.WorkspaceCreateUpdate;
import com.sysm.devsync.infrastructure.controllers.dto.response.WorkspaceResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequestMapping("workspaces")
@Tag(name = "Workspaces")
public interface WorkspaceAPI {

    @IsMemberOrAdmin
    @PostMapping
    @Operation(summary = "Create a new workspace")
    @ApiResponse(responseCode = "201", description = "Workspace created successfully")
    @ApiResponse(responseCode = "400", description = "Invalid input data")
    ResponseEntity<?> create(@RequestBody WorkspaceCreateUpdate request);

    @IsMemberOrAdmin
    @GetMapping("/{id}")
    @Operation(summary = "Get a workspace by its ID")
    ResponseEntity<WorkspaceResponse> getById(@PathVariable("id") String id);

    @IsMemberOrAdmin
    @GetMapping
    @Operation(summary = "Search for workspaces with pagination")
    Pagination<WorkspaceResponse> search(
            @RequestParam(name = "pageNumber", defaultValue = "0") int pageNumber,
            @RequestParam(name = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam(name = "sort", defaultValue = "name") String sort,
            @RequestParam(name = "direction", defaultValue = "asc") String direction,
            @RequestParam Map<String, String> filters
    );

    @IsMemberOrAdmin
    @PutMapping("/{id}")
    @Operation(summary = "Update a workspace's details")
    @ApiResponse(responseCode = "204", description = "Workspace updated successfully")
    @ApiResponse(responseCode = "404", description = "Workspace not found")
    ResponseEntity<?> update(@PathVariable("id") String id, @RequestBody WorkspaceCreateUpdate request);

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @Operation(summary = "Delete a workspace")
    @ApiResponse(responseCode = "204", description = "Workspace deleted successfully")
    @ApiResponse(responseCode = "404", description = "Workspace not found")
    ResponseEntity<?> delete(@PathVariable("id") String id);

    @IsMemberOrAdmin
    @PatchMapping("/{id}/privacy")
    @Operation(summary = "Change a workspace's privacy setting")
    ResponseEntity<?> changePrivacy(@PathVariable("id") String id, @RequestParam("isPrivate") boolean isPrivate);

    @IsMemberOrAdmin
    @PostMapping("/{id}/members/{memberId}")
    @Operation(summary = "Add a member to a workspace")
    ResponseEntity<?> addMember(@PathVariable("id") String id, @PathVariable("memberId") String memberId);

    @IsMemberOrAdmin
    @DeleteMapping("/{id}/members/{memberId}")
    @Operation(summary = "Remove a member from a workspace")
    ResponseEntity<?> removeMember(@PathVariable("id") String id, @PathVariable("memberId") String memberId);

    @PreAuthorize("hasAnyRole('ADMIN')")
    @PatchMapping("/{id}/owner/{newOwnerId}")
    @Operation(summary = "Change the owner of a workspace")
    ResponseEntity<?> changeOwner(@PathVariable("id") String id, @PathVariable("newOwnerId") String newOwnerId);
}
