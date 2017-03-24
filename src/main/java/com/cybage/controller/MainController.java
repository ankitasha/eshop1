package com.cybage.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.cybage.dao.OrderDao;
import com.cybage.dao.ProductDao;
import com.cybage.entity.Product;
import com.cybage.model.CartInfo;
import com.cybage.model.CustomerInfo;
import com.cybage.model.PaginationResult;
import com.cybage.model.ProductInfo;
import com.cybage.util.Utils;
import com.cybage.validator.CustomerInfoValidator;

@Controller
@Transactional
// for enabling hibernate transactions
@EnableWebMvc
// enabling redirecting attributes
public class MainController
{
 @Autowired
 private OrderDao orderDao;
 
 @Autowired
 private ProductDao productDao;
 
 @Autowired
 private CustomerInfoValidator customerInfoValidator;

@InitBinder
 public void myInitBinder(WebDataBinder dataBinder)
 {
	 Object target= dataBinder.getTarget();
	 if(target== null)
		 {return;}
	 System.out.println("Target="+target);
	 
	 // For cart form 
	 // (@ModelAttribute("cartForm") @Validated CartInfo cartForm)
     if (target.getClass() == CartInfo.class) {
     }
	 
	 //  For Customer Form
     //@ModelAttributr("customerForm")@Validated CustomerInfo 
     // CustomerForm
     
	 if(target.getClass()== CustomerInfo.class)
	 {
		 dataBinder.setValidator(customerInfoValidator);
		 
	 }
 }
 
 @RequestMapping("/403")
 public String accessDenied()
 {
	 return "/403";
 }
 
 /*@RequestMapping("/")
 public String home()
 {
	 return "index";
 }*/
   
 @RequestMapping(value="/", method = RequestMethod.GET)
 public ModelAndView visitHome() {
     return new ModelAndView("index");
 }
 // Product list page 
 @RequestMapping({"/productList"})
 public String listProductHandler(Model model,//
		  @RequestParam(value = "name", defaultValue = "") String likeName,
		  @RequestParam(value = "page", defaultValue = "1") int page)
 {
	final int maxResult = 5;
	final int maxNavigationPage = 10;

    PaginationResult<ProductInfo> result = productDao.queryProducts(page, //
            maxResult, maxNavigationPage, likeName);

    model.addAttribute("paginationProducts", result);
    return "productList";
}

 @RequestMapping({"/buyProduct"})
 public String listProductHandler(HttpServletRequest request, Model model,
		 @RequestParam(value="code", defaultValue = "")String code)
 {
	 Product product= null;
	 if(code != null && code.length()>0)
	 {
		 product= productDao.findProduct(code);
	 }
	 
	 if(product!= null)
	 {
	   CartInfo cartInfo = Utils.getCartInSession(request);
	   
	   ProductInfo productInfo = new ProductInfo(product);
	   
	   cartInfo.addProduct(productInfo, 1);
	 }
	return "redirect:/shoppingCart";
 }
 
 
 @RequestMapping({"/shoppingCartRemoveProduct"})
 public String removeProductHandler(HttpServletRequest request,Model model,
		  @RequestParam(value = "code", defaultValue = "") String code)
 	{
  Product product= null;
  if (code != null && code.length() > 0) {
      product = productDao.findProduct(code);
  }
  if (product != null) {

      // Cart Info stored in Session.
      CartInfo cartInfo = Utils.getCartInSession(request);

      ProductInfo productInfo = new ProductInfo(product);

      cartInfo.removeProduct(productInfo);

  }	 
return "redirect:/shoppingCart";
 	}
 
 
 
