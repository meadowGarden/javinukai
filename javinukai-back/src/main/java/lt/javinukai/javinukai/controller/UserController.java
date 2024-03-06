package lt.javinukai.javinukai.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lt.javinukai.javinukai.dto.request.user.UserRegistrationRequest;
import lt.javinukai.javinukai.dto.request.user.UserUpdateRequest;
import lt.javinukai.javinukai.entity.User;
import lt.javinukai.javinukai.mapper.UserMapper;
import lt.javinukai.javinukai.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/users")
@Slf4j
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
@Validated
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/{userId}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MODERATOR', 'ROLE_USER')")
    public ResponseEntity<User> getUser(@PathVariable UUID userId) {
        log.info("Data for user {} requested", userId);
        return ResponseEntity.ok().body(userService.getUser(userId));
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MODERATOR')")
    public ResponseEntity<Page<User>> getAllUsers(@RequestParam(defaultValue = "0") @Min(0) Integer page,
                                                  @RequestParam(defaultValue = "10") @Min(0) Integer limit,
                                                  @RequestParam(defaultValue = "name") String sortBy,
                                                  @RequestParam(defaultValue = "false") boolean sortDesc,
                                                  @RequestParam(required = false) String contains
    ) {
        Sort.Direction direction = sortDesc ? Sort.Direction.DESC : Sort.Direction.ASC;
        Sort sort = Sort.by(direction, sortBy);
        PageRequest pageRequest = PageRequest.of(page, limit, sort);
        return ResponseEntity.ok().body(userService.getUsers(pageRequest, contains));
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MODERATOR', 'ROLE_USER')")
    public ResponseEntity<User> createUser(@RequestBody @Valid UserRegistrationRequest registration) {
        log.info("Creating user manually: {}", registration);
        return ResponseEntity.ok().body(userService.createUser(registration));
    }

    @PutMapping("/{userId}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MODERATOR', 'ROLE_USER')")
    public ResponseEntity<User> updateUser(@PathVariable UUID userId, @RequestBody @Valid UserUpdateRequest updateDTO) {
        return ResponseEntity.ok().body(userService.updateUser(UserMapper.mapToUser(updateDTO), userId));
    }

    @PatchMapping("/{userId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<User> updateUserRole(@PathVariable UUID userId, @RequestBody User newUser) {
        return ResponseEntity.ok().body(userService.updateUserForAdmin(userId, newUser));
    }

    @DeleteMapping("/{userId}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MODERATOR', 'ROLE_USER')")
    @Transactional
    public ResponseEntity<List<User>> deleteUser(@PathVariable UUID userId) {
        return ResponseEntity.ok().body(userService.deleteUser(userId));
    }

}
