package com.example.OTP.controller;

import com.example.OTP.model.OtpRequest;
import com.example.OTP.model.OtpResponse;
import com.example.OTP.service.OtpService;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@CrossOrigin(origins = "http://192.168.4.170:3000")
@RestController
@RequestMapping("/api/otp")
public class OtpController {

    private final OtpService otpService;
    private final Map<String, String> otpStore = new HashMap<>();

    public OtpController(OtpService otpService) {
        this.otpService = otpService;
    }

    @PostMapping("/send")
    public OtpResponse sendOtp(@RequestBody OtpRequest otpRequest) {
        String email = otpRequest.getEmail();
        String otp = otpService.generateOtp();
        otpService.sendOtpEmail(email, otp);
        otpStore.put(email, otp);
        return new OtpResponse("OTP sent");
    }

    @PostMapping("/verify")
    public OtpResponse verifyOtp(@RequestBody OtpRequest otpRequest) {
        String email = otpRequest.getEmail();
        String otp = otpRequest.getOtp();
        String storedOtp = otpStore.get(email);
        if (storedOtp != null && storedOtp.equals(otp)) {
            otpStore.remove(email);
            return new OtpResponse("OTP verified");
        } else {
            return new OtpResponse("Invalid OTP");
        }
    }
}
