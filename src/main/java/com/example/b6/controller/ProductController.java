package com.example.b6.controller;

import com.example.b6.entity.Product;
import com.example.b6.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@RequestMapping("/products")
@Controller
public class ProductController {
    @Autowired
    private ProductService productService;
    @GetMapping("")
    public String Index(Model model)
    {
        model.addAttribute("Listproduct",productService.GetAll());
        return "products/index";
    }
    @GetMapping("/create")
    public String create(Model model)
    {
        model.addAttribute("product",new Product());
        return "products/create";
    }
    @PostMapping("/create")
    public String create(Product np,Model model,@RequestParam MultipartFile imageProduct )
    {
        if(imageProduct!= null&&imageProduct.getSize()>0){
            try{
                File savefile=new ClassPathResource("static/image").getFile();
                String newImageFile= UUID.randomUUID()+".png";
                Path path= Paths.get(savefile.getAbsolutePath()+File.separator+newImageFile);
                Files.copy(imageProduct.getInputStream(),path, StandardCopyOption.REPLACE_EXISTING);
                np.setImage(newImageFile);
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
        productService.add(np);
        return "redirect:/products";
    }

}
