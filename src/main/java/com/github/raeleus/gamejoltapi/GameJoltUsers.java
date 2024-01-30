package com.github.raeleus.gamejoltapi;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonValue;
import com.github.raeleus.gamejoltapi.GameJoltApi.GameJoltTextureListener;
import lombok.*;

import java.util.List;
import java.util.Locale;

import static com.github.raeleus.gamejoltapi.GameJoltApi.urlEncode;

/**
 * Your games will not authenticate users by using their username and password. Instead, users have a token to
 * verify themselves along with their username.
 * <p>
 * Passing in the username and token can sometimes interrupt the flow of your game, so Game Jolt makes the effort to
 * automatically pass your game the username and token whenever possible.
 *
 * @see GameJoltApi#downloadImageUrlAsTextureRegion(String, GameJoltTextureListener)
 */
public class GameJoltUsers {

    /**
     * Authenticates the user's information. This should be done before you make any calls for the user, to make sure
     * the user's credentials (username and token) are valid.
     */
    @Builder
    @Getter
    @Setter
    public static class UsersAuthRequest implements GameJoltRequest {

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
            builder.append("/users/auth/?game_id=").append(gameID);
            builder.append("&username=").append(urlEncode(username));
            builder.append("&user_token=").append(urlEncode(userToken));

            return builder.toString();
        }

