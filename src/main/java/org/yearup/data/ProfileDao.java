package org.yearup.data;


import org.yearup.models.Profile;

public interface ProfileDao
{
    Profile create(Profile profile);
    // add additional method signatures here
    Profile getByUserId(int userId);
    void updateUser(Profile profile);
}
