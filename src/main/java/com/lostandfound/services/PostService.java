package com.lostandfound.services;

import com.lostandfound.model.Post;
import com.lostandfound.model.UserSession;
import com.lostandfound.repo.PostRepository;

import java.time.LocalDate;
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
            : postRepo.findAllUser();
    }

    public List<Post> search(String keyword) {
        boolean isAdmin = UserSession.getInstance().getCurrentUser().isAdmin();
        if(keyword.isBlank()) {
            return isAdmin
                ? postRepo.findAll()
                : postRepo.findAllUser();
        }else{
            return isAdmin
                ? postRepo.searchByKeyword(keyword)
                : postRepo.searchByKeywordUser(keyword);
        }
    }



    public void updateExpiredStatus(){

        postRepo.updateExpiredStatus();

    }

    public void updatePost(Post post){
        postRepo.updatePost(post);
    }

    public void deletePost(long postID){
        postRepo.deletePost(postID);
    }
}
