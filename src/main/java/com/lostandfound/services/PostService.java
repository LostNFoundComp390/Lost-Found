package com.lostandfound.services;

import com.lostandfound.model.Post;
import com.lostandfound.model.User;
import com.lostandfound.model.UserSession;
import com.lostandfound.repo.PostRepository;

import java.util.List;

public class PostService {
    private final PostRepository postRepo;
    public PostService(PostRepository postRepo){
        this.postRepo = postRepo;
    }

    public List<Post> findAll() {
        boolean isAdmin = UserSession.getInstance().getCurrentUser().isAdmin();
        return isAdmin
            ? postRepo.findAll()
            : postRepo.findAllNonExpired();
    }

    public List<Post> search(String keyword) {
        boolean isAdmin = UserSession.getInstance().getCurrentUser().isAdmin();
        if(keyword.isBlank()) {
            return isAdmin
                ? postRepo.findAll()
                : postRepo.findAllNonExpired();
        }else{
            return isAdmin
                ? postRepo.searchByKeyword(keyword)
                : postRepo.searchByKeywordNonExpired(keyword);
        }
    }
}
