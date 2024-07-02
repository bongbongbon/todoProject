package com.example.cicdtest.service;

import com.example.cicdtest.common.exception.CustomException;
import com.example.cicdtest.controller.running.response.RunningResponse;
import com.example.cicdtest.controller.runninguser.response.RunningUserResponse;
import com.example.cicdtest.domain.running.Running;
import com.example.cicdtest.domain.runninguser.RunningUser;
import com.example.cicdtest.domain.users.User;
import com.example.cicdtest.repository.RunningRepository;
import com.example.cicdtest.repository.RunningUserRepository;
import com.example.cicdtest.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RunningUserService {

    private final RunningUserRepository runningUserRepository;
    private final RunningRepository runningRepository;
    private final UserRepository userRepository;

    public RunningUserResponse join(User user, Long runningId) {

        User hostUser = userRepository.findByEmail(user.getEmail())
                .orElseThrow(()-> CustomException.USER_NOT_FOUND);

        Running running = runningRepository.findById(runningId)
                .orElseThrow(() -> CustomException.RUNNING_NOT_FOUND);

        // 내가 이팀에 런닝리더일때
        if (running.getUser().getEmail().equals(hostUser.getEmail())) {
            throw CustomException.YOU_ARE_RUNNING_LEADER;
        }


        // 이미 팀에 참여 했을때
        if (running.getRunningUserList().stream()
                .map(RunningUser::getUser)  // RunningUser 객체에서 User 객체를 추출
                .map(User::getEmail)  // User 객체에서 이메일을 추출
                .anyMatch(email -> email.equals(hostUser.getEmail()))) {
            throw CustomException.ALREADY_JOIN_THIS_TEAM;
        }

        // 팀의 인원이 꽉찼을때
        if (running.getLimitedPeople() == running.getRunningUserList().size()) {
            throw CustomException.TEAM_USER_IS_FULL;
        }

        RunningUser runningUser = RunningUser.builder()
                .running(running)
                .user(hostUser)
                .build();


        return RunningUserResponse.fromEntity(runningUserRepository.save(runningUser));
    }

    public List<RunningUserResponse> getUsers(Long runningId) {

        List<RunningUser> runningUserList = runningUserRepository.findAllByRunningId(runningId);

        return RunningUserResponse.fromEntity(runningUserList);
    }

}
