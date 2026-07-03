package com.waste.portal.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, length = 1000)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipient_id")
    private User recipient;

    @Column(name = "created_date", nullable = false)
    private LocalDateTime createdDate;

    private boolean isRead = false;

    public Notification() {}

    public Notification(Long id, String title, String content, User recipient, LocalDateTime createdDate, boolean isRead) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.recipient = recipient;
        this.createdDate = createdDate;
        this.isRead = isRead;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public User getRecipient() { return recipient; }
    public void setRecipient(User recipient) { this.recipient = recipient; }

    public LocalDateTime getCreatedDate() { return createdDate; }
    public void setCreatedDate(LocalDateTime createdDate) { this.createdDate = createdDate; }

    public boolean isRead() { return isRead; }
    public void setRead(boolean isRead) { this.isRead = isRead; }

    public static NotificationBuilder builder() {
        return new NotificationBuilder();
    }

    public static class NotificationBuilder {
        private Long id;
        private String title;
        private String content;
        private User recipient;
        private LocalDateTime createdDate;
        private boolean isRead = false;

        public NotificationBuilder id(Long id) { this.id = id; return this; }
        public NotificationBuilder title(String title) { this.title = title; return this; }
        public NotificationBuilder content(String content) { this.content = content; return this; }
        public NotificationBuilder recipient(User recipient) { this.recipient = recipient; return this; }
        public NotificationBuilder createdDate(LocalDateTime createdDate) { this.createdDate = createdDate; return this; }
        public NotificationBuilder isRead(boolean isRead) { this.isRead = isRead; return this; }

        public Notification build() {
            return new Notification(id, title, content, recipient, createdDate, isRead);
        }
    }
}
