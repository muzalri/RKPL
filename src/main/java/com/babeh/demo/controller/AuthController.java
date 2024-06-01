package com.babeh.demo.controller;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

import com.babeh.demo.dto.UserDto;
import com.babeh.demo.model.Menu;
import com.babeh.demo.model.Transaksi;
import com.babeh.demo.model.Transaksi;
import com.babeh.demo.model.User;
import com.babeh.demo.repository.MenuRepository;
import com.babeh.demo.service.MenuService;
import com.babeh.demo.service.UserService;
import com.babeh.demo.service.TransaksiService;
import com.babeh.demo.service.TransaksiService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.util.Collections;

import java.util.List;
import org.springframework.web.bind.annotation.RequestParam;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;


@Controller
public class AuthController {

    private UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    // handler method to handle home page request
    @GetMapping("/")
    public String home(){
        return "index";
    }

    // handler method to handle login request
    @GetMapping("/login")
    public String login(){
        return "login";
    }

    // handler method to handle user registration form request
    @GetMapping("/register")
    public String showRegistrationForm(Model model){
        // create model object to store form data
        UserDto user = new UserDto();
        model.addAttribute("user", user);
        return "register";
    }

    // handler method to handle register user form submit request
    @PostMapping("/register/save")
    public String registration(@Valid @ModelAttribute("user") UserDto user,
                               BindingResult result,
                               Model model){
        User existing = userService.findByUsername(user.getUsername());
        if (existing != null) {
            result.rejectValue("username", null, "There is already an account registered with that username");
        }
        if (result.hasErrors()) {
            model.addAttribute("user", user);
            return "register";
        }
        userService.saveUser(user);
        return "redirect:/users";
    }

    // handler method to handle list of users
    @GetMapping("/users")
    public String users(Model model){
        List<UserDto> users = userService.findAllUsers();
        model.addAttribute("users", users);
        return "users";
    }

    @GetMapping("/edit/{username}")
    public String showEditForm(@PathVariable("username") String username,
                                Model model) {
        UserDto userDto = userService.findUserDtoByUsername(username);
        model.addAttribute("user", userDto);
        return "edit-user";
    }

    @PostMapping("/edit/{username}/save")
    public String editUser(@PathVariable("username") String username, 
                    @Valid @ModelAttribute("user") UserDto user, 
                    Model model) {
    userService.updateUser(user, username);
    return "redirect:/users";
    }

    @RequestMapping("/delete/{username}")
    public String deleteUser(@PathVariable("username") String username) {
        userService.deleteUserByUsername(username);
        return "redirect:/users"; // Mengalihkan ke halaman daftar pengguna setelah penghapusan
    }





















     @Autowired
    private MenuService menuService;

    
    @GetMapping("/menu")
    public String semuaMenu(Model model) {
        List<Menu> menus = menuService.semuaMenu();
        model.addAttribute("menus", menus);
        return "menu";
    }

    @GetMapping("/menu/tambahmenu")
    public String showTambahMenuForm(Model model) {
        model.addAttribute("menu", new Menu());
        return "tambah_menu";
    }

    @PostMapping("/menu/tambahmenu/save")
    public String tambahMenu(@ModelAttribute Menu menu) {
        menuService.tambahMenu(menu);
        return "redirect:/menu"; // Redirect to the menu list page after submission
    }

    @GetMapping("/menu/edit/{id}")
    public String editmenu(@PathVariable Long id, Model model) {
        Menu menu = menuService.dapatkanMenu(id);
        if (menu != null) {
            model.addAttribute("menu", menu);
            return "edit-menu"; // Nama template Thymeleaf untuk halaman edit menu
        } else {
            return "menu-not-found"; // Template yang ditampilkan jika menu tidak ditemukan
        }
    }

    @PostMapping("/menu/edit/{id}/save")
    public String editmenusave(@PathVariable Long id, @ModelAttribute Menu menu) {
        // Mendapatkan menu yang sudah ada dari database berdasarkan ID
        Menu existingMenu = menuService.dapatkanMenu(id);
        
        // Memeriksa apakah menu yang dimaksud benar-benar ada
        if (existingMenu != null) {
            // Mengatur ID dari objek menu yang dikirim dari formulir HTML
            menu.setId(id);
            
            // Memperbarui menu di dalam database
            menuService.updateMenu(menu);
        }
        
        return "redirect:/menu"; // Redirect kembali ke halaman menu setelah berhasil menyimpan
    }
    

    @DeleteMapping("menu/hapusmenu/{id}")
    public void hapusMenu(@PathVariable Long id) {
        menuService.hapusMenu(id);
    }

   






















    @Autowired
    private TransaksiService transaksiService;


@GetMapping("/transaksi/create")
    public String showCreateForm(Model model) {
        model.addAttribute("transaksi", new Transaksi());
        model.addAttribute("menus", menuService.semuaMenu());
        model.addAttribute("users", userService.findAllUsers());
        return "transaction-form";
    }

    @PostMapping("/transaksi/create/save")
    public String createTransaksi(@ModelAttribute Transaksi transaksi) {
        transaksiService.saveTransaksi(transaksi);
        return "redirect:/transaksi";
    }

    @GetMapping("/transaksi")
    public String listTransaksi(Model model) {
        model.addAttribute("transaksiList", transaksiService.getAllTransaksi());
        return "transaction-list";
    }
}