        /**
         * Handles the server JSON response and returns a corresponding {@link UsersAuthValue}.
         * @param jsonValue The JSON response from the server.
         * @return The {@link UsersAuthValue} with the values returned from the server.
         */
        @Override
        public UsersAuthValue handleResponse(JsonValue jsonValue) {
            return UsersAuthValue.builder()
                .jsonValue(jsonValue)
                .request(this)
                .success(jsonValue.getBoolean("success"))
                .message(jsonValue.getString("message", null))
                .build();
        }
    }

    /**
     * The result of authenticating a user.
     */
    @Builder
    @Getter
    @Setter
    public static class UsersAuthValue implements GameJoltValue {

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
     * Listener for {@link UsersAuthRequest}. Override {@link UsersAuthListener#usersAuth(UsersAuthValue)} to
     * handle the server response.
     */
    public static abstract class UsersAuthListener extends GameJoltAdapter {
        @Override
        public void response(GameJoltRequest request, GameJoltValue value) {
            if (value instanceof UsersAuthValue) usersAuth((UsersAuthValue) value);
        }

        public abstract void usersAuth(UsersAuthValue value);
    }

    /**
     * Returns a user's data. Only one parameter, {@link UsersFetchRequest#setUsername(String)} or {@link
     * UsersFetchRequest#setUserIDs(List)}, is required in addition to {@link UsersFetchRequest#setGameID(String)}.
     * <p>
     * The developer* fields are called this way for backwards compatibility. They are applicable to all users.
     */
    @Builder
    @Getter
    @Setter
    public static class UsersFetchRequest implements GameJoltRequest {

        /**
         * The ID of your game. Required.
         */
        @NonNull
        private String gameID;

        /**
         * The username of the user whose data you'd like to fetch. Either username or userIDs must be provided.
         */
        private String username;

        /**
         * The ID's of the users whose data you'd like to fetch. Either username or userIDs must be provided.
         */
        @Singular
        private List<Integer> userIDs;

        /**
         * The url string defining this request. Note that it does not contain the base URL pointing to the Game Jolt
         * API.
         */
        @Override
        public String toString() {
            var builder = new StringBuilder();
            builder.append("/users/?game_id=").append(gameID);
            if (username != null) builder.append("&username=").append(urlEncode(username));
            else {
                if (!userIDs.isEmpty()) {
                    builder.append("&user_id=").append(userIDs.get(0));
                    for (int i = 1; i < userIDs.size(); i++) {
                        var userID = userIDs.get(i);
                        builder.append(",").append(userID);
                    }
                }
            }

            return builder.toString();
        }

        /**
         * Handles the server JSON response and returns a corresponding {@link UsersFetchValue}.
         * @param jsonValue The JSON response from the server.
         * @return The {@link UsersFetchValue} with the values returned from the server.
         */
        @Override
        public UsersFetchValue handleResponse(JsonValue jsonValue) {
            

            var users = new Array<GameJoltUser>();
            if (jsonValue.has("users")) {
                var usersJsonValue = jsonValue.get("users");
                for (JsonValue userJsonValue : usersJsonValue.iterator()) {
                    var user = GameJoltUser.builder()
                            .id(userJsonValue.getInt("id"))
                            .type(UserType.valueOf(userJsonValue.getString("type").toUpperCase(Locale.ROOT)))
                            .username(userJsonValue.getString("username"))
                            .avatarURL(userJsonValue.getString("avatar_url"))
                            .signedUp(userJsonValue.getString("signed_up"))
                            .signedUpTimestamp(userJsonValue.getLong("signed_up_timestamp"))
                            .lastLoggedIn(userJsonValue.getString("last_logged_in"))
                            .lastLoggedInTimestamp(userJsonValue.getLong("last_logged_in_timestamp"))
                            .status(UserStatus.valueOf(userJsonValue.getString("status").toUpperCase(Locale.ROOT)))
                            .developerName(userJsonValue.getString("developer_name"))
                            .developerWebsite(userJsonValue.getString("developer_website"))
                            .developerDescription(userJsonValue.getString("developer_description"))
                            .build();
                    users.add(user);
                }
            }

            return UsersFetchValue.builder()
                .jsonValue(jsonValue)
                .request(this)
                .success(jsonValue.getBoolean("success"))
                .message(jsonValue.getString("message", null))
                .users(users)
                .build();
        }
    }

    /**
     * The result of fetching the user data.
     */
    @Builder
    @Getter
    @Setter
    public static class UsersFetchValue implements GameJoltValue {

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
         * The users returned by the fetch request.
         */
        public Array<GameJoltUser> users;
    }

    /**
     * Listener for {@link UsersFetchRequest}. Override {@link UsersFetchListener#usersFetch(UsersFetchValue)}
     * to handle the server response.
     */
    public static abstract class UsersFetchListener extends GameJoltAdapter {
        @Override
        public void response(GameJoltRequest request, GameJoltValue value) {
            if (value instanceof UsersFetchValue) usersFetch((UsersFetchValue) value);
        }

        public abstract void usersFetch(UsersFetchValue value);
    }

    /**
     * An individual user as returned by {@link UsersFetchValue}
     */
    @Builder
    @Getter
    @Setter
    @ToString
    public static class GameJoltUser {
        /**
         * The ID of the user.
         */
        public int id;

        /**
         * The type of user. Can be User, Developer, Moderator, or Administrator.
         * @see UserType
         */
        public UserType type;

        /**
         * The user's username.
         */
        public String username;

        /**
         * The URL of the user's avatar.
         * @see GameJoltApi#downloadImageUrlAsTextureRegion(String, GameJoltTextureListener)
         */
        public String avatarURL;

        /**
         * How long ago the user signed up.
         */
        public String signedUp;

        /**
         * The timestamp (in seconds) of when the user signed up.
         */
        public long signedUpTimestamp;

        /**
         * How long ago the user was last logged in. Will be Online Now if the user is currently online.
         */
        public String lastLoggedIn;

        /**
         * The timestamp (in seconds) of when the user was last logged in.
         */
        public long lastLoggedInTimestamp;

        /**
         * Active if the user is still a member of the site. Banned if they've been banned.
         */
        public UserStatus status;

        /**
         * The user's display name.
         */
        public String developerName;

        /**
         * The user's website (or empty string if not specified)
         */
        public String developerWebsite;

        /**
         * The user's profile markdown description.
         */
        public String developerDescription;
    }

    /**
     * The type of User.
     */
    public enum UserType {
        USER("User"), DEVELOPER("Developer"), MODERATOR("Moderator"), ADMINISTRATOR("Administrator");

        public final String name;

        UserType(String name) {
            this.name = name;
        }
    }

    /**
     * The status of the User.
     */
    public enum UserStatus {
        ACTIVE("Active"), BANNED("Banned");

        public final String name;

        UserStatus(String name) {
            this.name = name;
        }
    }
}
