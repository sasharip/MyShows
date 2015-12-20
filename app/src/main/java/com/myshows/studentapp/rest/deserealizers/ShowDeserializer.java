package com.myshows.studentapp.rest.deserealizers;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.myshows.studentapp.model.Show;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Map;

public class ShowDeserializer implements JsonDeserializer<ArrayList<Show>> {

    @Override
    public ArrayList<Show> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        ArrayList<Show> shows = new ArrayList<>();

        try {
            for (Map.Entry<String, JsonElement> eShow : json.getAsJsonObject().entrySet()) {
                JsonObject showJson = eShow.getValue().getAsJsonObject();

                Show show = new Show();
                show.id = showJson.get("showId").getAsString();
                show.title = showJson.get("ruTitle").getAsString();
                show.watchedEpisodes = showJson.get("watchedEpisodes").getAsInt();
                show.totalEpisodes = showJson.get("totalEpisodes").getAsInt();
                show.image = showJson.get("image").getAsString();

                shows.add(show);
            }
        } catch (IllegalStateException ignored) {
        }
        return shows;
    }

}
