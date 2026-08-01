package com.ponir.portfolio.controller;

import com.ponir.portfolio.domain.ContactMessage;
import com.ponir.portfolio.service.ContactMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/messages")
@RequiredArgsConstructor
public class AdminMessageController {
    private final ContactMessageService contactMessageService;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("messages", contactMessageService.findAll());
        return "admin/messages/list";
    }

    @GetMapping("/{id}")
    public String view(@PathVariable Long id, Model model) {
        ContactMessage message = contactMessageService.findByIdAndMarkRead(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        model.addAttribute("message", message);
        return "admin/messages/view";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        contactMessageService.delete(id);
        redirectAttributes.addFlashAttribute("success", "Message deleted.");
        return "redirect:/admin/messages";
    }
}
