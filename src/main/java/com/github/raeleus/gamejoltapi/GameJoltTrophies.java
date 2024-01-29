package com.github.raeleus.gamejoltapi;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonValue;
import lombok.*;

import java.util.List;
import java.util.Locale;

import static com.github.raeleus.gamejoltapi.GameJoltApi.urlEncode;

/**
 * Game Jolt allows you to add trophies to your games!
 * <p>
 * Trophies come in four materials: bronze, silver, gold, and platinum. This is to reflect how difficult it is to
 * achieve a trophy. A bronze trophy should be easy to achieve, whereas a platinum trophy should be very hard to
 * achieve.
 * <p>
 * On Game Jolt, trophies are always listed in order from easiest to most difficult to achieve.
 * <p>
 * You can also tag trophies on the site as "secret". A sercet trophy's image and description is not visible until a
 * gamer has achieved it.
 */
public class GameJoltTrophies {
    /**
     * Returns one trophy or multiple trophies, depending on the parameters passed in.
     */
    @Builder
    @Getter
    @Setter
    public static class TrophiesFetchRequest implements GameJoltRequest {

        /**
         * The ID of your game. Required.
         */
        @NonNull
        private String gameID;

        /**
         * The user's username. Required.
         */
        @NonNull
        private String username;

        /**
         * The user's token. Required.
         */
        @NonNull
        private String userToken;

        /**
         * Pass in true to return only the achieved trophies for a user. Pass in false to return only trophies the user
         * hasn't achieved. Leave null to retrieve all trophies. Optional.
         */
        private Boolean achieved;

        /**
         * The ID's of the trophies you'd like to fetch. Optional.
         */
        @Singular
        private List<Integer> trophyIDs;

        /**
         * The url string defining this request. Note that it does not contain the base URL pointing to the Game Jolt
         * API.
         */
        @Override
        public String toString() {
            var builder = new StringBuilder();
            builder.append("/trophies/?game_id=").append(gameID);
            builder.append("&username=").append(urlEncode(username));
            builder.append("&user_token=").append(urlEncode(userToken));
            if (achieved != null) builder.append("&achieved=").append(achieved);
            if (trophyIDs != null && !trophyIDs.isEmpty()) {
                builder.append("&trophy_id=").append(trophyIDs.get(0));
                for (int i = 1; i < trophyIDs.size(); i++) {
                    var trophyID = trophyIDs.get(i);
                    builder.append(",").append(trophyID);
                }
            }

            return builder.toString();
        }

        /**
         * Handles the server JSON response and returns a corresponding {@link TrophiesFetchValue}.
         * @param jsonValue The JSON response from the server.
         * @return The {@link TrophiesFetchValue} with the values returned from the server.
         */
        @Override
        public TrophiesFetchValue handleResponse(JsonValue jsonValue) {

            var trophies = new Array<GameJoltTrophy>();
            var trophiesJsonValue = jsonValue.get("trophies");
            for (var trophyJsonValue : trophiesJsonValue.iterator()) {
                var trophy = GameJoltTrophy.builder()
                    .id(trophyJsonValue.getInt("id"))
                    .title(trophyJsonValue.getString("title"))
                    .description(trophyJsonValue.getString("description"))
                    .difficulty(TrophyDifficulty.valueOf(trophyJsonValue.getString("difficulty").toUpperCase(Locale.ROOT)))
                    .imageURL(trophyJsonValue.getString("image_url"))
                    .achieved(trophyJsonValue.getString("achieved"))
                    .build();
                trophies.add(trophy);
            }

            return TrophiesFetchValue.builder()
                .jsonValue(jsonValue)
                .request(this)
                .success(jsonValue.getBoolean("success"))
                .message(jsonValue.getString("message", null))
                .build();
        }
    }

    /**
     * The result of fetching the trophies.
     */
    @Builder
    @Getter
    @Setter
    public static class TrophiesFetchValue implements GameJoltValue {

        /**
         * The JSON data from the server response.
         */
        public JsonValue jsonValue;
        
        /**
         * The request that triggered the response.
         */
        public GameJoltRequest request;

        /**
         * Whether the request succeeded or failed.
         */
        public boolean success;

        /**
         * If the request was not successful, this contains the error message.
         */
        public String message;

        /**
         * The list of trophies returned from the fetch.
         */
        public Array<GameJoltTrophy> trophies;
    }

    /**
     * Listener for {@link TrophiesFetchRequest}. Override {@link TrophiesFetchListener#trophiesFetch(
     *TrophiesFetchValue)} to handle the server response.
     */
    public static abstract class TrophiesFetchListener extends GameJoltAdapter {
        @Override
        public void response(GameJoltRequest request, GameJoltValue value) {
            if (value instanceof TrophiesFetchValue) trophiesFetch((TrophiesFetchValue) value);
        }

        public abstract void trophiesFetch(TrophiesFetchValue value);
    }

    /**
     * The trophy data.
     */
    @Builder
    @Getter
    @Setter
    public static class GameJoltTrophy {

        /**
         * The ID of the trophy.
         */
        public int id;

        /**
         * The title of the trophy on the site.
         */
        public String title;

        /**
         * The trophy description text.
         */
        public String description;

        /**
         * Bronze, Silver, Gold, or Platinum
         */
        public TrophyDifficulty difficulty;

        /**
         * The URL of the trophy's thumbnail image.
         */
        public String imageURL;

        /**
         * Date/time when the trophy was achieved by the user. Null if it hasn't been achieved.
         */
        public String achieved;
    }

    /**
     * The difficulty of a trophy.
     */
    public enum TrophyDifficulty {
        BRONZE("Bronze"), SILVER("Silver"), GOLD("Gold"), PLATINUM("Platinum");

