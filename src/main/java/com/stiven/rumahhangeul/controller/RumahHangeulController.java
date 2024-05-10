package com.stiven.rumahhangeul.controller;

import com.stiven.rumahhangeul.dto.*;
import com.stiven.rumahhangeul.entity.*;
import com.stiven.rumahhangeul.service.RumahHangeulService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
public class RumahHangeulController {
    @Autowired
    private RumahHangeulService rumahHangeulService;

    @PostMapping(value = "/register")
    @ResponseStatus(value = HttpStatus.CREATED)
    public ResponseEntity<String> registerUser(@Validated @RequestBody UserDto userCreateDto) {
        return rumahHangeulService.registerUser(userCreateDto);
    }
    @PostMapping(value = "/login")
    @ResponseStatus(value = HttpStatus.OK)
    public AuthDto loginUser (@Validated @RequestBody LoginDto loginDto){
        return rumahHangeulService.loginUser(loginDto);
    }
    @GetMapping(value = "/user/profile/{userId}")
    @ResponseStatus(value = HttpStatus.OK)
    public UserProjection findUserProfile (@PathVariable Long userId){
        return rumahHangeulService.findUserProfile(userId);
    }

    @PostMapping(value = "/user/profile/{id}/item")
    @ResponseStatus(value = HttpStatus.OK)
    public ResponseEntity<String> updateOrAddUserItem(@Validated @RequestBody ItemDto itemDto, @PathVariable Long id) {
        return rumahHangeulService.updateOrAddUserItem(itemDto, id);
    }
    @PostMapping(value = "/user/profile/{id}/course")
    @ResponseStatus(value = HttpStatus.OK)
    public ResponseEntity<String> updateOrAddUserCourse(@Validated @RequestBody CourseDto courseDto, @PathVariable Long id) {
        return rumahHangeulService.updateOrAddUserCourse(courseDto, id);
    }
    @PostMapping(value = "/user/profile/{id}/challenge")
    @ResponseStatus(value = HttpStatus.OK)
    public ResponseEntity<String> updateOrAddUserChallenge(@Validated @RequestBody ChallengeDto challengeDto, @PathVariable Long id) {
        return rumahHangeulService.updateOrAddUserChallenge(challengeDto, id);
    }


    @PatchMapping(value = "/user/profile/{id}/update")
    public ResponseEntity<String> updateUser(@Validated @RequestBody UserDto userUpdateDto, @PathVariable Long id) {
        return rumahHangeulService.updateUser(userUpdateDto, id);
    }
    @PatchMapping(value = "/user/profile/{id}/update-score")
    public ResponseEntity<String> updateUserScore(@Validated @RequestBody UserDto userScoreDto, @PathVariable Long id) {
        return rumahHangeulService.updateUserScore(userScoreDto, id);
    }
    @PatchMapping(value = "/user/profile/{id}/profil")
    public ResponseEntity<String> updateUserProfile(@PathVariable Long id, @RequestBody UserDto userEditProfileDto) {
        return rumahHangeulService.updateUserProfile(userEditProfileDto, id);
    }


    @GetMapping(value = "/users/leaderboard")
    @ResponseStatus(value = HttpStatus.OK)
    public List<UserProjection> findAllUserForLeaderboard() {
        return rumahHangeulService.findAllUserForLeaderboard();
    }
    @GetMapping(value = "/users/profile/{id}/courses")
    @ResponseStatus(value = HttpStatus.OK)
    public List<Course> findAllUserCourseByUserId(@PathVariable Long id) {
        return rumahHangeulService.findAllUserCourseByUserId(id);
    }
    @GetMapping(value = "/users/profile/{id}/challenges")
    @ResponseStatus(value = HttpStatus.OK)
    public List<Challenge> findAllUserChallengeByUserId(@PathVariable Long id) {
        return rumahHangeulService.findAllUserChallengeByUserId(id);
    }
    @GetMapping(value = "/users/profile/{id}/items")
    @ResponseStatus(value = HttpStatus.OK)
    public List<Item> findAllUserItemByUserId(@PathVariable Long id) {
        return rumahHangeulService.findAllUserItemByUserId(id);
    }
}
