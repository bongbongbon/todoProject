package com.example.cicdtest.service;

import com.example.cicdtest.common.exception.CustomException;
import com.example.cicdtest.common.exception.ErrorCode;
import com.example.cicdtest.domain.users.User;
import com.example.cicdtest.domain.users.common.UserLevel;
import com.example.cicdtest.domain.users.common.UserProfileImg;
import com.example.cicdtest.domain.users.common.UserStatus;
import com.example.cicdtest.dto.LoginRequest;
import com.example.cicdtest.dto.SignupRequest;
import com.example.cicdtest.dto.UserResponse;
import com.example.cicdtest.dto.UserTokenDto;
import com.example.cicdtest.common.jwt.JwtProperties;
import com.example.cicdtest.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final CustomUserDetailsService userDetailsService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtProperties jwtProperties;



    public UserResponse signUp(SignupRequest request) {

        // 이메일 중복확인
        isExistUserEmail(request.getEmail());

        checkPassword(request.getPassword(), request.getPasswordCheck());

        User user =  User.builder()
                .email(request.getEmail())
                .password(bCryptPasswordEncoder.encode(request.getPassword()))
                .userLevel(UserLevel.USER)
                .userStatus(UserStatus.NORMAL)
                .profileImg(UserProfileImg.IMG_FINN)
                .nickname(request.getNickname())
                .build();

        return UserResponse.fromEntity(userRepository.save(user));
    }

    public UserTokenDto login(LoginRequest request) {

        authenticate(request.getEmail(), request.getPassword());
        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());
        checkEncodePassword(request.getPassword(), userDetails.getPassword());

        // 로그인된 회원 닉네임
        User loginUser = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> CustomException.USER_NOT_FOUND);

        String token = jwtProperties.generateToken(userDetails);
        return UserTokenDto.fromEntity(loginUser, token);
    }

    public UserResponse getUser(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        return UserResponse.fromEntity(user);
    }

    /**
     * 아이디(이메일) 중복 체크
     *
     * @param email
     */

    private void isExistUserEmail(String email) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw CustomException.ALREADY_USER_EXISTS;
        }
    }

    private void authenticate(String email, String pwd) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, pwd));
        } catch (DisabledException e) {
            throw CustomException.USER_NOT_FOUND;
        } catch (BadCredentialsException e) {
            throw CustomException.PASSWORD_NOT_MATCH;
        }
    }

    // db에 있는 비밀번호와 같은지
    private void checkEncodePassword(String rawPassword, String encodedPassword) {
        if (!bCryptPasswordEncoder.matches(rawPassword, encodedPassword)) {
            throw CustomException.PASSWORD_NOT_MATCH;
        }

    }


    private void checkPassword(String password, String passwordCheck) {
        if (!password.equals(passwordCheck)) {
            throw CustomException.PASSWORD_NOT_MATCH;
        }
    }
}
