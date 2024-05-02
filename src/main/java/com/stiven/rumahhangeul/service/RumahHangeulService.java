package com.stiven.rumahhangeul.service;

import com.stiven.rumahhangeul.dto.*;
import com.stiven.rumahhangeul.entity.*;
import com.stiven.rumahhangeul.repository.*;
import lombok.RequiredArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RumahHangeulService {

    private final UserRepository userRepository;
    private final ChallengeRepository challengeRepository;
    private final CourseRepository courseRepository;
    private final ItemRepository itemRepository;

    public ResponseEntity<String> registerUser(UserCreateDto userCreateDto) {
        StringBuilder errorMessage = new StringBuilder();

        Optional<User> existingUserOptional = userRepository.findByUsername(userCreateDto.getUsername());
        if (existingUserOptional.isPresent()) {
            errorMessage.append("Username sudah terdaftar\n");
        }
        Optional<User> existingEmailOptional = userRepository.findByEmail(userCreateDto.getEmail());
        if (existingEmailOptional.isPresent()) {
            errorMessage.append("Email sudah terdaftar\n");
        }

        if (errorMessage.length() == 0) {
            String hashedPassword = BCrypt.hashpw(userCreateDto.getPassword(), BCrypt.gensalt());

            User user = User.builder()
                    .username(userCreateDto.getUsername())
                    .namaDepan(userCreateDto.getNamaDepan())
                    .namaBelakang(userCreateDto.getNamaBelakang())
                    .password(hashedPassword)
                    .email(userCreateDto.getEmail())
                    .score(0L)
                    .point(0L)
                    .borderUsed("white")
                    .profileUsed("/profile/basic.png")
                    .build();

            userRepository.save(user);

            return ResponseEntity.ok("Pengguna berhasil didaftarkan");
        }

        return ResponseEntity.badRequest().body(errorMessage.toString().trim());
    }
    public AuthDto authenticateUser(LoginDto loginDto) {
        Optional<User> optionalUser = userRepository.findByUsername(loginDto.getUsername());

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (BCrypt.checkpw(loginDto.getPassword(), user.getPassword())) {
                Optional<UserProjection> optionalUserProjection = userRepository.findByUsernameAndPassword(loginDto.getUsername(), user.getPassword());
                if (optionalUserProjection.isPresent()) {
                    UserProjection userProjection = optionalUserProjection.get();
                    userRepository.save(user);

                    return AuthDto.builder()
                            .message("Berhasil Login")
                            .userProjection(userProjection)
                            .build();
                }
            }
        }
        return AuthDto.builder()
                .message("Username atau password yang dimasukkan salah")
                .userProjection(null)
                .build();
    }
    public UserProjection findByUserId(Long userId) {
        Optional<UserProjection> optionalUserProjection = userRepository.findByUserId(userId);
        UserProjection userProjection = null;
        if(optionalUserProjection.isPresent()){
            userProjection = optionalUserProjection.get();
        }
        return userProjection;
    }


    public List<Course> findUserCourseByUserId(Long userId) {
        return courseRepository.findAllByUserId(userId);
    }
    public List<Challenge> findUserChallengeByUserId(Long userId) {
        return challengeRepository.findAllByUserId(userId);
    }
    public List<Item> findUserItemByUserId(Long userId) {
        return itemRepository.findAllByUserId(userId);
    }
    public List<UserProjection> findAllUserForLeaderboard() {
        return userRepository.findAllByOrderByScoreDescNamaDepanAsc();
    }

    public AuthDto updateUser(UserUpdateDto userUpdateDto, Long id) {
        AuthDto authDto;
        User existingUser;
        Optional<UserProjection> optionalUserProjection = userRepository.findByUserId(id);
        UserProjection userProjection = null;

        Optional<User> existingUserOptional = userRepository.findById(id);
        if (existingUserOptional.isEmpty()) {
            authDto = AuthDto.builder()
                    .message("Pengguna tidak ditemukan\n")
                    .userProjection(userProjection)
                    .build();
            return authDto;
        } else{
            existingUser = existingUserOptional.get();
        }

        if(optionalUserProjection.isPresent()){
            userProjection = optionalUserProjection.get();
        }

//        if (!existingUser.getEmail().equals(userUpdateDto.getEmail())) {
//            Optional<User> existingEmailOptional = userRepository.findByEmail(userUpdateDto.getEmail());
//            if (existingEmailOptional.isPresent()) {
//                authDto = AuthDto.builder()
//                        .message("Email is already registered\n")
//                        .userProjection(userProjection)
//                        .build();
//                return authDto;
//            }
//        }

        existingUser.setNamaDepan(userUpdateDto.getNamaDepan());
        existingUser.setNamaBelakang(userUpdateDto.getNamaBelakang());
//        existingUser.setEmail(userUpdateDto.getEmail());

        userRepository.save(existingUser);

        authDto = AuthDto.builder()
                .message("Berhasil memperbarui profil")
                .userProjection(userProjection)
                .build();

        return authDto;
    }
    public ResponseEntity<String> updateUserScore(UserScoreDto userScoreDto, Long id) {
        StringBuilder errorMessage = new StringBuilder();

        // Check if the user exists
        Optional<User> existingUserOptional = userRepository.findById(id);
        if (existingUserOptional.isEmpty()) {
            errorMessage.append("Pengguna tidak ditemukan\n");
            return ResponseEntity.badRequest().body(errorMessage.toString().trim());
        }

        User existingUser = existingUserOptional.get();

        existingUser.setScore(existingUser.getScore() +userScoreDto.getScore());
        existingUser.setPoint(existingUser.getPoint() +userScoreDto.getPoint());

        userRepository.save(existingUser);

        return ResponseEntity.ok("User Score updated successfully");
    }
    public ResponseEntity<String> updateUserProfil(UserEditProfileDto userEditProfileDto, Long id) {
        StringBuilder errorMessage = new StringBuilder();

        Optional<User> existingUserOptional = userRepository.findById(id);
        if (existingUserOptional.isEmpty()) {
            errorMessage.append("Pengguna tidak ditemukan\n");
            return ResponseEntity.badRequest().body(errorMessage.toString().trim());
        }

        User existingUser = existingUserOptional.get();

        existingUser.setProfileUsed(userEditProfileDto.getNewProfile());
        existingUser.setBorderUsed(userEditProfileDto.getNewBorder());

        userRepository.save(existingUser);

        return ResponseEntity.ok("Profil pengguna berhasil diperbarui");
    }


    public ResponseEntity<String> updateOrAddUserItems(ItemDto itemDto, Long id) {
        Optional<User> existingUserOptional = userRepository.findById(id);
        User user;
        if (existingUserOptional.isPresent()) {
            user = existingUserOptional.get();
        } else{
            return ResponseEntity.ok("Pengguna tidak ada");
        }

        String message;
        Optional<Item> existingItemOptional = itemRepository.findByNamaItemAndUserId(itemDto.getNamaItem(), id);
        if (existingItemOptional.isPresent()) {
            message= "Anda sudah mempunyai " + itemDto.getTipeItem() + itemDto.getNamaItem();

        } else {
            message= "Anda telah membeli " + itemDto.getTipeItem() + itemDto.getNamaItem();
            Item item = Item.builder()
                    .user(user)
                    .namaItem(itemDto.getNamaItem())
                    .tipeItem(itemDto.getTipeItem())
                    .valueItem(itemDto.getValueItem())
                    .build();
            itemRepository.save(item);
        }

        return ResponseEntity.ok(message);
    }
    public ResponseEntity<String> updateOrAddUserChallenges(ChallengeDto challengeDto, Long id) {
        Optional<User> existingUserOptional = userRepository.findById(id);
        User user;
        if (existingUserOptional.isPresent()) {
            user = existingUserOptional.get();
        } else{
            return ResponseEntity.ok("Pengguna tidak ada");
        }

        Optional<Challenge> existingChallengeOptional = challengeRepository.findByNamaChallengeAndUserId(challengeDto.getNamaChallenge(), id);
        if (existingChallengeOptional.isPresent()) {
            Challenge existingChallenge = existingChallengeOptional.get();
            existingChallenge.setPerfectClear(challengeDto.getPerfectClear());
            challengeRepository.save(existingChallenge);
        } else {
            Challenge challenge = Challenge.builder()
                    .user(user)
                    .namaChallenge(challengeDto.getNamaChallenge())
                    .firstClear(challengeDto.getFirstClear())
                    .perfectClear(challengeDto.getPerfectClear())
                    .gambarUrl(challengeDto.getGambarUrl())
                    .build();
            challengeRepository.save(challenge);
        }

        String message;
        if (challengeDto.getPerfectClear().equals("yes")) {
            message= "Hebat! Kuis " + challengeDto.getNamaChallenge() + " diselesaikan dengan sempurna";
        } else {
            message= "Kuis " + challengeDto.getNamaChallenge() + " diselesaikan";
        }

        return ResponseEntity.ok(message);
    }
    public ResponseEntity<String> updateOrAddUserCourses(CourseDto courseDto, Long id) {
        Optional<User> existingUserOptional = userRepository.findById(id);
        User user;
        if (existingUserOptional.isPresent()) {
            user = existingUserOptional.get();
        } else{
            return ResponseEntity.ok("Pengguna tidak ada");
        }

        String message;
        Optional<Course> existingCourseOptional = courseRepository.findByNamaCourseAndUserId(courseDto.getNamaCourse(), id);
        if (existingCourseOptional.isPresent()) {
            message= "Selamat Pelajaran " + courseDto.getNamaCourse() + " telah dipelajari ulang";
        } else {
            message= "Selamat Pelajaran " + courseDto.getNamaCourse() + " telah diselesaikan";
            Course course = Course.builder()
                    .user(user)
                    .namaCourse(courseDto.getNamaCourse())
                    .completed(courseDto.getCompleted())
                    .gambarUrl(courseDto.getGambarUrl())
                    .build();
            courseRepository.save(course);
        }

        return ResponseEntity.ok(message);
    }
}
