package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.example.demo.dto.ProductDTO;
import com.example.demo.model.Category;
import com.example.demo.model.Product;
import com.example.demo.service.CategoryService;
import com.example.demo.service.ProductService;

@RestController
public class AdminController {
	public static String uploadDir = System.getProperty("user.dir") + "/src/main/resources/static/productImage";
	@Autowired
	CategoryService categoryService;

	@Autowired
	ProductService productService;

	@GetMapping("/admin")
	public ModelAndView adminHome() {
		ModelAndView modelandview = new ModelAndView();
		modelandview.setViewName("adminHome");
		return modelandview;
	}

	@GetMapping("/admin/categories")
	public ModelAndView getCateg(Model model) {
		model.addAttribute("categories", categoryService.getAllCategory());
		ModelAndView modelandview = new ModelAndView();
		modelandview.setViewName("categories");
		return modelandview;

	}

	@GetMapping("/admin/categories/add")
	public ModelAndView postCateg(Model model) {
		ModelAndView modelandview = new ModelAndView();
		model.addAttribute("category", new Category());
		modelandview.setViewName("categoriesAdd");
		return modelandview;
	}

	@PostMapping("/admin/categories/add")
	public ModelAndView postCatAdd(@ModelAttribute("category") Category category) {
		ModelAndView modelandview = new ModelAndView();
		categoryService.addCategory(category);
		modelandview.setViewName("redirect:/admin/categories");
		return modelandview;

	}

	@GetMapping("/admin/categories/delete/{id}")
	public ModelAndView deletecat(@PathVariable int id) {
		ModelAndView modelandview = new ModelAndView();
		categoryService.removeCategoryById(id);
		modelandview.setViewName("redirect:/admin/categories");
		return modelandview;
	}

	@GetMapping("/admin/categories/update/{id}")
	public ModelAndView updatecat(@PathVariable int id, Model model) {
		Optional<Category> category = categoryService.getCategoryById(id);
		ModelAndView modelandview = new ModelAndView();
		if (category.isPresent()) {
			model.addAttribute("category", category.get());
			ModelAndView modelAndview = new ModelAndView();
			categoryService.removeCategoryById(id);
			modelAndview.setViewName("categoriesAdd");
			return modelAndview;
		} else
			modelandview.setViewName("404");
		return modelandview;

	}

	@GetMapping("/admin/products")
	public ModelAndView getProd(Model model) {
		ModelAndView modelandview = new ModelAndView();
		model.addAttribute("products", productService.getAllProduct());
		modelandview.setViewName("products");
		return modelandview;
	}

	@GetMapping("/admin/products/add")
	public ModelAndView getProdadd(Model model) {
		ModelAndView modelandview = new ModelAndView();
		model.addAttribute("productDTO", new ProductDTO());
		model.addAttribute("categories", categoryService.getAllCategory());
		modelandview.setViewName("productsAdd");
		return modelandview;
	}

	@PostMapping("/admin/products/add")
	public ModelAndView productAddPost(@ModelAttribute("ProductDTO") ProductDTO productDTO,
			@RequestParam("productImage") MultipartFile file, @RequestParam("imgName") String imgName)
			throws IOException {
		ModelAndView modelandview = new ModelAndView();
		Product product = new Product();
		product.setId(productDTO.getId());
		product.setName(productDTO.getName());

		product.setCategory(categoryService.getCategoryById(productDTO.getCategoryId()).get());
		product.setPrice(productDTO.getPrice());
		product.setWeight(productDTO.getWeight());
		product.setDescription(productDTO.getDescription());
		String imageUUID;
		if (!file.isEmpty()) {
			imageUUID = file.getOriginalFilename();
			Path fileNameAndPath = Paths.get(uploadDir, imageUUID);
			Files.write(fileNameAndPath, file.getBytes());
		} else {
			imageUUID = imgName;
		}
		product.setImageName(imageUUID);
		productService.addProduct(product);
		modelandview.setViewName("redirect:/admin/products");
		return modelandview;
	}

	@GetMapping("/admin/product/delete/{id}")
	public ModelAndView deleteProduct(@PathVariable long id) {
		ModelAndView modelandview = new ModelAndView();
		productService.removeProductById(id);
		modelandview.setViewName("redirect:/admin/products");
		return modelandview;
	}

	@GetMapping("/admin/product/update/{id}")
	public ModelAndView updateProductGet(@PathVariable long id, Model model) {
		Product product = productService.getProductById(id).get();
		ProductDTO productDTO = new ProductDTO();
		productDTO.setId(product.getId());
		productDTO.setName(product.getName());
		productDTO.setCategoryId(product.getCategory().getId());
		productDTO.setPrice(product.getPrice());
		productDTO.setWeight(product.getWeight());
		productDTO.setDescription(product.getDescription());
		productDTO.setImageName(product.getImageName());

		model.addAttribute("categories", categoryService.getAllCategory());
		model.addAttribute("productDTO", productDTO);
		ModelAndView modelandview = new ModelAndView();
		modelandview.setViewName("productsAdd");
		return modelandview;
	}
}