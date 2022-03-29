package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

import com.example.demo.global.GlobalData;
import com.example.demo.model.Product;
import com.example.demo.service.ProductService;

@Controller
public class CartController {
	@Autowired
	ProductService productservice;
	
	@GetMapping("/addToCart/{id}")
	public ModelAndView addToCart(@PathVariable int id) {
		GlobalData.cart.add(productservice.getProductById(id).get());
		ModelAndView modelandview = new ModelAndView();
		modelandview.setViewName("redirect:/shop");
		return modelandview;
	}
	@GetMapping("/cart")
	public ModelAndView cartGet(Model model) {
		model.addAttribute("cartCount", GlobalData.cart.size());
		model.addAttribute("total", GlobalData.cart.stream().mapToDouble(Product::getPrice).sum());
		model.addAttribute("cart", GlobalData.cart);
		ModelAndView modelandview= new ModelAndView();
		modelandview.setViewName("cart");
		return modelandview;
	}
    @GetMapping("/cart/removeItem/{index}")
    public ModelAndView cartItemRemove(@PathVariable int index) {
    	GlobalData.cart.remove(index);
    	ModelAndView modelandview = new ModelAndView();
    	modelandview.setViewName("redirect:/cart");
    	return modelandview;
    			
    	
    }
    @GetMapping("/checkout")
    public ModelAndView checkout(Model model) {
		model.addAttribute("total", GlobalData.cart.stream().mapToDouble(Product::getPrice).sum());
		ModelAndView modelandview = new ModelAndView();
		modelandview.setViewName("checkout");
		return modelandview;
    }
    @GetMapping("/checkout/payNow")
    public ModelAndView pay() {
    	ModelAndView modelandview = new ModelAndView();
    	modelandview.setViewName("orderPlaced");
    	return modelandview;
    }
    
}
