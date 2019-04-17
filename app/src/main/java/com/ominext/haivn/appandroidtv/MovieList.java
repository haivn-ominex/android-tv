/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package com.ominext.haivn.appandroidtv;

import com.ominext.haivn.appandroidtv.model.MyItem;

import java.util.ArrayList;
import java.util.List;

public final class MovieList {
    public static final String MOVIE_CATEGORY[] = {
            "Video",
            "Photo"
    };

    private static List<Movie> list;
    private static long count = 0;

    public static List<Movie> getList(List<MyItem> itemList) {
        if (list == null) {
            list = setupMovies(itemList);
        }
        return list;
    }

    public static List<Movie> setupMovies(List<MyItem> itemList) {
        list = new ArrayList<>();
        //String title[] = String[5];
        List<String> title = new ArrayList<>();
        List<String> videoUrl = new ArrayList<>();
        List<String> bgImageUrl = new ArrayList<>();
        List<String> cardImageUrl = new ArrayList<>();

        for (MyItem item : itemList) {
            title.add(item.getTitle());
            videoUrl.add(item.getWebContentLink());
            bgImageUrl.add(item.getThumbnailLink().replace("220", "480"));
            cardImageUrl.add(item.getThumbnailLink().replace("220", "480"));
        }

        String description = "";
        String studio = "";

        for (int index = 0; index < title.size(); index++) {
            list.add(
                    buildMovieInfo(
                            title.get(index),
                            description,
                            studio,
                            videoUrl.get(index),
                            cardImageUrl.get(index),
                            bgImageUrl.get(index)));
        }

        return list;
    }

    private static Movie buildMovieInfo(
            String title,
            String description,
            String studio,
            String videoUrl,
            String cardImageUrl,
            String backgroundImageUrl) {
        Movie movie = new Movie();
        movie.setId(count++);
        movie.setTitle(title);
        movie.setDescription(description);
        movie.setStudio(studio);
        movie.setCardImageUrl(cardImageUrl);
        movie.setBackgroundImageUrl(backgroundImageUrl);
        movie.setVideoUrl(videoUrl);
        return movie;
    }
}