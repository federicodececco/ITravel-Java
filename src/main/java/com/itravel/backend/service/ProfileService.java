package com.itravel.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.itravel.backend.models.Profile;
import com.itravel.backend.models.User;
import com.itravel.backend.repository.ProfileRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ProfileService {

    @Autowired
    ProfileRepository profileRepository;

    public boolean createProfileByUser(User user) {
        try {
            Profile profile = new Profile(user);
            profileRepository.save(profile);
            return true;
        } catch (Exception e) {
            log.error("Error during profile creation: {}", e.getMessage());
            return false;
        }

    }

}
