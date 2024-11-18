package com.swe.libraryprogram.domainmodel;

public class Genre {

    // Libri (codice 01)
//     NON_FICTION(1, "Based on real facts and events"),
//    SELF_HELP(1, "Guides for personal growth"),
//    POETRY(1, "Artistic and emotional verses"),
//    CLASSICS(1, "Timeless literary masterpieces"),
//    PHILOSOPHY(1, "Ideas and reflections on life and existence"),
//    CHILDREN(1, "Books for young readers"),
//    YOUNG_ADULT(1, "Stories for teens and young adults");

    // Riviste (codice 02)
//    FASHION(2, "Latest trends in style and clothing"),
//    TECHNOLOGY(2, "Innovations and tech reviews"),
//    HEALTH(2, "Fitness, wellness, and healthy living"),
//    SCIENCE(2, "Discoveries and scientific advancements"),
//    BUSINESS(2, "Economics and market insights"),
//    SPORTS(2, "Updates and stories from the sports world"),
//    LIFESTYLE(2, "Tips for everyday living and hobbies"),
//    TRAVEL(2, "Destinations and travel guides"),
//    FOOD(2, "Recipes, restaurants, and culinary tips"),
//    ENTERTAINMENT(2, "Movies, TV shows, and celebrities"),

    // Media (codice 03)
//    ACTION(3, "High-energy action and excitement"),
//    COMEDY(3, "Laughter and humor"),
//    ANIMATION(3, "Cartoons and animated films"),
//    DOCUMENTARY(3, "Educational and informative content"),
//    WAR(3, "War and historical conflicts"),
//    WESTERN(3, "Cowboys and the Wild West"),
//    MUSICAL(3, "Music and choreography"),
//    SUPERHERO(3, "Stories of superpowered heroes");

    // Riviste e Libri (codice 04)
//    ART(4, "Fine arts, photography, and design"),
//    EDUCATION(4, "Learning resources and academia"),

    // Libri e Media (codice 05)
//    FANTASY(5, "Magical worlds and epic tales"),
//    SCIENCE_FICTION(5, "Futuristic technology and worlds"),
//    ADVENTURE(5, "Exploration and daring journeys"),
//    THRILLER(5, "Suspenseful and fast-paced stories"),
//    MYSTERY(5, "Detective stories and enigmas"),
//    HORROR(5, "Thrilling and frightening tales"),
//    ROMANCE(5, "Stories of love and relationships"),
//    HISTORICAL(5, "Stories based on historical events"),
//    BIOGRAPHICAL(5, "Life stories of real people"),
//    DRAMA(5, "Emotional and complex narratives");

    // Generi Universali (codice 06)
//    SCI_FI(6, "Science fiction across formats"),
//    HISTORICAL(6, "Stories and events from history"),
//    CRIME(6, "Crime stories and investigations");

    private final String name;
    private final Integer code;
    private final String description;


    Genre(String name, Integer code, String description) {
        this.name = name;
        this.code = code;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public Integer getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

}
