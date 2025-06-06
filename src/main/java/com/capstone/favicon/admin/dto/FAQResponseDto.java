package com.capstone.favicon.admin.dto;

import com.capstone.favicon.admin.domain.FAQ;
import lombok.Getter;

@Getter
public class FAQResponseDto {
    private Long faqId;
    private String category;
    private String question;
    private String answer;

    public FAQResponseDto(FAQ faq) {
        this.faqId = faq.getFaqId();
        this.category = faq.getCategory().name();
        this.question = faq.getQuestion();
        this.answer = faq.getAnswer();
    }
}