package com.ponir.portfolio.service;

import com.ponir.portfolio.domain.ContactMessage;
import com.ponir.portfolio.repository.ContactMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ContactMessageService {
    private final ContactMessageRepository messages;

    public List<ContactMessage> findAll() {
        return messages.findAllByOrderByCreatedAtDesc();
    }

    public List<ContactMessage> findRecent(int limit) {
        return findAll().stream().limit(limit).toList();
    }

    @Transactional
    public Optional<ContactMessage> findByIdAndMarkRead(Long id) {
        return messages.findById(id).map(message -> {
            if (!message.isRead()) {
                message.setRead(true);
                return messages.save(message);
            }
            return message;
        });
    }

    public long count() {
        return messages.count();
    }

    public long countUnread() {
        return messages.countByReadFalse();
    }

    @Transactional
    public ContactMessage save(ContactMessage message) {
        return messages.save(message);
    }

    @Transactional
    public void delete(Long id) {
        messages.deleteById(id);
    }
}
