package com.ponir.portfolio.service;

import com.ponir.portfolio.domain.SiteProfile;
import com.ponir.portfolio.repository.SiteProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProfileService {
    private static final long PROFILE_ID = 1L;
    private final SiteProfileRepository profiles;
    private final ImageStorageService imageStorage;

    public SiteProfile getProfile() {
        return profiles.findById(PROFILE_ID).orElseGet(SiteProfile::new);
    }

    @Transactional
    public SiteProfile save(SiteProfile profile, MultipartFile portraitImage) {
        String uploadedImage = imageStorage.store(portraitImage, "profile");
        if (uploadedImage != null) profile.setPortraitUrl(uploadedImage);
        profile.setId(PROFILE_ID);
        return profiles.save(profile);
    }
}
