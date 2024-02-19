package com.github.raeleus.gamejoltapi;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonValue;
import lombok.NonNull;

import static com.github.raeleus.gamejoltapi.GameJoltApi.urlEncode;

/**
 * Game Jolt provides cloud-based storage for games. This can store anything that can be submitted as a String. Examples
 * include global game statistics, user-specific statistics, in-game messaging, user-generated content, and more. Data
 * storage is limited to 16MB per key.
 */
public class GameJoltDataStore {
    /**
     * Returns data from the data store. Data can be fetched for an individual user or from the global game store. To
     * access the global game store, leave username and userToken as null.
     */
    public static class DataStoreFetchRequest implements GameJoltRequest {
        /**
         * The ID of your game. Required.
         */
        @NonNull
        private String gameID;
        
        /**
         * The key of the data item you'd like to fetch. You may apply a pattern to the request using the "*" as a
         * placeholder. Only keys with applicable key names will be returned. Required.
         */
        @NonNull
        private String key;
        
        /**
         * The user's username. Optional.
         */
        private String username;
        
        /**
         * The user’s token. Optional.
         */
        private String userToken;
        
        DataStoreFetchRequest(@NonNull String gameID, @NonNull String key, String username, String userToken) {
            this.gameID = gameID;
            this.key = key;
            this.username = username;
            this.userToken = userToken;
        }
        
        public static DataStoreFetchRequestBuilder builder() {
            return new DataStoreFetchRequestBuilder();
        }
        
        /**
         * The url string defining this request. Note that it does not contain the base URL pointing to the Game Jolt
         * API.
         */
        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder();
            builder.append("/data-store/?game_id=").append(gameID);
            builder.append("&key=").append(urlEncode(key));
            if (username != null) builder.append("&username=").append(urlEncode(username));
            if (userToken != null) builder.append("&user_token=").append(urlEncode(userToken));
            
            return builder.toString();
        }
        
        /**
         * Handles the server JSON response and returns a corresponding {@link DataStoreFetchValue}.
         *
         * @param jsonValue The JSON response from the server.
         * @return The {@link DataStoreFetchValue} with the values returned from the server.
         */
        @Override
        public DataStoreFetchValue handleResponse(JsonValue jsonValue) {
            return DataStoreFetchValue.builder()
                    .jsonValue(jsonValue)
                    .request(this)
                    .success(jsonValue.getBoolean("success"))
                    .message(jsonValue.getString("message", null))
                    .data(jsonValue.getString("data"))
                    .build();
        }
        
        public @NonNull String getGameID() {
            return this.gameID;
        }
        
        public @NonNull String getKey() {
            return this.key;
        }
        
        public String getUsername() {
            return this.username;
        }
        
        public String getUserToken() {
            return this.userToken;
        }
        
        public void setGameID(@NonNull String gameID) {
            this.gameID = gameID;
        }
        
        public void setKey(@NonNull String key) {
            this.key = key;
        }
        
        public void setUsername(String username) {
            this.username = username;
        }
        
        public void setUserToken(String userToken) {
            this.userToken = userToken;
        }
        
        public static class DataStoreFetchRequestBuilder {
            private @NonNull String gameID;
            private @NonNull String key;
            private String username;
            private String userToken;
            
            DataStoreFetchRequestBuilder() {
            }
            
            public DataStoreFetchRequestBuilder gameID(@NonNull String gameID) {
                this.gameID = gameID;
                return this;
            }
            
            public DataStoreFetchRequestBuilder key(@NonNull String key) {
                this.key = key;
                return this;
            }
            
            public DataStoreFetchRequestBuilder username(String username) {
                this.username = username;
                return this;
            }
            
            public DataStoreFetchRequestBuilder userToken(String userToken) {
                this.userToken = userToken;
                return this;
            }
            
            public DataStoreFetchRequest build() {
                return new DataStoreFetchRequest(this.gameID, this.key, this.username, this.userToken);
            }
            
