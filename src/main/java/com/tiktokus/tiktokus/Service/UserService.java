package com.tiktokus.tiktokus.Service;

import com.tiktokus.tiktokus.Convert.UserConvert;
import com.tiktokus.tiktokus.DTO.UserDTO;
import com.tiktokus.tiktokus.Entity.User;
import com.tiktokus.tiktokus.Enum.ProductStatus;
import com.tiktokus.tiktokus.Enum.RoleUser;
import com.tiktokus.tiktokus.Enum.UserStatus;
import com.tiktokus.tiktokus.Repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Slf4j
public class UserService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    PasswordEncoderService passwordEncoderService;

    @Lazy
    @Autowired
    TransactionService transactionService;

    public List<UserDTO> getAll(){
        return UserConvert.convertListToDTO(userRepository.findAll());
    }

    public List<UserDTO> getAllBStatus(List<UserStatus> userStatuses){
        List<RoleUser> roles = new ArrayList<>(List.of(RoleUser.values()));
        roles.remove(RoleUser.ADMIN);
        return UserConvert.convertListToDTO(userRepository.findByStatusInAndRoleIn(userStatuses,roles));
    }
    public User getById(Long id){
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not fuond"));
    }

    public UserDTO register(User user){
        

        user.setPassword(passwordEncoderService.encodedPassword(user.getPassword()));
        userRepository.save(user);
        user.setGroupId(user.getId());
        userRepository.save(user);
        return UserConvert.convertDTO(user);
    }

    @Transactional
    public UserDTO create(User user){
        Optional<User> check = userRepository.findByEmail(user.getEmail());
        Optional<User> check2 = userRepository.findByPhone(user.getPhone());
        if(!check.isEmpty()){
            throw new RuntimeException("Email đã tồn tại!");
        }

        if(!check2.isEmpty()){
            throw new RuntimeException("Số điện thoại đã tồn tại!");
        }

        user.setPassword(passwordEncoderService.encodedPassword(user.getPassword()));
        user.setStatus(UserStatus.ACTIVE);
        user.setRole(user.getRole() == null ? RoleUser.STAFF : user.getRole());
        userRepository.save(user);
        user.setGroupId(user.getId());
        if(user.getBalance() != 0){
            transactionService.create(user.getId(),user.getBalance(),user.getBalance(),"Thay đôi tiền trong ví " + user.getFullName());
        }

        userRepository.save(user);
        return UserConvert.convertDTO(user);
    }

    @Transactional
    public UserDTO edit(User user, Long id){
        User userLogin = getUseLogin();
        if(userLogin.getRole() == RoleUser.STAFF && !Objects.equals(userLogin.getId(), id)){
            throw new RuntimeException("Bạn không có quyền chỉnh sửa tài khoản người khác!");
        }

        Optional<User> check = userRepository.findByEmail(user.getEmail());
        Optional<User> check2 = userRepository.findByPhone(user.getPhone());

        User oldUser = getById(id);

        if(!check.isEmpty() && id != check.get().getId()){
            throw new RuntimeException("Email đã tồn tại!");
        }

        if(!check2.isEmpty() && id != check2.get().getId()){
            throw new RuntimeException("Số điện thoại đã tồn tại!");
        }

        if (user.getPassword() != null && !user.getPassword().isBlank()) {
            oldUser.setPassword(passwordEncoderService.encodedPassword(user.getPassword()));
        }


        if(userLogin.getRole() == RoleUser.ADMIN){
            oldUser.setBalance(user.getBalance() + oldUser.getBalance());
            oldUser.setStatus(user.getStatus());
            transactionService.create(oldUser.getId(),user.getBalance(),oldUser.getBalance(), "Thay đôi tiền trong ví " + user.getFullName());
        }
        oldUser.setEmail(user.getEmail());
        oldUser.setFullName(user.getFullName());
        oldUser.setPhone(user.getPhone());
        userRepository.save(oldUser);
        return UserConvert.convertDTO(oldUser);
    }

    public User getUseLogin(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println( authentication.getName());
        User user = userRepository.findByEmail(authentication.getName()).get();
        return  user;
    }
}
