package EmployeeManagementSystem.controller;

import EmployeeManagementSystem.entity.Wish;
import EmployeeManagementSystem.repository.WishRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/wish")
public class WishController {

    private final WishRepository wishRepository;

    @PostMapping("/send")
    public ResponseEntity<?> sendWish(@RequestBody Map<String, String> requestData) {
        String senderId = requestData.get("senderId");     // Logged-in Emp ID
        String receiverId = requestData.get("receiverId"); // Receiver Emp ID
        String wishType = requestData.get("type");

        // Duplicate wish check (Agar aaj pehle hi wish kar chuka hai)
        boolean alreadyWished = wishRepository.existsBySenderUserIdAndReceiverUserIdAndWishType(senderId, receiverId, wishType);

        if (alreadyWished) {
            return ResponseEntity.badRequest().body("Already Wished Today!");
        }

        Wish wish = new Wish();
        wish.setSenderUserId(senderId);
        wish.setReceiverUserId(receiverId);
        wish.setWishType(wishType);
        wish.setMessage(wishType.equals("Birthday") ? "Happy Birthday! 🎉" : "Happy Work Anniversary! 🏆");

        wishRepository.save(wish);

        return ResponseEntity.ok("Wish Sent Successfully!");
    }
}