            public String toString() {
                return "GameJoltDataStore.DataStoreFetchRequest.DataStoreFetchRequestBuilder(gameID=" + this.gameID + ", key=" + this.key + ", username=" + this.username + ", userToken=" + this.userToken + ")";
            }
        }
    }
    
    /**
     * The returned data from the data store
     */
    public static class DataStoreFetchValue implements GameJoltValue {
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
         * If the request was successful, this contains the item's data.
         */
        public String data;
        
        DataStoreFetchValue(JsonValue jsonValue, GameJoltRequest request, boolean success, String message,
                            String data) {
            this.jsonValue = jsonValue;
            this.request = request;
            this.success = success;
            this.message = message;
            this.data = data;
        }
        
        public static DataStoreFetchValueBuilder builder() {
            return new DataStoreFetchValueBuilder();
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
        
        public String getData() {
            return this.data;
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
        
        public void setData(String data) {
            this.data = data;
        }
        
        public static class DataStoreFetchValueBuilder {
            private JsonValue jsonValue;
            private GameJoltRequest request;
            private boolean success;
            private String message;
            private String data;
            
            DataStoreFetchValueBuilder() {
            }
            
            public DataStoreFetchValueBuilder jsonValue(JsonValue jsonValue) {
                this.jsonValue = jsonValue;
                return this;
            }
            
            public DataStoreFetchValueBuilder request(GameJoltRequest request) {
                this.request = request;
                return this;
            }
            
            public DataStoreFetchValueBuilder success(boolean success) {
                this.success = success;
                return this;
            }
            
            public DataStoreFetchValueBuilder message(String message) {
                this.message = message;
                return this;
            }
            
            public DataStoreFetchValueBuilder data(String data) {
                this.data = data;
                return this;
            }
            
            public DataStoreFetchValue build() {
                return new DataStoreFetchValue(this.jsonValue, this.request, this.success, this.message, this.data);
            }
            
            public String toString() {
                return "GameJoltDataStore.DataStoreFetchValue.DataStoreFetchValueBuilder(jsonValue=" + this.jsonValue + ", request=" + this.request + ", success=" + this.success + ", message=" + this.message + ", data=" + this.data + ")";
            }
        }
    }
    
    /**
     * Listener for {@link DataStoreFetchRequest}. Override
     * {@link DataStoreFetchListener#dataStoreFetch(DataStoreFetchValue)} (DataStoreFetchValue)} to handle the server
     * response.
     */
    public static abstract class DataStoreFetchListener extends GameJoltAdapter {
        @Override
        public void response(GameJoltRequest request, GameJoltValue value) {
            if (value instanceof DataStoreFetchValue) dataStoreFetch((DataStoreFetchValue) value);
        }
        
        public abstract void dataStoreFetch(DataStoreFetchValue value);
    }
    
    /**
     * Returns either all the keys in the game's global data store, or all the keys in a user's data store. If you pass
     * in the user information, this function will return all the keys in a user's data store. If you leave the user
     * information empty, it will return all the keys in the game's global data store.
     * <p>
     * This request will return a list of the key values. The key return value can appear more than once.
     */
    public static class DataStoreGetKeysRequest implements GameJoltRequest {
        /**
         * The ID of your game. Required.
         */
        @NonNull
        private String gameID;
        
        /**
         * The pattern to apply to the key names in the data store. Optional.
         */
        private String pattern;
        
        /**
         * The user's username. Optional.
         */
        private String username;
        
        /**
         * The user's token. Optional.
         */
        private String userToken;
        
        DataStoreGetKeysRequest(@NonNull String gameID, String pattern, String username, String userToken) {
            this.gameID = gameID;
            this.pattern = pattern;
            this.username = username;
            this.userToken = userToken;
        }
        
        public static DataStoreGetKeysRequestBuilder builder() {
            return new DataStoreGetKeysRequestBuilder();
        }
        
        /**
         * The url string defining this request. Note that it does not contain the base URL pointing to the Game Jolt
         * API. Optional.
         */
        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder();
            builder.append("/data-store/get-keys/?game_id=").append(gameID);
            if (pattern != null) builder.append("&pattern=").append(urlEncode(pattern));
            if (username != null) builder.append("&username=").append(urlEncode(username));
            if (userToken != null) builder.append("&user_token=").append(urlEncode(userToken));
            
            return builder.toString();
        }
        
        /**
         * Handles the server JSON response and returns a corresponding {@link DataStoreGetKeysValue}.
         *
         * @param jsonValue The JSON response from the server.
         * @return The {@link DataStoreGetKeysValue} with the values returned from the server.
         */
        @Override
        public DataStoreGetKeysValue handleResponse(JsonValue jsonValue) {
            Array<String> keys = new Array<String>();
            JsonValue keysJsonValue = jsonValue.get("keys");
            for (JsonValue keyJsonValue : keysJsonValue) {
                keys.add(keyJsonValue.getString("key"));
            }
            return DataStoreGetKeysValue.builder()
                    .jsonValue(jsonValue)
                    .request(this)
                    .success(jsonValue.getBoolean("success"))
                    .message(jsonValue.getString("message", null))
                    .keys(keys)
                    .build();
        }
        
        public @NonNull String getGameID() {
            return this.gameID;
        }
        
        public String getPattern() {
            return this.pattern;
        }
        
        public String getUsername() {
            return this.username;
        }
        
        public String getUserToken() {
            return this.userToken;
        }
        
        public void setGameID(@NonNull String gameID) {
            this.gameID = gameID;
        }
        
        public void setPattern(String pattern) {
            this.pattern = pattern;
        }
        
        public void setUsername(String username) {
            this.username = username;
        }
        
        public void setUserToken(String userToken) {
            this.userToken = userToken;
        }
        
        public static class DataStoreGetKeysRequestBuilder {
            private @NonNull String gameID;
            private String pattern;
            private String username;
            private String userToken;
            
            DataStoreGetKeysRequestBuilder() {
            }
            
            public DataStoreGetKeysRequestBuilder gameID(@NonNull String gameID) {
                this.gameID = gameID;
                return this;
            }
            
            public DataStoreGetKeysRequestBuilder pattern(String pattern) {
                this.pattern = pattern;
                return this;
            }
            
            public DataStoreGetKeysRequestBuilder username(String username) {
                this.username = username;
                return this;
            }
            
            public DataStoreGetKeysRequestBuilder userToken(String userToken) {
                this.userToken = userToken;
                return this;
            }
            
            public DataStoreGetKeysRequest build() {
                return new DataStoreGetKeysRequest(this.gameID, this.pattern, this.username, this.userToken);
            }
            
            public String toString() {
                return "GameJoltDataStore.DataStoreGetKeysRequest.DataStoreGetKeysRequestBuilder(gameID=" + this.gameID + ", pattern=" + this.pattern + ", username=" + this.username + ", userToken=" + this.userToken + ")";
            }
        }
    }
    
    /**
     * The returned keys from the data store.
     */
    public static class DataStoreGetKeysValue implements GameJoltValue {
        
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
         * The name of the key. This function will return all the keys for this particular data store.
         */
        public Array<String> keys;
        
        DataStoreGetKeysValue(JsonValue jsonValue, GameJoltRequest request, boolean success, String message,
                              Array<String> keys) {
            this.jsonValue = jsonValue;
            this.request = request;
            this.success = success;
            this.message = message;
            this.keys = keys;
        }
        
        public static DataStoreGetKeysValueBuilder builder() {
            return new DataStoreGetKeysValueBuilder();
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
        
        public Array<String> getKeys() {
            return this.keys;
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
        
        public void setKeys(Array<String> keys) {
            this.keys = keys;
        }
        
        public static class DataStoreGetKeysValueBuilder {
            private JsonValue jsonValue;
            private GameJoltRequest request;
            private boolean success;
            private String message;
            private Array<String> keys;
            
            DataStoreGetKeysValueBuilder() {
            }
            
            public DataStoreGetKeysValueBuilder jsonValue(JsonValue jsonValue) {
                this.jsonValue = jsonValue;
                return this;
            }
            
            public DataStoreGetKeysValueBuilder request(GameJoltRequest request) {
                this.request = request;
                return this;
            }
            
            public DataStoreGetKeysValueBuilder success(boolean success) {
                this.success = success;
                return this;
            }
            
            public DataStoreGetKeysValueBuilder message(String message) {
                this.message = message;
                return this;
            }
            
            public DataStoreGetKeysValueBuilder keys(Array<String> keys) {
                this.keys = keys;
                return this;
            }
            
            public DataStoreGetKeysValue build() {
                return new DataStoreGetKeysValue(this.jsonValue, this.request, this.success, this.message, this.keys);
            }
            
            public String toString() {
                return "GameJoltDataStore.DataStoreGetKeysValue.DataStoreGetKeysValueBuilder(jsonValue=" + this.jsonValue + ", request=" + this.request + ", success=" + this.success + ", message=" + this.message + ", keys=" + this.keys + ")";
            }
        }
    }
    
    
    /**
     * Listener for {@link DataStoreGetKeysRequest}. Override
     * {@link DataStoreGetKeysListener#dataStoreGetKeys(DataStoreGetKeysValue)} (DataStoreGetKeysValue)} to handle the
     * server response.
     */
    public static abstract class DataStoreGetKeysListener extends GameJoltAdapter {
        @Override
        public void response(GameJoltRequest request, GameJoltValue value) {
            if (value instanceof DataStoreGetKeysValue) dataStoreGetKeys((DataStoreGetKeysValue) value);
        }
        
        public abstract void dataStoreGetKeys(DataStoreGetKeysValue value);
    }
    
    /**
     * Removes data from the data store. If you pass in the user information, the item will be removed from a user's
     * data store. If you leave the user information empty, it will be removed from the game's global data store.
     */
    public static class DataStoreRemoveRequest implements GameJoltRequest {
        
        /**
         * The ID of your game. Required.
         */
        @NonNull
        private String gameID;
        
        /**
         * The key of the data item you'd like to remove. Required.
         */
        @NonNull
        private String key;
        
        /**
         * The user's username. Optional.
         */
        private String username;
        
        /**
         * The user's token. Optional.
         */
        private String userToken;
        
        DataStoreRemoveRequest(@NonNull String gameID, @NonNull String key, String username, String userToken) {
            this.gameID = gameID;
            this.key = key;
            this.username = username;
            this.userToken = userToken;
        }
        
        public static DataStoreRemoveRequestBuilder builder() {
            return new DataStoreRemoveRequestBuilder();
        }
        
        /**
         * The url string defining this request. Note that it does not contain the base URL pointing to the Game Jolt
         * API.
         */
        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder();
            builder.append("/data-store/remove/?game_id=").append(gameID);
            builder.append("&key=").append(urlEncode(key));
            if (username != null) builder.append("&username=").append(urlEncode(username));
            if (userToken != null) builder.append("&user_token=").append(urlEncode(userToken));
            
            return builder.toString();
        }
        
        /**
         * Handles the server JSON response and returns a corresponding {@link DataStoreRemoveValue}.
         *
         * @param jsonValue The JSON response from the server.
         * @return The {@link DataStoreRemoveValue} with the values returned from the server.
         */
        @Override
        public DataStoreRemoveValue handleResponse(JsonValue jsonValue) {
            return DataStoreRemoveValue.builder()
                    .jsonValue(jsonValue)
                    .request(this)
                    .success(jsonValue.getBoolean("success"))
                    .message(jsonValue.getString("message", null))
                    .build();
        }
        
        public @NonNull String getGameID() {
            return this.gameID;
        }
        
        public @NonNull String getKey() {
            return this.key;
        }
        
        public String getUsername() {
            return this.username;
        }
        
        public String getUserToken() {
            return this.userToken;
        }
        
        public void setGameID(@NonNull String gameID) {
            this.gameID = gameID;
        }
        
        public void setKey(@NonNull String key) {
            this.key = key;
        }
        
        public void setUsername(String username) {
            this.username = username;
        }
        
        public void setUserToken(String userToken) {
            this.userToken = userToken;
        }
        
        public static class DataStoreRemoveRequestBuilder {
            private @NonNull String gameID;
            private @NonNull String key;
            private String username;
            private String userToken;
            
            DataStoreRemoveRequestBuilder() {
            }
            
            public DataStoreRemoveRequestBuilder gameID(@NonNull String gameID) {
                this.gameID = gameID;
                return this;
            }
            
            public DataStoreRemoveRequestBuilder key(@NonNull String key) {
                this.key = key;
                return this;
            }
            
            public DataStoreRemoveRequestBuilder username(String username) {
                this.username = username;
                return this;
            }
            
            public DataStoreRemoveRequestBuilder userToken(String userToken) {
                this.userToken = userToken;
                return this;
            }
            
            public DataStoreRemoveRequest build() {
                return new DataStoreRemoveRequest(this.gameID, this.key, this.username, this.userToken);
            }
            
            public String toString() {
                return "GameJoltDataStore.DataStoreRemoveRequest.DataStoreRemoveRequestBuilder(gameID=" + this.gameID + ", key=" + this.key + ", username=" + this.username + ", userToken=" + this.userToken + ")";
            }
        }
    }
    
    /**
     * The result of removing data from the data store.
     */
    public static class DataStoreRemoveValue implements GameJoltValue {
        
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
        
        DataStoreRemoveValue(JsonValue jsonValue, GameJoltRequest request, boolean success, String message) {
            this.jsonValue = jsonValue;
            this.request = request;
            this.success = success;
            this.message = message;
        }
        
        public static DataStoreRemoveValueBuilder builder() {
            return new DataStoreRemoveValueBuilder();
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
        
        public static class DataStoreRemoveValueBuilder {
            private JsonValue jsonValue;
            private GameJoltRequest request;
            private boolean success;
            private String message;
            
            DataStoreRemoveValueBuilder() {
            }
            
            public DataStoreRemoveValueBuilder jsonValue(JsonValue jsonValue) {
                this.jsonValue = jsonValue;
                return this;
            }
            
            public DataStoreRemoveValueBuilder request(GameJoltRequest request) {
                this.request = request;
                return this;
            }
            
            public DataStoreRemoveValueBuilder success(boolean success) {
                this.success = success;
                return this;
            }
            
            public DataStoreRemoveValueBuilder message(String message) {
                this.message = message;
                return this;
            }
            
            public DataStoreRemoveValue build() {
                return new DataStoreRemoveValue(this.jsonValue, this.request, this.success, this.message);
            }
            
            public String toString() {
                return "GameJoltDataStore.DataStoreRemoveValue.DataStoreRemoveValueBuilder(jsonValue=" + this.jsonValue + ", request=" + this.request + ", success=" + this.success + ", message=" + this.message + ")";
            }
        }
    }
    
    /**
     * Listener for {@link DataStoreRemoveRequest}. Override
     * {@link DataStoreRemoveListener#dataStoreRemove(DataStoreRemoveValue)} to handle the server response.
     */
    public static abstract class DataStoreRemoveListener extends GameJoltAdapter {
        @Override
        public void response(GameJoltRequest request, GameJoltValue value) {
            if (value instanceof DataStoreRemoveValue) dataStoreRemove((DataStoreRemoveValue) value);
        }
        
        public abstract void dataStoreRemove(DataStoreRemoveValue value);
    }
    
    /**
     * Sets data in the data store. You can create a new data store item by passing in a key that doesn't exist yet. If
     * you pass in the user information, the item will be added to a user's data store. If you leave the user
     * information empty, it will be added to the game's global data store.
     */
    public static class DataStoreSetRequest implements GameJoltRequest {
        
        /**
         * The ID of your game. Required.
         */
        @NonNull
        private String gameID;
        
        /**
         * The key of the data item you'd like to set. Required.
         */
        @NonNull
        private String key;
        
        /**
         * The data you'd like to set. Required.
         */
        @NonNull
        private String data;
        
        /**
         * The user's username. Optional.
         */
        private String username;
        
        /**
         * The user's token. Optional.
         */
        private String userToken;
        
        DataStoreSetRequest(@NonNull String gameID, @NonNull String key, @NonNull String data, String username,
                            String userToken) {
            this.gameID = gameID;
            this.key = key;
            this.data = data;
            this.username = username;
            this.userToken = userToken;
        }
        
        public static DataStoreSetRequestBuilder builder() {
            return new DataStoreSetRequestBuilder();
        }
        
        /**
         * The url string defining this request. Note that it does not contain the base URL pointing to the Game Jolt
         * API.
         */
        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder();
            builder.append("/data-store/set/?game_id=").append(gameID);
            builder.append("&key=").append(urlEncode(key));
            builder.append("&data=").append(urlEncode(data));
            if (username != null) builder.append("&username=").append(urlEncode(username));
            if (userToken != null) builder.append("&user_token=").append(urlEncode(userToken));
            
            return builder.toString();
        }
        
        /**
         * Handles the server JSON response and returns a corresponding {@link DataStoreSetValue}.
         *
         * @param jsonValue The JSON response from the server.
         * @return The {@link DataStoreSetValue} with the values returned from the server.
         */
        @Override
        public DataStoreSetValue handleResponse(JsonValue jsonValue) {
            return DataStoreSetValue.builder()
                    .jsonValue(jsonValue)
                    .request(this)
                    .success(jsonValue.getBoolean("success"))
                    .message(jsonValue.getString("message", null))
                    .build();
        }
        
        public @NonNull String getGameID() {
            return this.gameID;
        }
        
        public @NonNull String getKey() {
            return this.key;
        }
        
        public @NonNull String getData() {
            return this.data;
        }
        
        public String getUsername() {
            return this.username;
        }
        
        public String getUserToken() {
            return this.userToken;
        }
        
        public void setGameID(@NonNull String gameID) {
            this.gameID = gameID;
        }
        
        public void setKey(@NonNull String key) {
            this.key = key;
        }
        
        public void setData(@NonNull String data) {
            this.data = data;
        }
        
        public void setUsername(String username) {
            this.username = username;
        }
        
        public void setUserToken(String userToken) {
            this.userToken = userToken;
        }
        
        public static class DataStoreSetRequestBuilder {
            private @NonNull String gameID;
            private @NonNull String key;
            private @NonNull String data;
            private String username;
            private String userToken;
            
            DataStoreSetRequestBuilder() {
            }
            
            public DataStoreSetRequestBuilder gameID(@NonNull String gameID) {
                this.gameID = gameID;
                return this;
            }
            
            public DataStoreSetRequestBuilder key(@NonNull String key) {
                this.key = key;
                return this;
            }
            
            public DataStoreSetRequestBuilder data(@NonNull String data) {
                this.data = data;
                return this;
            }
            
            public DataStoreSetRequestBuilder username(String username) {
                this.username = username;
                return this;
            }
            
            public DataStoreSetRequestBuilder userToken(String userToken) {
                this.userToken = userToken;
                return this;
            }
            
            public DataStoreSetRequest build() {
                return new DataStoreSetRequest(this.gameID, this.key, this.data, this.username, this.userToken);
            }
            
            public String toString() {
                return "GameJoltDataStore.DataStoreSetRequest.DataStoreSetRequestBuilder(gameID=" + this.gameID + ", key=" + this.key + ", data=" + this.data + ", username=" + this.username + ", userToken=" + this.userToken + ")";
            }
        }
    }
    
    /**
     * The result of setting data in the data store.
     */
    public static class DataStoreSetValue implements GameJoltValue {
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
        
        DataStoreSetValue(JsonValue jsonValue, GameJoltRequest request, boolean success, String message) {
            this.jsonValue = jsonValue;
            this.request = request;
            this.success = success;
            this.message = message;
        }
        
        public static DataStoreSetValueBuilder builder() {
            return new DataStoreSetValueBuilder();
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
        
        public static class DataStoreSetValueBuilder {
            private JsonValue jsonValue;
            private GameJoltRequest request;
            private boolean success;
            private String message;
            
            DataStoreSetValueBuilder() {
            }
            
            public DataStoreSetValueBuilder jsonValue(JsonValue jsonValue) {
                this.jsonValue = jsonValue;
                return this;
            }
            
            public DataStoreSetValueBuilder request(GameJoltRequest request) {
                this.request = request;
                return this;
            }
            
            public DataStoreSetValueBuilder success(boolean success) {
                this.success = success;
                return this;
            }
            
            public DataStoreSetValueBuilder message(String message) {
                this.message = message;
                return this;
            }
            
            public DataStoreSetValue build() {
                return new DataStoreSetValue(this.jsonValue, this.request, this.success, this.message);
            }
            
            public String toString() {
                return "GameJoltDataStore.DataStoreSetValue.DataStoreSetValueBuilder(jsonValue=" + this.jsonValue + ", request=" + this.request + ", success=" + this.success + ", message=" + this.message + ")";
            }
        }
    }
    
    /**
     * Listener for {@link DataStoreSetRequest}. Override {@link DataStoreSetListener#dataStoreSet(DataStoreSetValue)}
     * to handle the server response.
     */
    public static abstract class DataStoreSetListener extends GameJoltAdapter {
        @Override
        public void response(GameJoltRequest request, GameJoltValue value) {
            if (value instanceof DataStoreSetValue) dataStoreSet((DataStoreSetValue) value);
        }
        
        public abstract void dataStoreSet(DataStoreSetValue value);
    }
    
    /**
     * Updates data in the data store. You can only perform mathematical operations on numerical data. See
     * {@link DataStoreUpdateRequest#setValue(String)}. If you pass in the user information, this function will return
     * all the keys in a user's data store. If you leave the user information empty, it will return all the keys in the
     * game's global data store.
     */
    public static class DataStoreUpdateRequest implements GameJoltRequest {
        
        /**
         * The ID of your game. Required.
         */
        @NonNull
        private String gameID;
        
        /**
         * The key of the data item you'd like to update. Required.
         */
        @NonNull
        private String key;
        
        /**
         * The user's username. Optional.
         */
        private String username;
        
        /**
         * The user's token. Optional.
         */
        private String userToken;
        
        /**
         * The operation you'd like to perform. Required.
         */
        @NonNull
        private OperationType operation;
        
        /**
         * The String value you'd like to apply to the data store item. You may only use the operations
         * {@link OperationType#ADD}, {@link OperationType#SUBTRACT}, {@link OperationType#MULTIPLY},
         * {@link OperationType#DIVIDE} if you submit a numerical value.
         */
        private String value;
        
        DataStoreUpdateRequest(@NonNull String gameID, @NonNull String key, String username, String userToken,
                               @NonNull GameJoltDataStore.OperationType operation, String value) {
            this.gameID = gameID;
            this.key = key;
            this.username = username;
            this.userToken = userToken;
            this.operation = operation;
            this.value = value;
        }
        
        public static DataStoreUpdateRequestBuilder builder() {
            return new DataStoreUpdateRequestBuilder();
        }
        
        /**
         * The url string defining this request. Note that it does not contain the base URL pointing to the Game Jolt
         * API.
         */
        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder();
            builder.append("/data-store/update/?game_id=").append(gameID);
            builder.append("&key=").append(urlEncode(key));
            if (username != null) builder.append("&username=").append(urlEncode(username));
            if (userToken != null) builder.append("&user_token=").append(urlEncode(userToken));
            builder.append("&operation=").append(urlEncode(operation.name));
            builder.append("&value=").append(urlEncode(value));
            
            return builder.toString();
        }
        
        /**
         * Handles the server JSON response and returns a corresponding {@link DataStoreUpdateValue}.
         *
         * @param jsonValue The JSON response from the server.
         * @return The {@link DataStoreUpdateValue} with the values returned from the server.
         */
        @Override
        public DataStoreUpdateValue handleResponse(JsonValue jsonValue) {
            return DataStoreUpdateValue.builder()
                    .jsonValue(jsonValue)
                    .request(this)
                    .success(jsonValue.getBoolean("success"))
                    .message(jsonValue.getString("message", null))
                    .data(jsonValue.getString("data", null))
                    .build();
        }
        
        public @NonNull String getGameID() {
            return this.gameID;
        }
        
        public @NonNull String getKey() {
            return this.key;
        }
        
        public String getUsername() {
            return this.username;
        }
        
        public String getUserToken() {
            return this.userToken;
        }
        
        public @NonNull OperationType getOperation() {
            return this.operation;
        }
        
        public String getValue() {
            return this.value;
        }
        
        public void setGameID(@NonNull String gameID) {
            this.gameID = gameID;
        }
        
        public void setKey(@NonNull String key) {
            this.key = key;
        }
        
        public void setUsername(String username) {
            this.username = username;
        }
        
        public void setUserToken(String userToken) {
            this.userToken = userToken;
        }
        
        public void setOperation(@NonNull GameJoltDataStore.OperationType operation) {
            this.operation = operation;
        }
        
        public void setValue(String value) {
            this.value = value;
        }
        
        public static class DataStoreUpdateRequestBuilder {
            private @NonNull String gameID;
            private @NonNull String key;
            private String username;
            private String userToken;
            private @NonNull GameJoltDataStore.OperationType operation;
            private String value;
            
            DataStoreUpdateRequestBuilder() {
            }
            
            public DataStoreUpdateRequestBuilder gameID(@NonNull String gameID) {
                this.gameID = gameID;
                return this;
            }
            
            public DataStoreUpdateRequestBuilder key(@NonNull String key) {
                this.key = key;
                return this;
            }
            
            public DataStoreUpdateRequestBuilder username(String username) {
                this.username = username;
                return this;
            }
            
            public DataStoreUpdateRequestBuilder userToken(String userToken) {
                this.userToken = userToken;
                return this;
            }
            
            public DataStoreUpdateRequestBuilder operation(@NonNull GameJoltDataStore.OperationType operation) {
                this.operation = operation;
                return this;
            }
            
            public DataStoreUpdateRequestBuilder value(String value) {
                this.value = value;
                return this;
            }
            
            public DataStoreUpdateRequest build() {
                return new DataStoreUpdateRequest(this.gameID, this.key, this.username, this.userToken, this.operation,
                        this.value);
            }
            
            public String toString() {
                return "GameJoltDataStore.DataStoreUpdateRequest.DataStoreUpdateRequestBuilder(gameID=" + this.gameID + ", key=" + this.key + ", username=" + this.username + ", userToken=" + this.userToken + ", operation=" + this.operation + ", value=" + this.value + ")";
            }
        }
    }
    
    /**
     * The valid operations compatible with {@link DataStoreUpdateRequest#setOperation(OperationType)}.
     */
    public enum OperationType {
        /**
         * Adds the value to the current data store item.
         */
        ADD("add"),
        
        /**
         * Substracts the value from the current data store item.
         */
        SUBTRACT("subtract"),
        
        /**
         * Multiplies the value by the current data store item.
         */
        MULTIPLY("multiply"),
        
        /**
         * Divides the current data store item by the value.
         */
        DIVIDE("divide"),
        
        /**
         * Appends the value to the current data store item.
         */
        APPEND("append"),
        
        /**
         * Prepends the value to the current data store item.
         */
        PREPEND("prepend");
        
        private final String name;
        
        OperationType(String name) {
            this.name = name;
        }
    }
    
    /**
     * The result of updating data in the data store.
     */
    public static class DataStoreUpdateValue implements GameJoltValue {
        
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
         * If the request was successful, this returns the new value of the data item.
         */
        public String data;
        
        DataStoreUpdateValue(JsonValue jsonValue, GameJoltRequest request, boolean success, String message,
                             String data) {
            this.jsonValue = jsonValue;
            this.request = request;
            this.success = success;
            this.message = message;
            this.data = data;
        }
        
        public static DataStoreUpdateValueBuilder builder() {
            return new DataStoreUpdateValueBuilder();
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
        
        public String getData() {
            return this.data;
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
        
        public void setData(String data) {
            this.data = data;
        }
        
        public static class DataStoreUpdateValueBuilder {
            private JsonValue jsonValue;
            private GameJoltRequest request;
            private boolean success;
            private String message;
            private String data;
            
            DataStoreUpdateValueBuilder() {
            }
            
            public DataStoreUpdateValueBuilder jsonValue(JsonValue jsonValue) {
                this.jsonValue = jsonValue;
                return this;
            }
            
            public DataStoreUpdateValueBuilder request(GameJoltRequest request) {
                this.request = request;
                return this;
            }
            
            public DataStoreUpdateValueBuilder success(boolean success) {
                this.success = success;
                return this;
            }
            
            public DataStoreUpdateValueBuilder message(String message) {
                this.message = message;
                return this;
            }
            
            public DataStoreUpdateValueBuilder data(String data) {
                this.data = data;
                return this;
            }
            
            public DataStoreUpdateValue build() {
                return new DataStoreUpdateValue(this.jsonValue, this.request, this.success, this.message, this.data);
            }
            
            public String toString() {
                return "GameJoltDataStore.DataStoreUpdateValue.DataStoreUpdateValueBuilder(jsonValue=" + this.jsonValue + ", request=" + this.request + ", success=" + this.success + ", message=" + this.message + ", data=" + this.data + ")";
            }
        }
    }
    
    /**
     * Listener for {@link DataStoreUpdateRequest}. Override
     * {@link DataStoreUpdateListener#dataStoreUpdate(DataStoreUpdateValue)} to handle the server response.
     */
    public static abstract class DataStoreUpdateListener extends GameJoltAdapter {
        @Override
        public void response(GameJoltRequest request, GameJoltValue value) {
            if (value instanceof DataStoreUpdateValue) dataStoreUpdate((DataStoreUpdateValue) value);
        }
        
        public abstract void dataStoreUpdate(DataStoreUpdateValue value);
    }
}
