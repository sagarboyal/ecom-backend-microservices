package com.main.userservice.serviceImpl;

import com.main.userservice.exceptions.custom.ResourceNotFoundException;
import com.main.userservice.model.Address;
import com.main.userservice.model.Role;
import com.main.userservice.model.Roles;
import com.main.userservice.model.User;
import com.main.userservice.payload.request.UserRequest;
import com.main.userservice.payload.response.AddressResponse;
import com.main.userservice.payload.response.PagedResponse;
import com.main.userservice.payload.response.UserResponse;
import com.main.userservice.repository.RoleRepository;
import com.main.userservice.repository.UserRepository;
import com.main.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final RoleRepository roleRepository;

    @Override
    public UserResponse createUser(UserRequest userRequest) {
        User user = modelMapper.map(userRequest, User.class);
        user.setRoles(Set.of(roleRepository.findByRoleName(Roles.ROLE_USER).get()));
        user = userRepository.save(user);
        return convertToUserResponse(user);

    }

    @Override
    public UserResponse getUserById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        return convertToUserResponse(user);
    }

    @Override
    public UserResponse updateUser(Long id, UserRequest request) {
        User user = validateUser(id);
        user.setUserName(request.getUserName() != null ? request.getUserName() : user.getUserName());
        user.setEmail(request.getEmail() != null ? request.getEmail() : user.getEmail());
        user.setPassword(request.getPassword() != null ? request.getPassword() : user.getPassword());
        user =  userRepository.save(user);
        return convertToUserResponse(user);
    }

    @Override
    public void deleteUser(Long id) {
        User user = validateUser(id);
        userRepository.delete(user);
    }

    @Override
    public PagedResponse<UserResponse> getAllUser(Integer pageNumber,Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<User> userPage = userRepository.findAll(pageable);

        List<UserResponse> userResponses = userPage.getContent().stream()
                .map(this::convertToUserResponse)
                .collect(Collectors.toList());

        return PagedResponse.<UserResponse>builder()
                .content(userResponses)
                .pageNumber(userPage.getNumber())
                .pageSize(userPage.getSize())
                .totalElements(userPage.getTotalElements())
                .totalPages(userPage.getTotalPages())
                .lastPage(userPage.isLast())
                .build();
    }

    @Override
    public UserResponse getUserByEmailId(String emailId) {
        return convertToUserResponse(userRepository.findByEmail(emailId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", emailId)));
    }


    private UserResponse convertToUserResponse(User user) {
        return UserResponse.builder()
                .userId(user.getUserId())
                .userName(user.getUserName())
                .email(user.getEmail())
                .roles(user.getRoles().stream()
                        .map(role -> role.getRoleName().name())
                        .collect(Collectors.toSet()))
                .addresses(user.getAddresses().stream()
                        .map(this::convertToAddressResponse)
                        .collect(Collectors.toList()))
                .build();
    }

    private AddressResponse convertToAddressResponse(Address address) {
        return AddressResponse.builder()
                .addressId(address.getAddressId())
                .street(address.getStreet())
                .building(address.getBuilding())
                .city(address.getCity())
                .state(address.getState())
                .country(address.getCountry())
                .zipcode(address.getZipcode())
                .build();
    }

    private User validateUser(Long userId) {
        return  userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
    }
}
