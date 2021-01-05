package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.Credentials;
import com.udacity.jwdnd.course1.cloudstorage.model.Files;
import com.udacity.jwdnd.course1.cloudstorage.model.Notes;
import com.udacity.jwdnd.course1.cloudstorage.services.*;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/home")
public class HomeController {
    private final FileService fileService;
    private final NoteService noteService;
    private final CredentialService credentialService;
    private final UserService userService;
    private final CommonService commonService;
    private final EncryptionService encryptionService;

    public HomeController(FileService fileService, NoteService noteService, CredentialService credentialService, UserService userService,
                          CommonService commonService, EncryptionService encryptionService) {
        this.fileService = fileService;
        this.noteService = noteService;
        this.credentialService = credentialService;
        this.userService = userService;
        this.commonService = commonService;
        this.encryptionService = encryptionService;
    }

    @GetMapping
    public String home(Model model) {
        model.addAttribute("encryptionService", encryptionService);
        return "home";
    }

    @PostMapping("/file-upload")
    public String uploadFile(@RequestParam("fileUpload") MultipartFile file, Model model) {
        String fileUploadError = null;
        int fileLength = 0;
        try {
            fileLength = file.getBytes().length;
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (fileLength < 1) {
            fileUploadError = "You cannot upload an empty file";
        }

        if (fileUploadError == null && fileService.isFileNameAvailable(file.getOriginalFilename())) {
            fileUploadError = "You can not upload a file that has been uploaded before";
        }
        if (fileUploadError == null) {
            int rowsAdded = fileService.uploadFiles(file, commonService.getUserId());
            if (rowsAdded < 0) {
                fileUploadError = "File upload error occurred please try again";
            }
        }
        if (fileUploadError == null) {
            model.addAttribute("success", file);
        } else {
            model.addAttribute("error", fileUploadError);
        }

        return "result";
    }

    @ModelAttribute("files")
    public List<Files> files() {
        return fileService.getAllFiles(commonService.getUserId());
    }

    @GetMapping("/delete-file/{fileId}")
    public String deleteFile(@PathVariable int fileId, Model model) {
        int rowsRemoved = fileService.deleteFile(fileId);
        if (rowsRemoved < 1) {
            model.addAttribute("error", "An error occurred while deleting this file");
        } else {
            model.addAttribute("success", true);
        }
        return "result";
    }

    @GetMapping("/download/{fileId}")
    public ResponseEntity<Resource> downloadFile(@PathVariable int fileId) {
        Files files = fileService.getSingleFile(fileId);
        ByteArrayResource resource = new ByteArrayResource(files.getFileData());

        HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");

        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(Long.parseLong(files.getFileSize()))
                .contentType(MediaType.valueOf(files.getContentType()))
                .body(resource);
    }

    @ModelAttribute("notes")
    public List<Notes> notes() {
        return noteService.getAllNotes(commonService.getUserId());
    }

    @PostMapping("/note")
    public String saveNote(@ModelAttribute("noteForm") Notes notes, Model model) {
        String errorMessage = null;
        notes.setUserId(commonService.getUserId());
        if (notes.getNoteId() == null) {
            int rows = noteService.addNote(notes);
            if (rows < 1) {
                errorMessage = "An error occurred while creating a note, please try again";
            }
        } else {
            int rows = noteService.updateNotes(notes);
            if (rows < 1) {
                errorMessage = "An error occurred while updating this note please try again";
            }
        }
        if (errorMessage == null) {
            model.addAttribute("notes", noteService.getAllNotes(commonService.getUserId()));
            model.addAttribute("success", true);
        } else {
            model.addAttribute("error", errorMessage);
        }
        return "result";
    }

    @GetMapping("/delete-note/{noteId}")
    public String deleteNote(@PathVariable int noteId, Model model) {
        String errorMessage = null;
        int rowsRemoved = noteService.deleteNote(noteId);
        if (rowsRemoved < 1) {
            errorMessage = "An error occurred while deleting this note please try again";
        }
        if (errorMessage == null) {
            model.addAttribute("success", true);
        } else {
            model.addAttribute("error", errorMessage);
        }
        return "result";
    }


    @ModelAttribute("credentials")
    public List<Credentials> credentials() {
        return credentialService.getAllCredentials(commonService.getUserId());
    }

    @PostMapping("/credentials")
    public String saveCredential(@ModelAttribute("credentialForm") Credentials credentials, Model model) {
        String errorMessage = null;
        credentials.setUserId(commonService.getUserId());
        if (credentials.getCredentialId() == null) {
            int rowsAdded = credentialService.addCredential(credentials);
            if (rowsAdded < 1) {
                errorMessage = "An error occurred while adding this credential, please try again";
            }
        } else {
            int rowsUpdated = credentialService.updateCredential(credentials);
            if (rowsUpdated < 1) {
                errorMessage = "An error occurred while updating this credential, please try again";
            }
        }
        if (errorMessage == null) {
            model.addAttribute("credentials", credentialService.getAllCredentials(commonService.getUserId()));
            model.addAttribute("success", true);
        } else {
            model.addAttribute("error", errorMessage);
        }
        return "result";
    }

    @GetMapping("/delete-credential/{credentialId}")
    public String deleteCredential(@PathVariable int credentialId, Model model) {
        int rowsRemoved = credentialService.deleteCredential(credentialId);
        if (rowsRemoved < 1) {
            model.addAttribute("error", "An error occurred while deleting this note");
        } else {
            model.addAttribute("success", true);
        }
        return "result";
    }
}
