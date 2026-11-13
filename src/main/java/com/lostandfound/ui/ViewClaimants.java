package com.lostandfound.ui;

import com.lostandfound.model.Claim;
import com.lostandfound.model.Post;
import com.lostandfound.repo.DbPostsRepository;

import javax.swing.*;
import java.util.List;

public class ViewClaimants extends JDialog {
    private final Post post;
    private final List<Claim> claims;
    private final DbPostsRepository myRepo;

    public ViewClaimants(Post post, List<Claim> claims, DbPostsRepository myRepo) {
        this.post = post;
        this.claims = claims;
        this.myRepo = myRepo;
    }
}
