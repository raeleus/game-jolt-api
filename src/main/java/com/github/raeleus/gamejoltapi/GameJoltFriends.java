package com.github.raeleus.gamejoltapi;

import com.badlogic.gdx.utils.JsonValue;
import lombok.*;

import static com.github.raeleus.gamejoltapi.GameJoltApi.urlEncode;

/**
 * A namespace to get information about users friends on Game Jolt.
 */
public class GameJoltFriends {

    /**
     * Returns the list of a user's friends.
     */
    @Builder
    @Getter
    @Setter
    public static class FriendsFetchRequest implements GameJoltRequest {

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
         * The url string defining this request. Note that it does not contain the base URL pointing to the Game Jolt
         * API.
         */
        @Override
        public String toString() {
            var builder = new StringBuilder();
            builder.append("/friends/?game_id=").append(gameID);
            builder.append("&username=").append(urlEncode(username));
            builder.append("&user_token=").append(urlEncode(userToken));

            return builder.toString();
        }

        /**
         * Handles the server JSON response and returns a corresponding {@link FriendsFetchData}.
         * @param jsonValue The JSON response from the server.
         * @return The {@link FriendsFetchData} with the values returned from the server.
         */
        @Override
        public FriendsFetchData handleResponse(JsonValue jsonValue) {
            return FriendsFetchData.builder()
                .jsonValue(jsonValue)
                .success(jsonValue.getBoolean("success"))
                .message(jsonValue.getString("message", null))
                .friendID(jsonValue.getInt("friends_id"))
                .build();
        }
    }

    /**
     * The result of fetching the friends list.
     */
    @Builder
    @Getter
    @Setter
    public static class FriendsFetchData implements GameJoltData {

        /**
         * The JSON data from the server response.
         */
        public JsonValue jsonValue;

        /**
         * Whether the request succeeded or failed.
         */
        public boolean success;

        /**
         * If the request was not successful, this contains the error message.
         */
        public String message;

        /**
         * The friend's user ID.
         */
        public int friendID;
    }

    /**
     * Listener for {@link FriendsFetchRequest}. Override {@link FriendsFetchListener#friendsFetch(JsonValue,
     * FriendsFetchData)} to handle the server response.
     */
    public static abstract class FriendsFetchListener extends GameJoltAdapter {
        @Override
        public void response(JsonValue jsonValue, GameJoltData data) {
            if (data instanceof FriendsFetchData) friendsFetch(jsonValue, (FriendsFetchData) data);
        }

        public abstract void friendsFetch(JsonValue jsonValue, FriendsFetchData data);
    }
}
