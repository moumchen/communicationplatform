package com.jixiata.model.Vo;

import com.jixiata.model.Bo.Message;

import java.util.List;

public class CommentResult {
    private Message comment;
    private List<Message> childComments;

    public Message getComment() {
        return comment;
    }

    public void setComment(Message comment) {
        this.comment = comment;
    }

    public List<Message> getChildComments() {
        return childComments;
    }

    public void setChildComments(List<Message> childComments) {
        this.childComments = childComments;
    }
}
