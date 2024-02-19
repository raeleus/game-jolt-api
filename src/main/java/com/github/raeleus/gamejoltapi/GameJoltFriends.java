package com.github.raeleus.gamejoltapi;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonValue;
import lombok.NonNull;

import static com.github.raeleus.gamejoltapi.GameJoltApi.urlEncode;

/**
 * A namespace to get information about users friends on Game Jolt.
 */
public class GameJoltFriends {
    
    /**
     * Returns the list of a user's friends.
     */
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
        
        FriendsFetchRequest(@NonNull String gameID, @NonNull String username, @NonNull String userToken) {
            this.gameID = gameID;
            this.username = username;
            this.userToken = userToken;
        }
        
        public static FriendsFetchRequestBuilder builder() {
            return new FriendsFetchRequestBuilder();
        }
        
        /**
         * The url string defining this request. Note that it does not contain the base URL pointing to the Game Jolt
         * API.
         */
        @Override
        public String toString() {
            String builder = "/friends/?game_id=" + gameID +
                    "&username=" + urlEncode(username) +
                    "&user_token=" + urlEncode(userToken);
            
            return builder;
        }
        
        /**
         * Handles the server JSON response and returns a corresponding {@link FriendsFetchValue}.
         *
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
        
        public static class FriendsFetchRequestBuilder {
            private @NonNull String gameID;
            private @NonNull String username;
            private @NonNull String userToken;
            
            FriendsFetchRequestBuilder() {
            }
            
            public FriendsFetchRequestBuilder gameID(@NonNull String gameID) {
                this.gameID = gameID;
                return this;
            }
            
            public FriendsFetchRequestBuilder username(@NonNull String username) {
                this.username = username;
                return this;
            }
            
            public FriendsFetchRequestBuilder userToken(@NonNull String userToken) {
                this.userToken = userToken;
                return this;
            }
            
            public FriendsFetchRequest build() {
                return new FriendsFetchRequest(this.gameID, this.username, this.userToken);
            }
            
            public String toString() {
                return "GameJoltFriends.FriendsFetchRequest.FriendsFetchRequestBuilder(gameID=" + this.gameID + ", username=" + this.username + ", userToken=" + this.userToken + ")";
            }
        }
    }
    
    /**
     * The result of fetching the friends list.
     */
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
        
        FriendsFetchValue(JsonValue jsonValue, GameJoltRequest request, boolean success, String message,
                          Array<Integer> friends) {
            this.jsonValue = jsonValue;
            this.request = request;
            this.success = success;
            this.message = message;
            this.friends = friends;
        }
        
        public static FriendsFetchValueBuilder builder() {
            return new FriendsFetchValueBuilder();
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
        
        public Array<Integer> getFriends() {
            return this.friends;
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
        
        public void setFriends(Array<Integer> friends) {
            this.friends = friends;
        }
        
        public static class FriendsFetchValueBuilder {
            private JsonValue jsonValue;
            private GameJoltRequest request;
            private boolean success;
            private String message;
            private Array<Integer> friends;
            
            FriendsFetchValueBuilder() {
            }
            
            public FriendsFetchValueBuilder jsonValue(JsonValue jsonValue) {
                this.jsonValue = jsonValue;
                return this;
            }
            
            public FriendsFetchValueBuilder request(GameJoltRequest request) {
                this.request = request;
                return this;
            }
            
            public FriendsFetchValueBuilder success(boolean success) {
                this.success = success;
                return this;
            }
            
            public FriendsFetchValueBuilder message(String message) {
                this.message = message;
                return this;
            }
            
            public FriendsFetchValueBuilder friends(Array<Integer> friends) {
                this.friends = friends;
                return this;
            }
            
            public FriendsFetchValue build() {
                return new FriendsFetchValue(this.jsonValue, this.request, this.success, this.message, this.friends);
            }
            
            public String toString() {
                return "GameJoltFriends.FriendsFetchValue.FriendsFetchValueBuilder(jsonValue=" + this.jsonValue + ", request=" + this.request + ", success=" + this.success + ", message=" + this.message + ", friends=" + this.friends + ")";
            }
        }
    }
    
    /**
     * Listener for {@link FriendsFetchRequest}. Override {@link FriendsFetchListener#friendsFetch(FriendsFetchValue)}
     * to handle the server response.
     */
    public static abstract class FriendsFetchListener extends GameJoltAdapter {
        @Override
        public void response(GameJoltRequest request, GameJoltValue value) {
            if (value instanceof FriendsFetchValue) friendsFetch((FriendsFetchValue) value);
        }
        
        public abstract void friendsFetch(FriendsFetchValue value);
    }
}
