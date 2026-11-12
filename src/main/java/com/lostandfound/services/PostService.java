package com.lostandfound.services;

import com.lostandfound.model.Post;
import com.lostandfound.model.UserSession;
import com.lostandfound.repo.PostRepository;

import java.util.List;

public class PostService {
    private final PostRepository postRepo;
    public PostService(PostRepository postRepo){
        this.postRepo = postRepo;
    }


    public List<Post> findAll() {
        return postRepo.findAll();
    }

    public List<Post> search(String keyword) {
        boolean isAdmin = UserSession.getInstance().getCurrentUser().isAdmin();

        if(!keyword.isBlank()) {
            return isAdmin
                ? postRepo.adminSearchByKeyword(keyword)
                : postRepo.userSearchByKeyword(keyword);
        }else{
            throw new IllegalArgumentException("keyword is empty");
        }
    }
}