        public String name;

        TrophyDifficulty(String name) {
            this.name = name;
        }
    }

    /**
     * Sets a trophy as achieved for a particular user.
     */
    @Builder
    @Getter
    @Setter
    public static class TrophiesAddAchievedRequest implements GameJoltRequest {

        /**
         * The ID of your game. Required.
         */
        @NonNull
        private String gameID;

        /**
         * The user's username. Required.
         */
        @NonNull
        private String username;

        /**
         * The user's token. Required.
         */
        @NonNull
        private String userToken;

        /**
         * The ID of the trophy to add for the user. Required.
         */
        @NonNull
        private Integer trophyID;

        /**
         * The url string defining this request. Note that it does not contain the base URL pointing to the Game Jolt
         * API.
         */
        @Override
        public String toString() {
            var builder = new StringBuilder();
            builder.append("/trophies/add-achieved/?game_id=").append(gameID);
            builder.append("&username=").append(urlEncode(username));
            builder.append("&user_token=").append(urlEncode(userToken));
            builder.append("&trophy_id=").append(trophyID);

            return builder.toString();
        }

        /**
         * Handles the server JSON response and returns a corresponding {@link TrophiesAddAchievedValue}.
         * @param jsonValue The JSON response from the server.
         * @return The {@link TrophiesAddAchievedValue} with the values returned from the server.
         */
        @Override
        public TrophiesAddAchievedValue handleResponse(JsonValue jsonValue) {
            return TrophiesAddAchievedValue.builder()
                .jsonValue(jsonValue)
                .request(this)
                .success(jsonValue.getBoolean("success"))
                .message(jsonValue.getString("message", null))
                .build();
        }
    }

    /**
     * The result of adding an achieved trophy.
     */
    @Builder
    @Getter
    @Setter
    public static class TrophiesAddAchievedValue implements GameJoltValue {

        /**
         * The JSON data from the server response.
         */
        public JsonValue jsonValue;
        
        /**
         * The request that triggered the response.
         */
        public GameJoltRequest request;

        /**
         * Whether the request succeeded or failed.
         */
        public boolean success;

        /**
         * If the request was not successful, this contains the error message.
         */
        public String message;
    }

    /**
     * Listener for {@link TrophiesAddAchievedRequest}. Override {@link
     * TrophiesAddAchievedListener#trophiesAddAchieved(TrophiesAddAchievedValue)} to handle the server
     * response.
     */
    public static abstract class TrophiesAddAchievedListener extends GameJoltAdapter {
        @Override
        public void response(GameJoltRequest request, GameJoltValue value) {
            if (value instanceof TrophiesAddAchievedValue) trophiesAddAchieved((TrophiesAddAchievedValue) value);
        }

        public abstract void trophiesAddAchieved(TrophiesAddAchievedValue value);
    }

    /**
     * Remove a previously achieved trophy for a particular user.
     */
    @Builder
    @Getter
    @Setter
    public static class TrophiesRemoveAchievedRequest implements GameJoltRequest {

        /**
         * The ID of your game. Required.
         */
        @NonNull
        private String gameID;

        /**
         * The user's username. Required.
         */
        @NonNull
        private String username;

        /**
         * The user's token. Required.
         */
        @NonNull
        private String userToken;

        /**
         * The ID of the trophy to remove from the user. Required.
         */
        @NonNull
        private Integer trophyID;

        /**
         * The url string defining this request. Note that it does not contain the base URL pointing to the Game Jolt
         * API.
         */
        @Override
        public String toString() {
            var builder = new StringBuilder();
            builder.append("/trophies/remove-achieved/?game_id=").append(gameID);
            builder.append("&username=").append(urlEncode(username));
            builder.append("&user_token=").append(urlEncode(userToken));
            builder.append("&trophy_id=").append(trophyID);

            return builder.toString();
        }

        /**
         * Handles the server JSON response and returns a corresponding {@link TrophiesRemoveAchievedValue}.
         * @param jsonValue The JSON response from the server.
         * @return The {@link TrophiesRemoveAchievedValue} with the values returned from the server.
         */
        @Override
        public TrophiesRemoveAchievedValue handleResponse(JsonValue jsonValue) {
            return TrophiesRemoveAchievedValue.builder()
                .jsonValue(jsonValue)
                .request(this)
                .success(jsonValue.getBoolean("success"))
                .message(jsonValue.getString("message", null))
                .build();
        }
    }

    /**
     * The result of removing a previously achieved trophy.
     */
    @Builder
    @Getter
    @Setter
    public static class TrophiesRemoveAchievedValue implements GameJoltValue {

        /**
         * The JSON data from the server response.
         */
        public JsonValue jsonValue;
        
        /**
         * The request that triggered the response.
         */
        public GameJoltRequest request;

        /**
         * Whether the request succeeded or failed.
         */
        public boolean success;

        /**
         * If the request was not successful, this contains the error message.
         */
        public String message;
    }

    /**
     * Listener for {@link TrophiesRemoveAchievedRequest}. Override {@link
     * TrophiesRemoveAchievedListener#trophiesRemoveAchieved(TrophiesRemoveAchievedValue)} to handle the
     * server response.
     */
    public static abstract class TrophiesRemoveAchievedListener extends GameJoltAdapter {
        @Override
        public void response(GameJoltRequest request, GameJoltValue value) {
            if (value instanceof TrophiesRemoveAchievedValue) trophiesRemoveAchieved((TrophiesRemoveAchievedValue) value);
        }

        public abstract void trophiesRemoveAchieved(TrophiesRemoveAchievedValue value);
    }
}
