package com.github.raeleus.gamejoltapi;

import com.badlogic.gdx.utils.JsonValue;
import lombok.*;

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
    @Builder
    @Getter
    @Setter
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

        /**
         * The url string defining this request. Note that it does not contain the base URL pointing to the Game Jolt
         * API.
         */
        @Override
        public String toString() {
            var builder = new StringBuilder();
            builder.append("/data-store/?game_id=").append(gameID);
            builder.append("&key=").append(urlEncode(key));
            if (username != null) builder.append("&username=").append(urlEncode(username));
            if (userToken != null) builder.append("&user_token=").append(urlEncode(userToken));

            return builder.toString();
        }

        /**
         * Handles the server JSON response and returns a corresponding {@link DataStoreFetchValue}.
         * @param jsonValue The JSON response from the server.
         * @return The {@link DataStoreFetchValue} with the values returned from the server.
         */
        @Override
        public DataStoreFetchValue handleResponse(JsonValue jsonValue) {
            return DataStoreFetchValue.builder()
                .jsonValue(jsonValue)
                .success(jsonValue.getBoolean("success"))
                .message(jsonValue.getString("message", null))
                .data(jsonValue.getString("data"))
                .build();
        }
    }

    /**
     * The returned data from the data store
     */
    @Builder
    @Getter
    @Setter
    public static class DataStoreFetchValue implements GameJoltValue {
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
         * If the request was successful, this contains the item's data.
         */
        public String data;
    }

    /**
     * Listener for {@link DataStoreFetchRequest}. Override {@link
     * DataStoreFetchListener#dataStoreFetch(DataStoreFetchValue)} (DataStoreFetchValue)} to handle the server response.
     */
    public static abstract class DataStoreFetchListener extends GameJoltAdapter {
        @Override
        public void response(GameJoltValue value) {
            if (value instanceof DataStoreFetchValue) dataStoreFetch((DataStoreFetchValue) value);
        }

        public abstract void dataStoreFetch(DataStoreFetchValue value);
    }

    /**
     * Returns either all the keys in the game's global data store, or all the keys in a user's data store.
     * If you pass in the user information, this function will return all the keys in a user's data store. If you leave
     * the user information empty, it will return all the keys in the game's global data store.
     * <p>
     * This request will return a list of the key values. The key return value can appear more than once.
     */
    @Builder
    @Getter
    @Setter
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

        /**
         * The url string defining this request. Note that it does not contain the base URL pointing to the Game Jolt
         * API. Optional.
         */
        @Override
        public String toString() {
            var builder = new StringBuilder();
            builder.append("/data-store/get-keys/?game_id=").append(gameID);
            if (pattern != null) builder.append("&pattern=").append(urlEncode(pattern));
            if (username != null) builder.append("&username=").append(urlEncode(username));
            if (userToken != null) builder.append("&user_token=").append(urlEncode(userToken));

            return builder.toString();
        }

        /**
         * Handles the server JSON response and returns a corresponding {@link DataStoreGetKeysValue}.
         * @param jsonValue The JSON response from the server.
         * @return The {@link DataStoreGetKeysValue} with the values returned from the server.
         */
        @Override
        public DataStoreGetKeysValue handleResponse(JsonValue jsonValue) {
            return DataStoreGetKeysValue.builder()
                .jsonValue(jsonValue)
                .success(jsonValue.getBoolean("success"))
                .message(jsonValue.getString("message", null))
                .key(jsonValue.getString("key"))
                .build();
        }
    }

    /**
     * The returned keys from the data store.
     */
    @Builder
    @Getter
    @Setter
    public static class DataStoreGetKeysValue implements GameJoltValue {

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
         * The name of the key. This function will return all the keys for this particular data store.
         */
        public String key;
    }


    /**
     * Listener for {@link DataStoreGetKeysRequest}. Override {@link
     * DataStoreGetKeysListener#dataStoreGetKeys(DataStoreGetKeysValue)} (DataStoreGetKeysValue)} to handle the server
     * response.
     */
    public static abstract class DataStoreGetKeysListener extends GameJoltAdapter {
        @Override
        public void response(GameJoltValue value) {
            if (value instanceof DataStoreGetKeysValue) dataStoreGetKeys((DataStoreGetKeysValue) value);
        }

        public abstract void dataStoreGetKeys(DataStoreGetKeysValue value);
    }

    /**
     * Removes data from the data store. If you pass in the user information, the item will be removed from a user's
     * data store. If you leave the user information empty, it will be removed from the game's global data store.
     */
    @Builder
    @Getter
    @Setter
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

        /**
         * The url string defining this request. Note that it does not contain the base URL pointing to the Game Jolt
         * API.
         */
        @Override
        public String toString() {
            var builder = new StringBuilder();
            builder.append("/data-store/remove/?game_id=").append(gameID);
            builder.append("&key=").append(urlEncode(key));
            if (username != null) builder.append("&username=").append(urlEncode(username));
            if (userToken != null) builder.append("&user_token=").append(urlEncode(userToken));

            return builder.toString();
        }

        /**
         * Handles the server JSON response and returns a corresponding {@link DataStoreRemoveValue}.
         * @param jsonValue The JSON response from the server.
         * @return The {@link DataStoreRemoveValue} with the values returned from the server.
         */
        @Override
        public DataStoreRemoveValue handleResponse(JsonValue jsonValue) {
            return DataStoreRemoveValue.builder()
                .jsonValue(jsonValue)
                .success(jsonValue.getBoolean("success"))
                .message(jsonValue.getString("message", null))
                .build();
        }
    }

    /**
     * The result of removing data from the data store.
     */
    @Builder
    @Getter
    @Setter
    public static class DataStoreRemoveValue implements GameJoltValue {

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
    }

    /**
     * Listener for {@link DataStoreRemoveRequest}. Override {@link DataStoreRemoveListener#dataStoreRemove(DataStoreRemoveValue)} to
     * handle the server response.
     */
    public static abstract class DataStoreRemoveListener extends GameJoltAdapter {
        @Override
        public void response(GameJoltValue value) {
            if (value instanceof DataStoreRemoveValue) dataStoreRemove((DataStoreRemoveValue) value);
        }

        public abstract void dataStoreRemove(DataStoreRemoveValue value);
    }

    /**
     * Sets data in the data store. You can create a new data store item by passing in a key that doesn't exist yet. If
     * you pass in the user information, the item will be added to a user's data store. If you leave the user
     * information empty, it will be added to the game's global data store.
     */
    @Builder
    @Getter
    @Setter
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

        /**
         * The url string defining this request. Note that it does not contain the base URL pointing to the Game Jolt
         * API.
         */
        @Override
        public String toString() {
            var builder = new StringBuilder();
            builder.append("/data-store/set/?game_id=").append(gameID);
            builder.append("&key=").append(urlEncode(key));
            builder.append("&data=").append(urlEncode(data));
            if (username != null) builder.append("&username=").append(urlEncode(username));
            if (userToken != null) builder.append("&user_token=").append(urlEncode(userToken));

            return builder.toString();
        }

        /**
         * Handles the server JSON response and returns a corresponding {@link DataStoreSetValue}.
         * @param jsonValue The JSON response from the server.
         * @return The {@link DataStoreSetValue} with the values returned from the server.
         */
        @Override
        public DataStoreSetValue handleResponse(JsonValue jsonValue) {
            return DataStoreSetValue.builder()
                .jsonValue(jsonValue)
                .success(jsonValue.getBoolean("success"))
                .message(jsonValue.getString("message", null))
                .build();
        }
    }

    /**
     * The result of setting data in the data store.
     */
    @Builder
    @Getter
    @Setter
    public static class DataStoreSetValue implements GameJoltValue {
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
    }

    /**
     * Listener for {@link DataStoreSetRequest}. Override {@link DataStoreSetListener#dataStoreSet(DataStoreSetValue)} to
     * handle the server response.
     */
    public static abstract class DataStoreSetListener extends GameJoltAdapter {
        @Override
        public void response(GameJoltValue value) {
            if (value instanceof DataStoreSetValue) dataStoreSet((DataStoreSetValue) value);
        }

        public abstract void dataStoreSet(DataStoreSetValue value);
    }

    /**
     * Updates data in the data store. You can only perform mathematical operations on numerical data. See {@link
     * DataStoreUpdateRequest#setValue(String)} and {@link DataStoreUpdateRequest#setValueLong(Long)} If you pass
     * in the user information, this function will return all the keys in a user's data store. If you leave the user
     * information empty, it will return all the keys in the game's global data store.
     */
    @Builder
    @Getter
    @Setter
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
         * The String value you'd like to apply to the data store item. You may only use the operations {@link
         * OperationType#ADD}, {@link OperationType#SUBTRACT}, {@link OperationType#MULTIPLY}, {@link
         * OperationType#DIVIDE} if you submit a numerical value.
         */
        private String value;

        /**
         * The url string defining this request. Note that it does not contain the base URL pointing to the Game Jolt
         * API.
         */
        @Override
        public String toString() {
            var builder = new StringBuilder();
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
         * @param jsonValue The JSON response from the server.
         * @return The {@link DataStoreUpdateValue} with the values returned from the server.
         */
        @Override
        public DataStoreUpdateValue handleResponse(JsonValue jsonValue) {
            return DataStoreUpdateValue.builder()
                .jsonValue(jsonValue)
                .success(jsonValue.getBoolean("success"))
                .message(jsonValue.getString("message", null))
                .data(jsonValue.getString("data", null))
                .build();
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

        private String name;

        OperationType(String name) {
            this.name = name;
        }
    }

    /**
     * The result of updating data in the data store.
     */
    @Builder
    @Getter
    @Setter
    public static class DataStoreUpdateValue implements GameJoltValue {

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
         * If the request was successful, this returns the new value of the data item.
         */
        public String data;
    }

    /**
     * Listener for {@link DataStoreUpdateRequest}. Override {@link DataStoreUpdateListener#dataStoreUpdate(
     *DataStoreUpdateValue)} to handle the server response.
     */
    public static abstract class DataStoreUpdateListener extends GameJoltAdapter {
        @Override
        public void response(GameJoltValue value) {
            if (value instanceof DataStoreUpdateValue) dataStoreUpdate((DataStoreUpdateValue) value);
        }

        public abstract void dataStoreUpdate(DataStoreUpdateValue value);
    }
}
