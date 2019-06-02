package com.bookorder.management.service;

import com.bookorder.management.entities.UserLoginLogModel;
import com.bookorder.management.entities.UserModel;
import com.bookorder.management.exception.BookOrderException;
import com.bookorder.management.model.AccessTokenResponse;
import com.bookorder.management.repositories.UserLoginLogRepository;
import com.bookorder.management.repositories.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserLoginLogRepository userLoginLogRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${endpoint.oauth.token}")
    private String endpointOauthToken;

    @Value("${security.oauth2.client.client-id}")
    private String clientId;

    @Value("${security.oauth2.client.client-secret}")
    private String clientSecret;

    @Autowired
    private ObjectMapper objectMapper;

    private HttpHeaders httpHeaders;

    public UserService() {
        this.httpHeaders = new HttpHeaders();
    }

    public UserModel saveUser(UserModel userModel) throws BookOrderException {
        if (userRepository.getUserByUsername(userModel.getUsername()) != null) {
            throw new BookOrderException("User already exist!");
        }
        String rawPassword = userModel.getPassword();
        String encodePassword = passwordEncoder.encode(rawPassword);
        userModel.setPassword(encodePassword);
        return userRepository.save(userModel);
    }

    public AccessTokenResponse getAccessTokenByRestApi(UserModel userModel) {
        ResponseEntity<AccessTokenResponse> accessTokenResponse = null;
        try {
            accessTokenResponse = restTemplate.exchange(endpointOauthToken, HttpMethod.POST, createHttpEntity(userModel), AccessTokenResponse.class);
            UserLoginLogModel log = new UserLoginLogModel();
            log.setLoginDate(new Date());
            log.setUsername(userModel.getUsername());
            log.setLoginStatus("SUCCESS");
            userLoginLogRepository.save(log);
        } catch (Exception e) {
            UserLoginLogModel log = new UserLoginLogModel();
            log.setUsername(userModel.getUsername());
            log.setLoginDate(new Date());
            log.setLoginStatus("FAIL");
            log.setLoginMessage(StringUtils.equals("400 null", e.getMessage()) ? "User not found!" : e.getMessage());
            userLoginLogRepository.save(log);
        }
        return accessTokenResponse != null ? accessTokenResponse.getBody() : null;

    }


    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {

        UserModel user = userRepository.getUserByUsername(s);
        if (user == null) {
            throw new UsernameNotFoundException(s);
        }
        return new User(user.getUsername(), user.getPassword(), true, true, true, true, getAuthorities(Collections.EMPTY_LIST));
    }


    public List<GrantedAuthority> getAuthorities(List<String> roles) {
        List<GrantedAuthority> authorities = new ArrayList<>();

        for (String role : roles) {
            authorities.add(new SimpleGrantedAuthority(role));
        }

        //BaseCredential
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        return authorities;
    }


    private HttpEntity<MultiValueMap<String, Object>> createHttpEntity(UserModel userModel) {

        MultiValueMap<String, Object> parts = new LinkedMultiValueMap<>();
        parts.add("username", userModel.getUsername());
        parts.add("password", userModel.getPassword());
        parts.add("grant_type", "password");
        httpHeaders.setBasicAuth(clientId, clientSecret);
        httpHeaders.setContentType(MediaType.MULTIPART_FORM_DATA);
        HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity(parts, httpHeaders);
        return entity;
    }

    public UserModel getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            String currentUserName = authentication.getName();
            return userRepository.getUserByUsername(currentUserName);
        }
        return null;
    }

    public void deleteLogForCurrentUser(UserModel userModel) {
        if (userModel != null && userModel.getId() != null) {
            userLoginLogRepository.deleteLogByUserId(userModel.getId());
        }
    }
}
