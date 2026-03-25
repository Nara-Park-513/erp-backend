package port.sm.erp.controller;

import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import port.sm.erp.dto.GoogleLoginRequest;
import port.sm.erp.dto.GoogleUserInfoResponse;
import port.sm.erp.dto.MemberRequestDTO;
import port.sm.erp.entity.Member;
import port.sm.erp.security.SecurityJwtConfig;
import port.sm.erp.service.GoogleAuthService;
import port.sm.erp.service.MemberService;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class AuthController {

    private final MemberService memberService;
    private final SecurityJwtConfig securityJwtConfig;
    private final GoogleAuthService googleAuthService;

    public AuthController(
            MemberService memberService,
            SecurityJwtConfig securityJwtConfig,
            GoogleAuthService googleAuthService
    ) {
        this.memberService = memberService;
        this.securityJwtConfig = securityJwtConfig;
        this.googleAuthService = googleAuthService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        String password = body.get("password");

        Member member = memberService.login(email, password);

        if (member == null) {
            return ResponseEntity.status(401).body(Map.of("error", "Invalid email or password"));
        }

        String token = securityJwtConfig.generateToken(member.getId(), member.getEmail());

        return ResponseEntity.ok(Map.of(
                "token", token,
                "id", member.getId(),
                "email", member.getEmail()
        ));
    }

    @PostMapping("/google")
    public ResponseEntity<?> googleLogin(@RequestBody GoogleLoginRequest request) {
        GoogleUserInfoResponse userInfo = googleAuthService.getUserInfo(request.getCode());

        Member member = memberService.findOrCreateGoogleMember(
                userInfo.getSub(),
                userInfo.getEmail(),
                userInfo.getName()
        );

        String token = securityJwtConfig.generateToken(member.getId(), member.getEmail());

        return ResponseEntity.ok(Map.of(
                "token", token,
                "id", member.getId(),
                "email", member.getEmail(),
                "name", userInfo.getName(),
                "provider", "google"
        ));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody MemberRequestDTO dto) {
        try {
            Member member = memberService.register(dto);
            return ResponseEntity.ok(member);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}