package video_sharing_site.back_end.VideoSite.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import video_sharing_site.back_end.VideoSite.Entity.CategoriesEntity;
import video_sharing_site.back_end.VideoSite.Entity.UsersEntity;
import video_sharing_site.back_end.VideoSite.Exception.CategoryExceptions.CategoryException;
import video_sharing_site.back_end.VideoSite.Exception.UserExceptions.UserForbiddenException;
import video_sharing_site.back_end.VideoSite.Repository.CategoriesRepository;
import video_sharing_site.back_end.VideoSite.Repository.UsersRepository;
import video_sharing_site.back_end.VideoSite.Shared.Services.jwt.TokenService;

@Service
public class CategoriesService {

    @Autowired
    private CategoriesRepository categoriesRepository;

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private TokenService tokenService;

    public Map<String, Object> getCategories() {
        List<CategoriesEntity> categories = categoriesRepository.findAll();
        
        List<Map<String, Object>> categorieList = new ArrayList<>();

        for (CategoriesEntity category : categories) {
            Map<String, Object> categoryData = new HashMap<>();
            categoryData.put("id", category.getId());
            categoryData.put("name", category.getName());
            categorieList.add(categoryData);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("categories", categorieList);
        return response;
    }

    public Map<String, Object> newCategory(String Authorization, CategoriesEntity categories) {
        String token = Authorization.split(" ")[1];
        String email = tokenService.getUserFromAccessToken(token);
        UsersEntity user = usersRepository.findByEmail(email);
        if (user == null || !user.isRole()) {
            throw new UserForbiddenException();
        }
        if (categories.getName() == null || categories.getDescription() == null) {
            throw new NullPointerException();
        }
        CategoriesEntity category = categoriesRepository.findByName(categories.getName());
        if (category != null) {
            throw new CategoryException();
        } else {
            category = new CategoriesEntity();
            category.setName(categories.getName());
            category.setDescription(categories.getDescription());
            categoriesRepository.save(category);
        }

        return Map.of("Success", "Category created successfully.");
    }
}
