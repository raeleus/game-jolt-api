package com.github.raeleus.gamejoltapi;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonValue;
import com.github.raeleus.gamejoltapi.GameJoltApi.GameJoltTextureListener;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

import static com.github.raeleus.gamejoltapi.GameJoltApi.urlEncode;

/**
 * Your games will not authenticate users by using their username and password. Instead, users have a token to verify
 * themselves along with their username.
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
        
        UsersAuthRequest(@NonNull String gameID, @NonNull String username, @NonNull String userToken) {
            this.gameID = gameID;
            this.username = username;
            this.userToken = userToken;
        }
        
        public static UsersAuthRequestBuilder builder() {
            return new UsersAuthRequestBuilder();
        }
        
        /**
         * The url string defining this request. Note that it does not contain the base URL pointing to the Game Jolt
         * API.
         */
        @Override
        public String toString() {
            String builder = "/users/auth/?game_id=" + gameID +
                    "&username=" + urlEncode(username) +
                    "&user_token=" + urlEncode(userToken);
            
            return builder;
        }
        
        /**
         * Handles the server JSON response and returns a corresponding {@link UsersAuthValue}.
         *
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
        
        public @NonNull String getGameID() {
            return this.gameID;
        }
        
        public @NonNull String getUsername() {
            return this.username;
        }
        
        public @NonNull String getUserToken() {
            return this.userToken;
        }
        
        public void setGameID(@NonNull String gameID) {
            this.gameID = gameID;
        }
        
        public void setUsername(@NonNull String username) {
            this.username = username;
        }
        
        public void setUserToken(@NonNull String userToken) {
            this.userToken = userToken;
        }
        
        public static class UsersAuthRequestBuilder {
            private @NonNull String gameID;
            private @NonNull String username;
            private @NonNull String userToken;
            
            UsersAuthRequestBuilder() {
            }
            
            public UsersAuthRequestBuilder gameID(@NonNull String gameID) {
                this.gameID = gameID;
                return this;
            }
            
            public UsersAuthRequestBuilder username(@NonNull String username) {
                this.username = username;
                return this;
            }
            
            public UsersAuthRequestBuilder userToken(@NonNull String userToken) {
                this.userToken = userToken;
                return this;
            }
            
            public UsersAuthRequest build() {
                return new UsersAuthRequest(this.gameID, this.username, this.userToken);
            }
            
            public String toString() {
                return "GameJoltUsers.UsersAuthRequest.UsersAuthRequestBuilder(gameID=" + this.gameID + ", username=" + this.username + ", userToken=" + this.userToken + ")";
            }
        }
    }
    
    /**
     * The result of authenticating a user.
     */
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
        
        UsersAuthValue(JsonValue jsonValue, GameJoltRequest request, boolean success, String message) {
            this.jsonValue = jsonValue;
            this.request = request;
            this.success = success;
            this.message = message;
        }
        
        public static UsersAuthValueBuilder builder() {
            return new UsersAuthValueBuilder();
        }
        
        public JsonValue getJsonValue() {
            return this.jsonValue;
        }
        
        public GameJoltRequest getRequest() {
            return this.request;
        }
        
        public boolean isSuccess() {
            return this.success;
        }
        
        public String getMessage() {
            return this.message;
        }
        
        public void setJsonValue(JsonValue jsonValue) {
            this.jsonValue = jsonValue;
        }
        
        public void setRequest(GameJoltRequest request) {
            this.request = request;
        }
        
        public void setSuccess(boolean success) {
            this.success = success;
        }
        
        public void setMessage(String message) {
            this.message = message;
        }
        
        public static class UsersAuthValueBuilder {
            private JsonValue jsonValue;
            private GameJoltRequest request;
            private boolean success;
            private String message;
            
            UsersAuthValueBuilder() {
            }
            
            public UsersAuthValueBuilder jsonValue(JsonValue jsonValue) {
                this.jsonValue = jsonValue;
                return this;
            }
            
            public UsersAuthValueBuilder request(GameJoltRequest request) {
                this.request = request;
                return this;
            }
            
            public UsersAuthValueBuilder success(boolean success) {
                this.success = success;
                return this;
            }
            
            public UsersAuthValueBuilder message(String message) {
                this.message = message;
                return this;
            }
            
            public UsersAuthValue build() {
                return new UsersAuthValue(this.jsonValue, this.request, this.success, this.message);
            }
            
            public String toString() {
                return "GameJoltUsers.UsersAuthValue.UsersAuthValueBuilder(jsonValue=" + this.jsonValue + ", request=" + this.request + ", success=" + this.success + ", message=" + this.message + ")";
            }
        }
    }
    
    /**
     * Listener for {@link UsersAuthRequest}. Override {@link UsersAuthListener#usersAuth(UsersAuthValue)} to handle the
     * server response.
     */
    public static abstract class UsersAuthListener extends GameJoltAdapter {
        @Override
        public void response(GameJoltRequest request, GameJoltValue value) {
            if (value instanceof UsersAuthValue) usersAuth((UsersAuthValue) value);
        }
        
        public abstract void usersAuth(UsersAuthValue value);
    }
    
    /**
     * Returns a user's data. Only one parameter, {@link UsersFetchRequest#setUsername(String)} or
     * {@link UsersFetchRequest#setUserIDs(List)}, is required in addition to
     * {@link UsersFetchRequest#setGameID(String)}.
     * <p>
     * The developer* fields are called this way for backwards compatibility. They are applicable to all users.
     */
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
        private List<Integer> userIDs;
        
        UsersFetchRequest(@NonNull String gameID, String username, List<Integer> userIDs) {
            this.gameID = gameID;
            this.username = username;
            this.userIDs = userIDs;
        }
        
        public static UsersFetchRequestBuilder builder() {
            return new UsersFetchRequestBuilder();
        }
        
        /**
         * The url string defining this request. Note that it does not contain the base URL pointing to the Game Jolt
         * API.
         */
        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder();
            builder.append("/users/?game_id=").append(gameID);
            if (username != null) builder.append("&username=").append(urlEncode(username));
            else {
                if (!userIDs.isEmpty()) {
                    builder.append("&user_id=").append(userIDs.get(0));
                    for (int i = 1; i < userIDs.size(); i++) {
                        Integer userID = userIDs.get(i);
                        builder.append(",").append(userID);
                    }
                }
            }
            
            return builder.toString();
        }
        
        /**
         * Handles the server JSON response and returns a corresponding {@link UsersFetchValue}.
         *
         * @param jsonValue The JSON response from the server.
         * @return The {@link UsersFetchValue} with the values returned from the server.
         */
        @Override
        public UsersFetchValue handleResponse(JsonValue jsonValue) {
            
            
            Array<GameJoltUser> users = new Array<GameJoltUser>();
            if (jsonValue.has("users")) {
                JsonValue usersJsonValue = jsonValue.get("users");
                for (JsonValue userJsonValue : usersJsonValue.iterator()) {
                    GameJoltUser user = GameJoltUser.builder()
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
        
        public @NonNull String getGameID() {
            return this.gameID;
        }
        
        public String getUsername() {
            return this.username;
        }
        
        public List<Integer> getUserIDs() {
            return this.userIDs;
        }
        
        public void setGameID(@NonNull String gameID) {
            this.gameID = gameID;
        }
        
        public void setUsername(String username) {
            this.username = username;
        }
        
        public void setUserIDs(List<Integer> userIDs) {
            this.userIDs = userIDs;
        }
        
        public static class UsersFetchRequestBuilder {
            private @NonNull String gameID;
            private String username;
            private ArrayList<Integer> userIDs;
            
            UsersFetchRequestBuilder() {
            }
            
            public UsersFetchRequestBuilder gameID(@NonNull String gameID) {
                this.gameID = gameID;
                return this;
            }
            
            public UsersFetchRequestBuilder username(String username) {
                this.username = username;
                return this;
            }
            
            public UsersFetchRequestBuilder userID(Integer userID) {
                if (this.userIDs == null) this.userIDs = new ArrayList<Integer>();
                this.userIDs.add(userID);
                return this;
            }
            
            public UsersFetchRequestBuilder userIDs(Collection<? extends Integer> userIDs) {
                if (userIDs == null) {
                    throw new NullPointerException("userIDs cannot be null");
                }
                if (this.userIDs == null) this.userIDs = new ArrayList<Integer>();
                this.userIDs.addAll(userIDs);
                return this;
            }
            
            public UsersFetchRequestBuilder clearUserIDs() {
                if (this.userIDs != null)
                    this.userIDs.clear();
                return this;
            }
            
            public UsersFetchRequest build() {
                List<Integer> userIDs;
                switch (this.userIDs == null ? 0 : this.userIDs.size()) {
                    case 0:
                        userIDs = java.util.Collections.emptyList();
                        break;
                    case 1:
                        userIDs = java.util.Collections.singletonList(this.userIDs.get(0));
                        break;
                    default:
                        userIDs = java.util.Collections.unmodifiableList(new ArrayList<Integer>(this.userIDs));
                }
                
                return new UsersFetchRequest(this.gameID, this.username, userIDs);
            }
            
            public String toString() {
                return "GameJoltUsers.UsersFetchRequest.UsersFetchRequestBuilder(gameID=" + this.gameID + ", username=" + this.username + ", userIDs=" + this.userIDs + ")";
            }
        }
    }
    
    /**
     * The result of fetching the user data.
     */
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
        
        UsersFetchValue(JsonValue jsonValue, GameJoltRequest request, boolean success, String message,
                        Array<GameJoltUser> users) {
            this.jsonValue = jsonValue;
            this.request = request;
            this.success = success;
            this.message = message;
            this.users = users;
        }
        
        public static UsersFetchValueBuilder builder() {
            return new UsersFetchValueBuilder();
        }
        
        public JsonValue getJsonValue() {
            return this.jsonValue;
        }
        
        public GameJoltRequest getRequest() {
            return this.request;
        }
        
        public boolean isSuccess() {
            return this.success;
        }
        
        public String getMessage() {
            return this.message;
        }
        
        public Array<GameJoltUser> getUsers() {
            return this.users;
        }
        
        public void setJsonValue(JsonValue jsonValue) {
            this.jsonValue = jsonValue;
        }
        
        public void setRequest(GameJoltRequest request) {
            this.request = request;
        }
        
        public void setSuccess(boolean success) {
            this.success = success;
        }
        
        public void setMessage(String message) {
            this.message = message;
        }
        
        public void setUsers(Array<GameJoltUser> users) {
            this.users = users;
        }
        
        public static class UsersFetchValueBuilder {
            private JsonValue jsonValue;
            private GameJoltRequest request;
            private boolean success;
            private String message;
            private Array<GameJoltUser> users;
            
            UsersFetchValueBuilder() {
            }
            
            public UsersFetchValueBuilder jsonValue(JsonValue jsonValue) {
                this.jsonValue = jsonValue;
                return this;
            }
            
            public UsersFetchValueBuilder request(GameJoltRequest request) {
                this.request = request;
                return this;
            }
            
            public UsersFetchValueBuilder success(boolean success) {
                this.success = success;
                return this;
            }
            
            public UsersFetchValueBuilder message(String message) {
                this.message = message;
                return this;
            }
            
            public UsersFetchValueBuilder users(Array<GameJoltUser> users) {
                this.users = users;
                return this;
            }
            
            public UsersFetchValue build() {
                return new UsersFetchValue(this.jsonValue, this.request, this.success, this.message, this.users);
            }
            
            public String toString() {
                return "GameJoltUsers.UsersFetchValue.UsersFetchValueBuilder(jsonValue=" + this.jsonValue + ", request=" + this.request + ", success=" + this.success + ", message=" + this.message + ", users=" + this.users + ")";
            }
        }
    }
    
    /**
     * Listener for {@link UsersFetchRequest}. Override {@link UsersFetchListener#usersFetch(UsersFetchValue)} to handle
     * the server response.
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
    public static class GameJoltUser {
        /**
         * The ID of the user.
         */
        public int id;
        
        /**
         * The type of user. Can be User, Developer, Moderator, or Administrator.
         *
         * @see UserType
         */
        public UserType type;
        
        /**
         * The user's username.
         */
        public String username;
        
        /**
         * The URL of the user's avatar.
         *
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
        
        GameJoltUser(int id, UserType type, String username, String avatarURL, String signedUp, long signedUpTimestamp,
                     String lastLoggedIn, long lastLoggedInTimestamp, UserStatus status, String developerName,
                     String developerWebsite, String developerDescription) {
            this.id = id;
            this.type = type;
            this.username = username;
            this.avatarURL = avatarURL;
            this.signedUp = signedUp;
            this.signedUpTimestamp = signedUpTimestamp;
            this.lastLoggedIn = lastLoggedIn;
            this.lastLoggedInTimestamp = lastLoggedInTimestamp;
            this.status = status;
            this.developerName = developerName;
            this.developerWebsite = developerWebsite;
            this.developerDescription = developerDescription;
        }
        
        public static GameJoltUserBuilder builder() {
            return new GameJoltUserBuilder();
        }
        
        public int getId() {
            return this.id;
        }
        
        public UserType getType() {
            return this.type;
        }
        
        public String getUsername() {
            return this.username;
        }
        
        public String getAvatarURL() {
            return this.avatarURL;
        }
        
        public String getSignedUp() {
            return this.signedUp;
        }
        
        public long getSignedUpTimestamp() {
            return this.signedUpTimestamp;
        }
        
        public String getLastLoggedIn() {
            return this.lastLoggedIn;
        }
        
        public long getLastLoggedInTimestamp() {
            return this.lastLoggedInTimestamp;
        }
        
        public UserStatus getStatus() {
            return this.status;
        }
        
        public String getDeveloperName() {
            return this.developerName;
        }
        
        public String getDeveloperWebsite() {
            return this.developerWebsite;
        }
        
        public String getDeveloperDescription() {
            return this.developerDescription;
        }
        
        public void setId(int id) {
            this.id = id;
        }
        
        public void setType(UserType type) {
            this.type = type;
        }
        
        public void setUsername(String username) {
            this.username = username;
        }
        
        public void setAvatarURL(String avatarURL) {
            this.avatarURL = avatarURL;
        }
        
        public void setSignedUp(String signedUp) {
            this.signedUp = signedUp;
        }
        
        public void setSignedUpTimestamp(long signedUpTimestamp) {
            this.signedUpTimestamp = signedUpTimestamp;
        }
        
        public void setLastLoggedIn(String lastLoggedIn) {
            this.lastLoggedIn = lastLoggedIn;
        }
        
        public void setLastLoggedInTimestamp(long lastLoggedInTimestamp) {
            this.lastLoggedInTimestamp = lastLoggedInTimestamp;
        }
        
        public void setStatus(UserStatus status) {
            this.status = status;
        }
        
        public void setDeveloperName(String developerName) {
            this.developerName = developerName;
        }
        
        public void setDeveloperWebsite(String developerWebsite) {
            this.developerWebsite = developerWebsite;
        }
        
        public void setDeveloperDescription(String developerDescription) {
            this.developerDescription = developerDescription;
        }
        
        public String toString() {
            return "GameJoltUsers.GameJoltUser(id=" + this.getId() + ", type=" + this.getType() + ", username=" + this.getUsername() + ", avatarURL=" + this.getAvatarURL() + ", signedUp=" + this.getSignedUp() + ", signedUpTimestamp=" + this.getSignedUpTimestamp() + ", lastLoggedIn=" + this.getLastLoggedIn() + ", lastLoggedInTimestamp=" + this.getLastLoggedInTimestamp() + ", status=" + this.getStatus() + ", developerName=" + this.getDeveloperName() + ", developerWebsite=" + this.getDeveloperWebsite() + ", developerDescription=" + this.getDeveloperDescription() + ")";
        }
        
        public static class GameJoltUserBuilder {
            private int id;
            private UserType type;
            private String username;
            private String avatarURL;
            private String signedUp;
            private long signedUpTimestamp;
            private String lastLoggedIn;
            private long lastLoggedInTimestamp;
            private UserStatus status;
            private String developerName;
            private String developerWebsite;
            private String developerDescription;
            
            GameJoltUserBuilder() {
            }
            
            public GameJoltUserBuilder id(int id) {
                this.id = id;
                return this;
            }
            
            public GameJoltUserBuilder type(UserType type) {
                this.type = type;
                return this;
            }
            
            public GameJoltUserBuilder username(String username) {
                this.username = username;
                return this;
            }
            
            public GameJoltUserBuilder avatarURL(String avatarURL) {
                this.avatarURL = avatarURL;
                return this;
            }
            
            public GameJoltUserBuilder signedUp(String signedUp) {
                this.signedUp = signedUp;
                return this;
            }
            
            public GameJoltUserBuilder signedUpTimestamp(long signedUpTimestamp) {
                this.signedUpTimestamp = signedUpTimestamp;
                return this;
            }
            
            public GameJoltUserBuilder lastLoggedIn(String lastLoggedIn) {
                this.lastLoggedIn = lastLoggedIn;
                return this;
            }
            
            public GameJoltUserBuilder lastLoggedInTimestamp(long lastLoggedInTimestamp) {
                this.lastLoggedInTimestamp = lastLoggedInTimestamp;
                return this;
            }
            
            public GameJoltUserBuilder status(UserStatus status) {
                this.status = status;
                return this;
            }
            
            public GameJoltUserBuilder developerName(String developerName) {
                this.developerName = developerName;
                return this;
            }
            
            public GameJoltUserBuilder developerWebsite(String developerWebsite) {
                this.developerWebsite = developerWebsite;
                return this;
            }
            
            public GameJoltUserBuilder developerDescription(String developerDescription) {
                this.developerDescription = developerDescription;
                return this;
            }
            
            public GameJoltUser build() {
                return new GameJoltUser(this.id, this.type, this.username, this.avatarURL, this.signedUp,
                        this.signedUpTimestamp, this.lastLoggedIn, this.lastLoggedInTimestamp, this.status,
                        this.developerName, this.developerWebsite, this.developerDescription);
            }
            
            public String toString() {
                return "GameJoltUsers.GameJoltUser.GameJoltUserBuilder(id=" + this.id + ", type=" + this.type + ", username=" + this.username + ", avatarURL=" + this.avatarURL + ", signedUp=" + this.signedUp + ", signedUpTimestamp=" + this.signedUpTimestamp + ", lastLoggedIn=" + this.lastLoggedIn + ", lastLoggedInTimestamp=" + this.lastLoggedInTimestamp + ", status=" + this.status + ", developerName=" + this.developerName + ", developerWebsite=" + this.developerWebsite + ", developerDescription=" + this.developerDescription + ")";
            }
        }
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
