package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

import com.example.demo.global.GlobalData;
import com.example.demo.service.CategoryService;
import com.example.demo.service.ProductService;

@Controller
public class HomeController {
	@Autowired
	CategoryService categoryService;
	
	@Autowired
	ProductService productService;
	
	@GetMapping({"/", "/home"})
	public ModelAndView home(Model model) {
		ModelAndView modelandview = new ModelAndView();
		model.addAttribute("cartCount", GlobalData.cart.size());
		modelandview.setViewName("index");
		return modelandview;
}
	@GetMapping("/shop")
	public ModelAndView shop(Model model) {
	model.addAttribute("categories",categoryService.getAllCategory());
	model.addAttribute("cartCount", GlobalData.cart.size());
	model.addAttribute("products",productService.getAllProduct());
	ModelAndView modelandview = new ModelAndView();
	modelandview.setViewName("shop");
	return modelandview;
	
}
	@GetMapping("/shop/category/{id}")
	public ModelAndView shopByCategory(Model model, @PathVariable int id) {
	model.addAttribute("categories",categoryService.getAllCategory());
	model.addAttribute("cartCount", GlobalData.cart.size());
	model.addAttribute("products",productService.getAllProductByCategoryId(id));
	ModelAndView modelandview = new ModelAndView();
	modelandview.setViewName("shop");
	return modelandview;
}
	@GetMapping("/shop/viewproduct/{id}")
	public ModelAndView viewproduct(Model model, @PathVariable int id) {
	model.addAttribute("product", productService.getProductById(id).get());
	model.addAttribute("cartCount", GlobalData.cart.size());
	ModelAndView modelandview = new ModelAndView();
	modelandview.setViewName("viewProduct");
	return modelandview;
	
}}