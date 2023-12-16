package com.data.filtro.api;

import com.data.filtro.model.ErrorResponse;
import com.data.filtro.model.Feedback;
import com.data.filtro.model.Product;
import com.data.filtro.model.User;
import com.data.filtro.repository.ProductRepository;
import com.data.filtro.service.FeedbackService;
import com.data.filtro.service.ProductService;
import com.data.filtro.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.support.SimpleTriggerContext;
import org.springframework.web.bind.annotation.*;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/comment")
public class FeedbackAPIController {
    @Autowired
    UserService userService;
    @Autowired
    ProductService productservice;
    @Autowired
    FeedbackService feedbackService;
    @GetMapping("/getAll/{productId}")
    public ResponseEntity<?> getAllFeedbackByProduct(@PathVariable int productId) {
        try {
            List<Feedback> feedbacks = feedbackService.getAllFeedbackByProduct(productId);
            if (feedbacks == null) {
                String message = "No feedback found!";
                ErrorResponse err = new ErrorResponse(message, HttpStatus.NOT_FOUND.value());
                return new ResponseEntity<>(err, HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(feedbacks, HttpStatus.OK);
        } catch (NoSuchElementException ex) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @PostMapping("/sentfeedback")
    public ResponseEntity<?> addFeedbackToProduct(@RequestParam int userId,
                                                  @RequestParam String content,
                                                  @RequestParam int productId) {
        Product product = productservice.getProductById(productId);
        User user = userService.getByUserId(userId);
        Calendar calendar = Calendar.getInstance();
        Date currentDate = calendar.getTime();
        Feedback feedback1 = new Feedback();



        if (product != null) {
            feedback1.setProduct(product);
            feedback1.setUser(user);
            feedback1.setContent(content);
            feedback1.setDate(currentDate);
            feedbackService.addFeedback(feedback1);
            return new ResponseEntity<>("Người dùng "+user.getName()+" đã bình luận "+content+" cho sản phẩm "
                    +product.getProductName(), HttpStatus.OK);
        } else {
            String message = "Product not found!";
            ErrorResponse err = new ErrorResponse(message, HttpStatus.INTERNAL_SERVER_ERROR.value());
            return new ResponseEntity<>(err, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}