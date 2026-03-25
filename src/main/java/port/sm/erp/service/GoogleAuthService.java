package port.sm.erp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import port.sm.erp.dto.GoogleTokenResponse;
import port.sm.erp.dto.GoogleUserInfoResponse;

@Service
@RequiredArgsConstructor
public class GoogleAuthService {

    @Value("${google.client-id}")
    private String clientId;

    @Value("${google.client-secret}")
    private String clientSecret;

    @Value("${google.redirect-uri}")
    private String redirectUri;

    private final RestTemplate restTemplate = new RestTemplate();

    public GoogleUserInfoResponse getUserInfo(String code) {
        String tokenUrl = "https://oauth2.googleapis.com/token";

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("code", code);
        params.add("client_id", clientId);
        params.add("client_secret", clientSecret);
        params.add("redirect_uri", redirectUri);
        params.add("grant_type", "authorization_code");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> tokenRequest = new HttpEntity<>(params, headers);

        ResponseEntity<GoogleTokenResponse> tokenResponse = restTemplate.postForEntity(
                tokenUrl,
                tokenRequest,
                GoogleTokenResponse.class
        );

        GoogleTokenResponse tokenBody = tokenResponse.getBody();
        if (tokenBody == null || tokenBody.getAccessToken() == null) {
            throw new RuntimeException("구글 access token 요청 실패");
        }

        String userInfoUrl = "https://www.googleapis.com/oauth2/v3/userinfo";

        HttpHeaders userHeaders = new HttpHeaders();
        userHeaders.setBearerAuth(tokenBody.getAccessToken());

        HttpEntity<Void> userRequest = new HttpEntity<>(userHeaders);

        ResponseEntity<GoogleUserInfoResponse> userResponse = restTemplate.exchange(
                userInfoUrl,
                HttpMethod.GET,
                userRequest,
                GoogleUserInfoResponse.class
        );

        GoogleUserInfoResponse userInfo = userResponse.getBody();
        if (userInfo == null) {
            throw new RuntimeException("구글 사용자 정보 조회 실패");
        }

        return userInfo;
    }
}