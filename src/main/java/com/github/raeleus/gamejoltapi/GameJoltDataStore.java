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
         * Handles the server JSON response and returns a corresponding {@link DataStoreFetchData}.
         * @param jsonValue The JSON response from the server.
         * @return The {@link DataStoreFetchData} with the values returned from the server.
         */
        @Override
        public DataStoreFetchData handleResponse(JsonValue jsonValue) {
            return DataStoreFetchData.builder()
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
    public static class DataStoreFetchData implements GameJoltData {
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
     * DataStoreFetchListener#dataStoreFetch(DataStoreFetchData)} (DataStoreFetchData)} to handle the server response.
     */
    public static abstract class DataStoreFetchListener extends GameJoltAdapter {
        @Override
        public void response(GameJoltData data) {
            if (data instanceof DataStoreFetchData) dataStoreFetch((DataStoreFetchData) data);
        }

        public abstract void dataStoreFetch(DataStoreFetchData data);
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
         * Handles the server JSON response and returns a corresponding {@link DataStoreGetKeysData}.
         * @param jsonValue The JSON response from the server.
         * @return The {@link DataStoreGetKeysData} with the values returned from the server.
         */
        @Override
        public DataStoreGetKeysData handleResponse(JsonValue jsonValue) {
            return DataStoreGetKeysData.builder()
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
    public static class DataStoreGetKeysData implements GameJoltData {

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
     * DataStoreGetKeysListener#dataStoreGetKeys(DataStoreGetKeysData)} (DataStoreGetKeysData)} to handle the server
     * response.
     */
    public static abstract class DataStoreGetKeysListener extends GameJoltAdapter {
        @Override
        public void response(GameJoltData data) {
            if (data instanceof DataStoreGetKeysData) dataStoreGetKeys((DataStoreGetKeysData) data);
        }

        public abstract void dataStoreGetKeys(DataStoreGetKeysData data);
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
         * Handles the server JSON response and returns a corresponding {@link DataStoreRemoveData}.
         * @param jsonValue The JSON response from the server.
         * @return The {@link DataStoreRemoveData} with the values returned from the server.
         */
        @Override
        public DataStoreRemoveData handleResponse(JsonValue jsonValue) {
            return DataStoreRemoveData.builder()
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
    public static class DataStoreRemoveData implements GameJoltData {

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
     * Listener for {@link DataStoreRemoveRequest}. Override {@link DataStoreRemoveListener#dataStoreRemove(DataStoreRemoveData)} to
     * handle the server response.
     */
    public static abstract class DataStoreRemoveListener extends GameJoltAdapter {
        @Override
        public void response(GameJoltData data) {
            if (data instanceof DataStoreRemoveData) dataStoreRemove((DataStoreRemoveData) data);
        }

        public abstract void dataStoreRemove(DataStoreRemoveData data);
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
         * Handles the server JSON response and returns a corresponding {@link DataStoreSetData}.
         * @param jsonValue The JSON response from the server.
         * @return The {@link DataStoreSetData} with the values returned from the server.
         */
        @Override
        public DataStoreSetData handleResponse(JsonValue jsonValue) {
            return DataStoreSetData.builder()
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
    public static class DataStoreSetData implements GameJoltData {
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
     * Listener for {@link DataStoreSetRequest}. Override {@link DataStoreSetListener#dataStoreSet(DataStoreSetData)} to
     * handle the server response.
     */
    public static abstract class DataStoreSetListener extends GameJoltAdapter {
        @Override
        public void response(GameJoltData data) {
            if (data instanceof DataStoreSetData) dataStoreSet((DataStoreSetData) data);
        }

        public abstract void dataStoreSet(DataStoreSetData data);
    }

    /**
     * Updates data in the data store. You can only perform mathematical operations on numerical data. See {@link
     * DataStoreUpdateRequest#setValueString(String)} and {@link DataStoreUpdateRequest#setValueLong(Long)} If you pass
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
         * The String value you'd like to apply to the data store item. Only compatible with the OperationTypes {@link
         * OperationType#APPEND} and {@link OperationType#PREPEND} You must provide either a valueString or valueLong.
         */
        private String valueString;

        /**
         *The long value you'd like to apply to the data store item. Only compatible with the OperationTypes {@link
         *OperationType#ADD}, {@link OperationType#SUBTRACT}, {@link OperationType#MULTIPLY}, and {@link
         *OperationType#DIVIDE} You must provide either a valueString or valueLong.
         */
        private Long valueLong;

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
            if (operation == OperationType.APPEND || operation == OperationType.PREPEND) builder.append("&value=").append(urlEncode(valueString));
            else builder.append("&value=").append(valueLong);

            return builder.toString();
        }

        /**
         * Handles the server JSON response and returns a corresponding {@link DataStoreUpdateData}.
         * @param jsonValue The JSON response from the server.
         * @return The {@link DataStoreUpdateData} with the values returned from the server.
         */
        @Override
        public DataStoreUpdateData handleResponse(JsonValue jsonValue) {
            return DataStoreUpdateData.builder()
                .jsonValue(jsonValue)
                .success(jsonValue.getBoolean("success"))
                .message(jsonValue.getString("message", null))
                .data(jsonValue.getString("data"))
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
    public static class DataStoreUpdateData implements GameJoltData {

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
     * DataStoreUpdateData)} to handle the server response.
     */
    public static abstract class DataStoreUpdateListener extends GameJoltAdapter {
        @Override
        public void response(GameJoltData data) {
            if (data instanceof DataStoreUpdateData) dataStoreUpdate((DataStoreUpdateData) data);
        }

        public abstract void dataStoreUpdate(DataStoreUpdateData data);
    }
}
