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

    public ResponseEntity<String> registerUser(UserDto userCreateDto) {
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

    public AuthDto loginUser(LoginDto loginDto) {
        Optional<User> optionalUser = userRepository.findByUsername(loginDto.getUsername());

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (BCrypt.checkpw(loginDto.getPassword(), user.getPassword())) {
                Optional<UserProjection> optionalUserProjection = userRepository.findByUsernameAndPassword(loginDto.getUsername(), user.getPassword());
                if (optionalUserProjection.isPresent()) {
                    UserProjection userProjection = optionalUserProjection.get();

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

    public UserProjection findUserProfile(Long userId) {
        return userRepository.findByUserId(userId).orElse(null);
    }
    public List<Course> findAllUserCourseByUserId(Long userId) {
        return courseRepository.findAllByUserId(userId);
    }
    public List<Challenge> findAllUserChallengeByUserId(Long userId) {
        return challengeRepository.findAllByUserId(userId);
    }
    public List<Item> findAllUserItemByUserId(Long userId) {
        return itemRepository.findAllByUserId(userId);
    }
    public List<UserProjection> findAllUserForLeaderboard() {
        return userRepository.findAllByOrderByScoreDescNamaDepanAsc();
    }

    public ResponseEntity<String> updateUser(UserDto userUpdateDto, Long id) {
        Optional<User> existingUserOptional = userRepository.findById(id);
        if (existingUserOptional.isEmpty()) {
            return ResponseEntity.badRequest().body("Pengguna tidak ada");
        }

        User existingUser = existingUserOptional.get();
        existingUser.setNamaDepan(existingUser.getScore() + userUpdateDto.getNamaDepan());
        existingUser.setNamaBelakang(existingUser.getPoint() + userUpdateDto.getNamaBelakang());
        userRepository.save(existingUser);

        return ResponseEntity.ok("Berhasil memperbarui pengguna");
    }

    public ResponseEntity<String> updateUserScore(UserDto userScoreDto, Long id) {
        Optional<User> existingUserOptional = userRepository.findById(id);
        if (existingUserOptional.isEmpty()) {
            return ResponseEntity.badRequest().body("Pengguna tidak ada");
        }

        User existingUser = existingUserOptional.get();
        existingUser.setScore(existingUser.getScore() + userScoreDto.getScore());
        existingUser.setPoint(existingUser.getPoint() + userScoreDto.getPoint());
        userRepository.save(existingUser);

        return ResponseEntity.ok("Skor dan poin berhasil diperbarui");
    }

    public ResponseEntity<String> updateUserProfile(UserDto userEditProfileDto, Long id) {
        Optional<User> existingUserOptional = userRepository.findById(id);
        if (existingUserOptional.isPresent()) {
            User existingUser = existingUserOptional.get();
            existingUser.setProfileUsed(userEditProfileDto.getNewProfile());
            existingUser.setBorderUsed(userEditProfileDto.getNewBorder());
            userRepository.save(existingUser);
            return ResponseEntity.ok("Profil pengguna berhasil diperbarui");
        } else {
            return ResponseEntity.badRequest().body("Pengguna tidak ada");
        }
    }

    public ResponseEntity<String> updateOrAddUserItem(ItemDto itemDto, Long id) {
        Optional<User> existingUserOptional = userRepository.findById(id);

        if (existingUserOptional.isPresent()) {
            User user = existingUserOptional.get();

            Optional<Item> existingItemOptional = itemRepository.findByNamaItemAndUserId(itemDto.getNamaItem(), id);
            if (existingItemOptional.isPresent()) {
                return ResponseEntity.ok("Anda sudah mempunyai " + itemDto.getTipeItem() + itemDto.getNamaItem());
            } else {
                Item item = Item.builder()
                        .user(user)
                        .namaItem(itemDto.getNamaItem())
                        .tipeItem(itemDto.getTipeItem())
                        .valueItem(itemDto.getValueItem())
                        .build();
                itemRepository.save(item);
                return ResponseEntity.ok("Anda telah membeli " + itemDto.getTipeItem() + itemDto.getNamaItem());
            }
        } else {
            return ResponseEntity.badRequest().body("Pengguna tidak ada");
        }
    }

    public ResponseEntity<String> updateOrAddUserChallenge(ChallengeDto challengeDto, Long id) {
        Optional<User> existingUserOptional = userRepository.findById(id);

        if (existingUserOptional.isPresent()) {
            User user = existingUserOptional.get();

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
                message = "Hebat! Kuis " + challengeDto.getNamaChallenge() + " diselesaikan dengan sempurna";
            } else {
                message = "Kuis " + challengeDto.getNamaChallenge() + " diselesaikan";
            }

            return ResponseEntity.ok(message);
        } else {
            return ResponseEntity.badRequest().body("Pengguna tidak ada");
        }
    }

    public ResponseEntity<String> updateOrAddUserCourse(CourseDto courseDto, Long id) {
        Optional<User> existingUserOptional = userRepository.findById(id);

        if (existingUserOptional.isPresent()) {
            User user = existingUserOptional.get();

            Optional<Course> existingCourseOptional = courseRepository.findByNamaCourseAndUserId(courseDto.getNamaCourse(), id);
            if (existingCourseOptional.isPresent()) {
                return ResponseEntity.ok("Selamat Pelajaran " + courseDto.getNamaCourse() + " telah dipelajari ulang");
            } else {
                Course course = Course.builder()
                        .user(user)
                        .namaCourse(courseDto.getNamaCourse())
                        .completed(courseDto.getCompleted())
                        .gambarUrl(courseDto.getGambarUrl())
                        .build();
                courseRepository.save(course);
                return ResponseEntity.ok("Selamat Pelajaran " + courseDto.getNamaCourse() + " telah diselesaikan");
            }
        } else {
            return ResponseEntity.badRequest().body("Pengguna tidak ada");
        }
    }
}
