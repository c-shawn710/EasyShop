package org.yearup.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.yearup.data.ProfileDao;
import org.yearup.data.UserDao;
import org.yearup.models.Profile;
import org.yearup.models.User;

import java.security.Principal;

@RestController
@RequestMapping("/profile")
@CrossOrigin
@PreAuthorize("isAuthenticated()")
public class ProfileController {
    private ProfileDao profileDao;
    private UserDao userDao;

    @Autowired
    public ProfileController(ProfileDao profileDao, UserDao userDao) {
        this.profileDao = profileDao;
        this.userDao = userDao;
    }

    @GetMapping("")
    public Profile getProfile(Principal principal) {
        try {
            // get the currently logged in username
            String userName = principal.getName();
            // find the user in the database
            User user = userDao.getByUserName(userName);
            int userId = user.getId();

            // use the profileDao to get user's profile
            return profileDao.getByUserId(userId);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad.");
        }
    }

    @PutMapping("")
    public ResponseEntity<String> updateProfile(@RequestBody Profile updatedProfile, Principal principal) {
        try {
            // get the currently logged in username
            String userName = principal.getName();
            // find the user in the database
            User user = userDao.getByUserName(userName);
            int userId = user.getId();

            // set the userId in the updated profile
            updatedProfile.setUserId(userId);

            // update profile in the database
            profileDao.updateUser(updatedProfile);
            return ResponseEntity.ok("Profile updated successfully.");
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to update profile.");
        }
    }
}