 // post:updating shopping cart quantity
 @RequestMapping(value = { "/shoppingCart" }, method = RequestMethod.POST)
 public String shoppingCartUpdateQuantity(HttpServletRequest request,
		    Model model , @ModelAttribute("cartForm") CartInfo cartForm)
 {
	 CartInfo cartInfo = Utils.getCartInSession(request);
	 cartInfo.updateQuantity(cartForm);
	return "redirect:/shoppingCart";
 } 
 
 
// get:show cart
@RequestMapping(value={"/shoppingCart"},method= RequestMethod.GET)
 public String shoppingCartHandler(HttpServletRequest request, Model model)
 {
	CartInfo myCart= Utils.getCartInSession(request);
	
	model.addAttribute("cartForm",myCart);
	
	return "shoppingCart";
 }
// get: Enter customer information

@RequestMapping(value={"/shoppingCartCustomer"}, method= RequestMethod.GET)
public String shoppingCartCustomerForm(HttpServletRequest request, Model model)
{  
	CartInfo cartInfo= Utils.getCartInSession(request);
	
	if(cartInfo.isEmpty())
	{
		return "redirect:/shoppingCart";
	}
	CustomerInfo customerInfo= cartInfo.getCustomerInfo();
	if(customerInfo== null)
	{
	customerInfo = new CustomerInfo();	
	}
	
	model.addAttribute("customerForm", customerInfo);
	
	return "shoppingCartCustomer";	
	} 
	 
	 
// post : Save customer information
@RequestMapping(value={"/shoppingCartCustomer"}, method= RequestMethod.POST)
public String shoppingCartCustomerSave(HttpServletRequest request,
		Model model, @ModelAttribute("customerForm")
         @Validated CustomerInfo customerForm,
         BindingResult result, final RedirectAttributes redirectAttributes)
{
	if(result.hasErrors())
	{
	customerForm.setValid(false);	
	return "shoppingCartCustomer";
	}
	customerForm.setValid(true);
	CartInfo cartInfo = Utils.getCartInSession(request);
	cartInfo.setCustomerInfo(customerForm);
	return "redirect:/shoppingCartConfirmation";
	
}
 
// get: review cart to confirm
@RequestMapping(value={"/shoppingCartConfirmation"}, method = RequestMethod.GET)
public String shoppingCartConfirmationReview(HttpServletRequest request, Model model)
{
	CartInfo cartInfo = Utils.getCartInSession(request);
	if(cartInfo.isEmpty())
	{
	return "redirect:/shoppingCart";
	}
	else if (!cartInfo.isValidCustomer())
	{
		return "redirect:/shoppingCartCustomer";
	}
	return "shoppingCartConfirmation";
}
// to save the confirmed shopping cart
@RequestMapping(value={"/shoppingCartConfirmation"}, method= RequestMethod.POST)
public String shoppingCartConfirmationSave(HttpServletRequest request, Model model)
{ 
 CartInfo cartInfo = Utils.getCartInSession(request);
 
 if(cartInfo.isEmpty())
 {
	 return"redirect:/shoppingCart";
 }
 else if(!cartInfo.isValidCustomer())
 {
	 return"redirect:/shoppingCartCustomer";
 }
 try
 {
 orderDao.saveOrder(cartInfo);
 }catch(Exception e)
 {
return "shoppingCartConfirmation";
 }
Utils.removeCartInSession(request);	

Utils.storeLastOrderedCartInSession(request, cartInfo);

return "redirect:/shoppingCartFinalize";
}

@RequestMapping(value={"/shoppingCartFinalize"},method=RequestMethod.GET)
public String shoppingCartFinalize(HttpServletRequest request,Model model)
{
CartInfo lastOrderedCart = Utils.getLastOrderedCartInSession(request);
if(lastOrderedCart== null)
{
return "redirect:/shoppingCart";
	}

return "shoppingCartFinalize";
}

// to get image of product
@RequestMapping(value = { "/productImage" }, method = RequestMethod.GET)
public void productImage(HttpServletRequest request, HttpServletResponse response, Model model,
        @RequestParam("code") String code) throws IOException {
    Product product = null;
    if (code != null) {
        product = this.productDao.findProduct(code);
    }
    if (product != null && product.getImage() != null) {
        response.setContentType("image/jpeg, image/jpg, image/png, image/gif");
        response.getOutputStream().write(product.getImage());
    }
    response.getOutputStream().close();
}
}

