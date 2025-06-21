package com.roadmaps.Roadmaps.modules.roadmap.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.UUID;

@Getter
public class CommentDeleteEvent extends ApplicationEvent {
    private UUID commentId;

    public CommentDeleteEvent(Object source, UUID commentId) {
        super(source);
        this.commentId = commentId;
    }
}
