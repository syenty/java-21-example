package com.example.demo.user.service.impl;

import com.example.demo.common.dto.PageWrapper;
import com.example.demo.common.exception.BusinessException;
import com.example.demo.common.exception.ErrorCode;
import com.example.demo.user.domain.User;
import com.example.demo.user.dto.UserUpdateRequest;
import com.example.demo.user.repository.UserRepository;
import com.example.demo.user.service.UserService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
  private final UserRepository repository;

  @Override
  public PageWrapper<User> search(String name, String employeeNumber, Pageable pageable) {
    Page<User> page = repository.findAll(pageable);
    return PageWrapper.of(page);
  }

  @Override
  public Optional<User> getById(Long id) {
    return repository.findById(id);
  }

  @Override
    public Optional<User> getByExternalId(String externalId) {
        return repository.findByExternalId(externalId);
    }

    @Override
    public Optional<User> getByEmployeeNumberAndName(String employeeNumber, String name) {
        return repository.findByEmployeeNumberAndName(employeeNumber, name);
    }
  
  @Override
  public User getRequiredByEmployeeNumberAndName(String employeeNumber, String name) {
    return repository.findByEmployeeNumberAndName(employeeNumber, name)
        .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
  }

  @Override
  public User create(User user) {
    User toSave = User.builder()
        .name(user.getName())
        .phoneNumber(user.getPhoneNumber())
        .employeeNumber(user.getEmployeeNumber())
        .branchCode(user.getBranchCode())
        .externalId(UUID.randomUUID().toString())
        .blocked(user.isBlocked())
        .phoneVerified(user.isPhoneVerified())
        .phoneVerifiedDt(user.getPhoneVerifiedDt())
        .build();
    return repository.save(toSave);
  }

  @Override
  public Optional<User> update(Long id, UserUpdateRequest request) {
    return repository.findById(id)
        .map(existing -> {
          existing.update(request.name(), request.employeeNumber(), request.branchCode());
          return repository.save(existing);
        });
  }

  @Override
  public Optional<User> updateBlocked(Long id, boolean blocked) {
    return repository.findById(id)
        .map(existing -> {
          existing.changeBlocked(blocked);
          return repository.save(existing);
        });
  }

  @Override
  public boolean delete(Long id) {
    if (!repository.existsById(id))
      return false;
    repository.deleteById(id);
    return true;
  }
}
