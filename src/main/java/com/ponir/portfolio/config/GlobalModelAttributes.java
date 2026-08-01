package com.ponir.portfolio.config;

import com.ponir.portfolio.domain.SiteProfile;
import com.ponir.portfolio.repository.ContactMessageRepository;
import com.ponir.portfolio.repository.SiteProfileRepository;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.time.Year;

@ControllerAdvice
public class GlobalModelAttributes {
    private final SiteProfileRepository profileRepository;
    private final ContactMessageRepository messageRepository;

    public GlobalModelAttributes(SiteProfileRepository profileRepository,
                                 ContactMessageRepository messageRepository) {
        this.profileRepository = profileRepository;
        this.messageRepository = messageRepository;
    }

    @ModelAttribute("profile")
    public SiteProfile profile() {
        return profileRepository.findById(1L).orElseGet(SiteProfile::new);
    }

    @ModelAttribute("currentYear")
    public int currentYear() {
        return Year.now().getValue();
    }

    @ModelAttribute("unreadMessageCount")
    public long unreadMessageCount() {
        return messageRepository.countByReadFalse();
    }
}
