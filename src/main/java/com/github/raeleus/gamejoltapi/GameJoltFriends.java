package com.github.raeleus.gamejoltapi;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.IntArray;
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
         * Handles the server JSON response and returns a corresponding {@link FriendsFetchValue}.
         * @param jsonValue The JSON response from the server.
         * @return The {@link FriendsFetchValue} with the values returned from the server.
         */
        @Override
        public FriendsFetchValue handleResponse(JsonValue jsonValue) {
            var friends = new Array<Integer>();
            var friendsJsonValue = jsonValue.get("friends");
            if (friendsJsonValue != null) for (JsonValue friendJsonValue : friendsJsonValue.iterator()) {
                friends.add(friendJsonValue.getInt("friend_id"));
            }
            return FriendsFetchValue.builder()
                .jsonValue(jsonValue)
                .request(this)
                .success(jsonValue.getBoolean("success"))
                .message(jsonValue.getString("message", null))
                .friends(friends)
                .build();
        }
    }

    /**
     * The result of fetching the friends list.
     */
    @Builder
    @Getter
    @Setter
    public static class FriendsFetchValue implements GameJoltValue {

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
         * The friend user ID's.
         */
        public Array<Integer> friends;
    }

    /**
     * Listener for {@link FriendsFetchRequest}. Override {@link FriendsFetchListener#friendsFetch(
     *FriendsFetchValue)} to handle the server response.
     */
    public static abstract class FriendsFetchListener extends GameJoltAdapter {
        @Override
        public void response(GameJoltRequest request, GameJoltValue value) {
            if (value instanceof FriendsFetchValue) friendsFetch((FriendsFetchValue) value);
        }

        public abstract void friendsFetch(FriendsFetchValue value);
    }
}
