package com.ponir.portfolio.config;

import com.ponir.portfolio.domain.SiteProfile;
import com.ponir.portfolio.service.ContactMessageService;
import com.ponir.portfolio.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.time.Year;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalModelAttributes {
    private final ProfileService profileService;
    private final ContactMessageService contactMessageService;

    @ModelAttribute("profile")
    public SiteProfile profile() {
        return profileService.getProfile();
    }

    @ModelAttribute("currentYear")
    public int currentYear() {
        return Year.now().getValue();
    }

    @ModelAttribute("unreadMessageCount")
    public long unreadMessageCount() {
        return contactMessageService.countUnread();
    }
}
