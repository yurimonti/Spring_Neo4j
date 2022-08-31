package com.example.Neo4jExample.service;

import com.example.Neo4jExample.model.*;
import com.example.Neo4jExample.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

/**
 * a service class that allows to manage the operation of a Commerciante
 */
@Service
@RequiredArgsConstructor
public class CommercianteService {
    private final CommercianteRepository commercianteRepository;
    private final FoodSectionNodeRepository foodSectionNodeRepository;
    private final MenuNodeRepository menuNodeRepository;
    private final DishNodeRepository dishNodeRepository;
    private final RestaurantPoiRepository restaurantPoiRepository;

    /**
     * create,save and return a dish
     * @param name of dish
     * @param price of dish
     * @param ingradients of dish
     * @return dish just created
     */
    public DishNode createDish(String name,Double price,String ...ingradients){
        DishNode result = new DishNode(name,price,ingradients);
        dishNodeRepository.save(result);
        return result;
    }

    /**
     * create,save and return a food section
     * @param name of section
     * @param dishes of section
     * @return the saved section
     */

    public FoodSectionNode createFoodSection(String name, DishNode ...dishes){
        FoodSectionNode result = new FoodSectionNode(name,dishes);
        foodSectionNodeRepository.save(result);
        return result;
    }

    /**
     * create a menu in for the owner's restaurant
     * @param commerciante owner of a restaurant
     * @param restaurant restaurant from add menu
     * @param priceValue price value (1 to 5) of menu
     * @param foodSections sections of menu
     * @return the menu just created
     */
    public MenuNode createMenu(Commerciante commerciante,RestaurantPoi restaurant,Integer priceValue,
                               FoodSectionNode ...foodSections){
        Collection<RestaurantPoi> restaurants = restaurantPoiRepository.findByOwner(commerciante);
        if(restaurants.contains(restaurant)){
            MenuNode result = new MenuNode(priceValue, foodSections);
            menuNodeRepository.save(result);
            restaurant.setMenu(result);
            restaurantPoiRepository.save(restaurant);
            return result;
        }else return null;
    }


}
