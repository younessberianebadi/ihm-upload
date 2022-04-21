package io.digiwise.ldapihm;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import io.digiwise.ldapihm.model.Authority;
import io.digiwise.ldapihm.model.User;
import io.digiwise.ldapihm.model.UserCreation;
import io.digiwise.ldapihm.repository.AuthorityRepository;
import io.digiwise.ldapihm.repository.UserRepository;
import io.digiwise.ldapihm.storage.StorageFileNotFoundException;
import io.digiwise.ldapihm.storage.StorageService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class HomeController {

    private final StorageService storageService;

    @Autowired
    private AuthorityRepository authorityRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    public HomeController(StorageService storageService) {
        this.storageService = storageService;
    }

    @GetMapping("/home")
    public String index(Model model) {
        model.addAttribute("files", storageService.loadAll().map(
                path -> MvcUriComponentsBuilder.fromMethodName(HomeController.class,
                        "serveFile", path.getFileName().toString()).build().toUri().toString().split("/files/")[1])
                .collect(Collectors.toList()));
        List<Authority> authorities = (List<Authority>) authorityRepository.findAll();
        model.addAttribute("users", authorities);
        return "index";
    }

    @GetMapping("/upload")
    public String upload(Model model) throws IOException {
        model.addAttribute("files", storageService.loadAll().map(
                path -> MvcUriComponentsBuilder.fromMethodName(HomeController.class,
                        "serveFile", path.getFileName().toString()).build().toUri().toString().split("/files/")[1])
                .collect(Collectors.toList()));
        return "upload";
    }

    @GetMapping("/files/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {

        Resource file = storageService.loadAsResource(filename);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }

    @PostMapping("/upload")
    public String handleFileUpload(@RequestParam("file") MultipartFile file,
                                   RedirectAttributes redirectAttributes) {

        storageService.store(file);
        redirectAttributes.addFlashAttribute("message",
                "Vous avez chargé avec succès le fichier " + file.getOriginalFilename() + "!");

        return "redirect:/upload";
    }

    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/admin")
    public String admin(Model model) {
        List<Authority> authorities = (List<Authority>) authorityRepository.findAll();
        model.addAttribute("users", authorities);
        model.addAttribute("usertobecreated", new UserCreation());
        return "admin";
    }


    @RequestMapping(value = "saveuser", method = RequestMethod.POST)
    public String save(UserCreation userCreation){
        User user = new User(userCreation.getUsername(), userCreation.getPassword(), true);
        Authority authority = new Authority(userCreation.getUsername(), userCreation.getAuthority());
        userRepository.insert(user);
        authorityRepository.insert(authority);
        return "redirect:/admin";

    }

    @GetMapping(value = "/login")
    public String main() {
        return "authentification";
    }

    @GetMapping(value = "/accessDenied")
    public String accessdenied() {
        return "accessDenied";
    }

}
