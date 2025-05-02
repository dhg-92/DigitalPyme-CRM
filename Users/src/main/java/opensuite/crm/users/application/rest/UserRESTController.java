package opensuite.crm.users.application.rest;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import opensuite.crm.users.application.rest.data.RestorePasswordData;
import opensuite.crm.users.application.rest.request.CreateAuthUserRequest;
import opensuite.crm.users.application.rest.request.CreateRestorePasswordUserRequest;
import opensuite.crm.users.application.rest.request.CreateUserRequest;
import opensuite.crm.users.application.rest.request.RestorePasswordRequest;
import opensuite.crm.users.application.rest.response.UserResponse;
import opensuite.crm.users.domain.User;
import opensuite.crm.users.infrastructure.repository.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import opensuite.crm.users.domain.services.UserService;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.kafka.core.KafkaTemplate;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Log4j2
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RestController
@RequestMapping("/users")
public class UserRESTController {

    private final UserService userService;
    private final KafkaTemplate<String, RestorePasswordData> userKafkaTemplate;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody CreateAuthUserRequest userRequest) {
        log.trace("authUser");

        log.trace("Auth user: " + userRequest.toString());

        Optional<User> user = userService.findUserByEmail(userRequest.getEmail());

        if (user.isPresent() && userRequest.getPassword().equals(user.get().getPassword())) {
            return ResponseEntity.ok(JwtUtil.generateToken(user.orElse(null)));
        }

        return ResponseEntity.badRequest().body("Invalid username or password.");
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<UserResponse> getAllUsers() {
        log.trace("getAllUsers");

        return userService.findAllUsers().stream()
                .map(UserResponse::userToGetUserResponse)
                .collect(Collectors.toList());
    }

    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody @Valid CreateUserRequest createUserRequest) {
        log.trace("createUser");

        log.trace("Creating user " + createUserRequest);

        Optional<User> existingUser = userService.findUserByEmail(createUserRequest.getEmail());

        if (existingUser.isPresent()) {
            log.trace("User exist " + createUserRequest);
            return ResponseEntity.status(409).body("User Already Exists.");
        }

        User newUser = new User();
        newUser.setIsAdmin(createUserRequest.getIsAdmin());
        newUser.setEmail(createUserRequest.getEmail());
        newUser.setName(createUserRequest.getName());
        newUser.setPhone(createUserRequest.getPhone());
        newUser.setSurname(createUserRequest.getSurname());

        Long userId = userService.createUser(newUser);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(userId)
                .toUri();
        return ResponseEntity.created(uri).body(userId);
    }

    @DeleteMapping("/{idUser}")
    public ResponseEntity<?> deleteUser(@PathVariable Long idUser){
        log.trace("deleteUser");

        if (idUser == 1) {
            log.trace("The admin user cannot be deleted");
            return ResponseEntity.status(409).body("User cannot be deleted.");
        }

        Optional<User> existingUser = userService.findUserById(idUser);

        if (existingUser.isEmpty()) {
            log.trace("User to deleted not exist");
            return ResponseEntity.status(409).body("User not Exists.");
        }

        if (userService.removeUser(idUser)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(409).body("Could not be removed.");
        }
    }

    @PutMapping("/{idUser}")
    public ResponseEntity<?> updateUser(@RequestBody @Valid CreateUserRequest updateUserRequest, @PathVariable Long idUser) {
        log.trace("updateUser");

        Optional<User> existingUser = userService.findUserById(idUser);

        if (existingUser.isEmpty()) {
            log.trace("User not exist " + updateUserRequest);
            return ResponseEntity.status(409).body("User not Exists.");
        }

        log.trace("Update user " + updateUserRequest);

        User updateUser = existingUser.get();
        updateUser.setName(updateUserRequest.getName());
        updateUser.setSurname(updateUserRequest.getSurname());
        updateUser.setEmail(updateUserRequest.getEmail());
        updateUser.setPhone(updateUserRequest.getPhone());
        updateUser.setIsAdmin(updateUserRequest.getIsAdmin());

        userService.createUser(updateUser);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(idUser)
                .toUri();
        return ResponseEntity.created(uri).body(idUser);
    }

    @GetMapping("/{idUser}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<UserResponse> findUserById(@PathVariable @NotNull Long idUser) {
        log.info("findUser");

        return userService.findUserById(idUser)
                .map(client -> ResponseEntity.ok().body(UserResponse.userToGetUserResponse(client)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/info")
    @ResponseStatus(HttpStatus.OK)
    public Optional<?>  getInfoUser(HttpServletRequest request) {
        log.trace("getInfoUser");

        try {
            User user = JwtUtil.extractUsername(request.getHeader("Authorization").substring(7));
            if (user != null) {
                return userService.findUserById(user.getIdUser())
                        .map(UserResponse::userToGetUserResponse);
            } else {
                log.trace("User not exist " + user);
                return Optional.of(ResponseEntity.status(409).body("User not Exists."));
            }
        } catch (Exception e) {
            return Optional.of(ResponseEntity.status(401).body("Invalid credentials."));
        }
    }


    @PostMapping("/restorePassword")
    public ResponseEntity<?> restorePassword(@Valid @RequestBody RestorePasswordRequest restorePasswordRequest) {
        log.trace("restorePassword");

        log.trace("Restore password user: " + restorePasswordRequest.toString());

        Optional<User> user = userService.findUserByEmail(restorePasswordRequest.getEmail());

        if (user.isPresent()) {
            String token = UUID.randomUUID().toString();
            user.get().setRestoreToken(token);
            user.get().setValidRestoreToken(LocalDateTime.now().plusMinutes(60));
            userService.createUser(user.get());

            RestorePasswordData restorePasswordData = new RestorePasswordData(UUID.randomUUID().toString(), restorePasswordRequest.getEmail(), token);
            userKafkaTemplate.send("restorePassword", restorePasswordData);
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.badRequest().body("Invalid username or password.");
    }

    @PostMapping("/setPassword")
    public ResponseEntity<?> setNewPassword(@Valid @RequestBody CreateRestorePasswordUserRequest createRestorePasswordUserRequest) {
        log.trace("setNewPassword");

        Optional<User> user = userService.findUserByEmail(createRestorePasswordUserRequest.getEmail());

        if (user.isPresent()) {
            if (user.get().getRestoreToken().equals(createRestorePasswordUserRequest.getRestoreToken())
                    && LocalDateTime.now().isBefore(user.get().getValidRestoreToken()))
            {
                user.get().setPassword(createRestorePasswordUserRequest.getPassword());
                user.get().setRestoreToken(null);
                user.get().setValidRestoreToken(null);
                userService.createUser(user.get());

                RestorePasswordData restorePasswordData = new RestorePasswordData(UUID.randomUUID().toString(),  user.get().getEmail(), null);
                userKafkaTemplate.send("restorePassword", restorePasswordData);
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.badRequest().body("Invalid token reset.");
        }
        return ResponseEntity.badRequest().body("Invalid username or password.");
    }
}
