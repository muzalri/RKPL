package com.babeh.demo.controller;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

import com.babeh.demo.dto.UserDto;
import com.babeh.demo.model.Menu;
import com.babeh.demo.model.Transaksi;
import com.babeh.demo.model.User;
import com.babeh.demo.repository.MenuRepository;
import com.babeh.demo.service.MenuService;
import com.babeh.demo.service.UserService;
import com.itextpdf.io.source.ByteArrayOutputStream;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.babeh.demo.service.TransaksiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
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
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.UnitValue;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Collections;

import org.springframework.web.bind.annotation.RequestParam;


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
    

    @RequestMapping("menu/hapusmenu/{id}")
    public String hapusMenu(@PathVariable Long id) {
        menuService.hapusMenu(id);
        return "redirect:/menu";
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
    public String createTransaksi(@ModelAttribute Transaksi transaksi,
    @RequestParam("username") String username,
    @RequestParam("menuIds") List<Long> menuIds,
    @RequestParam("quantities") List<Integer> quantities) {
transaksiService.saveTransaksi(transaksi, username, menuIds, quantities);
return "redirect:/transaksi";
}
        
    @GetMapping("/transaksi")
    public String listTransaksi(Model model) {
        model.addAttribute("transaksiList", transaksiService.getAllTransaksi());
        return "transaction-list";
    }

    @GetMapping("/transaksi/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        Transaksi transaksi = transaksiService.findById(id);
        model.addAttribute("transaksi", transaksi);
        model.addAttribute("menus", menuService.semuaMenu());
        model.addAttribute("users", userService.findAllUsers());
        return "edit-transaction-form";
    }

    @PostMapping("/transaksi/edit/{id}")
    public String updateTransaksi(@PathVariable("id") Long id,
                                  @ModelAttribute Transaksi transaksi,
                                  @RequestParam("username") String username,
                                  @RequestParam("menuIds") List<Long> menuIds,
                                  @RequestParam("quantities") List<Integer> quantities) {
        transaksiService.updateTransaksi(id, transaksi, username, menuIds, quantities);
        return "redirect:/transaksi";
    }

    @GetMapping("/transaksi/delete/{id}")
    public String deleteTransaksi(@PathVariable("id") Long id) {
        transaksiService.deleteTransaksi(id);
        return "redirect:/transaksi";
    }

    @GetMapping("/transaksi/struk/{id}")
    public String struk (@PathVariable("id") Long id, Model model) {
        model.addAttribute("transaksi",transaksiService.findById(id));
        return "struk";
    }


    @GetMapping("/laporan")
    public String listLaporan(Model model) {
        model.addAttribute("transaksiList", transaksiService.getAllTransaksi());
        return "laporan";
    }

    
    @GetMapping("/laporan/cetak_pdf")
    public ResponseEntity<InputStreamResource> cetakPdf() throws IOException {
        List<Transaksi> transaksiList = transaksiService.getAllTransaksi();

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(out);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);

        // Add title
        document.add(new Paragraph("Daftar Transaksi"));

        // Create table with columns
        Table table = new Table(UnitValue.createPercentArray(new float[]{1, 3, 3, 2, 2, 3, 3}));
        table.setWidth(UnitValue.createPercentValue(100));

        // Add table headers
        table.addHeaderCell(new Cell().add(new Paragraph("No.")));
        table.addHeaderCell(new Cell().add(new Paragraph("Nama Pelanggan")));
        table.addHeaderCell(new Cell().add(new Paragraph("Nama Menu")));
        table.addHeaderCell(new Cell().add(new Paragraph("Jumlah")));
        table.addHeaderCell(new Cell().add(new Paragraph("Total")));
        table.addHeaderCell(new Cell().add(new Paragraph("Nama Pegawai")));
        table.addHeaderCell(new Cell().add(new Paragraph("Tanggal")));

        // Add table rows
        for (int i = 0; i < transaksiList.size(); i++) {
            Transaksi transaksi = transaksiList.get(i);
            table.addCell(new Cell().add(new Paragraph(String.valueOf(i + 1))));
            table.addCell(new Cell().add(new Paragraph(transaksi.getNamaPelanggan())));

            // Get the names of the items
            String itemNames = transaksi.getItems().stream()
                    .map(Menu::getNamaMenu) // Assuming 'Menu' class has 'getNamaMenu' method
                    .collect(Collectors.joining(", "));
            table.addCell(new Cell().add(new Paragraph(itemNames)));

            table.addCell(new Cell().add(new Paragraph(String.valueOf(transaksi.getKuantitas()))));
            table.addCell(new Cell().add(new Paragraph(String.valueOf(transaksi.getTotal()))));
            table.addCell(new Cell().add(new Paragraph(transaksi.getNamaPegawai())));
            table.addCell(new Cell().add(new Paragraph(transaksi.getCreatedAt().toString())));
        }

        document.add(table);
        document.close();

        ByteArrayInputStream bis = new ByteArrayInputStream(out.toByteArray());
        InputStreamResource resource = new InputStreamResource(bis);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=transactions.pdf");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(resource);
    }

}




